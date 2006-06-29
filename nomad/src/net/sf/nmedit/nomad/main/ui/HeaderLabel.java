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
 * Created on Jun 29, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.Icon;
import javax.swing.JLabel;

public class HeaderLabel extends JLabel
{
    
    // TODO getters/setters for gradient color
    
    public final static Color gradT1 = Color.decode("#CFB651");
    public final static Color gradT2 = Color.decode("#FFD763");

    private Color gradTop = gradT1;
    private Color gradBottom = gradT2;
    private static GradientPaint defaultGradient = createGradient(gradT1, gradT2);
    private GradientPaint gradient = defaultGradient;

    private static GradientPaint createGradient(Color gtop, Color gbottom)
    {
        return new GradientPaint(0, 0, gtop, 0, 6, gbottom, false);
    }
 
    public Color getGradientTop()
    {
        return gradTop;
    }
 
    public Color getGradientBottom()
    {
        return gradBottom;
    }
    
    protected void paintComponent(Graphics g)
    {
        // paint background

        Graphics2D g2 = (Graphics2D) g;
        Paint p = g2.getPaint();
        g2.setPaint(gradient);
        g.fillRect(0, 0, getWidth(), getHeight());
        g2.setPaint(p);
        
        super.paintComponent(g);
    }
    
    public HeaderLabel( String text, Icon icon, int horizontalAlignment )
    {
        super( text, icon, horizontalAlignment );
    }

    public HeaderLabel( String text, int horizontalAlignment )
    {
        super( text, horizontalAlignment );
    }

    public HeaderLabel( String text )
    {
        super( text );
    }

    public HeaderLabel( Icon image, int horizontalAlignment )
    {
        super( image, horizontalAlignment );
    }

    public HeaderLabel( Icon image )
    {
        super( image );
    }

    public HeaderLabel()
    {
        super();
    }
    
}
