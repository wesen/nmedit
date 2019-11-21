package main;

import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JModScrollBar extends JModParameterObject {

    private JScrollBar scrollBar = null;
    JLabel label = null;
    
    public final static int LARGE = 1;
    public final static int SMALL = 2;
    
//    public JModSlider() {
//        super();
//    }
    
    public JModScrollBar(Object obj, int x, int y, int min_val, int max_val, Parameter newPar) {
        super(min_val, max_val, newPar);
        
//        scrollBar = new JScrollBar(type);
        scrollBar.setSize(80, 16);
        scrollBar.setVisible(true);
        add(scrollBar);
        
        setLocation(x, y);
        setSize(scrollBar.getWidth(), scrollBar.getHeight());
        setVisible(true);

//        switch (type) {
//            case LARGE: setSize(35, 35); break;
//            case SMALL: setSize(25, 25); break;
//            default: setSize(35, 35); break;
//        }
        
        addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = (int)((JModScrollBar) e.getSource()).getValue();
                par.setValueWithoutFireStarter(val);
            }
        });
    }
}
