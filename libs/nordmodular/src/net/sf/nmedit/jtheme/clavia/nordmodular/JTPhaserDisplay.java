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
import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.Phaser;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class JTPhaserDisplay extends JTDisplay implements ChangeListener
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3271589716317598895L;

    private Phaser phaser;
    
    private JTControlAdapter feedbackAdapter;
    private JTControlAdapter peakCountAdapter;
    private JTControlAdapter spreadAdapter;

    public JTPhaserDisplay(JTContext context)
    {
        super(context);
        
        phaser = new Phaser();
    }
    
    protected void paintComponent(Graphics g)
    {
        if (phaser.isModified())
        {
            phaser.setModified(false);
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }
    
    protected void paintDynamicLayer(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(JTNM1Context.GRAPH_DISPLAY_LINE);

        Insets insets = getInsets();
        int w = getWidth()-insets.left-insets.right;
        int h = getHeight()-insets.top-insets.bottom;
        
        int y = insets.top+h/2;
        g.drawLine(insets.left, y, w-1, y);

        g.translate(insets.left, insets.top);
        g.setColor(getForeground());
        phaser.setBounds(0, 0, w, h);        
        g.setColor(JTNM1Context.GRAPH_DISPLAY_FILL);      
    	g.fill(phaser);
    	g.setColor(JTNM1Context.GRAPH_DISPLAY_FILL_LINE);
        g.draw(phaser);
        g.translate(-insets.left, -insets.top); // undo translation
    }
    
    public int getFeedback()
    {
        return phaser.getFeedBack();
    }
    
    public float getPeakCount()
    {
        return phaser.getNb_peaks();
    }
    
    public int getSpread()
    {
        return phaser.getSpread();
    }
    
    public void setFeedback(int value)
    {
        if (getFeedback() != value)
        {
            phaser.setFeedBack(value);
            repaint();
        }
    }
    
    public void setPeakCount(int value)
    {
        if (getPeakCount() != value)
        {
            phaser.setNbPeaks(value);
            repaint();
        }
    }
    
    public void setSpread(int value)
    {
        if (getSpread() != value)
        {
            phaser.setSpread(value);
            repaint();
        }
    }
    
    public JTControlAdapter getFeedbackAdapter()
    {
        return feedbackAdapter;
    }

    public JTControlAdapter getPeakCountAdapter()
    {
        return peakCountAdapter;
    }

    public JTControlAdapter getSpreadAdapter()
    {
        return spreadAdapter;
    }

    @BindParameter(name="feedback")
    public void setFeedbackAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.feedbackAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.feedbackAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateFeedback();
        }
    }

    @BindParameter(name="peaks")
    public void setPeakCountAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.peakCountAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.peakCountAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updatePeakCount();
        }
    }

    @BindParameter(name="spread")
    public void setSpreadAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.spreadAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.spreadAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateSpreadAdapter();
        }
    }

    protected void updateFeedback()
    {
        if (feedbackAdapter != null)
            setFeedback((int)(feedbackAdapter.getNormalizedValue()*127f));
    }

    protected void updatePeakCount()
    {
        if (peakCountAdapter != null)
            setPeakCount(1+((int)(peakCountAdapter.getNormalizedValue()*5)));
    }

    protected void updateSpreadAdapter()
    {
        if (spreadAdapter != null)
            setSpread((int)(spreadAdapter.getNormalizedValue()*127f));
    }

    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == feedbackAdapter)
        {
            updateFeedback();
            return;
        }
        if (e.getSource() == peakCountAdapter)
        {
            updatePeakCount();
            return;
        }
        if (e.getSource() == spreadAdapter)
        {
            updateSpreadAdapter();
            return;
        }
    }
}
