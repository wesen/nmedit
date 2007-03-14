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
 * Created on Nov 11, 2006
 */
package net.sf.nmedit.nomad.core.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.nmutils.swing.NmSwingUtilities;

public class JTabComponentUI extends ComponentUI
{
    
    private static String PATH_PREFIX = "swing/jtab/";

    private static Icon getIcon(String name)
    {
        ClassLoader loader = JTabComponentUI.class.getClassLoader();

        return new ImageIcon(loader.getResource(PATH_PREFIX+name));
    }
    
    final static Icon defaultIcon = getIcon("file_obj.gif");
    
    final static Icon navBackwardEnabledIcon = getIcon("backward-enabled.png");
    final static Icon navBackwardDisabledIcon = getIcon("backward-disabled.png");
    final static Icon navForwardEnabledIcon = getIcon("forward-enabled.png");
    final static Icon navForwardDisabledIcon = getIcon("forward-disabled.png");
    final static Icon navDropDownIcon = getIcon("tab-down-arrow.png");

    final static Icon close_dis_hl = getIcon("close_btn_dis_hl.png");
    final static Icon close_dis = getIcon("close_btn_dis.png");
    final static Icon close_en_hl = getIcon("close_btn_en_hl.png");
    final static Icon close_en = getIcon("close_btn_en.png");
    
    
    private JTabComponent tabComponent;

    private int minTabWidth = 100;
    private int maxTabWidth = 180;
    private int firstTabIndex = 0;
    private int lastWidth = 0;
    private int lastHeight = 0;
    
    private int hoverTabIndex = -1;

    private Dimension prefSize = new Dimension(0,0);
    private Dimension tabSize = new Dimension(0,0);
    
    //boolean showNavIcons = false;
    final boolean showNavIcons = true;
    
    final int specialTabsWidth = 16;
    
    final static Color lineColor = Color.decode("#AAAAAA");
    
    private boolean flagCloseButton = false;

    public int mouseToInternalIndex(int mx)
    {
        
        flagCloseButton = false;
        
        Insets is = tabComponent.getInsets();

        int r = tabComponent.getWidth()-is.right;
        
        if (mx<is.left || mx>=r)
            return -1;
        mx-= is.left;
        
        if (mx<specialTabsWidth)
            return 0; // back button
        if (mx>r-specialTabsWidth)
            return 2; // down button
        if (mx>r-specialTabsWidth-specialTabsWidth)
            return 1; // forward button
    
        mx -= specialTabsWidth;
        
        
        
        int tabIndex = tabComponent.getTabCount()==0 ? -1 : (mx/tabSize.width);
        
        if (tabIndex>=0 && tabIndex<tabComponent.getTabCount())
        {
            
            int cc = mx - (tabIndex*tabSize.width);
            
            flagCloseButton = (cc> tabSize.width-4-16) && (cc<tabSize.width-4);
            
            return tabIndex+3;
        }
        else
            return -1;
    }
    
    
    public void setMouseOverIndex( int index )
    {
        if (hoverTabIndex != index)
        {
            hoverTabIndex = index;
            tabComponent.repaint();
        }
    }
    
    public JTabComponentUI(JTabComponent tabComponent)
    {
        this.tabComponent = tabComponent;
    }

    public static ComponentUI createUI(JComponent c) 
    {
        return new JTabComponentUI((JTabComponent) c);
    }

    public Dimension getMinimumSize(JComponent c) {
        Dimension d = getPreferredSize(c);
        d.width = Math.min(d.width, minTabWidth);
        return d;
    }
    
    public Dimension getPreferredSize(JComponent c) {
        
        updatePrefSize();        
        return new Dimension(prefSize);
    }
    
    private void updatePrefSize()
    {
        updateTabSize();
        prefSize.width=tabSize.width*tabComponent.getTabCount();
        prefSize.height =tabSize.height;
    }
    
