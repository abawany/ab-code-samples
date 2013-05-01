#include <stdio.h>
#include <stdlib.h>

int main(void)
{
  char *x="xyz";
  char y[3]="ab";
  char z[4]="abcd";
  int a[4];
  char *w;

  w=(char*)malloc(20);

  printf("%u %u %u %u %u\n", sizeof(x), sizeof(y), sizeof(z), sizeof(a), sizeof(w));

  free(w);
  return 0;
}
