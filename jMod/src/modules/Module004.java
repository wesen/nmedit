package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModRadioButton;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module004 extends Module {

    JModKnob level = null;
    JModToggleButton mute = null;
    JModDisplayLabel lab = null;
    
    JModRadioButton b1 = null;
    JModRadioButton b2 = null;
    JModRadioButton b3 = null;
    
	public Module004(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);

        level = new JModKnob(JModKnob.LARGE, false, true, 212, 8, 0, 127, moduleData.getParameter(0));
        mute = new JModToggleButton(107, 23, 15, 15, "M", moduleData.getParameter(2));
        lab = new JModDisplayLabel(183, 27, 25, level, null);

        b1 = new JModRadioButton(31, 23, 23, 15, "1/2", -1, 0, moduleData.getParameter(1));
        b2 = new JModRadioButton(52, 23, 25, 15, "3/4", -1, 1, moduleData.getParameter(1));
        b3 = new JModRadioButton(75, 23, 29, 15, "CVA", -1, 2, moduleData.getParameter(1));

        addObjects();
    }

    public void addObjects() {
        this.add(level);
        this.add(mute);
        this.add(lab);
        
        this.add(b1);
        this.add(b2);
        this.add(b3);
	}
}