    private void updateTabSize()
    {
        int tcount = tabComponent.getTabCount();
        if (tcount>0)
        {
            tabSize.width = Math.max(minTabWidth,Math.min(tabComponent.getWidth()/tcount, maxTabWidth));
            tabSize.height = 28;
            
        //    showNavIcons = tabSize.width*tcount>tabComponent.getWidth();            
            
            return ;
        }
        
        // showNavIcons = false;
        tabSize.width = 0;
        tabSize.height = 28;
        return ;
        /*
        tabSize.setSize(minTabWidth, 20);
           
        for (int i=0;i<tabComponent.getTabCount();i++)
        {
            Dimension d = getPreferredTabSize(tabComponent, tabComponent.getTab(i));

            tabSize.width = Math.max(tabSize.width, d.width);
            tabSize.height = Math.max(tabSize.height, d.height);
        }
        
        tabSize.width = Math.min(tabSize.width, maxTabWidth);
        */

    }
    
    public void update(Graphics g, JComponent c)
    {
        paint(g, c);
    }

    private GradientPaint bgGradient1 = null;
    private GradientPaint bgGradientInv = null;
    private GradientPaint bgGradientTab = null;
    int bgGradientH = -1;
    
    public void paint(Graphics g, JComponent c)
    {
        Insets insets = c.getInsets(paintViewInsets);
        if (c.isOpaque())
        {
            int innerHeight = c.getHeight();
            
            if (innerHeight>0)
            {
                if (bgGradientH<0 || bgGradientH!=innerHeight)
                {
                    final Color c1 = Color.decode("#BCBCBC");
                    final Color c2 = Color.decode("#D4D4D4");
                    bgGradientH = innerHeight;
                    
                    bgGradient1 = new GradientPaint(0,0,c1, 0, bgGradientH, c2);
                    bgGradientInv =  new GradientPaint(0,0,c2, 0, bgGradientH, c1);
                    //bgGradient2 = new GradientPaint(0,twoDiv3,c2, 0, , c1);
                    
                    bgGradientTab = new GradientPaint(0,0,Color.WHITE, 0,bgGradientH, Color.decode("#D9D9D9"));
                }
                
                if (g instanceof Graphics2D)
                {
                    Graphics2D g2 = (Graphics2D) g;
                    final Paint oldPaint = g2.getPaint();
                    g2.setPaint(bgGradient1);
                    g2.fillRect(0, 0, c.getWidth(), c.getHeight());
                    /*g2.fillRect(0, 0, c.getWidth(), (int)twoDiv3);
                    g2.setPaint(bgGradient2);
                    g2.fillRect(0, (int)twoDiv3, c.getWidth(), c.getHeight()-(int)twoDiv3);*/
                    g2.setPaint(oldPaint);
                }
                else
                {
                    g.setColor(c.getBackground());
                    g.fillRect(0, 0, c.getWidth(), c.getHeight());
                }
            }
        }
        
        if (tabSize.width==0 || lastWidth!=c.getWidth()||lastHeight!=c.getHeight())
        {
            lastWidth = c.getWidth();
            lastHeight = c.getHeight();
            adjustTabs();
            updatePrefSize();
        }

        FontMetrics fm = c.getFontMetrics(g.getFont());

        final int y = insets.top;
        final int th = tabSize.height-(insets.top+insets.bottom);
        
        
        firstTabIndex = (tabComponent.getSelectedTabIndex()+1)*tabSize.width>tabComponent.getWidth()
            ? tabComponent.getSelectedTabIndex() : 0;
        
        int dx = showNavIcons?(navForwardEnabledIcon.getIconWidth()+2*4) : 0;
            
        for (int i=firstTabIndex;i<tabComponent.getTabCount();i++)
        {
            JTab tab = tabComponent.getTab(i);
            int tabPositionIndex = i-firstTabIndex;
            int x = insets.left+tabPositionIndex*tabSize.width+dx;
            paintTab(g, fm, tabComponent, tab, i, tabPositionIndex, x, y, tabSize.width, th);
        }
        
        if (showNavIcons)
        {
            final boolean canGoBack = tabComponent.getSelectedTabIndex()!=0;
            final boolean canGoForward = tabComponent.getSelectedTabIndex()>=0&&
                tabComponent.getSelectedTabIndex()!=tabComponent.getTabCount()-1;
            
            paintTabBackground(g, insets.left, y, specialTabsWidth, th, 
                    hoverTabIndex==0 && canGoBack,
                    false
            );

            Icon back = canGoBack ? navBackwardEnabledIcon : navBackwardDisabledIcon;

            back.paintIcon(tabComponent, g, 4+(specialTabsWidth-back.getIconWidth())/2, y+(th-back.getIconHeight())/2);
            
            int xr = tabComponent.getWidth()/*-(insets.right)*/-1-specialTabsWidth;

            paintTabBackground(g, xr-specialTabsWidth, y, specialTabsWidth, th, 
                    hoverTabIndex==1 && canGoForward,
                    false
            );
            paintTabBackground(g, xr, y, specialTabsWidth, th, 
                    hoverTabIndex==2 && tabComponent.getTabCount()>0,
                    false
            );
/*
            Graphics2D g2 = (Graphics2D) g;
            final Paint oldPaint = g2.getPaint();
            g2.setPaint(bgGradient1);
            g2.fillRect(xr-specialTabsWidth, 0, specialTabsWidth*2, c.getHeight());
            g2.setPaint(oldPaint);
            */
            navDropDownIcon.paintIcon(tabComponent, g, 
                    xr+(specialTabsWidth-navDropDownIcon.getIconWidth())/2, 
                    y+(th-navDropDownIcon.getIconHeight())/2);
            
            xr-=specialTabsWidth;
            
            
            
            Icon forw = canGoForward ? navForwardEnabledIcon : navForwardDisabledIcon;
            forw.paintIcon(tabComponent, g, xr+(specialTabsWidth-forw.getIconWidth())/2,
                    y+(th-forw.getIconHeight())/2);
            
        }
        
    }
    
