// LinkedListSortedMerge.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <stdlib.h>
#include <assert.h>

struct node {
  int iData;
  node *Next;
};

void initNode(struct node *nd, struct node *nxt, int iData)
{
  assert(nd);
  nd->iData=iData;
  nd->Next=nxt;
} // initNode

// adds a node to the end of the list
bool addNode(struct node **head, int iData)
{
  assert(head);

  // empty list
  if (*head==NULL) {
    struct node *nd=(struct node*)malloc(sizeof(struct node));
    assert(nd);
    initNode(nd, NULL, iData);
    *head=nd;
  }
  else {
    struct node *iter=*head;
    while (iter) {
      if (iter->Next==NULL) {
        struct node *tmp=(struct node*)malloc(sizeof(struct node));
        initNode(tmp, NULL, iData);
        iter->Next=tmp;
        break;
      }
      iter=iter->Next;
    }
  }
  return true;
} // addNode

void printNodes(struct node *head)
{
  printf("list: ");
  if (head==NULL) { 
    printf("<empty>\n");
    return;
  }

  while (head) {
    printf("%d ", head->iData);
    head=head->Next;
  }
  printf("\n");
} // printNodes

// inout will contain the new list and in2 will be empty
// Challenges: 
// - changing sort order requires too many changes
// - assumes lists sorted
// - assumes lists are correct
void mergeSortedList(struct node **inout, struct node **in2)
{
  assert(inout && in2);
  bool blFirstNode=true;

  struct node *iter1=*inout;
  struct node *iter1prev;
  while (iter1 && *in2) {
    if (iter1->iData <= (*in2)->iData) {
      // make sure that the next node in list 1 is > the current node in in2
      if (iter1->Next && (iter1->Next->iData > (*in2)->iData)) {
        struct node *nd1=iter1->Next;
        struct node *nd2=*in2;
        *in2=(*in2)->Next;
        iter1->Next=nd2;
        nd2->Next=nd1;
      }
    }
    else { // 1st node in 2nd list > 1st node in 1st list
      if (blFirstNode) {
        struct node *nd2=*in2;
        *in2=(*in2)->Next; // new head for in2
        nd2->Next=*inout;
        *inout=nd2;
        iter1=*inout;
        continue;
      }
    }
    iter1prev=iter1;
    iter1=iter1->Next;
    blFirstNode=false;
  } // loop

  // make sure that nothing remains in in2
  if (*in2) {
    iter1prev->Next=*in2;
    *in2=NULL;
  }

} // mergeSortedList

int _tmain(int argc, _TCHAR* argv[])
{
  struct node *lst1=NULL, *lst2=NULL;
  
  // populate lst1
  addNode(&lst1, 1);
  addNode(&lst1, 5);
  addNode(&lst1, 10);
  
  // populate lst2
  addNode(&lst2, 0);
  addNode(&lst2, 0);
  addNode(&lst2, 1);
  addNode(&lst2, 2);
  addNode(&lst2, 3);
  addNode(&lst2, 6);
  addNode(&lst2, 9);
  addNode(&lst2, 10);
  addNode(&lst2, 12);

  mergeSortedList(&lst1, &lst2);

  printNodes(lst1);
  printNodes(lst2);

	return 0;
}

