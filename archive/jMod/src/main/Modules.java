package main;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;

import modules.*;

public class Modules {

// Hashtables, omdat we bijna altijd op patch index zullen zoeken, met enumeratie door tabel lopen.
// HashSet heeft geen key values om op te zoeken...

	private Hashtable poly = null;
	private Hashtable common = null;
	
	private int maxGridXPoly = 0;
	private int maxGridYPoly = 0;

	private int maxGridXCommon = 0;
	private int maxGridYCommon = 0;
	
//	private Cables cables;

    private PatchData patchData = null; 
    
	Modules(PatchData patchData) {
		poly = new Hashtable();
		common = new Hashtable();
//		props = new Properties();
//		cables = newCables;
		
//		props = openProp();

        this.patchData = patchData;
	}
	
	public void rearangeModules(JDesktopPane desktopPane, Module module, boolean bPoly) { 
		Module tempMod, tempMod2 = null;
		Vector modVector = new Vector();

//		Het schikken van de modulen
//		Nu alleen voor 1 geplaatste module
		for (int i=0;i<desktopPane.getComponentCountInLayer(JLayeredPane.DEFAULT_LAYER.intValue());i++) {
			 // Zoek alle module in de zelfde colom
		
			tempMod = ((Module)desktopPane.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER.intValue())[i]);
			if (module.getModuleData().getGridX() == tempMod.getModuleData().getGridX()) {
// We gaan net zolang null inserten todat we de Vector hebben gevult tot Verctor.size() == getGridY
				while (modVector.size() <= tempMod.getModuleData().getGridY())
					modVector.add(null);

				if (modVector.get(tempMod.getModuleData().getGridY()) == null)
					modVector.set(tempMod.getModuleData().getGridY(), tempMod);
				else
					modVector.insertElementAt(tempMod, tempMod.getModuleData().getGridY());
			}
		}
		
// Haal alle lege plaatsen uit de Vector (leuke oplossing eej!?)
		while (modVector.remove(null));
		
		for (int j=0;j<modVector.size()-1;j++) {
			tempMod = (Module) modVector.get(j);
			tempMod2 = (Module) modVector.get(j+1);
			if (tempMod2.getModuleData().getGridY() < (tempMod.getModuleData().getGridY() + tempMod.getModuleData().getGridHeight())) {
				module.setPixLocation(tempMod2, tempMod.getModuleData().getPixLocationX(), tempMod.getModuleData().getPixLocationY() + tempMod.getModuleData().getPixHeight());
			}
		}
		
		getCables().redrawCables(this, bPoly);
	}

	public Cables getCables() {
		return patchData.getCables();
	}

