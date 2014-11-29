package main

import (
	"flag"
	"fmt"
	"io/ioutil"
	"os"
	"regexp"
)

var verbosity int

func main() {
	var matchReStr = flag.String("matchRe", "", "regular expression for use with the rename operation (required)")
	var replStr = flag.String("replStr", "", "string with which to replace the matched part (optional, assumes blank)")
	var filePath = flag.String("startDir", "", "starting directory for rename operation traversal (required)")
	var dryRun = flag.Bool("dryRun", true, "if true, just show the results of the operation (optional, defaults to true)")
	flag.IntVar(&verbosity, "verbosity", 0, "verbosity level control (optional, defaults to 0)")
	flag.Parse()

	fmt.Println("1", *matchReStr, "2", *replStr, "3", *filePath, "4", *dryRun)
	if len(*matchReStr) == 0 || len(*filePath) == 0 {
		flag.PrintDefaults()
		return
	}

	matchRe, err := regexp.Compile(*matchReStr)
	if err != nil {
		panic("regexp.Compile:match " + err.Error())
	}

	readDirAndRename(matchRe, *replStr, *filePath, *dryRun)
}

func readDirAndRename(matchRe *regexp.Regexp, replStr string, filePath string, dryRun bool) {
	err := os.Chdir(filePath)
	if err != nil {
		panic("os.Chdir " + err.Error())
	}
	
	curDir, err := os.Getwd()
	if err != nil {
		panic("os.Getwd " + err.Error())	
	}
	
	dirInfo, err := ioutil.ReadDir(curDir)
	if err != nil {
		panic("ioutil.ReadDir " + err.Error())
	}

	var dirs []os.FileInfo = make([]os.FileInfo, 0) // worst-case length
	for i := 0; i < len(dirInfo); i++ {
		if dirInfo[i].IsDir() {
			dirs = append(dirs, dirInfo[i])
		} else {
			orgName := dirInfo[i].Name()
			newName := matchRe.ReplaceAll([]byte(orgName), []byte(replStr))
			if string(newName) == orgName {
				if verbosity > 0 {
					fmt.Println("skipping file [", orgName, "]")
				}
				continue
			}
			fmt.Println("ORIG [", orgName, "]\nNEW [", string(newName), "]")
			if dryRun {
				continue
			}
			err := os.Rename(orgName, string(newName))
			if err != nil {
				panic("os.Rename " + err.Error())
			}
		}
	}

	// traverse subsequent dirs recursively
	for i := 0; i < len(dirs); i++ {
		readDirAndRename(matchRe, replStr, dirs[i].Name(), dryRun)
		err := os.Chdir(curDir)
		if err != nil {
			panic("os.Chdir " + err.Error())
		}
	}
}
