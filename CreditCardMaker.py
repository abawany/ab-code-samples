import sys
import random
import array

ccType={"mc":0, "mastercard":0, "amex":1, "visa":2, "discover":3, "disc":3}
ccLen={"MC":16, "AMEX":15, "DISC":16, "VISA":16}
ccEnum={0:"MC", 1:"AMEX", 2:"VISA", 3:"DISC"}

def fillRawDigits(len):
    for i in range(0, len-1):
        cardNumber.append(random.randint(0,9))
    return None

def fixRawDigits(sz):
    checkDigit=[0, 9, 8, 7, 6, 5, 4, 3, 2, 1]
    count=0
    sum=0
    for i in cardNumber:
        if((count&1)==(sz&1)):
            i=i*2
        if (i>9):
            i=i-9
        sum=sum+i
        count=count+1
    
    cardNumber.append(checkDigit[sum%10])
    return None

def makeMc():
    len=ccLen["MC"]-2
    cardNumber.append(5)
    cardNumber.append(random.randint(1,5))
    return len

def makeDisc():
    len=ccLen["DISC"]-4
    cardNumber.append(6)
    cardNumber.append(0)
    cardNumber.append(1)
    cardNumber.append(1)
    return len

def makeVisa():
    len=ccLen["VISA"]-1
    cardNumber.append(4)
    return len

def makeAmex():
    len=ccLen["AMEX"]-2
    cardNumber.append(4)
    cardNumber.append(random.randint(4, 9))
    return len

ccFunc={0:makeMc, 1:makeAmex, 2:makeVisa, 3:makeDisc}
cardNumber=[]

def makeCC(ccName):
    if (len(ccName)==0):
        raise Exception('ccName', 'isNull')
    
    try:
        ccTypeEnum=ccType[ccName]
    except KeyError:
        print ("ccName must be one of: "),
        print (ccType.keys())
        return None

    random.seed()
    #invoke fn pointer from hash
    sz=ccFunc[ccTypeEnum]()
    fillRawDigits(sz)
    fixRawDigits(ccLen[ccEnum[ccTypeEnum]])
    
    for i in cardNumber:
        sys.stdout.write(str(i))
    
    return None

if __name__=='__main__':
    if len(sys.argv)!=2:
        print("usage: CreditCardMaker <type>\ntypes are: ",)
        print(ccType.keys())
        quit()

    jnk, ccName=sys.argv
    makeCC(ccName)
    