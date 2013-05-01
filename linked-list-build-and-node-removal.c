#include <stdio.h>

typedef struct {
	int data;
	void *next;
} node;

void printList(node *head) 
{
	printf("HEAD->");
	while(head) {
		printf("%x->", head->data);
		head=head->next;
	}
	printf("NULL\n");
}

void buildList(node **head) 
{
	node *tmp;
	int k=0, i=0;
	*head=(node*)malloc(sizeof(node));
	tmp=*head;
	
	while (1) {
		tmp->data=i; 
		if (k++%2==0) i++;
		if (i<5) 
			tmp->next=(node*)malloc(sizeof(node));
		else {
			tmp->next=NULL;
			break;
		}
		tmp=tmp->next;
	}
}

int removeNode(node **head, int iVal)
{
	node *entry=*head;
	
	while (entry) {
		if (entry->data==iVal) {
			node *del=entry;
			node **pp=&entry;
			*pp=del->next;
			free(del);
		}
		if (entry) entry=entry->next;
	}
}

int removeNodeOld(node **head, int iVal) 
{
	node *iter=*head;
	node *prev=NULL;
	node *del=NULL;
	
	while (iter) {
		if (iter->data==iVal) {
			del=iter;
			if (iter==*head) {
				*head=iter->next;
				iter=*head;
			}
			else {
				prev->next=iter->next;
				iter=iter->next;
			}
			free(del);
		}
		else {
			prev=iter;
			iter=iter?iter->next:iter;
		}
	}
	
	return (del!=NULL)?1:0;
}

int main(void)
{
	int i=0;
	node *head=NULL;
	buildList(&head);
	printList(head);
	for (; i<9; ++i) {
		printf("rmv %d\n", i);
		removeNode(&head, i);
		printList(head);
	}
	buildList(&head);
	printList(head);
	for (; i>-1; --i) {
		printf("rmv %d\n", i);
		removeNode(&head, i);
		printList(head);
	}
	return 0;
}
