package main

import (
	"container/heap"
	"fmt"
	"math"
	"math/rand"
	"sort"
)

var rnd = rand.New(rand.NewSource(10))

func getNextInt() (val int) {
	return int(rnd.Float64()*(math.MaxInt32-math.MinInt32) + math.MinInt32)
}

// attribution: http://play.golang.org/p/Vz_mpb85uR
type IntHeap []int

func (h IntHeap) Len() int            { return len(h) }
func (h IntHeap) Less(i, j int) bool  { return h[i] < h[j] }
func (h IntHeap) Swap(i, j int)       { h[j], h[i] = h[i], h[j] }
func (h *IntHeap) Push(x interface{}) { *h = append(*h, x.(int)) }
func (h *IntHeap) Pop() interface{} {
	old := *h
	n := len(old)
	x := old[n-1]
	*h = old[0 : n-1]
	return x
}

func main() {
	h := &IntHeap{2, 1, 5}
	heap.Init(h)

	var samples = 20
	var heapSz = 10
	var rawData = make([]int, samples)
	for i := 0; i < samples; i++ {
		var val = getNextInt()
		
		rawData[i] = val
		heap.Push(h, val)
		if h.Len() > heapSz {
			heap.Pop(h)
		} 
	}

	sort.Ints(rawData)
	fmt.Println("raw data (sorted)", rawData, "\n")
	
	fmt.Println("results (sorted ascending)", *h)
}
