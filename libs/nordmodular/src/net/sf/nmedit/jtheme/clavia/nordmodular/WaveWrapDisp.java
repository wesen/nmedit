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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

/*
 * Created on Jul 24, 2006
 */

public class WaveWrapDisp extends JTDisplay implements ChangeListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 8368226950144587665L;

    public WaveWrapDisp( JTContext context )
    {
        super( context );
    }

    private double vwrap = 0; // 0 - 1

    private int gw = 0;
    private int gh = 0;
    private boolean flagForceUpdate = true;
    private GeneralPath gp = new GeneralPath();
    private boolean modified = true;

    protected void setModified(boolean modified)
    {
        this.modified = modified;
    }
    protected void paintComponent(Graphics g)
    {
        if (modified)
        {
            modified = false;
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }
    
    protected void paintStaticLayer(Graphics2D g)
    {
        super.paintStaticLayer(g);
        
        // paint cross
        paintCross(g);
    }
    
    protected void paintCross(Graphics2D g)
    {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int w = getWidth();
        final int h = getHeight();

        final int cx = w/2;
        final int cy = h/2;
        final int len = Math.min(w, h)-1; // sidelen
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(cx, cy-(len/2), cx, cy+(len/2));
        g.drawLine(cx-(len/2), cy, cx+(len/2), cy);
    }

    protected void paintDynamicLayer(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int w = getWidth();
        final int h = getHeight();

        final int cx = w/2;
        final int cy = h/2;
        final int len = Math.min(w, h)-1; // sidelen
        
        updateGraph();
        g.setColor(getForeground());

        g = (Graphics2D) g.create(cx-len/2, cy-len/2, len, len);
        g.translate(-(cx-len/2),-(cy-len/2));
        ((Graphics2D)g).draw(gp);
        g.dispose();
    }
    
    private float x(double x, double len, int peaks)
    {
        x = (x+0.5)*len;

        final double divMin = 1;
        final double divMax = (peaks*2d-1);
        final double div = (divMax-divMin)*(vwrap)+divMin;
        x = x/div;
        
        return (float) x;
    }
    
    private float fww(double n)
    {
        n = (n*2-1)*Math.PI/2d;
        return (float) Math.sin(n);
    }
    
    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }

    public void setWaveWrap( double v )
    {
        double oldValue = this.vwrap;
        double newValue = bounded(v);
        if (oldValue != newValue)
        {
            this.vwrap = newValue;
            flagForceUpdate = true;
            setModified(true);
            repaint();
        }
    }
    
    private void updateGraph()
    {
        final int w = getWidth();
        final int h = getHeight();

        if ((w==gw)&&(h==gh)&&(!flagForceUpdate)) return;
        
        gw = w;
        gh = h;
        gp.reset();
        flagForceUpdate = false;
        
        final int cx = w/2;
        final int cy = h/2;
        final int len = Math.min(w, h)-1; // sidelen

        gp.reset();
        final int peaks = 9;// 4+5
        {
            int n = -peaks;
            gp.moveTo(x(n, len, peaks), fww(n));
            for (;n<peaks;n++)
            {
                gp.lineTo(x(n,len,peaks), fww(n));
            }
        }

        AffineTransform at = new AffineTransform();
        at.translate(cx, cy);
        at.scale(1, len/2);
        gp.transform(at);
    }

    private JTControlAdapter waveWrapAdapter;
    
    public JTControlAdapter getWaveWrapAdapter()
    {
        return waveWrapAdapter;
    }

    @BindParameter(name="wavewrap")
    public void setWaveWrapAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.waveWrapAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.waveWrapAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateWaveWrap();
        }
    }

    protected void updateWaveWrap()
    {
        if (waveWrapAdapter != null)
            setWaveWrap(waveWrapAdapter.getNormalizedValue());
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == waveWrapAdapter)
        {
            updateWaveWrap();
            return;
        }
    }
    
}
