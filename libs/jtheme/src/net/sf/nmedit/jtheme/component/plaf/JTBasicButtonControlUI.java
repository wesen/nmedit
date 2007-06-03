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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.component.JTButtonControl;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.misc.CallDescriptor;

public class JTBasicButtonControlUI extends JTButtonControlUI implements SwingConstants
{

    protected JTButtonControl control;
    
    private Insets paddingInsets = new Insets(0,0,0,0); // inside border
    private int iconLabelGap = 2; // space between icon and label
    
    private Border border;
    private Border selectedBorder;
    
    private transient Insets borderInsets;
    private transient Insets selectedBorderInsets;
    private transient Insets maxBorderInsets;
    
    private transient int internalHoverIndex = -1;
    private transient int internalArmedIndex = -1;

    private Color stateBackground ;
    private Color selectedBackground ;
    
    public JTBasicButtonControlUI(JTButtonControl control)
    {
        this.control = control;
    }
   
    public static JTComponentUI createUI(JComponent c)
    {
        return new JTBasicButtonControlUI((JTButtonControl) c);
    }
    
    private int normalizeButtonIndex(int index)
    {
        if (index<0) return -1;
        else if (index>=range()) return -1;
        else return index;
    }
    
    public void setHoveredAt(int index)
    {
        index = normalizeButtonIndex(index);
        
        if (internalHoverIndex != index)
        {
            internalHoverIndex = index;
            control.repaint();
        }
    }
    
    public void setArmedAt(int index)
    {

        index = normalizeButtonIndex(index);
        
        if (internalArmedIndex != index)
        {
            internalArmedIndex = index;
            control.repaint();
        }
    }
    
    private Insets getBorderInsets()
    {
        if (borderInsets == null)
            borderInsets = border.getBorderInsets(control);
        return borderInsets;
    }
    
    private Insets getSelectedBorderInsets()
    {
        if (selectedBorderInsets == null)
            selectedBorderInsets = selectedBorder.getBorderInsets(control);
        return selectedBorderInsets;
    }
    
    private Insets getMaxBorderInsets()
    {
        if (maxBorderInsets == null)
        {
            Insets a = getBorderInsets();
            Insets b = getSelectedBorderInsets();
            
            maxBorderInsets = new Insets(
                    Math.max(a.top, b.top),
                    Math.max(a.left, b.left),
                    Math.max(a.bottom, b.bottom),
                    Math.max(a.right, b.right)      
            );
        }
        return maxBorderInsets;
    }
    
    public void installUI(JComponent c)
    {
        checkComponent(c);
        UIDefaults defaults = control.getContext().getUIDefaults();
        
        control.setFocusable(true);
        
        border = defaults.getBorder(BORDER_KEY);
        selectedBorder = defaults.getBorder(SELECTED_BORDER_KEY);
        
        Color background = defaults.getColor(BACKGROUND_KEY);
        if (background != null)
            control.setBackground(background);
        else
        {
            background = control.getBackground();
        }
        
        stateBackground = defaults.getColor(BACKGROUND_STATE_KEY);
        selectedBackground = defaults.getColor(BACKGROUND_SELECTED_KEY);
        if (selectedBackground == null)
            selectedBackground = stateBackground;
        
        if (border == null)
            border = BorderFactory.createEmptyBorder();
        if (selectedBorder == null)
            selectedBorder = border;
        
        
        BasicButtonListener bbl = createBasicButtonListener(control);
        if (bbl != null)
            bbl.install(control);
    }
    
    public void uninstallUI(JComponent c)
    {
        checkComponent(c);
        BasicButtonListener bbl = getBasicButtonListener(control);
        if (bbl != null)
            bbl.uninstall(control);
    }

    private void checkComponent(JComponent c)
    {
        if (c != this.control)
            throw new IllegalArgumentException("invalid component "+c);
    }

    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        checkComponent(c);
        
