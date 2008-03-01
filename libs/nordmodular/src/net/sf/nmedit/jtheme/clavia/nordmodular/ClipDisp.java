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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

/*
 * Created on Jul 24, 2006
 */

public class ClipDisp extends JTDisplay implements ChangeListener 
{

    /**
     * 
     */
    private static final long serialVersionUID = 5156590739842216466L;
    private JTControlAdapter clipParam; 
    private JTControlAdapter symParam;

    private double vclip = 1; // 0 - 1
    private boolean vsym = false; //|log
    private boolean modified = true;

    public ClipDisp(JTContext context)
    {
        super(context);
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
    
    protected void setModified(boolean modified)
    {
        this.modified = true;
    }

    public void paintDynamicLayer(Graphics2D g)
    {
        final int w = getWidth();
        final int h = getHeight();

        final int cx = w/2;
        final int cy = h/2;
        final int len = Math.min(w, h)-1; // sidelen
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(cx, cy-(len/2), cx, cy+(len/2));
        g.drawLine(cx-(len/2), cy, cx+(len/2), cy);
        
        g.setColor(getForeground());
        
        double delta = (len*(1-vclip))/2.0d;
        int dx = (int) Math.ceil(delta);
        int dy = dx;

        g.drawLine(cx, cy, cx+dx, cy-dy);
        g.drawLine(cx+dx, cy-dy, cx+(len/2), cy-dy);
        
        if (vsym)
        {
            g.drawLine(cx, cy, cx-dx, cy+dy);
            g.drawLine(cx-dx, cy+dy, cx-(len/2), cy+dy);
        }
        else
        {
            g.drawLine(cx, cy, cx-(len/2), cy+(len/2));
        }
    }
    
    public void setClip(float v)
    {
        float newValue = bounded(v); 
        if (newValue != vclip)
        {
            this.vclip = newValue;
            setModified(true);
            repaint();
        }
    }
    
    public void setSymmetric(boolean v)
    {
        boolean newValue = v;
        if (vsym != newValue)
        {
            this.vsym = v;
            setModified(true);
            repaint();
        }
    }

    protected float bounded(float v)
    {
        return Math.max(0, Math.min(v, 1));
    }

    @BindParameter(name="clip")
    public void setCliParameterAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = clipParam;
        this.clipParam = adapter;
        
        if (oldAdapter != null)
            oldAdapter.setChangeListener(null);
        if (adapter != null)
            adapter.setChangeListener(this);
    }

    @BindParameter(name="symmetry")
    public void setSymmetricParameterAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = symParam;
        this.symParam = adapter;
        
        if (oldAdapter != null)
            oldAdapter.setChangeListener(null);
        if (adapter != null)
            adapter.setChangeListener(this);
    }

    private void updateValues()
    {
        setClip((float)(clipParam != null ? 1f-clipParam.getNormalizedValue() : 0));
        setSymmetric(symParam != null ? symParam.getValue() == symParam.getMaxValue() : true);
    }

    public void stateChanged(ChangeEvent e)
    {
        updateValues();
    } 

}
