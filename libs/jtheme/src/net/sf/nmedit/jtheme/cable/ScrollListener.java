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
package net.sf.nmedit.jtheme.cable;

import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class ScrollListener implements PropertyChangeListener, AdjustmentListener
{

    private JScrollPane scrollPane;
    private JTCableManager cableManager;
    private boolean installed;

    public ScrollListener(JScrollPane scrollPane, JTCableManager cableManager)
    {
        this.scrollPane = scrollPane;
        this.cableManager = cableManager;
        this.installed = false;
        install();
    }
    
    public void install()
    {
        if (installed)
            return;
        
        installed = true;
        scrollPane.addPropertyChangeListener(this);

        updateScrollbar(true, null, scrollPane.getHorizontalScrollBar());
        updateScrollbar(false, null, scrollPane.getVerticalScrollBar());
    }
    
    public void uninstall()
    {
        if (!installed)
            return;

        updateScrollbar(true, scrollPane.getHorizontalScrollBar(), null);
        updateScrollbar(false, scrollPane.getVerticalScrollBar(), null);
        installed = false;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getSource() == scrollPane)
        {
            if ("horizontalScrollBar".equals(evt.getPropertyName()))
            {
                updateScrollbar(true, evt);
            }
            if ("verticalScrollBar".equals(evt.getPropertyName()))
            {
                updateScrollbar(false, evt);   
            }
            // property 'model' ???
        }
    }

    private void updateScrollbar(boolean horizontalScrollbar, PropertyChangeEvent evt)
    {
        updateScrollbar(horizontalScrollbar, (JScrollBar) evt.getOldValue(), (JScrollBar) evt.getNewValue());
    }

    private void updateScrollbar(boolean horizontalScrollbar, JScrollBar oldBar, JScrollBar newBar)
    {
        if (oldBar != null) uninstall(oldBar);
        if (newBar != null) install(newBar);
    }

    private void install(JScrollBar bar)
    {
        bar.addAdjustmentListener(this);
    }
    
    private void uninstall(JScrollBar bar)
    {
        bar.removeAdjustmentListener(this);
    }

    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        updateVisibleRegion();
    }

    private transient Rectangle cachedRectangle;
    
    private Rectangle getCachedRectangle()
    {
        if (cachedRectangle == null)
            cachedRectangle = new Rectangle();
        return cachedRectangle;
    }
    
    public void updateVisibleRegion()
    {
        JComponent view = ((JComponent)scrollPane.getViewport().getView()); 
        if (view == null)
            return;
        
        Rectangle visibleRect = getCachedRectangle();
        view.computeVisibleRect(visibleRect);
        //cableManager.setVisibleRegion(visibleRect);
    //    cableManager.notifyRepaintManager();
    }
    
}

