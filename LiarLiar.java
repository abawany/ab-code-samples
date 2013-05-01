import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

class LiarLiar {

	BufferedReader inFile;
	public LiarLiar(String sFileName) {
		try {
			this.inFile=new BufferedReader(new FileReader(new File(sFileName)));
		}
		catch(IOException e) {
			e.printStackTrace();
			inFile=null;
		}
	}

	public void printMaxMin() throws Exception {
		if (inFile==null) throw new Exception("bad");

		String sBuf=inFile.readLine();
		int iNum=Integer.valueOf(sBuf);
		System.out.println("number of members: " + iNum);

		HashMap<String, Integer> map=new HashMap<String, Integer>(iNum);

		while (true) {
			try {
				// read a member's accusation list
				sBuf=inFile.readLine();
				if (sBuf==null) break;
				sBuf=sBuf.trim();
				if (sBuf.length()==0) break;
			} 
			catch(IOException e) { break; }
			
			StringTokenizer tok=new StringTokenizer(sBuf);
			String sAccuser=tok.nextToken();
			int iNumAccused=Integer.valueOf(tok.nextToken());
			// add this member to the map
			if (map.containsKey(sAccuser)==false)
				map.put(sAccuser, new Integer(0));
			for (int i=0; i<iNumAccused; ++i) {
				String sAccused=inFile.readLine().trim();
				if (map.containsKey(sAccused)==false) 
					map.put(sAccused, new Integer(1));
				else 
					map.put(sAccused, map.get(sAccused)+1);
			}
		}

		System.out.println(map.toString());
	}

	public static void main(String[] sArgs) throws Exception {
		if (sArgs.length!=1) throw new Exception("usage: LiarLiar <file>");

		LiarLiar l=new LiarLiar(sArgs[0]);
		l.printMaxMin();
	}
}
