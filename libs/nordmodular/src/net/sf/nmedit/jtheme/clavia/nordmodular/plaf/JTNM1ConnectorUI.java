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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.Signal;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.jtheme.component.plaf.JTBasicConnectorUI;
import net.sf.nmedit.nmutils.graphics.RoundGradientPaint;

public class JTNM1ConnectorUI extends JTBasicConnectorUI
{

    private static JTNM1ConnectorUI instance = new JTNM1ConnectorUI();

    protected Color darkBright = new Color(0x22000000, true);
    protected Color darkDark = new Color(0xAA000000, true);
    
    public static JTBasicConnectorUI createUI(JComponent c)
    {
        return instance;
    }
    
    private transient int holeSize;
    private transient Paint holeGradient;
    
    protected Paint getHoleGradient(int size)
    {
        if (holeGradient == null || holeSize != size)
        {
            size--;
            double cxy = (size/(2.0));
            double crad = (5*size/(4.0*4));
            holeGradient = new RoundGradientPaint(
                    cxy, cxy, crad, darkDark, darkBright
            );
        }
        return holeGradient;
    }
    
    protected void paintConnector(Graphics2D g, JTConnector c, Signal signal, 
            boolean output, 
            boolean connected, boolean focused)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = getSize(c);
        
        //Color outline = c.hasFocus() ? Color.BLUE : Color.BLACK;
        g.setColor(signal.getColor());
        
        Paint holeGradient = getHoleGradient(size);
        int cxy = (int)(size/(2.0));
        int crad = (int)(5*size/(4.0*4));
        
        if (output)
        {
            g.fillRect(0, 0, size, size);

            if (connected)
                g.setPaint(signal.getColor());
            else
            g.setPaint(holeGradient);
            g.fillOval( cxy-crad, cxy-crad, crad*2, crad*2 );

            g.setColor(darkBright);
            g.drawRect(0, 0, size-1, size-1);

            if (focused)
            {
                g.setColor(signal.getColor().brighter());
                g.drawRect(0, 0, size-1, size-1);
            }
        }
        else
        {
            g.fillOval(0, 0, size, size);

            if (connected)
                g.setPaint(signal.getColor());
            else
                g.setPaint(holeGradient);
            g.fillOval( cxy-crad, cxy-crad, crad*2, crad*2 );
            
            g.setColor(darkBright);
            g.drawOval(0, 0, size-1, size-1);
            
            if (focused)
            {
                g.setColor(signal.getColor().brighter());
                g.drawOval(0, 0, size-1, size-1);
            }
        }
    }
    
    public Dimension getPreferredSize(JComponent c)
    {
        return new Dimension(13, 13);
    }
    
}

