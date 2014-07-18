package main

import (
	"fmt"
	"log"
	"net"
)

type tftpCmd uint16

const (
	cmdNUL tftpCmd = iota
	cmdRRQ
	cmdWRQ
	cmdDATA
	cmdACK
	cmdERR
)

type pktCmd struct {
	cmd        tftpCmd
	fileName   string
	mode       string
	tid        *net.UDPAddr
	clientHdlr (*func(*pktCmd))
	errMsg     string // TODO: slightly cheesy - overloads this struct to handle special needs of nullHdlr
}

type pktData struct {
	opCode   tftpCmd
	blockNum uint16
	data     []byte
}

var mapTftpCmdToProcess map[tftpCmd](*func(*pktCmd)) = map[tftpCmd](*func(*pktCmd)){
	cmdRRQ: &sendFile, cmdWRQ: &recvFile, cmdNUL: &nullHdlr, cmdERR: &nullHdlr}

// returns a function that can used in a goroutine to interact with a connected client
func (cmd *pktCmd) spinClientHandler() (err error) {
	var ok bool
	var pFn *func(*pktCmd)

	if pFn, ok = mapTftpCmdToProcess[cmd.cmd]; !ok {
		err = fmt.Errorf("ERR: unknown cmd %v", cmd.cmd)
	}

	cmd.clientHdlr = pFn

	return err
}

// this method interacts with a client to receive or send a file. It will self terminate if the client
// doesn't interact with it for a minute
func (cmd *pktCmd) handleClientReq() {
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
		panic("ERR: missing null terminator after transfer mode")
	}

	cmd.mode = string(buf[i+1 : j])
	if cmd.mode != "octet" {
		panic(fmt.Sprintf("ERR: server only supports octet transfer mode, got: %s", cmd.mode))
	}

	return cmd, err
}
