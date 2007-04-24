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

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTCompressorDisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.ControlStore;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store.Store;
import net.sf.nmedit.jtheme.store.helpers.ParameterDescriptorHelper;

import org.jdom.Element;

public class CompressorDisplayStore extends ControlStore
{
	//inherited fielad parameterDescriptorHelper is used for the threshold parameter    
    protected ParameterDescriptorHelper ratioParameterHelper;
    protected ParameterDescriptorHelper refLevelParameterHelper;
    protected ParameterDescriptorHelper limiterParameterHelper;
    
    protected CompressorDisplayStore(Element element)
    {
        super(element);        
    }

    public static Store create(StorageContext context, Element element)
    {
        return new CompressorDisplayStore(element);
    }

    protected void initDescriptors(Element element)
    {
        parameterDescriptorHelper = ParameterDescriptorHelper.createHelper(element.getChild("threshold"));
        ratioParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("ratio"));
        refLevelParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("ref-level"));
        limiterParameterHelper = ParameterDescriptorHelper.createHelper(element.getChild("limiter"));
    }

    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        JTComponent component = context.createComponentInstance(JTCompressorDisplay.class);
        applyLocation(component);
        applySize(component);
        return component;
    }

    protected void link(JTContext context, JTComponent component, Module module)
      throws JTException
    {
        Parameter threshold = parameterDescriptorHelper.lookup(module);        
        Parameter ratio = ratioParameterHelper.lookup(module);        
        Parameter limiter = limiterParameterHelper.lookup(module);
        Parameter refLevel = refLevelParameterHelper.lookup(module);
        
        JTCompressorDisplay disp = (JTCompressorDisplay) component;
                
        if (ratio != null)
            disp.setRatioAdapter(new JTParameterControlAdapter(ratio));
        if (threshold != null)
            disp.setThresholdAdapter(new JTParameterControlAdapter(threshold));
        if (limiter!= null)
            disp.setLimiterAdapter(new JTParameterControlAdapter(limiter));
        if (refLevel!= null)
            disp.setRefLevelAdapter(new JTParameterControlAdapter(refLevel));
    }
    
    protected void link2(JTContext context, JTComponent component, Module module, Parameter parameter)
    {
        throw new UnsupportedOperationException();
    }
    
}

