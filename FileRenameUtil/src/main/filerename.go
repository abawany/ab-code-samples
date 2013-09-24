package main

import (
	"fmt"
	"os"
	"path/filepath"
)

func main() {
	if len(os.Args)!=3 {
		fmt.Printf("usage: %s <regex> <directory>", filepath.Base(os.Args[0]));
		os.Exit(1);
	}
}