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

var mapTftpCmdToProcess map[tftpCmd](*func(*pktCmd)) = map[tftpCmd](*func(*pktCmd)){
	cmdRRQ: &sendFile, cmdWRQ: &recvFile}

// acts as a factory to spin off goroutines that work with a connected client
func cmdFactory(cmd *pktCmd) (pFn *func(*pktCmd), err error) {
	var ok bool
	if pFn, ok = mapTftpCmdToProcess[cmd.cmd]; !ok { 
		err = fmt.Errorf("ERR: unknown cmd %v", cmd.cmd)
	}

	return pFn, err
}

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
			continue // not fatal - wait for another command
		}

		// add the client tid to the cmd struct
		cmd.tid = dstUDPAddr

		fnProcess, err := cmdFactory(cmd)
		if err != nil {
			log.Printf("Unable to process cmd: %v", err)
		}
		go (*fnProcess)(cmd)
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
