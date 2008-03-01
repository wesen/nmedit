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
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.EqualizerMid;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class JTEqMidDisplay extends JTDisplay implements ChangeListener
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7924250746781242031L;

    private EqualizerMid equalizerMid;
    
    private JTControlAdapter freqAdapter;
    private JTControlAdapter gainAdapter;
    private JTControlAdapter bwAdapter;

    public JTEqMidDisplay(JTContext context)
    {
        super(context);
        
        equalizerMid = new EqualizerMid();
    }
    
    protected void paintComponent(Graphics g)
    {
        if (equalizerMid.isModified())
        {
            equalizerMid.setModified(false);
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }

    // do not use field directly, instead use getGraphDisplayLineColor()
    private transient Color cachedShapeOutlineColor;

    protected Color getShapeOutlineColor()
    {
        if (cachedShapeOutlineColor == null)
        {
            cachedShapeOutlineColor =
                getContext().getUIDefaults().getColor(JTNM1Context.DISPLAY_SHAPE_OUTLINE_COLOR_KEY);
            if (cachedShapeOutlineColor == null)
                cachedShapeOutlineColor = getForeground();
        }
        return cachedShapeOutlineColor;
    }
    
    protected void paintDynamicLayer(Graphics2D g)
    {        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Insets insets = getInsets();
        int w = getWidth()-insets.left-insets.right;
        int h = getHeight()-insets.top-insets.bottom;
        Color outline = getShapeOutlineColor();
        if (outline != null)
        {
            g.setColor(outline);
            int y = insets.top + h/2;
            g.drawLine(insets.left, y, w-1, y);
        }
        g.translate(insets.left, insets.top);
        g.setColor(getForeground());
        equalizerMid.setBounds(insets.left, insets.top, w, h);
        g.draw(equalizerMid);
        g.translate(-insets.left,-insets.top); // undo translation
    }
    
    public float getFreq()
    {
        return equalizerMid.getFreq();
    }
    
    public void setFreq(float value)
    {
        if (getFreq() != value)
        {
            equalizerMid.setFreq(value);
            repaint();
        }
    }
    
    public float getGain()
    {
        return equalizerMid.getGain();
    }
    
    public void setGain(float value)
    {
        if (getGain() != value)
        {
            equalizerMid.setGain(value);
            repaint();
        }
    }
    
    public float getBandWidth()
    {
        return equalizerMid.getBW();
    }
    
    public void setBandWidth(float value)
    {
        if (getGain() != value)
        {
            equalizerMid.setBW(value);
            repaint();
        }
    }

    public JTControlAdapter getFreqAdapter()
    {
        return freqAdapter;
    }

    public JTControlAdapter getGainAdapter()
    {
        return gainAdapter;
    }

    public JTControlAdapter getBWAdapter()
    {
        return bwAdapter;
    }

    @BindParameter(name="frequency")
    public void setFreqAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.freqAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.freqAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateFreq();
        }
    }

    @BindParameter(name="gain")
    public void setGainAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.gainAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.gainAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateGain();
        }
    }

    @BindParameter(name="bandwidth")
    public void setBWAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.bwAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.bwAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateBW();
        }
    }

    protected void updateFreq()
    {
        if (freqAdapter != null)
            setFreq((float)freqAdapter.getNormalizedValue());
    }

    protected void updateGain()
    {
        if (gainAdapter != null)
            setGain((float)gainAdapter.getNormalizedValue());
    }

    protected void updateBW()
    {
        if (bwAdapter != null)
            setBandWidth((float)bwAdapter.getNormalizedValue());
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == freqAdapter)
        {
            updateFreq();
            return;
        }
        if (e.getSource() == gainAdapter)
        {
            updateGain();
            return;
        }
        if (e.getSource() == bwAdapter)
        {
            updateBW();
            return;
        }
    }
}
