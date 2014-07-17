package main

import (
	"fmt"
	"log"
	"net"
)

type tftpCmd uint16

const (
	cmdRRQ  tftpCmd = 1 << iota
	cmdWRQ  tftpCmd = 1 << iota
	cmdDATA tftpCmd = 1 << iota
	cmdACK  tftpCmd = 1 << iota
	cmdERR  tftpCmd = 1 << iota
)

type pktCmd struct {
	cmd      tftpCmd
	fileName string
	mode     string
	tid      *net.UDPAddr
}

type pktData struct {
	opCode   tftpCmd
	blockNum uint16
	data     []byte
}

func decodeCmd(buf []byte) (cmd *pktCmd, err error) {

	defer func() {
		if r := recover(); r != nil {
			log.Println("Error in decodeCmd ", r)
			err = fmt.Errorf("ERR: decodeCmd %v", r)
		}
	}()

	// quality check: is this a sane tftp command packet?
	if len(buf) < 6 { // 2 byte command, 1 byte + null byte, 1 byte + null byte
		panic("Message is not a valid tftp command")
	}

	cmd = new(pktCmd)

	(*cmd).cmd = tftpCmd(buf[0]<<8 + buf[1])

	var argsFoundCount = 0
	var i, j int
	for i = 2; i < len(buf); i++ {
		if buf[i] == 0 {
			argsFoundCount++
			break
		}
	}
	if argsFoundCount != 1 {
		panic("missing null terminator after filename")
	}

	cmd.fileName = string(buf[2:i])

	log.Printf("DEBUG: filename [%s] af [%d] i [%d] j [%d]\n", cmd.fileName, argsFoundCount, i, j)

	for j = i + 1; j < len(buf); j++ {
		if buf[j] == 0 {
			argsFoundCount++
			break
		}
	}

	log.Printf("DEBUG: filename [%s] af [%d] i [%d] j [%d]\n", cmd.fileName, argsFoundCount, i, j)

	if argsFoundCount != 2 {
		panic("missing null terminator after transfer mode")
	}

	cmd.mode = string(buf[i+1 : j])
	if cmd.mode != "octet" {
		panic(fmt.Sprintf("server only supports octet transfer mode, got: %s", cmd.mode))
	}

	return cmd, err
}
