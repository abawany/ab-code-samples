package main

import (
	"os"
	"os/exec"
	"testing"
)

func TestIntegration(t *testing.T) {
	const tstName = "TestIntegration: "
	var err error

	var fileList []string = []string{"out1", "out1023", "out1024", "out1025", "out511", "out512"}

	// copy off the files to /tmp
	for _, f := range fileList {
		cmdCp := exec.Command("cp", "testFiles/"+f, "/tmp")
		outCp, err := cmdCp.CombinedOutput()
		if err != nil {
			t.Fatalf("%s copying test files to tmp [%v] [%v] failed [%v] [%v]\n",
				tstName, cmdCp.Args, cmdCp.Dir, err, string(outCp))
		}
	}

	// spin off the tftp server
	cmdBld := exec.Command("go", "build")
	outBld, err := cmdBld.CombinedOutput()
	if err != nil {
		t.Fatalf("%s unable to build this project [%v] [%v]\n", tstName, err, outBld)
	}

	cmdTftpSvr := exec.Command("./ProgEx-GoTftpServer", "-port", "8099")
	err = cmdTftpSvr.Start()
	if err != nil {
		t.Fatalf("%s unable to spawn the tftp server [%v]\n", tstName, err)
	}

	// spawn off tftp and send it commands via stdin
	cmdTftpCli := exec.Command("tftp")
	cmdTftpCli.Dir = "/tmp" // change process working directory

	// write the out* files in /tmp to the tftp server and then read them back
	stdinTftpCli, err := cmdTftpCli.StdinPipe()
	if err != nil {
		t.Fatalf("%s unable to get the tftp client stdin [%v]\n", tstName, err)
	}

	err = cmdTftpCli.Start()
	if err != nil {
		t.Fatalf("%s unable to spawn the tftp client [%v]\n", tstName, err)
	}

	var tftpCliCmds []string = []string{"connect localhost 8099\n", "put out1\n",
		"put out1023\n", "put out1024\n", "put out1025\n", "put out511\n", "put out512\n",
		"get out1\n", "get out1023\n", "get out1024\n", "get out1025\n", "get out511\n",
		"get out512\n", "quit\n"}

	for _, cmd := range tftpCliCmds {
		_, err = stdinTftpCli.Write([]byte(cmd))
		if err != nil {
			t.Fatalf("%s failed to send tftp cmd [%v], error [%v]\n", tstName, cmd, err)
		}
	}

	// byte-compare the test files with the files sent/recvd from tftp
	for _, f := range fileList {
		var srcPath = "./testFiles/" + f
		srcFile, err := os.Open(srcPath)
		if err != nil {
			t.Fatalf("%s failed to open test file [%s] err [%v]\n", tstName, srcPath, err)
		}

		var srcBuf []byte = make([]byte, 0, 4096)
		_, err = srcFile.Read(srcBuf)
		if err != nil {
			t.Fatalf("%s unable to read file [%s] err [%v]\n", tstName, srcPath, err)
		}
		srcFile.Close()

		var dstPath = "/tmp/" + f
		dstFile, err := os.Open(dstPath)
		if err != nil {
			t.Fatalf("%s failed to open test file [%s] err [%v]\n", tstName, dstPath, err)
		}

		var dstBuf []byte = make([]byte, 0, 4096)
		_, err = dstFile.Read(srcBuf)
		if err != nil {
			t.Fatalf("%s unable to read file [%s] err [%v]\n", tstName, dstPath, err)
		}
		dstFile.Close()

		if len(dstBuf) != len(srcBuf) {
			t.Fatalf("%s files %s and %s are of different lengths %d, %d\n",
				tstName, srcPath, dstPath, len(srcBuf), len(dstBuf))
		}

		for i := 0; i < len(srcBuf); i++ {
			if srcBuf[i] != dstBuf[i] {
				t.Logf("\nsrc: [%v]\ndst: [%v]\n", srcBuf, dstBuf)
				t.Errorf("%s byte mismatch at offset %d, exp %d got %d\n",
					tstName, i, srcBuf[i], dstBuf[i])
			}
		}
	}

	t.Logf("%s Now trying to kill running tftp server\n", tstName)
	err = cmdTftpSvr.Process.Signal(os.Interrupt)
	if err != nil {
		t.Errorf("%s unable to kill tftp server [%v]\n", tstName, err)
	}

	t.Logf("%s completed\n", tstName)
}
