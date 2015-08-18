package com.abawany;

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

	protected void runThread() {
		this.t = new Thread(this);
		this.t.start();
	}

	public void run() {
		synchronized (this.t) {
			this.result = eval();
			this.runCompleted = true;
			this.t.notify();
		}
	}

	int[] vals;

	public void initialize(int[] vals) {
		this.vals = vals;
		this.result = false;
		this.runThread();
	}

	public boolean eval() {
		throw new UnsupportedOperationException("sub class must implement eval");
	}

	public final boolean getResult() throws Exception {
		synchronized (this.t) {
			if (!this.runCompleted)
				this.t.wait();
		}
		return this.result;
	}
}
