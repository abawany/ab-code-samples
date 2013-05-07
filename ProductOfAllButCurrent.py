
# This function would not handle integer overflow that can result from multiplying
# all of the elements in the passed-in list though it appears that Python doesn't
# suffer from this limitation for integers
def getProductOfAllButCurrentIndex(intList):
	product=1
	for val in intList:
		product *=val
	
	outList=[]
	for val in intList:
		if val==0:
			outList.append('NaN')
		else:
			outList.append(product/val)
	
	return outList

if __name__ == "__main__":
	inList=[-1,-2,1,2,3,4,9999999999999999999999999999999999999999999999999999999]
	rsList=getProductOfAllButCurrentIndex(inList)
	
	for val in inList:
		print val, 
	print
	
	for val in rsList:
		print val,
	print