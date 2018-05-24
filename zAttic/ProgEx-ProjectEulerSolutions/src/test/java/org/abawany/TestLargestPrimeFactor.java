package org.abawany;

import junit.framework.Assert;

import org.junit.Test;

public class TestLargestPrimeFactor {
	@Test
	public void test() throws Exception {
		Assert.assertEquals(29,
				LargestPrimeFactor.INSTANCE.getLargestPrimeFactor(13195));
	}
}
