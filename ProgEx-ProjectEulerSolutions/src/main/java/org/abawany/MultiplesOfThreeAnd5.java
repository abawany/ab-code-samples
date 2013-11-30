package org.abawany;

public enum MultiplesOfThreeAnd5 {
	INSTANCE;

	public long getSumOfMultiples(long range) {
		long sum = 0;

		for (long i = 1; i < range; ++i) {
			if (i % 3 == 0 || i % 5 == 0)
				sum += i;
		}

		return sum;
	}
}
