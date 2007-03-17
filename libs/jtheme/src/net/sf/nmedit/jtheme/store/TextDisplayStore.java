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

import java.awt.Color;

import org.jdom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTTextDisplay;

public class TextDisplayStore extends ControlStore
{

    private CSSStyleDeclaration styleDecl;
    private Color fill;

    protected TextDisplayStore(Element element, CSSStyleDeclaration styleDecl)
    {
        super(element);
        this.styleDecl = styleDecl;
    }

    public static TextDisplayStore create(StorageContext context, Element element)
    {
        CSSStyleDeclaration styleDecl = 
            CSSUtils.getStyleDeclaration("textDisplay", element, context);
        
        return new TextDisplayStore(element, styleDecl);
    }
    
    @Override
    public JTComponent createComponent(JTContext context) throws JTException
    {
        JTComponent component = context.createComponent(JTContext.TYPE_TEXTDISPLAY);
        applyLocation(component);
        applySize(component);
        applyStyle(component);
        return component;
    }

    private void applyStyle(JTComponent component)
    {
        if (styleDecl == null) return;

        if (fill == null)
            fill = CSSUtils.getColor(styleDecl, "fill");
        
        if (fill != null)
        {
            component.setBackground(fill);
            Color inverse = new Color(0xFF-fill.getRed(), 0xFF-fill.getGreen(), 0xFF-fill.getBlue());
            component.setForeground(inverse);
        }
        
        if (CSSUtils.isBorderNone(styleDecl, "border", false))
            component.setBorder(null);
    }
    
    protected void link2(JTContext context, JTComponent component, Module module, Parameter parameter)
    {
        ((JTTextDisplay)component).setParameter(parameter);
    }

}

