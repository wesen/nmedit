package nomad.com;

import java.util.Vector;

/**
 * List of MidiDriver objects
 * @author Christian Schneider
 * @see nomad.com.MidiDriver
 * @has 1 - * nomad.com.MidiDriver
 */
public class MidiDriverList {
	
	Vector driverList = new Vector();

	/**
	 * Creates the list
	 */
	public MidiDriverList() {
		super();
	}

	/**
	 * Adds a new MidiDriver object to the list
	 * @param driver the driver
	 */
	void registerDriver(MidiDriver driver) {
		driverList.add(driver);
	}

	/**
	 * Returns the driver at the specified index
	 * @param index index of the requested driver
	 * @return a driver
	 */
	public MidiDriver getDriver(int index) {
		return (MidiDriver) driverList.get(index);
	}

	/**
	 * Returns the number of drivers in the list.
	 * @return the number of drivers in the list
	 */
	public int getDriverCount() {
		return driverList.size();
	}
	
}
