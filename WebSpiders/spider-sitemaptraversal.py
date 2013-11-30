import sys
import urllib2
from xml.dom import minidom
#from xml.etree.ElementTree import parse 
import xml.parsers.expat

def getter(url):
	print url, "\n"
	urls = []
	try:
		response=urllib2.urlopen(url)
		xmldoc=minidom.parse(response)
		for element in xmldoc.getElementsByTagName('loc'):
			urls.append(element.firstChild.nodeValue)
	
	except (TypeError, AttributeError) as t:
		print "a",
		print t
	except xml.parsers.expat.ExpatError as x:
		print "ignoring non-xml document at " + url + "\n"
	except Exception as e: 
		print "e",
		print type(e),
		print e

	for childurl in urls:
		getter(childurl)

getter(sys.argv[1])
