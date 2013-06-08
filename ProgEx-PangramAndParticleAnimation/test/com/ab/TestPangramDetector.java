package com.ab;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPangramDetector {
	
	PangramDetector uut=null;
	
	@Before
	public void setUp() throws Exception {
		this.uut=new PangramDetector();
	}
	
	@Test
	public void testFindMissingLetters() throws Exception {
		// case 0
		String result=this.uut.findMissingLetters( "The quick brown fox jumps over the lazy dog");
		Assert.assertEquals("", result);
		// case 1
		result=this.uut.findMissingLetters("The slow purple oryx meanders past the quiescent canine");
		Assert.assertEquals("bfgjkvz", result);
		// case 2
		result=this.uut.findMissingLetters("We hates Bagginses!");
		Assert.assertEquals("cdfjklmopqruvxyz", result);
		// case 3
		result=this.uut.findMissingLetters("");
		Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", result);
	}
}