        if (c.isOpaque())
        {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        checkComponent(c);
        checkContents();
        
        final int range = range();
        
        if (range <= 0)
            return;

        int w = control.getWidth();
        int h = control.getHeight();
        int btnw = w; 
        int btnh = h;
        final int min = control.getMinValue();
        final int value = control.getValue();
        final int intSelectionIndex = min+value;
        
        if (control.isCyclic())
        {
            Icon icon = btnIcons[intSelectionIndex];
            String label = btnLabels[intSelectionIndex];
            
            int defaultSelection = -1;
            
            if (control.isToggleEnabledRequested() && (!control.isIncrementModeEnabled()))
            {
                defaultSelection = control.getDefaultValue()-min+1;
            }
            
            paintButton(g, defaultSelection, intSelectionIndex, icon, label, 0, 0, btnw, btnh);
        }
        else 
        {
            float dx, dy;
            if (control.getOrientation() == HORIZONTAL)
            {
                float btnf = w/(float)range;
                btnw = (int)btnf;
                dx = btnf;
                dy = 0;
            }
            else
            {
                // vertical
                float btnf = h/(float)range;
                btnh = (int)btnf;
                dx = 0;
                dy = btnf;
            }
            
            float x = 0;
            float y = 0;
            
            for (int i=0;i<range;i++)
            {
                Icon icon = btnIcons[i];
                String label = btnLabels[i];
             
                paintButton(g, intSelectionIndex, min+i, icon, label, (int)x, (int)y, btnw, btnh);
                
                x+=dx; 
                y+=dy;
            }
        }
    }
    
    public int getInternalButtonIndexForLocation(Point loc)
    {
        return getInternalButtonIndexForLocation(loc.x, loc.y);
    }
    
    public int getInternalButtonIndexForLocation(int x, int y)
    {
        checkContents();
        
        int index = -1;
        
        if (!control.isCyclic())
        {
            int range = range();
            if (range>0)
            {
                int size;
                int pos;
                if (control.getOrientation()==HORIZONTAL)
                {
                    pos = x;
                    size = control.getWidth();
                }
                else
                {
                    pos = y;
                    size = control.getHeight();
                }
                
                if (size>0)
                {
                    if (range==0)
                        index = -1;
                    else
                    {
                        int d = size/range;
                        if (d==0)
                            index = -1;
                        else
                        {
                            index = pos/d;
                            if (index<0 || index>range())
                                index = -1;
                            }
                        }
                }
            }
        }
        else
        {
            index = control.getValue()-control.getMinValue(); 
        }
        return index;
    }
    
    private void paintButton(Graphics2D g, int intBtnSelIndex, 
            int intBtnIndex, Icon icon, String label,
            int x, int y, int btnw, int btnh)
    {
        // intButtonIndex in [0..range())
        boolean selected;
        Border border;
        Insets borderInsets;
        
        boolean armed = internalArmedIndex == intBtnIndex;
        boolean hovered = internalHoverIndex == intBtnIndex;
        
        if (intBtnSelIndex == intBtnIndex || armed)
        {
            selected = true;
            border = selectedBorder;
            borderInsets = getSelectedBorderInsets();
        }
        else
        {
            selected = false;
            border = this.border;
            borderInsets = getBorderInsets();
        }

        Color bg = null;
        
        if ((control.hasFocus() && (selected || control.isCyclic()))||hovered||armed)
            bg = stateBackground;
        else if (selected)
            bg = selectedBackground;
        
        if (bg != null)
        {
            g.setColor(bg);
            g.fillRect(x+borderInsets.left, y+borderInsets.top, 
                    btnw-(borderInsets.left+borderInsets.right),
                    btnh-(borderInsets.top+borderInsets.bottom));
        }
        
        border.paintBorder(control, g, x, y, btnw, btnh);        
        int px = x+borderInsets.left+paddingInsets.left;
        int py = y+borderInsets.top+paddingInsets.top;
        

        if (selected) px+=1;
        int mid = y+btnh/2;
        
        int contentwidth = 0;
        FontMetrics fm = null;
        if (icon != null)
            contentwidth+=icon.getIconWidth();
        if (label != null)
        {
            fm = getFontMetrics();
            contentwidth+=iconLabelGap+fm.stringWidth(label);
        } 
        
        // inner width
        int t = btnw-(borderInsets.left+paddingInsets.left+borderInsets.right+paddingInsets.right);
        
        int off = (t-contentwidth)/2;
        
        px += off;

        if (icon != null)
        {
            int top = Math.max(py, (2*mid-icon.getIconHeight())/2);
            if (selected) top+=1;
            icon.paintIcon(control, g, px, top);
            px+=icon.getIconWidth()+iconLabelGap;
        }
        
        if (label!=null)
        {
            g.setColor(control.getForeground());
            int top = Math.max(py, (2*mid+(fm.getAscent()+fm.getDescent()))/2-fm.getDescent());
            if (selected) top+=1;
            g.drawString(label, px, top);
        }
    }

