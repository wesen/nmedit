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

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTButtonControl;

import org.jdom.Element;

public class ButtonStore extends ControlStore
{

    private StorageContext context;

    protected ButtonStore(Element element, StorageContext context)
    {
        super(element);
        this.context = context;
    }

    public static ButtonStore create(StorageContext context, Element element)
    {
        return new ButtonStore(element, context);
    }
    
    @Override
    public JTButtonControl createComponent(JTContext context) throws JTException
    {
        JTButtonControl buttons = (JTButtonControl) context.createComponent(JTContext.TYPE_BUTTONS);
        applyLocation(buttons);
        applySize(buttons);
        
        configure(buttons);
        
        return buttons;
    }

    private void configure(JTButtonControl buttons)
    {
        Element root = getElement();
        
        buttons.setOrientation(
                Boolean.parseBoolean(root.getAttributeValue("landscape"))
                ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL);
        
        buttons.setCyclic(Boolean.parseBoolean(root.getAttributeValue("cyclic")));
        
        for (Element btn : (List<Element>) root.getChildren("btn"))
        {
            int index = getIntAtt(btn, "index");
            
            Element img = btn.getChild("image");
            if (img != null)
            {
                String href = ImageStore.getXlinkHref(img);
                Image image = context.getImage(href);
                buttons.setIcon(index, new ImageIcon(image));
            }
            else
            {
                buttons.setText(index, btn.getText());
            }
        }
    }

}

