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
package net.sf.nmedit.jtheme.store;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;

import org.jdom.Element;

public abstract class ControlStore extends DefaultStore
{

    protected String parameterId;
    
    protected ControlStore(Element element)
    {
        super(element);
        initDescriptors();
    }
    
    protected void initDescriptors()
    {
        parameterId = lookupChildElementComponentId("parameter");
    }

    protected void link(JTContext context, JTComponent component, PModule module)
      throws JTException
    {
        PParameter parameter = module.getParameterByComponentId(parameterId);
        
        if (parameter != null)
            link2(context, component, module, parameter);
    }
    
    protected void link2(JTContext context, JTComponent component, PModule module, PParameter parameter)
    {
        JTControl control = (JTControl) component;
        control.setAdapter(new JTParameterControlAdapter(parameter));
        
        PParameter ext = parameter.getExtensionParameter();
        
        if (ext != null) control.setExtensionAdapter(new JTParameterControlAdapter(ext));
        
    }

}

