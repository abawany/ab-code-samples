package main

const (
	errCodeUndefined uint16 = iota
	errCodeFileNotFound
	errCodeAccessViolation
	errCodeAllocExceeded
	errCodeIllegalOp
	errCodeUnknownXferId
	errCodeFileAlreadyExists
	errCodeNoSuchUser
)

func buildErrPkt(errCode uint16, errMsg string) (msg []byte) {
	msg = make([]byte, 1024)

	// add the command bytes
	msg[0] = byte(0)
	msg[1] = byte(5)

	// add the error code
	msg[2] = byte(errCode << 8)
	msg[3] = byte(errCode & 0x00ff)

	// add the error message
	offset := copy(msg[4:], []byte(errMsg))
	msg[4+offset] = 0 // null terminate string

	return msg
}

func buildAckPkt(blockNum uint16) (msg []byte) {
	msg = make([]byte, 1024)

	// add the command bytes
	msg[0] = byte(0)
	msg[1] = byte(4)

	// add block number
	msg[2] = byte(blockNum << 8)
	msg[3] = byte(blockNum & 0x00ff)

	return msg
}

func buildDataPkt(blockNum uint16, data []byte) (msg []byte) {
	msg = make([]byte, 1024)

	// add command bytes
	msg[0] = byte(0)
	msg[1] = byte(3)

	// add block number
	msg[2] = byte(blockNum << 8)
	msg[3] = byte(blockNum & 0x00ff)

	copy(msg[4:], data)

	return msg
}
