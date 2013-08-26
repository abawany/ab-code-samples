package main

import (
	"fmt"
	"b64"
)

func main() {
	fmt.Printf("Hello World %d", 12)
	fmt.Printf("e %s", b64.b64Decode())
}
