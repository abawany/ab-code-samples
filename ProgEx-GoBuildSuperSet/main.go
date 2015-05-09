package main

import (
	"fmt"
	"math"
	"math/rand"
)

type Set map[int]bool
type Superset map[*Set]bool

func buildRandomSet(sz int) (set Set) {
	set = make(Set)

	for i := 0; i < sz; i++ {
		val := rand.Int() % 20

		if _, ok := set[val]; ok {
			i--
			continue
		}

		set[val] = true
	}

	return set
}

func naiveMakeSuperset(set Set) (superset Superset) {
	superset = make(Superset)

	for k, _ := range set {

		// to store new elements
		var tmpSuperset = make(Superset)

		for k1, _ := range superset {
			// copy each set and then append this element to it
			var cpySet = copySet(*k1)
			cpySet[k] = true
			tmpSuperset[&cpySet] = true
		}

		// copy tmp elements into result superset
		for k1, v := range tmpSuperset {
			superset[k1] = v
		}

		tmpSet := make(Set)
		tmpSet[k] = true
		superset[&tmpSet] = true
	}

	emptySet := make(Set)
	superset[&emptySet] = true

	return superset
}

func effectvMakeSuperset(set Set) (superset Superset) {
	superset = make(Superset)

	var setLen = len(set)
	var elemsInSuperset = int64(math.Pow(float64(setLen), 2.0))

	var setAsArr = make([]int, 0, setLen)
	for k, _ := range set {
		setAsArr = append(setAsArr, k)
	}

	var i int64
	for i = 0; i < elemsInSuperset; i++ {
		var tmpSet = make(Set)
		var j = i
		var c = 0
		for ; j > 0; j = j >> 1 {
			var addToSet = j & 0x1
			if addToSet == 1 {
				tmpSet[setAsArr[c]] = true
			}
			c++
		}
		superset[&tmpSet] = true
	}

	return superset
}

func copySet(set Set) (outSet Set) {
	outSet = make(Set)
	for k, v := range set {
		outSet[k] = v
	}

	return outSet
}

func printSuperset(superset Superset) {
	fmt.Println("* Superset", len(superset))
	for k, _ := range superset {
		fmt.Print(*k, " ")
		fmt.Println("")
	}
}

func main() {
	var randSet = buildRandomSet(4)
	fmt.Println("orig", randSet)

	var superset1 = naiveMakeSuperset(randSet)
	printSuperset(superset1)

	var superset2 = effectvMakeSuperset(randSet)
	printSuperset(superset2)
}
