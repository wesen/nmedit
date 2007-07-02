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
import net.sf.nmedit.jtheme.clavia.nordmodular.JTMultiEnvDisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store2.AbstractMultiParameterElement;
import net.sf.nmedit.jtheme.store2.ComponentElement;

import org.jdom.Element;

public class MultiEnvDisplayStore extends AbstractMultiParameterElement
{

    /**
     * 
     */
    private static final long serialVersionUID = -2686307579438779799L;
    private static final String[] PARAMETERS = {
        "sustain",
        "l1", // level
        "l2",
        "l3",
        "l4",
        "t1", // time
        "t2",
        "t3",
        "t4",
        "t5"
    };
   
    protected MultiEnvDisplayStore()
    {
        super(PARAMETERS);
    }

    public static ComponentElement createElement(StorageContext context, Element element)
    {
        MultiEnvDisplayStore e = new MultiEnvDisplayStore();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    public JTComponent createComponent(JTContext context, PModuleDescriptor descriptor, PModule module)
    throws JTException
    {
        JTComponent component = context.createComponentInstance(JTMultiEnvDisplay.class);
        setName(component);
        setBounds(component);
        link(component, module);
        return component;
    }

    protected void link(JTComponent component, PModule module)
    {
    	
    	JTMultiEnvDisplay disp = (JTMultiEnvDisplay) component;

        PParameter sustain = module.getParameterByComponentId(componentIdList[0]);
        if (sustain != null)
            disp.setSustainAdapter(new JTParameterControlAdapter(sustain));
        
        for (int i = 1 ; i <= 4  ; i++){
            PParameter p = module.getParameterByComponentId(componentIdList[i]);
            if (p!=null)
            	disp.setLevelAdapter(i-1,new JTParameterControlAdapter(p));
        } 
       
        for (int i = 5 ; i <= 9  ; i++){
            PParameter p = module.getParameterByComponentId(componentIdList[i]);
            if (p!=null)
            	disp.setTimeAdapter(i-5,new JTParameterControlAdapter(p));
        } 
        
    }
   
}
