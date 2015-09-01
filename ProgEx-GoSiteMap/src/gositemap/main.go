package main

import (
	"flag"
	"fmt"
	"log"
)

func main() {
	var baseUrl = flag.String("baseUrl", "", "base url from which to start the site crawling")
	var printHelp = flag.Bool("help", false, "print help")
	flag.Parse()

	if len(*baseUrl) == 0 || *printHelp {
		flag.PrintDefaults()
		return
	}

	fmt.Println("Starting with...", *baseUrl)
	rootPage, err := Crawler(*baseUrl)
	if err != nil {
		log.Fatalln("Unable to crawl: ", *baseUrl, " Err: ", err)
	}
	fmt.Println(rootPage)
}
