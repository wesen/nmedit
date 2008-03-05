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
package net.sf.nmedit.jtheme.component;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.SwingConstants;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.misc.CallDescriptor;

public class JTButtonControl extends JTControl
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5006356339062640600L;

    public static final String uiClassID = "ButtonConrolUI";

    private Map<Integer, String> textMap;
    private Map<Integer, Icon> iconMap;
    private boolean cyclic = false;
    private boolean reversed = false;
    private int orientation = SwingConstants.VERTICAL;
    private int spacing = 2;
    private boolean toggleEnabledRequested = true;
    private boolean incrementMode = false;
    private CallDescriptor call;
    
    public JTButtonControl(JTContext context)
    {
        super(context);
    }

    public void setCall(CallDescriptor call)
    {
        this.call = call;
    }
    
    public CallDescriptor getCall()
    {
        return this.call;
    }

    public boolean isIncrementModeEnabled()
    {
        return this.incrementMode;
    }
    
    public void setIncrementModeEnabled(boolean enable)
    {
        this.incrementMode = enable;
    }
    
    public boolean isToggleEnabledRequested()
    {
        return toggleEnabledRequested;
    }
    
    public void setToggleEnabledRequested(boolean enable)
    {
        if (toggleEnabledRequested!=enable)
        {
            this.toggleEnabledRequested = enable;
            repaint();
        }
    }
    
    public String getUIClassID()
    {
        return uiClassID;
    }
    
    public int getSpacing()
    {
        return spacing;
    }
    
    public void setSpacing(int spacing)
    {
        this.spacing = spacing;
    }
    
    public boolean isCyclic()
    {
        return cyclic;
    }
    
    public void setCyclic(boolean cyclic)
    {
        this.cyclic = cyclic;
    }
    
    public boolean isReversed() {
    	return reversed;
    }
    
    public void setReversed(boolean reverse) {
    	this.reversed = reverse;
    }
    
    public void setOrientation(int orientation)
    {
        if (orientation != SwingConstants.HORIZONTAL
                && orientation != SwingConstants.VERTICAL)
            throw new IllegalArgumentException("invalid orientation: "+orientation);
        
        this.orientation = orientation;
    }
    
    public int getOrientation()
    {
        return isCyclic() ? SwingConstants.HORIZONTAL : orientation;
    }
    
    public String getSelectedText()
    {
        return getText(getValue());
    }
    
    public Icon getSelectedIcon()
    {
        return getIcon(getValue());
    }
    
    public void setText(int index, String text)
    {
        if (textMap == null)
        {
            if (text == null)
                return;
            textMap = new HashMap<Integer, String>();
        }
        
        if (text == null)
            textMap.remove(index);
        else
            textMap.put(index, text);
    }
    
    public void setIcon(int index, Icon icon)
    {
        if (iconMap == null)
        {
            if (icon == null)
                return;
            iconMap = new HashMap<Integer, Icon>();
        }
        
        if (icon == null)
            iconMap.remove(index);
        else
            iconMap.put(index, icon);
    }
    
    public String getText(int index)
    {
        return textMap == null ? null : textMap.get(index); 
    }
    
    public Icon getIcon(int index)
    {
        return iconMap == null ? null : iconMap.get(index);
    }

}

