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

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTMultiEnvDisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.ControlStore;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store.Store;
import net.sf.nmedit.jtheme.store.helpers.ParameterDescriptorHelper;

import org.jdom.Element;

public class MultiEnvDisplayStore extends ControlStore
{

    protected ParameterDescriptorHelper levelParameterHelper[];
    protected ParameterDescriptorHelper timeParameterHelper[];
   
    
    protected MultiEnvDisplayStore(Element element)
    {
        super(element);
    }

    public static Store create(StorageContext context, Element element)
    {
        return new MultiEnvDisplayStore(element);
    }

    protected void initDescriptors(Element element)
    {
        parameterDescriptorHelper = ParameterDescriptorHelper.createHelper(element.getChild("sustain"));
        levelParameterHelper = new ParameterDescriptorHelper[4];
        for (int i = 0 ; i < levelParameterHelper.length  ; i++){
            levelParameterHelper[i] = ParameterDescriptorHelper.createHelper(element.getChild("l"+(i+1)));
        }
        
        timeParameterHelper = new ParameterDescriptorHelper[5];
        for (int i = 0 ; i < timeParameterHelper.length  ; i++){
            timeParameterHelper[i] = ParameterDescriptorHelper.createHelper(element.getChild("t"+(i+1)));
        }       
    }

    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        JTComponent component = context.createComponentInstance(JTMultiEnvDisplay.class);
        applyLocation(component);
        applySize(component);
        return component;
    }

    protected void link(JTContext context, JTComponent component, Module module)
      throws JTException
    {
    	
    	JTMultiEnvDisplay disp = (JTMultiEnvDisplay) component;
    	
        
        for (int i = 0 ; i < levelParameterHelper.length  ; i++){
            Parameter p = levelParameterHelper[i].lookup(module);
            if (p!=null)
            	disp.setLevelAdapter(i,new JTParameterControlAdapter(p));
        } 
       
        for (int i = 0 ; i < timeParameterHelper.length  ; i++){
            Parameter p = timeParameterHelper[i].lookup(module);
            if (p!=null)
            	disp.setTimeAdapter(i,new JTParameterControlAdapter(p));
        } 
        
        Parameter sustain = parameterDescriptorHelper.lookup(module);
        if (sustain != null)
            disp.setSustainAdapter(new JTParameterControlAdapter(sustain));        
    }
    
    protected void link2(JTContext context, JTComponent component, Module module, Parameter parameter)
    {
        throw new UnsupportedOperationException();
    }
    
}
