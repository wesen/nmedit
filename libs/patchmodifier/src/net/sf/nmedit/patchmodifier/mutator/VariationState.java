package net.sf.nmedit.patchmodifier.mutator;

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
		
	private boolean isSelected = false;
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		fireStateChanged();
	}

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
		    	
    	for (int i = 0; i < values.size(); i ++) {    		
    		PParameter param = paraVector.get(i);
			this.parametersValues.put(param, values.get(i));
		}  
    	valuesDirty = true;
    	
    	fireStateChanged();
	}	
	
	void setParameters(Vector<PParameter> p) {
		paraVector = p;
	}
	
	Vector<PParameter> getParameters() {
		return paraVector;
	}
	
	public HashMap<PParameter, Integer> getParametersValues() {
		return parametersValues;
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
			
		for (PParameter param: paraVector)
		{
			
			if (Math.random() < probability)
			{
				double amplitude = 127*2*range;
				int offset = (int)(Math.random()* amplitude
							-amplitude/2);
				int val = refVar.getParametersValues().get(param) + offset;
				parametersValues.put(param, val < 0 ? 0: val > 128 ? 127:val);
			} 
		}	
		
		valuesDirty = true;
		
		fireStateChanged();
	}
	
	public void randomize()
	{
		for (int i = 0 ; i  < values.size() ; i++)
		{
			for (PParameter p: paraVector){
				parametersValues.put(p, (int)(Math.random()*127));
			}
			
		}
		valuesDirty = true;
		fireStateChanged();
	}

	public void setValuesDirty() {		
		valuesDirty = true;
		
	}
}
