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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.Border;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.plaf.JTModuleContainerUI;

public class JTModuleContainer extends JTBaseComponent
{
    
    public static final String uiClassId = "ModuleContainerUI";
    
    private boolean optimizedDrawing;
    private JTCableManager cableManager;
    private JTPatch patchContainer;

    public JTModuleContainer(JTContext context, JTCableManager cableManager)
    {
        super(context);
        setOpaque(true);
        optimizedDrawing = true;// does not work: !context.hasModuleContainerOverlay();
                                // we have to overwrite boolean isPaintingOrigin() which is package private 
        setCableManager(cableManager);
    }
    
    public JTPatch getPatchContainer()
    {
        return patchContainer;
    }
    
    public void setPatchContainer(JTPatch patchContainer)
    {
        this.patchContainer = patchContainer;
    }

    public JTModuleContainerUI getUI()
    {
        return (JTModuleContainerUI) ui;
    }
    
    public void setUI(JTModuleContainerUI ui)
    {
        super.setUI(ui);
    }
    
    public String getUIClassID()
    {
        return uiClassId;
    }

    protected void setCableManager(JTCableManager cableManager)
    {
        JTCableManager oldManager = this.cableManager;
        if (oldManager != cableManager)
        {
            if (oldManager != null)
                oldManager.setView(null);
            this.cableManager = cableManager;
            if (cableManager != null)
                cableManager.setView(this);
        }
    }
    
    public JTCableManager getCableManager()
    {
        return cableManager;
    }
    
    public boolean isOptimizedDrawingEnabled()
    {
        return optimizedDrawing;
    }

    /**
     * Calls {@link #paint(Graphics)}.
     */
    public void update(Graphics g)
    {
        paint(g);
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
    }
    
    public void setBorder(Border border)
    {
        if (border != null)
            throw new UnsupportedOperationException("border not supported");
    }
    
    /*public void setLayout(LayoutManager layout)
    {
        if (layout != null)
            throw new UnsupportedOperationException("layout not supported");
    }*/
    
    protected final void paintBorder(Graphics g)
    {
        // no op
    }
    
    protected void paintChildren(Graphics g)
    {
        super.paintChildren(g);
        
        JTCableManager cableManager = getCableManager();
        if (cableManager != null)
        {
            Graphics gs = g.create();
            try
            {
                cableManager.paintCables((Graphics2D) gs);
            }
            finally
            {
                gs.dispose();
            }
        }

        if (ui != null)
        {
            getUI().paintChildrenHack(g);
        }
    }
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }

    public Dimension computePreferredSize(Dimension dim)
    {
        if (dim == null) 
            dim = new Dimension(0,0);
        else
            dim.setSize(0,0);
        
        for (int i=getComponentCount()-1;i>=0;i--)
        {
            Component c = getComponent(i);

            dim.width = Math.max(dim.width, c.getX()+c.getWidth());
            dim.height = Math.max(dim.height, c.getY()+c.getHeight());
            
        }
        
        return dim;
    }

    public boolean isDnDAllowed()
    {
        return getContext().isDnDAllowed();
    }

}
