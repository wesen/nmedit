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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public class ModuleDragAction extends PaintAbleDragAction implements Iterable<ModuleUI>
{
    
    private ModuleSelectionTool tool;
    private List<Rectangle> bounds = new ArrayList<Rectangle>();
    private Rectangle globalBounds = new Rectangle();
    private Rectangle dirtyRegion = new Rectangle();
    private boolean copyAction = false;

    public ModuleDragAction( Component source, int startX, int startY, ModuleSelectionTool selection )
    {
        super( source, startX, startY );
        
        tool = selection;
        globalBounds.x = getStartX();
        globalBounds.y = getStartY();
        globalBounds.width = 0;
        globalBounds.height = 0;

        for (ModuleUI m : selection)
            addBounds(m);
    }
    
    public void copyModules(VoiceArea va, int pxX, int pxY)
    {
        Point tl = tool.getTopLeftPX();
        Point p = ModuleUI.Metrics.getBestGridLocation(tl.x+pxX,tl.y+pxY);
        
        tl = tool.getTopLeft();
        tool.copyTo(va, p.x-tl.x, p.y-tl.y); 
    }
    
    public void mouseReleased( MouseEvent e )
    {
        copyAction = e.isControlDown();
        super.mouseReleased(e);
    }

    public boolean isCopyAction()
    {
        return copyAction; 
    }
    
    public void moveModules(int pxX, int pxY)
    {
        tool.moveSelection(pxX, pxY);
    }
    
    private void addBounds(ModuleUI module)
    {
        int x = module.getX();
        int y = module.getY();
        int w = module.getWidth();
        int h = module.getHeight();
        
        bounds.add(new Rectangle(x, y, w, h));

        GraphicsToolkit.union(globalBounds, x, y, w, h);   
    }
    
    public void addModule(ModuleUI module)
    {
        tool.add(module);

        addModule(module);
    }
    
    public Set<ModuleUI> getModules()
    {
        return tool.getModuleUIs();
    }

    public Iterator<ModuleUI> iterator()
    {
        return tool.iterator();
    }
    
    protected void start()
    {
        install();
    }
    
    protected void stop()
    {
        super.stop();
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
        Point p2 = new Point(globalBounds.x+getLastDeltaX(), globalBounds.y+getLastDeltaY());
        Point p1 = new Point(globalBounds.x+getDeltaX(), globalBounds.y+getDeltaY()); 
        

        p1 = ModuleUI.Metrics.getBestGridLocation(p1.x, p1.y);
        p2 = ModuleUI.Metrics.getBestGridLocation(p2.x, p2.y);
        p1.x = ModuleUI.Metrics.getPixelX(p1.x);
        p1.y = ModuleUI.Metrics.getPixelY(p1.y);
        p2.x = ModuleUI.Metrics.getPixelX(p2.x);
        p2.y = ModuleUI.Metrics.getPixelY(p2.y);
        
        GraphicsToolkit.union(dirtyRegion,
                p2.x, p2.y, globalBounds.width, globalBounds.height,
                p1.x, p1.y, globalBounds.width, globalBounds.height);
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
    {/*
        Point p = ModuleUI.Metrics.getBestGridLocation(bounds.x+getDeltaX(), bounds.y+getDeltaY());
        p.x = ModuleUI.Metrics.getPixelX(p.x);
        p.y = ModuleUI.Metrics.getPixelY(p.y);
        g.drawRect(p.x, p.y, bounds.width-1, bounds.height-1);*/
    }
    
    public static int al(int xy, int cellsz)
    {
        return xy-(xy % cellsz);
    }
    
}
