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
package net.sf.nmedit.nomad.core.swing.tabs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.swing.NMLazyActionMap;
import net.sf.nmedit.nmutils.swing.NmSwingUtilities;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.misc.FocusStroke;

public class FFTabBarUI extends TabBarUI
{

    public static final int BACKWARDS_TAB = -2;
    public static final int FORWARD_TAB = -3;
    public static final int DROP_DOWN_TAB = -4;

    public static final Color DEFAULT_BG_GRADIENT_TOP = new ColorUIResource(
            0xcbc8c4);

    public static final Color DEFAULT_BG_GRADIENT_MIDDLE = new ColorUIResource(
            0xeae7e2);

    public static final Color DEFAULT_BG_GRADIENT_BOTTOM = new ColorUIResource(
            0xdddad5);

    public static final float BG_GRADIENT_MIDDLE = 1f - 0.3333f;

    public static final Color DEFAULT_BORDER_LINE_COLOR = new ColorUIResource(
            0xb1a598);

    public static final Color DEFAULT_BORDER_FILL_COLOR = new ColorUIResource(
            0xefece8);

    public static final int DEFAULT_BORDER_FILL_HEIGHT = 2;

    protected JTabBar tabBar;

    protected Color bgGradientTop = DEFAULT_BG_GRADIENT_TOP;

    protected Color bgGradientMiddle = DEFAULT_BG_GRADIENT_MIDDLE;

    protected Color bgGradientBottom = DEFAULT_BG_GRADIENT_BOTTOM;

    protected GradientPaint bgGradientTopMiddle;

    protected GradientPaint bgGradientMiddleBottom;

    protected boolean bgGradientEnabled = true;

    protected Color tabBgUpperGradientTop = new Color(0xe2e0de);

    protected Color tabBgUpperGradientBottom = new Color(0xdddbd7);

    protected Color tabBgLowerGradientTop = new Color(0xd8d6d2);

    protected Color tabBgLowerGradientBottom = new Color(0xe5e2dd);

    protected GradientPaint tabBgUpperGradient = null;

    protected GradientPaint tabBgLowerGradient = null;

    protected Color tabBgUpperGradientTopSelected = new Color(0xfaf9f7);

    protected Color tabBgUpperGradientBottomSelected = new Color(0xebe9e6);

    protected Color tabBgLowerGradientTopSelected = new Color(0xe7e5e2);

    protected Color tabBgLowerGradientBottomSelected = new Color(0xe8e6e2);

    protected GradientPaint tabBgUpperGradientSelected = null;

    protected GradientPaint tabBgLowerGradientSelected = null;

    protected int tabBgGradientHeight = -1;

    protected int tabBgGradientMid;

    protected Color lineColor = Color.decode("#AAAAAA");

    protected Color tabBorderOuterSelected = new Color(0xa3a19e);

    protected Color tabBorderInnerSelected = new Color(0xf3f2f0);

    protected Color tabBorderOuter = new Color(0xbfbdba);

    protected Color tabBorderInner = new Color(0xf1f0ed);

    protected Color borderLineColor = DEFAULT_BORDER_LINE_COLOR;

    protected Color borderFillColor = DEFAULT_BORDER_FILL_COLOR;

    protected int borderFillHeight = DEFAULT_BORDER_FILL_HEIGHT;

    private int prevHeight = -1;

    private int prevWidth = -1;

    private boolean resized = false;

    protected int tabPaddingTop = 2;
    protected int tabPaddingLeftRight = 3;

    private int prevTabCount = -1;
    
    private int dndPreviousSelectionIndex = -1;

    protected transient Insets tmpInsets;

    private Dimension specialTabSize = new Dimension();
    protected Rectangle tabBounds = new Rectangle();
    protected Rectangle innerTabBounds = new Rectangle(); // tabBounds+tabInsets
    protected Rectangle closeButtonBounds = new Rectangle();
    
    protected int minTabWidth = 16;

    protected int maxTabWidth = 180;

    protected Insets iconPadding = new Insets(2, 2, 2, 2);

    protected Insets textPadding = new Insets(2, 2, 2, 2);

    protected Insets tabInsets = new Insets(2, 2, 1, 2);

    protected int hoverIndex = -1;
    protected int closeIconHoverIndex = -1;

    protected boolean specialButtonsVisible = false;
    protected int dropTargetIndex = -1;
    
    
    // icons

    public final static Icon defaultIcon = getIcon2("mimetypes/text-x-generic.png");

    final static Icon navBackwardEnabledIcon = getIcon("backward-enabled.png");

    final static Icon navBackwardDisabledIcon = getIcon("backward-disabled.png");

    final static Icon navForwardEnabledIcon = getIcon("forward-enabled.png");

    final static Icon navForwardDisabledIcon = getIcon("forward-disabled.png");

    final static Icon navDropDownIcon = getIcon("tab-down-arrow.png");

    protected static Icon navDropDownDisabledIcon;

    final static Icon close_dis_hl = getIcon("close_btn_dis_hl.png");

    final static Icon close_dis = getIcon("close_btn_dis.png");

    final static Icon close_en_hl = getIcon("close_btn_en_hl.png");

    final static Icon close_en = getIcon("close_btn_en.png");
    final static Icon dropTabArrow = getIcon("dropTabArrow.png");

    private static final String PATH_PREFIX = "/swing/jtab/";

    private static Icon getIcon(String name)
    {
        URL url = FFTabBarUI.class.getResource(PATH_PREFIX+name);
        if (url == null) return null;
        return new ImageIcon(url);
    }
    private static Icon getIcon2(String name)
    {
        final String PATH_PREFIX2 = "/icons/tango/16x16/";
        URL url = Nomad.sharedInstance().getClass().getResource(PATH_PREFIX2+name);
        if (url == null) return null;
        return new ImageIcon(url);
    }