    FontMetrics getFontMetrics()
    {
        return tabComponent.getFontMetrics(tabComponent.getFont());
    }

    private static Rectangle paintIconR = new Rectangle();
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);
    
    private Font selectedFont = null;
    private Font currentFont = null;
    
    public Font getSelectedFont(Font font)
    {
        if (currentFont!=font)
        {
            currentFont = font;
            selectedFont = new Font(font.getName(), Font.BOLD, font.getSize());
        }
        
        return selectedFont;
    }
    
    protected void paintTabBackground(Graphics g, int x, int y, int w, int h, boolean hovered, boolean
            selected)
    {

        if (hovered||selected)
        {
            Graphics2D g2 = (Graphics2D) g;
            Paint oldPaint = g2.getPaint();
            g2.setPaint(bgGradientTab);
            g.fillRect(x, y, w, h);
            g2.setPaint(oldPaint);
            
            if (hovered)
                g.setColor(lineColor.darker());
            else
                g.setColor(lineColor);

           // g.drawLine(x, y, x+tabWidth-1, y);
            //g.setColor(Color.DARK_GRAY);
            g.drawLine(x+w-1, y, x+w-1, y+h-1);
        }
        else
        {
            Graphics2D g2 = (Graphics2D) g;
            Paint oldPaint = g2.getPaint();
            g2.setPaint(bgGradientInv);
            g.fillRect(x, y, w, h);
            g2.setPaint(oldPaint);

            g.setColor(lineColor);
            g.drawLine(x+w-1, y, x+w-1, h/*c.getHeight()*/-1);
        }
        
    }
    
    private BasicStroke focusStroke = 
    new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{ 1, 1 }, 0 );
    protected void paintTab(Graphics g, FontMetrics fm, JTabComponent c, JTab t, 
            int tabIndex, int tabPositionIndex, int x, int y, int tabWidth, int tabHeight)
    {
       // MetalLabelUI

        paintTabBackground(g, x, y, tabWidth, tabHeight, 
        tabIndex == hoverTabIndex-3,
        tabIndex == c.getSelectedTabIndex()
        );
        
        String text = t.getTitle();
        Icon icon = (c.isEnabled()) ? t.getIcon() : t.getDisabledIcon();

        if (icon == null)
            icon = defaultIcon;
        
        if ((icon == null) && (text == null)) {
            return;
        }

        Icon closeIcon;
        
        if (tabIndex==tabComponent.getSelectedTabIndex())
        {
           closeIcon = (hoverTabIndex-3 == tabIndex)&&flagCloseButton ? close_en_hl : close_en; 
        }
        else
        {
            closeIcon = (hoverTabIndex-3 == tabIndex)&&flagCloseButton ? close_dis_hl : close_dis;
        }
        
        
        paintViewR.x = x+2;
        paintViewR.y = y;
        paintViewR.width = tabWidth - closeIcon.getIconWidth()-8;
        paintViewR.height = tabHeight;

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        Font oldFont = g.getFont();
        
        
        if (c.getSelectedTabIndex()==tabIndex)
        {
            g.setFont(getSelectedFont(oldFont));
            fm = tabComponent.getFontMetrics(g.getFont());
        }

        String clippedText = 
            layoutCL(c, fm, text, icon, paintViewR, paintIconR, paintTextR);

        if (icon != null) {
            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
        }
        
        
        closeIcon.paintIcon(c, g, x+tabWidth-closeIcon.getIconWidth()-5, 
                y+((tabHeight-closeIcon.getIconHeight())/2));

        if (text != null) {
        int textX = paintTextR.x;
        int textY = paintTextR.y + fm.getAscent() ;
        
        if (c.isEnabled()) {
            
                paintEnabledText(t, g, clippedText, textX, textY);
            
            if (c.getSelectedTabIndex()==tabIndex && c.hasFocus())
            {
                g.setColor(Color.BLACK); // focus color

                Graphics2D g2 = (Graphics2D) g;
                
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(focusStroke);
                g.drawRect(paintTextR.x-4,paintTextR.y,
                        tabWidth-18-paintTextR.x+x, paintTextR.height-1);

                g2.setStroke(oldStroke);
            }
            
        }
        else {
         //   paintDisabledText(t, g, clippedText, textX, textY);
        }
        }
        g.setFont(oldFont);
    }
    
    private EventHandler eventHandler = new EventHandler();

    private static Border theBorder = 
        
        //BorderFactory.createCompoundBorder(
                //BorderFactory.createLoweredBevelBorder(),
                new BottomLineBorder(lineColor, 3, 2, 0, 2)
        /*)*/;
    
    private static class BottomLineBorder implements Border
    {
        
        private Color line;
        private Insets insets;

        public BottomLineBorder(Color line, int top, int left, int bottom, int right)
        {
            this(line, new Insets(top, left, bottom, right));
        }

        public BottomLineBorder(Color line, Insets insets)
        {
            this.line = line;
            this.insets = insets;
        }

        public Insets getBorderInsets(Component c)
        {
            return new Insets(insets.top, insets.left, insets.bottom, insets.right);
        }

        public boolean isBorderOpaque()
        {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
        {
            g.setColor(line);
            int b = y+height-1;
            g.drawLine(x, b, x+width-1, b);
        }
        
    }
    
    public void installUI(JComponent c) 
    {
        c.addPropertyChangeListener(eventHandler);
        c.addMouseListener(eventHandler);
        c.addMouseMotionListener(eventHandler);
        c.addFocusListener(eventHandler);
        c.addKeyListener(eventHandler);
        c.setBorder(theBorder);
    }
    
    public void uninstallUI(JComponent c) 
    {
        c.addPropertyChangeListener(eventHandler);
        c.removeMouseListener(eventHandler);
        c.removeMouseMotionListener(eventHandler);
        c.removeFocusListener(eventHandler);
        c.removeKeyListener(eventHandler);
        c.setBorder(null);
    }
    
    protected void adjustTabs()
    {
        
    }

    protected void showDropDownPopup()
    {
        if (tabComponent.getTabCount()>0)
        {
            JPopupMenu popup = new JPopupMenu();
            
            for (int i=0;i<tabComponent.getTabCount();i++)
            {
                JTab t = tabComponent.getTab(i);
                JMenuItem menuItem = popup.add(new SelectTabAction(t, tabComponent)); 
                menuItem.setSelected(i==tabComponent.getSelectedTabIndex());
            }
            
            popup.show(tabComponent, tabComponent.getWidth(), tabComponent.getHeight());
        }
    }
    
    private static class SelectTabAction extends AbstractAction
    {
        private JTab tab;
        private JTabComponent c;

        public SelectTabAction(JTab tab, JTabComponent c)
        {
            this.tab = tab;
            this.c = c;
            putValue(NAME, tab.getTitle());
            putValue(SMALL_ICON, tab.getIcon());
        }

        public void actionPerformed( ActionEvent e )
        {
            c.setSelectedTab(tab);
        }
    }
    
    private class EventHandler implements 
        PropertyChangeListener, MouseListener, FocusListener,
        MouseMotionListener, KeyListener
    {

        public void propertyChange( PropertyChangeEvent evt )
        {
            if ("tabcount".equals(evt.getPropertyName()))
            {
                adjustTabs();
            }
        }

        public void mouseClicked( MouseEvent e )
        {
            
        }
        
        public void mousePressed( MouseEvent e )
        {
            handleMouseDown(mouseToInternalIndex(e.getX()));
            
            tabComponent.requestFocus();
            
        }

        private void handleMouseDown(int internalTabIndex)
        {
            if (internalTabIndex<0)
                return;
            if (internalTabIndex==0)
            {
                tabComponent.setSelectedTabIndex(tabComponent.getSelectedTabIndex()-1);
            }
            else if (internalTabIndex==1)
            {
                tabComponent.setSelectedTabIndex(tabComponent.getSelectedTabIndex()+1);
            }
            else if (internalTabIndex==2)
            {
                showDropDownPopup();
            }
            else
            {
                if (flagCloseButton)
                {
                    tabComponent.closeTabAt(internalTabIndex-3);
                }
                else
                {
                    tabComponent.setSelectedTabIndex(internalTabIndex-3);
                }
            }
        }
        
        public void mouseReleased( MouseEvent e )
        {
            
        }

        public void mouseEntered( MouseEvent e )
        {
            
        }

        public void mouseExited( MouseEvent e )
        {
            setMouseOverIndex(-1);
        }

        public void focusGained( FocusEvent e )
        {
            if (tabComponent.getTabCount()>0)
                tabComponent.repaint();
        }

        public void focusLost( FocusEvent e )
        {
            if (tabComponent.getTabCount()>0)
                tabComponent.repaint();   
        }

        public void mouseDragged( MouseEvent e )
        {
            
        }

        public void mouseMoved( MouseEvent e )
        {
            boolean closeButtonHovered = flagCloseButton;
            setMouseOverIndex(mouseToInternalIndex(e.getX()));
            if (closeButtonHovered!=flagCloseButton)
                tabComponent.repaint();
        }

        public void keyTyped( KeyEvent e )
        {
            
        }

        public void keyPressed( KeyEvent e )
        {
            if (tabComponent.hasFocus())
            {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                        tabComponent.setSelectedTabIndex(tabComponent.getSelectedTabIndex()-1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        tabComponent.setSelectedTabIndex(tabComponent.getSelectedTabIndex()+1);
                        break;
                }
            }
        }

        public void keyReleased( KeyEvent e )
        {
            
        }
        
    }

    /**
     * Forwards the call to SwingUtilities.layoutCompoundLabel().
     * This method is here so that a subclass could do Label specific
     * layout and to shorten the method name a little.
     * 
     * @see SwingUtilities#layoutCompoundLabel
     */
    protected String layoutCL(
        JComponent c,                  
        FontMetrics fontMetrics, 
        String text, 
        Icon icon, 
        Rectangle viewR, 
        Rectangle iconR, 
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            (JComponent) c,
            fontMetrics,
            text,
            icon,
            SwingConstants.CENTER,
            SwingConstants.LEADING,
            SwingConstants.CENTER,
            SwingConstants.TRAILING,
            /*c.getVerticalAlignment(),
            c.getHorizontalAlignment(),
            c.getVerticalTextPosition(),
            c.getHorizontalTextPosition(),*/
            viewR,
            iconR,
            textR,
            6/*c.getIconTextGap()*/);
    }

    protected void paintEnabledText(JTab tab, Graphics g, String s, int textX, int textY)
    {
        g.setColor(tabComponent.getForeground());
        drawText(tabComponent, g, s, -1, textX, textY);        
    }
    
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
        int accChar = l.getDisplayedMnemonicIndex();
        Color background = l.getBackground();
        g.setColor(background.brighter());
        
        drawText(l, g, s, accChar, textX+1, textY+1);   
        g.setColor(background.darker());
        drawText(l, g, s, accChar, textX, textY);  
    }

    private void drawText(JComponent c, Graphics g, String s, int mnemonicIndex, int textX, int textY)
    {
        NmSwingUtilities.
        drawStringUnderlineCharAt(/*tabComponent, */g, s, -1,
                                                     textX, textY);
    }
}
