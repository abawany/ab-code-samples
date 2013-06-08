package com.ab;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestParticleAnimation {

	ParticleAnimation uut=null;
	
	@Before
	public void setup() throws Exception {
		this.uut=new ParticleAnimation();
	}
	
	@Test 
	public void testAnimate() throws Exception {
		// case 0
		String expc1[]={"..X....", "....X..", "......X", "......."};
		String rslt1[]=this.uut.animate(2, "..R....");
		Assert.assertArrayEquals(expc1, rslt1);
		this.outputParticles(rslt1);
		
		// case 1
		String expc2[]={"XX..XXX", ".X.XX..", "X.....X", "......." };
		String rslt2[]=this.uut.animate(3, "RR..LRL");
		Assert.assertArrayEquals(expc2, rslt2);
		this.outputParticles(rslt2);
		
		// case 2
		String expc3[]={ "XXXX.XXXX", "X..X.X..X", ".X.X.X.X.", ".X.....X.",
				"........." };
		String rslt3[]=this.uut.animate(2,  "LRLR.LRLR");
		Assert.assertArrayEquals(expc3, rslt3);
		this.outputParticles(rslt3);
		
		// case 3
		String expc4[]={ "XXXXXXXXXX", ".........." };
		String rslt4[]=this.uut.animate(10,  "RLRLRLRLRL");
		Assert.assertArrayEquals(expc4, rslt4);
		this.outputParticles(rslt4);
		
		// case 4
		String expc5[]={ "..." };
		String rslt5[]=this.uut.animate(1,  "...");
		Assert.assertArrayEquals(expc5, rslt5);
		this.outputParticles(rslt5);
		
		// case 5
		String expc6[]={ "XXXX.XX.XXX.X.XXXX.",	"..XXX..X..XX.X..XX.",
				".X.XX.X.X..XX.XX.XX", "X.X.XX...X.XXXXX..X",
				".X..XXX...X..XX.X..", "X..X..XX.X.XX.XX.X.",
				"..X....XX..XX..XX.X", ".X.....XXXX..X..XX.",
				"X.....X..XX...X..XX", ".....X..X.XX...X..X",
				"....X..X...XX...X..", "...X..X.....XX...X.",
				"..X..X.......XX...X", ".X..X.........XX...",
				"X..X...........XX..", "..X.............XX.",
				".X...............XX", "X.................X",
				"..................." }; 
		String rslt6[]=this.uut.animate(1,  "LRRL.LR.LRR.R.LRRL.");
		Assert.assertArrayEquals(expc6, rslt6);
		this.outputParticles(rslt6);
	}
	
	private void outputParticles(String []particles) {
		System.out.println("results: ");
		for (String x: particles) {
			System.out.println(x);
		}
	}
}
