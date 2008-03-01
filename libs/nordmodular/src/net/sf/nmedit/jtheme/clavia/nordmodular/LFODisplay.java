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
 * Created on Jul 24, 2006
 */
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;


public class LFODisplay extends JTDisplay implements ChangeListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 4431079405680653586L;

    public LFODisplay( JTContext context )
    {
        super( context );
    }

    public final static int WF_SINE = 0;
    public final static int WF_TRI = 1;
    public final static int WF_SAW = 2;
    public final static int WF_INV_SAW = 3;
    public final static int WF_SQUARE = 4;
    
    private int vwf = WF_SAW;
    private double vphase = 0.5;
    private GeneralPath gp = new GeneralPath();
    private boolean rebuildpath = true;
    private int w = 0;
    private int h = 0;
    private int ww = 0;
    private int hh = 0;

    private JTControlAdapter phaseAdapter;
    private JTControlAdapter waveAdapter;

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
    
    public JTControlAdapter getPhaseAdapter()
    {
        return phaseAdapter;
    }
    
    public JTControlAdapter getWaveAdapter()
    {
        return waveAdapter;
    }
    
    @BindParameter(name="phase")
    public void setPhaseAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.phaseAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.phaseAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updatePhase();
        }
    }

    @BindParameter(name="shape")
    public void setWaveAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.waveAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.waveAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateWave();
        }
    }
    
    private void ensurePathBuilt()
    {
        if (rebuildpath || getWidth()!=w || getHeight()!=h)
        {
            gp.reset();
            w = getWidth();
            h = getHeight();
            rebuildpath = false;

            switch (vwf)
            {
                case WF_SINE: 
                    generateSine(gp);
                    break;
                case WF_TRI: 
                    generateTri(gp);
                    break;
                case WF_SAW: 
                    generateSaw(gp);
                    break;
                case WF_INV_SAW: 
                    generateInvSaw(gp); 
                    break;
                case WF_SQUARE: 
                    generateSquare(gp); 
                    break;
            }

            Insets insets = getInsets();
            
            this.ww = w-insets.left-insets.right;
            this.hh = h-insets.top-insets.bottom;
            
            AffineTransform at = new AffineTransform();
            at.translate(0, 1+(hh-1)/2d);
            at.scale(ww-1, (hh-1)/2d);
            at.scale(1, -1);
            gp.transform(at);
        }
    }
    
    public void paintDynamicLayer(Graphics2D g)
    {   
    	ensurePathBuilt();
      
        Graphics2D g2 = (Graphics2D) g.create();

        try
        {
	        g2.setColor(getForeground());
	
	        double tx = (((1-vphase)-0.5)*(ww-1));
	        double ty = 0;
	        
	        g2.translate(tx, ty);
	        g2.draw(gp);
	        g2.translate(-ww, 0);
	        g2.draw(gp);
	        g2.translate(2*ww, 0);
	        g2.draw(gp);
        }
        finally
        {
        	g2.dispose();
        }
    }
    
    // path bounds:
    //     +1
    // 0 ------- 1
    //     -1
    
    private void generateSine(GeneralPath gp)
    {
        // peaks
        final float cx = 0.5f;
        final float var = 1/8f;
        final float h = 1.32f;
        
        gp.moveTo(0, 0);   
        gp.curveTo(
                 0+var, h,
                cx-var, h,
                cx, 0);
        gp.curveTo(
                cx+var, -h,
                 1-var, -h,
                 1, 0);
    }
    
    private void generateTri(GeneralPath gp)
    {
        gp.moveTo(0, 0);
        gp.lineTo(1/4f, 1);
        gp.lineTo(2/4f, 0);
        gp.lineTo(3/4f,-1);
        gp.lineTo(1, 0);
    }
    
    private void generateSaw(GeneralPath gp)
    {
        gp.moveTo(0, 0);
        gp.lineTo(1/2f, 1);
        gp.lineTo(1/2f,-1);
        gp.lineTo(1, 0);
    }
    
    private void generateInvSaw(GeneralPath gp)
    {
        gp.moveTo(0, -1);
        gp.lineTo(0, 1);
        gp.lineTo(1, -1);
    }
    
    private void generateSquare(GeneralPath gp)
    {
        gp.moveTo(0,-1);
        gp.lineTo(0, 1);
        gp.lineTo(1/2f, 1);
        gp.lineTo(1/2f, -1);
        gp.lineTo(1, -1);
    }

    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }

    @BindParameter(name="waveform") // TODO name=shape
    public void setWaveForm( int v )
    {
        if (this.vwf!=v)
        {
    
            switch (v)
            {
                case WF_SINE:
                case WF_TRI:
                case WF_SAW:
                case WF_INV_SAW:
                case WF_SQUARE: break;
                default: throw new IllegalArgumentException("Unknown waveform id:"+v);
            }
            
            this.vwf = v;
            rebuildpath = true;
            setModified(true);
            repaint();
        }
    }

    public void setPhase( double v )
    {
        double oldValue = this.vphase;
        v = bounded(v);
        if (oldValue != v)
        {
            this.vphase = v;
            setModified(true);
            repaint();
        }
    }

    public int getWaveForm()
    {
        return vwf;
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == waveAdapter)
        {
            updateWave();
            return;
        }
        if (e.getSource() == phaseAdapter)
        {
            updatePhase();
            return;
        }
    }
    
    protected void updateWave()
    {
        if (waveAdapter != null)
            setWaveForm(waveAdapter.getValue()-waveAdapter.getMinValue());
    }
    
    protected void updatePhase()
    {
        if (phaseAdapter != null)
            setPhase(phaseAdapter.getNormalizedValue());
    }

}
