package main

import (
	"flag"
	"fmt"
	"log"
	"net"
	"os"
	"os/signal"
	"strconv"
	"sync"
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
			log.Printf("ERR: Unable to decode cmd %v\n", err)
			go errHdlr(&pktCmd{tid: dstUDPAddr}, err.Error())
			continue // not fatal - wait for another command
		}

		// add the client tid to the cmd struct
		cmd.tid = dstUDPAddr

		// spin off a goroutine to interact with a connected client
		go func(cmd *pktCmd) {
			waitGroup.Add(1)
			defer waitGroup.Done()

			switch cmd.cmd {
			case cmdRRQ, cmdWRQ:
				cmd.clientConnectionHandler() // start interacting with client

			default:
				err = fmt.Errorf("ERR: unexpected cmd %v", cmd.cmd)
				log.Printf(err.Error())
				go errHdlr(cmd, err.Error())
			}
		}(cmd)
	}
}

var waitGroup sync.WaitGroup

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
	log.Println("Got interrupted - waiting for existing transfers to finish (up to 1 minute)")
	waitGroup.Wait()
	log.Println("All transfers finished - exiting")
}
