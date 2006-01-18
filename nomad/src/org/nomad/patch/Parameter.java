package org.nomad.patch;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.xml.dom.module.DParameter;


public class Parameter {
	private int value;
	private DParameter dParameter = null;
	//private NomadControl port = null;
	private ArrayList changeListeners = new ArrayList();
	//private PortToParam p2p = new PortToParam(this);

	Parameter(DParameter dParameter) {
		this.dParameter = dParameter;
		value = dParameter.getDefaultValue();
//		addChangeListener(p2p);
	}

	public int getValue() {
		return value;
	}

    public void setValue(int newValue) {
    	setValue(this, newValue);
    }

    public void setValue(Object sender, int newValue) {
    	if (value!=newValue) {
    		value = newValue;
    		fireChangeEvent(sender);
    	}
    }
    
    public DParameter getInfo() {
    	return dParameter;
    }
 
    /*public void setUI(NomadControl port) {
    	if (this.port!=null) {
    		this.port.removeChangeListener(p2p);
    	}

		this.p2p.port = port;
    	this.port = port;
    	if (this.port!=null) {
    		this.port.setParameterValue(getValue());
    		this.port.addChangeListener(p2p);
    		// TODO update value
    		// this.port.setParameterValue(getValue());
    	}
    }
    
    public AbstractControlPort getUI() {
    	return this.port;
    }*/
  /*  
    private class PortToParam implements ChangeListener {
    	
    	private Parameter p = null;
    	private AbstractControlPort port = null;
    	
    	public PortToParam(Parameter p) {
    		this.p = p;
    	}
    	
		public void stateChanged(ChangeEvent event) {
			if (event.getSource()==port) {
				p.setValue(this,port.getParameterValue());
			} else {
				if (port!=null) {
					port.removeChangeListener(this);
					port.setParameterValue(p.getValue());
					port.addChangeListener(this);
				}
			}
		}
    	
    }
*/
	public void fireChangeEvent() {
		fireChangeEvent(new ChangeEvent(this));
	}

	public void fireChangeEvent(Object sender) {
		ChangeEvent event = new ChangeEvent(this);
		for (int i=0;i<changeListeners.size();i++) {
			ChangeListener listener = (ChangeListener)changeListeners.get(i);
			if (listener!=sender)
				listener.stateChanged(event);
		}
	}
	
	public void addChangeListener(ChangeListener listener) {
		if (!changeListeners.contains(listener))
			changeListeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		if (changeListeners.contains(listener))
			changeListeners.remove(listener);
	}

	

/*
    public void setValue(Object sender, int newValue) {
        if (bc!=null && sender!=bc)
        	bc.paramChanged(dParameter.getParent().getModuleID(), dParameter.getId(), value);

        System.out.println("param:in:"+newValue);
        if ((port!=null)&&(port!=sender))
        	port.setParameterValue(newValue);
        else
        	System.out.println("buhu, no port, "+port+", "+sender);
    }*/
	
}
