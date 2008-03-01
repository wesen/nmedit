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

    public static final String fontKey = "Label.font";

    private static UIInstance<JTLabelUI> labelUIInstance = new UIInstance<JTLabelUI>(JTLabelUI.class);
    
    public static JTLabelUI createUI(JComponent c)
    {
        JTLabelUI ui = labelUIInstance.getInstance(c);
        if (ui == null) labelUIInstance.setInstance(c, ui = new JTLabelUI());
        return ui;
    }

    private transient Font labelFont;
    
    protected boolean alignLabel = false;
    
    public void installUI(JComponent c)
    {
        if (labelFont == null)
            labelFont = ((JTComponent) c).getContext().getUIDefaults().getFont(fontKey);
        if (labelFont != null)
            c.setFont(labelFont);
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
    private static Rectangle paintIconR = new Rectangle(0,0,0,0);
    private static Rectangle textR = new Rectangle();
    
/*
    public void update(Graphics g, JComponent c) 
    {
        paint(g, c);
    }*/

    private boolean reducible(JTComponent c)
    {
        return c.isReducible();
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

    private transient Font currentFont;
    private transient FontMetrics currentFontMetrics;
    
    protected FontMetrics getFontMetrics(Font font, JComponent c)
    {
        if (font == currentFont)
            return currentFontMetrics;
        currentFont = font;
        return currentFontMetrics = c.getFontMetrics(font); 
    }
    
    public void paintLabel(Graphics g, JTComponent c) 
    {
        JTLabel label = (JTLabel) c;
        String text = label.getText();
        if ((text == null)) {
            return;
        }

        String splitText[] = label.getSplitText();
        if (splitText == null)
        	return;
        
        FontMetrics fm = getFontMetrics(label.getFont(), label);
        Insets insets = c.getInsets(paintViewInsets);

        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = c.getWidth() - (insets.left + insets.right);
        paintViewR.height = c.getHeight() - (insets.top + insets.bottom);
//        paintViewR.width = Short.MAX_VALUE;
//        paintViewR.height = Short.MAX_VALUE;

        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        int yHeight = 0;
        
        for (String elt : splitText) {
        	if (alignLabel)
        		layout(label, fm, elt, label.getWidth(), label.getHeight());
        	
        	if (splitText.length > 1) {
        		layout(label, fm, elt, 0, 0);
        		paintTextR.y = 0;
        	}
        	
        	int textX = paintTextR.x;
        	int textY = yHeight + paintTextR.y + fm.getAscent(); // + paintTextR.y + fm.getAscent();
        	
        	if (label.isEnabled()) {
        		paintEnabledText(label, g, elt, textX, textY);
        	}
        	else {
        		paintDisabledText(label, g, elt, textX, textY);
        	}
        	yHeight += paintTextR.height;
        }
    }
    private String layout(JTLabel label, FontMetrics fm, String text,
            int width, int height) {
        Insets insets = label.getInsets(paintViewInsets);
        Icon icon = null;
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = width - (insets.left + insets.right);
        paintViewR.height = height - (insets.top + insets.bottom);
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        return layoutCL(label, fm, text, icon, paintViewR, paintIconR,
                  paintTextR);
    }

    private Insets viewInsets = new Insets(0,0,0,0);
    private Rectangle viewR = new Rectangle(0,0,0,0);
    private Rectangle iconR = new Rectangle(0,0,0,0);
    
    public Dimension getPreferredSize(JComponent c) 
    {
    	JTLabel label = (JTLabel)c;
    	String splitText[] = label.getSplitText();
    	Insets insets = label.getInsets(viewInsets);
    	Font font = label.getFont();

    	int dx = insets.left + insets.right;
    	int dy = insets.top + insets.bottom;

    	if (((splitText == null) || 
    			((splitText != null) && (font == null)))) {
    		return new Dimension(dx, dy);
    	} else {
    		FontMetrics fm = getFontMetrics(font, label);

    		textR.x = textR.y = textR.width = textR.height = 0;
    		viewR.x = dx;
    		viewR.y = dy;
    		viewR.width = viewR.height = Short.MAX_VALUE;

    		Dimension rv = new Dimension(0, 0);
    		for (String elt : splitText) {
    			layoutCL(label, fm, elt, null, viewR, iconR, textR);
    			int right = textR.x + textR.width;
    			if (right > rv.width) {
    				rv.width = right;
    			}
    			rv.height += textR.height;
    			viewR.y += textR.height;
    		}

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
        
        int verticalAlignment = CENTER;
        int horizontalAlignment = LEADING;
        int verticalTextPosition = CENTER;
        int horizontalTextPosition = TRAILING;
        
        if (alignLabel)
        {
            horizontalAlignment = CENTER;
            horizontalTextPosition = CENTER;
        }
        
        return SwingUtilities.layoutCompoundLabel(
            (JComponent) label,
            fontMetrics,
            text,
            icon,
            verticalAlignment, 
            horizontalAlignment, 
            verticalTextPosition, 
            horizontalTextPosition, 
            viewR,
            iconR,
            textR,
            4//label.getIconTextGap()
            );
    }

}
