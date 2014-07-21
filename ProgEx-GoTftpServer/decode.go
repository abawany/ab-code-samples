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

func decodeAck(buf []byte) (blockNum uint16, err error) {
	defer func() {
		if r := recover(); r != nil {
			log.Println("ERR: decodeAck ", r)
			err = fmt.Errorf("ERR: %v", r)
		}
	}()

	// quality check: is this a sane tftp ack packet?
	if len(buf) < 4 { // 2 byte command, 2 byte blocknum
		panic(fmt.Sprintf("Message is not a valid tftp ack packet - size [%d]", len(buf)))
	}

	opCode := extractOpCode(buf)
	if opCode != cmdACK {
		panic(fmt.Sprintf("Unexpected non-ack packet received %v", opCode))
	}

	blockNum = uint16(buf[2]<<8 + buf[3])
	return blockNum, err
}

// decodeData decodes a TFTP data packet
func decodeData(bytesRead int, buf []byte) (pkt *pktData, err error) {
	defer func() {
		if r := recover(); r != nil {
			log.Println("ERR: decodeData ", r)
			err = fmt.Errorf("ERR: %v", r)
		}
	}()

	// quality check: is this a sane tftp data packet?
	if len(buf) < 4 { // 2 byte command, 2 byte blocknum, 0+ data bytes
		panic("Message is not a valid tftp data packet")
	}

	pkt = new(pktData)

	pkt.opCode = extractOpCode(buf)
	if pkt.opCode != cmdDATA {
		panic(fmt.Sprintf("Unexpected non-data packet received %v", pkt.opCode))
	}

	pkt.blockNum = uint16(buf[2]<<8 + buf[3])

	// copy the data bytes
	pkt.data = buf[4:bytesRead]

	return pkt, err
}

// decodeCmd decodes a TFTP command
func decodeCmd(buf []byte) (cmd *pktCmd, err error) {
	defer func() {
		if r := recover(); r != nil {
			log.Println("ERR decodeCmd: ", r)
			err = fmt.Errorf("ERR: %v", r)
		}
	}()

	// quality check: is this a sane tftp command packet?
	if len(buf) < 6 { // 2 byte command, 1 byte + null byte, 1 byte + null byte
		panic("Message is not a valid tftp command")
	}

	cmd = new(pktCmd)

	cmd.cmd = extractOpCode(buf)

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
		panic(fmt.Sprintf("server only supports octet transfer mode, got: %s", cmd.mode))
	}

	return cmd, err
}

func extractOpCode(buf []byte) (opCode tftpCmd) {
	opCode = tftpCmd(buf[0]<<8 + buf[1])
	return opCode
}
