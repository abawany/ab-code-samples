#include <iostream>
using namespace std;

class LinkedList; // pre-declaration

class Node {
  friend class LinkedList;
  int value;
  Node* next;

public:
  Node() {next = 0;}
  void setValue(int iVal) { this->value=iVal; }
};

class LinkedList {
  Node* head;
  int iSize;

public:
  LinkedList() { head = 0; }

  // assumes that the head node is a blank node (this is to avoid boundary conditions related to replacing 
  // the head node in cases where newElement->value < head->value)
  // assumes that newElement is a valid heap-allocated instance (i.e. takes ownership 
  // of the passed-in Node instead of making a copy of it)
  bool addElement(Node *newElement)
  { 
    assert(newElement);
    
    if (head==0) {
      head=new Node();
      head->value=INT_MIN;
      head->next=newElement;
      head->next->next=0;
      iSize=1;
      return false;
    }
    else {
      Node *current=head;
      Node *prev=head;
      bool bInserted=false;
      while (current) {
        if (current->value < newElement->value) {
          prev=current;
          current=current->next;
        }
        else { // insertion point found (insert before)
          prev->next=newElement;
          newElement->next=current; 
          bInserted=true;
          break;
        } // found insertion point
      } // loop over existing nodes
      // handle case where no existing node in list is >= newElement
      if (!bInserted) {
        prev->next=newElement;
        newElement->next=0;
      }
      ++iSize;
      return true;
    }
    
  } // addElement

  bool deleteNthLargestElement(int N)
  {
    if (N>=iSize) return false;

    int i=0;
    Node *current=head ? head->next : 0;
    Node *prev=current;
    while (current) {
      if (i < N) {
        prev=current;
        current=current->next;
        ++i;
      }
      else {
        if (current->next==0)  {
          prev->next=0;
          delete current;
        }
        else {
          Node *deleted=current;
          prev->next=current->next;
          delete deleted;
        }
        --iSize;
        return true;
      }
    } // loop over nodes 
  }

  void printAll() 
  {
    Node *current= head ? head->next : 0;
    cout << "size " << iSize << " start ";
    while (current) {
      cout << current->value << " ";
      current=current->next;
    }
    cout << "end" << endl;
  }
}; 

int main(void)
{
  LinkedList lst;

  Node *tmp=new Node();
  tmp->setValue(4);
  lst.addElement(tmp);

  tmp=new Node();
  tmp->setValue(2);
  lst.addElement(tmp);

  tmp=new Node();
  tmp->setValue(2);
  lst.addElement(tmp);

  tmp=new Node();
  tmp->setValue(6);
  lst.addElement(tmp);

  lst.printAll();

  lst.deleteNthLargestElement(2);

  lst.printAll();
  
  return 0;
} 
