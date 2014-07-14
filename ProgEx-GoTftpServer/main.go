package main

import (
	"bytes"
	"encoding/gob"
	"encoding/hex"
	"flag"
	"log"
	"net"
	"os"
	"os/signal"
	"strconv"
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
}

type pktData struct {
	opCode   tftpCmd
	blockNum uint16
	data     []byte
}

func listenForConnections(lsnPort int) {
	lsnAddr, err := net.ResolveUDPAddr("udp", ":"+strconv.Itoa(lsnPort))
	udpConn, err := net.ListenUDP("udp", lsnAddr)
	if err != nil {
		log.Fatalf("Unable to listen on port %d %v", lsnPort, err)
	}
	defer udpConn.Close()

	var buf []byte = make([]byte, 0, 1024)
	// listen for commands
	for {
		sz, dstUDPAddr, err := udpConn.ReadFromUDP(buf)
		if err != nil || dstUDPAddr == nil {
			log.Printf("Error while waiting for command %v %v\n", err, dstUDPAddr)
			continue // not fatal - try to read another command
		}

		// deserialize the buffer to the command struct
		var cmd pktCmd
		var dec *gob.Decoder = gob.NewDecoder(bytes.NewBuffer(buf))
		err = dec.Decode(&cmd)
		if err != nil {
			log.Printf("Got garbled message from %s:%d size [%d] dump [%s] %v",
				dstUDPAddr.IP.String(), dstUDPAddr.Port, sz,
				hex.Dump(buf), err)
			continue // not fatal - try to read another command
		}
	}
}

func main() {
	var lsnPort *int = flag.Int("port", 69, "listen port (default 69)")
	flag.Parse()

	log.Printf("About to start listening for connection on port %d", *lsnPort)

	// spin off the listener in a goroutine
	go listenForConnections(*lsnPort)

	// setup a signal handler for interrupt to exit gracefully
	var chanInt = make(chan os.Signal, 1)
	signal.Notify(chanInt, os.Interrupt, os.Kill)
	<-chanInt

	log.Println("Got interrupted - exiting")
}
