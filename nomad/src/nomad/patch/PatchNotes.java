package nomad.patch;

import java.io.BufferedReader;

class PatchNotes {
	private StringBuffer patchNotes;

	PatchNotes () {
		patchNotes = new StringBuffer("");
    }

// Setters

	public void addLine(String line) {
		patchNotes.append(line + "\r\n");
	}

// Getters

// Inlezen patch gegevens.

	public void readPatchNotes(BufferedReader pchFile) {
		String dummy;
		try {
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/Notes]") != 0)
					addLine(dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readPatchNotes");
		}
	}

// Creeren patch gegevens

	public StringBuffer createNotes(StringBuffer result) {
		if (patchNotes.length() > 0) {
			result.append("[Notes]\r\n");
			result.append(patchNotes);
			result.append("[/Notes]\r\n");
		}
		return result;
	}
}	
