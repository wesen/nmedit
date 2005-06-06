package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module096 extends Module {

    private JModKnob freq = null;
    private JModKnob fine = null;
    
    private JModKnob pitch1 = null;
    private JModKnob pitch2 = null;

    private JModKnob timbre = null;
    
    private JModToggleButton mute = null;
    private JModToggleButton kbt = null;

    private JModDisplayLabel freqDisplay = null;
    
    private JModDisplayLabel timbreDisplay = null;

    public Module096(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
        super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);
        
        freq = new JModKnob(JModKnob.LARGE, true, true, 104, 30, 0, 127, moduleData.getParameter(0));
        fine = new JModKnob(JModKnob.SMALL, true, true, 142, 37, 0, 127, moduleData.getParameter(1));
        
        pitch1 = new JModKnob(JModKnob.SMALL, false, true, 24, 38, 0, 127, moduleData.getParameter(5));
        pitch2 = new JModKnob(JModKnob.SMALL, false, true, 74, 38, 0, 127, moduleData.getParameter(6));

        timbre = new JModKnob(JModKnob.SMALL, false, true, 220, 15, 0, 127, moduleData.getParameter(4));

        mute = new JModToggleButton(221, 45, 15, 15, "M", moduleData.getParameter(3));
        kbt = new JModToggleButton(193, 45, 25, 15, "Kbt", moduleData.getParameter(2));

        freqDisplay = new JModDisplayLabel(60, 19, 42, freq, fine);
        
        timbreDisplay = new JModDisplayLabel(194, 23, 25, timbre, null);

        addObjects();
    }
    
    public void addObjects() {
        this.add(freq);
        this.add(fine);
        
        this.add(pitch1);
        this.add(pitch2);

        this.add(timbre);

        this.add(mute);
        this.add(kbt);
        
        this.add(freqDisplay);
        this.add(timbreDisplay);
    }
}
