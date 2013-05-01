#include <stdio.h>
#define X(y, z)  ((int)  & ( ((y*) 0)->z))

typedef struct {
  int z;
} aaa;

int main(void){
  aaa u;
  u.z=9;

  printf("%d\n", X(int, ));
  return 0;
}
