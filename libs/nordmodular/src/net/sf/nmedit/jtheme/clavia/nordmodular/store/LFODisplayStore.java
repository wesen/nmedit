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

import org.jdom.Element;

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.LFODisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.ControlStore;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store.Store;
import net.sf.nmedit.jtheme.store.helpers.ParameterDescriptorHelper;

public class LFODisplayStore extends ControlStore
{
    
    private ParameterDescriptorHelper phaseDescriptorHelper;
    private ParameterDescriptorHelper shapeDescriptorHelper;
    private int waveform = -1;

    protected LFODisplayStore(Element element)
    {
        super(element);
    }

    protected void initDescriptors(Element element)
    {
        phaseDescriptorHelper = ParameterDescriptorHelper.createHelper(element.getChild("phase"));
        shapeDescriptorHelper = ParameterDescriptorHelper.createHelper(element.getChild("shape"));
        
        Element wv = element.getChild("waveform");
        String wvvalue = wv != null ? wv.getAttributeValue("value") : null;
        if (wvvalue != null)
        {
            try
            {
                waveform = Integer.parseInt(wvvalue);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }

        }
    }
    
    public static Store create(StorageContext context, Element element)
    {
        return new LFODisplayStore(element);
    }

    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        JTComponent component = context.createComponentInstance(LFODisplay.class);
        applyLocation(component);
        applySize(component);
        return component;
    }

    protected void link(JTContext context, JTComponent component, Module module)
      throws JTException
    {
        Parameter phase = phaseDescriptorHelper.lookup(module);
        Parameter shape = shapeDescriptorHelper.lookup(module);
        
        LFODisplay disp = (LFODisplay) component;

        if (waveform >= 0)
            disp.setWaveForm(waveform);

        if (phase != null) disp.setPhaseAdapter(new JTParameterControlAdapter(phase));
        if (shape != null) disp.setWaveAdapter(new JTParameterControlAdapter(shape));
    }
    
    protected void link2(JTContext context, JTComponent component, Module module, Parameter parameter)
    {
        throw new UnsupportedOperationException();
    }
    
}
