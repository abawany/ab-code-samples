#include <stdio.h> 
#include <stdlib.h>

struct linknode {
  void *data;
  struct linknode *next;
};

struct linknode *head=NULL;

void addtoend(struct linknode *val)
{
  struct linknode *x=head;

  if (head==NULL) { head=val; return; }
  
  // advance to end
  while (x->next!=NULL) {
    x=x->next;
  }
  
  x->next=val;
  val->next=NULL;
} // addtoend

void printlist(struct linknode *start)
{
  struct linknode *x=start;
  while (x!=NULL) {
    printf("val: %x\n", x->data);    
    x=x->next;
  }
} // printlist

struct linknode* reverselist(struct linknode *start)
{
  struct linknode *cur, *next;
  struct linknode *rtn=NULL;

  cur=start;

  while (cur!=NULL) {
    next=cur->next;
    cur->next=rtn;
    rtn=cur;
    cur=next;    
  }
  
  return rtn;
} // reverselist

int main(void)
{
  struct linknode *tmp;
  int i=0;

  // populate ll
  for (i=0; i<10; ++i) {
    tmp=malloc(sizeof(struct linknode));
    tmp->data=0xcafebab0+(void*)i; // pointer=counter == DONT DEREF!
    addtoend(tmp);
  }
  
  printf("orig\n");
  printlist(head);

  head=reverselist(head);

  printf("\nrev\n");
  printlist(head);

  head=reverselist(head);  

  printf("\norig2\n");
  printlist(head);

  return 0;
} // main
