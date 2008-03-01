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
import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.Compressor;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class JTCompressorDisplay extends JTDisplay implements ChangeListener
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3622081455931068485L;

    private Compressor compressor;
    
    private JTControlAdapter ratioAdapter;
    private JTControlAdapter refLevelAdapter;
    private JTControlAdapter limiterAdapter;
    private JTControlAdapter thresholdAdapter;
    
    public JTCompressorDisplay(JTContext context)
    {
        super(context);
        
        compressor = new Compressor();
    }
    
    protected void paintComponent(Graphics g)
    {
        if (compressor.isModified())
        {
            compressor.setModified(false);
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }

    protected void paintDynamicLayer(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(JTNM1Context.GRAPH_DISPLAY_LINE);
        
        g.drawLine(0, getHeight(), getWidth(), 0);                   
        g.drawLine(-1, (int)((1-getRefLevel())*getHeight()), getWidth()+1, (int)((1-getRefLevel())*getHeight()));
        g.drawLine((int)((getRefLevel())*getWidth()), -1, (int)((getRefLevel())*getWidth()), getHeight()+1);
        g.setColor(getForeground());
        compressor.setBounds(0, 0, getWidth(), getHeight());
        g.draw(compressor);
    }
    
    public float getThreshold()
    {
        return compressor.getThreshold();
    }
    
    public float getRatio()
    {
        return compressor.getRatio();
    }
    
    public float getRefLevel()
    {
        return compressor.getRefLevel();
    }
  
    public int getLimiter()
    {
        return compressor.getLimiter();
    }
    
    public void setThreshold(float value)
    {
        if (getThreshold() != value)
        {
            compressor.setThreshold(value);
            repaint();
        }
    }
    
    public void setRatio(int value)
    {
        if (getRatio() != value)
        {
            compressor.setRatio(value);
            repaint();
        }
    }
    
    public void setLimiter(int value)
    {
        if (getLimiter() != value)
        {
            compressor.setLimiter(value);
            repaint();
        }
    }

    public void setRefLevel(float value)
    {
        if (getRefLevel() != value)
        {
            compressor.setRefLevel(value);
            repaint();
        }
    }
    
    public JTControlAdapter getRatioAdapter()
    {
        return ratioAdapter;
    }

    public JTControlAdapter getLimiterAdapter() {
		return limiterAdapter;
	}

	public JTControlAdapter getRefLevelAdapter() {
		return refLevelAdapter;
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

    @BindParameter(name="ref-level")
    public void setRefLevelAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.refLevelAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.refLevelAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateRefLevel();
        }
    }

    @BindParameter(name="limiter")
    public void setLimiterAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.limiterAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.limiterAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateLimiter();
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
    
    protected void updateLimiter()
    {
        if (limiterAdapter != null)
            setLimiter((int)limiterAdapter.getValue());
    }
    
    protected void updateRefLevel()
    {
        if (refLevelAdapter != null) 
        	setRefLevel((float)refLevelAdapter.getNormalizedValue());                     
    }
    
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == ratioAdapter)
        {
            updateRatio();
            return;
        }
      
        if (e.getSource() == limiterAdapter)
        {
            updateLimiter();
            return;
        }
        if (e.getSource() == refLevelAdapter)
        {
            updateRefLevel();
            return;
        }
        if (e.getSource() == thresholdAdapter)
        {
            updateThreshold();
            return;
        }
    }
}