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
 * Created on Feb 22, 2006
 */
package net.sf.nmedit.nomad.theme.property;

import java.util.Map;

import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.xml.dom.theme.ComponentNode;


public class PropertyUtils {

    public static void exportToDOM(ComponentNode node, Map<String, Property> map, NomadComponent component) {
        for (Property p : map.values()) {
            Value v = p.encode(component);

            if (v == null)
            {
                throw new NullPointerException("Encoding failed in component "+component.getClass().getName()+" property "+p);
            }

            if (!v.isInDefaultState()) 
            {
                node.putProperty(p.getName(), v.getRepresentation());
            }
        }
    }
/*
    public static void exportToXml(XMLFileWriter xml, Map<String, Property> map, NomadComponent component) {
        xml.beginTag("properties", true);
        for (Property p : map.values())
            p.exportToXml(xml, component);
        xml.endTag();
    }*/
    
    /*
	public static void exportToDOM(ComponentNode node, Map<String, Property> map, NomadComponent component) {
		for (Property p : map.values()) {
			if (p.isExportable() && (!p.isInDefaultState(component))) {
				node.putProperty(p.getName(), p.getValue(component));
			}
		}
	}

	public static void exportToXml(XMLFileWriter xml, Map<String, Property> map, NomadComponent component) {
		xml.beginTag("properties", true);
		for (Property p : map.values())
			p.exportToXml(xml, component);
		xml.endTag();
	}
*/
}
