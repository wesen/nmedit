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
 * Created on May 5, 2006
 */
package net.sf.nmedit.nomad.patch.ui.drag;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public class ModuleDragAction extends PaintAbleDragAction implements Iterable<ModuleUI>
{
    
    private List<ModuleUI> modules = new ArrayList<ModuleUI>();
    private List<Rectangle> bounds = new ArrayList<Rectangle>();
    private Rectangle globalBounds = new Rectangle();
    private Rectangle dirtyRegion = new Rectangle();

    public ModuleDragAction( Component source, int startX, int startY )
    {
        super( source, startX, startY );

        globalBounds.x = getStartX();
        globalBounds.y = getStartY();
        globalBounds.width = 0;
        globalBounds.height = 0;
    }

    public void addModule(ModuleUI module)
    {
        int x = module.getX();
        int y = module.getY();
        int w = module.getWidth();
        int h = module.getHeight();
        
        modules.add(module);
        bounds.add(new Rectangle(x, y, w, h));

        GraphicsToolkit.union(globalBounds, x, y, w, h);
    }
    
    public ModuleUI[] getModules()
    {
        return modules.toArray(new ModuleUI[modules.size()]);
    }

    public Iterator<ModuleUI> iterator()
    {
        return modules.iterator();
    }
    
    protected void start()
    {
        install();
    }
    
    protected void stop()
    {
        super.stop();
        modules.clear();
        bounds.clear();
    }
    
    protected void dragged()
    {
    }
    
    public Rectangle getBounds()
    {
        return new Rectangle(globalBounds);
    }
    
    protected Rectangle getDirtyRegion()
    {
        GraphicsToolkit.union(dirtyRegion,
            globalBounds.x+getLastDeltaX(),
            globalBounds.y+getLastDeltaY(),
            globalBounds.width,
            globalBounds.height,
            globalBounds.x+getDeltaX(),
            globalBounds.y+getDeltaY(),
            globalBounds.width,
            globalBounds.height);
        return dirtyRegion;
    }
    
    public void paint(Graphics g)
    {   
        for (int i=bounds.size()-1;i>=0;i--)
        {
            paintBounds(g, bounds.get(i));
        }
    }
    
    public void paintBounds(Graphics g, Rectangle bounds)
    {
        int x = bounds.x+getDeltaX();
        int y = bounds.y+getDeltaY();
        g.drawRect(x, y, bounds.width-1, bounds.height-1);
    }
    
}
