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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.nmedit.jtheme.Property;
import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.component.NomadComponent;

public class ComponentNode implements Iterable<String> {
	
	private String name;
    private Map<String,String> properties = new HashMap<String,String>();
    private Map<String,Object> compiled = new HashMap<String,Object>();
	
	public ComponentNode(String componentName) {
		name = componentName; 
	}
	
	public String getName() {
		return name;
	}
	
	public void putProperty(String name, String value) {
		if (value==null)
        {
            properties.remove(name);
        }  else
        {
            properties.put(name, value);
        }
        compiled.remove(name);
	}
	
	public void removeProperty(String name) {
        putProperty(name, null);
	}
	
	public String getProperty(String name) {
		return properties.get(name);
	}

	public int getPropertyCount() {
		return properties.size();
	}

	public void assignProperties(UIFactory factory, NomadComponent component) {
        Map<String,Property> pmap = factory.configuration.getProperties(component);
        
        for (String name : properties.keySet())
        {
            Property p = pmap.get(name);
            try
            {
            factory.configuration.setPropertyString(component, name, getProperty(name));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}

    public Iterator<String> iterator()
    {
        return properties.keySet().iterator();
    }
	
}
