package nomad.gui.model.component.builtin.implementation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import nomad.misc.JPaintComponent;
import nomad.model.descriptive.DParameter;

public abstract class JModParameterObject extends JPaintComponent {
    
    //private JLabel parLabel = null;
    
    public float internalValue;
    private float MIN_VAL = 0.0F;
    private float MAX_VAL = 127.0F;

    private ChangeEvent changeEvent = null;
    private EventListenerList listenerList = new EventListenerList();

    public JModParameterObject(int min_val, int max_val/*, Parameter newPar*/) {
        setMinValue((float)min_val);
        setMaxValue((float)max_val);
        //setValue(par.getValue()+getMinValue());
    }

    public float getRange() {
        return getMaxValue() - getMinValue();
    }

    public boolean isHalfway() {
        // TODO ?Shouldn't this be at the 'default' position?
        return ((getValue()-getMinValue())*2) == (getRange() + 1);
    }
    
    public float getInternalValue() {
        return internalValue;
    }

    public int getValue() {
        return (int)((internalValue * getRange())+getMinValue());
    }

    public void setMaxValue(float val) {
        MAX_VAL = val;
    }

    public float getMaxValue() {
        return MAX_VAL;
    }

    public void setMinValue(float val) {
        MIN_VAL = val;
    }

    public float getMinValue() {
        return MIN_VAL;
    }

    public void setValue(float val) {
        setInternalValue((val-getMinValue()) / getRange(), true);
    }

    public void setValueWithoutFireStarter(float val) {
        setInternalValue((val-getMinValue()) / getRange(), false);
    }
    
    public DParameter info = null;

    public void setInternalValue(float val, boolean fire) {
        if (val < 0) val = 0;
        if (val > 1) val = 1;
        internalValue = val;
        // calcMisc zit in de paint -> lees paint waarom...
        calcMisc(false); // Do calculation specialy for derived object
        updateKnob();
        if (info!=null)
        this.setToolTipText(info.getName() + ": " + info.getFormattedValue(getValue()));
        //this.setToolTipText(par.getName() + ": " + String.valueOf(getValue()));
        if (fire) fireChangeEvent();
    }
    
    public void calcMisc(boolean custom) {
        // Do calculation specialy for derived object
    }
    
    public void updateKnob() {
        // Not every object needs to repaint() after a value update, otherwise place repaint here
    }
    
    public void addChangeListener(ChangeListener cl) {
        listenerList.add(ChangeListener.class, cl);
    }

    public void removeChangeListener(ChangeListener cl) {
        listenerList.remove(ChangeListener.class, cl);      
    }

    protected void fireChangeEvent() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
           		((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
}
