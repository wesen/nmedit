package org.nomad.patch;

import java.io.BufferedReader;

class KeyboardAssignment {
    
    // .pch info
	Integer[] keyboardAssignment;
    // .pch info

	KeyboardAssignment() {
		keyboardAssignment = new Integer[4];
	}
	
	public int getKeyboardAssignment(int index) {
		return keyboardAssignment[index].intValue();
	}
	
	public void readKeyboardAssignment(BufferedReader pchFile) {
		String[] sa = new String[4];
		try {		
			sa = pchFile.readLine().split(" ");
			keyboardAssignment[0] = new Integer(sa[0]);
			keyboardAssignment[1] = new Integer(sa[1]);
			keyboardAssignment[2] = new Integer(sa[2]);
			keyboardAssignment[3] = new Integer(sa[3]);
		}
		catch(Exception e) {
			System.out.println(e + " in readKeyboardAssignment");
		}
	}

// Creeren patch gegevens

	public StringBuffer createKeyboardAssignment(StringBuffer result) {
		if (keyboardAssignment[0] != null) {
			result.append("[KeyboardAssignment]\r\n");
// Geen gebruik van getKeyboardAssignment[] ipv keyboardAssignment[], want dat levert alleen maar meer stack op...
			result.append("" + keyboardAssignment[0] + " " + keyboardAssignment[1] + " " + keyboardAssignment[2] + " " + keyboardAssignment[3] + "\r\n");
			result.append("[/KeyboardAssignment]\r\n\r\n");
		}
		return result;
	}
}
