package nomad.patch;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JLayeredPane;

import nomad.gui.ModuleSectionGUI;
import nomad.model.descriptive.DModule;
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

		moduleSectionGUI = new ModuleSectionGUI(this);
		this.moduleSection = moduleSection;
        this.patch = patch;
	}
	
    public int getMaxPixWidth() {
        return maxGridX * ModulePixDimension.PIXWIDTH;
    }

    public int getMaxPixHeight() {
        return maxGridY * ModulePixDimension.PIXHEIGHT;
    }

    public Module addModuleAfterDrop(DModule dModule, int x, int y) {
    	Integer pchIndex = new Integer(getModulesMaxIndex() + 1);
    	
        Module mod = new Module(pchIndex, 0, 0, this, dModule);

//        mod.setNewPixLocation(x, y);
        // Use a slightly other algorithm
		mod.setPixLocationX(x / ModulePixDimension.PIXWIDTH);
		mod.setPixLocationY(y / ModulePixDimension.PIXHEIGHT);

    	modules.put(pchIndex, mod);
        if (mod.getGridX() + mod.getGridWidth() > maxGridX)
            maxGridX = mod.getGridX() + mod.getGridWidth();
        if (mod.getGridY() + mod.getGridHeight() > maxGridY)
            maxGridY = mod.getGridY() + mod.getGridHeight();

        mod.createModuleGUI(moduleSectionGUI);
        
        rearangeModules(mod);

        moduleSectionGUI.add(mod.getModuleGUI(), JLayeredPane.DEFAULT_LAYER.intValue());
        
    	return mod;
    }
    
	public Module addModule(String params) {
		String[] paramArray = new String[4];
		paramArray = params.split(" ");
		
		Integer pchIndex = new Integer(paramArray[0]);
		int type = Integer.parseInt(paramArray[1]);
		int gridX = Integer.parseInt(paramArray[2]);
		int gridY = Integer.parseInt(paramArray[3]);
		
        Module mod = new Module(pchIndex, gridX, gridY, this, ModuleDescriptions.model.getModuleById(type));
//        mod.createModuleGUI(moduleSectionGUI);

//        if (moduleSection == ModulesSectionType.POLY) {
        	modules.put(pchIndex, mod);
            if (gridX + mod.getGridWidth() > maxGridX)
                maxGridX = gridX + mod.getGridWidth();
            if (gridY + mod.getGridHeight() > maxGridY)
                maxGridY = gridY + mod.getGridHeight();
//        }
        
		return mod;
	}

	public void removeModule(Integer modIndex) {
	    patch.getCables().removeCablesFromModule(modIndex.intValue(), moduleSection);
	    if (moduleSection == ModulesSectionType.POLY)
	    	modules.remove(modIndex);
    }
    
	public void addModuleTitle(String params) {
		String[] sa = new String[2];
		sa = params.split(" ", 2);
		getModule(Integer.parseInt(sa[0])).setModuleTitle(sa[1]);
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

	public int getModulesMaxIndex() {
		Enumeration e = modules.keys();
		int i = 0;
		while (e.hasMoreElements()) {
			i = Math.max(((Integer)e.nextElement()).intValue(), i);
		}
		return i;
	}
	
	public int getModulesMaxIndexInRow(int row) {
		// get the max moduleIndex from the given grid row (y) 
		Enumeration e = modules.keys();
		int i = 0;
		Integer t = null;
		while (e.hasMoreElements()) {
			t = (Integer)e.nextElement();
			if (((Module)modules.get(t)).getGridY() == row);
				i = Math.max((t).intValue(), i);
		}
		return i;
	}

	public Module getModule(int moduleIndex) {
		// moduleIndex is the ID of the module in the .pch file, not the 'index' in the Hashtable!!!
		Module returnMod;
		returnMod = (Module) modules.get(new Integer(moduleIndex));
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
        
		getModuleSectionGUI().setPreferredSize(new Dimension(getMaxPixWidth(), getMaxPixHeight()));
		getModuleSectionGUI().revalidate();
	}

	private void printList(List modList) {
		// for debugging
		System.out.println();
		for(int i=0;i<modList.size();i++) {
			if (modList.get(i) != null) {
				Module mod = (Module)modList.get(i);
				System.out.println(mod.getModuleTitle() + ":" + mod.getGridY() + ":" + (mod.getGridHeight()+mod.getGridY()));
			}
			else {
				System.out.println(i + ":Leeg");
			}
		}
	}
	
	public void rearangeModules(Module module) {
		Module tempMod, tempMod2 = null;

		// The size of the temp modList won't exceed maxGrid 
		int size = Math.max(module.getGridY(), maxGridY) + 1;
		
		List modList = new ArrayList(size);
		
		// We need to fill the list, the initial capacity does not force a fill, just 'speed'.
		for (int i=0;i<size;i++)
			modList.add(null);

		//	Walk through all modules 
		for (int i=0;i<getModulesMaxIndexInRow(module.getGridY()) + 1;i++) {
			
			// Seek all modules in the same column (gridX)
			tempMod = getModule(i);
			if (tempMod != null && module.getGridX() == tempMod.getGridX()) {

				/**
				 * We build a list with every position is a 'row'
				 * Then we fill the list with the modules on the gridY position
				 * 
				 * modList[0] = module1 (y = 0, height = 3) 
				 * modList[1] = null
				 * modList[2] = module4 (y = 2, height = 2) (new placed module)
				 * modList[3] = module2 (y = 3, height = 2)
				 * modList[4] = null
				 * modList[5] = null
				 * modList[6] = null
				 * modList[7] = module3 (y = 7, height = 3)
				 * modList[n] = ...
				 */
				
				if (modList.get(tempMod.getGridY()) == null)
					modList.set(tempMod.getGridY(), tempMod);
				else
					modList.add(tempMod.getGridY(), tempMod);
			}
		}

		/**
		 * Clear the nulls
		 * 
		 * modList[0] = module1 (y = 0, height = 3) 
		 * modList[1] = module4 (y = 2, height = 2) (new placed module)
		 * modList[2] = module2 (y = 3, height = 2)
		 * modList[3] = module3 (y = 7, height = 3)
		 */

		Collection c = new ArrayList(1);
		c.add(null);
		modList.removeAll(c);

		/**
		 * Update the Y positions
		 * 
		 * modList[0] = module1 (y = 0, height = 3) 
		 * modList[1] = module4 (y = 3, height = 2) 
		 * modList[2] = module2 (y = 5, height = 2)
		 * modList[3] = module3 (y = 7, height = 3)
		 */

		for (int j=0;j<modList.size()-1;j++) {
			tempMod = (Module) modList.get(j);
			tempMod2 = (Module) modList.get(j+1);
			if (tempMod2.getGridY() < (tempMod.getGridY() + tempMod.getGridHeight())) {
				tempMod2.setNewPixLocation(tempMod.getPixLocationX(), tempMod.getPixLocationY() + tempMod.getPixHeight());
			}
		}

		// Update the size of the moduleSection 
		recalcMaxGridXY();

//		getCables().redrawCables(this, bPoly);
	}
	
// Read .pch sections

	public void readModuleDump(BufferedReader pchFile) {
		String dummy = "";
		try {
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/ModuleDump]") != 0)
					addModule(dummy);
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
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/NameDump]") != 0)
					addModuleTitle(dummy);
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
