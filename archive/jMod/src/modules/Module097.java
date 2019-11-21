package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module097 extends Module {

    private JModKnob pitch = null;
    private JModKnob fine = null;

    private JModToggleButton kbt = null;

    private JModKnob pitchmod1 = null;
    private JModKnob pitchmod2 = null;

    private JModDisplayLabel pitchDisplay = null;
    
    public Module097(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);
        
        pitch = new JModKnob(JModKnob.LARGE, true, true, 74, 12, 0, 127, moduleData.getParameter(0));
        fine = new JModKnob(JModKnob.SMALL, true, true, 112, 19, 0, 127, moduleData.getParameter(1));

        kbt = new JModToggleButton(225, 5, 25, 15, "Kbt", moduleData.getParameter(2));

        pitchmod1 = new JModKnob(JModKnob.SMALL, false, true, 160, 19, 0, 127, moduleData.getParameter(3));
        pitchmod2 = new JModKnob(JModKnob.SMALL, false, true, 200, 19, 0, 127, moduleData.getParameter(4));

        pitchDisplay = new JModDisplayLabel(30, 33, 42, pitch, fine);

        addObjects();
	}
    
    public void addObjects() {
        this.add(pitch);
        this.add(fine);
        this.add(kbt);
        this.add(pitchmod1);
        this.add(pitchmod2);
        
        this.add(pitchDisplay);
    }
}
