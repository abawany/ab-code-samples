package com.abawany;

/**
 * Rule B : If any integer in the sequence is at least 500 greater than the one
 * previous to it in the sequence, the rule is "passed", else it is "failed"
 */
public class RuleB extends RuleCommon {

	public boolean eval() {
		boolean rslt = false;
		for (int i = 1; i < this.vals.length; ++i) {
			if (this.vals[i] - 500 > this.vals[i - 1]) {
				rslt = true;
				break;
			}
		}

		return rslt;
	}

}
