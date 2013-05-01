#include <iostream>
#include <algorithm>
#include <functional>
#include <list>
using namespace std;

typedef enum {sortorder_asc, sortorder_desc} sortorder;

template <typename T>
void listsort(const list<T> &src, list<T> &dst, sortorder so)
{
  dst=src;
  dst.sort();

  // flip the list if the sort was to be descending
  if(so==sortorder_desc) {
    list<T> tmp;
    typename list<T>::iterator end=dst.end();
    end--;
    for (; end!=dst.begin(); --end) {
      tmp.push_back(*end);
    }
    dst=tmp;
  }
} // listsort

// go through the list and make sure that the prev element is <comparison> or equal to
// the current element
template <typename T>
bool sorttest(const list<T> &src, sortorder so)
{
  typename list<T>::const_iterator liprev=src.begin();
  typename list<T>::const_iterator licur=liprev;
  licur++;
  
  while (licur!=src.end()) {
    switch(so)
    {
    case sortorder_asc:
      if (*licur < *liprev) return false;      
      break;

    case sortorder_desc:
      if (*licur > *liprev) return false;
      break;
    } // switch
    ++liprev; 
    ++licur;
  } // for
  
  return true;
} // sorttest

int main(void)
{
  list<int> a, b;

  // add 20 random numbers to the list
  for (int i=0; i < 20; ++i) 
    a.push_back(rand()%10);

  cout << "original " << endl;
  for (list<int>::iterator x=a.begin(); x!=a.end(); ++x)
    cout << *x << endl;

  listsort<int>(a, b, sortorder_asc);
  cout << "new ascending" << endl;
  for (list<int>::iterator x=b.begin(); x!=b.end(); ++x)
    cout << *x << endl;
  if (sorttest<int>(b, sortorder_asc)) cout << "ascending test passed" << endl;
  else cout << "ascending test failed" << endl;

  listsort<int>(a, b, sortorder_desc);
  cout << "new descending" << endl;
  for (list<int>::iterator x=b.begin(); x!=b.end(); ++x)
    cout << *x << endl;
  if (sorttest<int>(b, sortorder_desc)) cout << "descending test passed" << endl;
  else cout << "descending test failed" << endl;

  return 0;
}
