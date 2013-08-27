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

	for i :=0; i<len(encd);  {
		var triple int32 = (0 << 24) | (int32(encd[i]) << 16)
		triple |= ((i+1) < len(encd)) ? int32(encd(i) << 8) : (0 << 8)
	}

	return "e"
}

func b64Decode(inVal string) string {
	return "d"
}
