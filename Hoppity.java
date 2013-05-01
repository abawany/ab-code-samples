import java.io.*;

class Hoppity {
	
	BufferedReader inFile;
	public Hoppity(String sFileName) {
		try {
			this.inFile=new BufferedReader(new FileReader(new File(sFileName)));
		}
		catch(IOException e) {
			e.printStackTrace();
			inFile=null;
		}
	}

	public void printOutput() throws Exception {
		if (inFile==null) throw new Exception("class not initialized");
		String sIn=this.inFile.readLine();
		sIn=sIn.trim();
		Integer iVal=new Integer(sIn);
		System.out.println("Got3: [" + iVal + "]");

		for (int i=1; i<=iVal.intValue(); ++i) {
			boolean blThree=((i%3)==0);
			boolean blFive=((i%5)==0);
			if (blThree&&blFive) 
				System.out.println("Hop");
			else if (blThree) 
				System.out.println("Hoppity");
			else if (blFive)
				System.out.println("Hophop");

		}
	}

	public static void main(String[] sArgs) {
		if (sArgs.length!=1) {
			System.out.println("usage: Hoppity <file>");
			return;
		}

		try {
			Hoppity h=new Hoppity(sArgs[0]);
			h.printOutput();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} // main
}