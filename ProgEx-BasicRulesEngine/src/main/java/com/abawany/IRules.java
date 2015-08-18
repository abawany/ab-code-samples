package com.abawany;

public interface IRules {
	void initialize(int[] vals);

	/** allows stand-alone invocation of the evaluation function */
	boolean eval();

	/** returns the stored result of last eval invocation */
	boolean getResult() throws Exception;
}
