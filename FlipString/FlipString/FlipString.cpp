// FlipString.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// modified in-place in linear time (but clobbers original)
int flipstring3(char *sIn) // pass the string 
{ 
  char *t=sIn; 
  while(*t) { 
    if(*t==' ') 
      *t++='\0'; 
    else 
      ++t; 
  }

  --t; 

  while(t>=sIn) {
    if(*t!='\0') 
      --t; 
    else 
      printf("%s ",(t-- + 1)); 
  }
  if (t<sIn) printf("%s\n", (++t));

  //printf("%s",myStr); 
  return 1;
} 

// Modifies in-place
int flipstring2(char *sIn)
{
  char *sStart, *sEnd, *sInStrt, *sInEnd;
  int iStrLen=0;
  int iStatus=0;
  char cTmp;

  if (!sIn) return iStatus;
  
  iStrLen=strlen(sIn)-1;
  sStart=sIn;
  sEnd=sIn+iStrLen;

  // flip the entire string, char by char
  while (sStart <= sEnd) {
    cTmp=*sStart;
    *sStart=*sEnd;
    *sEnd=cTmp;
    ++sStart; 
    --sEnd;
  }

  // now flip each word in the string
  sStart=sIn;
  sEnd=sIn;
  while (1) {
    if ( (*sEnd==' ') || (*sEnd=='\t') || (*sEnd==0x0) ) {
      sInEnd=sEnd-1;
      sInStrt=sStart;
      while (sInStrt <= sInEnd) {
        cTmp=*sInStrt;
        *sInStrt=*sInEnd;
        *sInEnd=cTmp;
        ++sInStrt;
        --sInEnd;
      }
      sStart=sEnd+1;
      if (!(*sEnd)) break;
    }
    ++sEnd;
  }
  iStatus=1;

  return iStatus;
} //

// sOut must be the same size as sIn
int flipstring(const char *sIn, char *sOut)
{
  char *sWrdStart, *sWrdEnd, *sDst;
  int iStatus=0;
  int iStrLen=0;
  if(!sIn) return iStatus;

  iStrLen=strlen(sIn);
  sWrdStart=(char*)sIn;
  sWrdEnd=(char*)sIn;
  sDst=sOut+iStrLen;

  while (*sWrdEnd!=NULL) {
    if ((*sWrdEnd==' ')||(*sWrdEnd=='\t')||(*sWrdEnd==0)) {
      sDst=sDst-(sWrdEnd-sWrdStart);
      memcpy(sDst, sWrdStart, sWrdEnd-sWrdStart);
      // copy the whitespace character
      --sDst;
      
      fprintf(stderr, "[%s]\n", sDst);
      sWrdStart=sWrdEnd+1;
    }
    ++sWrdEnd;
  } // while

  // traverse string and look for a space

  return iStatus;
} // flipstring

int _tmain(int argc, char* argv[])
{
  int status;
  char *sIn, *sOut;

  if (argc!=2) { fprintf(stderr, "%s <string>\n", argv[0]); exit(-1); }

  //printf("Got: [%s]\n", argv[1]);

  //sOut=(char*)malloc(strlen(argv[1])+1);
  sIn=(char*)malloc(10);
  strcpy(sIn, "hell ther");
  //strcpy(sOut, sIn);

  flipstring3(sIn);
  //status=flipstring2(sIn);
  //if (status) printf("After: [%s]\n", sIn);
  //else printf("error during flipstring\n");
  //flipstring(sIn, sOut);
 
  return 0;
}

