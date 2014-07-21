package main

import (
	"testing"
)

func TestFileStoreAndLoad(t *testing.T) {
	var fileBuf = []byte{0, 1, 2, 3, 4, 5, 6}

	err := writeFile("test1", fileBuf)
	if err != nil {
		t.Errorf("writeFile: Unexpected error writing file test1 [%v]\n", err)
	}

	readBuf, err := readFile("test1")
	if err != nil {
		t.Errorf("readFile: Unexpected error reading back file test1 [%v]\n", err)
	}

	if len(fileBuf) != len(readBuf) {
		t.Errorf("readFile: Expected file read size [%d] but got [%v]\n", len(fileBuf), len(readBuf))
	}

	for i := 0; i < len(fileBuf); i++ {
		if fileBuf[i] != readBuf[i] {
			t.Errorf("readFile: Unexpected file difference at offset %d: expected [%d] got [%d]\n",
				i, fileBuf[i], readBuf[i])
		}
	}
}

func TestFileLoadNotFound(t *testing.T) {
	buf, err := readFile("THIS-FILE-DOES-NOT-EXIST")
	if err == nil {
		t.Errorf("readFile: Got unexpected file [%v] - expected file to not exist\n", buf)
	}
}
