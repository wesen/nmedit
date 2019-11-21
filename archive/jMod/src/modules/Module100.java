package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.Module;
import main.Modules;

public class Module100 extends Module {

	public Module100(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);
        
        JModKnob lower = new JModKnob(JModKnob.SMALL, false, true, 95, 20, 0, 127, moduleData.getParameter(1));
        JModKnob upper = new JModKnob(JModKnob.SMALL, false, true, 130, 20, 0, 127, moduleData.getParameter(0));
        this.add(lower);
        this.add(upper);

        JModDisplayLabel lowerLab = new JModDisplayLabel(65, 25, 25, lower, null);
        this.add(lowerLab);
        
        JModDisplayLabel upperLab = new JModDisplayLabel(160, 25, 25, upper, null);
        this.add(upperLab);

	}
}
