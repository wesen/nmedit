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
 * Created on May 15, 2006
 */
package net.sf.nmedit.nomad.theme.laf;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.nomad.theme.component.NomadLabel;

public class LabelUI extends ComponentUI
{

    public LabelUI()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private final static LabelUI labelUI = new LabelUI();
    
    private Insets labelInsets = new Insets(0,0,0,0);

    public static ComponentUI createUI(JComponent c)
    {
        return labelUI;
    }

    public Dimension getPreferredSize(JComponent c) 
    {
        NomadLabel label = (NomadLabel) c;
        Image image = label.getImage();

        Insets insets = label.getInsets(labelInsets);
        int dx = insets.left + insets.right;
        int dy = insets.top + insets.bottom;
        
        if (image!=null)
            return new Dimension(dx+image.getWidth(null), dy+image.getHeight(null));
        
        String text = label.getText();
        Font font = label.getFont();

        FontMetrics fm = label.getFontMetrics(font);
        
        if (label.isVertical())
        {
            return new Dimension(fm.getMaxAdvance()+dx,
                    ( fm.getHeight() + 1 ) * text.length()+dy);
        }
        else 
        {
            String[] lines = text.split("\\\\n");
            
            int maxW = 0;
            
            for (int i=0;i<lines.length;i++)
                maxW = Math.max(maxW, fm.stringWidth(lines[i]));
            maxW+=dx;
            
            int h = dy+fm.getDescent()+lines.length*fm.getHeight();
            return new Dimension(maxW, h);
        }
    }
    
    public void paint(Graphics g, JComponent component)
    {
        NomadLabel nl = (NomadLabel) component;
        
        Image image = nl.getImage();
        if (image!=null)
            g.drawImage(image, 0, 0, null);
        else
        {
            g.setColor(nl.getForeground());
            g.setFont(nl.getFont());
            
            if (nl.isVertical())
                paintVerticalText(g, nl);
            else
                paintHorizontalText(g, nl);
        }
    }

    private void paintHorizontalText( Graphics g, NomadLabel nl )
    {
        Insets insets = nl.getInsets(labelInsets);
        FontMetrics fm = nl.getFontMetrics(nl.getFont());

        String[] lines = nl.getText().split("\\\\n");
        for (int i=0;i<lines.length;i++)
            g.drawString(lines[i], insets.left, insets.top+
                    (fm.getHeight()*(i+1))-fm.getDescent());
    }

    private void paintVerticalText( Graphics g, NomadLabel nl )
    {
        //Insets insets = nl.getInsets(labelInsets);

        FontMetrics fm = nl.getFontMetrics( nl.getFont() );
        String text = nl.getText();
        
        for (int i = 0; i < text.length(); i++)
            g.drawString( Character.toString( text.charAt( i ) ), 0,
                    ( fm.getHeight() + 1 ) * ( i + 1 ) - 1 );
    }

}
