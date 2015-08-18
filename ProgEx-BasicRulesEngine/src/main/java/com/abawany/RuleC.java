package com.abawany;

/**
 * Rule C : If the average of all the integers in the sequence is 500 or greater
 * the rule is "passed", else it is “failed".
 */
public class RuleC extends RuleCommon {

	public boolean eval() {
		boolean rslt = false;
		long total = 0;

		for (int i = 0; i < this.vals.length; ++i) {
			total += this.vals[i];
		}

		long avg = total / this.vals.length;

		if (avg >= 500) {
			rslt = true;
		}

		return rslt;
	}

}
