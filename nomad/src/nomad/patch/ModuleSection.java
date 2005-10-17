package nomad.patch;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JLayeredPane;

import nomad.gui.ModuleGUI;
import nomad.gui.ModuleSectionGUI;
import nomad.model.descriptive.ModuleDescriptions;

public class ModuleSection {

	public final static class ModulesSectionType {
        public final static int COMMON = 0;
        public final static int POLY = 1;
        public final static int MORPH = 2;
    }

    public final static class ModulePixDimension {
        final public static int PIXWIDTH = 256;
        final public static int PIXHEIGHT = 16;
        final public static int PIXWIDTHDIV2 = 128;
        final public static int PIXHEIGHTDIV2 = 8;
    }

    private int moduleSection = ModulesSectionType.POLY;
    private ModuleSectionGUI moduleSectionGUI = null;
    
    private int maxGridX = 0;
    private int maxGridY = 0;

    // Using Hashtables, we will search for modules on module number
	private Hashtable modules = null;
    
    Patch patch = null;
	
	ModuleSection(Patch patch, int moduleSection) {
		modules = new Hashtable();

		moduleSectionGUI = new ModuleSectionGUI();
		this.moduleSection = moduleSection;
        this.patch = patch;
	}
	
    public int getMaxPixWidth() {
        return maxGridX * ModulePixDimension.PIXWIDTH;
    }

    public int getMaxPixHeight() {
        return maxGridY * ModulePixDimension.PIXHEIGHT;
    }

	public Module addModule(String params, ModuleDescriptions moduleDescriptions) {
		String[] paramArray = new String[4];
		paramArray = params.split(" ");
		
		Integer pchIndex = new Integer(paramArray[0]);
		int type = Integer.parseInt(paramArray[1]);
		int gridX = Integer.parseInt(paramArray[2]);
		int gridY = Integer.parseInt(paramArray[3]);
		
        Module mod = new Module(pchIndex, type, gridX, gridY, this, moduleDescriptions);
        mod.createModuleGUI(moduleSectionGUI);

        if (moduleSection == ModulesSectionType.POLY) {
        	modules.put(pchIndex, mod);
            if (gridX + mod.getGridWidth() > maxGridX)
                maxGridX = gridX + mod.getGridWidth();
            if (gridY + mod.getGridHeight() > maxGridY)
                maxGridY = gridY + mod.getGridHeight();
        }
        
		return mod;
	}

	public void removeModule(Integer modIndex) {
	    patch.getCables().removeCablesFromModule(modIndex.intValue(), moduleSection);
	    if (moduleSection == ModulesSectionType.POLY)
	    	modules.remove(modIndex);
    }
    
	public void addModuleName(String params) {
		String[] sa = new String[2];
		sa = params.split(" ", 2);
		getModule(Integer.parseInt(sa[0])).setModuleName(sa[1]);
	}
	
	public void addParameter(String params) {
//		String[] sa = new String[2];
//		sa = params.split(" ",2);
//		getModule(Integer.parseInt(sa[0])).setParameters(params);
		//TODO Set Parameter values
	}

	public void addCustom(String params) {
//		String[] sa = new String[2];
//		sa = params.split(" ",2);
//		getModule(Integer.parseInt(sa[0])).setCustoms(params);
		//TODO Set Custom values
	}
	
	public Hashtable getModules() {
		return modules;
	}

	public int getModulesSize() {
		return modules.size();
	}

	public Module getModule(int index) {
		Module returnMod;
		returnMod = (Module) modules.get(new Integer(index));
		return returnMod;
	}

	public ModuleSectionGUI getModuleSectionGUI() {
		return moduleSectionGUI;
	}

	public void recalcMaxGridXY() {
		Integer i = null;
		Module tempMod = null;
		
        maxGridX = 0;
        maxGridY = 0;
        Enumeration e = null;
        e = modules.keys();
        while (e.hasMoreElements()) {
            i = (Integer)e.nextElement();
            tempMod = (Module)modules.get(i); 
            if (tempMod.getGridX() + tempMod.getGridWidth() > maxGridX)
                maxGridX = tempMod.getGridX() + tempMod.getGridWidth();
            if (tempMod.getGridY() + tempMod.getGridHeight() > maxGridY)
                maxGridY = tempMod.getGridY() + tempMod.getGridHeight();
        }
	}

