package main

import (
	"log"
	"net"
)

var recvFile = func(cmd *pktCmd) {
	log.Println("recvFile", cmd.tid.IP, cmd.tid.Port)
}

var sendFile = func(cmd *pktCmd) {
	log.Println("sendFile", cmd.tid.IP, cmd.tid.Port)
}

var nullHdlr = func(cmd *pktCmd) {
	log.Println("nullHdlr")
	udpConn, err := net.DialUDP("udp", nil, cmd.tid)
	if err != nil {
		log.Printf("unable to return error [%v] to client [%v]\n", err, cmd.tid)
		return
	}

	var errMsgStr string = "unknown error"
	if len(cmd.errMsg) > 0 { // TODO: only looks at the first message string for now
		errMsgStr = cmd.errMsg
	}

	rsp := buildErrPkt(errCodeUndefined, errMsgStr)
	_, err = udpConn.Write(rsp)
	if err != nil {
		log.Printf("unable to write error response to client [%] due to [%v]\n", cmd.tid, err)
	}
}
