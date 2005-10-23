package nomad.gui.property;

import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Property {

	/**
	 * The current value
	 */
	private Object value = getDefaultValue();
	
	/**
	 * The objects listening this class
	 */
	private Vector changeListeners = new Vector();

	/**
	 * default constructor
	 */
	public Property() {
		;
	}
	
	/**
	 * Returns the current value.
	 * @return the current value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns a string representation of the current value.
	 * @return a string representation of the current value.
	 *
	public String getStringRepresentation() {
		return ""+value;
	}*/
	
	/**
	 * Sets the current value to value. (And notifies all listeners)
	 * @param value the new Value 
	 * @throws InvalidValueException the value is an unrecognized type,
	 * or it has an illegal state.
	 */
	public void setValue(Object value) throws InvalidValueException {
		setValue(value, null);
	}

	/**
	 * Sets the current value to value. (And notifies all listeners except sender)
	 * @param value the new Value 
	 * @param sender sender will not be notified
	 * @throws InvalidValueException the value is an unrecognized type,
	 * or it has an illegal state.
	 */
	public void setValue(Object value, Object sender) throws InvalidValueException {
		this.value = checkAndNormalizeValue(value);
		notifyListeners(sender);
	}

	/**
	 * Checks the object value. If value is unrecognized, an InvalidValueException is
	 * raised. Else the method returns the internal representation of the object value.
	 * 
	 * @param value the value to check
	 * @return internal representation of value
	 * @throws InvalidValueException
	 */
	protected Object checkAndNormalizeValue(Object value) throws InvalidValueException {
		return value; // nothing to do, yet
	}
	
	/**
	 * Returns the default value of this property
	 * @return the default value
	 */
	public Object getDefaultValue() {
		return null;
	}

	/**
	 * Returns true if the getOptions method is implemented and returns an array.
	 * @return true if the getOptions method is implemented and returns an array.
	 */
	public boolean hasOptions() {
		return getOptions()!=null;
	}
	
	/**
	 * Returns an array containing all possibilities for the value.
	 * @return an array containing all possibilities for the value.
	 */
	public Object[] getOptions() {
		return null;
	}
	
	/**
	 * Adds a changelistener to this property
	 * @param listener the listener object
	 */
	public void addChangeListener(ChangeListener listener) {
		if (!changeListeners.contains(listener))
			changeListeners.add(listener);
	}
	
	/**
	 * Removes a changelistener from this property
	 * @param listener the listener object
	 */
	public void removeChangeListener(ChangeListener listener) {
		if (changeListeners.contains(listener))
			changeListeners.remove(listener);
	}
	
	/**
	 * Notifies all listeners except sender
	 * @param sender excluded listener
	 */
	protected void notifyListeners(Object sender) {
		ChangeEvent event = new ChangeEvent(this);
		for (int i=0;i<changeListeners.size();i++) {
			ChangeListener listener = (ChangeListener)changeListeners.get(i);
			if (sender!=listener)
				listener.stateChanged(event);
		}
	}

}
