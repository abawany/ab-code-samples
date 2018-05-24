package org.abawany;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

// Implement the addReservation function of a restaurant reservation system.
// parameters: date, time, number of people

// collection of tables ordered by table size
// each table has slots that represent a unit of time, say every 30 minutes

public enum ReserveTable {
	INSTANCE;

	HashMap<Integer, List<Table>> tableMap;

	static final int MAX_TABLE_SLOTS = 32;

	class Table {
		List<Boolean> reservations = new ArrayList<Boolean>(MAX_TABLE_SLOTS);
		int id;
	}

	/**
	 * Assumptions: - first priority is to seat the party at their desired time,
	 * so it is possible for a party of two to be seated at a table of 8, if that
	 * is the only table available at that time - each table can be reserved for
	 * an hour by each party - in the class Table, reservations array is sized
	 * MAX_TABLE_SLOTS since the restaurant is expected to remain open for 8 hours
	 * per day (thus 16 slots per day) and that tables can reserved for no more
	 * than 1 day in the future - returns -1 if no table could be reserved, else
	 * returns the id of the table
	 */
	public int addReservation(int numberOfPeople, Date appointmentTime,
			int numberOfSlots) {
		int tableId = -1;

		int slot = getSlot(appointmentTime);
		for (Integer tableSize : tableMap.keySet()) {
			if (tableSize < numberOfPeople)
				continue;

			List<Table> tables = tableMap.get(tableSize);
			for (Table t : tables) {
				Boolean slotFound = true;
				for (int i = 0; i < numberOfSlots; ++i) {
					if (t.reservations.get(slot + i) == false) {
						slotFound = false;
						break;
					}
				}
				if (slotFound) {
					for (int i = 0; i < numberOfSlots; ++i) {
						t.reservations.set(slot + i, false);
					}
					tableId = t.id;
					break;
				}
			}
		}

		return tableId;
	}

	/**
	 * Given a date and time component, first uses the date to determine if it is
	 * today or tomorrow. If today, return value will be between 0-15, else 16-31.
	 * Earliest restaurant open time is 10am and close time is 6pm, which
	 * represents 16 30-minute slots
	 */
	private int getSlot(Date appointmentTime) {
		return 0;
	}
}