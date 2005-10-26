package nomad.gui.model.component.builtin.implementation;




public class JModKnob extends JModKnobGrafix /*implements ChangeListener*/ {

    public final static int LARGE = 1;
    public final static int SMALL = 2;
    private int type = LARGE;
    
//    public JModKnob() {
//        super();
//    }
    
    public JModKnob(int type, boolean ind, boolean lab, int x, int y, int min_val, int max_val/*, Parameter newPar*/) {
        // TODO ?min/max getting the min/max values from the .properties?
        super(x, y, min_val, max_val/*, newPar*/);
        
        setLocation(x, y);
        
        indicator = ind;
        label = lab;

        switch (type) {
            case LARGE: setSize(35, 35); break;
            case SMALL: setSize(25, 25); break;
            default: setSize(35, 35); break;
        }
    }
    
    public void setType(int type) {
    	switch (type) {
        	case LARGE: {
        		setSize(35, 35);
        		this.type = type;
        		break;
        	}
        	case SMALL: {
        		setSize(25, 25);
        		this.type = type; 
        		break;
        	}
    	}
    }
    
    public int getType() {
    	return type;
    }
/*
    public void stateChanged(ChangeEvent e) {
        //int val = (int)((Parameter) e.getSource()).getValue();
        //setValueWithoutFireStarter(val);
    }
    */
}
