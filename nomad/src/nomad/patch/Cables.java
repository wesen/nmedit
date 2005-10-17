package nomad.patch;

import java.io.BufferedReader;
import java.util.Vector;

import nomad.patch.ModuleSection.ModulesSectionType;

public class Cables {
	private Vector poly, common = null;

	public final static class CableType {
	    public final static int AUDIO = 0;
	    public final static int CNTRL = 1;
	    public final static int LOGIC = 2;
	    public final static int SLAVE = 3;
	    public final static int USER1 = 4;
	    public final static int USER2 = 5;
	    public final static int LOOSE = 6;
	    public final static int CREATE = 7; // When user creates a Cable  
	    public final static int MOVE = 8;   // When user moves a Cable
	}

	Cables() {
		poly = new Vector();
		common = new Vector();
	}

  public void removeCablesFromModule(int modIndex, int moduleSection) {
    int i = 0;
    Cable cab = null;
    Vector tempVector = new Vector();

    for (i=0; i < (moduleSection == ModulesSectionType.POLY?getPolySize():getCommonSize()); i++) {
        cab = getCable(moduleSection, i);
        if (cab.getBeginModule() == modIndex || cab.getEndModule() == modIndex) {
//            removeCable(cab, bPoly);
            tempVector.add(cab);
        }
    }
    
    for (i=0; i < tempVector.size(); i++) {
        removeCable((Cable)tempVector.get(i), moduleSection);
    }
  }
  
  public void removeCable(Cable cab, int moduleSection) {
    if (moduleSection == ModulesSectionType.POLY) {
      poly.remove(cab);
    }
    else {
      common.remove(cab);
    }
  }

// Setters

   public Cable addCable(int moduleSection, String params) {
	   Cable cab = new Cable(params);
	   if (moduleSection == ModulesSectionType.POLY)
		   poly.add(cab);
	   else
		   common.add(cab);
	   return cab;
   }

   public void addCable(int moduleSection, Cable newCable) {
	   if (moduleSection == ModulesSectionType.POLY)
		   poly.add(newCable);
	   else
		   common.add(newCable);
   }

// Getters

	public int getPolySize() {
		return poly.size();
	}

	public int getCommonSize() {
		return common.size();
	}

	public Cable getCable(int moduleSection, int index) {
		Cable returnCab;
		returnCab = (Cable) null;
		if (moduleSection == ModulesSectionType.POLY)
			returnCab = (Cable) poly.get(index);
		else
			returnCab = (Cable) common.get(index);
		return returnCab;
	}

// Inlezen patch gegevens

	public void readCableDump(BufferedReader pchFile) {
		String dummy;
		int moduleSection;
		try {
			if (pchFile.readLine().trim().compareTo("1") == 0)
                moduleSection = ModulesSectionType.POLY;
			else
                moduleSection = ModulesSectionType.COMMON;
			
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/CableDump]") != 0)
					addCable(moduleSection, dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readCableDump");
		}
	}

// Creeren patch gegevens

	public StringBuffer createCableDump(StringBuffer result) {
		int i = 0;
		Cable cab = null;
		result.append("[CableDump]\r\n");
		result.append("1\r\n");
		if (getPolySize() > 0) {
			for (i=0; i < getPolySize(); i++) {
				cab = getCable(ModulesSectionType.POLY, i);
				result.append("" + cab.getColor() + " " + cab.getBeginModule() + " " + cab.getBeginConnector() + " 0 " + cab.getEndModule() + " " + cab.getEndConnector() + " " + cab.getEndConnectorType() + "\r\n");
			}
		}
		result.append("[/CableDump]\r\n");
		
		result.append("[CableDump]\r\n");
		result.append("0\r\n");
		if (getCommonSize() > 0) {
			for (i=0; i < getCommonSize(); i++) {
				cab = getCable(ModulesSectionType.COMMON, i);
				result.append("" + cab.getColor() + " " + cab.getBeginModule() + " " + cab.getBeginConnector() + " 0 " + cab.getEndModule() + " " + cab.getEndConnector() + " " + cab.getEndConnectorType() + "\r\n");
			}
		}
		result.append("[/CableDump]\r\n");
		return result;
	}
}
