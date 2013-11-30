package org.abawany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum GenerateDistinctListsFromList {
	INSTANCE;

	public List<List<String>> getDistinctLists(List<String> input) {
		HashMap<String, Integer> tracker = new HashMap<String, Integer>();

		int maxCount = 0;
		for (String item : input) {
			Integer count = tracker.get(item);
			if (count == null) {
				count = new Integer(1);
				tracker.put(item, count);
			} else {
				count = count + 1;
				tracker.put(item, count);
			}

			if (count > maxCount)
				maxCount = count;
		}

		List<List<String>> rslt = new ArrayList<List<String>>(maxCount);
		for (int i = 0; i < maxCount; ++i)
			rslt.add(new ArrayList<String>());

		for (String item : input) {
			Integer count = tracker.get(item);
			rslt.get(count - 1).add(item);
			count = count - 1;
			tracker.put(item, count);
		}

		return rslt;
	}
}
