package org.abawany;

import java.util.ArrayList;
import java.util.List;

public enum SumOfEvenValuedFibonacciNumbers {
	INSTANCE;

	public long getSum(long largestValueInSequence) {
		long sum = 0;

		List<Long> seq = getFibSeq(largestValueInSequence);
		for (long l : seq) {
			if (l % 2 == 0)
				sum += l;
		}

		return sum;
	}

	private List<Long> getFibSeq(long largestValue) {
		List<Long> seq = new ArrayList<Long>();

		long first = 0;
		long next = 1;
		seq.add(next);

		while (true) {
			long tmp = first;
			first = next;
			next += tmp;
			tmp = next + first;
			if (tmp > largestValue)
				break;
			seq.add(tmp);
		}

		return seq;
	}
}
