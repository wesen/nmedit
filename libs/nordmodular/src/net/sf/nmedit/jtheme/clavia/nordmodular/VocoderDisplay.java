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
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class VocoderDisplay extends JTDisplay implements ChangeListener
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 519965459660076704L;
    private JTControlAdapter[] adapters;

    public VocoderDisplay(JTContext context)
    {
        super(context);
        this.adapters = new JTControlAdapter[getBands()];
    }

    public int getBands()
    {
        return 16;
    }

    public void rnd()
    {
        for (int i=getBands()-1;i>=0;i--)
        {
            JTControlAdapter a = adapters[i];
            if (a != null)
                a.setNormalizedValue(Math.random());
        }
    }
    
    public int getBand(int band)
    {
        JTControlAdapter adapter = adapters[band];
        return adapter != null ? adapter.getValue() : 0;
    }
    
    public void setBand(int band, int value)
    {
        JTControlAdapter adapter = adapters[band];
        if (adapter != null)
            adapter.setValue(value);
    }
    
    @BindParameter(name="band", count=16)
    public void setBandAdapter(int band, JTControlAdapter adapter)
    {
        JTControlAdapter old = adapters[band];
        if (old != adapter)
        {
            if (old != null)
                old.setChangeListener(null);
            this.adapters[band] = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        repaint();
    }
    
    public void paintDynamicLayer(Graphics2D g2)
    {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Insets insets = getInsets();
        
        int aw = getWidth()-(insets.left+insets.right);
        int ah = getHeight()-(insets.top+insets.bottom);
        
        float space = aw/(float)getBands(); // space between two bands
        int loffset = insets.left+(int)(space/2); // offset from left
        int boffset = ah+insets.top-1; // bottom

        g2.setColor(getForeground());
        for (int band=getBands()-1;band>=0;band--)
        {
            JTControlAdapter a = adapters[band];
            if (a != null && a.getValue()>0)
            {
                int x0 = loffset+(int)(space*(a.getValue()-1));
                int x1 = loffset+(int)(space*band);
                g2.drawLine(x0, insets.top, x1, boffset);                
            }
        }
    }
    
}