    // protected MetalTabbedPaneUI

    public FFTabBarUI(JTabBar bar)
    {
        this.tabBar = bar;
    }

    protected void setDropTargetIndex(int index)
    {
        if (index<0) index = -1;
        
        if (dropTargetIndex != index)
        {
            this.dropTargetIndex = index;
            
            tabBar.repaint();
        }
    }
    
    protected void setHoverIndex(int hoverIndex)
    {
        if (this.hoverIndex != hoverIndex)
        {
            this.hoverIndex = hoverIndex;
            tabBar.repaint();
        }
    }

    // **** ComponentUI

    public static FFTabBarUI createUI(JComponent c)
    {
        return new FFTabBarUI((JTabBar) c);
    }

    public void installUI(JComponent c)
    {
        checkComponent(c);
        installUI();
    }

    public void uninstallUI(JComponent c)
    {
        checkComponent(c);
        uninstallUI();
    }

    // **** ComponentUI

    protected boolean resized()
    {
        return resized;
    }

    protected void setResized(boolean resized)
    {
        this.resized = resized;
        prevWidth = tabBar.getWidth();
        prevHeight = tabBar.getHeight();
    }

    protected void updateResizedFlag()
    {
        setResized(prevWidth != tabBar.getWidth()
                || prevHeight != tabBar.getHeight());
    }

    protected void checkComponent(JComponent c)
    {
        if (c != tabBar)
            throw new IllegalArgumentException();
    }

    public void installUI()
    {
        BasicEventHandler eventHandler = createEventHandler(tabBar);
        if (eventHandler != null)
            eventHandler.install();

        // TODO lookup settings in UIDefaults
        tabBar.setBackground(bgGradientBottom);
        tabBar.setBorder(new FFTabBorder(borderLineColor, borderFillColor,
                borderFillHeight));
        tabBar.setFocusable(true);

        if (navDropDownDisabledIcon == null)
            navDropDownDisabledIcon = UIManager.getLookAndFeel()
                    .getDisabledIcon(tabBar, navDropDownIcon);
    }

    public void uninstallUI()
    {
        BasicEventHandler eventHandler = lookupEventHandler(tabBar);
        if (eventHandler != null)
            eventHandler.uninstall();
        tabBar.setFocusable(false);
    }

    // **** painting

    public void update(Graphics g, JComponent c)
    {
        paint(g, c);
    }

    protected void checkUpdate()
    {
        tmpInsets = tabBar.getInsets(tmpInsets);
        updateResizedFlag();
        boolean fontChanged = updateFont(tabBar.getFont());

        if (prevTabCount != tabBar.getTabCount() || resized() || fontChanged)
        {
            computeTabBounds();
        }
    }

    public void paint(Graphics g, JComponent c)
    {
        if (c.getHeight()<=0 || c.getWidth()<=0)
            return ;
        
        checkComponent(c);
        checkUpdate();
        paint2((Graphics2D) g);
        setResized(false);
    }

    private void paint2(Graphics2D g2)
    {
        paintBackground(g2);
        paintTabs(g2);
        if (specialButtonsVisible)
            paintSpecialButtons(g2);
        
        if (dropTargetIndex>=0)
            paintDropTarget(g2);
    }

    private void paintDropTarget(Graphics2D g2)
    {
        if (dropTargetIndex<0) return;
        
        int tx = tabBounds.x+(dropTargetIndex*tabBounds.width);
        
        tx-=dropTabArrow.getIconWidth()/2;
        
        dropTabArrow.paintIcon(tabBar, g2, tx, tabBounds.y+(tabBounds.height-dropTabArrow.getIconHeight())/2);
        
    }

    protected void computeTabBounds()
    {
        int minw = Math.max(this.minTabWidth,
                (16 + iconPadding.left + iconPadding.right) * 2 // icon+close-icon
                );

        int textHeight = Math.max(fontMetrics.getHeight(), selectedfontMetrics
                .getHeight())
                + textPadding.top + textPadding.bottom;
        int iconHeight = 16 + iconPadding.top + iconPadding.bottom;

        int tabCount = tabBar.getTabCount();
        
        int innerWidth = tabBar.getWidth()-tmpInsets.left-tmpInsets.right-tabPaddingLeftRight-tabPaddingLeftRight;
        
        int width;
        if (tabCount == 0)
            width = 0;
        else if (tabCount * maxTabWidth <= innerWidth)
            width = maxTabWidth;
        else if (tabCount*minw >= innerWidth)
            width = minw;
        else
        {
            width = innerWidth / tabCount;
        }
        
        int height = tabInsets.top + tabInsets.bottom
                + Math.max(iconHeight, textHeight)+1;

        if (tabBounds == null)
            tabBounds = new Rectangle();
        
        tabBounds.setSize(width, height);

        specialButtonsVisible = tabCount > 0
                && (width * tabCount > tabBar.getWidth());

        if (specialTabSize == null)
            specialTabSize = new Dimension();

        int specialIconWidth = Math.max(navForwardEnabledIcon.getIconHeight(), 
                Math.max(navBackwardEnabledIcon.getIconWidth(), navDropDownIcon.getIconWidth()));
        specialTabSize.height = tabBounds.height;
        specialTabSize.width = tabInsets.left + tabInsets.right + specialIconWidth + iconPadding.left
        + iconPadding.right;
        
        
        // compute visible tabs
        
        int dx = 0;
        int selectedIndex = tabBar.getSelectedIndex();
        
        if (specialButtonsVisible)
        {
            dx = specialTabSize.width;
        }

        int minIndex = 0;
        int maxIndex = tabBar.getTabCount()-1;
        
        int selX = selectedIndex*tabBounds.width;
        
        while (dx+selX+tabBounds.width>tabBar.getWidth())
        {
            minIndex++;
            dx -= tabBounds.width;
            if (minIndex>selectedIndex) break;
        }
        
        if (specialButtonsVisible)
        {
            if (dx+selX+tabBounds.width>tabBar.getWidth()-specialTabSize.width*2)
                dx-= specialTabSize.width*2;
        }
        
        while (dx+maxIndex*tabBounds.width>tabBar.getWidth())
        {
            maxIndex--;
            if (maxIndex<0) break;
        }
        
        tabRunStart = minIndex;
        tabRunStop = maxIndex;
        
        tabBounds.x = dx+(specialButtonsVisible?0:tabPaddingLeftRight);
        tabBounds.y = tabPaddingTop+tmpInsets.top;
        
        innerTabBounds.setBounds(tabBounds.x+tabInsets.left, tabBounds.y+tabInsets.top,
                tabBounds.width-tabInsets.left-tabInsets.right,
                tabBounds.height-tabInsets.top-tabInsets.bottom);
        
        Icon closeButton = close_en_hl;
        closeButtonBounds.setSize(closeButton.getIconWidth(), closeButton.getIconHeight());
        closeButtonBounds.setLocation(getCloseButtonX(tabBounds.x, closeButton), getCloseButtonY(tabBounds.y, closeButton));
    }
    
