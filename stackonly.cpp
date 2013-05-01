#include <iostream>
using namespace std;

class Y
{
public:
  auto Y() {}
  auto Y(const Y&) {}
  auto Y& operator=(const Y&) {}  

};

int main(void)
{
  return 0;
}
