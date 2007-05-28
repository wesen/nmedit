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

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTLabel;

import org.jdom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

public class LabelStore extends DefaultStore
{

    private CSSStyleDeclaration styleDecl;
    private Color clFill = null;
    private int fontSize = -1;

    protected LabelStore(Element element, CSSStyleDeclaration styleDecl)
    {
        super(element);
        this.styleDecl = styleDecl;
        
        preloadStyles();
    }

    private void preloadStyles()
    {
        if (styleDecl == null)
            return ;
        
        clFill = CSSUtils.getColor(styleDecl, "fill");
        
        float fs = CSSUtils.getPx(styleDecl, "font-size", -1);
        
        if (fs>0)
            fontSize = (int) Math.floor(fs);
        
    }

    public static LabelStore create(StorageContext context, Element element)
    {
        CSSStyleDeclaration styleDecl = 
            CSSUtils.getStyleDeclaration("label", element, context);
        
        return new LabelStore(element, styleDecl);
    }
  
    @Override
    public JTLabel createComponent(JTContext context) throws JTException
    {
        JTLabel label = context.createLabel();
        setReducible(label);
        label.setText(getElement().getTextTrim());
       // label.setFont(defaultFont);
        
        applyStyles(label);
        applyName(label);
        
        label.setSize(label.getPreferredSize());
        
        // fix y-value
        label.setLocation(getX(), getY()-label.getHeight());
        
        return label;
    }

    private void applyStyles(JTLabel label)
    {
        if (styleDecl == null) return;
        
        if (clFill != null) label.setForeground(clFill);
       // if (fontSize > 0) label.setFont(font)
        
    }

}

