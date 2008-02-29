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
package net.sf.nmedit.nomad.core.swing;

import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;

public class Factory
{
    /** Defines the margin used in toolbar buttons. */
    private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);

    public static void setupToolBarButton(AbstractButton button) 
    {
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
    }

    public static JButton createToolBarButton(Action action) 
    {
        JButton button = new JButton(action);
        setupToolBarButton(button);
        // show either icon or text but not both: 
        button.putClientProperty("hideActionText", Boolean.TRUE);
        return button;
    }

}
