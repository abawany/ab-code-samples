import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;

public class RegexTestHarness {
	public static void main(String[] args){
	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.print("regex> ");
				Pattern pattern = 
					Pattern.compile(in.readLine());

				System.out.print("input> ");
				Matcher matcher = 
					pattern.matcher(in.readLine());

					boolean found = false;
					while (matcher.find()) {
						System.out.println("Found [" + matcher.group() + 
							"] starting at " + matcher.start() + 
							"and ending at " + matcher.end());
						found = true;
					}
					if(!found)
						System.out.println("No match found");
			} catch (Exception e) { e.printStackTrace(); }
		} 
	}
}