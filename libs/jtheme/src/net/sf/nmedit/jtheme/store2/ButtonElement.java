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

import java.awt.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTButtonControl;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.misc.CallDescriptor;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Attribute;
import org.jdom.Element;

public class ButtonElement extends ControlElement
{

    /**
     * 
     */
    private static final long serialVersionUID = -6842481386886896976L;
    public static final String ATT_LANDSCAPE = "landscape";
    public static final String ATT_REVERSE = "reverse";
    public static final String ATT_CYCLIC = "cyclic";
    public static final String ATT_MODE = "mode";
    
    private int orientation = SwingConstants.VERTICAL;
    private boolean cyclic ;
    private boolean incrementMode;
	private boolean reverse;
    
    private String callComponent;
    private String callMethod;

    public static AbstractElement createElement(StorageContext context, Element element)
    {
        ButtonElement e = new ButtonElement();
        e.initElement(context, element);
        return e;
    }
    
    @Override
    public JTComponent createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTButtonControl buttons = (JTButtonControl) context.createComponent(JTContext.TYPE_BUTTONS);
        setName(buttons);
        setBounds(buttons);
        
        if (callComponent != null && callMethod != null)
            buttons.setCall(new CallDescriptor(buttons, callComponent, callMethod));
        
        configure(buttons);
        setParameter(buttons, descriptor, module);
        return buttons;
    }

    @Override
    protected void initLinks(Element e)
    {
        // we initialize componentId in initElement, not here
    }

    @Override
    protected void initAttributes(StorageContext context, Attribute att)
    {
        String name = att.getName();
        if (ATT_LANDSCAPE.equals(name))
        {
            this.orientation = Boolean.parseBoolean(att.getValue())
                ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL;
        }
        else if (ATT_MODE.equals(name))
        {
            this.incrementMode = "increment".equals(att.getValue());   
        }
        else if (ATT_CYCLIC.equals(name))
        {
            this.cyclic = Boolean.parseBoolean(att.getValue());  
        }
        else if (ATT_REVERSE.equals(name))
        {
        	this.reverse = Boolean.parseBoolean(att.getValue());
        }
        else
        {
            super.initAttributes(context, att);
        }
    }

    public static final String EL_CALL = "call";
    public static final String EL_BTN = "btn";
    public static final String EL_PARAMETER = "parameter";
    

    private Map<Integer, ImageIcon> images = new HashMap<Integer, ImageIcon>(4);
    private Map<Integer, String> labels = new HashMap<Integer, String>(4);
    
    @Override
    protected void initElement(StorageContext context, Element e)
    {
        super.initElement(context, e);
        
        List children = e.getChildren();
        for (int i=children.size()-1;i>=0;i--)
        {
            Element child = (Element) children.get(i);
            String name = child.getName();
            
            if (EL_BTN.equals(name))
            {
                int index = parseInt(child.getAttributeValue("index"), "index");
                
                Element img = child.getChild("image");
                if (img != null)
                {
                    String href = ImageElement.getXlinkHref(img);
                    Image image = ImageElement.getImage(href, context);
                    if (image != null)
                        images.put(index, new ImageIcon(image));
                }
                else
                {
                    labels.put(index, child.getText());
                }
            }
            else if (EL_PARAMETER.equals(name))
            {
                Attribute a = child.getAttribute(ATT_COMPONENT_ID);
                if (a != null)
                    componentId = a.getValue();
            } 
            else if (EL_CALL.equals(name))
            {
                callComponent = child.getAttributeValue("component");
                callMethod = child.getAttributeValue("method");
            }
        }
    }
    
    private void configure(JTButtonControl buttons)
    {
        buttons.setIncrementModeEnabled(incrementMode);
        buttons.setOrientation(orientation);
        buttons.setCyclic(cyclic);
        buttons.setReversed(reverse);

        for (Integer key: images.keySet())
        {
            ImageIcon icon = images.get(key);
            buttons.setIcon(key.intValue(), icon);
        }

        for (Integer key: labels.keySet())
        {
            String text  = labels.get(key);
            buttons.setText(key.intValue(), text);
        }
    }

}
