package com.ab;

import java.util.HashMap;

public class PangramDetector {
	
	public String findMissingLetters(String sentence) {
		HashMap<Character, Boolean> map=new HashMap<Character, Boolean>(cleanMap);
		
		// update map with all letters in sentence
		for (int i=0; i<sentence.length(); ++i) {
			Character c=Character.toLowerCase(sentence.charAt(i));
			map.put(c, Boolean.TRUE);
		}
		
		// build string that contains the missing letters
		StringBuffer rtn=new StringBuffer(26);
		for (int i=0; i < initString.length; ++i) {
			if (map.get(initString[i]).equals(Boolean.FALSE)) {
				rtn.append(initString[i]);
			}
		}
		
		return rtn.toString();
	}
	
	// initialize the golden template once for repeated use
	static HashMap<Character, Boolean> cleanMap;
	static char[] initString="abcdefghijklmnopqrstuvwxyz".toCharArray();
	static {
		cleanMap=new HashMap<Character, Boolean>(26);
		
		for (int i=0; i<initString.length; ++i) {
			cleanMap.put(initString[i], false);
		}
	}
}
