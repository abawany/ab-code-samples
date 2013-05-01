import java.io.*;

/** 
Simple class to unit test IsStringValid
*/
class IsStringValidTest {

	String[] testCase={
        "[]",
				"()", 
				"()[]", 
				"[()]", 
				"a(b[c]d)e",
				"[",
				")",
				"[(])",
				"[123]]",
				"[123]]]",
				"[123][]",
        "([]",
        "((helo))",
	};

	Boolean[] testCaseRslts={
          Boolean.TRUE,
					Boolean.TRUE, 
					Boolean.TRUE,
					Boolean.TRUE,
					Boolean.TRUE,
					Boolean.FALSE,
					Boolean.FALSE,
					Boolean.FALSE,
					Boolean.FALSE,
					Boolean.FALSE,
					Boolean.TRUE,
          Boolean.FALSE,
          Boolean.TRUE,
	};

	public void testAll() {
		IsStringValid isv=new IsStringValid();

		for (int i=0; i<testCase.length; ++i) {
			Boolean result=isv.isStringValid(testCase[i]);
			Boolean testResult=testCaseRslts[i].equals(result);

			System.out.println( ((testResult==Boolean.TRUE)?"PASS":"FAIL")
				+ " for " + testCase[i] 
				+ "\n\texpected: " + testCaseRslts[i]
				+ " got: " + result);
		}
	}

	public static void main(String[] sArgs) {
		(new IsStringValidTest()).testAll();
	}
}