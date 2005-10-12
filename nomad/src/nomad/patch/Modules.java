package nomad.patch;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Hashtable;

import nomad.gui.NomadModuleSection.ModulesSectionType;

public class Modules {

    public final static class ModulePixDimension {
        final public static int PIXWIDTH = 256;
        final public static int PIXHEIGHT = 16;
    }

    private int maxGridXPoly = 0;
    private int maxGridYPoly = 0;

    private int maxGridXCommon = 0;
    private int maxGridYCommon = 0;

    // Using Hashtables, we will search for modules on module number
	private Hashtable poly = null;
	private Hashtable common = null;
    
    Patch patch = null;
	
	Modules(Patch patch) {
		poly = new Hashtable();
		common = new Hashtable();
        
        this.patch = patch;
	}
	
    public int getMaxWidth(int moduleSection) {
        return (moduleSection == ModulesSectionType.POLY?maxGridXPoly:maxGridXCommon) * ModulePixDimension.PIXWIDTH;
    }

    public int getMaxHeight(int moduleSection) {
        return (moduleSection == ModulesSectionType.POLY?maxGridYPoly:maxGridYCommon) * ModulePixDimension.PIXHEIGHT;
    }

	public Module addModule(int moduleSection, String params) {
		String[] paramArray = new String[4];
		paramArray = params.split(" ");
		
		Module mod;

		Integer pchIndex = new Integer(paramArray[0]);
		int type = Integer.parseInt(paramArray[1]);
		int gridX = Integer.parseInt(paramArray[2]);
		int gridY = Integer.parseInt(paramArray[3]);
		
        mod = new Module(pchIndex, type, gridX, gridY);

        if (moduleSection == ModulesSectionType.POLY) {
            poly.put(pchIndex, mod);
            if (gridX + mod.getGridWidth() > maxGridXPoly)
                maxGridXPoly = gridX + mod.getGridWidth();
            if (gridY + mod.getGridHeight() > maxGridYPoly)
                maxGridYPoly = gridY + mod.getGridHeight();
        }
        else {
            common.put(pchIndex, mod);
            if (gridX + mod.getGridWidth() > maxGridXCommon)
                maxGridXCommon = gridX + mod.getGridWidth();
            if (gridY + mod.getGridHeight() > maxGridYCommon)
                maxGridYCommon = gridY + mod.getGridHeight();
        }
        
		return mod;
	}

	public void removeModule(Integer modIndex, int moduleSection) {
	    patch.getCables().removeCablesFromModule(modIndex.intValue(), moduleSection);
	    if (moduleSection == ModulesSectionType.POLY)
	        poly.remove(modIndex);
	    else
	        common.remove(modIndex);
    }
    
	public void addModuleName(int moduleSection, String params) {
		String[] sa = new String[2];
		sa = params.split(" ", 2);
		getModule(moduleSection, Integer.parseInt(sa[0])).setModuleName(sa[1]);
	}
	
	public void addParameter(int moduleSection, String params) {
		String[] sa = new String[2];
		sa = params.split(" ",2);
		getModule(moduleSection, Integer.parseInt(sa[0])).setParameters(params);
	}

	public void addCustom(int moduleSection, String params) {
		String[] sa = new String[2];
		sa = params.split(" ",2);
		getModule(moduleSection, Integer.parseInt(sa[0])).setCustoms(params);
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
    
	public Module getModule(int moduleSection, int index) {
		Module returnMod;
		returnMod = (Module) null;
		if (moduleSection == ModulesSectionType.POLY)
			returnMod = (Module) poly.get(new Integer(index));
		else
			returnMod = (Module) common.get(new Integer(index));
		return returnMod;
	}

// Read .pch sections

	public void readModuleDump(BufferedReader pchFile) {
		String dummy = "";
		int moduleSection;
		try {
			if (pchFile.readLine().trim().compareTo("1") == 0)
                moduleSection = ModulesSectionType.POLY;
			else
                moduleSection = ModulesSectionType.COMMON;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/ModuleDump]") != 0)
					addModule(moduleSection, dummy);
				else
					return;
			}
			return; // End of file?
		}
		catch(Exception e) {
			System.out.println(e + " in readModuleDump " + dummy);
		}
	}

	public void readNameDump(BufferedReader pchFile) {
		String dummy = "";
        int moduleSection;
		try {
            if (pchFile.readLine().trim().compareTo("1") == 0)
                moduleSection = ModulesSectionType.POLY;
            else
                moduleSection = ModulesSectionType.COMMON;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/NameDump]") != 0)
					addModuleName(moduleSection, dummy);
				else
					return;
			}
			return; // End of file?
		}
		catch(Exception e) {
			System.out.println(e + " in readNameDump " + dummy);
		}
	}

	public void readParameterDump(BufferedReader pchFile) {
		String dummy = "";
        int moduleSection;
		try {
            if (pchFile.readLine().trim().compareTo("1") == 0)
                moduleSection = ModulesSectionType.POLY;
            else
                moduleSection = ModulesSectionType.COMMON;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/ParameterDump]") != 0)
					addParameter(moduleSection, dummy);
				else
					return;
			}
			return; // End of file?
		}
		catch(Exception e) {
			System.out.println(e + " in readParameterDump " + dummy);
		}
	}

	public void readCustomDump(BufferedReader pchFile) {
		String dummy = "";
        int moduleSection;
		try {
            if (pchFile.readLine().trim().compareTo("1") == 0)
                moduleSection = ModulesSectionType.POLY;
            else
                moduleSection = ModulesSectionType.COMMON;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/CustomDump]") != 0)
					addCustom(moduleSection, dummy);
				else
					return;
			}
			return; // End of file?
		}
		catch(Exception e) {
			System.out.println(e + " in readCustomDump " + dummy);
		}
	}

