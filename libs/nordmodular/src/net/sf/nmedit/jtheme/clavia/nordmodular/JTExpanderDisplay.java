/* Copyright (C) 2007 Julien Pauty
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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;

import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.Expander;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class JTExpanderDisplay extends JTDisplay implements ChangeListener
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3622081455931068485L;

    private Expander expander;
    
    private JTControlAdapter ratioAdapter;
    private JTControlAdapter gateAdapter;
    private JTControlAdapter thresholdAdapter;
    
    public JTExpanderDisplay(JTContext context)
    {
        super(context);
        
        expander = new Expander();
    }
    
    protected void paintComponent(Graphics g)
    {
        if (expander.isModified())
        {
            expander.setModified(false);
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }

    protected void paintDynamicLayer(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(JTNM1Context.GRAPH_DISPLAY_LINE);
        
        g.drawLine(0, getHeight(), getWidth(), 0);                   
        g.setColor(getForeground());
        expander.setBounds(0, 0, getWidth(), getHeight());
        g.draw(expander);
    }
    
    public float getThreshold()
    {
        return expander.getThreshold();
    }
    
    public float getRatio()
    {
        return expander.getRatio();
    }
    
    public float getGate()
    {
    	return expander.getGate();
    }
    
    public void setThreshold(float value)
    {
        if (getThreshold() != value)
        {
            expander.setThreshold(value);
            repaint();
        }
    }
    
    public void setRatio(int value)
    {
        if (getRatio() != value)
        {
            expander.setRatio(value);
            repaint();
        }
    }


    
    public void setGate(float value)
    {
        if (getGate() != value)
        {
            expander.setGate(value);
            repaint();
        }
    } 
    
    public JTControlAdapter getRatioAdapter()
    {
        return ratioAdapter;
    }

    public JTControlAdapter getLimiterAdapter() {
		return gateAdapter;
	}


	public JTControlAdapter getThresholdAdapter() {
		return thresholdAdapter;
	}

    @BindParameter(name="ratio")
    public void setRatioAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.ratioAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.ratioAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateRatio();
        }
    }

    @BindParameter(name="threshold")
    public void setThresholdAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.thresholdAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.thresholdAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateThreshold();
        }
    }

    @BindParameter(name="gate")
    public void setGateAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.gateAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.gateAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateGate();
        }
    }

    protected void updateRatio()
    {
        if (ratioAdapter != null)        	
            setRatio(ratioAdapter.getValue());
    }

    protected void updateThreshold()
    {
        if (thresholdAdapter != null)
            setThreshold((float)thresholdAdapter.getNormalizedValue());
    }
    
    protected void updateGate()
    {
        if (gateAdapter != null)        	
            setGate((float)gateAdapter.getNormalizedValue());
    }
    
 
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == ratioAdapter)
        {
            updateRatio();
            return;
        }
      
        if (e.getSource() == gateAdapter)
        {
            updateGate();
            return;
        }
     
        if (e.getSource() == thresholdAdapter)
        {
            updateThreshold();
            return;
        }
    }
}