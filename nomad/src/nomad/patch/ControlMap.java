package nomad.patch;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Hashtable;

class ControlMap {
	private Hashtable controlMap = null;

// controller.ccNumber is hashkey.
// object is controller
	
	ControlMap () {
		controlMap = new Hashtable();
	}

// Setters

	public void addControl(String params) {
		String[] paramArray = new String[4];
		paramArray = params.split(" ");
		int newSection, newModule, newParameter, newCCNumber;
		Controller controller;

		newSection = Integer.parseInt(paramArray[0]);
		newModule = Integer.parseInt(paramArray[1]);
		newParameter = Integer.parseInt(paramArray[2]);
		newCCNumber = Integer.parseInt(paramArray[3]);

		controller = new Controller(newSection, newModule, newParameter, newCCNumber);
// Check op duplicate ccNumber. Misschien een HashMap gebruiken?		
		controlMap.put(new Integer(newCCNumber), controller);
	}

// Getters

	public Hashtable getControlMap() {
		return controlMap;
	}

	public int getControlMapSize() {
		return controlMap.size();
	}
	
	public Controller getController(int index) {
		return (Controller) controlMap.get(new Integer(index));
	}

// Inlezen patch gegevens.

	public void readCtrlMapDump(BufferedReader pchFile) {
		String dummy;
		try {
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/CtrlMapDump]") != 0)
					addControl(dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readCtrlMapDump");
		}
	}

// Creeren patch gegevens

	public StringBuffer createControlMapDump(StringBuffer result) {
		int i = 0;
		Controller con = null;
		if (getControlMapSize() > 0) {
			result.append("[ControlMapDump]\r\n");
			for (Enumeration e = getControlMap().keys(); e.hasMoreElements();) {
				i = ((Integer) e.nextElement()).intValue();
				con = getController(i);
				result.append("" + con.getSection() + " " + con.getModule() + " " + con.getParameter() + " " + con.getCCNumber() + "\r\n");
			}
			result.append("[/ControlMapDump]\r\n");
		}
		return result;
	}
}
