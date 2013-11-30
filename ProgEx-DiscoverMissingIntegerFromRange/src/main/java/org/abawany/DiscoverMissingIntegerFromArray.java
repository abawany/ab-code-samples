package org.abawany;

/**
 * Given an array containing positive and negative numbers, return the one
 * missing integer from the list. If more than one integers are missing, then
 * throw exception. No sort order is assumed for array elements.
 */
public enum DiscoverMissingIntegerFromArray {
	INSTANCE;

	public int getMissingInt(int[] in) throws Exception {

		// figure out the min value, max value, and whether zero is present
		int minVal = Integer.MAX_VALUE;
		int maxVal = Integer.MIN_VALUE;
		int arraySum = 0;
		boolean zeroFound = false;
		for (int i = 0; i < in.length; ++i) {
			if (in[i] > maxVal)
				maxVal = in[i];
			if (in[i] < minVal)
				minVal = in[i];
			if (in[i] == 0)
				zeroFound = true;
			arraySum += in[i];
		}

		int maxSum = triangularSum(maxVal);
		int minSum = triangularSum(minVal);

		int missingInt = maxSum + minSum - arraySum;
		if (missingInt == 0) {
			if (zeroFound)
				throw new IllegalArgumentException("no missing integer found");
			else
				return 0;
		} else {
			if (missingInt > minVal && missingInt < maxVal)
				return missingInt;
			else
				throw new IllegalArgumentException(
						"array appears to have multiple missing ints");
		}
	}

	/**
	 * http://www.maths.surrey.ac.uk/hosted-sites/R.Knott/runsums/triNbProof.html
	 * if i < 0, returns the sum of the numbers with negative magnitude
	 * 
	 * @param i
	 * @return
	 */
	private int triangularSum(int i) {
		int sign = (i < 0) ? -1 : 1;
		int rslt = ((i * sign) * ((i * sign) + 1)) / 2;
		return rslt * sign;
	}
}
