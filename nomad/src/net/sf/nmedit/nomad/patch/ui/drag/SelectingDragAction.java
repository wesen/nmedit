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

import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public abstract class SelectingDragAction extends PaintAbleDragAction
{
    
    private Rectangle lastSelection = new Rectangle();    
    private Rectangle selection = new Rectangle();
    private Rectangle dirtyRegion = new Rectangle();

    public SelectingDragAction( Component source, int startX, int startY )
    {
        super( source, startX, startY );
        selection.x = startX;
        selection.y = startY;
        selection.width = 0;
        selection.height = 0;
        lastSelection.setBounds(selection);
    }

    public void dragged()
    {
        super.dragged();
        lastSelection.setBounds(selection);
        selection.x = Math.min(getStartX(), getCurrentX());
        selection.y = Math.min(getStartY(), getCurrentY());
        selection.width = Math.abs(getDeltaX())+1;
        selection.height = Math.abs(getDeltaY())+1;
    }
    
    protected Rectangle getDirtyRegion()
    {
        GraphicsToolkit.union(dirtyRegion,
                selection.x, selection.y, selection.width, selection.height,
                lastSelection.x, lastSelection.y, lastSelection.width, lastSelection.height
        );
        return dirtyRegion; 
    }
    
    public Rectangle getLastSelection()
    {
        return lastSelection;
    }
    
    public Rectangle getSelection()
    {
        return selection;
    }
    
    public void paint(Graphics g)
    {
        paintSelection(g);
    }
    
    public void paintSelection(Graphics g)
    {
        g.drawRect(selection.x, selection.y, selection.width-1, selection.height-1);
    }
    
}
