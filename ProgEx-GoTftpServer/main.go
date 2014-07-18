package main

import (
	"flag"
	"log"
	"net"
	"os"
	"os/signal"
	"strconv"
)

func listenForConnections(lsnPort int) {
	lsnAddr, err := net.ResolveUDPAddr("udp", ":"+strconv.Itoa(lsnPort))
	udpConn, err := net.ListenUDP("udp", lsnAddr)
	if err != nil {
		log.Fatalf("Unable to listen on port %d %v", lsnPort, err)
	}
	defer udpConn.Close()

	var buf []byte = make([]byte, 1024)
	// listen for commands
	for {
		_, dstUDPAddr, err := udpConn.ReadFromUDP(buf)
		if err != nil || dstUDPAddr == nil {
			log.Printf("Error while waiting for command %v %v\n", err, dstUDPAddr)
			continue // not fatal - try to read another command
		}

		// deserialize the buffer to the command struct
		cmd, err := decodeCmd(buf)
		if err != nil {
			log.Printf("Error decoding cmd %v\n", err)
			(*mapTftpCmdToProcess[cmdNUL])(&pktCmd{tid: dstUDPAddr})
			continue // not fatal - wait for another command
		}

		// add the client tid to the cmd struct
		cmd.tid = dstUDPAddr

		err = cmd.spinClientHandler()
		if err != nil {
			log.Printf("Unable to process cmd: %v", err)
		}
	}
}

func main() {
	var lsnPort *int = flag.Int("port", 69, "listen port (default 69)")
	flag.Parse()

	log.Printf("TFTP: Listening for commands on port %d - interrupt to exit", *lsnPort)

	// spin off the listener in a goroutine
	go listenForConnections(*lsnPort)

	// setup a signal handler for interrupt to exit gracefully
	var chanInt = make(chan os.Signal, 1)
	signal.Notify(chanInt, os.Interrupt, os.Kill)
	<-chanInt

	log.Println("Got interrupted - exiting")
}
