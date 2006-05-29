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

/*
 * Created on May 1, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Transparency;

import javax.swing.JComponent;

import net.sf.nmedit.nomad.main.background.Background;
import net.sf.nmedit.nomad.main.background.BackgroundFactory;

public class NComponent extends JComponent
{
    
    private Background background = BackgroundFactory.createDefaultBackground();
    private Insets insets = null;
    private boolean contentAreaFilled = true;
    private boolean borderAreaFilled = false;

    public NComponent()
    {
    }

    public void setBackgroundB(Background b)
    {
        if (this.background!=b)
        {
            if (b==null)
            {
                b = BackgroundFactory.createDefaultBackground();
            }
            this.background = b;
            setOpaque(isOpaqueInternal());
            repaint();
        }
    }
    
    protected boolean isBorderAreaFilled()
    {
        return borderAreaFilled;
    }
    
    protected void setBorderAreaFilled(boolean filled)
    {
        if (borderAreaFilled!=filled)
        {
            borderAreaFilled = filled;
            repaint();
        }
    }
    
    public Background getBackgroundB()
    {
        return background;
    }
    
    public boolean isOpaque()
    {
        return isOpaqueInternal();
    }
    
    private boolean isOpaqueInternal()
    {
        return contentAreaFilled && (background.getTransparency()==Transparency.OPAQUE);
    }
    
    public boolean isContentAreaFilled()
    {
        return contentAreaFilled;
    }
    
    public void setContentAreaFilled(boolean f)
    {
        if (f!=contentAreaFilled)
        {
            boolean wasOpaque = isOpaque();
            this.contentAreaFilled = f;
            if (wasOpaque!=isOpaque())
            {
                setOpaque(isOpaqueInternal());
                repaint();
            }
        }
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }
    
    protected void paintBackground(Graphics g)
    {
        if (isContentAreaFilled())
        {
            if (isBorderAreaFilled() || (getBorder()!=null && !getBorder().isBorderOpaque()))
            {
                background.paintBackground(this, g,
                  0, 0, getWidth(), getHeight());
            }
            else
            {
                // get insets and reuse object if possible
                insets = getInsets(insets);
                background.paintBackground(this, g, 
                        insets.left,
                        insets.top, 
                        getWidth()-insets.left-insets.right, 
                        getHeight()-insets.top-insets.bottom);
            }
        }
    }
    
    protected void paintComponent(Graphics g)
    {
        paintBackground(g);
        super.paintComponent(g);
    }
    
}
