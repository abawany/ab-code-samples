import java.io.*;
import java.util.*;

class ReverseString {
	/**
	* Given an input string (in), returns a version with the character
	* order reversed. Thus, "Hello World" will be returned as 
	* "World Hello". If a null string is provided, then an 
	* IllegalArgumentException is thrown.
	*/
	public static String reverse(String in) {
		if (in==null) 
			throw new IllegalArgumentException("null input string");
		StringBuffer out=new StringBuffer(in.length());
		
		for (int i=in.length()-1; i>=0; --i) {
			out.append(in.charAt(i));
		}
		
		return out.toString();
	}
	
	/** 
	* Test method and class used to keep track of expected outcome and results
	*/
	static class TestVector {
		public String input;
		public String expected;
		public boolean pass;
		
		public TestVector(String input, String expected, boolean pass) {
			this.input=input;
			this.expected=expected;
			this.pass=pass;
		}
	}

	public static List<TestVector> getTestSet() {
		List<TestVector> lstTestVector=new ArrayList<TestVector>();
		
		lstTestVector.add(new TestVector(null, null, false));
		lstTestVector.add(new TestVector("hello", "olleh", true));
		lstTestVector.add(new TestVector("Hello", "olleH", true));
		lstTestVector.add(new TestVector("a", "a", true));
		lstTestVector.add(new TestVector("Hello, World", "dlroW ,olleH", true));
		return lstTestVector;
	}
	
	public static void testReverseString() {
		List<TestVector> lstTestVectors=ReverseString.getTestSet();
		
		for (TestVector v: lstTestVectors) {
			try {
				String result=ReverseString.reverse(v.input);
				ReverseString.evaluateResult(v, result);
			} catch (Exception e) {
				ReverseString.evaluateResult(v, e);
			}
		}
	}
	
	/**
	* If pass expected, ensure that the expected matches the result
	* If fail expected, ensure that the expected doesn't match the result
	*/
	public static void evaluateResult(TestVector v, Object result) {
		if (v.pass) {
			if (result instanceof String && v.expected.equals((String)result))
				System.out.print("*PASS*: ");
			else 
				System.out.print("*FAIL*: " );
			System.out.println("input: [" + v.input + "] output: [" + v.expected 
				+ "] result: [" + result + "]");
		}	
		else {
			if (result==null|| !(result instanceof String))
				System.out.print("*PASS*: ");
			else if (!v.expected.equals(result))
				System.out.print("*PASS*: ");
			else 
				System.out.print("*FAIL*: ");
			System.out.println("input: [" + v.input + "]");
		}
	}
		
	public static void main(String []args) {
		ReverseString.testReverseString();
	}
}