	public void rearangeModules(Module module) { 
//		Module tempMod, tempMod2 = null;
		ModuleGUI tempModGUI, tempModGUI2 = null;
		Vector modVector = new Vector();

//		Nu alleen voor 1 geplaatste module
		for (int i=0;i<moduleSectionGUI.getComponentCountInLayer(JLayeredPane.DEFAULT_LAYER.intValue());i++) {

			// Seek all modules in the same column
			tempModGUI = ((ModuleGUI)moduleSectionGUI.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER.intValue())[i]);
			if (module.getGridX() == tempModGUI.getModule().getGridX()) {
				// aanvullen tijdelijke moduleVector met null voor iedere moduleVoogte
				
				while (modVector.size() <= tempModGUI.getModule().getGridY())
					modVector.add(null);

				if (modVector.get(tempModGUI.getModule().getGridY()) == null)
					modVector.set(tempModGUI.getModule().getGridY(), tempModGUI);
				else
					modVector.insertElementAt(tempModGUI, tempModGUI.getModule().getGridY());
			}
		}
		
		while (modVector.remove(null));
		
		for (int j=0;j<modVector.size()-1;j++) {
			tempModGUI = (ModuleGUI) modVector.get(j);
			tempModGUI2 = (ModuleGUI) modVector.get(j+1);
			if (tempModGUI2.getModule().getGridY() < (tempModGUI.getModule().getGridY() + tempModGUI.getModule().getGridHeight())) {
				tempModGUI2.getModule().setNewPixLocation(tempModGUI.getModule().getPixLocationX(), tempModGUI.getModule().getPixLocationY() + tempModGUI.getModule().getPixHeight());
			}
		}

		// Update the size of the moduleSection 
		recalcMaxGridXY();
		getModuleSectionGUI().setPreferredSize(new Dimension(module.getModuleSection().getMaxPixWidth(), module.getModuleSection().getMaxPixHeight()));
		getModuleSectionGUI().revalidate();

//		getCables().redrawCables(this, bPoly);
	}
	
// Read .pch sections

	public void readModuleDump(BufferedReader pchFile, ModuleDescriptions moduleDescriptions) {
		String dummy = "";
		try {
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/ModuleDump]") != 0)
					addModule(dummy, moduleDescriptions);
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
//        int moduleSection;
		try {
            if (pchFile.readLine().trim().compareTo("1") == 0)
                moduleSection = ModulesSectionType.POLY;
            else
                moduleSection = ModulesSectionType.COMMON;
				
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/NameDump]") != 0)
					addModuleName(dummy);
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
		try {
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/ParameterDump]") != 0)
					addParameter(dummy);
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
		try {
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/CustomDump]") != 0)
					addCustom(dummy);
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
		result.append("" + moduleSection + "1\r\n");
		Enumeration e = getModules().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(i);
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
		result.append("" + moduleSection + "\r\n");
		e = getModules().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
//			modData = getModule(i).getModuleData();
//			if (modData.getNoParameters() > 0) {
//				result.append("" + modData.getModIndex() + " " + modData.getModType() + " " + modData.getNoParameters() + " ");
//				for (j=0; j < modData.getNoParameters(); j++) {
//					result.append(modData.getParameterValue(j) + " ");
//				}
//				result.append("\r\n");
//			}
		}
		result.append("[/ParameterDump]\r\n");

		return result;
	}

	public StringBuffer createCustomDump(StringBuffer result) {
		int i, j = 0;
		Enumeration e;
		Module mod = null;

		result.append("[CustomDump]\r\n");
		result.append("" + moduleSection + "1\r\n");
		e = getModules().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
//			modData = getModule(true, i).getModuleData();
//			if (modData.getNoCustoms() > 0) {
//				result.append("" + modData.getModIndex() + " " + modData.getNoCustoms() + " ");
//				for (j=0; j < modData.getNoCustoms(); j++) {
//					result.append(modData.getCustomValue(j) + " ");
//				}
//				result.append("\r\n");
//			}
		}
		result.append("[/CustomDump]\r\n");

		return result;
	}

	public StringBuffer createNameDump(StringBuffer result) {
		int i = 0;
		Enumeration e;
		Module mod;
		
		result.append("[NameDump]\r\n");
		result.append("" + moduleSection + "1\r\n");
		e = getModules().keys();
		while (e.hasMoreElements()) {
			i = ((Integer) e.nextElement()).intValue();
			mod = getModule(i);
			result.append("" + mod.getModIndex() + " " + mod.getModuleName() + "\r\n");
		}
		result.append("[/NameDump]\r\n");
	
		return result;
	}
}
