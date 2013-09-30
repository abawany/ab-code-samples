#include <stdio.h>

int main(void)
{
  int cnt=0;

  for (cnt=0; cnt < 100; ++cnt) {
    printf("%d %08x %d %08x\n", cnt, cnt, ~cnt, ~cnt);
  }

  return 0;
}