// Creation of .pch

	public StringBuffer createModuleDump(StringBuffer result) {
		int i = 0;
		Module mod = null;

		result.append("[ModuleDump]\r\n");
		result.append("1\r\n");
		Enumeration e = getPoly().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.POLY, i);
			result.append("" + mod.getModIndex() + " " + mod.getModType() + " " + mod.getGridX() + " " + mod.getGridY() + "\r\n");
		}
		result.append("[/ModuleDump]\r\n");

		result.append("[ModuleDump]\r\n");
		result.append("0\r\n");
		e = getCommon().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.COMMON, i);
			result.append("" + mod.getModIndex() + " " + mod.getModType() + " " + mod.getGridX() + " " + mod.getGridY() + "\r\n");
		}
		result.append("[/ModuleDump]\r\n");

		return result;
	}

	public StringBuffer createParameterDump(StringBuffer result) {
		int i, j = 0;
		Enumeration e;
		Module mod = null;

		result.append("[ParameterDump]\r\n");
		result.append("1\r\n");
		e = getPoly().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.POLY, i);
			if (mod.getNoParameters() > 0) {
				result.append("" + mod.getModIndex() + " " + mod.getModType() + " " + mod.getNoParameters() + " ");
				for (j=0; j < mod.getNoParameters(); j++) {
					result.append(mod.getParameterValue(j) + " ");
				}
				result.append("\r\n");
			}
		}
		result.append("[/ParameterDump]\r\n");

		result.append("[ParameterDump]\r\n");
		result.append("0\r\n");
		e = getCommon().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.COMMON, i);
			if (mod.getNoParameters() > 0) {
				result.append("" + mod.getModIndex() + " " + mod.getModType() + " " + mod.getNoParameters() + " ");
				for (j=0; j < mod.getNoParameters(); j++) {
					result.append(mod.getParameterValue(j) + " ");
				}
				result.append("\r\n");
			}
		}
		result.append("[/ParameterDump]\r\n");

		return result;
	}

	public StringBuffer createCustomDump(StringBuffer result) {
		int i, j = 0;
		Enumeration e;
		Module mod = null;

		result.append("[CustomDump]\r\n");
		result.append("1\r\n");
		e = getPoly().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.POLY, i);
			if (mod.getNoCustoms() > 0) {
				result.append("" + mod.getModIndex() + " " + mod.getNoCustoms() + " ");
				for (j=0; j < mod.getNoCustoms(); j++) {
					result.append(mod.getCustomValue(j) + " ");
				}
				result.append("\r\n");
			}
		}
		result.append("[/CustomDump]\r\n");

		result.append("[CustomDump]\r\n");
		result.append("0\r\n");
		e = getCommon().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.COMMON, i);
			if (mod.getNoCustoms() > 0) {
				result.append("" + mod.getModIndex() + " " + mod.getNoCustoms() + " ");
				for (j=0; j < mod.getNoCustoms(); j++) {
					result.append(mod.getCustomValue(j) + " ");
				}
				result.append("\r\n");
			}
		}
		result.append("[/CustomDump]\r\n");

		return result;
	}

	public StringBuffer createNameDump(StringBuffer result) {
		int i = 0;
		Enumeration e;
		Module mod;
		
		result.append("[NameDump]\r\n");
		result.append("1\r\n");
		e = getPoly().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.POLY, i);
			result.append("" + mod.getModIndex() + " " + mod.getModuleName() + "\r\n");
		}
		result.append("[/NameDump]\r\n");
	
		result.append("[NameDump]\r\n");
		result.append("0\r\n");
		e = getCommon().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(ModulesSectionType.COMMON, i);
			result.append("" + mod.getModIndex() + " " + mod.getModuleName() + "\r\n");
		}
		result.append("[/NameDump]\r\n");
		
		return result;
	}
}
