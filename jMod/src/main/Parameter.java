package main;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


class Parameter extends JComponent{
	private int value;
	private String name;

    private ChangeEvent changeEvent = null;
    private EventListenerList listenerList = new EventListenerList();
    
    private JModParameterObject jmpo = null;

	Parameter(int newValue, String newName) {
		value = newValue;
		name = newName;

	}

    public void addChangeListener(ChangeListener cl) {
        listenerList.add(ChangeListener.class, cl);
    }
    
    public void removeChangeListener(ChangeListener cl) {
        listenerList.remove(ChangeListener.class, cl);      
    }

    private void fireChangeEvent() {
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
    
    public void setKnob(JModParameterObject new_jmpo) {
        jmpo = new_jmpo;
        jmpo.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int val = (int)((JModParameterObject) e.getSource()).getValue();
                setValue(val);
            }
        });
    }
    
// Getters

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
// Setters

    public void setValue(int newValue) {
        value = newValue;
        fireChangeEvent();
    }   

    public void setValueWithoutFireStarter(int newValue) {
        value = newValue;
    }

	public void setName(String newName) {
		name = newName;
	}	
}
