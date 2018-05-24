package org.abawany;

import java.text.DateFormat;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ReserveTableTest {
	@Test
	public void testValidInstance() {
		Assert.assertNotNull(ReserveTable.INSTANCE);
	}

	@Test
	public void testGetTable() throws ParseException {
		int table = ReserveTable.INSTANCE.addReservation(2, DateFormat
				.getDateTimeInstance().parse("2013-09-27 11:00:00"), 1);
	}
}
