package modules;

import main.JModAreaPane;
import main.JModKnob;
import main.Module;
import main.Modules;

public class Module019 extends Module {

    JModKnob vol1 = null;
    JModKnob vol2 = null;
    JModKnob vol3 = null;
    
	public Module019(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);
        
        vol1 = new JModKnob(JModKnob.SMALL, false, true, 90, 5, 0, 127, moduleData.getParameter(0));
        vol2 = new JModKnob(JModKnob.SMALL, false, true, 150, 5, 0, 127, moduleData.getParameter(1));
        vol3 = new JModKnob(JModKnob.SMALL, false, true, 205, 5, 0, 127, moduleData.getParameter(2));
        
        addObjects();
	}
    
    public void addObjects() {
        this.add(vol1);
        this.add(vol2);
        this.add(vol3);
    }
}