// Setters

	public Module addModule(boolean bPoly, String params, JModAreaPane newDesktopPane) {
		String[] paramArray = new String[4];
		paramArray = params.split(" ");
		
		Module mod;

		Integer pchIndex = new Integer(paramArray[0]);
		int type = Integer.parseInt(paramArray[1]);
		int gridX = Integer.parseInt(paramArray[2]);
		int gridY = Integer.parseInt(paramArray[3]);
		
//        mod = new Module(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane);
//        mod = (Module001)mod;
        
        // !TODO ?ClassLoader? http://www.javaworld.com/javaworld/jw-10-1996/jw-10-indepth.html
        
		switch (type) {
	      case   1: mod = new Module001(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      case   2: mod = new Module002(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      case   3: mod = new Module003(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      case   4: mod = new Module004(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      case   5: mod = new Module005(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      case   7: mod = new Module007(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      case   8: mod = new Module008(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      case   9: mod = new Module009(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  10: mod = new Module010(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  11: mod = new Module011(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  12: mod = new Module012(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  13: mod = new Module013(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  14: mod = new Module014(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  15: mod = new Module015(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  16: mod = new Module016(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  17: mod = new Module017(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  18: mod = new Module018(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  19: mod = new Module019(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  20: mod = new Module020(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  21: mod = new Module021(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  22: mod = new Module022(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  23: mod = new Module023(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  24: mod = new Module024(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  25: mod = new Module025(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  26: mod = new Module026(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  27: mod = new Module027(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  28: mod = new Module028(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  29: mod = new Module029(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  30: mod = new Module030(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  31: mod = new Module031(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  32: mod = new Module032(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  33: mod = new Module033(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  34: mod = new Module034(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  35: mod = new Module035(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  36: mod = new Module036(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  37: mod = new Module037(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  38: mod = new Module038(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  39: mod = new Module039(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  40: mod = new Module040(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  41: mod = new Module041(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  42: mod = new Module042(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  43: mod = new Module043(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  44: mod = new Module044(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  45: mod = new Module045(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  46: mod = new Module046(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  47: mod = new Module047(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  48: mod = new Module048(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  49: mod = new Module049(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  50: mod = new Module050(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  51: mod = new Module051(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  52: mod = new Module052(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  53: mod = new Module053(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  54: mod = new Module054(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  55: mod = new Module055(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  56: mod = new Module056(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  57: mod = new Module057(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  58: mod = new Module058(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  59: mod = new Module059(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  60: mod = new Module060(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  61: mod = new Module061(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  62: mod = new Module062(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  63: mod = new Module063(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  64: mod = new Module064(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  65: mod = new Module065(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  66: mod = new Module066(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  67: mod = new Module067(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  68: mod = new Module068(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  69: mod = new Module069(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  70: mod = new Module070(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  71: mod = new Module071(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  72: mod = new Module072(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  73: mod = new Module073(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  74: mod = new Module074(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  75: mod = new Module075(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  76: mod = new Module076(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  77: mod = new Module077(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  78: mod = new Module078(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  79: mod = new Module079(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  80: mod = new Module080(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  81: mod = new Module081(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  82: mod = new Module082(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  83: mod = new Module083(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  84: mod = new Module084(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  85: mod = new Module085(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  86: mod = new Module086(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  87: mod = new Module087(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  88: mod = new Module088(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  89: mod = new Module089(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  90: mod = new Module090(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  91: mod = new Module091(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  92: mod = new Module092(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  93: mod = new Module093(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  94: mod = new Module094(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  95: mod = new Module095(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  96: mod = new Module096(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  97: mod = new Module097(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  98: mod = new Module098(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case  99: mod = new Module099(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 100: mod = new Module100(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 101: mod = new Module101(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 102: mod = new Module102(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 103: mod = new Module103(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 104: mod = new Module104(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 105: mod = new Module105(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 106: mod = new Module106(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 107: mod = new Module107(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 108: mod = new Module108(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 109: mod = new Module109(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 110: mod = new Module110(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 111: mod = new Module111(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 112: mod = new Module112(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 113: mod = new Module113(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 114: mod = new Module114(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 115: mod = new Module115(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 116: mod = new Module116(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 117: mod = new Module117(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 118: mod = new Module118(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
          case 127: mod = new Module127(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
	      default : mod = new Module000(pchIndex, type, gridX, gridY, this, bPoly, newDesktopPane); break;
		}
		if (bPoly) {
			poly.put(pchIndex, mod);
			if (gridX + mod.getModuleData().getGridWidth() > maxGridXPoly)
				maxGridXPoly = gridX + mod.getModuleData().getGridWidth();
			if (gridY + mod.getModuleData().getGridHeight() > maxGridYPoly)
				maxGridYPoly = gridY + mod.getModuleData().getGridHeight();
		}
		else {
			common.put(pchIndex, mod);
			if (gridX + mod.getModuleData().getGridWidth() > maxGridXCommon)
				maxGridXCommon = gridX + mod.getModuleData().getGridWidth();
			if (gridY + mod.getModuleData().getGridHeight() > maxGridYCommon)
				maxGridYCommon = gridY + mod.getModuleData().getGridHeight();
		}
		return mod;
	}

	public void removeModule(Integer modIndex, boolean bPoly) {
	    getCables().removeCablesFromModule(modIndex.intValue(), bPoly);
	    if (bPoly)
	        poly.remove(modIndex);
	    else
	        common.remove(modIndex);
    }
    
	public void addModuleName(boolean bPoly, String params) {
		String[] sa = new String[2];
//		ModuleData mod;
		sa = params.split(" ", 2);
		getModule(bPoly, Integer.parseInt(sa[0])).getModuleData().setModuleName(sa[1]);
	}
	
	public void addParameter(boolean bPoly, String params) {
		String[] sa = new String[2];
//		ModuleData mod;
		sa = params.split(" ",2);
		getModule(bPoly, Integer.parseInt(sa[0])).getModuleData().setParameters(params);
	}

	public void addCustom(boolean bPoly, String params) {
		String[] sa = new String[2];
//		Custom cus;
		sa = params.split(" ",2);
		getModule(bPoly, Integer.parseInt(sa[0])).getModuleData().setCustoms(params);
	}
	
// Getters

	public void recalcGridXY(boolean bPoly) {
		int i = 0;
		Module tempMod = null;
		
//        if (bPoly) {
            maxGridXPoly = 0;
            maxGridYPoly = 0;
            Enumeration e = null;
            if (bPoly) e = poly.keys(); else e = common.keys();
            while (e.hasMoreElements()) {
                i = ((Integer) e.nextElement()).intValue();
                tempMod = getModule(bPoly, i); 
                if (tempMod.getModuleData().getGridX() + tempMod.getModuleData().getGridWidth() > maxGridXPoly)
                    maxGridXPoly = tempMod.getModuleData().getGridX() + tempMod.getModuleData().getGridWidth();
                if (tempMod.getModuleData().getGridY() + tempMod.getModuleData().getGridHeight() > maxGridYPoly)
                    maxGridYPoly = tempMod.getModuleData().getGridY() + tempMod.getModuleData().getGridHeight();
            }
//        }
    
//        if (bPoly) {
//            maxGridXPoly = 0;
//            maxGridYPoly = 0;
//            Enumeration e = poly.keys();
//            while (e.hasMoreElements()) {
//                i = ((Integer) e.nextElement()).intValue();
//                tempMod = getModule(bPoly, i); 
//                if (tempMod.getModuleData().getGridX() + tempMod.getModuleData().getGridWidth() > maxGridXPoly)
//                    maxGridXPoly = tempMod.getModuleData().getGridX() + tempMod.getModuleData().getGridWidth();
//                if (tempMod.getModuleData().getGridY() + tempMod.getModuleData().getGridHeight() > maxGridYPoly)
//                    maxGridYPoly = tempMod.getModuleData().getGridY() + tempMod.getModuleData().getGridHeight();
//            }
//        }
//		else {
//			maxGridXCommon = 0;
//			maxGridYCommon = 0;
//			Enumeration e = common.keys();
//			while (e.hasMoreElements()) {
//				i = ((Integer) e.nextElement()).intValue();
//				tempMod = getModule(bPoly, i); 
//				if (tempMod.getModuleData().getGridX() + tempMod.getModuleData().getGridWidth() > maxGridXCommon)
//					maxGridXCommon = tempMod.getModuleData().getGridX() + tempMod.getModuleData().getGridWidth();
//				if (tempMod.getModuleData().getGridY() + tempMod.getModuleData().getGridHeight() > maxGridYCommon)
//					maxGridYCommon = tempMod.getModuleData().getGridY() + tempMod.getModuleData().getGridHeight();
//			}
//		}
	}

  	public int getMaxWidth(boolean poly) {
  		return (poly?maxGridXPoly:maxGridXCommon) * ModuleData.pixWidth;
	}

	public int getMaxHeight(boolean poly) {
		return (poly?maxGridYPoly:maxGridYCommon) * ModuleData.pixHeight;
	}

	public Hashtable getPoly() {
		return poly;
	}

	public Hashtable getCommon() {
		return common;
	}

	public int getPolySize() {
		return poly.size();
	}

	public int getCommonSize() {
		return common.size();
	}

	public Module getModule(boolean bPoly, int index) {
// Omdat we bijna altijd op patch index zullen zoeken, anders met enumeratie door tabel lopen.
		Module returnMod;
		returnMod = (Module) null;
		if (bPoly)
			returnMod = (Module) poly.get(new Integer(index));
		else
			returnMod = (Module) common.get(new Integer(index));
		return returnMod;
	}

// Inlezen patch gegevens.

	public void readModuleDump(BufferedReader pchFile, JModAreaPane desktopPanePoly, JModAreaPane desktopPaneCommon) {
		String dummy = "";
		boolean bPoly;
		try {
			if (pchFile.readLine().trim().compareTo("1") == 0)
				bPoly = true;
			else
				bPoly = false;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/ModuleDump]") != 0)
					addModule(bPoly, dummy, bPoly?desktopPanePoly:desktopPaneCommon);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readModuleDump " + dummy);
		}
	}

	public void readNameDump(BufferedReader pchFile) {
		String dummy = "";
		boolean bPoly;
		try {
			if (pchFile.readLine().trim().compareTo("1") == 0)
				bPoly = true;
			else
				bPoly = false;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/NameDump]") != 0)
					addModuleName(bPoly, dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readNameDump " + dummy);
		}
	}

	public void readParameterDump(BufferedReader pchFile) {
		String dummy = "";
		boolean bPoly;
		try {
			if (pchFile.readLine().trim().compareTo("1") == 0)
				bPoly = true;
			else
				bPoly = false;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/ParameterDump]") != 0)
					addParameter(bPoly, dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readParameterDump " + dummy);
		}
	}

	public void readCustomDump(BufferedReader pchFile) {
		String dummy = "";
		boolean bPoly;
		try {
			if (pchFile.readLine().trim().compareTo("1") == 0)
				bPoly = true;
			else
				bPoly = false;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/CustomDump]") != 0)
					addCustom(bPoly, dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readCustomDump " + dummy);
		}
	}

// Creeren patch gegevens.

	public StringBuffer createModuleDump(StringBuffer result) {
		int i = 0;
		ModuleData modData = null;
//		if (getPolySize() > 0) {
			result.append("[ModuleDump]\r\n");
			result.append("1\r\n");
			Enumeration e = getPoly().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(true, i).getModuleData();
				result.append("" + modData.getModIndex() + " " + modData.getModType() + " " + modData.getGridX() + " " + modData.getGridY() + "\r\n");
			}
			result.append("[/ModuleDump]\r\n");
//		}
		
//		if (getCommonSize() > 0) {
			result.append("[ModuleDump]\r\n");
			result.append("0\r\n");
			e = getCommon().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(false, i).getModuleData();
				result.append("" + modData.getModIndex() + " " + modData.getModType() + " " + modData.getGridX() + " " + modData.getGridY() + "\r\n");
			}
			result.append("[/ModuleDump]\r\n");
//		}
		return result;
	}

	public StringBuffer createParameterDump(StringBuffer result) {
		int i, j = 0;
		Enumeration e;
		ModuleData modData = null;
//		if (getPolySize() > 0) {
			result.append("[ParameterDump]\r\n");
			result.append("1\r\n");
			e = getPoly().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(true, i).getModuleData();
				if (modData.getNoParameters() > 0) {
					result.append("" + modData.getModIndex() + " " + modData.getModType() + " " + modData.getNoParameters() + " ");
					for (j=0; j < modData.getNoParameters(); j++) {
						result.append(modData.getParameterValue(j) + " ");
					}
					result.append("\r\n");
				}
			}
			result.append("[/ParameterDump]\r\n");
//		}
		
//		if (getCommonSize() > 0) {
			result.append("[ParameterDump]\r\n");
			result.append("0\r\n");
			e = getCommon().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(false, i).getModuleData();
				if (modData.getNoParameters() > 0) {
					result.append("" + modData.getModIndex() + " " + modData.getModType() + " " + modData.getNoParameters() + " ");
					for (j=0; j < modData.getNoParameters(); j++) {
						result.append(modData.getParameterValue(j) + " ");
					}
					result.append("\r\n");
				}
			}
			result.append("[/ParameterDump]\r\n");
//		}
		return result;
	}

	public StringBuffer createCustomDump(StringBuffer result) {
		int i, j = 0;
		Enumeration e;
		ModuleData modData = null;
//		if (getPolySize() > 0) {
			result.append("[CustomDump]\r\n");
			result.append("1\r\n");
			e = getPoly().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(true, i).getModuleData();
				if (modData.getNoCustoms() > 0) {
					result.append("" + modData.getModIndex() + " " + modData.getNoCustoms() + " ");
					for (j=0; j < modData.getNoCustoms(); j++) {
						result.append(modData.getCustomValue(j) + " ");
					}
					result.append("\r\n");
				}
			}
			result.append("[/CustomDump]\r\n");
//		}
		
//		if (getCommonSize() > 0) {
			result.append("[CustomDump]\r\n");
			result.append("0\r\n");
			e = getCommon().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(false, i).getModuleData();
				if (modData.getNoCustoms() > 0) {
					result.append("" + modData.getModIndex() + " " + modData.getNoCustoms() + " ");
					for (j=0; j < modData.getNoCustoms(); j++) {
						result.append(modData.getCustomValue(j) + " ");
					}
					result.append("\r\n");
				}
			}
			result.append("[/CustomDump]\r\n");
//		}
		return result;
	}

	public StringBuffer createNameDump(StringBuffer result) {
		int i = 0;
		Enumeration e;
		ModuleData modData;
//		if (getPolySize() > 0) {
			result.append("[NameDump]\r\n");
			result.append("1\r\n");
			e = getPoly().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(true, i).getModuleData();
				result.append("" + modData.getModIndex() + " " + modData.getModuleName() + "\r\n");
			}
			result.append("[/NameDump]\r\n");
//		}
		
//		if (getCommonSize() > 0) {
			result.append("[NameDump]\r\n");
			result.append("0\r\n");
			e = getCommon().keys();
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				modData = getModule(false, i).getModuleData();
				result.append("" + modData.getModIndex() + " " + modData.getModuleName() + "\r\n");
			}
			result.append("[/NameDump]\r\n");
//		}
		return result;
	}
}
