/* Copyright (C) 2007 Julien Pauty
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

package net.sf.nmedit.jtheme.clavia.nordmodular.store;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTCompressorDisplay;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTExpanderDisplay;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store2.AbstractMultiParameterElement;
import net.sf.nmedit.jtheme.store2.ComponentElement;

import org.jdom.Element;

public class ExpanderDisplayStore extends AbstractMultiParameterElement
{
	/**
     * 
     */
    private static final long serialVersionUID = -3089610669534017095L;
    private static final String[] PARAMETERS = { "threshold", "ratio", 
        "gate"   
    };

    protected ExpanderDisplayStore()
    {
        super(PARAMETERS);        
    }

    public static ComponentElement createElement(StorageContext context, Element element)
    {
        ExpanderDisplayStore e = new ExpanderDisplayStore();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    public JTExpanderDisplay createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
        throws JTException
    {
        JTExpanderDisplay component = (JTExpanderDisplay) context.createComponentInstance(JTExpanderDisplay.class);
        setName(component);
        setBounds(component);
        link(component, module);
        return component; 
    }

    protected void link(JTExpanderDisplay disp, PModule module)
    {
        PParameter threshold = module.getParameterByComponentId(componentIdList[0]);
        PParameter ratio = module.getParameterByComponentId(componentIdList[1]);
        PParameter gate = module.getParameterByComponentId(componentIdList[2]);
      
                
        if (ratio != null)
            disp.setRatioAdapter(new JTParameterControlAdapter(ratio));
        if (threshold != null)
            disp.setThresholdAdapter(new JTParameterControlAdapter(threshold));
        if (gate!= null)
            disp.setGateAdapter(new JTParameterControlAdapter(gate));        
    }

}

