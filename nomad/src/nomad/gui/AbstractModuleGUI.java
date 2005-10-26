package nomad.gui;

import javax.swing.JPanel;

public class AbstractModuleGUI extends JPanel {

	private ModuleGUIComponents components = new ModuleGUIComponents();
	
	public AbstractModuleGUI() {
		//
	}
    
    public ModuleGUIComponents getModuleComponents() {
    	return components;
    }
}
