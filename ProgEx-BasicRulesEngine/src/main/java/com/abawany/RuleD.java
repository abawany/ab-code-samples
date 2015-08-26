package com.abawany;

public class RuleD extends RuleCommon {

	public boolean eval() {
		boolean rslt = true;
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rslt;
	}
}
