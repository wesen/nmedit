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
 */package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.FilterE;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class JTFilterEDisplay extends JTDisplay implements ChangeListener
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4230718814779546880L;

    private FilterE filterE;
    
    private JTControlAdapter cutoffAdapter;
    private JTControlAdapter resonanceAdapter;
    private JTControlAdapter typeAdapter;
    private JTControlAdapter slopeAdapter;
    private JTControlAdapter gainControlAdapter;

    public JTFilterEDisplay(JTContext context)
    {
        super(context);
        
        filterE = new FilterE();        
    }

    protected void paintComponent(Graphics g)
    {
        if (filterE.isModified())
        {
            filterE.setModified(false);
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }

    protected void paintDynamicLayer(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Insets insets = getInsets();
        int w = getWidth()-insets.left-insets.right;
        int h = getHeight()-insets.top-insets.bottom;
        
        g.setColor(JTNM1Context.GRAPH_DISPLAY_LINE);
        int y = (int)(insets.top+h*.45);
        g.drawLine(insets.left, y, w-1, y);

        g.setColor(getForeground());
        filterE.setBounds(0, 0, w, h);
        g.translate(insets.left, insets.top);
        g.setColor(JTNM1Context.GRAPH_DISPLAY_FILL);
        g.fill(filterE);
        g.setColor(JTNM1Context.GRAPH_DISPLAY_FILL_LINE);
        g.draw(filterE);
        g.translate(-insets.left, -insets.top); // undo translation
    }
    
    public float getCutoff()
    {
        return filterE.getCutOff();
    }
    
    public float getResonance()
    {
        return filterE.getResonance();
    }
    
    public int getType()
    {
        return filterE.getType();
    }
    
    public int getSlope()
    {
        return filterE.getSlope();
    }
    
    public int getGainControl(){
    	return filterE.getGainControl();
    }
    
    
    public void setCutoff(float value)
    {
        if (getCutoff() != value)
        {
            filterE.setCutOff(value);
            repaint();
        }
    }
    
    public void setResonance(float value)
    {
        if (getResonance() != value)
        {
            filterE.setResonance(value);
            repaint();
        }
    }
    
    public void setType(int value)
    {
        if (getType() != value)
        {
            filterE.setType(value);
            repaint();
        }
    }
    
    public void setSlope(int value)
    {
        if (getSlope() != value)
        {
            filterE.setSlope(value);
            repaint();
        }
    }
    
    public void setGainControl(int value){
    	if (getGainControl() != value)
    	{
    		filterE.setGainControl(value);
    		repaint();
    	}
    }
    
    public JTControlAdapter getCutoffAdapter()
    {
        return cutoffAdapter;
    }

    public JTControlAdapter getResonanceAdapter()
    {
        return resonanceAdapter;
    }

    public JTControlAdapter getTypeAdapter()
    {
        return typeAdapter;
    }
    
    public JTControlAdapter getSlopeAdapter()
    {
        return slopeAdapter;
    }

    @BindParameter(name="cutoff")
    public void setCutoffAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.cutoffAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.cutoffAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateCutoff();
        }
    }

    @BindParameter(name="resonance")
    public void setResonanceAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.resonanceAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.resonanceAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateResonance();
        }
    }

    @BindParameter(name="type")
    public void setTypeAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.typeAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.typeAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateType();
        }
    }

    @BindParameter(name="slope")
    public void setSlopeAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.slopeAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.slopeAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateSlope();
        }
    }

    @BindParameter(name="gain-control")
    public void setGainControlAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.gainControlAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.gainControlAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateGainControl();
        }
    }
    
    protected void updateCutoff()
    {
        if (cutoffAdapter != null)
            setCutoff((float)cutoffAdapter.getNormalizedValue());
    }

    protected void updateResonance()
    {
        if (resonanceAdapter != null)
            setResonance((float)resonanceAdapter.getNormalizedValue());
    }

    protected void updateType()
    {
        if (typeAdapter != null)
            setType(typeAdapter.getValue());
    }
    
    protected void updateSlope()
    {
    	if (slopeAdapter != null){    		
    		setSlope(slopeAdapter.getValue());
    	}
    }

    protected void updateGainControl()
    {
    	if (gainControlAdapter!= null){    		
    		setGainControl(gainControlAdapter.getValue());
    	}
    }
    
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == cutoffAdapter)
        {
            updateCutoff();
            return;
        }
        if (e.getSource() == resonanceAdapter)
        {
            updateResonance();
            return;
        }
        if (e.getSource() == typeAdapter)
        {
            updateType();
            return;
        }
        
        if (e.getSource() == slopeAdapter)
        {
            updateSlope();
            return;
        }
        
        if (e.getSource() == gainControlAdapter)
        {
            updateGainControl();
            return;
        }
    }
}
