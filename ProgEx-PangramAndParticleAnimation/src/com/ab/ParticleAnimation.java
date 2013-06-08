package com.ab;

import java.util.ArrayList;
import java.util.List;

public class ParticleAnimation {
	
	public ParticleAnimation() {
		
	}
	
	class Particle {
		public Particle(char c, int pos) {
			this.c=c;
			this.pos=pos;
		}
		public char c;
		public int pos;
	}
	
	public String[] animate(int speed, String init) {
		// validity checks
		if (speed < 1 || speed > 10) 
			throw new IllegalArgumentException("speed must be between 1 and 10");
		if (init==null || init.length() < 1 || init.length() > 50)
			throw new IllegalArgumentException("init must be between 1 and 50 characters");
		
		int len=init.length();
		List<Particle> tracker=new ArrayList<Particle>();
		
		// character validity test and transformation
		for (int i=0; i<len; ++i) {
			char x=init.charAt(i);
			if (x != '.' && x != 'R' && x != 'L') 
				throw new IllegalArgumentException("Invalid particle specified at offset " + i);
			
			if (x != '.') {
				tracker.add(new Particle(x, i));
			}
		}
		
		List<String> rslt=new ArrayList<String>();
		// run simulation and populate results array
		while (true) {
			// add the current snapshot to the results array
			rslt.add(this.getCurrentParticleSnapshot(len, tracker));
			
			// termination condition
			if (this.isSimulationDone(len, tracker))
				break;
			
			// update particles
			this.updateParticles(speed, tracker);
		}
		
		return rslt.toArray(new String[rslt.size()]);
	}
	
	private void updateParticles(int speed, List<Particle> tracker) {
		for (Particle p: tracker) {
			switch(p.c) {
			case 'R':
				p.pos += speed;
				break;
			case 'L':
				p.pos -= speed;
				break;
			}
		}
	}
	
	private String getCurrentParticleSnapshot(int len, List<Particle> tracker) {
		StringBuffer rslt = new StringBuffer(len);
		for (int i=0; i < len; ++i) 
			rslt.append('.');
		
		for (Particle p: tracker) {
			if (p.pos > -1 && p.pos < len) 
				rslt.setCharAt(p.pos, 'X');
		}
		
		return rslt.toString();
	}
	
	private boolean isSimulationDone(int len, List<Particle> tracker) {
		boolean isDone=true;
		
		for (Particle p: tracker) {
			if (p.pos > 0 && p.pos < len) {
				isDone = false;
				break;
			}
		}
		
		return isDone;
	}
	
}
