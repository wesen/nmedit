/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.jtheme.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTModuleContainer;

public abstract class PatchImageExporter
{

    private boolean transparent = true;

    public PatchImageExporter()
    {
        super();
    }

    public boolean isTransparent()
    {
        return transparent;
    }
    
    public void setTransparent(boolean t)
    {
        this.transparent = t;
    }
    
    public abstract Image export();
    
    public Image[] renderModuleContainer(JTModuleContainer[] containerList)
    {
        List<Image> imageList = new ArrayList<Image>(containerList.length);
        
        int totalMaxW = 0;
        if (!transparent)
        {
            for (int i=0;i<containerList.length;i++)
            {
                totalMaxW = Math.max(totalMaxW, containerList[i].getWidth());
            }
        }
        
        for (int i=0;i<containerList.length;i++)
        {
            JTModuleContainer container = containerList[i];
            synchronized (container.getTreeLock())
            {
                int w = 0;
                int h = 0;
                int minx = Integer.MAX_VALUE;
                int miny = Integer.MAX_VALUE;
                for (int j=0;j<container.getComponentCount();j++)
                {
                    Component child = container.getComponent(j);
                    w = Math.max(w, child.getX()+child.getWidth());
                    h = Math.max(h, child.getY()+child.getHeight());
                    minx = Math.min(minx, child.getX());
                    miny = Math.min(miny, child.getY());
                }
                
                Dimension previousSize = container.getSize();
                try
                {
                    if (!transparent)
                    {
                        container.setSize(totalMaxW, h);
                        BufferedImage bi = new BufferedImage(totalMaxW, h, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2 = bi.createGraphics();
                        try
                        {
                            g2.setFont(container.getFont());
                            g2.setColor(container.getForeground());
                            
                            container.paint(g2);
                        }
                        finally
                        {
                            g2.dispose();
                        }
                        imageList.add(bi);
                    }
                    else
                    {
                        container.setSize(w, h);
                        BufferedImage bi = new BufferedImage(w-minx, h-miny, BufferedImage.TYPE_INT_ARGB);
                        
                        Graphics2D g2 = bi.createGraphics();
                        try
                        {
                            for (int j=0;j<container.getComponentCount();j++)
                            {
                                Component child = container.getComponent(j);
                                Graphics gc = g2.create();
                                try
                                {
                                    gc.setFont(child.getFont());
                                    gc.setColor(child.getForeground());
                                    int cx = Math.max(0, child.getX()-minx);
                                    int cy = Math.max(0, child.getY()-miny);
                                    gc.clipRect(cx, cy, child.getWidth(), child.getHeight());
                                    
                                    gc.translate(cx, cy);
                                    child.paint(gc);
                                    gc.translate(-cx, -cy);
                                }
                                finally
                                {
                                    gc.dispose();
                                }
                            }

                            JTCableManager cm = container.getCableManager();
                            if (cm != null)
                            {
                                //Rectangle vr = cm.getVisibleRegion();
                                try
                                {
                                  //  cm.setVisibleRegion(minx, miny, w, h);
                                    g2.translate(-minx, -miny);
                                    cm.paintCables(g2);
                                    g2.translate(minx, miny);
                                }
                                finally
                                {
                                    //cm.setVisibleRegion(vr);
                                }
                            }
                        }
                        finally
                        {
                            g2.dispose();
                        }
                        imageList.add(bi);
                    }
                }
                finally
                {
                    container.setSize(previousSize);
                }
            }
        }
        
        return imageList.toArray(new Image[imageList.size()]);
    }
    
}