    protected int getCloseButtonX(int xoffset, Icon icon)
    {
        return xoffset + tabBounds.width - icon.getIconWidth() - iconPadding.right - tabInsets.right;
    }
    
    protected int getCloseButtonY(int yoffset, Icon icon)
    {
        return yoffset+(tabBounds.height-icon.getIconHeight())/2;
    }

    private int tabRunStart = 0;
    private int tabRunStop = -1;
    
    private transient Font font;

    private transient Font selectedFont;

    private transient FontMetrics fontMetrics;

    private transient FontMetrics selectedfontMetrics;

    public boolean updateFont(Font font)
    {
        boolean updateSelectedFont = selectedFont == null;
        boolean fontChanged = false;

        if (this.font != font)
        {
            this.font = font;
            this.fontMetrics = tabBar.getFontMetrics(font);
            updateSelectedFont = true;
            fontChanged = true;
        }

        if (updateSelectedFont)
        {
            selectedFont = new Font(font.getName(), Font.BOLD, font.getSize());
            selectedfontMetrics = tabBar.getFontMetrics(selectedFont);
        }

        return fontChanged || updateSelectedFont;
    }

    public void showDropDownMenu(MouseEvent e)
    {
        JPopupMenu popup = new JPopupMenu();
        
        for (int i=0;i<tabBar.getTabCount();i++)
        {
            popup.add(new JRadioButtonMenuItem(new SelectTabAction(i)));
        }
        popup.show(tabBar, e.getX(), e.getY());
    }
    
    private class SelectTabAction extends AbstractAction implements Runnable
    {

        /**
         * 
         */
        private static final long serialVersionUID = 7325111901000279179L;
        private int tabIndex;

        public SelectTabAction(int tabIndex)
        {
            this.tabIndex = tabIndex;
            putValue(NAME, tabBar.getTitleAt(tabIndex));
            putValue(MLEntry.SELECTED_KEY, tabIndex == tabBar.getSelectedIndex());
            putValue(SHORT_DESCRIPTION, tabBar.getToolTipTextAt(tabIndex));
            putValue(SMALL_ICON, tabBar.getIconAt(tabIndex));
            putValue(MLEntry.DISPLAYED_MNEMONIC_INDEX_KEY, tabBar.getDisplayedMnemonicIndexAt(tabIndex));
            putValue(MNEMONIC_KEY, tabBar.getMnemonicAt(tabIndex));
        }
        
        public void actionPerformed(ActionEvent e)
        {
            SwingUtilities.invokeLater(this);
        }

        public void run()
        {
            if (tabIndex>=0 && tabIndex<tabBar.getTabCount())
            {
                tabBar.setSelectedIndex(tabIndex);
                tabIndex = -1;
            }
        }
        
    }
    
    public String getToolTipTextAt(JTabBar<?> c, int x, int y)
    {
        int index = getTabIndexForLocation(x, y);
    
        if (index >= 0) {
            return tabBar.getToolTipTextAt(index);
        }
        
        if (index == DROP_DOWN_TAB)
            return "All Tabs";
        
        return c.getToolTipText();
    }

    private void paintTabs(Graphics2D g2)
    {
        if (tabBar.getTabCount() <= 0)
            return;

        int selectedIndex = tabBar.getSelectedIndex();

        boolean enabled = tabBar.isEnabled();
        
        for (int i = tabRunStop; i > selectedIndex; i--)
        {
            boolean e = enabled ? tabBar.isEnabledAt(i) : enabled;
            paintTab(g2, i, false, e);
        }
        for (int i = selectedIndex - 1; i >= tabRunStart; i--)
        {
            boolean e = enabled ? tabBar.isEnabledAt(i) : enabled;
            paintTab(g2, i, false, e);
        }

        int bottom = tabBounds.y+tabBounds.height-1; //tabBar.getHeight() - 1 - tmpInsets.bottom;
        g2.setColor(borderLineColor);
        g2.drawLine(0, bottom, tabBar.getWidth(), bottom);

        if (selectedIndex >= 0)
        {
            boolean e = enabled ? tabBar.isEnabledAt(selectedIndex) : enabled;
            paintTab(g2, selectedIndex, true, e);
        }
    }

