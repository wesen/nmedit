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

import javax.swing.SwingConstants;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTSlider;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Attribute;
import org.jdom.Element;

public class SliderElement extends ControlElement
{

    /**
     * 
     */
    private static final long serialVersionUID = 5479694009082150230L;

    protected static final String ATT_ORIENTATION = "orientation";

    protected int orientation = SwingConstants.VERTICAL;

    public static AbstractElement createElement(StorageContext context, Element element)
    {
        SliderElement e = new SliderElement();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    protected void initAttributes(StorageContext context, Attribute att)
    {
        if (ATT_ORIENTATION.equals(att.getName()))
        {
            orientation = ("horizontal".equals(att.getValue()))
                ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL;
        }
        else
        {
            super.initAttributes(context, att);
        }
    }

    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTSlider slider = (JTSlider) context.createComponent(JTContext.TYPE_SLIDER);
        setBounds(slider);
        setName(slider);
        setParameter(slider, descriptor, module);
        slider.setOrientation(orientation);
        return slider;
    }

}
