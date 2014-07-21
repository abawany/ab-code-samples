package main

import (
	"fmt"
	"log"
	"net"
	"time"
)

const _TIMEOUT time.Duration = 1 * time.Minute

// returns a function that can used in a goroutine to interact with a connected client
func (cmd *pktCmd) spinClientHandler() (err error) {

	switch cmd.cmd {
	case cmdRRQ, cmdWRQ:
		go cmd.clientConnectionHandler() // spin it off

	default:
		err = fmt.Errorf("ERR: unknown cmd %v", cmd.cmd)
	}

	return err
}

func (cmd *pktCmd) clientConnectionHandler() {
	const fName string = "clientConnectionHandler:"

	// build a connection to use for talking with this client
	udpConn, err := net.DialUDP("udp", nil, cmd.tid)
	if err != nil {
		log.Printf("ERR: %s Connection error [%v] for client [%v]\n", fName, err, cmd.tid)
		return
	}
	defer udpConn.Close()

	log.Printf("DEBUG: %s cmd [%v] file [%v]", fName, cmd.cmd, cmd.fileName)
	// mechanism to exit the function
	defer func() {
		if r := recover(); r != nil {
			log.Printf("ERR: %s err [%v] while exchanging data with [%v]", fName, r, cmd.tid)
		}
	}()

	switch cmd.cmd {
	case cmdWRQ:
		log.Printf("DEBUG: WRQ client [%v]", cmd.tid)
		cmd.wrqHdlr(udpConn)

	case cmdRRQ:
		log.Printf("DEBUG: RRQ client [%v]", cmd.tid)
		cmd.rrqHdlr(udpConn)

	default:
		go errHdlr(cmd, "unexpected command")
		panic(fmt.Sprintf("Unexpected packet [%v]", cmd.cmd))
	}
}

func (cmd *pktCmd) wrqHdlr(udpConn *net.UDPConn) {
	// handle communications with client
	var cliBuf []byte = make([]byte, 1024)

	ackPkt := buildAckPkt(0)
	_, err := udpConn.Write(ackPkt)
	if err != nil {
		panic(fmt.Sprintf("client [%v] first ACK error [%v]", cmd.tid, err))
	}
	log.Printf("ACK sent to [%v]", cmd.tid)

	var fileBuf []byte = make([]byte, 0, 512)
	var lastAckedPkt uint16 = 0
	var xferStarted bool = false

writeLoop:
	for {
		udpConn.SetReadDeadline(time.Now().Add(_TIMEOUT))
		// now read data bytes and ack requests
		bytesRead, err := udpConn.Read(cliBuf)
		if err != nil {
			panic(fmt.Sprintf("client [%v] first ACK error [%v]", cmd.tid, err))
		}
		log.Println("DEBUG: BR", bytesRead)

		dPkt, err := decodeData(bytesRead, cliBuf)
		if err != nil {
			go errHdlr(cmd, fmt.Sprintf("garbled packet %v", err))
			panic(fmt.Sprintf("client [%v] garbled packet err [%v]", cmd.tid, err))
		}

		// dPkt.blockNum = 0 // to introduce errors
		// deal with out of sequence or repeated packets
		log.Println("r-blknum", dPkt.blockNum, "l-blknum", lastAckedPkt)
		if xferStarted && lastAckedPkt+1 != dPkt.blockNum {
			log.Printf("Out of sequence packet: got %d expected %d", dPkt.blockNum, lastAckedPkt)
			continue
		}

		ackPkt = buildAckPkt(dPkt.blockNum)
		_, err = udpConn.Write(ackPkt)
		if err != nil {
			panic(fmt.Sprintf("client [%v] write ack error [%v]", cmd.tid, err))
		}
		xferStarted = true
		lastAckedPkt = dPkt.blockNum

		if len(dPkt.data) > 0 {
			fileBuf = append(fileBuf, dPkt.data...)
		}

		if len(dPkt.data) < 512 { // last packet: store file and exit
			log.Printf("Recv completed for file [%s] from client [%s] bytes [%d]",
				cmd.fileName, cmd.tid, len(fileBuf))
			writeFile(cmd.fileName, fileBuf)
			break writeLoop
		}
	}
}

func (cmd *pktCmd) rrqHdlr(udpConn *net.UDPConn) {
	// check if file exists
	fileBuf, err := readFile(cmd.fileName)
	if err != nil {
		errHdlr(cmd, fmt.Sprintf("File not found [%s]", cmd.fileName))
		panic(fmt.Sprintf("Client [%v] requested unknown file [%s]", cmd.tid, cmd.fileName))
	}

	// start sending data in 512-byte chunks
	var chunks = len(fileBuf) / 512
	var finalChunk = len(fileBuf) % 512
	var blockNum uint16 = 1
	var startOffset, endOffset = 0, 512
	var ackBuf []byte = make([]byte, 100)

	for i := 0; i < chunks+1; { // go one over the limit to handle the final unaligned chunk

		log.Println("A", i, chunks, len(fileBuf), finalChunk, startOffset, endOffset, endOffset-startOffset)

		if i == chunks {
			if finalChunk > 0 {
				endOffset = startOffset + finalChunk
			} else {
				endOffset = startOffset
			}
		}

		log.Println("B", startOffset, endOffset, endOffset-startOffset)

		pkt := buildDataPkt(blockNum, fileBuf[startOffset:endOffset])
		log.Println("len(dataPkt)", len(pkt), " d ", pkt)
		_, err = udpConn.Write(pkt)
		if err != nil {
			panic(fmt.Sprintf("Client [%v] error [%v] sending chunk [%d] of [%d]",
				cmd.tid, err, i, chunks))
		}

		log.Println("D")
		// wait (timed) for ack
		udpConn.SetReadDeadline(time.Now().Add(_TIMEOUT))
		_, err = udpConn.Read(ackBuf)
		if err != nil {
			panic(fmt.Sprintf("Client [%v] error [%] waiting for ack for chunk [%d]",
				cmd.tid, err, i))
		}

		recvdBlockNum, err := decodeAck(ackBuf)
		if err != nil {
			log.Printf("Client [%v] ACK packet error [%v]", err)
			continue // maybe an  errant client sent a bad packet - try to continue
		}

		if blockNum != recvdBlockNum {
			log.Printf("Client [%v] ACK problem: expected [%d], got [%d]\n", cmd.tid, blockNum, recvdBlockNum)
			continue
		}

		// ready to send next packet
		i++
		startOffset += 512
		endOffset += 512
		blockNum++
	}

}

var errHdlr = func(cmd *pktCmd, errMsg string) {
	log.Println("nullHdlr")
	udpConn, err := net.DialUDP("udp", nil, cmd.tid)
	if err != nil {
		log.Printf("ERR: unable to return error [%v] to client [%v]\n", err, cmd.tid)
		return
	}
	defer udpConn.Close()
	udpConn.SetWriteDeadline(time.Now().Add(1 * time.Minute)) // NOTE: ignores error

	rsp := buildErrPkt(errCodeUndefined, errMsg)
	_, err = udpConn.Write(rsp)
	if err != nil {
		log.Printf("ERR: errHdlr: unable to write error response to client [%] due to [%v]\n",
			cmd.tid, err)
	}
}
