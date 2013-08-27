package b64

import (
	"testing"
)

func TestB64Encode(t *testing.T) {
	x := b64Encode("Man is distinguished, not only by his reason")
	if x != "e" {
		t.Errorf("B64Encode failed %s", x)
	}
}

func TestB64Decode(t *testing.T) {
	x := b64Decode("hello")
	if x != "d" {
		t.Errorf("B64Decode failed %s", x)
	}
}
