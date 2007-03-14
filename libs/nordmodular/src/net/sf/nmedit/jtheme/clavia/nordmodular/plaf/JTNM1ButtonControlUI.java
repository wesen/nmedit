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
package net.sf.nmedit.jtheme.clavia.nordmodular.plaf;

import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.component.JTButtonControl;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.plaf.JTBasicButtonControlUI;
import net.sf.nmedit.jtheme.component.plaf.JTButtonControlUI;

public class JTNM1ButtonControlUI extends JTBasicButtonControlUI
{

    public static final String borderDownKey = "ButtonControlUI.border.down";
    public static final String borderUpKey = "ButtonControlUI.border.up";
 
    public JTNM1ButtonControlUI(JTButtonControl btn)
    {
        super(btn);
        padding = 0;
    }

    public static JTButtonControlUI createUI(JComponent c)
    {
        return new JTNM1ButtonControlUI((JTButtonControl)c);
    }

    private Border borderDown;
    private Border borderUp;
    //private transient Insets maxBorderInsets;
    
    public void installUI(JComponent c)
    {
        super.installUI(c);
        UIDefaults defaults =

            ((JTComponent)c)
            .getContext()
            .getUIDefaults();
        
        btn.setSpacing(0);

        this.borderDown = defaults.getBorder(borderDownKey);
        this.borderUp = defaults.getBorder(borderUpKey);

        if (borderDown == null)
            throw new NullPointerException();
        if (borderUp == null)
            throw new NullPointerException();
        
    }

    protected Insets getBorderInsets(Insets insets)
    {
        if (insets == null)
            insets = new Insets(0,0,0,0);
        insets.set(2, 2, 2, 2);
        return insets;
    }
    
    protected void paintButtonBorder(Graphics2D g, JTButtonControl c, int x, int y, int w, int h
            , boolean selected, boolean hovered)
    {
        Border border = selected|hovered ? borderDown : borderUp;
        
        border.paintBorder(c, g, x, y, w, h);
    }

}

