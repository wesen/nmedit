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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.component.JTButtonControl;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;

public class JTBasicButtonControlUI extends JTButtonControlUI
{
    
    protected JTButtonControl btn;
    protected int hoverIndex = -1;
    protected int selectionOffset = 1;
    protected int padding = 2;
    
    public JTBasicButtonControlUI(JTButtonControl btn)
    {
        this.btn = btn;
        
        //btn.setCyclic(true);
        btn.setOrientation(SwingConstants.HORIZONTAL);
    }

    public static JTButtonControlUI createUI(JComponent c)
    {
        return new JTBasicButtonControlUI((JTButtonControl)c);
    }

    public void installUI(JComponent c)
    {
        c.setOpaque(false); // because of spacing
        
        BasicButtonListener listener = createBasicButtonListener(btn);
        listener.install(btn);

        // TESTING
        /*
        btn.setText(0, "Hallo");
        btn.setText(1, "Hallo");
        btn.setText(2, "Hallo");
        //btn.setText(3, "Hallo");
        
        btn.setIcon(0, createIcon());
        btn.setIcon(1, createIcon());
        // btn.setIcon(2, createIcon());
        btn.setIcon(3, createIcon());
        
        btn.setAdapter(new JTDefaultControlAdapter(0, 3, 1));*/
        
      //  btn.setFont(new Font("dialog", Font.PLAIN, 10));
    }
    
    static Icon createIcon()
    {
        BufferedImage b = new BufferedImage(13, 13, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = b.createGraphics();
        try
        {
            Color c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());

            Color c2 = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
            
            g.setPaint(new GradientPaint(0, 0, c, b.getWidth(), b.getHeight(), c2));
            g.fillRect(0, 0, b.getWidth(), b.getHeight());
            
            g.setPaint(new GradientPaint(0, 0, Color.BLACK, b.getWidth(), b.getHeight(), Color.WHITE));
            g.drawRect(0, 0, b.getWidth()-1, b.getHeight()-1);
            
        }
        finally
        {
            g.dispose();
        }
        return new ImageIcon(b);
    }
    
    public void uninstallUI(JComponent c)
    {
        BasicButtonListener listener = getBasicButtonListener(btn);
        listener.uninstall(btn);

        
    }
    
    protected void setHoverIndex(int hoverIndex)
    {
        if (this.hoverIndex != hoverIndex)
        {
            this.hoverIndex = hoverIndex;
            btn.repaint();
        }
    }
    
    private transient Insets cachedInsets;
    private transient Rectangle cachedRect;
    
    protected Insets getCachedInsets()
    {
        if (cachedInsets == null)
            cachedInsets = new Insets(0,0,0,0);
        return cachedInsets;
    }
    
    protected Rectangle getCachedRect()
    {
        return cachedRectangle(cachedRect);
    }
    
    private static boolean eq(Object a, Object b)
    {
        return a == b || (a != null && a.equals(b));
    }

    private int range(JTControl c)
    {
        return c.getMaxValue()-c.getMinValue()+1;
    }
    
    protected Rectangle cachedRectangle(Rectangle r)
    {
        return r == null ? new Rectangle() : r;
    }
    
    protected Insets cachedInsets(Insets i)
    {
        return i == null ? new Insets(0,0,0,0) : i;
    }
    
    protected boolean labelsValid()
    {
        if (btnLabels == null || btnLabels.length != range(btn)) return false;
        for (int i=btn.getMinValue();i<=btn.getMaxValue();i++)
            if (!eq(btn.getText(i), btnLabels[i])) return false;
        return true; 
    }
    
    protected boolean iconsValid()
    {
        if (btnIcons == null || btnIcons.length != range(btn)) return false;
        for (int i=btn.getMinValue();i<=btn.getMaxValue();i++)
            if (btn.getIcon(i)!=btnIcons[i]) return false;
        return true; 
    }
    
