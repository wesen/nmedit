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
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.Signal;
import net.sf.nmedit.jtheme.JTCursor;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.DragCable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTConnector;

public class JTBasicConnectorUI extends JTConnectorUI
{

    private static JTBasicConnectorUI instance = new JTBasicConnectorUI();
    
    public static JTBasicConnectorUI createUI(JComponent c)
    {
        return instance;
    }
    
    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        JTConnector connector = (JTConnector) c;
        Signal signal = connector.getSignal();
        paintConnector(g, connector, signal, connector.isOutput(), connector.isConnected(), c.hasFocus());
    }
    
    protected int getSize(JTConnector c)
    {
        int size = Math.min(c.getWidth(), c.getHeight()) -1;
        if (size % 2 == 0) size --;
        return size;
    }
    
    protected void paintConnector(Graphics2D g, JTConnector c, Signal signal, boolean output, 
            boolean connected, boolean focused)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        /*Color outline = c.hasFocus() ? Color.BLUE : Color.BLACK;
        g.setColor(outline);*/
        
        int size = getSize(c);
        
        g.setColor(signal.getColor());
        if (output)
        {
            if (connected) g.fillRect(0, 0, size, size);
            g.drawRect(0, 0, size-1, size-1);
        }
        else
        {
            if (connected) g.fillOval(0, 0, size-1, size-1);
            g.drawOval(0, 0, size-1, size-1);
        }
    }

    protected BasicConnectorListener createConnectorListener(JComponent c)
    {
        return new BasicConnectorListener();
    }
    
    protected BasicConnectorListener getConnectorListener(JComponent c)
    {
        MouseListener[] listeners = c.getListeners(MouseListener.class);
        
        if (listeners != null)
        {
            for (int i=0;i<listeners.length;i++)
            {
                MouseListener l = listeners[i];
                if (l instanceof BasicConnectorListener)
                {
                    return (BasicConnectorListener) l;
                }
            }
        }
        return null;
    }
    
    public void installUI(JComponent c)
    {
        c.setOpaque(false);
        c.setCursor(JTCursor.getJackCursor(JTCursor.JACK1));
        
        // TODO this should be defined somewhere else
        c.setFocusable(true);
        BasicConnectorListener listener = createConnectorListener(c);
        if (listener != null)
        {
            listener.installListener(c);
        }
    }
    
    public void uninstallUI(JComponent c)
    {
        c.setCursor(null);
        
        BasicConnectorListener listener = getConnectorListener(c);
        if (listener != null)
        {
            listener.uninstallListener(c);
        }
    }
    
    public static class BasicConnectorListener
        implements MouseListener, MouseMotionListener, FocusListener
    {

        public void installListener(JComponent c)
        {
            installMouseListener(c);
            installMouseMotionListener(c);
            installFocusListener(c);
        }

        protected void installMouseMotionListener(JComponent c)
        {
            c.addMouseMotionListener(this);
        }

        protected void installMouseListener(JComponent c)
        {
            c.addMouseListener(this);
        }

        public void installFocusListener(JComponent c)
        {
            c.addFocusListener(this);
        }
        
        protected void uninstallMouseMotionListener(JComponent c)
        {
            c.removeMouseMotionListener(this);
        }

        protected void uninstallMouseListener(JComponent c)
        {
            c.removeMouseListener(this);
        }

        public void uninstallFocusListener(JComponent c)
        {
            c.removeFocusListener(this);
        }
        
        public void uninstallListener(JComponent c)
        {
            uninstallMouseListener(c);
            uninstallMouseMotionListener(c);
            uninstallFocusListener(c);
        }

        public void mouseDragged(MouseEvent e)
        {
            updateDragLocation((JTConnector) e.getComponent(), e.getX(), e.getY());
        }

        public void mouseMoved(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseClicked(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseEntered(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseExited(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mousePressed(MouseEvent e)
        {
            JTConnector c = (JTConnector) e.getComponent();
            if (!c.hasFocus())
                c.requestFocus();

            if (!SwingUtilities.isLeftMouseButton(e))
                return ;
            
            int clicks = e.getClickCount();
            if (clicks == 1)
            {
                startDragNewCable(c, e.getX(), e.getY());
            }
            else if (clicks == 2)
            {
                startDragCurrentCables(c, e.getX(), e.getY());
            }
            
        }

        public void mouseReleased(MouseEvent e)
        {
            stopDrag((JTConnector) e.getComponent(), e.getX(), e.getY());
        }

        public void focusGained(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void startDragNewCable(JTConnector c, int x, int y)
        {
            dragCreate = true;
            JTCableManager cableManager = c.getCableManager();
            
            if (cableManager != null && cableManager.getView() != null)
            {
                dragSource = c;
                DragCable drag = new DragCable(cableManager.createCable(c, null));
                cables = new Cable[] {drag};
                cableManager.add(drag);
                
                drag.setStartLocation(c);
                updateDragLocation(c, x, y);
            }
        }
        
        protected JTConnector findConnectorAt(JTConnector otherConnector, int modContX, int modContY)
        {
            Component c = otherConnector.getParent().getParent().findComponentAt(modContX, modContY);
            if (c instanceof JTConnector)
                return (JTConnector)c;
            return null;
        }
        
        protected void updateDragLocation(JTConnector c, int x, int y)
        {
            if (!isDragging())
                return ;

            JTCableManager cableManager = c.getCableManager();
            if (cableManager == null)
                return;

            Point stop = SwingUtilities.convertPoint(c, new Point(x, y), cableManager.getView()); 
            JTConnector target = findConnectorAt(c, stop.x, stop.y);
            if (target != null)
                DragCable.setLocation(stop, target);
            
            for (int i=cables.length-1;i>=0;i--)
            {
                Cable cable = cables[i];
                cableManager.markDirty(cable);
                cable.setEndPoints(cable.getStart(), new Point(stop));
                cableManager.markDirty(cable);
            }
            
            cableManager.markCompletelyDirty();
            cableManager.notifyRepaintManager();
        }
        
        public void startDragCurrentCables(JTConnector c, int x, int y)
        {
            // TODO Auto-generated method stub
            
        }
        
        public void stopDrag(JTConnector c, int x, int y)
        {
            if (!isDragging())
                return ;
            dragSource = null;

            JTCableManager cableManager = c.getCableManager();

            if (cableManager != null)
            {
                for (Cable cable : cables)
                    cableManager.remove(cable);
                cableManager.notifyRepaintManager();
            }
            cables = NO_CABLES;

            Point stop = SwingUtilities.convertPoint(c, new Point(x, y), cableManager.getView()); 
            JTConnector target = findConnectorAt(c, stop.x, stop.y);

            if (target != null)
            {
                if (dragCreate)
                {
                    Connector a = c.getConnector();
                    Connector b = target.getConnector();
                    if (a == null && b == null)
                    {
                        Cable cable = cableManager.createCable(c, target);
                     
                        cableManager.add(cable);
                        cableManager.notifyRepaintManager();
                    }
                    else if (a != null && b != null)
                    {
                        a.getConnectionManager().connect(a, b);
                    }
                }
            }
        }
        
        public boolean isDragging()
        {
            return dragSource != null;
        }
        
        private static final Cable[] NO_CABLES = new Cable[0];
        private JTConnector dragSource;
        private Cable[] cables = NO_CABLES;
        private boolean dragCreate = true;
        
        public Cable[] getConnectedCables(JTConnector c)
        {
            JTCableManager cableManager = c.getCableManager();
            List<Cable> cableList = new ArrayList<Cable>();
            for (Cable cable : cableManager)
            {
                if (cable.getSourceComponent() == c || cable.getDestinationComponent()==c)
                    cableList.add(cable);
            }
            if (cableList.isEmpty())
                return NO_CABLES;
            
            return cableList.toArray(new Cable[cableList.size()]);
        }

    }

}

