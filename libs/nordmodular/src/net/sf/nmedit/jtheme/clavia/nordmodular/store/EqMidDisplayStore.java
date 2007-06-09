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
package net.sf.nmedit.jtheme.clavia.nordmodular.store;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTEqMidDisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store2.AbstractMultiParameterElement;
import net.sf.nmedit.jtheme.store2.ComponentElement;

import org.jdom.Element;

public class EqMidDisplayStore extends AbstractMultiParameterElement
{

    private static final String[] PARAMETERS = {"frequency", "gain", "bandwidth"};

    public EqMidDisplayStore()
    {
        super(PARAMETERS);
    }

    public static ComponentElement createElement(StorageContext context, Element element)
    {
        EqMidDisplayStore e = new EqMidDisplayStore();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    public JTComponent createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
        throws JTException
    {
        JTComponent component = context.createComponentInstance(JTEqMidDisplay.class);
        setName(component);
        setBounds(component);
        link((JTEqMidDisplay) component, module);
        return component; 
    }

    protected void link(JTEqMidDisplay disp, PModule module)
    {
        PParameter freq = module.getParameterByComponentId(componentIdList[0]);
        PParameter gain = module.getParameterByComponentId(componentIdList[1]);
        PParameter bw = module.getParameterByComponentId(componentIdList[2]);

        if (freq != null)
            disp.setFreqAdapter(new JTParameterControlAdapter(freq));
        if (gain != null)
            disp.setGainAdapter(new JTParameterControlAdapter(gain));
        if (bw != null)
            disp.setBWAdapter(new JTParameterControlAdapter(bw));
    }

}