    protected void paintSpecialButtons(Graphics2D g2)
    {

        Icon navBackIcon, navForwIcon, navDDIcon;
        int selectedIndex = tabBar.getSelectedIndex();

        boolean navBackEnabled = false;
        boolean navForwEnabled = false;
        boolean navDDEnabled = false;

        if (tabBar.isEnabled())
        {
            navBackEnabled = selectedIndex > 0;
            navForwEnabled = selectedIndex < tabBar.getTabCount() - 1;
            navDDEnabled = true;
        }

        navBackIcon = navBackEnabled ? navBackwardEnabledIcon
                : navBackwardDisabledIcon;
        navForwIcon = navForwEnabled ? navForwardEnabledIcon
                : navForwardDisabledIcon;
        navDDIcon = navDDEnabled ? navDropDownIcon : navDropDownDisabledIcon;

        int w = specialTabSize.width;
        int top = tmpInsets.left + tabPaddingTop;
        
        int iconTop = top + iconPadding.top;
        // back button
        paintTabBackground(g2, tmpInsets.left, top, w, tabBounds.height, false,
                hoverIndex == BACKWARDS_TAB);
        navBackIcon.paintIcon(tabBar, g2, (w-navBackIcon.getIconWidth())/2, iconTop
                + (tabBounds.height - navBackIcon.getIconHeight()) / 2);

        int ddx = tabBar.getWidth() - tmpInsets.right - w;

        // right button
        paintTabBackground(g2, ddx - w, top, w, tabBounds.height, false, hoverIndex == FORWARD_TAB);
        navForwIcon.paintIcon(tabBar, g2, ddx - w +(w-navForwIcon.getIconWidth())/2
                /*+ iconPadding.left
                + (iw - navForwIcon.getIconWidth()) / 2*/, iconTop
                + (tabBounds.height - navForwIcon.getIconHeight()) / 2);

        // dd button
        paintTabBackground(g2, ddx, top, w, tabBounds.height, false, hoverIndex == DROP_DOWN_TAB);
        navDDIcon.paintIcon(tabBar, g2, ddx + (w-navDDIcon.getIconWidth())/2, iconTop
                + (tabBounds.height - navDDIcon.getIconHeight()) / 2);
    }

    private void paintTab(Graphics2D g2, int index, boolean selected,
            boolean enabled)
    {
        Icon icon;

        if (tabBar.isEnabledAt(index))
        {
            icon = tabBar.getIconAt(index);
        }
        else
        {
            icon = tabBar.getDisabledIconAt(index);
        }

        String title = tabBar.getTitleAt(index);
        if (title != null && title.length() == 0)
            title = null;

        int mnemonic = tabBar.getMnemonicAt(index);
        int mnemonicIndex = tabBar.getMnemonicAt(index);

        paintTab(g2, index, icon, title, mnemonic, mnemonicIndex, selected,
                hoverIndex == index, enabled, tabBounds.x+(index * tabBounds.width), 
                tabBounds.y, tabBounds.width, tabBounds.height);
    }

    private static Rectangle paintIconR = new Rectangle();

    private static Rectangle paintTextR = new Rectangle();

    private static Rectangle paintViewR = new Rectangle();

    private void paintTab(Graphics2D g2, int index, Icon icon, String title, int mnemonic,
            int mnemonicIndex, boolean selected, boolean hovered,
            boolean enabled, int x, int y, int width, int height)
    {
        paintTabBackground(g2, x, y, width, height, selected, hovered);

        if ((icon == null) && (title == null))
        {
            return;
        }

        int closeIconX = Integer.MAX_VALUE;
        int closeIconWidth = 0;
        
        if (tabBar.isCloseActionEnabled())
        {
            Icon closeIcon;
    
            if (selected)
            {
                closeIcon = hovered && closeIconHoverIndex==index ? close_en_hl : close_en;
            }
            else
            {
                closeIcon = hovered && closeIconHoverIndex==index ? close_dis_hl : close_dis;
            }
    
            closeIconX = getCloseButtonX(tabBounds.x+(index*tabBounds.width), closeIcon);
            
            closeIconWidth = closeIcon.getIconWidth();
            
            closeIcon.paintIcon(tabBar, g2, closeIconX, getCloseButtonY(tabBounds.y,closeIcon) );
        }
        
        
        
        paintViewR.x = tabInsets.left + x + iconPadding.left;
        paintViewR.y =  innerTabBounds.y;
        paintViewR.width = innerTabBounds.width - (closeIconWidth + iconPadding.left + iconPadding.right);
        paintViewR.height = innerTabBounds.height;

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        Font font;
        FontMetrics fm;

        if (selected)
        {
            font = this.selectedFont;
            fm = this.selectedfontMetrics;
        }
        else
        {
            font = this.font;
            fm = this.fontMetrics;
        }

        g2.setFont(font);

        String clippedText = layoutCL(tabBar, fm, title, icon, paintViewR, paintIconR, paintTextR);

        if (icon != null) icon.paintIcon(tabBar, g2, paintIconR.x, paintIconR.y);

        if (title != null)
        {

            int textX = paintTextR.x;
            int textY = paintTextR.y + fm.getAscent();

            if (enabled)
            {

                paintEnabledText(g2, clippedText, mnemonicIndex, textX, textY);

                if (selected && tabBar.hasFocus())
                {
                    g2.setColor(Color.BLACK); // focus color

                    // stroke
                    int sx = paintTextR.x; 
                    int sy = paintTextR.y;
                    int sh = paintTextR.height;
                    
                    int sr;

                    if (closeIconWidth > 0 && closeIconX != Integer.MAX_VALUE)
                    {
                        sr = closeIconX-iconPadding.left;
                    }
                    else
                    {
                        sr = x+width-textPadding.right-tmpInsets.right-tabInsets.right;
                    }
                    int sw = sr-sx;
                    
                    Stroke oldStroke = g2.getStroke();
                    g2.setStroke(FocusStroke.getFocusStroke());
                    g2.drawRect(sx, sy, sw, sh);
                    g2.setStroke(oldStroke);
                }
            }
            else
            {
                paintDisabledText(g2, clippedText, mnemonicIndex, textX, textY);
            }
        }

    }

