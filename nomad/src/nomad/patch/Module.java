package nomad.patch;

import java.util.Vector;

import nomad.gui.ModuleGUI;
import nomad.gui.ModuleSectionGUI;
import nomad.gui.helpers.ModuleGUIFactory;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.patch.ModuleSection.ModulePixDimension;

/**
 * @author Ian Hoogeboom
 *
 * The Module class holds all the internal information of the module.
 * A Factory is used to create it's representation.
 * TODO Create an interface to pass to the Factory.
 */
public class Module {

    private String title = "";

    private int modType = 0;
    private Integer modIndex = null;
    
    // where am I on grid?
    private int gridX, gridY;

    private ModuleDescriptions moduleDescriptions = null;
    private ModuleGUI moduleGUI = null; 
    private ModuleSection moduleSection = null;

	private Vector parametersVector = null;
    private Vector customsVector = null;

	public Module(Integer newIndex, int newType, int newGridX, int newGridY, ModuleSection moduleSection, ModuleDescriptions moduleDescriptions) {
		this.moduleDescriptions = moduleDescriptions;
		this.moduleSection = moduleSection;
		
		title = new String();

        modIndex = newIndex;
        modType = newType;
        gridX = newGridX;
        gridY = newGridY;

		parametersVector = new Vector();
		customsVector = new Vector();
	}
    
    public int getModIndex() {
        return modIndex.intValue();
    }
    
    public int getModType() {
        return modType;
    }

    public String getModuleName() {
        return moduleDescriptions.getModuleById(modType).getName();
    }
    
    public int getGridHeight() {
        return moduleDescriptions.getModuleById(modType).getHeight();
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

    public int getPixWidth() {
        return ModulePixDimension.PIXWIDTH;
    }

    public int getPixHeight() {
    	return moduleDescriptions.getModuleById(modType).getHeight() * ModulePixDimension.PIXHEIGHT;
    }

    public void setModuleName(String newName) {
    	title = newName;
    }
    
    public String getModuleTitle() {
        return title;
    }

    public ModuleGUI createModuleGUI(ModuleSectionGUI moduleSectionGUI) {
    	moduleGUI = ModuleGUIFactory.createGUI(this, moduleSectionGUI); 
        return moduleGUI;
    }
    
    public ModuleGUI getModuleGUI() {
        return moduleGUI;
    }
	
    public ModuleSection getModuleSection() {
        return moduleSection;
    }
    
	public void setNewPixLocation(int newPixLocationX, int newPixLocationY) {
		// Coordinates are in pixels, we like to 'snap' to the grid coordinates
		setPixLocationX((newPixLocationX + ModulePixDimension.PIXWIDTHDIV2) / ModulePixDimension.PIXWIDTH);
		setPixLocationY((newPixLocationY + ModulePixDimension.PIXHEIGHTDIV2) / ModulePixDimension.PIXHEIGHT);

		// Update GUI to 'snap' coordinates
		getModuleGUI().setLocation(getPixLocationX(), getPixLocationY());
	}

}
