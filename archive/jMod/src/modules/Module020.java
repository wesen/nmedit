package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModRadioButton;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module020 extends Module {

    private JModKnob A = null;
    private JModKnob D = null;
    private JModKnob S = null;
    private JModKnob R = null;
    
    private JModDisplayLabel Alabel = null;
    private JModDisplayLabel Dlabel = null;
    private JModDisplayLabel Slabel = null;
    private JModDisplayLabel Rlabel = null;

    private JModRadioButton log = null;
    private JModRadioButton lin = null;
    private JModRadioButton exp = null;

    private JModToggleButton inv = null;

	public Module020(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);

        A = new JModKnob(JModKnob.SMALL, false, false, 62, 50, 0, 127, moduleData.getParameter(1));
        D = new JModKnob(JModKnob.SMALL, false, false, 99, 50, 0, 127, moduleData.getParameter(2));
        S = new JModKnob(JModKnob.SMALL, false, false, 136, 50, 0, 127, moduleData.getParameter(3));
        R = new JModKnob(JModKnob.SMALL, false, false, 173, 50, 0, 127, moduleData.getParameter(4));
        
        Alabel = new JModDisplayLabel(64, 40, 23, A, null);
        Dlabel = new JModDisplayLabel(101, 40, 23, D, null);
        Slabel = new JModDisplayLabel(138, 40, 23, S, null);
        Rlabel = new JModDisplayLabel(175, 40, 23, R, null);

        log = new JModRadioButton(62, 17, 27, 15, "Log", -1, 0, moduleData.getParameter(0));
        lin = new JModRadioButton(87, 17, 27, 15, "Lin", -1, 1, moduleData.getParameter(0));
        exp = new JModRadioButton(112, 17, 27, 15, "Exp", -1, 2, moduleData.getParameter(0));

        inv = new JModToggleButton(150, 17, 30, 15, "Inv", moduleData.getParameter(5));
        
        addObjects();
	}
    
    public void addObjects() {
        this.add(A);
        this.add(D);
        this.add(S);
        this.add(R);

        this.add(Alabel);
        this.add(Dlabel);
        this.add(Slabel);
        this.add(Rlabel);

        this.add(log);
        this.add(lin);
        this.add(exp);
        
        this.add(inv);
    }
}
