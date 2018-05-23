package b64

import (
	"fmt"
)

const b64tbl string = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

func b64Encode(inVal string) string {
	var encd []uint8 = make([]uint8, len(inVal))
	for c := range inVal {
		fmt.Printf("(%d,%x) ", inVal[c], inVal[c])
		encd[c] = inVal[c]
	}

	var leftover uint8 = 0x00
	for i := 0; i < len(encd); i++ {
		var sixbit uint8 = 0xfc & encd[i]
		leftover = 0x3 & encd[i]
		fmt.Printf("%b %b\n", sixbit, leftover)
	}

	return "e"
}

func b64Decode(inVal string) string {
	return "d"
}
