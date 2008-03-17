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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnectionManager;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PSignal;
import net.sf.nmedit.jpatch.PSignalTypes;
import net.sf.nmedit.jpatch.history.PUndoableEditSupport;
import net.sf.nmedit.jtheme.JTCursor;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.DragCable;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.graphics.RoundGradientPaint;

public class JTBasicConnectorUI extends JTConnectorUI
{

    private static UIInstance<JTConnectorUI> uiInstance = new UIInstance<JTConnectorUI>(JTConnectorUI.class);
    
    public static JTConnectorUI createUI(JComponent c)
    {
        JTConnectorUI ui = uiInstance.getInstance(c);
        if (ui == null) uiInstance.setInstance(c, ui = new JTBasicConnectorUI());
        return ui;
    }
    
    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        JTConnector con;
        try
        {
            con = (JTConnector) c;
        }
        catch (ClassCastException e)
        {
            throw e;
        }
        PSignal signal = con.getSignal();
        
        Color color = signal == null ? Color.BLACK : signal.getColor();
        
        int size = diameter(c.getWidth(), c.getHeight());
        paintConnector(g, size, color, con.isOutput(), con.isConnected());
    }
    
    protected int getSize(JTConnector c)
    {
        int size = Math.min(c.getWidth(), c.getHeight()) -1;
        if (size % 2 == 0) size --;
        return size;
    }
    
    private static int diameter(int w, int h)
    {
        int d = Math.min(w, h);
        return d - (d+1)%2;
    }

    private static final Color OUTLINE = new Color(0,0,0,.5f);
    private static final Color RGP_INNER = new Color(0,0,0,.85f);
    private static final Color RGP_OUTER = new Color(0,0,0,.05f);
    
    protected static int computeHash(int size, Color fill, boolean output, boolean connected)
    {
        return ((fill.hashCode()*size)<<2)|(output?2:0)|(connected?1:0);
    }
    
    private static class CachedConnector
    {
        private int size;
        private Color fill;
        private boolean output;
        private boolean connected;
        private int hashCode;
        private Image image;

        public CachedConnector(int size, Color fill, boolean output, boolean connected, int hashCode, Image image)
        {
            this.size = size;
            this.fill = fill;
            this.output = output;
            this.connected = connected;
            this.hashCode = hashCode;
            this.image = image;
        }
        
        public int hashCode()
        {
            return hashCode;
        }
        
        public boolean equals(Object o)
        {
            if (o == null || o.hashCode() != hashCode) return false;
            if (o == this) return true;
            if (!(o instanceof CachedConnector)) return false;
            
            CachedConnector c = (CachedConnector) o;
            return equals(c.size, c.fill, c.output, c.connected);
        }
        
        public boolean equals(int size, Color fill, boolean output, boolean connected)
        {
            return size==this.size && fill.getRGB()==this.fill.getRGB()&&output==this.output&&connected==this.connected;
        }
    }

    private transient SoftReference<Map<Integer, CachedConnector>> cache ; 
    
    protected Image getConnector(int size, Color fill, boolean output, boolean connected)
    {
        final int hash = computeHash(size, fill, output, connected);
        
        Map<Integer, CachedConnector> map = null;
        if (cache != null) map = cache.get();
        if (map == null)
        {
            map = new HashMap<Integer, CachedConnector>();
            cache = new SoftReference<Map<Integer,CachedConnector>>(map);
        }
        else
        {
            CachedConnector c = map.get(hash);
            
            if (c != null && c.equals(size, fill, output, connected))
            {
                // found
                return c.image;
            }
        }
        
        // render new image
        BufferedImage bi = new BufferedImage(size, size, output ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        try
        {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            renderConnector(g2, size, fill, output, connected);
        }
        finally
        {
            g2.dispose();
        }
        
        map.put(hash, new CachedConnector(size, fill, output, connected, hash, bi));
        return bi;   
    }
    
    
    
    protected void paintConnector(Graphics2D g, int s, Color fill, boolean output, boolean connected)
    {

        /*
        renderConnector(g, s, fill, output, connected);
        */
        
        Image buf = getConnector(s, fill, output, connected);
        g.drawImage(buf, 0, 0, null);
    }
    
    protected void renderConnector(Graphics2D g, final int s, Color fill, boolean output, boolean connected)
    {
        int c = (s+1)/2; // (s mod 2) == 1 

        Color bright = fill.brighter().brighter();
        g.setPaint(new GradientPaint(0, 0, fill, s, s, bright));

        if (output) 
        {
            g.fillRect(0, 0, s, s);
            g.setPaint(new GradientPaint(0, 0, bright, c, c, fill));
            g.fillRect(0, 0, c, c);
        }
        else g.fillOval(0, 0, s, s);
        
        final int xy = (int)(s*3f/16f);
        c++;

        float c2 = c/2f; 
        float sxy = xy+c2;
        g.setPaint(new RoundGradientPaint(sxy, sxy, c2, RGP_INNER, RGP_OUTER));
        g.fillOval(xy, xy, c, c);
        
        if (connected)
        {
            g.setColor(fill);
            g.fillOval(xy, xy, c, c);
        }

        // outlines
        g.setColor(OUTLINE);
        if (output) g.drawRect(0, 0, s-1, s-1);
        else g.drawOval(0, 0, s-1, s-1);
    }

    private transient BasicConnectorListener connectorListenerInstance;
    
    protected BasicConnectorListener createConnectorListener(JComponent c)
    {
        if (connectorListenerInstance == null)
            connectorListenerInstance = new BasicConnectorListener();
        return connectorListenerInstance;
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
            //c.addFocusListener(this);
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
            //c.removeFocusListener(this);
        }
        
        public void uninstallListener(JComponent c)
        {
            uninstallMouseListener(c);
            uninstallMouseMotionListener(c);
            uninstallFocusListener(c);
        }

        public void mouseDragged(MouseEvent e)
        {
            JTConnector c = (JTConnector) e.getComponent();

            if (pressClickCount == 1)
            {
                if (!isDragging())
                {
                    startDragNewCable(c, e.getX(), e.getY());
                }
            }
            else if (pressClickCount == 2)
            {
                if (!isDragging())
                {
                startDragCurrentCables(c, e.getX(), e.getY());
                }
            }
            updateDragLocation(c, e.getX(), e.getY());
        }

        public void mouseMoved(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseClicked(MouseEvent e)
        {
        }

        public void mouseEntered(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseExited(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }
        
        private int pressClickCount = 0;
        
        public void mousePressed(MouseEvent e)
        {
            pressClickCount = 0;
            
            JTConnector c = (JTConnector) e.getComponent();
            if (!c.hasFocus())
                c.requestFocus();

            if (!Platform.isLeftMouseButtonOnly(e))
                return ;
        
            pressClickCount = e.getClickCount();
        }

        public void mouseReleased(MouseEvent e)
        {
            pressClickCount = 0;
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
            if (isDragging())
                return;

            dragCreate = true;
            JTCableManager cableManager = c.getCableManager();

            if (cableManager != null && cableManager.getView() != null)
            {
                dragSource = c;

                DragCable drag = new DragCable(cableManager.createCable(c, null));
                
                PConnectorDescriptor cd = c.getConnectorDescriptor();
                if (cd != null)
                {
                    PSignalTypes signalTypes = 
                        cd.getParentDescriptor().getModules().getDefinedSignals();
                    PSignal noSignal = signalTypes.noSignal();
                    if (noSignal != null && noSignal.getColor() != null)
                        drag.setColor(noSignal.getColor());
                }
                
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

            Point stop = SwingUtilities.convertPoint(c, new Point(x, y), cableManager.getOwner()); 
            
            JTConnector target = findConnectorAt(c, stop.x, stop.y);
            
            if (target != null)
                DragCable.setLocation(stop, target);
            
            if (connectedCables != null)
            {
                cableManager.update(connectedCables);
                for (Cable cable:connectedCables)
                {
                    Point cstart = cable.getStart();
                    Point cstop = cable.getStop();
                    
                    if (cable.getSource() == c.getConnector()) {
                        cstart.setLocation(stop);
                    } else {
                        cstop.setLocation(stop);
                    }
                    
                    cable.setEndPoints(cstart, cstop);
                }
                cableManager.update(connectedCables);
                
                scrollToVisible(cableManager.getOwner(), c, x, y);
                
                return;
            }

            cableManager.update(cables);
            for (int i=cables.length-1;i>=0;i--)
            {
                Cable cable = cables[i];
                cable.setEndPoints(cable.getStart(), new Point(stop));
            }
            cableManager.update(cables);
            
            
            scrollToVisible(cableManager.getOwner(), c, x, y);
        }
        
        private void scrollToVisible(JComponent view, JComponent c, int x, int y)
        {
            Point p = new Point(x, y);
            
            p = SwingUtilities.convertPoint(c, p, view);
            
            Rectangle r = new Rectangle(p.x-10, p.y-10, 20, 20);
            
            SwingUtilities.computeIntersection(0, 0, view.getWidth(), view.getHeight(), r);
            
            view.scrollRectToVisible(r);
        }
        
        private transient Cable[] connectedCables;
        
        public void startDragCurrentCables(JTConnector c, int x, int y)
        {
            if (isDragging())
                return;
            
            connectedCables = c.getConnectedCables();
            
            if (connectedCables.length>0)
                dragSource = c;
            else 
                connectedCables = null;
        }
        
        public void stopDrag(JTConnector c, int x, int y)
        {
            if (!isDragging())
                return ;
            
            dragSource = null;

            JTCableManager cableManager = c.getCableManager();

            Point stop = SwingUtilities.convertPoint(c, new Point(x, y), cableManager.getOwner());
            JTConnector target = findConnectorAt(c, stop.x, stop.y);

            if (connectedCables != null)
            {
                PConnectionManager cm =
                    c.getConnector().getConnectionManager();

                cableManager.update(connectedCables);
                for (Cable cable: connectedCables)
                {
                    cable.updateEndPoints();
                }
                cableManager.update(connectedCables);
                
                
                PUndoableEditSupport ues = c.getConnector().getEditSupport();
                
                try
                {
                    if (ues != null)
                        ues.beginUpdate();
                    
                    
                    if (target != null)
                    {
                        Collection<PConnection> cc = cm.connections(c.getConnector());
                        cm.removeAllConnections(cc);
    
                        for (PConnection con: cc)
                        {
                            PConnector b = con.getA();
                            if (c.getConnector() == b) b = con.getB();
                            
                            b.connect(target.getConnector());
                        }
                    }
                    else
                    {
                    	// disconnect cables connected to this connector
                    	c.getConnector().disconnect();
                    }
                }
                finally
                {
                    if (ues != null)
                        ues.endUpdate();
                }

                connectedCables = null;
                cables = JTConnector.NO_CABLES;
                //System.out.println("X not deleted(cables=):"+cables.length);
                return;
            }

            if (cableManager != null)
            {
                cableManager.remove(cables);
            }
            if (target != null)
            {
                if (dragCreate)
                {
                    PConnector a = c.getConnector();
                    PConnector b = target.getConnector();
                    if (a == null && b == null)
                    {
                        Cable cable = cableManager.createCable(c, target);
                     
                        if (cables.length == 1)
                            cable.setShake(cables[0].getShake());
                        cableManager.add(cable);
                    }
                    else if (a != null && b != null)
                    {
                        // TODO create new cable with same 'shake' value
                        a.getConnectionManager().add(a, b);
                    }
                }
            }
            cables = JTConnector.NO_CABLES;

        }
        
        public boolean isDragging()
        {
            return dragSource != null;
        }
        
        private JTConnector dragSource;
        private Cable[] cables = JTConnector.NO_CABLES;
        private boolean dragCreate = true;
        
    }

}

