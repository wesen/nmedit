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

import java.awt.Color;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTTextDisplay;
import net.sf.nmedit.jtheme.store.CSSUtils;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

public class TextDisplayElement extends ControlElement
{

    private static final long serialVersionUID = 6232380514341586662L;
    private String cssStyleValue;
    private transient CSSStyleDeclaration styleDecl;
    private transient Color fill;

    public static AbstractElement createElement(StorageContext context, Element element)
    {
        TextDisplayElement e = new TextDisplayElement();
        e.initElement(context, element);
        e.checkDimensions();
        e.checkLocation();
        return e;
    }

    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTTextDisplay component = (JTTextDisplay) context.createComponent(JTContext.TYPE_TEXTDISPLAY);
        setBounds(component);
        setName(component);
        setParameter(component, descriptor, module);
        applyStyle(component);
        return component;
    }

    protected void setParameter(JTTextDisplay component, PModuleDescriptor descriptor, PModule module)
    {
        if (module != null)
        {
            PParameterDescriptor parameterDescriptor = null;
            if (componentId != null)
            {
                parameterDescriptor = descriptor.getParameterByComponentId(componentId);
            }
            if (parameterDescriptor != null)
            {
                PParameter parameter = module.getParameter(parameterDescriptor);
                if (parameter != null)
                    component.setParameter(parameter);
            }
        }
    }

    @Override
    public void initializeElement(StorageContext context)
    {
        styleDecl = CSSUtils.getStyleDeclaration("textDisplay", cssStyleValue, context);
        if (styleDecl == null)
        {
            CSSStyleRule rule = context.getStyleRule("textDisplay");
            if (rule != null)
                styleDecl = rule.getStyle();
        }
    }
    
    @Override
    protected void initCSSStyle(StorageContext context, String styleValue)
    {
        this.cssStyleValue = styleValue;
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
    
}
