package main

import (
	"fmt"
	"log"
	"net/http"
)

var assetTracker = make(map[string]interface{})

func Crawler(baseUrl string) (rslt *Page, err error) {
	defer func() {
		if r := recover(); r != nil {
			log.Println("Crawler.recover", r)
			err = fmt.Errorf("Crawler.ERR: %v", r)
		}
	}()

	rsp, err := http.Get(baseUrl)
	if err != nil {
		panic(err)
	}
	defer rsp.Body.Close()

	var pg *Page = NewPage(baseUrl)
	PageParser(rsp.Body, pg)

	return nil, nil
}
