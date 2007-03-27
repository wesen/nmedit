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
package net.sf.nmedit.jtheme.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.editor.misc.RectangularPattern;

public class ComponentView extends JComponent
{

    private JComponent view;
    private ComponentSink sink;
    private ComponentListener viewListener;
    private ComponentSelector selector;
    
    public ComponentView()
    {
        setOpaque(true);
        setBackground(Color.WHITE);
        sink = new ComponentSink(this);
        
        add(sink);
        viewListener = new ViewEventDispatcher(this);
        
        selector = new ComponentSelector(sink);
    }
    
    public JComponent getSink()
    {
        return sink;
    }

    public Component[] getSelection()
    {
        return selector.getSelection();
    }
    
    public void addSelectionChangeListener(ChangeListener l)
    {
        listenerList.add(ChangeListener.class, l);
    }
    
    public void removeSelectionChangeListener(ChangeListener l)
    {
        listenerList.remove(ChangeListener.class, l);
    }
    
    private transient Paint backgroundPattern = null;
    private transient Rectangle cachedRect;
    
    protected void paintComponent(Graphics g)
    {
        if (backgroundPattern == null)
        {
            backgroundPattern = RectangularPattern.create();
        }
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(backgroundPattern);
        
        if (cachedRect == null)
            cachedRect = new Rectangle();
        cachedRect = g.getClipBounds(cachedRect);
        
        g2.fillRect(cachedRect.x, cachedRect.y, cachedRect.width, cachedRect.height);
    }
    
    protected void fireSelectionChangeEvent()
    {
        ChangeListener[] list = listenerList.getListeners(ChangeListener.class);
        if (list.length == 0)
            return;
        
        ChangeEvent e = new ChangeEvent(this);
        for (int i=0;i<list.length;i++)
            list[i].stateChanged(e);
    }
    
