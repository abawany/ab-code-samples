package main

import (
	"log"
)

var recvFile = func(cmd *pktCmd) {
	log.Println("recvFile", cmd.tid.IP, cmd.tid.Port)
}
