package net.sf.nmedit.jpatch.randomizer;

import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jpatch.PParameter;

public class VariationState extends JComponent {
	
	
	private static final long serialVersionUID = 1664626117829085748L;
	
	private Vector<Integer> values = new Vector<Integer>(); 
	private boolean valuesDirty = false;
	
	// values of each parameter
	private HashMap<PParameter, Integer> parametersValues = 
		new HashMap<PParameter, Integer>();
	
	private Vector<PParameter> paraVector = new Vector<PParameter>();
		
	public VariationState() {
		super();
	}
	
	public Vector<Integer> getValues() {
		if (valuesDirty) {
			values.clear();
			for (PParameter p: paraVector) {
				values.add(parametersValues.get(p));				
			}
			valuesDirty = false;
			return values;
		}		
		return values;
	}
	
	public void updateValues(Vector<Integer> values) {
    	this.values.clear();
    	
    	for (Integer integer : values) {
			this.values.add(integer);
		}   
	}	
	
	void setParameters(Vector<PParameter> p) {
		paraVector = p;
	}
	
	Vector<PParameter> getParameters() {
		return paraVector;
	}
	
	void setParameterValue(PParameter p, int value) {
		parametersValues.put(p,value);
		valuesDirty = true;
		fireStateChanged();
	}
	
	public void addParameter(PParameter p) {
		paraVector.add(p);
		parametersValues.put(p, p.getValue());
		valuesDirty = true;
		
		fireStateChanged();
	}
	
	public void removeParameter(PParameter p) {
		paraVector.remove(p);
		parametersValues.remove(p);
		valuesDirty = true;
		
		fireStateChanged();
	}
	
	public void addChangeListener(ChangeListener l) {
		
		listenerList.add(ChangeListener.class, l);		
	}
	
	public void removeChangeListener(ChangeListener l)
	{
		
		listenerList.remove(ChangeListener.class, l);
	}

	protected transient ChangeEvent changeEvent; // this is source
    
	void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {			
	        if (listeners[i]==ChangeListener.class) {	        
	            // Lazily create the event:
	            if (changeEvent == null)
	                changeEvent = new ChangeEvent(this);
	            ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
	        }
	    }
	}
	
	public void mutate(VariationState refVar,double range,double probability )
	{
		values = new Vector<Integer>(refVar.getValues());
		
		
		for (int i = 0 ; i  < values.size() ; i++)
		{
			
			if (Math.random() < probability)
			{
				double amplitude = 127*2*range;
				int offset = (int)(Math.random()* amplitude
							-amplitude/2);
				int val = refVar.getValues().get(i) + offset;
				values.set(i, val < 0 ? 0: val > 128 ? 127:val);
			} else {
				values.set(i, refVar.getValues().get(i));
			}
		}	
		
		fireStateChanged();
	}
	
	public void randomize()
	{
		for (int i = 0 ; i  < values.size() ; i++)
		{
			values.set(i, (int)(Math.random()*127));
		}
		fireStateChanged();
	}

	public void setValuesDirty() {		
		valuesDirty = true;
		
	}
}
