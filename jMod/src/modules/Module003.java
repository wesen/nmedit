package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.Module;
import main.Modules;

public class Module003 extends Module {

    public Module003(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
        super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);
        
        JModKnob level = new JModKnob(JModKnob.LARGE, false, true, 212, 8, 0, 127, moduleData.getParameter(0));
        add(level);
        
        JModDisplayLabel lab = new JModDisplayLabel(183, 27, 25, level, null);
        this.add(lab);
    }
}
