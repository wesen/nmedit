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

import javax.swing.JComponent;
import javax.swing.border.Border;

import net.sf.nmedit.jtheme.component.JTComponent;

public class JTTextDisplayUI extends JTLabelUI
{

    private final static JTTextDisplayUI instance = new JTTextDisplayUI();
    public static final String borderKey = "TextDisplay.border";

    public static JTTextDisplayUI createUI(JComponent c)
    {
        return instance;
    }

    public void installUI(JComponent c)
    {
        JTComponent jtc = (JTComponent) c;
        Border border = jtc.getContext().getUIDefaults().getBorder(borderKey);
        jtc.setBorder(border);
    }
    
    public void uninstallUI(JComponent c)
    {
        c.setBorder(null);
    }
    
}

