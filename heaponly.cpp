#include <iostream>
using namespace std;

class X
{
private:
  ~X() {}

public:
  X() { cout << "constructed" << endl; }
  static void destroy(X *inst) { cout << "destroyed" << endl; delete inst; }
  void callme() { cout << "called" << endl; }
}; 

int main(void)
{
  // X a; // uncommenting this line causes a compile-time error as expected
  X *b=new X();

  //a.callme();
  b->callme();

  X::destroy(b);
  
  cout << "end" << endl;

  return 0;
}
