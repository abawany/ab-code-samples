def fibRecur(val):
	if (val==0 or val==1):
		return val
	return fibRecur(val-1)+fibRecur(val-2)

def fibIter(val):
	if (val==0 or val==1):
		return val
		
	prev=0
	rslt=1
	count=2
	while (count <= val):
		tmp=rslt+prev
		prev=rslt
		rslt=tmp
		count+=1
		
	return rslt
	
def main():
	print "Enter value> ",
	val=int(raw_input())
	print 'recur: ' , val , '->' , fibIter(val)
	print 'recur: ' , val , '->' , fibRecur(val)

main()