package nomad.patch;

import java.io.BufferedReader;
import java.util.Vector;

class MorphMap {
	private Integer[] morphValues = new Integer[4];
	private Vector morphParams = null;

	MorphMap (){
		morphValues = new Integer[4];
		morphParams = new Vector();
	}

// Setters

	public void addMorphs(String params) {
		Morph morph;
		String[] paramArray = new String[6];
		int newSectionIndex, newModuleIndex, newParamIndex, newMorphIndex, newMorphRange;
		do {
			paramArray = params.split(" ", 6);
			newSectionIndex = Integer.parseInt(paramArray[0]);
			newModuleIndex = Integer.parseInt(paramArray[1]);
			newParamIndex = Integer.parseInt(paramArray[2]);
			newMorphIndex = Integer.parseInt(paramArray[3]);
			newMorphRange = Integer.parseInt(paramArray[4]);
			params = paramArray[5];
			morph = new Morph(newSectionIndex, newModuleIndex, newParamIndex, newMorphIndex, newMorphRange);
			morphParams.add(morph);
		} while (params.trim().length() > 0); // trim, omdat en nog een ' ' achter komt.
	}

// Getters

	public int getMorphMapParamsSize() {
		return morphParams.size();
	}

	public Morph getMorphParam(int index) {
//		Morph morph;
		return (Morph) morphParams.get(index);
	}

	public int getMorphValue(int index) {
		return morphValues[index].intValue();
	}

// Inlezen patch gegevens.

	public void readMorphMapDump(BufferedReader pchFile) {
//		String params;
		String[] valueArray = new String[4];
		try {		
			valueArray = pchFile.readLine().split(" ");
			morphValues[0] = new Integer(valueArray[0]);
			morphValues[1] = new Integer(valueArray[1]);
			morphValues[2] = new Integer(valueArray[2]);
			morphValues[3] = new Integer(valueArray[3]);
			addMorphs(pchFile.readLine());
		}
		catch(Exception e) {
			System.out.println(e + " in readMorphMapDump");
		}
	}

// Creeren patch gegevens

	public StringBuffer createMorphMapDump(StringBuffer result) {
		int i = 0;
		Morph morph = null;
		if (getMorphMapParamsSize() > 0) {
			result.append("[MorphMapDump]\r\n");
	// Geen gebruik van getMorphValue() ipv morphValues[], want dat levert alleen maar meer stack op...
			result.append("" + morphValues[0] + " "+ morphValues[1] + " "+ morphValues[2] + " "+ morphValues[3] + "\r\n");
			for (i=0; i < getMorphMapParamsSize(); i++) {
				morph = getMorphParam(i);
				result.append("" + morph.getSectionIndex() + " " + morph.getModuleIndex() + " " + morph.getParamIndex() + " " + morph.getMorphIndex() + " " + morph.getMorphRange() + " ");
			}
			result.append("\r\n[/MorphMapDump]\r\n\r\n");
		}
		return result;
	}
}
