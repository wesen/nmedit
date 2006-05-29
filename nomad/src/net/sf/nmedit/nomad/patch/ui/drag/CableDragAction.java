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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.nomad.patch.ui.Cable;
import net.sf.nmedit.nomad.patch.ui.CableDisplay;
import net.sf.nmedit.nomad.patch.ui.Curve;
import net.sf.nmedit.nomad.patch.virtual.Connector;
import net.sf.nmedit.nomad.theme.component.NomadConnector;
import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;
import net.sf.nmedit.nomad.util.graphics.shape.RenderOp;

public abstract class CableDragAction extends PaintAbleDragAction
{
    
    protected List<Curve> curveList = new ArrayList<Curve>();
    private RenderOp renderOp;
    private int renderFlags;
    private NomadConnector start;
    private Point startNCPos = new Point();
    private Point tmp = new Point();
    private CableDisplay cd;
    private Rectangle latestBounds = new Rectangle();
    private Rectangle bounds = new Rectangle();
    private Rectangle dirtyRegion = new Rectangle(); 
    private NomadConnector downUnder = null;
    
    public CableDragAction( NomadConnector source, int startX, int startY, CableDisplay cd, int renderFlags )
    {
        super( source, startX, startY );
        this.cd = cd;
        start = source;
        this.renderOp = cd.getRenderOp();
        this.renderFlags = renderFlags;
        cd.getLocation(start, startNCPos);
        
        bounds.setBounds(0,0,0,0);
        latestBounds.setBounds(0,0,0,0);
    }

    public Cable[] getCables(Connector c)
    {
        return cd.getCables(c);
    }
    
    public NomadConnector getDownUnder()
    {
        return downUnder;
    }
    
    public void dragged()
    {
        super.dragged();
        
        latestBounds.setBounds(bounds);
        bounds.setBounds(0,0,0,0);
        
        for (int i=curveList.size()-1;i>=0;i--)
        {
            Rectangle b = curveList.get(i).getBounds();
            GraphicsToolkit.union(bounds, bounds.x,bounds.y, bounds.width,bounds.height,
                    b.x,b.y,b.width,b.height);
        }
        
        
        NomadConnector over = cd.findConnectorAt(getAbsoluteX(), getAbsoluteY());
        if (over!=downUnder)
        {
            if (downUnder!=null)
                exitConnector(downUnder);
            downUnder = over;
            if (downUnder!=null)
                enterConnector(downUnder);
        }
    }
    
    public int getAbsoluteX()
    {
        return startNCPos.x+getDeltaX();
    }
    
    public int getAbsoluteY()
    {
        return startNCPos.y+getDeltaY();   
    }

    public void enterConnector(NomadConnector nc){}
    public void exitConnector(NomadConnector nc){}
    
    public Rectangle getDirtyRegion()
    {
        dirtyRegion.setBounds(latestBounds);
        GraphicsToolkit.union(dirtyRegion, bounds.x, bounds.y,bounds.width,bounds.height,
                latestBounds.x,latestBounds.y,latestBounds.width,latestBounds.height);
        return dirtyRegion; 
    }
    
    public Point getStartConnectorLocation()
    {
        return startNCPos;
    }
    
    public Point getDeltaStartConnectorLocation()
    {
        tmp.setLocation(startNCPos.x+getDeltaX(), startNCPos.y+getDeltaY());
        return tmp;
    }
    
    public Point getLocation(NomadConnector nc)
    {
        cd.getLocation(nc, tmp);
        return tmp;
    }
    
    public NomadConnector getStart()
    {
        return start;
    }
    
    public void add(Curve cable)
    {
        curveList.add(cable);
    }
    
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        renderOp.configure(g2, renderFlags);
        for (int i=curveList.size()-1;i>=0;i--)
        {
            renderOp.render(g2, curveList.get(i));
        }
        g2.dispose();
    }

}
