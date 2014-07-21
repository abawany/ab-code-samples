package main

import (
	"testing"
)

func TestBuildErrPkt(t *testing.T) {
	var expectedBuf = []byte{0, 5, 0, 12, 65, 0}
	uutBuf := buildErrPkt(12, "A")

	lenChk(t, "TestBuildErrPkt", expectedBuf, uutBuf)
	byteWiseCmp(t, "TestBuildErrPkt", expectedBuf, uutBuf)
}

func TestBuildAckPkt(t *testing.T) {
	var expectedBuf = []byte{0, 4, 0, 1}
	uutBuf := buildAckPkt(1)

	lenChk(t, "TestBuildAckPkt", expectedBuf, uutBuf)
	byteWiseCmp(t, "TestBuildAckPkt", expectedBuf, uutBuf)
}

func TestBuildDataPkt(t *testing.T) {
	var expectedBuf = []byte{0, 3, 0, 9, 65, 66, 67, 68, 69}
	uutBuf := buildDataPkt(9, []byte{'A', 'B', 'C', 'D', 'E'})

	t.Logf("\nEXP: %v\nRCV: %v\n", expectedBuf, uutBuf)

	lenChk(t, "TestBuildDataPkt", expectedBuf, uutBuf)
	byteWiseCmp(t, "TestBuildDataPkt", expectedBuf, uutBuf)
}

func lenChk(t *testing.T, testId string, expBuf []byte, uutBuf []byte) {
	if len(uutBuf) != len(expBuf) {
		t.Errorf("%s: packet buffer size difference: expected %d, got %d\n",
			testId, len(expBuf), len(uutBuf))
	}
}

func byteWiseCmp(t *testing.T, testId string, expBuf []byte, uutBuf []byte) {
	for i := 0; i < len(expBuf); i++ {
		if uutBuf[i] != expBuf[i] {
			t.Errorf("%s: Offset %d, expected [%d] got [%d]", testId, i, expBuf[i], uutBuf[i])
		}
	}
}
