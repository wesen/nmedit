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
 * Created on Nov 25, 2006
 */
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.nmutils.swing.NmSwingUtilities;

public class JTLabelUI extends JTComponentUI implements SwingConstants
{

    private final static JTLabelUI instance = new JTLabelUI();
    
    public static final String fontKey = "Label.font";

    public static JTLabelUI createUI(JComponent c)
    {
        return instance;
    }

    public void installUI(JComponent c)
    {
        Font font = 
        ((JTComponent ) c)
        .getContext()
        .getUIDefaults()
        .getFont(fontKey);
        
        
        if (font != null)
            c.setFont(font);
    }
    
    protected void paintEnabledText(JTLabel l, Graphics g, String s, int textX, int textY)
    {
        // int mnemIndex = l.getDisplayedMnemonicIndex();
        g.setColor(l.getForeground());
        g.setFont(l.getFont());
        NmSwingUtilities.drawString( g, s, textX, textY);
    }


    /**
     * Paint clippedText at textX, textY with background.lighter() and then 
     * shifted down and to the right by one pixel with background.darker().
     * 
     * @see #paint
     * @see #paintEnabledText
     */
    protected void paintDisabledText(JTLabel l, Graphics g, String s, int textX, int textY)
    {
        int accChar = 0;//l.getDisplayedMnemonicIndex();
        Color background = l.getBackground();
        g.setColor(background.brighter());
        NmSwingUtilities.drawStringUnderlineCharAt( g, s, accChar,
                                                   textX + 1, textY + 1);
        g.setColor(background.darker());
        NmSwingUtilities.drawStringUnderlineCharAt(g, s, accChar,
                                                   textX, textY);
    }

    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
//    private static Rectangle paintIconR = new Rectangle(0,0,0,0);
    private static Rectangle textR = new Rectangle();
    
/*
    public void update(Graphics g, JComponent c) 
    {
        paint(g, c);
    }*/

    private boolean reducible(JTComponent c)
    {
        return (((JTLabel) c).isReducible());
    }
    
    private static Insets labelInsets = new Insets(0,0,0,0);
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        if (c.isOpaque()) 
        {
            g.setColor(c.getBackground());
            Border b = c.getBorder();
            if (b == null || b.isBorderOpaque())
            {
                g.fillRect(0, 0, c.getWidth(),c.getHeight());
            }
            else
            {
                Insets i = c.getInsets(labelInsets);
                g.fillRect(i.left, i.top, c.getWidth()-i.left-i.right, c.getHeight()-i.top-i.bottom);    
            }
            
        }
        
        if (reducible(c))
            paintLabel(g, c);
    }
    
    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {    
        if (!reducible(c))
            paintLabel(g, c);
    }

    public void paintLabel(Graphics g, JTComponent c) 
    {
        JTLabel label = (JTLabel) c;
        String text = label.getText();
        if ((text == null)) {
            return;
        }

        FontMetrics fm = label.getFontMetrics(label.getFont());
        Insets insets = c.getInsets(paintViewInsets);

        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = c.getWidth() - (insets.left + insets.right);
        paintViewR.height = c.getHeight() - (insets.top + insets.bottom);

        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        if (text != null) {/*
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
        v.paint(g, paintTextR);
        } else {*/
        int textX = paintTextR.x;
        int textY = paintTextR.y + fm.getAscent();
        
        if (label.isEnabled()) {
            paintEnabledText(label, g, text, textX, textY);
        }
        else {
            paintDisabledText(label, g, text, textX, textY);
        }
        /*}*/
        }
    }

    private Insets viewInsets = new Insets(0,0,0,0);
    private Rectangle viewR = new Rectangle(0,0,0,0);
    private Rectangle iconR = new Rectangle(0,0,0,0);
    
    public Dimension getPreferredSize(JComponent c) 
    {
        JTLabel label = (JTLabel)c;
        String text = label.getText();
        Insets insets = label.getInsets(viewInsets);
        Font font = label.getFont();

        int dx = insets.left + insets.right;
        int dy = insets.top + insets.bottom;

        if (((text == null) || 
             ((text != null) && (font == null)))) {
            return new Dimension(dx, dy);
        }
        else {
            FontMetrics fm = label.getFontMetrics(font);

            textR.x = textR.y = textR.width = textR.height = 0;
            viewR.x = dx;
            viewR.y = dy;
            viewR.width = viewR.height = Short.MAX_VALUE;

            layoutCL(label, fm, text, null, viewR, iconR, textR);
            int x1 = textR.x;
            int x2 = textR.x + textR.width;
            int y1 = textR.y;
            int y2 = textR.y + textR.height;
            Dimension rv = new Dimension(x2 - x1, y2 - y1);

            rv.width += dx;
            rv.height += dy;
            return rv;
        }
    }

    protected String layoutCL(
        JTLabel label,                  
        FontMetrics fontMetrics, 
        String text, 
        Icon icon, 
        Rectangle viewR, 
        Rectangle iconR, 
        Rectangle textR)
    {
        return SwingUtilities.layoutCompoundLabel(
            (JComponent) label,
            fontMetrics,
            text,
            icon,
            CENTER, // label.getVerticalAlignment(),
            LEADING, // label.getHorizontalAlignment(),
            CENTER, // label.getVerticalTextPosition(),
            TRAILING, // label.getHorizontalTextPosition(),
            viewR,
            iconR,
            textR,
            4//label.getIconTextGap()
            );
    }

}
