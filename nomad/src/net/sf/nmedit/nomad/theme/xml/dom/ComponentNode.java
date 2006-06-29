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
 * Created on Feb 27, 2006
 */
package net.sf.nmedit.nomad.theme.xml.dom;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.property.Property;
import net.sf.nmedit.nomad.theme.property.PropertySet;
import net.sf.nmedit.nomad.theme.property.Value;


public class ComponentNode /*implements Iterable<String>*/ {
	
	private ArrayList<Value> values;
	private ArrayList<String> propertyNames ;
	private HashMap<String, String> propertyMap ;
	private String name;
	
	public ComponentNode(String componentName) {
        values = new ArrayList<Value>();
		propertyNames = new ArrayList<String>(10);
		propertyMap = new HashMap<String,String>();
		name = componentName; 
	}
	
	public String getName() {
		return name;
	}
	
	public void putProperty(String name, String value) {
		if (!propertyMap.containsKey(name))
			propertyNames.add(name);
		propertyMap.put(name, value);
	}
	
	public void removeProperty(String name) {
		propertyMap.remove(name);
		propertyNames.remove(name);
	}
	
	public String getProperty(String name) {
		return propertyMap.get(name);
	}

	public int getPropertyCount() {
		return propertyNames.size();
	}

	public String getPropertyName(int index) {
		return propertyNames.get(index);
	}
	
	public Value getPropertyC(int index) {
		return values.get(index);
	}
	
    
	public int getCompiledPropertyCount() {
		return values.size() ;
	}
	
	public void compileProperties(UIFactory factory) {
        values.clear() ;
		PropertySet set = factory.getProperties(factory.getNomadComponentClass(getName()));
		for(int i=0;i<propertyNames.size();i++) {
			String name = propertyNames.get(i);
			String value= propertyMap.get(name);
			
            Property p = set.get(name);
            if (p!=null)
            {
    			Value pvalue = p.decode(value);
                values.add(pvalue);
            }
            else
            {
                System.err.println("Property '"+name+"' not found.");
            }
		}
	}
	
	public void assignProperties(NomadComponent component) {
		for (int i=values.size()-1;i>=0;i--)
        {
            values.get(i).assignTo(component);
            /*Value v = values.get(i);
            if (v!=null)
                v.assignTo(component);*/
        }
	}
	
}
