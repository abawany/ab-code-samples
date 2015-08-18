package com.abawany;

import java.util.concurrent.CyclicBarrier;

public interface IRules {
	void initialize(CyclicBarrier b, int[] vals);

	/** allows stand-alone invocation of the evaluation function */
	boolean eval();

	/** returns the stored result of last eval invocation */
	boolean getResult() throws Exception;
}
