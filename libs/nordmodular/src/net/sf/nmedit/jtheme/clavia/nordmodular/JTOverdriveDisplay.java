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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.Overdrive;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class JTOverdriveDisplay extends JTDisplay implements ChangeListener
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5554991097404064863L;

	
    private Overdrive overdrive;
    
    private JTControlAdapter overdriveAdapter;

    public JTOverdriveDisplay(JTContext context)
    {
        super(context);
        
        overdrive = new Overdrive();
    }
    
    protected void paintComponent(Graphics g)
    {
        if (overdrive.isModified())
        {
            overdrive.setModified(false);
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
        g.setColor(outline);
        int y = insets.top + h/2;
        g.drawLine(insets.left, y, w-1, y);

        g.translate(insets.left, insets.top);
        g.setColor(getForeground());
        
        overdrive.setBounds(insets.left, insets.top,w, h-1);
        g.draw(overdrive);
        g.translate(-insets.left, -insets.top); // undo translation
    }
    
    public float getOverdrive()
    {
        return overdrive.getOverdrive();
    }
    
    public void setOverdrive(float value)
    {
        if (getOverdrive() != value)
        {
            overdrive.setOverdrive(value);
            repaint();
        }
    }    

    public JTControlAdapter getOverdriveAdapter()
    {
        return overdriveAdapter;
    }


    @BindParameter(name="overdrive")
    public void setOverdriveAdapter(JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = this.overdriveAdapter;
        
        if (oldAdapter != adapter)
        {
            if (oldAdapter != null)
                oldAdapter.setChangeListener(null);
            this.overdriveAdapter = adapter;
            if (adapter != null)
                adapter.setChangeListener(this);
            
            updateOverdrive();
        }
    }

    

    protected void updateOverdrive()
    {
        if (overdriveAdapter != null)
            setOverdrive((float)overdriveAdapter.getNormalizedValue());
    }

   
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == overdriveAdapter)
        {
            updateOverdrive();
            return;
        }
     
    }
}
