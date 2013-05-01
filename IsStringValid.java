
import java.util.HashSet;
import java.util.Set;

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

$ is-string-valid �[]�
true
$ is-string-valid �[�
false

***

Problem Generalization: ensure that closures are correct within the string.

*/

class IsStringValid {
  
	static final String startChars = "{[(<" ;
	static final String endChars =  "}])>";

	/**
	This method uses a combination of iteration and recursion to determine if the 
	given string is valid using the problem statement parameters.

	First it looks through the string for an instance of the start character, 
  as listed in array startChars. If one of these is present in the string,
  it will look for the matching end character, as listed in endChars, in the
  string. If also found, then it takes the resulting substring and calls this
  method again with it for further processing.

	If start character is not found for a given start character, the method also searches 
	for the matching end character to catch mis-matches for this case as well.
	*/
	public Boolean isStringValid(String in) {
		//System.out.println("M " + in + " M");
		
		Boolean returnVal=Boolean.TRUE;
    if (in==null || in.length()==0) return returnVal;

    try {
      for (int i=0; i<in.length(); ) {
        int start=startChars.indexOf(in.charAt(i));
        if (start!=-1) {
          int end=in.indexOf(endChars.charAt(start), i);
          if (end==-1) // start found but not end
            throw new IsStringValidNotValidException();
          Boolean rtn=
							isStringValid(in.substring(i+1, end));
          if (rtn==Boolean.FALSE)
							throw new IsStringValidNotValidException();
          i=end+1;
        }
        else {
          int endWithoutStart=endChars.indexOf(in.charAt(i));
          if (endWithoutStart!=-1)
            throw new IsStringValidNotValidException();
          ++i;
        }
      }
		}
		catch (IsStringValidNotValidException e) {
			// just set return value to false
			returnVal=Boolean.FALSE;
		}
		catch (Exception e) {
			// FIXME: weak error handling, just in case
			e.printStackTrace();
			returnVal=Boolean.FALSE;
		}
		
		return returnVal;
	}

	class IsStringValidNotValidException extends Exception {}

	public static void main(String[] sArgs) {

		IsStringValid isv=new IsStringValid();

		if (sArgs.length!=1) {
			System.out.println("usage: java " + isv.getClass().getName() + " <string>");
			return;
		}

		// prints the result
		System.out.println(isv.isStringValid(sArgs[0]));
	}

}