    private int range()
    {
        if (control.isIncrementModeEnabled())
            return 2;
        else
            return control.getMaxValue()-control.getMinValue()+1;
    }
    
    private transient String[] btnLabels;
    private transient Icon[] btnIcons;
    private transient Dimension preferredButtonSize;
    
    private boolean eq(Object a, Object b)
    {
        return a==b || (a!=null && a.equals(b));
    }

    private boolean checkLabels()
    {
        int range = range();
        boolean valid = true;
        
        if (btnLabels == null || btnLabels.length != range)
        {
            // rebuild strings
            btnLabels = new String[range];

            int min = control.getMinValue();
            for (int i=0;i<range;i++)
                btnLabels[i] = control.getText(min+i);
            valid = false; // not valid
        }
        else
        {
            int min = control.getMinValue();
            String label;
            for (int i=0;i<range;i++)
            {
                label = control.getText(min+i);
                if (!eq(btnLabels[i], label))
                {
                    btnLabels[i] = label;
                    valid = false;
                }
            }
        }

        // returns true is valid, false if labels changed
        return valid;
    }
    
    private boolean checkIcons()
    {
        int range = range();
        boolean valid = true;
        
        if (btnIcons == null || btnIcons.length != range)
        {
            // rebuild strings
            btnIcons = new Icon[range];

            int min = control.getMinValue();
            for (int i=0;i<range;i++)
                btnIcons[i] = control.getIcon(min+i);
            valid = false; // not valid
        }
        else
        {
            int min = control.getMinValue();
            Icon icon;
            for (int i=0;i<range;i++)
            {
                icon = control.getIcon(min+i);
                if (btnIcons[i]!=icon)
                {
                    btnIcons[i] = icon;
                    valid = false;
                }
            }
        }

        // returns true is valid, false if icons changed
        return valid;
    }
    
    private boolean checkContents()
    {
        return checkIcons() & checkLabels();
    }
    
    private boolean checkPreferredContents()
    {
        if (checkContents() || preferredButtonSize == null)
        {
            computePreferedButtonSize();
            return true;
        }
        return false;
    }
    
    public Dimension getPreferredSize(JComponent c)
    {
        checkComponent(c);
        checkPreferredContents();
        
        Dimension d = new Dimension(preferredButtonSize);
        
        if (!control.isCyclic())
        {
            if (control.getOrientation() == HORIZONTAL)
            {
                d.width *= range();
            }
            else
            {
                d.height*= range();
            }
        }
        
        return d;
    }
    
    private transient FontMetrics fontMetrics;
    
    private FontMetrics getFontMetrics()
    {
        if (fontMetrics == null)
            fontMetrics = control.getFontMetrics(control.getFont());
        return fontMetrics;
    }

