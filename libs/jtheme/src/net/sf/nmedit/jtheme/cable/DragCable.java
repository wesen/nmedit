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
import net.sf.nmedit.jtheme.component.JTConnector;

public class DragCable implements Cable
{

    private Cable peer;
    private Point start;
    private Point stop;

    public DragCable(Cable peer)
    {
        this.peer = peer;
        start = peer.getStart();
        stop = peer.getStop();
    }
    
    public void setStartLocation(JTConnector location)
    {
        setLocation(start, location);
        updateEndPoints();
    }
    
    public void setStopLocation(JTConnector location)
    {
        setLocation(stop, location);
        updateEndPoints();
    }

    public static void setLocation(Point dst, JTConnector location)
    {
        int x = location.getX()+(location.getWidth()/2);
        int y = location.getY()+(location.getHeight()/2);
        
        Container module = location.getParent();
        if (module != null)
        {
            x+=module.getX();
            y+=module.getY();
        }
        dst.setLocation(x, y);
    }

    public Point getStart()
    {
        return new Point(start);
    }
    
    public Point getStop()
    {
        return new Point(stop);
    }

    public void setStart(Point p)
    {
        setStart(p.x, p.y);
    }
    
    public void setStart(int x, int y)
    {
        start.setLocation(x, y);
        updateEndPoints();
    }

    public void setStop(Point p)
    {
        setStop(p.x, p.y);
    }
    
    public void setStop(int x, int y)
    {
        stop.setLocation(x, y);
        updateEndPoints();
    }
    
    public Cable getPeer()
    {
        return peer;
    }
    
    public Color getColor()
    {
        return peer.getColor();
    }
    
    public void setColor(Color color)
    {
        peer.setColor(color);
    }
    
    private PConnector getConnectorSavely(JTConnector c)
    {
        return c == null ? null : c.getConnector();
    }

    public PConnector getDestination()
    {
        return getConnectorSavely(getDestinationComponent());
    }

    public JTConnector getDestinationComponent()
    {
        return peer.getDestinationComponent();
    }

    public PConnector getSource()
    {
        return getConnectorSavely(getSourceComponent());
    }

    public JTConnector getSourceComponent()
    {
        return peer.getSourceComponent();
    }

    public void updateEndPoints()
    {
        peer.setEndPoints(start.x, start.y, stop.x, stop.y);
    }

    public Rectangle getBounds()
    {
        return peer.getBounds();
    }

    public Rectangle getBounds(Rectangle r)
    {
        return peer.getBounds(r);
    }

    public Shape getShape()
    {
        return peer.getShape();
    }

    public boolean intersects(int x, int y, int width, int height)
    {
        return peer.intersects(x, y, width, height);
    }

    public boolean intersects(Rectangle r)
    {
        return peer.intersects(r);
    }

    public void setEndPoints(int x1, int y1, int x2, int y2)
    {
        start.setLocation(x1, y1);
        stop.setLocation(x2, y2);
        updateEndPoints();
    }

    public void setEndPoints(Point p1, Point p2)
    {
        setEndPoints(p1.x, p1.y, p2.x, p2.y);
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
        return peer.getShake();
    }

    public void setShake(double shake)
    {
        peer.setShake(shake);
    }

    public void shake()
    {
        peer.shake();
    }

}

