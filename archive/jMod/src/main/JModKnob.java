package main;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JModKnob extends JModKnobGrafix {

    public final static int LARGE = 1;
    public final static int SMALL = 2;
    
//    public JModKnob() {
//        super();
//    }
    
    public JModKnob(int type, boolean ind, boolean lab, int x, int y, int min_val, int max_val, Parameter newPar) {
        // TODO ?min/max getting the min/max values from the .properties?
        super(x, y, min_val, max_val, newPar);
        
        setLocation(x, y);
        
        indicator = ind;
        label = lab;

        switch (type) {
            case LARGE: setSize(35, 35); break;
            case SMALL: setSize(25, 25); break;
            default: setSize(35, 35); break;
        }
        
          par.addChangeListener(new ChangeListener() {
              public void stateChanged(ChangeEvent e) {
                 int val = (int)((Parameter) e.getSource()).getValue();
                 setValueWithoutFireStarter(val);
             }
         });
    }
}
