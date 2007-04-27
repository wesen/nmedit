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
package net.sf.nmedit.jtheme.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;

public class ComponentListCellRenderer extends DefaultListCellRenderer
{

    private transient Map<Class, Icon> iconMap;
    
    protected Map<Class,Icon> getIconMap()
    {
        if (iconMap == null)
            iconMap = new HashMap<Class, Icon>();
        return iconMap;
    }
    
    public Component getListCellRendererComponent(
        JList list, Object value, int index, boolean isSelected,
        boolean cellHasFocus)
    {
        Object renderedValue = value;

        ComponentListModel listModel = (ComponentListModel) list.getModel();
        Class clazz = (Class) value;
        Icon iconForClass;
        
        synchronized (getTreeLock())
        {
            iconForClass = getIconMap().get(clazz);   
            
            while (getComponentCount()>0)
                remove(0);
            
        }
        if (iconForClass != null)
        {
            renderedValue = iconForClass;
        } 
        else if (listModel != null)
        {
            JTContext context = listModel.getContext();
            JComponent component = null;
            try
            {
                component = context.createComponentInstance(clazz);
            }
            catch (JTException e)
            {
                component = null;
                e.printStackTrace();
            }
            
            if (component != null)
            {
                add(component);
                
                Dimension d = component.getPreferredSize();
                if (d == null)
                    d = new Dimension(20, 20);
                else
                if (d.width<=2 || d.height<=2)
                    d.setSize(20, 20);
                
                component.setPreferredSize(d);
                component.setSize(d);
                
                d.width += padding*2;
                d.height+= padding*2;
                
                component.setLocation(padding, padding);
                add(component);
                
                setSize(d);
                setPreferredSize(d);
                renderedValue = "";
            }
        }
            
        super.getListCellRendererComponent(list, renderedValue, 
                index, isSelected, cellHasFocus);
        
        if (clazz != null)
            setText( clazz.getSimpleName() );
        else 
            setText("");
        
        return this;
    }

    private Image render(JComponent component)
    {
        BufferedImage img = new BufferedImage(component.getWidth(), component.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D gi = img.createGraphics();
        try
        {
            gi.setFont(component.getFont());
            gi.setColor(component.getForeground());
            component.paint(gi);
        }
        finally
        {
            gi.dispose();
        }
        return img;
    }

    protected void paintChildren(Graphics g)
    {
        Component renderMe = null;
        
        synchronized (getTreeLock())
        {
            if (getComponentCount()>0)
                renderMe = getComponent(0);
        }
        
        if (renderMe == null || !(renderMe instanceof JTComponent))
        {
            super.paintChildren(g);
            return ;
        }

        ImageIcon icon = new ImageIcon(render((JComponent)renderMe));

        synchronized (getTreeLock())
        {
            getIconMap().put(renderMe.getClass(), icon);   
        }
        
        super.paintChildren(g);
    }

    private static final int padding = 4;
    private static final Border paddingBorder = BorderFactory.createEmptyBorder(
            padding, padding, padding, padding);
    
    public void setBorder(Border border)
    {
        border = border != null ? BorderFactory.createCompoundBorder(border, paddingBorder)
                : paddingBorder;
     
        super.setBorder(border);
    }
    
}

