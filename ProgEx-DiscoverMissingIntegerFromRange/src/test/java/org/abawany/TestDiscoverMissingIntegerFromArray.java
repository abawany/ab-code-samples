package org.abawany;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDiscoverMissingIntegerFromArray {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void cleanUp() throws Exception {

	}

	@Test
	public void testFindMissingPositive() throws Exception {
		int[] in = { 0, 1, 3 };
		Assert.assertEquals(2,
				DiscoverMissingIntegerFromArray.INSTANCE.getMissingInt(in));
	}

	@Test
	public void testFindMissingNegative() throws Exception {
		int[] in = { -4, -3, -2, 0, 1, 2, 3, 4, 5, 6 };
		Assert.assertEquals(-1,
				DiscoverMissingIntegerFromArray.INSTANCE.getMissingInt(in));
	}

	@Test
	public void testFindZero() throws Exception {
		int[] in = { -1, 1, 2, 3 };
		Assert.assertEquals(0,
				DiscoverMissingIntegerFromArray.INSTANCE.getMissingInt(in));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNothingMissing() throws Exception {
		int[] in = { -1, 0, 1, 2, 3 };
		DiscoverMissingIntegerFromArray.INSTANCE.getMissingInt(in);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMultipleMissing() throws Exception {
		int[] in = { -4, -1, 0, 1, 2, 3, 4, 5 };
		DiscoverMissingIntegerFromArray.INSTANCE.getMissingInt(in);
	}
}
