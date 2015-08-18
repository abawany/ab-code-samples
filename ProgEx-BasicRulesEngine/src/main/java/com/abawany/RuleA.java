package com.abawany;

/**
 * Rule A : If any two sequential integers in the sequence add up to 1000 or
 * greater, the rule is "passed". If not, the rule is "failed".
 */
public class RuleA extends RuleCommon {

	@Override
	public boolean eval() {
		boolean rslt = false;
		for (int i = 1; i < this.vals.length; ++i) {
			if (this.vals[i - 1] + this.vals[i] >= 1000) {
				rslt = true;
				break;
			}
		}

		return rslt;
	}
}
