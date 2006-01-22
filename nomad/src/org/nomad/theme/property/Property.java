/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Jan 6, 2006
 */
package org.nomad.theme.property;

import java.util.HashMap;
import java.util.regex.Pattern;


import org.nomad.theme.ModuleComponent;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.editor.PropertyEditor;
import org.nomad.xml.XMLFileWriter;
import org.nomad.xml.dom.module.DModule;

/**
 * <p>The class Property describes the interface between any user interface component
 * which is be part of the patch-module view and outer parts.
 * The class allows read and write access to important properties so that their
 * state can be made persistent by writing them to a configuration file.</p>
 *
 * <p><b>Usage:</b> To implement a property for a custom type you have to
 * <ul>
 * 	<li>define the name of the property</li>
 * 	<li>add the new type handler</li>
 * 	<li>define the string handler</li>
 * 	<li>support for reading the components property</li>
 * 	<li>optional: advance the property to string transformation</li>
 * 	<li>optional: define a custom editor</li>
 * </ul>
 * </p>
 * 
 * <b>Example</b>
 * <p>
 * For example imagine a property for the components size:<br/>
 * <pre><code>
 * public class ComponentSizeProperty extends Property {
 * 
 *   public Property(NomadComponent component) {
 *   	setName("size"); // <b>defines the name</b>
 *   	setHandler(      // <b>define a handler for class Dimension</b>
 *        Dimension.class,
 *          new PropertyValueWriter(){
 *            public void writeValue(Object value) {
 *              getComponent().setSize((Dimension)value);
 *            }
 *          }
 *      ); // setHandler()
 *   }
 *   
 *   //<b>define the string handler</b>
 *   public void setValueFromString(String value) {
 *   	Dimension size = ...;// decode 'x,y' the String (see getValueString())
 *      setValue(size);      // will call the Dimension.class handler
 *   }
 *   
 *   //<b>support for reading the components property</b>
 *   public Object getValue() {
 *     return getComponent().getSize(); // returns Dimension 
 *   }
 *   
 *   //The Dimension.toString() method does not provide a usable string representation for the Dimension.
 *   //<b>optional: advance the property to string transformation</b>
 *   public String getValueString() {
 *     // return a string of type "x,y" where x and y are integers
 *     return getComponent().getWidth()+","+getComponent().getHeight();
 *   }
 * 
 * }
 * </code></pre>
 * </p>
 * 
 * <p>Properties will be accessed by either the visual editor or the factory and the configuration
 * file. Each property must provide a string representation of it's value and be able to  
 * set it's value from that string. This is made possible with the methods 
 * <code>public abstract void setValueFromString(String value);</code> and
 * <pre><code>  public String getValueString() {
 *      return ""+getValue();
 *  }</code></pre>
 * Each property has an preinstalled handler for the String class which will set the value
 * by invoking setValueFromString(String).
 * </p> 
 *
 * <p>To help with integration into the visual editor the method
 * <code>public PropertyEditor getValueEditor(Object owner)</code> returns
 * a class that is used by the editor to create the swing component that
 * allowes the user to modify the properties the value.
 * </p>
 *
 * @see org.nomad.theme.component.NomadComponent#getAccessibleProperties()
 * @see #getValue()
 * @see #setValueFromString(String)
 * @see #getValueString()
 * @see #getEditor(Object)
 * 
 * @author Christian Schneider
 */
public abstract class Property extends PropertyContainer {

	// ---- Property name + validation --------------------------------------
	
	/**
	 * Used to validate the properties name.
	 * @see #setName(String)
	 */
	private final static Pattern validateNamePattern = Pattern.compile("[:a-zA-Z][,#a-zA-Z0-9:\\.\\-]*");

	/**
	 * The properties name
	 * @see #setName(String)
	 */
	private String name = null;
	
	private boolean flagValidateName = false;
	
	/**
	 * Returns the name of this property.
	 * @return the name of this property
	 * @see #setName(String)
	 */
	public String getName() {
		return name;
	}
	
	public boolean isValidatingName() {
		return flagValidateName;
	}
	
	public void setValidatingName(boolean validate) {
		flagValidateName = validate;
	}

	/**
	 * Sets the name of the property
	 * The name must match the regular expression <code>[:a-zA-Z][,a-zA-Z0-9\.\-:]*</code>
	 * It must start with an letter and furthermore can contain letters, digits, comma,
	 * dot, dash.
	 * 
	 * If the name is modified a ChangeEvent will be fired.  
	 * 
	 * @param name how the property will be named
	 * @throws IllegalArgumentException if the name does not match the regular expression.
	 */
	public void setName(String name) {
		if (flagValidateName && !Property.isValidName(name))
			throw new IllegalArgumentException("Illegal property name.");

		//if (this.name==null||!this.name.equals(name)) {
			this.name = name;
			// fireChangeEvent();
		//}
	}
	
	public boolean isInDefaultState() {
		return false;
	}
	
	/**
	 * Returns true if name is not null and matching the regular expression.
	 * @param name name to validate
	 * @return true if name is not null and matching the regular expression.
	 * @see #setName(String)
	 */
	public static boolean isValidName(String name) {
		return name==null?false:validateNamePattern.matcher(name).matches();
	}
	
	// ---- Editor ------------------------------------------

	/**
	 * @see #isInlineEditor()
	 */
	private boolean flagIsInlineEditor = true;
	
	/**
	 * If true the editor will be displayed in a table cell.
	 * If false the editor will be displayed in it's own dialog.
	 * The latter is useful for properties that require several
	 * components for configuration. For example a font property
	 * needs to display components for choosing fontname, fontstyle
	 * and fontsize.
	 * @see get
	 */
	public boolean isInlineEditor() {
		return flagIsInlineEditor;
	}
	
