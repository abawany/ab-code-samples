TFTP Server in Go
=================

This is a simple tftp server written in Golang - currently a work-in-progress.  The server was tested with Go 1.2.x and 1.3.x. 

To build, run `go build` in the source directory. 

To run, type `./ProgEx-GoTftpServer` after completing the build process.

To run tests, type `go test` in the source directory.

Features:
---------
* Uses goroutines to allow simultaneous file transfers.
* Pressing ctrl+c will exit the server as long as no outstanding transfers are pending. 
* The server listens for connections on the default port 69 but can be configured to listen on other ports by invoking it with the "-port" flag.

Known Issues:
-------------
* Uses in-memory storage for files, which means that if the service is restarted, all files stored so far are lost
* To simplify build, this implementation uses no external packages and no sub-packages. This leads to a bit of code clutter and lack of separation of concerns.
* Pressing ctrl+c to exit can result in a wait of up to 1 minute while the server waits for all current active or idle/timed-out connections to finish. If a long transfer is active, the server will currently wait for it to finish or time out before exiting.
* Integration test (integration_test.go) assumes a Un*x-ish OS with access to /tmp and assumes the existence of a tftp client and checksum utility in the system path. This test will fail if these conditions are not true.
 
Application Structure:
----------------------
* Entry point is main.go/main. It spins off a go routine that listens for connections on specified port and it registers a signal handler to listen for interrupts (ctrl+c).
* The go routine above (main.go/listenForConnections) listens for UDP connections on specified port. When it gets one, it first ensures that it received a legal TFTP packet and that the TFTP command in question is a read (RRQ) or write (WRQ).
* If the request is legitimate, it spins off an instance of clientConnectionHandler (clientHandlers.go) to interact with the client further, else it returns an error packet to the client and goes back to waiting for new connections.
* clientConnectionHandler determines whether the command received is of type WRQ (write) or RRQ(read). For the former, it invokes clientHandler.go/wrqHdlr else it invokes clientHandler.go/rrqHdlr.
* clientHandlers.go/wrqHdlr: sends an ACK packet to the client with block number = 0 and then listens for DATA packets from the client. For each such packet received, it ensures that the block number is one higher than the last block number and then appends the received data block to its in-memory file buffer. When the transfer is complete (i.e. a data packet of size < 512 bytes is received), it stores the file in an in-memory map (fileStore.go) and closes the connection.
* clientHandlers.go/rrqHdlr: sends the first DATA packet for the file requested and waits for the ACK. Sends subsequent packets of size <= 512 bytes until the file is transferred over to the client.
