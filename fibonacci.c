#include <stdio.h>
#include <stdlib.h>

long fibiter(long in) 
{
	long cnt=2, prev=0, rslt=1, tmp;
	if (in==0 || in==1) return in;
	
	for (; cnt <= in; ++cnt) {
		tmp=rslt+prev;
		prev=rslt;
		rslt=tmp;
	}

	return rslt;
}

long fibrecr(long in)
{
	if (in==0 || in==1) return in;
	return fibrecr(in-1)+fibrecr(in-2);
}

int main(int argc, char *argv[]) 
{	
	long in;
	if (argc!=2) {
		printf("usage: %s <value>\n", argv[0]);
		exit(-1);
	}
	
	// FIXME:
	in=atol(argv[1]);
	
	printf("iter: %u->%u\n", in, fibiter(in));
	printf("recr: %u->%u\n", in, fibrecr(in));
	return 0;
}