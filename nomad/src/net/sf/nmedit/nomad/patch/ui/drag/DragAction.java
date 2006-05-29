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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DragAction implements MouseListener, MouseMotionListener, KeyListener
{

    private Component source;
    private int startX;
    private int startY;
    private int currentX;
    private int currentY;
    private int dx;
    private int dy;
    private int dx2;
    private int dy2;
    
    public DragAction(Component source, int startX, int startY)
    {
        this.source = source;
        this.startX = startX;
        this.startY = startY;
        this.currentX = startX;
        this.currentY = startY;
        dx = 0;
        dy = 0;
        dx2 = 0;
        dy2 = 0;
        
        start();
    }
    
    public void translateDelta(int x, int y)
    {
        dx+=x;
        dy+=y;
    }
    
    protected void start()
    {
        install();
    }
    
    protected void stop()
    {
        uninstall();
    }

    protected void escape()
    {
    }
    
    protected void dragged()
    {
        
    }
    
    public Component getSource()
    {
        return source;
    }
    
    public int getStartX()
    {
        return startX;
    }
    
    public int getStartY()
    {
        return startY;
    }
    
    public int getDeltaX()
    {
        return dx;
    }
    
    public int getDeltaY()
    {
        return dy;
    }
    
    public int getLastDeltaX()
    {
        return dx2;
    }
    
    public int getLastDeltaY()
    {
        return dy2;
    }
    
    public int getCurrentX()
    {
        return currentX;
    }
    
    public int getCurrentY()
    {
        return currentY;
    }
    
    protected void install()
    {
        source.addMouseListener(this);
        source.addMouseMotionListener(this);
        source.addKeyListener(this);
    }
    
    protected void uninstall()
    {
        source.removeMouseListener(this);
        source.removeMouseMotionListener(this);
        source.removeKeyListener(this);
    }

    public void mouseReleased( MouseEvent e )
    {
        stop();
    }

    public void mouseDragged( MouseEvent e )
    {
        currentX = e.getX();
        currentY = e.getY();
        dx2 = dx;
        dy2 = dy;
        dx = currentX-startX;
        dy = currentY-startY;
        dragged();
    }

    public void keyPressed( KeyEvent e )
    {
        // TODO why is event not caught ???
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE)
            escape();
    }

    public void mouseClicked( MouseEvent e )
    {
    }

    public void mousePressed( MouseEvent e )
    {
    }

    public void mouseEntered( MouseEvent e )
    {
    }

    public void mouseExited( MouseEvent e )
    {
    }

    public void mouseMoved( MouseEvent e )
    {
    }

    public void keyTyped( KeyEvent e )
    {
    }

    public void keyReleased( KeyEvent e )
    {
    }

}
