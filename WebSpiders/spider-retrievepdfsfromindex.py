import sys
import urllib2
from HTMLParser import HTMLParser

# attrib: http://docs.python.org/2/library/htmlparser.html
class MyHTMLParser(HTMLParser):
		def __init__(self, url):
			HTMLParser.__init__(self)
			self.state=0
			self.url=url
			self.base_url=self.url[:self.url.rfind("/")+1]
			print self.base_url
		
		def handle_starttag(self, tag, attrs):
			if tag=="li":
				self.state=1
				#print "l ", attrs
				
			elif tag=="a" and self.state==1:
				self.state=2
				for attr in attrs:
					if attr[0]=="href":
						file_name=attr[1][attr[1].rfind("/")+1:]
						file_data=urllib2.urlopen(self.base_url+attr[1])
						print "file-get ", file_name, " ", file_data.getcode()
						if file_data.getcode()==200:
							filebuf=file_data.read()
							outfile=open(file_name, "wb")
							print "opened ", file_name
							outfile.write(filebuf)
							outfile.close()
				
			#else:
				#print "tag ", tag
				
		def handle_endtag(self, tag):
			if tag=="li":
				self.state=0
			elif tag=="a" and self.state==2:
				self.state=1
				
		#def handle_data(self, data):
			#print "data ", self.state, " ", data

def getter(url):
	parser=MyHTMLParser(url)
	
	try:
		response=urllib2.urlopen(url)
		print "host ", response.geturl(), " return code ", response.getcode()
		parser.feed(response.read())
		response.close()
		
	except Exception as e: 
		print "e", type(e), " ", e

def main():		
	if len(sys.argv) <= 1:
		print "usage: ", sys.argv[0], " <url>"
		exit(1)
		
	getter(sys.argv[1])

# entry point
main()