    protected BasicButtonListener createBasicButtonListener(JTButtonControl btn)
    {
        return BasicButtonListener.createListener(this);
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
    
    protected static class BasicButtonListener implements MouseListener, 
        MouseMotionListener, FocusListener, ChangeListener, KeyListener
    {

        public static BasicButtonListener createListener(JTBasicButtonControlUI controlUI2)
        {
            return new BasicButtonListener();
        }
        
        protected JTButtonControl getControl(ComponentEvent e)
        {
            Component c = e.getComponent();
            if (c!= null && c instanceof JTButtonControl)
                return (JTButtonControl) c;
            return null;
        }
        
        protected JTBasicButtonControlUI getUI(JTButtonControl control)
        {
            Object ui = control.getUI();
            if (ui != null && ui instanceof JTBasicButtonControlUI)
                return (JTBasicButtonControlUI) ui;
            else
                return null;
        }
        
        public void install(JTButtonControl btn)
        {
            btn.addMouseListener(this);
            btn.addMouseMotionListener(this);
            btn.addFocusListener(this);
            btn.addChangeListener(this);
            btn.addKeyListener(this);
        }
        
        public void uninstall(JTButtonControl btn)
        {
            btn.removeMouseListener(this);
            btn.removeMouseMotionListener(this);
            btn.removeFocusListener(this);
            btn.removeChangeListener(this);
            btn.removeKeyListener(this);
        }

        transient JTButtonControl selectedControl;
        transient JTBasicButtonControlUI selectedUI;
        transient int internalSelectedButtonIndex;
        
        public boolean select(MouseEvent e)
        {
            JTButtonControl c = getControl(e);
            JTBasicButtonControlUI ui = null;
            int index = -1;
            if (c!=null)
            {
                ui = getUI(c);
                if (ui != null)
                    index = ui.getInternalButtonIndexForLocation(e.getX(), e.getY());
            }
            
            selectedControl = c;
            selectedUI = ui;
            internalSelectedButtonIndex = index;
            
            return index>=0;
        }
        
        public void mouseClicked(MouseEvent e)
        {
        }
        
        protected void checkArmedHoveredState(MouseEvent e)
        {
            if (select(e))
            {
                if (selectedControl.contains(e.getX(), e.getY()))
                {
                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        selectedUI.setHoveredAt(-1);
                        selectedUI.setArmedAt(internalSelectedButtonIndex);
                    }
                    else
                    {
                        selectedUI.setArmedAt(-1);
                        selectedUI.setHoveredAt(internalSelectedButtonIndex);
                    }
                }
                else
                {
                    selectedUI.setHoveredAt(-1);
                    selectedUI.setArmedAt(-1);   
                }
            }
        }

        public void mouseEntered(MouseEvent e)
        {
            checkArmedHoveredState(e);
        }

        public void mouseExited(MouseEvent e)
        {
            checkArmedHoveredState(e);
        }

        public void mousePressed(MouseEvent e)
        {
            checkArmedHoveredState(e);
            if (!e.getComponent().hasFocus())
                e.getComponent().requestFocus();
        }

        public void mouseReleased(MouseEvent e)
        {
            if (select(e))
            {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1)
                {
                    
                    CallDescriptor call = selectedControl.getCall();
                    if ( call != null)
                    {
                        call.call();
                    }
                    else
                    {
                        int newValue;
                        if (selectedControl.isIncrementModeEnabled())
                        {
                            newValue = selectedControl.getValue()+(internalSelectedButtonIndex>0?+1:-1);
                        }
                        else
                        if (selectedControl.isCyclic())
                        {
                            newValue = selectedControl.getValue()+1;
                            if (newValue>selectedControl.getMaxValue())
                                newValue = selectedControl.getMinValue();
                        }
                        else
                        {
                            newValue = selectedControl.getMinValue()+internalSelectedButtonIndex;
                        }
                        
                        selectedControl.setValue(newValue);
                    }
                }
                selectedUI.setHoveredAt(-1);
                selectedUI.setArmedAt(-1);   
            }
        }

        public void mouseDragged(MouseEvent e)
        {
            checkArmedHoveredState(e);
        }

        public void mouseMoved(MouseEvent e)
        {
            checkArmedHoveredState(e);
        }

