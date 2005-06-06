package modules;

import main.JModAreaPane;
import main.JModKnob;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module040 extends Module {

    JModKnob vol1 = null;
    JModKnob vol2 = null;
    JModKnob vol3 = null;
    JModKnob vol4 = null;
    JModKnob vol5 = null;
    JModKnob vol6 = null;
    JModKnob vol7 = null;
    JModKnob vol8 = null;
    
    JModToggleButton _6db = null;

	public Module040(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);

        vol1 = new JModKnob(JModKnob.SMALL, false, true, 7, 35, 0, 127, moduleData.getParameter(0));
        vol2 = new JModKnob(JModKnob.SMALL, false, true, 34, 35, 0, 127, moduleData.getParameter(1));
        vol3 = new JModKnob(JModKnob.SMALL, false, true, 61, 35, 0, 127, moduleData.getParameter(2));
        vol4 = new JModKnob(JModKnob.SMALL, false, true, 88, 35, 0, 127, moduleData.getParameter(3));
        vol5 = new JModKnob(JModKnob.SMALL, false, true, 115, 35, 0, 127, moduleData.getParameter(4));
        vol6 = new JModKnob(JModKnob.SMALL, false, true, 142, 35, 0, 127, moduleData.getParameter(5));
        vol7 = new JModKnob(JModKnob.SMALL, false, true, 169, 35, 0, 127, moduleData.getParameter(6));
        vol8 = new JModKnob(JModKnob.SMALL, false, true, 196, 35, 0, 127, moduleData.getParameter(7));

        _6db = new JModToggleButton(222, 19, 28, 15, "-6db", moduleData.getParameter(8));

        addObjects();
	}

    public void addObjects() {
        this.add(vol1);
        this.add(vol2);
        this.add(vol3);
        this.add(vol4);
        this.add(vol5);
        this.add(vol6);
        this.add(vol7);
        this.add(vol8);
        
        this.add(_6db);
    }
}
