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
 * Created on Sep 2, 2006
 */
package net.sf.nmedit.nomad.theme.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Signal;
import net.sf.nmedit.nomad.patch.ui.Cable;
import net.sf.nmedit.nomad.patch.ui.Curve;
import net.sf.nmedit.nomad.util.NomadUtilities;

public class CableRenderer
{

    private final static float cableSize = 2.3f;
    private final static float innerSize = cableSize-0.6f;
    private BasicStroke stShadow = new BasicStroke(cableSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private BasicStroke stFill   = new BasicStroke(innerSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public final static double shadowAlpha = 0.3;
    
    Configuration[] conf = new Configuration[7];

    public CableRenderer()
    {
        for (int i=0;i<conf.length;i++)
            conf[i] = new Configuration(Signal.bySignalID(i));
    }
    
    public void render(Graphics2D g2, Curve c)
    {
        Signal s;
        if (c instanceof Cable)
        {
            s = ((Cable)c).getColorCode();
            if (s==null) s = Signal.NONE;
        }
        else
        {
            s = Signal.NONE;
        }
        
        Configuration conf = this.conf[s.getSignalID()] ;
        paintWithShadow(g2, c, conf.c, conf.shadow);
    }

    public void paintWithShadow(Graphics2D g2, Curve curve, Color fill, Color shadow) {
        Stroke restoreStroke = g2.getStroke(); // save current stroke

            g2.setStroke(stShadow);
            g2.setColor(shadow);
    
            g2.translate(+1,+1); // draw shadow with a small offset
            g2.draw(curve);
            g2.translate(-1,-1); // undo translation
    
        g2.setStroke(stFill);
        g2.setColor(fill);
        
        g2.draw(curve);
        
        g2.setStroke(restoreStroke); // restore stroke
    }

    private final static class Configuration
    { 
        final Color c;
        final Color shadow;
        
        Configuration(Signal s)
        {
            this(s.getDefaultColor());
        }
        
        Configuration(Color c)
        {
            this.c = c;
            shadow = NomadUtilities.neighbour(c, Color.BLACK, shadowAlpha);
        }
        
    }
    
}
