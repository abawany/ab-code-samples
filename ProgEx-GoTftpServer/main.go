package main

import (
	"flag"
	"fmt"
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
		log.Fatalf("ERR: Unable to listen on port %d %v", lsnPort, err)
	}
	defer udpConn.Close()

	var buf []byte = make([]byte, 1024)
	// listen for commands
	for {
		_, dstUDPAddr, err := udpConn.ReadFromUDP(buf)
		if err != nil || dstUDPAddr == nil {
			log.Printf("ERR: [%v] while waiting for command [%v]\n", err, dstUDPAddr)
			continue // not fatal - try to read another command
		}

		// deserialize the buffer to the command struct
		cmd, err := decodeCmd(buf)
		if err != nil {
			errMsg := fmt.Sprintf("ERR: Unable to decode cmd %v\n", err)
			log.Print(errMsg)
			go (*mapTftpCmdToProcess[cmdERR])(&pktCmd{tid: dstUDPAddr, errMsg: errMsg})
			continue // not fatal - wait for another command
		}

		// add the client tid to the cmd struct
		cmd.tid = dstUDPAddr

		err = cmd.spinClientHandler()
		if err != nil {
			errMsg := fmt.Sprintf("ERR: Unable to process cmd: %v", err)
			log.Print(errMsg)
			go (*mapTftpCmdToProcess[cmdERR])(&pktCmd{tid: dstUDPAddr, errMsg: errMsg})
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
