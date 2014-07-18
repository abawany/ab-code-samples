package main

import (
	"log"
)

var recvFile = func(cmd *pktCmd) {
	log.Println("recvFile", cmd.tid.IP, cmd.tid.Port)
}

var sendFile = func(cmd *pktCmd) {
	log.Println("sendFile", cmd.tid.IP, cmd.tid.Port)
}

var nullHdlr = func(cmd *pktCmd) {
	log.Println("nullHdlr")
}