    /**
     * Forwards the call to SwingUtilities.layoutCompoundLabel(). This method is
     * here so that a subclass could do Label specific layout and to shorten the
     * method name a little.
     * 
     * @see SwingUtilities#layoutCompoundLabel
     */
    protected String layoutCL(JComponent c, FontMetrics fontMetrics,
            String text, Icon icon, Rectangle viewR, Rectangle iconR,
            Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel((JComponent) c, fontMetrics,
                text, icon, SwingConstants.CENTER, SwingConstants.LEADING,
                SwingConstants.CENTER, SwingConstants.TRAILING,
                /*
                 * c.getVerticalAlignment(), c.getHorizontalAlignment(),
                 * c.getVerticalTextPosition(), c.getHorizontalTextPosition(),
                 */
                viewR, iconR, textR, iconPadding.right + textPadding.left);
    }

    protected void paintEnabledText(Graphics g, String s, int mnemonicIndex,
            int textX, int textY)
    {
        g.setColor(tabBar.getForeground());
        drawText(g, s, mnemonicIndex, textX, textY);
    }

    protected void paintDisabledText(Graphics g, String s, int mnemonicIndex,
            int textX, int textY)
    {
        Color c = tabBar.getBackground();
        g.setColor(c.brighter());
        drawText(g, s, mnemonicIndex, textX + 1, textY + 1);
        g.setColor(c.darker());
        drawText(g, s, mnemonicIndex, textX, textY);
    }

    private void drawText(Graphics g, String s, int mnemonicIndex, int textX,
            int textY)
    {
        NmSwingUtilities.drawStringUnderlineCharAt(/* tabComponent, */g, s,
                mnemonicIndex, textX, textY);
    }

    protected void paintTabBackground(Graphics2D g2, int x, int y, int w,
            int h, boolean selected, boolean hovered)
    {
        if (tabBgGradientHeight < 0 || tabBgGradientHeight != h)
        {
            tabBgGradientHeight = h;
            tabBgGradientMid = tabBgGradientHeight / 2;
            tabBgUpperGradient = new GradientPaint(0, y, tabBgUpperGradientTop,
                    0, y + tabBgGradientMid, tabBgUpperGradientBottom);
            tabBgLowerGradient = new GradientPaint(0, y + tabBgGradientMid,
                    tabBgLowerGradientTop, 0, y + h, tabBgLowerGradientBottom);

            tabBgUpperGradientSelected = new GradientPaint(0, y,
                    tabBgUpperGradientTopSelected, 0, y + tabBgGradientMid,
                    tabBgUpperGradientBottomSelected);
            tabBgLowerGradientSelected = new GradientPaint(0, y
                    + tabBgGradientMid, tabBgLowerGradientTopSelected, 0,
                    y + h, tabBgLowerGradientBottomSelected);
        }

        Paint bgUpperGradient, bgLowerGradient;
        Color outerBorder, innerBorder;

        if (hovered || selected)
        {
            bgUpperGradient = tabBgUpperGradientSelected;
            bgLowerGradient = tabBgLowerGradientSelected;
            outerBorder = tabBorderOuterSelected;
            innerBorder = tabBorderInnerSelected;
        }
        else
        {
            bgUpperGradient = tabBgUpperGradient;
            bgLowerGradient = tabBgLowerGradient;
            outerBorder = tabBorderOuter;
            innerBorder = tabBorderInner;

        }

        Paint oldPaint = g2.getPaint();
        g2.setPaint(bgUpperGradient);
        g2.fillRect(x + 2, y + 2, w - 2, tabBgGradientMid - 2);
        g2.setPaint(bgLowerGradient);
        g2.fillRect(x + 2, y + tabBgGradientMid, w - 2, tabBgGradientMid);
        g2.setPaint(oldPaint);

        g2.setColor(innerBorder);
        drawTabBorder(g2, x + 1, y + 1, w - 2, h - 1);
        g2.setColor(outerBorder);
        drawTabBorder(g2, x, y, w, h-1);
        /*
         * g2.setColor(Color.red); g2.drawRect(x, y, w-1,h-1);
         */
    }

    private void drawTabBorder(Graphics2D g2, int x, int y, int w, int h)
    {
        int b = y + h - 1;
        int r = x + w - 1;

        g2.drawLine(x + 1, y, r - 1, y); // top
        g2.drawLine(x, y + 1, x, b); // left
        g2.drawLine(r, y + 1, r, b); // right
    }

    private void paintBackground(Graphics2D g2)
    {
        if (!tabBar.isOpaque())
            return;

        int w = tabBar.getWidth();
        int h = tabBar.getHeight();

        if (bgGradientEnabled)
        {
            float midY_px = h * BG_GRADIENT_MIDDLE;

            if (resized() || bgGradientTopMiddle == null
                    || bgGradientMiddleBottom == null)
            {
                bgGradientTopMiddle = new GradientPaint(0, 0, bgGradientTop, 0,
                        midY_px, bgGradientMiddle);
                bgGradientMiddleBottom = new GradientPaint(0, midY_px,
                        bgGradientMiddle, 0, h, bgGradientBottom);
            }

            Paint prevPaint = g2.getPaint();

            g2.setPaint(bgGradientTopMiddle);
            g2.fillRect(0, 0, w, (int) midY_px);

            g2.setPaint(bgGradientMiddleBottom);
            g2.fillRect(0, (int) midY_px, w, h - (int) (midY_px));

            g2.setPaint(prevPaint);
        }
        else
        {
            g2.setColor(tabBar.getBackground());
            g2.fillRect(0, 0, w, h);
        }
    }
    
