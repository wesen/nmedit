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
package net.sf.nmedit.jtheme.cable;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTConnector;

public class SimpleCable implements Cable
{
    
    private CableGeometrie cableGeometrie;
    private Color color;
    private JTConnector sourceComponent;
    private JTConnector destinationComponent;

    public SimpleCable(JTConnector source, JTConnector destination)
    {
        this(source, destination, null);
    }

    public SimpleCable(JTConnector source, JTConnector destination, CableGeometrie cableGeometrie)
    {
        this.sourceComponent = source;
        this.destinationComponent = destination;
        this.cableGeometrie = cableGeometrie != null ? cableGeometrie : createCableGeometrieInstance();
        this.update(cableGeometrie);
    }
    
    protected CableGeometrie createCableGeometrieInstance()
    {
        return new LineCableGeometrie();
    }
    
    public CableGeometrie getCableGeometrie()
    {
        return cableGeometrie;
    }
    
    public void updateEndPoints()
    {
        update(cableGeometrie);
    }

    protected void update(CableGeometrie cableGeometrie)
    {
        int sx = 0;
        int sy = 0;
        int dx = 0;
        int dy = 0;
        
        JTComponent sc = getSourceComponent();
        if (sc != null && sc.getParent() != null)
        {
            Container psc = sc.getParent();
            sx = psc.getX() + sc.getX() + (sc.getWidth()/2 );
            sy = psc.getY() + sc.getY() + (sc.getHeight()/2);
        }
        
        JTComponent dc = getDestinationComponent();
        if (dc != null && dc.getParent() != null)
        {
            Container pdc = dc.getParent();
            dx = pdc.getX() + dc.getX() + (dc.getWidth()/2 );
            dy = pdc.getY() + dc.getY() + (dc.getHeight()/2);
        }
        setEndPoints(sx, sy, dx, dy);
    }

    public Color getColor()
    {
        return color;
    }
    
    public void setColor(Color color)
    {
        this.color = color;
    }

    public Shape getShape()
    {
        return getCableGeometrie().getShape();
    }

    public PConnector getSource()
    {
        return getSourceComponent().getConnector();
    }

    public JTConnector getSourceComponent()
    {
        return sourceComponent;
    }

    public PConnector getDestination()
    {
        return getDestinationComponent().getConnector();
    }

    public JTConnector getDestinationComponent()
    {
        return destinationComponent;
    }

    public Rectangle getBounds()
    {
        return getCableGeometrie().getBounds();
    }

    public Rectangle getBounds(Rectangle r)
    {
        return getCableGeometrie().getBounds(r);
    }

    public boolean intersects(int x, int y, int width, int height)
    {
        return getCableGeometrie().intersects(x, y, width, height);
    }

    public boolean intersects(Rectangle r)
    {
        return getCableGeometrie().intersects(r);
    }

    public void setEndPoints(int x1, int y1, int x2, int y2)
    {
        getCableGeometrie().setEndPoints(x1, y1, x2, y2);
    }
    
    public void setEndPoints(Point p1, Point p2)
    {
        getCableGeometrie().setEndPoints(p1, p2);
    }
    
    public Point getStart()
    {
        return getCableGeometrie().getStart();
    }

    public Point getStop()
    {
        return getCableGeometrie().getStop();
    }

    public boolean contains(PConnector c)
    {
        return getDestination() == c || getSource() == c;
    }

    public boolean contains(PModule m)
    {
        return getDestinationModule() == m || getSourceModule() == m;
    }

    public PModule getDestinationModule()
    {
        PConnector c = getDestination();
        return c == null ? null : c.getParentComponent();
    }

    public PModule getSourceModule()
    {
        PConnector c = getSource();
        return c == null ? null : c.getParentComponent();
    }

    public double getShake()
    {
        return cableGeometrie.getShake();
    }

    public void setShake(double shake)
    {
        cableGeometrie.setShake(shake);
    }

    public void shake()
    {
        cableGeometrie.shake();
    }

}