    public Dimension getMinimumSize()
    {
        JComponent min = view;
        if (min == null)
            return super.getMinimumSize();
        
        return min.getSize();
    }
    
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }
    
    public Dimension getMaximumSize()
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public void setView(JComponent view)
    {
        JComponent oldView = this.view;
        
        if (oldView != view)
        {
            if (oldView != null)
                uninstall(oldView);
            this.view = view;
            if (view != null)
                install(view);

            updateViewBounds();
        }
    }
    
    protected void install(JComponent view)
    {
        view.addComponentListener(viewListener);
    }
    
    protected void uninstall(JComponent view)
    {
        view.removeComponentListener(viewListener);
    }

    public JComponent getView()
    {
        return view; 
    }

    public void doLayout()
    {
        // optimization
        updateViewBounds();
    }

    public void setLayout(LayoutManager layout)
    {
        throw new UnsupportedOperationException("layout not supported");
    }
    
    private static boolean isEachZero(int a, int b, int c, int d)
    {
        return a == 0 && b == 0 && c == 0 && d == 0;
    }
    
    private void updateViewBounds()
    {
        if (view == null)
        {
            if (!isEachZero(sink.getX(), sink.getY(), sink.getWidth(), sink.getHeight()))
            {
                sink.setBounds(0, 0, 0, 0);
                repaint();
            }
            return;
        }
        
        if (!isEachZero(view.getX(), view.getY(), 0, 0))
            view.setLocation(0, 0);

        synchronized (sink.getTreeLock())
        {
        Rectangle oldBounds = sink.getBounds();
        Rectangle newBounds = new Rectangle(
                (getWidth()-view.getWidth())/2,
                (getHeight()-view.getHeight())/2,
                view.getWidth(), view.getHeight()
        );
        
        sink.setBounds(newBounds);
        Rectangle union = oldBounds;
        SwingUtilities.computeUnion(newBounds.x, newBounds.y, newBounds.width, newBounds.height, union);
        repaint(union.x, union.y, union.width, union.height);
        }
    }
    
    protected static class ComponentSink extends JComponent
        implements MouseListener
    {
        
        private ComponentView componentView;
        private boolean noInvalidate = false;

        public ComponentSink(ComponentView componentView)
        {
            this.componentView = componentView;
            setOpaque(true);
            setFocusable(true);
            setBackground(Color.WHITE);
        }

        protected void paintChildren(Graphics g)
        {
            super.paintChildren(g);
            paintSelector(g);
        }

        public void paintSelector(Graphics g)
        {
            if (hasFocus())
                Selector.paintSelector(g, this, null);
        }

        public boolean isOptimizedDrawingEnabled()
        {
            return false;
        }
        
        public void paintComponent(Graphics g)
        {   
            JComponent component = componentView.getView();
            if (component == null)
            {
                super.paintComponent(g);
                return ;
            }

            try
            {
                // component must have a parent
                synchronized(getTreeLock())
                {
                    noInvalidate = true;
                }
                add(component, 0);
                
                // paint view
                g.setFont(component.getFont());
                g.setColor(component.getForeground());
                component.paint(g);
            }
            finally
            {
                // remove component again
                remove(component);
                
                synchronized(getTreeLock())
                {
                    noInvalidate = false;
                }
            }
        }
        
        public void invalidate()
        {
            synchronized (getTreeLock())
            {
                if (noInvalidate)
                    return;
            }
            super.invalidate();
        }

        public void setLayout(LayoutManager layout)
        {
            throw new UnsupportedOperationException("layout not supported");
        }

        public void mouseClicked(MouseEvent e)
        {
            // no op
        }

        public void mouseEntered(MouseEvent e)
        {
            // no op
        }

        public void mouseExited(MouseEvent e)
        {
            // no op
        }

        public void mousePressed(MouseEvent e)
        {
            if (!hasFocus())
                requestFocus();
        }

        public void mouseReleased(MouseEvent e)
        {
            // no op
        }
    }

    private static class ViewEventDispatcher extends ComponentAdapter
    {
        private ComponentView target;

        public ViewEventDispatcher(ComponentView target)
        {
            this.target = target;
        }
        
        public void componentResized(ComponentEvent e)
        {
            target.revalidate();
            target.repaint();
        }

        public void componentMoved(ComponentEvent e)
        {
            target.revalidate();
            target.repaint();
        }
    }

    private class ComponentSelector implements MouseListener, MouseMotionListener
    {
        
        private Set<Selector> selectors = new HashSet<Selector>();
        private Set<Component> selection = new HashSet<Component>();
        private ComponentSink sink;

        public ComponentSelector(ComponentSink sink)
        {
            this.sink = sink;
            install(sink);
        }
        
        public Component[] getSelection()
        {
            return selection.toArray(new Component[selection.size()]);
        }
        
        public void install(ComponentSink sink)
        {
            sink.addMouseListener(this);
            sink.addMouseMotionListener(this);
        }
        
        public int getSelectionCount()
        {
            return selection.size();
        }
        
        public boolean hasSelection()
        {
            return getSelectionCount() > 0;
        }
        
        public boolean isSelected(Component c)
        {
            return selection.contains(c);
        }
        
        public Component locateComponent(int x, int y)
        {
            Container parent = view;
            if (parent == null)
                return null;
            
            return parent.getComponentAt(x, y);
        }
        
        public void setSelection(Component c)
        {
            if ((c == null && !hasSelection())||(getSelectionCount()==1 && isSelected(c)))
                return ;

            if (c == sink)
                return ;
            
            if (c == view)
                c = null;
            
            selection.clear();
            if (c != null)
                selection.add(c);
            updateSelectors();
            
        }
        
        private void updateSelectors()
        {
            for (Selector selector : selectors)
            {
                selector.uninstall();
                sink.remove(selector);
            }
            selectors.clear();
            for (Component c : selection)
            {
                Selector selector = new Selector((JComponent)c);
                selectors.add(selector);
                sink.add(selector);
            }
            sink.repaint();
            fireSelectionChangeEvent();
        }

        public void setSelection(int x, int y)
        {
            setSelection(locateComponent(x, y));
        }
        
        public void mouseClicked(MouseEvent e)
        {
            // no op
        }

        public void mouseEntered(MouseEvent e)
        {
            // no op
        }

        public void mouseExited(MouseEvent e)
        {
            // no op
        }

        public void mousePressed(MouseEvent e)
        {
        }

        public void mouseReleased(MouseEvent e)
        {
            setSelection(e.getX(), e.getY());
        }

        public void mouseDragged(MouseEvent e)
        {
            // no op
        }

        public void mouseMoved(MouseEvent e)
        {
            // no op
        }
        
    }

    public static void setLocation(JComponent component, Point location)
    {
        setLocation(component, location.x, location.y);
    }
    
    public static void setLocation(JComponent component, int x, int y)
    {
        JComponent parent = (JComponent) component.getParent();
        if (parent == null)
        {
            component.setLocation(x, y);
            return;
        }
        
        Rectangle bounds = parent.getBounds();
        Insets insets = parent.getInsets();
        bounds.x = insets.left;
        bounds.y = insets.top;
        bounds.width -= (insets.left+insets.right);
        bounds.height -= (insets.top+insets.bottom);
        int br = bounds.x+bounds.width;
        int bb = bounds.y+bounds.height;
        
        if (x+component.getWidth()>br)
            x = br-component.getWidth();
        if (y+component.getHeight()>bb)
            y = bb-component.getHeight();
        if (x<bounds.x) x=bounds.x;
        if (y<bounds.y) y=bounds.y;
        
        component.setLocation(x, y);
    }
    
    public static void setSize(JComponent component, int width, int height)
    {
        JComponent parent = (JComponent) component.getParent();
        if (parent == null)
        {
            component.setSize(width, height);
            return;
        }
        Insets insets = parent.getInsets();
        int maxr = parent.getWidth()-insets.right;
        int maxb = parent.getHeight()-insets.bottom;
        if (component.getX()+width>maxr)
            width = maxr-component.getX();
        if (component.getY()+height>maxb)
            height = maxb-component.getY();
            
        component.setSize(width, height);
    }
    
    private static class Selector extends JComponent implements 
        MouseMotionListener, MouseListener 
    {
        
        private static Stroke dashStroke = new BasicStroke(
          1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
          1, new float[]{4}, 0 
        );
        
        private JComponent selection;
        private BoundaryUpdater boundaryUpdaterA;
        private BoundaryUpdater boundaryUpdaterB;
        private Rectangle resizeRect = new Rectangle();
        private boolean resizeMode = false;

        private boolean updateLocation;

        public Selector(JComponent selection)
        {
            this(selection, true);
        }
        
        public Selector(JComponent selection, boolean updateLocation)
        {
            setOpaque(false);
            this.selection = selection;
            this.updateLocation = updateLocation;
            boundaryUpdaterA = new BoundaryUpdater(this, selection, updateLocation);            
            boundaryUpdaterB = new BoundaryUpdater(selection, this, updateLocation);
            boundaryUpdaterA.install();
            boundaryUpdaterB.install();
            boundaryUpdaterB.apply();
            setFocusable(true);
            requestFocus();
            addMouseListener(this);
            addMouseMotionListener(this);
            setToolTipText(selection.getClass().getName());
        }
        
        public void uninstall()
        {
            boundaryUpdaterA.uninstall();
            boundaryUpdaterB.uninstall();
        }
        
        public Rectangle getResizeRect()
        {
            return resizeRect = getResizeRect(this, resizeRect);
        }
        
        public static Rectangle getResizeRect(JComponent c, Rectangle r)
        {
            if (r == null)
                r = new Rectangle();
            final int d = 4;
            r.setBounds(c.getWidth()-1-d, c.getHeight()-1-d, d+1, d+1);
            return r;
        }
        
        protected void paintChildren(Graphics g)
        {
            // has no children
        }
        
        protected void paintComponent(Graphics g)
        {
            paintSelector(g, this, resizeRect);
        }
        
        public static void paintSelector(Graphics g, JComponent c, Rectangle resizeRect)
        {
            Graphics2D g2 = (Graphics2D) g;
            final Stroke oldStroke = g2.getStroke();
            
            g2.setStroke(dashStroke);
            g.setColor(Color.RED);
            g.drawRect(0, 0, c.getWidth()-1, c.getHeight()-1);

            Rectangle rr = getResizeRect(c, resizeRect);
            g2.setStroke(oldStroke);
            g.setColor(Color.WHITE);
            g.fillRect(rr.x, rr.y, rr.width, rr.height);
            g.setColor(Color.BLACK);
            g.fillRect(rr.x, rr.y, rr.width-1, rr.height-1);
        }

        private Point dragStartLocation = null;
        private Dimension dragStartSize = null;
        
        public void mouseDragged(MouseEvent e)
        {
            Point p = dragStartLocation;
            Dimension d = dragStartSize;
            if (p == null || d == null)
                return;

            if (resizeMode)
            {
                // resize mode
                final int minsize = 4;
                int neww = Math.max(minsize, d.width+e.getX()-p.x);
                int newh = Math.max(minsize, d.height+e.getY()-p.y);
                
                ComponentView.setSize(selection, neww, newh);
            }
            else
            {
                if (updateLocation)
                {
                    // move mode
                    int newx = getX()+e.getX()-p.x;
                    int newy = getY()+e.getY()-p.y;
                    ComponentView.setLocation(selection, newx, newy);
                }
            }
        }

        public void mouseMoved(MouseEvent e)
        {
            // no op
        }

        public void mouseClicked(MouseEvent e)
        {
            // no op
        }

        public void mouseEntered(MouseEvent e)
        {
            // no op
        }

        public void mouseExited(MouseEvent e)
        {
            // no op
        }

        public void mousePressed(MouseEvent e)
        {
            dragStartLocation = e.getPoint();
            dragStartSize = getSize();
            resizeMode = getResizeRect().contains(e.getX(), e.getY());
        }

        public void mouseReleased(MouseEvent e)
        {
            dragStartLocation = null;
            dragStartSize = null;
        }
        
    }
    private static class BoundaryUpdater extends ComponentAdapter
    {
        private Component source;
        private Component target;
        private boolean installed = false;
        private boolean updateLocation;

        public BoundaryUpdater(Component source, Component target, boolean updateLocation)
        {
            this.updateLocation = updateLocation;
            this.source = source;
            this.target = target;
        }
        
        public void install()
        {
            if (installed)
                return;
            installed = true;
            source.addComponentListener(this);
        }
        
        public void uninstall()
        {
            source.removeComponentListener(this);
            installed = false;
        }
        
        public void componentMoved(ComponentEvent e)
        {
            apply();
        }

        public void componentResized(ComponentEvent e)
        {
            apply();
        }

        public void apply()
        {
            if (source.getX()==target.getX() 
                    && source.getY()==target.getY()
                    && source.getWidth()==target.getWidth() 
                    && source.getHeight()==target.getHeight())
            {
                return;
            }
            
            if (updateLocation)
                target.setBounds(source.getBounds());
            else
                target.setSize(source.getSize());
        }
        
    }
    
}

