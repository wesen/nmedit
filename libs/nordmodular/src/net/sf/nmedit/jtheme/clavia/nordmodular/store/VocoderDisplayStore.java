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
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.VocoderDisplay;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.DefaultStore;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.store.Store;

import org.jdom.Element;

public class VocoderDisplayStore extends DefaultStore
{
    
    private String[] bandHelpers = new String[16];

    public VocoderDisplayStore(Element element)
    {
        super(element);
        initDescriptors(element);
    }

    public static Store create(StorageContext context, Element xmlElement)
      throws JTException
    {
        return new VocoderDisplayStore(xmlElement);
    }
    
    protected void initDescriptors(Element element)
    {
        for (Object oelement : element.getChildren("parameter"))
        {
            Element e = (Element) oelement;
            int band = getIntAtt(e, "band");
            bandHelpers[band] = e.getAttributeValue("component-id");
        }
    }

    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        VocoderDisplay component = context.createComponentInstance(VocoderDisplay.class);
        applyName(component);
        applyLocation(component);
        applySize(component);
        return component;
    }

    protected void link(JTContext context, JTComponent component, PModule module)
      throws JTException
    {
        VocoderDisplay disp = (VocoderDisplay) component;
        
        for (int i=bandHelpers.length-1;i>=0;i--)
        {
            PParameter p = module.getParameterByComponentId(bandHelpers[i]);
            if (p != null) disp.setBandAdapter(i, new JTParameterControlAdapter(p));
        }
    }
    
}
