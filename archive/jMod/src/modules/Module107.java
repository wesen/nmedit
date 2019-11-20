package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModRadioButton;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module107 extends Module {

    private JModKnob freq = null;
    private JModKnob fine = null;
    private JModToggleButton kbt = null;
    
    private JModKnob sp = null;
    private JModKnob spmod = null;
    
    private JModKnob pitch1 = null;
    private JModKnob pitch2 = null;
    private JModKnob fma = null;

    private JModToggleButton mute = null;

    private JModRadioButton odd = null;
    private JModRadioButton all = null;

    private JModDisplayLabel freqDisplay = null;
    
	public Module107(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);
        freq = new JModKnob(JModKnob.LARGE, true, true, 104, 13, 0, 127, moduleData.getParameter(0));
        fine = new JModKnob(JModKnob.SMALL, true, true, 142, 20, 0, 127, moduleData.getParameter(1));
        kbt = new JModToggleButton(225, 20, 25, 15, "Kbt", moduleData.getParameter(8));
        
        sp = new JModKnob(JModKnob.SMALL, true, true, 184, 13, 0, 127, moduleData.getParameter(2));
        spmod = new JModKnob(JModKnob.SMALL, false, true, 194, 36, 0, 127, moduleData.getParameter(7));
        
        pitch1 = new JModKnob(JModKnob.SMALL, false, true, 24, 53, 0, 127, moduleData.getParameter(4));
        pitch2 = new JModKnob(JModKnob.SMALL, false, true, 74, 53, 0, 127, moduleData.getParameter(5));
        
        fma = new JModKnob(JModKnob.SMALL, false, true, 124, 53, 0, 127, moduleData.getParameter(6));

        mute = new JModToggleButton(221, 61, 15, 15, "M", moduleData.getParameter(9));

        odd = new JModRadioButton(159, 61, 27, 15, "Odd", -1, 0, moduleData.getParameter(3));
        all = new JModRadioButton(184, 61, 27, 15, "All", -1, 1, moduleData.getParameter(3));

        freqDisplay = new JModDisplayLabel(60, 30, 42, freq, fine);
        
        addObjects();
    }
    
    public void addObjects() {
        this.add(freq);
        this.add(fine);
        this.add(kbt);
        this.add(sp);
        this.add(spmod);
        
        this.add(pitch1);
        this.add(pitch2);
        this.add(fma);
        
        this.add(mute);
        
        this.add(freqDisplay);
        
        this.add(odd);
        this.add(all);
    }
}
