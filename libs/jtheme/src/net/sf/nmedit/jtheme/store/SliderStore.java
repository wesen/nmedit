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

import javax.swing.SwingConstants;

import org.jdom.Element;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTSlider;

public class SliderStore extends ControlStore
{

    protected SliderStore(Element element)
    {
        super(element);
    }

    public static SliderStore create(StorageContext context, Element element)
    {
        return new SliderStore(element);
    }
    
    @Override
    public JTSlider createComponent(JTContext context) throws JTException
    {
        JTSlider slider = (JTSlider) context.createComponent(JTContext.TYPE_SLIDER);
        applyLocation(slider);
        applySize(slider);
        
        configure(slider);
        
        return slider;
    }

    private void configure(JTSlider slider)
    {
        Element root = getElement();
        
        slider.setOrientation(
                "horizontal".equals(root.getAttributeValue("orientation"))
                ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL);
    }

}

