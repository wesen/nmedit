package nomad.patch;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Hashtable;

class KnobAssignMap {
    
    // .pch info
	private Hashtable knobMap = null;
    // .pch info

// knob.knob is hashkey.
// object is knob

	KnobAssignMap (){
		knobMap = new Hashtable();
	}

// Setters

	public void addKnob(String params) {
		String[] paramArray = new String[4];
		paramArray = params.split(" ");
		int newSection, newModule, newParameter, newKnob;
		KnobAssign knob;

		newSection = Integer.parseInt(paramArray[0]);
		newModule = Integer.parseInt(paramArray[1]);
		newParameter = Integer.parseInt(paramArray[2]);
		newKnob = Integer.parseInt(paramArray[3]);

		knob = new KnobAssign(newSection, newModule, newParameter, newKnob);
// Check op duplicate knob. Misschien een HashMap gebruiken?		
		knobMap.put(new Integer(newKnob), knob);
	}

// Getters

	public Hashtable getKnobMap() {
		return knobMap;
	}
	
	public int getKnobMapSize() {
		return knobMap.size();
	}
	
	public KnobAssign getKnob(int index) {
		return (KnobAssign) knobMap.get(new Integer(index));
	}

// Inlezen patch gegevens.
	
	public void readKnobMapDump(BufferedReader pchFile) {
		String dummy;
		try {
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/KnobMapDump]") != 0)
					addKnob(dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readKnobMapDump");
		}
	}

// Creeren patch gegevens

	public StringBuffer createKnobMapDump(StringBuffer result) {
		int i = 0;
		KnobAssign knob = null;
		if (getKnobMapSize() > 0) {
			result.append("[KnobMapDump]\r\n");
			for (Enumeration e = getKnobMap().keys(); e.hasMoreElements();) {
				i = ((Integer) e.nextElement()).intValue();
				knob = getKnob(i);
				result.append("" + knob.getSection() + " " + knob.getModule() + " " + knob.getParameter() + " " + knob.getKnob() + "\r\n");
			}
			result.append("[/KnobMapDump]\r\n");
		}
		return result;
	}
}
