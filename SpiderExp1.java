import java.net.*;
import java.io.*;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.net.URI;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.MutableAttributeSet;

class SpiderExp1 
{
	//final String sUrl="http://www.bing.com";
	private String sBase;
	private String sNextUrl;

	private Reader rdr;	
	private HTMLEditorKit.ParserCallback cb;
	private URL url;
	
	public SpiderExp1(String sBase, String sUrl) {
		try {
			this.sBase=sBase;
			this.url=new URL(this.sBase+ sUrl);

			BufferedReader in = new BufferedReader(
				new InputStreamReader(url.openStream()));

			final SpiderExp1 me=this;

			
			this.cb=new HTMLEditorKit.ParserCallback() {
				public void handleText(char[] data, int pos) {
					me.handleLText(data, pos);
				}
				public void handleStartTag(HTML.Tag t, 
					MutableAttributeSet a, int pos) {
					me.handleLStartTag(t, a, pos);
				}
				public void handleSimpleTag(HTML.Tag t, 
					MutableAttributeSet a, int pos) {
					me.handleLSimpleTag(t, a, pos);
				}
				public void handleEndTag(HTML.Tag t,int pos) {
					me.handleLEndTag(t, pos);
				}
				public void handleError(String sErr,int pos) {
					me.handleLError(sErr, pos);
				}
			};
	
			this.rdr=in;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String push() {

		boolean blException=false;
		do { 
		try {
			new ParserDelegator().parse(this.rdr, cb, false);
			blException=false;
		} 
		catch(IOException e) {
			e.printStackTrace();
			blException=true;
		}
		finally {
			//this.rdr.close();
		}
		} while (blException==true);

		return this.sNextUrl;
	}

	private void handleLText(char[] data, int pos) {
		//System.out.println(data);
	}

	private boolean nextFlag;
	private void handleLStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
		System.out.println("StTg");
		this.nextFlag=false;
		Enumeration en=a.getAttributeNames();
		while (en.hasMoreElements()) {
			Object o=en.nextElement();
			System.out.println(o.getClass() + ":" + a.getAttribute(o).getClass());
			System.out.println(o + ":" + a.getAttribute(o));
			if (o.equals(HTML.Attribute.HREF)) {
				this.sNextUrl=(String)a.getAttribute(o);
			}
		}
	}

	private void handleLEndTag(HTML.Tag t, int pos) {
		if (this.nextFlag!=true) this.sNextUrl=null;
		else System.out.println("NXT: " + this.sNextUrl);
	}

	private void handleLSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
		System.out.println("SmTg");
		Enumeration en=a.getAttributeNames();
		while (en.hasMoreElements()) {
			Object o=en.nextElement();
			System.out.println(o.getClass() + ":" + a.getAttribute(o).getClass());
			System.out.println(o + ":" + a.getAttribute(o));
			if (o.equals(HTML.Attribute.SRC)) {
				if (((String)a.getAttribute(o)).equals("next.gif")) {
					this.nextFlag=true;
				}
				this.saveImg(this.sBase, (String)a.getAttribute(o));
			}
		}
	}

	private void saveImg(String sBase, String sImg) {
		byte[] in=new byte[1024];
		try {
			URL img=new URL(sBase+sImg);
			InputStream is=img.openStream();
			FileOutputStream out=new FileOutputStream(new File(sImg));

			int iReadLen=0;
			while (true) {
				//System.out.println(iReadLen);
				iReadLen=is.read(in, 0, 1024);
				if (iReadLen==-1) break;
				out.write(in, 0, iReadLen);
			}
			out.close();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleLError(String sErr, int pos) {
		//System.out.println("Serr: " + sErr);
	}

	static public void main(String[] sArgs) {
		if (sArgs.length != 2) {
			System.err.println("usage: SpiderExp1 <base url> <starting document>");
			return;
		}
		
		final String sBase=sArgs[0];
		String sUrl=sArgs[1];

		do {
			System.out.println("going to " + sBase + sUrl);
			sUrl=(new SpiderExp1(sBase, sUrl)).push();
		} while (sUrl!=null);
	}
}
