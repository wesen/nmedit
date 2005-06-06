package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModRadioButton;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module005 extends Module {

    JModKnob level = null;
    JModToggleButton mute = null;
    JModDisplayLabel lab = null;
    
    JModRadioButton b1 = null;
    JModRadioButton b2 = null;
    JModRadioButton b3 = null;
    JModRadioButton b4 = null;
    JModRadioButton b5 = null;
    JModRadioButton b6 = null;
    
    public Module005(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
        super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);

        level = new JModKnob(JModKnob.LARGE, false, true, 212, 8, 0, 127, moduleData.getParameter(0));
        mute = new JModToggleButton(135, 23, 15, 15, "M", moduleData.getParameter(2));

        b1 = new JModRadioButton(31, 23, 17, 15, "1", -1, 0, moduleData.getParameter(1));
        b2 = new JModRadioButton(46, 23, 18, 15, "2", -1, 1, moduleData.getParameter(1));
        b3 = new JModRadioButton(62, 23, 18, 15, "3", -1, 2, moduleData.getParameter(1));
        b4 = new JModRadioButton(78, 23, 18, 15, "4", -1, 3, moduleData.getParameter(1));
        b5 = new JModRadioButton(94, 23, 18, 15, "L", -1, 4, moduleData.getParameter(1));
        b6 = new JModRadioButton(109, 23, 18, 15, "R", -1, 5, moduleData.getParameter(1));
        
        lab = new JModDisplayLabel(183, 27, 25, level, null);
        
        addObjects();
    }
    
    public void addObjects() {
        this.add(level);
        this.add(mute);
        this.add(lab);
        
        this.add(b1);
        this.add(b2);
        this.add(b3);
        this.add(b4);
        this.add(b5);
        this.add(b6);
    }
}
