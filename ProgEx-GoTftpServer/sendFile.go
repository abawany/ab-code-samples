package main

import (
	"log"
)

var sendFile = func(cmd *pktCmd) {
	log.Println("sendFile", cmd.tid.IP, cmd.tid.Port)
}
