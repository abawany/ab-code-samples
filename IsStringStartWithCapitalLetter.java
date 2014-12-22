import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class IsStringStartWithCapitalLetter {
	private static Pattern p = Pattern.compile("^[A-Z]\\S+");
	
	public static boolean isStartWithCapitalLetter(String in) 
		throws IllegalArgumentException {

		if (in == null || in.length() == 0)  {
			throw new IllegalArgumentException("invalid input");
		}
		
		Matcher m = p.matcher(in);
		boolean found = m.find();	
	
		return found;
	}

	public static void main(String []args) {
		String []testVals = { "T1", "a1", "aB", "123", "AB" };

		boolean []expectedResult = { true, false, false, false, true }; 

		for (int i = 0; i < testVals.length; ++i) {
			boolean rslt = isStartWithCapitalLetter(testVals[i]);
			String outcome;
			if (rslt != expectedResult[i]) {
				outcome = "FAIL";
			} else {
				outcome = "PASS";
			}

			System.out.println(outcome + ": " + testVals[i] + 
				" expected " + expectedResult[i] + " got " + 
				rslt);	
		}
	}
}
