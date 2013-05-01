// DiscoverDuplicateInArray.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

void swap(int *a, int *b)
{
  if ((a==NULL)||(b==NULL)) return;
  if (*a==*b) return;
  *a=*a^*b;
  *b=*a^*b;
  *a=*a^*b;
}

// given an array containing x elements with one duplicate and the rest arranged randomly, find the duplicate element
int getDuplicateElement(int *iArr, size_t as)
{
  size_t i=0;
  int iRslt=-1;

  for (; i < as;) {
    if (iArr[i]!=i) {
      if (iArr[i]==iArr[iArr[i]]) { iRslt=iArr[i]; break; }
      swap(&iArr[i], &iArr[iArr[i]]);
    }
    else ++i;
  }

  return iRslt;
} // getDuplicateElement

#define ARRSZ 11
int _tmain(int argc, _TCHAR* argv[])
{
  int arr[ARRSZ]={6, 5, 4, 10, 2, 7, 9, 3, 1, 8, 8};
  int iRslt=-1;

  // pass to method
	iRslt=getDuplicateElement(arr, ARRSZ);

  printf("dup element: %d\n", iRslt);
  
  return 0;
}