    public int tabForCoordinate(JTabBar<?> tabBar, int x, int y)
    {
        if (this.tabBar != tabBar)
            return -1;
        
        int index = getTabIndexForLocation(x, y);
        
        return index<0 ? -1 : index;
    }

    protected boolean isInCloseButtonBounds(int x, int y) {
        int index = getTabIndexForLocation(x, y);
        if (index<0) return false;
        return closeButtonBounds.contains(x-index*tabBounds.width, y-tabBounds.y);
    }
    
    public int getCloseButtonHoverIndex(int x, int y)
    {
        if (!tabBar.isCloseActionEnabled())
            return -1;
        
        int index = getTabIndexForLocation(x, y);
        return isInCloseButtonBounds(x, y) ? index : -1;
    }

    public void setCloseButtonHoverIndex(int tabIndex)
    {
        if (!tabBar.isCloseActionEnabled())
            tabIndex = -1;
        
        if (this.closeIconHoverIndex != tabIndex)
        {
            this.closeIconHoverIndex = tabIndex;
            tabBar.repaint();
        }
    }

    public int getTabIndexForLocation(int x, int y)
    {
        int w = tabBar.getWidth();

        if (y < tabBounds.y || y > (tabBounds.y+tabBounds.height) || x < 0 || x > w)
            return -1;

        if (specialButtonsVisible)
        {
            if (x<specialTabSize.width) return BACKWARDS_TAB;
            if (x>w-specialTabSize.width*2)
            {
                if (x>w-specialTabSize.width)
                    return DROP_DOWN_TAB;
                else
                    return FORWARD_TAB;
            }
        }
        
        if (tabBounds.width<=0) // implies that there are no tabs
            return -1;

        int index = (x-tabBounds.x) / tabBounds.width;

        if (index < 0)
            return -1;
        if (index >= tabBar.getTabCount())
            return -1;

        return index;
    }

    // **** dimensions

    public Dimension getPreferredSize(JComponent c)
    {
        checkComponent(c);
        checkUpdate();

        int tabCount = tabBar.getTabCount();
        Dimension size;

        int iw = tmpInsets.left + tmpInsets.right;
        int ih = tmpInsets.top + tmpInsets.bottom;
        int h = tabBounds.height + tabPaddingTop + ih;

        if (tabCount > 0)
        {
            size = new Dimension(iw + tabBounds.width * tabCount, h);
        }
        else
        {
            size = new Dimension(iw + tabBounds.width, h);
        }

        setResized(false);
        return size;
    }

    public Dimension getMinimumSize(JComponent c)
    {
        Dimension d = getPreferredSize(c);
        d.setSize(tabBounds.width+specialTabSize.width*3, tabBar.getTabCount() == 0 ? 0 : d.height);
        return d;
    }

    public Dimension getMaximumSize(JComponent c)
    {
        checkComponent(c);
        checkUpdate();

        return new Dimension(Integer.MAX_VALUE, tabBounds.height);
    }

    // **** event handler

    protected BasicEventHandler createEventHandler(JTabBar bar)
    {
        return new BasicEventHandler(bar, this);
    }

    protected BasicEventHandler lookupEventHandler(JTabBar bar)
    {
        Object[] listeners = bar.getListeners(MouseListener.class);
        for (int i = listeners.length - 1; i >= 0; i--)
            if (listeners[i] instanceof BasicEventHandler)
                return (BasicEventHandler) listeners[i];
        return null;
    }

    // **** event handler class

