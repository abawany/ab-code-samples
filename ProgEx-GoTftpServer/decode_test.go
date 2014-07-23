package main

import (
	"testing"
)

func TestDecodeAck(t *testing.T) {
	var uutBuf []byte = []byte{0, 4, 0, 9}

	blockNum, err := decodeAck(uutBuf)
	if err != nil {
		t.Fatalf("Error [%v] decoding ACK\n", err)
	}

	if blockNum != 9 {
		t.Fatalf("Unexpected block number: expected [%d] got [%d]\n", 9, blockNum)
	}

	// expect failure when asked to decode a non-ACK packet
	uutBuf = []byte{0, 3, 0, 0}
	blockNum, err = decodeAck(uutBuf)
	if err == nil {
		t.Fatalf("Expected failure trying to decode a non-ACK block")
	}
}

func TestDecodeData(t *testing.T) {
	var uutBuf []byte = []byte{0, 3, 0, 1, 0xc, 0xa, 0xf, 0xe}

	data, err := decodeData(7, uutBuf)
	if err != nil {
		t.Fatalf("Error [%v] decoding DATA\n", err)
	}

	if data.blockNum != 1 {
		t.Fatalf("Unexpected block number: expected [%d] got [%d]\n", 1, data.blockNum)
	}

	for i := 0; i < len(data.data); i++ {
		if data.data[i] != uutBuf[i+4] {
			t.Errorf("\nExp buf [%v]\nGot buf [%v]\n")
			t.Fatalf("Unexpected difference in data buffers at offset [%d]\n", i)
		}
	}

	// expect failure when asked to decode a non-DATA packet
	uutBuf = []byte{0, 4, 0, 0}
	data, err = decodeData(4, uutBuf)
	if err == nil {
		t.Fatalf("Expected failure trying to decode a non-ACK block")
	}

}
