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
import java.io.IOException;
import java.io.Serializable;

import org.jdom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.jtheme.store.CSSUtils;
import net.sf.nmedit.jtheme.store.StorageContext;

public class LabelElement extends AbstractElement implements Serializable
{

    private transient CSSStyleDeclaration styleDecl;
    private Color clFill = null;
    private int fontSize = -1;
    protected boolean reducible = false;
    protected String text;

    public static AbstractElement createElement(StorageContext context, Element element)
    {
        LabelElement e = new LabelElement();
        e.initElement(context, element);
        e.checkLocation();
        e.text = element.getTextTrim();
        return e;
    }

    @Override
    protected void initElement(StorageContext context, Element e)
    {
        super.initElement(context, e);
        if (styleDecl == null)
        {
            CSSStyleRule rule = context.getStyleRule(e.getName());
            if (rule != null)
                styleDecl = rule.getStyle();
        }
        

        if (styleDecl != null)
        {
            clFill = CSSUtils.getColor(styleDecl, "fill");
            float fs = CSSUtils.getPx(styleDecl, "font-size", -1);
            if (fs>0)
                fontSize = (int) Math.floor(fs);
        }
    }
    
    @Override
    protected void initCSSStyle(StorageContext context, String styleValue)
    {
        styleDecl = CSSUtils.getStyleDeclaration("label", styleValue, context);
    }
    
    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTLabel label = context.createLabel();
        this.reducible = label.isReducible();
        label.setText(text);
       // label.setFont(defaultFont);
        applyStyles(label);
        setName(label);
        label.setSize(label.getPreferredSize());
        
        // fix y-value
        // TODO check x,y>0
        label.setLocation(x, y-label.getHeight());
        
        return label;
    }

    private void applyStyles(JTLabel label)
    {
        //if (styleDecl != null) return;
        
        if (clFill != null) label.setForeground(clFill);
       // if (fontSize > 0) label.setFont(font)
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
    
}
