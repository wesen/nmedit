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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpPane extends JPanel
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3503936712415774819L;

    public static final String NO_HELP = "Help not available.";
    
    private ImageIcon helpIcon;
    private JTextArea textArea;

    public HelpPane()
    {
        this(null);
    }

    public HelpPane(ImageIcon helpIcon)
    {
        this.helpIcon = helpIcon;
        setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setOpaque(true);
        textArea.setBackground(new Color(0xffffff));
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        setHelp(NO_HELP);
    }
    
    public void setHelp(String text)
    {
        textArea.setText(text);
    }

}
