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
package net.sf.nmedit.jtheme.store2;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Element;

public class KnobElement extends ControlElement
{

    public static AbstractElement createElement(StorageContext context, Element element)
    {
        KnobElement e = new KnobElement();
        e.initElement(context, element);
        e.checkDimensions();
        
        if (e.x<0||e.y<0)
            System.out.println(((Element)element.getParent()).getAttributeValue("component-id"));
        
        e.checkLocation();
        return e;
    }

    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTControl knob = (JTControl) context.createComponent(JTContext.TYPE_KNOB);
        /*setLocation(knob);
        if (width>=0 && height>=0)
            setSize(knob);*/
        setBounds(knob);
        setName(knob);
        setParameter(knob, descriptor, module);
        return knob;
    }

}
