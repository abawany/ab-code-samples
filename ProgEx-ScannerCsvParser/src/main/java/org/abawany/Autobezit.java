package org.abawany;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

public class Autobezit {
	/**
	 * @param args
	 *          the command line arguments
	 * @throws java.io.FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			ParseException {
		Hood brugge = new Hood("brugge", 50000);
		System.out.println(brugge.getInfo());

		Autobezit situation = new Autobezit();
		situation.initialise();
	}

	public void initialise() throws FileNotFoundException, ParseException {
		Scanner sc = new Scanner(new File(
				"src/main/java/org/abawany/Gent_autobezit.csv"));
		sc.useDelimiter(";");
		for (int i = 0; i < 3; i++) {
			sc.nextLine();
		}
		while (sc.hasNext()) {
			String name = sc.next();
			name = name.trim();
			if (name.length() == 0)
				continue; // skip blank lines
			String number = sc.next();
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
			nf.setParseIntegerOnly(true);
			Number frm = nf.parse(number);

			Hood hood = new Hood(name, frm.intValue());
			sc.nextLine();
			System.out.print(hood.getInfo());
		}
	}
}
