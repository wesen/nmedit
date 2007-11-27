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

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class NoteVelScaleDisplay extends JTDisplay implements ChangeListener
{ 

    /**
     * 
     */
    private static final long serialVersionUID = -8628598647083829107L;

    public NoteVelScaleDisplay( JTContext context )
    {
        super( context );
    }

    private double vlGain = 0.5; // 0 - 1, 0.5=center
    private double vrGain = 0.5; // 0 - 1, 0.5=center
    private double vbreakPoint = 0.5; // 0 - 1

    private JTControlAdapter leftGainAdapter ;
    private JTControlAdapter rightGainAdapter ;
    private JTControlAdapter breakPointAdapter ;
    
    public JTControlAdapter getLeftGainAdapter()
    {
        return leftGainAdapter;
    }
    
    public JTControlAdapter getRightGainAdapter()
    {
        return rightGainAdapter;
    }
    
    public JTControlAdapter getBreakPointAdapter()
    {
        return breakPointAdapter;
    }

    @BindParameter(name="left-gain")
    public void setLeftGainAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.leftGainAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.leftGainAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateLeftGain();
        }
    }

    @BindParameter(name="right-gain")
    public void setRightGainAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.rightGainAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.rightGainAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateRightGain();
        }
    }

    @BindParameter(name="break-point")
    public void setBreakPointAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.breakPointAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.breakPointAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateBreakPoint();
        }
    }

    public void paintDynamicLayer(Graphics2D g)
    {
        final int w = getWidth();
        final int h = getHeight();

        final int cy = h/2;
        final int len = (int) Math.sqrt(w*w+h*h); // diagonal:rect(w,h)
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, cy, w-1, cy); // horizontal line
        
        g.setColor(getForeground());
        
        // origin 
        final int ox = (int) Math.round(vbreakPoint*(w-1));
        final double flg = ((1-vlGain)*2-1)*24/25;
        final double frg = (vrGain*2-1)*24/25;

        g.drawLine(ox, cy, ox+len, (int) (cy+flg*len) );
        g.drawLine(ox, cy, ox-len, (int) (cy-frg*len) );
    }
    
    private double bounded(double v)
    {
        return Math.max(0, Math.min(v, 1.0d));
    }

    public void setBreakPoint( double v )
    {
        this.vbreakPoint = bounded(v);
        repaint();
    }

    public void setLeftGain( double v )
    {
        this.vlGain = bounded(v);
        repaint();
    }

    public void setRightGain( double v )
    {
        this.vrGain = bounded(v);
        repaint();
    }

    protected void updateLeftGain()
    {
        if (leftGainAdapter != null)
            setLeftGain(leftGainAdapter.getNormalizedValue());
    }
    
    protected void updateRightGain()
    {
        if (rightGainAdapter != null)
            setRightGain(rightGainAdapter.getNormalizedValue());
    }
    
    protected void updateBreakPoint()
    {
        if (breakPointAdapter != null)
            setBreakPoint(breakPointAdapter.getNormalizedValue());
    }


    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == leftGainAdapter)
        {
            updateLeftGain();
            return;
        }
        if (e.getSource() == rightGainAdapter)
        {
            updateRightGain();
            return;
        }
        if (e.getSource() == breakPointAdapter)
        {
            updateBreakPoint();
            return;
        }
    }
}
