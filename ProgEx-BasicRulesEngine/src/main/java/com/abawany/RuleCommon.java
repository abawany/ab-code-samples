package com.abawany;

import java.util.concurrent.CyclicBarrier;

/**
 * This class implements common behaviors for a RuleX class to minimize the
 * amount of boiler-plate needed for the Rule class. Thus, a new RuleX class
 * must extend this class and implement a constructor and the eval function,
 * which is part of the IRules interface
 */
public class RuleCommon implements IRules, Runnable {

	protected boolean result = false;
	protected boolean runCompleted = false;
	Thread t;
	CyclicBarrier b;

	protected void runThread() {
		this.t = new Thread(this);
		this.t.start();
	}

	public void run() {
		this.result = eval();
		try {
			this.b.await();
		} catch (Exception e) {
			this.result = false;
			e.printStackTrace();
		}
	}

	int[] vals;

	public void initialize(CyclicBarrier b, int[] vals) {
		this.b = b;
		this.vals = vals;
		this.result = false;
		this.runThread();
	}

	public boolean eval() {
		throw new UnsupportedOperationException("sub class must implement eval");
	}

	public final boolean getResult() throws Exception {
		return this.result;
	}
}
