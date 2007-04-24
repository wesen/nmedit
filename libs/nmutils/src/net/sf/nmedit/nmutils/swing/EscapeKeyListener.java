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
package net.sf.nmedit.nmutils.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EscapeKeyListener extends KeyAdapter
{
    
    private boolean escapeTyped = false;
    private ActionListener a;
    private int actionId;
    private String actionCommand;
    
    public EscapeKeyListener(ActionListener a)
    {
        this(a, 0, "escape");
    }

    public EscapeKeyListener(ActionListener a, int actionId, String actionCommand)
    {
        this.a = a;
        this.actionId = actionId;
        this.actionCommand = actionCommand;
    }
    
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE:
                escape(e.getSource());
                break;
        }
    }

    public void escape(Object source)
    {
        if (escapeTyped) return;
        escapeTyped = true;
        a.actionPerformed(new ActionEvent(source, actionId, actionCommand));
    }

}