    protected void updateLabels()
    {
        if (btnLabels == null || btnLabels.length != range(btn))
            btnLabels = new String[range(btn)];

        Arrays.fill(btnLabels, null);
        maxLabel = -1;
        for (int i=btn.getMinValue();i<=btn.getMaxValue();i++)
        {
            btnLabels[i]= btn.getText(i);
            if (btnLabels[i] != null)
                maxLabel = i;
        }
    }
    
    protected void updateIcons()
    {
        if (btnIcons == null || btnIcons.length != range(btn))
            btnIcons = new Icon[range(btn)];
        
        Arrays.fill(btnIcons, null);
        maxIcon = -1;
        for (int i=btn.getMinValue();i<=btn.getMaxValue();i++)
        {
            btnIcons[i] = btn.getIcon(i);
            if (btnIcons[i] != null)
                maxIcon = i;
        }
    }
    
    protected boolean checkComputationIsValid()
    {
        boolean compute = false; 
        if (!iconsValid()) 
        {
            updateIcons();
            compute = true;
        }
        
        if (!labelsValid()) 
        {
            updateLabels();
            compute = true;
        }
        
        compute |= buttonRect == null;

        if (!btn.isCyclic())
        {
            if (orientation != btn.getOrientation())
            {
                orientation = btn.getOrientation();
                compute = true;
            }
        }
     
        if (btn.isCyclic()!=cyclic)
        {
            cyclic = btn.isCyclic();
            compute = true;
        }
        
        return compute;
    }
    
    protected void checkComputation()
    {
        if (checkComputationIsValid())
            compute(btnLabels, btnIcons);
    }
    
    // TODO buttonRect used without null checks
    private transient Rectangle buttonRect;
    private transient int orientation;
    private transient boolean cyclic;
    private transient String[] btnLabels;
    private transient Icon[] btnIcons;
    private transient int maxIcon = -1;
    private transient int maxLabel = -1;
    private transient int maxIconWidth = 0;
    private transient int maxIconHeight = 0;
    
    protected void compute(String[] labels, Icon[] icons)
    {
        buttonRect = cachedRectangle(buttonRect);
        buttonRect.setBounds(0, 0, 0, 0);
        
        for (int i=0;i<maxIcon;i++)
        {
            Icon ic = icons[i];
            if (ic != null)
            {
                buttonRect.width = Math.max(buttonRect.width, ic.getIconWidth());
                buttonRect.height = Math.max(buttonRect.height, ic.getIconHeight());
            }
        }

        int iw = buttonRect.width;
        int ih = buttonRect.height;

        maxIconWidth = iw;
        maxIconHeight = ih;
        
        Font f = btn.getFont();
        if (f == null)
            return;
        
        FontMetrics fm = btn.getFontMetrics(f);
        
        for (int i=0;i<maxLabel;i++)
        {
            String l = labels[i];
            if (l != null)
            {
                int sw = SwingUtilities.computeStringWidth(fm, l);
                int sh = fm.getHeight();
                
                buttonRect.width = Math.max(buttonRect.width, sw+iw+2);
                buttonRect.height = Math.max(buttonRect.height, Math.max(sh,ih));
            }
        }

        buttonRect.width += padding*2;
        buttonRect.height += padding*2; 

        buttonRect.width += selectionOffset;
        buttonRect.height += selectionOffset;
     
        Insets insets = getBorderInsets(null);
        if (insets != null)
        {
            buttonRect.width += insets.left+insets.right;
            buttonRect.height += insets.top+insets.bottom;
        }
        
    }

    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        checkComputation();
        
