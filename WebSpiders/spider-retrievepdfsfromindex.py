import sys
import urllib2
from HTMLParser import HTMLParser

# attrib: http://docs.python.org/2/library/htmlparser.html
class MyHTMLParser(HTMLParser):
		def __init__(self, url):
			HTMLParser.__init__(self)
			self.url=url
			self.base_url=self.url[:self.url.rfind("/")+1]
			print "base url [" + self.base_url + "]\n"
			self.file_count = 0
		
		def handle_starttag(self, tag, attrs):

			for attr in attrs:
				# print "attr " + attr[0] + " " + attr[1]
				if attr[0]=="href" and (attr[1].rfind("pdf") != -1):
					file_name=attr[1][attr[1].rfind("/")+1:]
					file_data=urllib2.urlopen(self.base_url+attr[1])
					print "file-get ", file_name, " ", file_data.getcode()
					if file_data.getcode()==200:
						filebuf=file_data.read()
						outfilename = str(self.file_count) + file_name # keep track of file order
						self.file_count = self.file_count + 1
						outfile=open(outfilename, "wb")
						print "opened ", outfilename
						outfile.write(filebuf)
						outfile.close()
				
		def handle_endtag(self, tag):
			return None

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

