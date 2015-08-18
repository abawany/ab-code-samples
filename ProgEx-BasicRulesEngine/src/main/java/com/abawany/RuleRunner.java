package com.abawany;

import java.util.ServiceLoader;

class RuleRunner {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: RuleRunner <list of integers to evaluate>");
			return;
		}

		try {
			RuleRunner inst = new RuleRunner();
			int[] vals = inst.getArrayOfInts(args);

			ServiceLoader<IRules> sl = ServiceLoader.load(IRules.class);

			boolean allPass = true;
			for (IRules i : sl) {
				i.initialize(vals);
				boolean pass = i.getResult();
				if (pass) {
					System.out
							.println("Rule " + i.getClass().getSimpleName() + " passed");
				} else {
					System.out
							.println("Rule " + i.getClass().getSimpleName() + " failed");
					allPass = pass;
				}
			}

			if (allPass) {
				System.out.println("ALL rules passed");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int[] getArrayOfInts(String[] args) throws Exception {
		int[] rslt = new int[args.length];

		for (int i = 0; i < args.length; ++i) {
			rslt[i] = Integer.valueOf(args[i]);
		}

		return rslt;
	}
}