        final int btnCount = btn.isCyclic() ? 1 : range(btn);
        final int spacing = btn.getSpacing();
        paintButtonBackgrounds(g, btn, btnCount, spacing, btn.getOrientation());
    }

    protected void paintButtonBackgrounds(Graphics2D g, JTButtonControl c, 
            int btnCount, int spacing, int orientation)
    {   

        if (c.isCyclic())
        {
            int x = 0;
            int y = 0;
            
            g.setColor(Color.LIGHT_GRAY);           
            g.fillRect(x, y, buttonRect.width, buttonRect.height);   
            return;
        }
        
        if (orientation == SwingConstants.HORIZONTAL)
        {
            
            for (int i=0;i<btnCount;i++)
            {
                int x = i*(buttonRect.width);
                if (i>0) x+=(i)*spacing;
                int y = 0;

                g.setColor(Color.LIGHT_GRAY);           
                g.fillRect(x, y, buttonRect.width, buttonRect.height);   
            }
            
        }
        else // vertical
        {
            for (int i=0;i<btnCount;i++)
            {
                int x = 0;
                int y = i*(buttonRect.height);
                if (i>0) y+=(i)*spacing;
                
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x, y, buttonRect.width, buttonRect.height);
            }
        }
    }
    
    protected void paintButtonBorder(Graphics2D g, JTButtonControl c, int x, int y, int w, int h
            , boolean selected, boolean hovered)
    {
        final Color clHover = Color.WHITE;
        final Color clBorder = Color.BLACK;
        final Color clSelected = Color.BLUE;

        if (selected)
            g.setColor(clSelected);
        else if (hovered)
            g.setColor(clHover);
        else
            g.setColor(clBorder);     

        g.drawRect(x, y, buttonRect.width-1, buttonRect.height-1);
    }

    protected boolean mouseDown = false;
    
    protected void setMousePressed(boolean pressed)
    {
        if (mouseDown != pressed)
        {
            this.mouseDown = pressed;
            
            if (btn.isCyclic())
                btn.repaint();
        }
    }
    
    protected void paintButtonBorders(Graphics2D g, JTButtonControl c, 
            int btnCount, int spacing, int orientation)
    {   
              
        if (c.isCyclic())
        {
            int x = 0;
            int y = 0;
            
            boolean selected = mouseDown || c.getValue()==1;
            
            // TODO selected?, hovered?
            paintButtonBorder(g, c, x, y, buttonRect.width, buttonRect.height, selected, false);
            return;
        }
        
        int selIndex = btn.getValue()-btn.getMinValue();
        if (orientation == SwingConstants.HORIZONTAL)
        {
            
            for (int i=0;i<btnCount;i++)
            {
                int x = i*(buttonRect.width);
                if (i>0) x+=(i)*spacing;
                int y = 0;

                boolean selected = (i == selIndex);
                boolean hovered = (i == hoverIndex);
                
                paintButtonBorder(g, c, x, y, buttonRect.width, buttonRect.height,selected, hovered);
            }
            
        }
        else // vertical
        {
            for (int i=0;i<btnCount;i++)
            {
                int x = 0;
                int y = i*(buttonRect.height);
                if (i>0) y+=(i)*spacing;

                boolean selected = (i == selIndex);
                boolean hovered = (i == hoverIndex);

                paintButtonBorder(g, c, x, y, buttonRect.width, buttonRect.height,selected, hovered);
            }
        }
    }

    protected void paintButtonLabels(Graphics2D g, JTButtonControl c, 
            int btnCount, int spacing, int orientation)
    {   
        
        Insets insets = getBorderInsets(null);
        if (insets == null)
            insets = new Insets(0,0,0,0);
        
        g.setColor(Color.black);

        Font f = g.getFont();
        FontMetrics fm = btn.getFontMetrics(f);
        
        if (c.isCyclic())
        {

            int x = 0;
            int y = 0;

            x+=padding;
            y+=padding;
            
            int i = c.getValue();
            
            Icon icon = c.getIcon(i+c.getMinValue());
            String text = c.getText(i+c.getMinValue());

            if (icon != null)                
                icon.paintIcon(c, g, x+insets.left, y+insets.top);
            x+= padding+maxIconWidth;
            
            if (text != null)
                g.drawString(text, x+insets.left, y+insets.top+(fm.getAscent()));
            
            return;
        }
        
        int selected = btn.getValue()-btn.getMinValue();
        
        if (orientation == SwingConstants.HORIZONTAL)
        {
            
            for (int i=0;i<btnCount;i++)
            {
                int x = i*(buttonRect.width);
                if (i>0) x+=(i)*spacing;
                int y = 0;
                
                if (i == selected)
                {
                    x+=selectionOffset;
                    y+=selectionOffset;
                }

                x+=padding;
                y+=padding;
                
                Icon icon = c.getIcon(i+c.getMinValue());
                String text = c.getText(i+c.getMinValue());

                if (icon != null)                
                    icon.paintIcon(c, g, x+insets.left, y+insets.top);
                x+= padding+maxIconWidth;
                
                if (text != null)
                    g.drawString(text, x+insets.left, y+insets.top+(fm.getAscent()));
            }
            
        }
        else // vertical
        {
            for (int i=0;i<btnCount;i++)
            {
                int x = 0;
                int y = i*(buttonRect.height);
                if (i>0) y+=(i)*spacing;
                
                if (i == selected)
                {
                    x+=selectionOffset;
                    y+=selectionOffset;
                }

                x+=padding;
                y+=padding;
                
                Icon icon = c.getIcon(i+c.getMinValue());
                String text = c.getText(i+c.getMinValue());
                
                if (icon != null)
                    icon.paintIcon(c, g, x+insets.left, y+insets.top);
                x+= padding+maxIconWidth;
                
                if (text != null)
                    g.drawString(text, x+insets.left, y+insets.top+(fm.getAscent()));
            }
        }
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        checkComputation();
        
        final int btnCount = btn.isCyclic() ? 1 : range(btn);
        final int spacing = btn.getSpacing();
        
        paintButtonBorders(g, btn, btnCount, spacing, btn.getOrientation());
        paintButtonLabels(g, btn, btnCount, spacing, btn.getOrientation());
    }
    
    protected Insets getBorderInsets(Insets insets)
    {
        return null;
    }
    
    public Dimension getPreferredSize(JComponent c)
    {
        checkComputation();
        
        Dimension d = buttonRect.getSize();
        
        if (btn.isCyclic()) return d;
        
        int space = btn.getSpacing();
        
        if (btn.getOrientation() == SwingConstants.HORIZONTAL)
        {
            d.width+=space;
            d.width*= range(btn);
        }
        else
        {
            d.height+=space;
            d.height*=range(btn);
        }
        return d;
    }
    
    protected BasicButtonListener createBasicButtonListener(JTButtonControl btn)
    {
        return new BasicButtonListener(this);
    }
    
    protected BasicButtonListener getBasicButtonListener(JTButtonControl btn)
    {
        for (MouseListener ml : btn.getMouseListeners())
        {
            if (ml instanceof BasicButtonListener)
            {
                return (BasicButtonListener) ml;
            }
        }
        return null;
    }
    
    protected int buttonForLocation(Point location)
    {
        return buttonForLocation(location.x, location.y);
    }
    
    protected int buttonForLocation(int x, int y)
    {
        if (btn.isCyclic())
        {
            return buttonRect.contains(x, y) ? btn.getValue()-btn.getMinValue() : -1;
        }
        
        int pos;
        int size;
        
        if (btn.getOrientation() == SwingConstants.HORIZONTAL)
        {
            pos = x;
            size = buttonRect.width;
        }
        else
        {
            pos = y;
            size = buttonRect.height;            
        }

        
        int index = pos/(size+btn.getSpacing());
        
        if (index>=0 && index<range(btn))
        {
            pos -= (index) * (size+btn.getSpacing());
            if (pos<size)
            {
                return index;
            }
        }
        return -1;
    }
    
    protected static class BasicButtonListener
      implements MouseListener, FocusListener,
      MouseMotionListener, MouseWheelListener,
      ChangeListener, KeyListener
    {

        private JTBasicButtonControlUI ui;

        public BasicButtonListener(JTBasicButtonControlUI ui)
        {
            this.ui = ui;
        }
        
        public void install(JTButtonControl c)
        {
            install(c, true);
        }

        public void uninstall(JTButtonControl c)
        {
            install(c, false);
        }
        
        private void install(JTButtonControl c, boolean install)
        {
            installMouseListener(c, install);
            installMouseMotionListener(c, install);
            installMouseWheelListener(c, install);
            installFocusListener(c, install);
            installChangeListener(c, install);
            installKeyListener(c, install);
        }

        public void installKeyListener(JTButtonControl c, boolean install)
        {
            if (install) c.addKeyListener(this);
            else c.removeKeyListener(this);
        }
        
        public void installMouseListener(JTButtonControl c, boolean install)
        {
            if (install) c.addMouseListener(this);
            else c.removeMouseListener(this);
        }
        
        public void installChangeListener(JTButtonControl c, boolean install)
        {
            if (install) c.addChangeListener(this);
            else c.removeChangeListener(this);
        }
        
        public void installMouseMotionListener(JTButtonControl c, boolean install)
        {
            if (install) c.addMouseMotionListener(this);
            else c.removeMouseMotionListener(this);
        }

        public void installFocusListener(JTButtonControl c, boolean install)
        {
            if (install)
            {
                c.setFocusable(true);
                //c.setFocusCycleRoot(true);
            }
            else
            {
                c.setFocusCycleRoot(false);
            }
            
            if (install) c.addFocusListener(this);
            else c.removeFocusListener(this);
        }

        public void installMouseWheelListener(JTButtonControl c, boolean install)
        {
            if (install) c.addMouseWheelListener(this);
            else c.removeMouseWheelListener(this);
        }

        public void stateChanged(ChangeEvent e)
        {
            ui.btn.repaint();
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
            ui.setHoverIndex(-1);
        }

        public void mousePressed(MouseEvent e)
        {   
            if (!e.getComponent().hasFocus())
                e.getComponent().requestFocus();
            
            ui.setMousePressed(true);
        }

        public void mouseReleased(MouseEvent e)
        {
            int selectedButton = ui.buttonForLocation(e.getX(), e.getY());
            if (selectedButton >= 0)
            {
                JTButtonControl btn = ui.btn;
                if (btn.isCyclic())
                {
                    int v = btn.getValue()+1;
                    if (v>btn.getMaxValue())
                        btn.setValue(btn.getMinValue());
                    else
                        btn.setValue(v);
                }
                else
                {
                    btn.setValue(selectedButton+btn.getMinValue());
                }
            }
            ui.setMousePressed(false);
        }

        public void focusGained(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void mouseDragged(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseMoved(MouseEvent e)
        {
            ui.setHoverIndex(ui.buttonForLocation(e.getX(), e.getY()));
        }

        public void mouseWheelMoved(MouseWheelEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    //if (ui.btn.getOrientation() == SwingConstants.VERTICAL)
                        incValue();
                    break;
                case KeyEvent.VK_DOWN:
                    //if (ui.btn.getOrientation() == SwingConstants.VERTICAL)
                        decValue();
                    break;
                case KeyEvent.VK_SPACE:
                    defaultValue();
                    break;
            }
        }
        
        private void incValue()
        {
            JTButtonControl btn = ui.btn;
            btn.setValue(btn.getValue()+1);
        }

        private void decValue()
        {
            JTButtonControl btn = ui.btn;
            btn.setValue(btn.getValue()-1);
        }

        private void defaultValue()
        {
            JTButtonControl btn = ui.btn;
            btn.setValue(btn.getDefaultValue());
        }

        public void keyReleased(KeyEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void keyTyped(KeyEvent e)
        {
            // TODO Auto-generated method stub
            
        }
        
    }

}

