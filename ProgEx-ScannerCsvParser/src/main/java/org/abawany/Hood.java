package org.abawany;

public class Hood {
	private String name;
	private int numberOfInhabitants;

	public Hood(String name, int numberOfInhabitants) {
		this.name = name;
		this.numberOfInhabitants = numberOfInhabitants;
	}

	public String getInfo() {
		return name + " " + numberOfInhabitants;
	}
}
