package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module009 extends Module {
    
    private JModKnob freq = null;
    private JModKnob fine = null;
    private JModToggleButton kbt = null;
    
    private JModKnob freqmod = null;
    private JModKnob fma = null;
    
    private JModToggleButton mute = null;

    private JModDisplayLabel freqDisplay = null;

	public Module009(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);

        freq = new JModKnob(JModKnob.LARGE, true, true, 94, 26, 0, 127, moduleData.getParameter(0));
        fine = new JModKnob(JModKnob.SMALL, true, true, 132, 33, 0, 127, moduleData.getParameter(1));
        kbt = new JModToggleButton(225, 15, 25, 15, "Kbt", moduleData.getParameter(2));
        
        freqmod = new JModKnob(JModKnob.SMALL, false, true, 59, 33, 0, 127, moduleData.getParameter(3));

        fma = new JModKnob(JModKnob.SMALL, false, true, 180, 33, 0, 127, moduleData.getParameter(4));
        
        mute = new JModToggleButton(221, 45, 15, 15, "M", moduleData.getParameter(5));

        freqDisplay = new JModDisplayLabel(50, 20, 42, freq, fine);

        addObjects();
	}
    
    public void addObjects() {
        this.add(freq);
        this.add(fine);
        this.add(kbt);
        
        this.add(freqmod);
        this.add(fma);
        
        this.add(mute);
        
        this.add(freqDisplay);
    }
}