        public void focusGained(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void focusLost(FocusEvent e)
        {
            e.getComponent().repaint();
        }

        public void stateChanged(ChangeEvent e)
        {
            Object o = e.getSource();
            if (o instanceof Component)
                ((Component)o).repaint();
        }

        public void keyPressed(KeyEvent e)
        {
            // if (e.getModifiers() == 0)
            {
                JTButtonControl control = getControl(e);
                if (control != null)
                {
                    switch (e.getKeyCode())
                    {
                        case KeyEvent.VK_UP:
                            //if (ui.btn.getOrientation() == SwingConstants.VERTICAL)
                                incValue(control);
                            break;
                        case KeyEvent.VK_DOWN:
                            //if (ui.btn.getOrientation() == SwingConstants.VERTICAL)
                                decValue(control);
                            break;
                        case KeyEvent.VK_SPACE:
                            defaultValue(control);
                            break;
                    }
                }
            }
        }

        private void incValue(JTButtonControl control)
        {
            control.setValue(control.getValue()+1);
        }

        private void decValue(JTButtonControl control)
        {
            control.setValue(control.getValue()-1);
        }

        private void defaultValue(JTButtonControl control)
        {
            control.setValue(control.getDefaultValue());
        }

        public void keyReleased(KeyEvent e)
        {
            // no op
        }

        public void keyTyped(KeyEvent e)
        {
            // no op
        }

    }
    
    private void computePreferedButtonSize()
    {
        int maxIconWidth = 0;
        int maxIconHeight = 0;
        int maxStringWidth = 0;
        
        FontMetrics fm = getFontMetrics();
        
        for (int i=0;i<btnLabels.length;i++)
        {
            // btnLabels.length == btnIcons.length
            
            Icon icon = btnIcons[i];
            if (icon != null)
            {
                maxIconWidth = Math.max(maxIconWidth, icon.getIconWidth());
                maxIconHeight = Math.max(maxIconHeight, icon.getIconHeight());
            }
            String label = btnLabels[i];
            if (label != null)
            {
                int stringWidth = SwingUtilities.computeStringWidth(fm, label);
                maxStringWidth = Math.max(maxStringWidth, stringWidth);
            }
            
        }
        
        if (preferredButtonSize == null)
            preferredButtonSize = new Dimension();
        
        Insets mb = getMaxBorderInsets();
        
        int pw = mb.left + paddingInsets.left
            + (maxIconWidth>0?(maxIconWidth+iconLabelGap):0) + maxStringWidth 
            + paddingInsets.right +mb.right;
        int ph = mb.top + paddingInsets.top
            + Math.max(fm.getAscent()+fm.getDescent(), maxIconHeight)
            + paddingInsets.bottom +mb.bottom;

        preferredButtonSize.setSize(pw, ph);
    }

    /*
    // for testing:
    public static void main(String[] args) throws JTException
    {
        
        BufferedImage img = new BufferedImage(11, 11, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        try
        {
            g2.setColor(Color.GREEN);
            g2.fillRect(0, 0, img.getWidth(), img.getHeight());
        }
        finally
        {
            g2.dispose();
        }
        ImageIcon icon = new ImageIcon(img);

        JTContext jtc = new JTNM1Context(null);
        
        jtc.getUIDefaults().put(JTButtonControl.uiClassID, JTBasicButtonControlUI.class.getName());
        jtc.getUIDefaults().put(BORDER_KEY, JTNM1BorderFactory.createNordEditor311RaisedButtonBorder());
        jtc.getUIDefaults().put(SELECTED_BORDER_KEY, JTNM1BorderFactory.createNordEditor311LoweredButtonBorder());
        jtc.getUIDefaults().put(BACKGROUND_KEY, new ColorUIResource(Color.LIGHT_GRAY));
        jtc.getUIDefaults().put(BACKGROUND_STATE_KEY, new ColorUIResource(Color.LIGHT_GRAY.brighter()));
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(0, 0, 200, 200);
        f.getContentPane().setLayout(null);
        
        JTButtonControl btc = jtc.createComponentInstance(JTButtonControl.class);

        btc.setAdapter(new JTDefaultControlAdapter(0, 2, 0));
        btc.setText(0, "Hi");
        btc.setText(1, "Di");
        btc.setText(2, "Ho");
        btc.setIcon(1, icon);
        btc.setCyclic(false);
        
        f.getContentPane().add(btc);
        btc.setLocation(10, 10);
        btc.setSize(btc.getPreferredSize());
    //    btc.setSize(btc.getWidth(), btc.getHeight()+50);
        f.setVisible(true);
    }*/
    
}
