// Tree.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <stdio.h>
#include <stdlib.h>
#include <map>
#include <list>
using namespace std;

/*
  4
  /\
 2  6
/\  /\
1 3 5 7

4
26
1357
*/

typedef struct treenode
{
  int data;
  struct treenode *left;
  struct treenode *right;
} tn;

// given a value, inserts it in the right location of a bin tree
void insertintree(tn **head, int data)
{
  if (*head==NULL) {
    *head=(tn*)malloc(sizeof(tn));
    (*head)->left=NULL;
    (*head)->right=NULL;
    (*head)->data=data;
  }
  else {
    if ((*head)->data <= data) {
      insertintree(&((*head)->right), data);
    }
    else {
      insertintree(&((*head)->left), data);
    }
  }
} // insertintree

void buildtree (tn **head)
{  
  insertintree(head, 4);
  insertintree(head, 2);
  insertintree(head, 3);
  insertintree(head, 1);
  insertintree(head, 6);
  insertintree(head, 7);
  insertintree(head, 5);
} // buildtree

map<int, list<int> > printtree(tn *head)
{
  static int level=0;
  static map <int, list<int> > stor;
  
  if (level==0) stor.clear();
  if (!head) { return stor; }
  stor[level].push_back(head->data);
  // debug: printf("%d %d\n", level, head->data);
  ++level;
  printtree(head->left);
  printtree(head->right);
  --level;
  return stor;
} // printtree

int gettreedepth(tn *head)
{
  int iDepth=1;
  if (head->left) iDepth+=gettreedepth(head->left);
  else return iDepth;
}

int _tmain(int argc, _TCHAR* argv[])
{
  tn *head=NULL;

  buildtree(&head);
  
  map<int, list<int> > stor=printtree(head);

  int iCurLevel=0;
  int iDepth=gettreedepth(head);

  for (map<int, list<int> >::iterator x=stor.begin(); x!=stor.end(); ++x) {
    list<int> &lst=x->second;
    // insert (depth-currentlevel) spaces
    for (int i=0; i < (iDepth-iCurLevel); ++i) printf(" ");
    for (list<int>::iterator y=lst.begin(); y!=lst.end(); ++y) {
      printf("%d", *y);
    }
    printf("\n");
    ++iCurLevel;
  }

  return 0;
}

