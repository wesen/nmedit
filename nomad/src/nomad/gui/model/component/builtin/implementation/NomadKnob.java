package nomad.gui.model.component.builtin.implementation;

import java.awt.Dimension;

/**
 * @author Ian Hoogebom
 * @hidden
 */
public class NomadKnob extends NomadKnobGrafix /*implements ChangeListener*/ {

    public final static int LARGE = 1;
    public final static int SMALL = 2;
    public final static Dimension SIZE_LARGE = new Dimension(30, 30);
    public final static Dimension SIZE_SMALL = new Dimension(25, 25);
    private int type = LARGE;
    
//    public JModKnob() {
//        super();
//    }
    
    public NomadKnob(NomadKnobLook look, int type, boolean ind, boolean lab, int x, int y, int min_val, int max_val/*, Parameter newPar*/) {
        // TODO ?min/max getting the min/max values from the .properties?
        super(look, x, y, min_val, max_val/*, newPar*/);
        
        setLocation(x, y);
        
        indicator = ind;
        label = lab;

        setType(type);
    }
    
    public void setType(int type) {
    	switch (type) {
        	case LARGE: {
        		setSize(SIZE_LARGE);
        		this.type = type;
        		break;
        	}
        	case SMALL: {
        		setSize(SIZE_SMALL);
        		this.type = type; 
        		break;
        	}
            default: {setSize(SIZE_SMALL); 
    			this.type = SMALL;
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
