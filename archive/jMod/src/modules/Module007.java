package modules;

import main.JModAreaPane;
import main.JModDisplayLabel;
import main.JModKnob;
import main.JModRadioButton;
import main.JModToggleButton;
import main.Module;
import main.Modules;

public class Module007 extends Module {
    
    private JModKnob freq = null;
    private JModKnob fine = null;
    private JModKnob kbt = null;
    
    private JModKnob pw = null;
    private JModKnob pwmod = null;
    
    private JModKnob pitch1 = null;
    private JModKnob pitch2 = null;
    private JModKnob fma = null;

    private JModToggleButton mute = null;

    private JModRadioButton sqr = null;
    private JModRadioButton tri = null;
    private JModRadioButton sin = null;
    private JModRadioButton saw = null;

    private JModDisplayLabel freqDisplay = null;

	public Module007(Integer newIndex, int newType, int newX, int newY, Modules newModulesRef, boolean newPoly, JModAreaPane newDesktopPane) {
		super(newIndex, newType, newX, newY, newModulesRef, newPoly, newDesktopPane);

        freq = new JModKnob(JModKnob.LARGE, true, true, 74, 19, 0, 127, moduleData.getParameter(0));
        fine = new JModKnob(JModKnob.SMALL, true, true, 112, 26, 0, 127, moduleData.getParameter(1));
        kbt = new JModKnob(JModKnob.SMALL, true, true, 142, 26, 0, 127, moduleData.getParameter(2));
        
        pw = new JModKnob(JModKnob.SMALL, true, true, 184, 17, 0, 127, moduleData.getParameter(3));
        pwmod = new JModKnob(JModKnob.SMALL, false, true, 194, 41, 0, 127, moduleData.getParameter(8));
        
        pitch1 = new JModKnob(JModKnob.SMALL, false, true, 24, 69, 0, 127, moduleData.getParameter(5));
        pitch2 = new JModKnob(JModKnob.SMALL, false, true, 74, 69, 0, 127, moduleData.getParameter(6));
        
        fma = new JModKnob(JModKnob.SMALL, false, true, 124, 69, 0, 127, moduleData.getParameter(7));

        mute = new JModToggleButton(221, 77, 15, 15, "M", moduleData.getParameter(9));

        sqr = new JModRadioButton(223, 15, 27, 15, "Sqa", -1, 3, moduleData.getParameter(4));
        tri = new JModRadioButton(223, 28, 27, 15, "Saw", -1, 2, moduleData.getParameter(4));
        sin = new JModRadioButton(223, 41, 27, 15, "Tri", -1, 1, moduleData.getParameter(4));
        saw = new JModRadioButton(223, 54, 27, 15, "Sin", -1, 0, moduleData.getParameter(4));

        freqDisplay = new JModDisplayLabel(30, 40, 42, freq, fine);
        
        addObjects();
	}
    
    public void addObjects() {
        this.add(freq);
        this.add(fine);
        this.add(kbt);
        this.add(pw);
        this.add(pwmod);
        
        this.add(pitch1);
        this.add(pitch2);
        this.add(fma);
        
        this.add(mute);
        
        this.add(freqDisplay);
        
        this.add(sqr);
        this.add(tri);
        this.add(sin);
        this.add(saw);
    }
}
