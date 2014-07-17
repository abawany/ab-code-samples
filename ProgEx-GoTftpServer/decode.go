package main

import (
	"fmt"
	"log"
)

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

	(*cmd).fileName = string(buf[2:i])

	for j = i + 1; j < len(buf); j++ {
		if buf[j] == 0 {
			argsFoundCount++
			break
		}
	}
	if argsFoundCount != 2 {
		panic("missing null terminator after transfer mode")
	}

	cmd.mode = string(buf[i+1 : j])
	if cmd.mode != "octet" {
		panic("server only supports octet transfer mode")
	}

	return cmd, err
}
