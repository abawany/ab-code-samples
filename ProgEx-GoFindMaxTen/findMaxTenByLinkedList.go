package main

import (
	"fmt"
	"math"
	"math/rand"
	"sort"
	//"time"
)

var rnd = rand.New(rand.NewSource(10))

func getNextInt() (val int) {
	return int(rnd.Float64()*(math.MaxInt32-math.MinInt32) + math.MinInt32)
}

type node struct {
	val int
	nxt *node
}

var headNode *node
var listSize = 0
var LIST_MAX_SIZE = 10

// insert given value in list while maintaining descending order
// if list size grows > MAX_SIZE, lops one item off the end
func listInsert(val int) {
	var insNode = new(node)
	insNode.val = val

	if headNode == nil {
		headNode = insNode
		listSize++
	} else {
		var curNode *node = headNode
		var lstNode *node = nil
		var prvNode *node = nil
		var inserted bool
		
		for curNode != nil {
			prvNode = lstNode
			lstNode = curNode
			curNode = curNode.nxt
			
			
			if lstNode.val > val {
				if curNode == nil {
					lstNode.nxt = insNode
					inserted = true
				}
			} else if prvNode != nil {
				prvNode.nxt = insNode
				insNode.nxt = lstNode
				inserted = true
			} else if prvNode == nil {
				headNode = insNode
				insNode.nxt = lstNode
				inserted = true
			}

			if inserted {
				listSize++
				break
			}
		}
	}

	// housekeeping: trim the list and re-initialize the min and max values so far
	if listSize > LIST_MAX_SIZE {
		var curNode *node = headNode

		for curNode != nil {
			if curNode.nxt.nxt == nil {
				curNode.nxt = nil
				listSize--
			}
			curNode = curNode.nxt
		}

	}
}

func listPrint() {
	fmt.Print("listPrint size ", listSize, " [")
	var curNode = headNode
	for curNode != nil {
		fmt.Printf("%d ", curNode.val)
		curNode = curNode.nxt
	}
	fmt.Println("]")
}

func main() {
	var samples = 100 
	var rawData = make([]int, samples)

	for i := 0; i < samples; i++ {
		var val = getNextInt()
		rawData[i] = val
		listInsert(val)
	}

	sort.Ints(rawData)
	fmt.Println("rawdata", rawData, "\n")
	listPrint()
}
