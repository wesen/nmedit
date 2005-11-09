package nomad.gui.model.property;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.xml.XMLFileWriter;

/**
 * A class used to access a components property either by it's real type
 * or using a string representation of the value.
 * 
 * @author Christian Schneider
 */
public abstract class Property {
	
	/** display name of the property */
	private String displayName;
	
	/** the affected component */
	private AbstractUIComponent uicomponent;
	
	/** Change Listeners that will be notified by changes */
	private ArrayList listeners = new ArrayList();
	
	/**
	 * Creates a new Property
	 * @param displayName property name to display in the editor
	 * @param uicomponent the component wrapper
	 */
	public Property(String displayName, AbstractUIComponent uicomponent) {
		this.displayName = displayName;
		this.uicomponent = uicomponent;
	}

	/**
	 * Returns the ui component this property belongs to
	 * @return the ui component this property belongs to
	 */
	public AbstractUIComponent getComponentUI() {
		return uicomponent;
	}
	
	/**
	 * Returns the id of the component
	 * @return the id of the component
	 */
	public abstract String getId();
	
	/**
	 * Returns the display name of the component
	 * @return the display name of the component
	 */
	public String getDisplayName() {
		return displayName!=null?displayName:getId();
	}
	
	/**
	 * Returns the current value
	 * @return the current value
	 */
	public Object getValue() {
		return getInternalValue();
	}

	/**
	 * Returns the component from getComponentUI():BasicUI
	 * @return the component
	 */
	public Component getComponent() {
		return getComponentUI().getComponent();
	}
	
	/**
	 * Sets the value
	 * @param value the new value
	 */
	public void setValue(Object value) {	
		setInternalValue(value instanceof String ? parseString((String)value):value);
	}

	/**
	 * Returns the current component value
	 * @return the current component value
	 */
	protected abstract Object getInternalValue();

	/**
	 * Sets the current component value
	 * @param value the new internal value
	 */
	protected abstract void setInternalValue(Object value);

	/**
	 * Returns the internal value represented as string
	 * @param representation the string representation
	 * @return the object represented by str
	 */
	public Object parseString(String representation) {
		return representation;
	}

	/**
	 * Returns all possible values
	 * @return all possible values
	 */
	public Object[] getAllValues() {
		return null;
	}

	/**
	 * Ads a change listener
	 * If an change event is fired, then getSource() must return a reference
	 * to this Property class.
	 * @param listener the listener
	 */
	public final void addChangeListener(ChangeListener listener) {
		if (!listeners.contains(listener)) 
			listeners.add(listener);
	}

	/**
	 * Ads a change listener
	 * @param listener the listener
	 */
	public final void removeChangeListener(ChangeListener listener) {
		if (listeners.contains(listener)) 
			listeners.remove(listener);
	}

	/**
	 * Sends all change listeners the changeEvent.
	 */
	public final void fireChangeEvent() {
		if (listeners.size()>0) {
			ChangeEvent event = new ChangeEvent(this);
			for (int i=0;i<listeners.size();i++) 
				((ChangeListener)listeners.get(i)).stateChanged(event);
		}
	}

	/**
	 * Sends all change listeners the changeEvent.
	 * @param sender  Sender will not be notified.
	 */
	public final void fireChangeEvent(ChangeListener sender) {
		if (listeners.size()>0) {
			ChangeEvent event = new ChangeEvent(this);
			for (int i=0;i<listeners.size();i++) {
				ChangeListener listener = (ChangeListener)listeners.get(i);
				if (listener!=sender)
					listener.stateChanged(event);
			}
		}
	}
	
	/**
	 * Installs the real listener to the component if install==true. Otherwise
	 * the real listener must be removed
	 * @param install true: install listener
	 */
	public void installComponentListener(boolean install) {
		//
	}
	
	/**
	 * Writes the property to a xml file
	 * @param out the xml file
	 */
	public void writeXMLEntry(XMLFileWriter out) {
		Object defaultValue = getDefaultValue();
		Object value = this.getValue();
		if (defaultValue==null || !defaultValue.equals(value)) {
			out.beginTagStart("property"); // new element
			out.addAttribute("id", ""+this.getId());
			out.addAttribute("value", getAsXMLValue(value));
			out.beginTagFinish(false); // finish element
		}
	}

	protected String getAsXMLValue(Object value) {
		return value.toString();
	}
	
	public Object getDefaultValue() {
		return null;
	}
	
}
