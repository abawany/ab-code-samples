package main.com.ab;

/*
 *  Given a string of form "A" or "AB" "AAB" return the numerical
 *  representation of the characters with the right order of magnitude. 
 *  For example, A=1, AA=27, etc.
 */
public class IntValueOfChars {
	public long getIntValue(String in) {
		long result=0;
		
		int len=in.length();
		for (int i=len-1; i>-1; --i) {
			char a=in.charAt(i);
			if (Character.isLetter(a)==false)
				throw new IllegalArgumentException("string must contain letters");
			a=Character.toUpperCase(a);
			result += (a-'A'+1) + (25*(len-i-1));
		}
		
		return result;
	}
}
