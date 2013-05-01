
import java.util.HashSet;
import java.util.Stack;
import java.util.EmptyStackException;

/*
Write a program that determines if a string is valid or invalid. 
In lieu of a formal definition, generalize from the following examples.

Examples of valid strings include:

[]
()
()[]
[()]
a(b[c]d)e

Examples of invalid strings include:

[
)
[(])

For example, a command-line program is-string-valid, 
should produce the following outputs for the given inputs:

$ is-string-valid []
true
$ is-string-valid [
false

***

Problem Generalization: ensure that closures are correct within the string.

*/

class IsStringValid2{
  
	static final String startChars = "{[(<" ;
	static final String endChars =  "}])>";

	/**
	 * attribution: http://www.seas.gwu.edu/~simhaweb/cs133/lectures/module8/module8.html
	 * goes through string one char at a time and pushes starting parens into the stack
	 * and pops the same when a ending parens is discovered
	 * If stack is empty after the string is done, then string was well formed
	*/
	public Boolean isStringValid(String in) {
		//System.out.println("M " + in + " M");
		
		Boolean returnVal=Boolean.TRUE;
		if (in==null || in.length()==0) return returnVal;

		Stack<Character> stk=new Stack<Character>();
		try {
			for (Character x: in.toCharArray()) {
				if (startChars.indexOf(x)!=-1) 
					stk.push((Character)x);	
				else {
					int termIndex=endChars.indexOf(x);
					if (termIndex==-1) 
						continue;
					Character current=stk.peek();
					if (startChars.indexOf(current.charValue())==termIndex)
						stk.pop();
				}
			}	
			if (stk.empty()==false)
				returnVal=Boolean.FALSE;
		}
		catch (EmptyStackException e) {
			returnVal=Boolean.FALSE;
		}
		catch (Exception e) {
			// FIXME: weak error handling, just in case
			e.printStackTrace();
			returnVal=Boolean.FALSE;
		}
		
		return returnVal;
	}

	public static void main(String[] sArgs) {

		IsStringValid2 isv=new IsStringValid2();

		if (sArgs.length!=1) {
			System.out.println("usage: java " + isv.getClass().getName() + " <string>");
			return;
		}

		// prints the result
		System.out.println(isv.isStringValid(sArgs[0]));
	}
}

