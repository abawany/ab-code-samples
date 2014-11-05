# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

import sys

# Given two files, open them, read in the lines and put them in respective
# maps, and then compare and output lines with differences
# TODO: if line is longer than some threshold, break it up into chunks
def comparator(fp1, fp2, pfxLen):
	# read in fp1 into in-memory hash
	lineCount = 1
	dictLineNum = {}
	dictLineVal = {}
	for line in fp1:
		key = line[:pfxLen]
		dictLineNum[key] = lineCount	
		if key in dictLineVal:
			print "WARNING: collision at ", lineCount, ": replacing [", dictLineVal[key], "] with [", line, "]"
		dictLineVal[key] = line
		lineCount = lineCount + 1

	# read in fp2 and look
	lineCount = 1
	for line in fp2:
		key = line[:pfxLen]
		line1 = dictLineVal[key]
		line1Num = dictLineNum[key]
		if line != line1:
			print "ORG: [", line1, "] at [",line1Num, "]\n"
			print "NEW: [", line, "] at [",lineCount, "]\n"
		lineCount = lineCount + 1


# check for input args
if len(sys.argv) != 4:
	print "usage: ", sys.argv[0], " <prefix size> <file1> <file2>"
	exit(-1)

#print sys.argv
fp1 = None
fp2 = None
try:
	fp1 = open(sys.argv[2], 'r')
	fp2 = open(sys.argv[3], 'r')
	pfx = int(sys.argv[1])
	comparator(fp1, fp2, pfx)

except Exception as e:
	print "Exception ", e
	exit(-1)

else:
	fp1.close()
	fp2.close()

