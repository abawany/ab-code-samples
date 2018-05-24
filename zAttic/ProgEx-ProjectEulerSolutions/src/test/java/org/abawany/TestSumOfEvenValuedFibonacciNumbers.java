package org.abawany;

import junit.framework.Assert;

import org.junit.Test;

public class TestSumOfEvenValuedFibonacciNumbers {

	@Test
	public void test() throws Exception {
		long sum = SumOfEvenValuedFibonacciNumbers.INSTANCE.getSum(4000000L);
		Assert.assertEquals(4613732, sum);
	}
}
