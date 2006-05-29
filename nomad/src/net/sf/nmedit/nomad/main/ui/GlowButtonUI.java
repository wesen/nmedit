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
 * Created on Apr 30, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import net.sf.nmedit.nomad.util.graphics.ButtonGlowPaint;

public class GlowButtonUI extends BasicButtonUI
{
    
    private static GlowButtonUI glowButtonUI = new GlowButtonUI();
    
    public static ComponentUI createUI(JComponent c) {
        return glowButtonUI;
    }
    public final static Color SILVER = Color.decode("#DAD2CE");
    
    public void update(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton)c;
        
        if (button.isContentAreaFilled())
        {
            ButtonModel model = button.getModel();
            
            boolean glow = true;
            
            if (button instanceof JToggleButton)
            {
                glow=model.isSelected();
            }
            
            if (glow)
                fill((Graphics2D)g, c.getWidth(), c.getHeight(), c.getBackground());
            else if (button.isOpaque())
            {
                super.update(g, c);
                return ;
            }
        }
        
        /*
        if ((c.getBackground() instanceof UIResource) &&
                  button.isContentAreaFilled() && c.isEnabled()) {
            ButtonModel model = button.getModel();
            if (!MetalUtils.isToolBarButton(c)) {
                if (!model.isArmed() && !model.isPressed() &&
                        MetalUtils.drawGradient(
                        c, g, "Button.gradient", 0, 0, c.getWidth(),
                        c.getHeight(), true)) {
                    paint(g, c);
                    return;
                }
            }
            else if (model.isRollover() && MetalUtils.drawGradient(
                        c, g, "Button.gradient", 0, 0, c.getWidth(),
                        c.getHeight(), true)) {
                paint(g, c);
                return;
            }
        }*/
        paint(g, c);
        //super.update(g, c);
    }

    protected void fill(Graphics2D g, int w, int h, Color c)
    {
        Paint prevPaint = g.getPaint();
        Composite prevComposite = g.getComposite();

        g.setPaint(new ButtonGlowPaint(c,w,h));
        g.fillRect(0, 0, w, h);
        
        g.setPaint(prevPaint);
        g.setComposite(prevComposite);
    }
    
}
