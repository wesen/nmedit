/* Copyright (C) 2006-2007 Christian Schneider
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

import org.jdom.Element;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.store.StorageContext;

public class DefaultStore extends AbstractMultiParameterElement
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6054952214662350115L;
    private Class<? extends JTComponent> jtclass;

    public static ComponentElement createElement(StorageContext context, Element element) throws JTException
    {
        Class<? extends JTComponent> jtclass = context.getContext().getComponentType(element.getName());
        if (jtclass == null)
                throw new JTException("no component class for element: "+element.getName());
       
        return new DefaultStore(context, element, jtclass);
    }

    public DefaultStore(StorageContext context, Element element, Class<? extends JTComponent> jtclass)
    {
        super(jtclass);
        this.jtclass =  jtclass;
        initElement(context, element);
        checkDimensions();
        checkLocation();
    }

    @Override
    public JTComponent createComponent(JTContext context, PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTComponent component = context.createComponentInstance(jtclass);
        setName(component);
        setBounds(component);
        link(component, module);
        return component;
    }
    
    public String toString()
    {
        return getClass().getSimpleName()+"[component="+jtclass.getName()+"]";
    }

}
