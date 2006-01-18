package org.nomad.patch;

import java.util.ArrayList;


import org.nomad.patch.ModuleSection.ModulePixDimension;
import org.nomad.theme.ModuleGUI;
import org.nomad.theme.ModuleGUIBuilder;
import org.nomad.theme.ModuleSectionGUI;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.DParameter;

/**
 * @author Ian Hoogeboom
 *
 * The Module class holds all the internal information of the module.
 * A Factory is used to create it's representation.
 * TODO Create an interface to pass to the Factory.
 */
public class Module {

    private String title = "";

    private Integer modIndex = null;
    
    // where am I on grid?
    private int gridX, gridY;

    private DModule dModule = null;
    private ModuleGUI moduleGUI = null; 
    private ModuleSection moduleSection = null;

	private ArrayList parameters = null;
    private ArrayList customs = null;
    private ArrayList connectors = null;

	public Module(Integer newIndex, int newGridX, int newGridY, ModuleSection moduleSection, DModule dModule) {
		this.dModule = dModule;
		this.moduleSection = moduleSection;
		
		title = dModule.getName();

        modIndex = newIndex;
        gridX = newGridX;
        gridY = newGridY;

		parameters = new ArrayList(dModule.getParameterCount());
		customs = new ArrayList(); //TODO add customs
		connectors = new ArrayList(dModule.getConnectorCount());
		
		for (int i = 0;i < dModule.getParameterCount();i++)
			parameters.add(new Parameter(dModule.getParameter(i)));
		
		for (int i = 0;i < dModule.getConnectorCount();i++)
			connectors.add(new Connector(dModule.getConnector(i), 0, 0));
	}
    
	public Connector findConnector(DConnector info) {
		for (int i=0;i<connectors.size();i++) { 
			Connector candidate = (Connector) connectors.get(i);
			if (info.equals(candidate.getInfo()))
				return candidate;
		}
		
		return null; // not found
	}
    
	public Parameter findParameter(DParameter info) {
		for (int i=0;i<parameters.size();i++) { 
			Parameter candidate = (Parameter) parameters.get(i);
			if (info.equals(candidate.getInfo()))
				return candidate;
		}
		
		return null; // not found
	}
	
	public DModule getDModule() {
		return dModule;
	}
	
    public int getModIndex() {
        return modIndex.intValue();
    }
    
    public int getModType() {
        return dModule.getModuleID();
    }

    public String getModuleName() {
        return dModule.getName();
    }
    
    public Parameter getParameter(int index) {
    	return (Parameter)parameters.get(index);
    }
    
    public int getGridHeight() {
        return dModule.getHeight();
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setPixLocationX(int newPixLocationX) {
		gridX = newPixLocationX;
		if (gridX < 0)
			gridX = 0;
	}

    public void setPixLocationY(int newPixLocationY) {
		gridY = newPixLocationY;
		if (gridY < 0)
			gridY = 0;
	}

    public int getPixLocationX() {
        return gridX * ModulePixDimension.PIXWIDTH;
    }

    public int getPixLocationY() {
        return gridY * ModulePixDimension.PIXHEIGHT;
    }

    public int getPixHeight() {
    	return getPixHeight(dModule);
    }
    
    public static int getPixWidth() {
    	return ModulePixDimension.PIXWIDTH;
    }
    
    public static int getPixHeight(DModule dModule) {
    	return dModule.getHeight() * ModulePixDimension.PIXHEIGHT;
    }

    public void setModuleTitle(String newTitle) {
    	title = newTitle;
    }
    
    public String getModuleTitle() {
        return title;
    }

    public ModuleGUI createModuleGUI(ModuleSectionGUI moduleSectionGUI) {
    	moduleGUI = ModuleGUIBuilder.createGUI(this, moduleSectionGUI); 
        return moduleGUI;
    }
    
    public ModuleGUI getModuleGUI() {
        return moduleGUI;
    }
	
    public ModuleSection getModuleSection() {
        return moduleSection;
    }
    
    public void remove() {
    	moduleSection.removeModule(this);
    }
    
	public void setNewPixLocation(int newPixLocationX, int newPixLocationY) {
		// Coordinates are in pixels, we like to 'snap' to the grid coordinates
		setPixLocationX((newPixLocationX + ModulePixDimension.PIXWIDTHDIV2) / ModulePixDimension.PIXWIDTH);
		setPixLocationY((newPixLocationY + ModulePixDimension.PIXHEIGHTDIV2) / ModulePixDimension.PIXHEIGHT);

		// Update GUI to 'snap' coordinates
		if (getModuleGUI() != null)
			getModuleGUI().setLocation(getPixLocationX(), getPixLocationY());
	}

}