    protected static class BasicEventHandler implements MouseListener,
            MouseMotionListener, FocusListener, DragGestureListener, DragSourceListener, DropTargetListener
    {

        public static String SELECT_INC = "sel.inc";
        public static String SELECT_DEC = "sel.dec";
        public static String SELECT_CLOSETAB = "sel.closeTab";

        protected JTabBar tabBar;

        protected FFTabBarUI ui;
        
        protected DragSource dragSource = new DragSource();
        protected DropTarget dropTarget;
        protected DragGestureRecognizer dgr;

        public BasicEventHandler(JTabBar bar, FFTabBarUI ui)
        {
            this.tabBar = bar;
            this.ui = ui;
        }

        public void install()
        {
            tabBar.addMouseListener(this);
            tabBar.addMouseMotionListener(this);
            tabBar.addFocusListener(this);
            installKeyboardActions();
            
            dgr = dragSource.createDefaultDragGestureRecognizer(tabBar, DnDConstants.ACTION_MOVE, this);
            dropTarget = new DropTarget(tabBar, DnDConstants.ACTION_MOVE, this);
        }

        public void uninstall()
        {
            tabBar.removeMouseListener(this);
            tabBar.removeMouseMotionListener(this);
            tabBar.removeFocusListener(this);
            
            uninstallKeyboardActions();
        }

        public static void loadActionMap(NMLazyActionMap map)
        {
            map.put(new Actions(SELECT_INC));
            map.put(new Actions(SELECT_DEC));
            map.put(new Actions(SELECT_CLOSETAB));
        }

        protected InputMap createInputMapWhenFocused()
        {
            InputMap map = new InputMap();
            fillInputMap(map);
            return map;
        }

        protected void fillInputMap(InputMap map)
        {
            map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), SELECT_INC);
            map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), SELECT_INC);
            map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), SELECT_DEC);
            map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), SELECT_DEC);
            map.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), SELECT_CLOSETAB);
        }

        public void installKeyboardActions()
        {
            NMLazyActionMap.installLazyActionMap(UIManager.getDefaults(),
                    tabBar, getClass(), "TabBarActionMapKey");

            InputMap im = createInputMapWhenFocused();
            SwingUtilities.replaceUIInputMap(tabBar, JComponent.WHEN_FOCUSED,
                    im);
        }

        public void uninstallKeyboardActions()
        {
            SwingUtilities.replaceUIInputMap(tabBar,
                    JComponent.WHEN_IN_FOCUSED_WINDOW, null);
            SwingUtilities.replaceUIInputMap(tabBar, JComponent.WHEN_FOCUSED,
                    null);
            SwingUtilities.replaceUIActionMap(tabBar, null);
        }

        public void mouseClicked(MouseEvent e)
        {
            if (Platform.isLeftMouseButtonOnly(e))
            {
                int closeButton = ui.getCloseButtonHoverIndex(e.getX(), e.getY());
                if (closeButton>=0)
                {
                    saveCloseTab(tabBar, closeButton);
                    return;
                }   
            }
        }

        public void mouseEntered(MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

        public void mouseExited(MouseEvent e)
        {
            ui.setHoverIndex(-1);
            ui.setCloseButtonHoverIndex(-1);
        }
        
        private boolean handlePopupTrigger(MouseEvent e)
        {
            if (Platform.isPopupTrigger(e))
            {   
                int tabIndex = ui.getTabIndexForLocation(e.getX(), e.getY());
                if (tabIndex>=0)
                {
                    tabBar.showContextMenuForTab(e, tabIndex);
                }
                return true;
            }
            return false;
        }

        public void mousePressed(MouseEvent e)
        {   
            if (handlePopupTrigger(e))
                return;
            
            if (!tabBar.hasFocus())
                tabBar.requestFocus();
            
            int closeButton = ui.getCloseButtonHoverIndex(e.getX(), e.getY());
            if (closeButton>=0)
            {
                return;
            }
            
            int index = ui.getTabIndexForLocation(e.getX(), e.getY());
            if (index>=0)
            {
                tabBar.setSelectedIndex(index);
            }
            else
            {
                switch (index)
                {
                    case BACKWARDS_TAB:
                        saveSetSelectedIndex(tabBar, tabBar.getSelectedIndex()-1);
                        break;
                    case FORWARD_TAB:
                        saveSetSelectedIndex(tabBar, tabBar.getSelectedIndex()+1);
                        break;
                    case DROP_DOWN_TAB:
                        ui.showDropDownMenu(e);
                        break;
                }
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            if (handlePopupTrigger(e))
                return;
        }

        public void mouseDragged(MouseEvent e)
        {
            // TODO Auto-generated method stub

        }

        public void mouseMoved(MouseEvent e)
        {
            int index = ui.getTabIndexForLocation(e.getX(), e.getY());
            
            ui.setHoverIndex(index);

            ui.setCloseButtonHoverIndex(ui.getCloseButtonHoverIndex(e.getX(), e.getY()));
        }

        public void focusGained(FocusEvent e)
        {
            if (tabBar.isEnabled() && tabBar.getTabCount() > 0)
                tabBar.repaint();
        }

        public void focusLost(FocusEvent e)
        {
            if (tabBar.isEnabled() && tabBar.getTabCount() > 0)
                tabBar.repaint();
        }

        public static class Actions extends AbstractAction
        {

            /**
             * 
             */
            private static final long serialVersionUID = -8696893310959747343L;

            public Actions(String name)
            {
                super(name);
            }

            public String getName()
            {
                return (String) super.getValue(NAME);
            }

            public void actionPerformed(ActionEvent e)
            {
                JTabBar<?> c = (JTabBar<?>) e.getSource();

                String key = getName();

                if (key == SELECT_INC)
                    saveSetSelectedIndex(c, c.getSelectedIndex() + 1);
                else if (key == SELECT_DEC)
                    saveSetSelectedIndex(c, c.getSelectedIndex() - 1);
                else if (key == SELECT_CLOSETAB)
                    saveCloseTab(c, c.getSelectedIndex());
            }

        }

        static void saveSetSelectedIndex(JTabBar<?> c, int selectedIndex)
        {
            if (checkIndex(c, selectedIndex))
                c.setSelectedIndex(selectedIndex);
        }

        static void saveCloseTab(JTabBar<?> c, int index)
        {
            if (c.isCloseActionEnabled() && checkIndex(c, index))
                c.askRemove(index);
        }

        static boolean checkIndex(JTabBar<?> c, int index)
        {
            return (index >= 0 && index < c.getTabCount());
        }
        
        protected transient static Object dragSourceComponent;

        public void dragGestureRecognized(DragGestureEvent dge)
        {
        	Point p = dge.getDragOrigin();
            if (ui.isInCloseButtonBounds(p.x, p.y)) {
            	return;
            }

            if (dge.getComponent() ==  tabBar && dge.getDragAction() == DnDConstants.ACTION_MOVE)
            {
                int tabIndex = tabBar.getSelectedIndex();
                if (tabIndex<0) return;
                
                Object item = tabBar.getItemAt(tabIndex);
                Transferable t = new TransferableTab(item, tabIndex);
                
                try
                {
                    dragSourceComponent = tabBar;
                    dragSource.startDrag(dge, DragSource.DefaultMoveDrop, t, this);
                }
                catch (InvalidDnDOperationException e)
                {
                    // e.printStackTrace();
                }
            }
        }
        
        public void dragDropEnd(DragSourceDropEvent dsde)
        {
        }


        public void dragEnter(DragSourceDragEvent dsde)
        {      
            DragSourceContext context = dsde.getDragSourceContext();

            if (validDragSourceEvent(dsde))
            {
                context.setCursor(DragSource.DefaultMoveDrop);
            }
            else
            {
                context.setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
        
        protected boolean validDragSourceEvent(DragSourceEvent e)
        {
            DragSourceContext context = e.getDragSourceContext();
            return (context.getComponent() == tabBar && context.getTransferable() instanceof TransferableTab);
        }

        public void dragExit(DragSourceEvent dse)
        {
            DragSourceContext context = dse.getDragSourceContext();

            context.setCursor(DragSource.DefaultMoveNoDrop);
            
            ui.setDropTargetIndex(-1);
        }

        protected transient Point p;
        
        protected Point screenToTabBar(int x, int y)
        {
            if (p == null)
                p = new Point();
            p.setLocation(x, y);
            SwingUtilities.convertPointFromScreen(p, tabBar);
            return p;
        }
        
        public void dragOver(DragSourceDragEvent dsde)
        {
        }

        public void dropActionChanged(DragSourceDragEvent dsde)
        {
        }

        public void dragEnter(DropTargetDragEvent dtde)
        {
            // TODO Auto-generated method stub
            
        }

        public void dragExit(DropTargetEvent dte)
        {
            ui.setDropTargetIndex(-1);
        }

        public void dragOver(DropTargetDragEvent dtde)
        {
            Point p = dtde.getLocation();
            Transferable transfer = dtde.getTransferable();
            if (!transfer.isDataFlavorSupported(TransferableTab.tabIndexFlavor))
            {
                int dragOverTab = ui.getTabIndexForLocation(p.x, p.y);
                if (dragOverTab>=0)
                {
                    // something unknown is dragged around
                    // store previous selection index
                    // TODO restore index after drop
                    if (ui.dndPreviousSelectionIndex < 0)
                        ui.dndPreviousSelectionIndex = ui.tabBar.getSelectedIndex();
                    
                    // select tab below cursor
                    ui.tabBar.setSelectedIndex(dragOverTab);
                }
                dtde.rejectDrag();
                return;
            }
            
            if (!(dropOk(dtde) && transferableOk(transfer)&& actionOk(dtde.getDropAction()) ))
            {
                ui.setDropTargetIndex(-1);
                dtde.rejectDrag();
                return;
            }
            
            
            int index = ui.getTabIndexForLocation(p.x, p.y);

            ui.setDropTargetIndex(index);

            dtde.acceptDrag(DnDConstants.ACTION_MOVE);
        }

        public void drop(DropTargetDropEvent dtde)
        {
            int target = ui.dropTargetIndex;
            ui.setDropTargetIndex(-1);

            if (!(target >= 0 && dropOk(dtde) 
                    && transferableOk(dtde.getTransferable())&& actionOk(dtde.getDropAction()) ))
            {
                dtde.rejectDrop();
                return;
            }

            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            int source;
            try
            {
                source = ((TransferableTab) dtde
                        .getTransferable()
                        .getTransferData(TransferableTab.tabIndexFlavor))
                        .getTabIndex();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                dtde.dropComplete(false);
                return;
            }
            
            tabBar.moveTab(target, source);
            ui.setHoverIndex(target);
            dtde.dropComplete(true);
        }

        public void dropActionChanged(DropTargetDragEvent dtde)
        {
            if (!(dropOk(dtde) && actionOk(dtde.getDropAction()) && transferableOk(dtde.getTransferable())))
                dtde.rejectDrag();
            else
                dtde.acceptDrag(DnDConstants.ACTION_MOVE);
        }

        private boolean actionOk(int action)
        {
            return action == DnDConstants.ACTION_MOVE;
        }
        
        private boolean transferableOk(Transferable t)
        {
            return t.isDataFlavorSupported(TransferableTab.tabIndexFlavor);
        }
        
        private boolean dropOk(DropTargetEvent dtde)
        {
            DropTargetContext context = dtde.getDropTargetContext();

            return context.getComponent() == dragSourceComponent;
        }
        
    }

    private static class TransferableTab implements Transferable
    {
        
        public static DataFlavor tabIndexFlavor = //DataFlavor.stringFlavor; 
            new DataFlavor(TransferableTab.class, "Tab");
        
        private int tabIndex;
        private Object tabItem;

        public TransferableTab(Object tabItem, int tabIndex)
        {
            this.tabItem = tabItem;
            this.tabIndex = tabIndex;
        }
        
        private Transferable getTransferableItem()
        {
            if (tabItem instanceof Transferable)
                return (Transferable) tabItem;
            return null;
        }
        
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
            if (tabIndexFlavor.equals(flavor))
                return this;
            
            Transferable t = getTransferableItem();
            if (t != null)
                return t.getTransferData(flavor);
            throw new UnsupportedFlavorException(flavor);
        }

        public int getTabIndex()
        {
            return tabIndex;
        }
        
        public DataFlavor[] getTransferDataFlavors()
        {
            List<DataFlavor> supportedDataFlavors = new ArrayList<DataFlavor>();
            supportedDataFlavors.add(tabIndexFlavor);
            Transferable t = getTransferableItem();
            if (t != null)
                Collections.addAll(supportedDataFlavors, t.getTransferDataFlavors());
            return supportedDataFlavors.toArray(new DataFlavor[supportedDataFlavors.size()]);
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            DataFlavor[] supported = getTransferDataFlavors();
            for (int i=0;i<supported.length;i++)
                if (supported[i].equals(flavor))
                    return true;
            return false;
        }
        
    }

}