	/**
	 * Sets up the behaviour of the editor.
	 * 
	 * If writing a custom property this method can be used to
	 * setup the behaviour for the editor.
	 * 
	 * If you write a more advanced editor which can not be displayed in a
	 * table cell you have to set the IsInlineEditor property
	 * to the non-default value false.
	 * 
	 * @param inline if true shows the editor in a table cell.
	 * if false it will be displayed in it's own dialog window.
	 */
	protected void setIsInlineEditor(boolean inline) {
		flagIsInlineEditor = inline;
	}

	/**
	 * Returns the editor.
	 * @return the editor
	 * @see #isInlineEditor()
	 * @see #setIsInlineEditor(boolean)
	 */
	public PropertyEditor getEditor() {
		return new PropertyEditor.TextEditor(this);
	}
	
	// ---- value type handler ------------------------------------------

	/**
	 * map containing pairs (Class, PropertyValueWriter)
	 */
	private HashMap handlerMap = new HashMap(); 

	/**
	 * Sets the handler for a given type.
	 * 
	 * If type is null this will set or remove the default handler.
	 * If type is not null this will set or remove the handler for this type.
	 * 
	 * If handler is null means the current handler for the given type should be removed.
	 * If handler is not null sets the new handler  for the given type.
	 *  
	 * The default handler is used if no other handler matches a values type.
	 * 
	 * @param type the type which will be handled
	 * @param handler the handler for the given type
	 */
	public void setHandler(Class type, PropertyValueHandler handler) {
		if (handler==null) {
			handlerMap.remove(type); // remove
		} else {
			handlerMap.put(type, handler); // set handler
		}
	}

	/**
	 * Returns the default handler
	 * @return the default handler
	 * @see #setDefaultHandler(PropertyValueHandler)
	 */
	public PropertyValueHandler getDefaultHandler() {
		return (PropertyValueHandler) handlerMap.get(null);
	}

	/**
	 * Returns true if the default handler is set
	 * @return true if the default handler is set
	 */
	public boolean isDefaultHandlerSet() {
		return handlerMap.containsKey(null);
	}
	
	/**
	 * Returns the handler for the value's type.
	 * If no handler has been found it tries to get the default handler.
	 * If the default handler is not set too, null will be returned.
	 * 
	 * @param value the value to find a handler for
	 * @return the handler for the given value
	 */
	public PropertyValueHandler findHandler(Object value) {
		if (value!=null && handlerMap.containsKey(value.getClass()))
			return (PropertyValueHandler) handlerMap.get(value.getClass());
		else
			return (PropertyValueHandler) handlerMap.get(null);
	}

	// ---- read/write the value ----------------------------------------

	/**
	 * Returns the value
	 * @return the value
	 */
	public abstract Object getValue();

	/**
	 * Returns a string representation of the value
	 * @return a string representation of the value
	 */
	public String getValueString() {
		return ""+getValue();
	}

	/**
	 * Uses the string representation to set the value.
	 * @param value string representation of the value to set
	 */
	public abstract void setValueFromString(String value);

	/**
	 * Uses <code>findHandler(Object)</code> to obtain a handler for value.
	 * If no handler is found an IllegalArgumentException will be thrown.
	 * Otherwise value will be passed to the handler and a ChangeEvent
	 * will be fired.
	 * 
	 * @param value the value to set
	 * @see #findHandler(Object)
	 */
	public void setValue(Object value) {
		if (value instanceof String) {
			setValueFromString((String)value);
			if (isEventhandlingInstalled)
				fireChangeEvent();
		} else {
			PropertyValueHandler handle = findHandler(value);
			if (handle==null) {
				throw new IllegalArgumentException("Property '"+this+"' does not handle: "+value);
			} else {
				handle.writeValue(value);
				if (isEventhandlingInstalled)
					fireChangeEvent();
			}
		}
	}
	
	// ---- construction -----------------------------------------
	
	/**
	 * The owner component of this property
	 */
	private NomadComponent ncomponent = null;
	
	/**
	 * Creates a new property
	 * @param component owner component of this property
	 */
	public Property(NomadComponent component) {
		super();
		this.ncomponent = component;
		setName("property");
	}

	/**
	 * Returns the owner component of this property
	 * @return the owner component of this property
	 */
	public NomadComponent getComponent() {
		return ncomponent;
	}

	public String toString() {
		return getClass().getName()+"["+getName()+"="+getValueString()+"]";
	}
	
	public void exportToXml(XMLFileWriter xml) {
		String name = getName();
		String value= getValueString();
		
		xml.beginTagStart("property");
		xml.addAttribute("name", name);
		xml.addAttribute("value", value);
		xml.beginTagFinish(false);
	}
	
	public DModule findModuleInfo() {
		DModule info = null;
		
		NomadComponent c = getComponent();
		
		if (c instanceof ModuleComponent)
			return ((ModuleComponent)c).getModuleInfo();
		
		if (c.getParent()instanceof ModuleComponent)
			return ((ModuleComponent)c.getParent()).getModuleInfo();
		
		return info;
	}
	
	private boolean flagExportable = true;

	private boolean isEventhandlingInstalled;

	public boolean isExportable() {
		return flagExportable;
	}

	public void setExportable(boolean exportable) {
		this.flagExportable = exportable;
	}

	/**
	 * only if this is called
	 * - listeners should be installed
	 * - handlers should be installed
	 */
	public void setupForEditing() {
		this.isEventhandlingInstalled = true;
	}

}
