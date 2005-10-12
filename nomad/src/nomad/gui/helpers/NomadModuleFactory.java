package nomad.gui.helpers;

import nomad.gui.NomadModule;
import nomad.patch.Module;

/**
 * @author Ian Hoogeboom
 *
 * To create a module's GUI.
 * This will read the build-up xml of modules and returns a modlePanel with it's 'knobs and sliders'.
 *  
 * This is to seperate the creation of the GUI from it's 'data' (Module)
 * It returns an NomadModule with a different look for each module type.
 * 
 */
public class NomadModuleFactory {
	public static NomadModule createGUI(Module module) {
		NomadModule modulePanel = null;
		modulePanel = new NomadModule();
    	modulePanel.setLocation(module.getPixLocationX(), module.getPixLocationY());
    	modulePanel.setSize(module.getPixWidth(), module.getPixHeight());
    	modulePanel.setNameLabel(module.getModuleName(), module.getPixWidth());
    	modulePanel.setVisible(true);
		return modulePanel;
	}
}
