package org.abawany;

import junit.framework.Assert;

import org.junit.Test;

public class TestMultiplesOfThreeAndFive {
	@Test
	public void test() throws Exception {
		long val = MultiplesOfThreeAnd5.INSTANCE.getSumOfMultiples(1000);
		Assert.assertEquals(233168, val);
	}
}
