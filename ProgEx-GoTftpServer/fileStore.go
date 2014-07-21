package main

import (
	"fmt"
	"sync"
)

var fileStore map[string][]byte = make(map[string][]byte)
var storeMutex sync.RWMutex

func writeFile(fName string, file []byte) (err error) {
	storeMutex.Lock()
	defer storeMutex.Unlock()

	fileStore[fName] = file
	return err
}

func readFile(fName string) (file []byte, err error) {
	storeMutex.RLock()
	defer storeMutex.RUnlock()

	var ok bool
	if file, ok = fileStore[fName]; !ok {
		err = fmt.Errorf("file not found [%s]", fName)
	}

	return file, err
}
