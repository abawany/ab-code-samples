package org.abawany;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestGenerateDistinctListsFromList {

	List<String> input;

	@Before
	public void setUp() throws Exception {
		input = new ArrayList<String>();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testOneItem() throws Exception {
		input.add("Hello");
		Assert.assertEquals(1, GenerateDistinctListsFromList.INSTANCE
				.getDistinctLists(input).size());
	}

	@Test
	public void testOneListOfDistinctItems() throws Exception {
		input.add("Hello");
		input.add("World");
		input.add("Something");
		input.add("Else");
		List<List<String>> output = GenerateDistinctListsFromList.INSTANCE
				.getDistinctLists(input);
		Assert.assertEquals(1, output.size());
		Assert.assertEquals(4, output.get(0).size());
	}

	@Test
	public void testOneItemDuplicated() throws Exception {
		input.add("Hello");
		input.add("Hello");
		input.add("Hello");

		List<List<String>> output = GenerateDistinctListsFromList.INSTANCE
				.getDistinctLists(input);
		Assert.assertEquals(3, output.size());
	}

	@Test
	public void testMultipleDuplicateAndDistinctItems() throws Exception {
		input.add("Hello");
		input.add("Hello");
		input.add("Hello");
		input.add("World");
		input.add("World");
		input.add("Something");
		input.add("Else");

		List<List<String>> output = GenerateDistinctListsFromList.INSTANCE
				.getDistinctLists(input);
		Assert.assertEquals(3, output.size());
		Assert.assertEquals(4, output.get(0).size());
	}
}
