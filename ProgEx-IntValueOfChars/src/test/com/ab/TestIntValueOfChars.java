package test.com.ab;

import main.com.ab.IntValueOfChars;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestIntValueOfChars {

	IntValueOfChars uut;
	
	@Before
	public void setUp() throws Exception {
		uut = new IntValueOfChars();
	}
	
	@Test
	public void testIntValueOfChars() throws Exception {
		Assert.assertEquals(1, uut.getIntValue("A"));
		Assert.assertEquals(26, uut.getIntValue("z"));
		Assert.assertEquals(27, uut.getIntValue("AA"));
		Assert.assertEquals(28, uut.getIntValue("AB"));
		Assert.assertEquals(52, uut.getIntValue("AZ"));
	}
	
	
	@After
	public void cleanUp() throws Exception {
		
	}
}
