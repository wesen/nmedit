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
package net.sf.nmedit.jtheme.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MouseInputListener;

public class RetargetMouseEventSupport implements MouseInputListener, MouseWheelListener
{

    private boolean isDragging = false;
    private transient Component mouseEventTarget;
    private Component dragTarget;
    private Component retargetSource;

    private RetargetMouseEventSupport(Component retargetSource)
    {
        this.retargetSource = retargetSource;
    }
    
    private static RetargetMouseEventSupport TO_PARENT = new RetargetMouseEventSupport(null);
    
    public static RetargetMouseEventSupport retargetToParent(Component c)
    {
        return TO_PARENT;
    }
    
    public static RetargetMouseEventSupport createSupport(Component c)
    {
        return new RetargetMouseEventSupport(c);
    }

    public void mouseClicked(MouseEvent e)
    {
        handleEvent(e);
    }

    public void mouseEntered(MouseEvent e)
    {
        handleEvent(e);
    }

    public void mouseExited(MouseEvent e)
    {
        handleEvent(e);
    }

    public void mousePressed(MouseEvent e)
    {
        handleEvent(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        handleEvent(e);
    }

    public void mouseDragged(MouseEvent e)
    {
        handleEvent(e);
    }

    public void mouseMoved(MouseEvent e)
    {
        handleEvent(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        handleEvent(e);
    }

    protected Component getTarget(MouseEvent e)
    {
        Component c = e.getComponent();
        return c.getParent();
    }
    
    private void handleEvent(MouseEvent e)
    {
      Component target = getTarget(e);
      if (target == null) return;

      int id = e.getID();
      switch (id)
      {
          case MouseEvent.MOUSE_ENTERED:
              if (!isDragging)
              {
                  mouseEventTarget = target;
                  redispatch(id, e, mouseEventTarget);
              }
              break;
        case MouseEvent.MOUSE_EXITED:
            if (!isDragging)
            {
                redispatch(id, e, mouseEventTarget);
            }
            break;
        case MouseEvent.MOUSE_PRESSED:
            mouseEventTarget = target;
            redispatch(id, e, mouseEventTarget);
            // Start dragging.
            dragTarget = target;
            break;
        case MouseEvent.MOUSE_RELEASED:
            if (isDragging)
            {
                redispatch(id, e, dragTarget);
                isDragging = false;
            }
            else
                redispatch(id, e, mouseEventTarget);
            break;
        case MouseEvent.MOUSE_CLICKED:
            redispatch(id, e, mouseEventTarget);
            break;
        case MouseEvent.MOUSE_MOVED:
            if (target != mouseEventTarget)
            {
              // Create additional MOUSE_EXITED/MOUSE_ENTERED pairs.
              redispatch(MouseEvent.MOUSE_EXITED, e, mouseEventTarget);
              mouseEventTarget = target;
              redispatch(MouseEvent.MOUSE_ENTERED, e, mouseEventTarget);
            }
            redispatch(id, e, mouseEventTarget);
            break;
        case MouseEvent.MOUSE_DRAGGED:
            if (! isDragging) isDragging = true;
            redispatch(id, e, mouseEventTarget);
            break;
        case MouseEvent.MOUSE_WHEEL:
            redispatch(id, e, mouseEventTarget);
            break;
        default:
            break;
      }
    }

    private void redispatch(int id, MouseEvent e, Component target)
    {
        if (target == null) // just in case
            return;
        
        Component source = retargetSource;
        if (source == null)
            source = e.getComponent();
        Point p = SwingUtilities.convertPoint(source, e.getX(), e.getY(), target);
        if (e instanceof MouseWheelEvent)
        {
            MouseWheelEvent w = (MouseWheelEvent) e;
            MouseEvent ev = new MouseWheelEvent(target, id, e.getWhen(),
                    e.getModifiers() | e.getModifiersEx(),
                    p.x, p.y, e.getClickCount(),
                    e.isPopupTrigger(),
                    w.getScrollType(),
                    w.getScrollAmount(),
                    w.getWheelRotation());
            target.dispatchEvent(ev);
        }
        else if (e instanceof MenuDragMouseEvent) {
            // no op
        }
        else if (e instanceof MouseEvent)
        {
            MouseEvent ev = new MouseEvent(target, id, e.getWhen(),
                                           e.getModifiers() | e.getModifiersEx(),
                                           p.x, p.y, e.getClickCount(),
                                           e.isPopupTrigger());
            target.dispatchEvent(ev);
        }
    }

    public void install(Component c)
    {
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
        c.addMouseWheelListener(this);
    }

    public void uninstall(Component c)
    {
        c.removeMouseListener(this);
        c.removeMouseMotionListener(this);
        c.removeMouseWheelListener(this);
    }
    
}
