package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.Module;
import main.Modules;

public class Module067 extends Module {

	public Module067(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);
        
        JModKnob note = new JModKnob(JModKnob.SMALL, false, true, 120, 5, 0, 127, moduleData.getParameter(0));
        this.add(note);
        
        JModDisplayLabel lab = new JModDisplayLabel(90, 13, 25, note, null);
        this.add(lab);
	}
}
