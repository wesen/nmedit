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

import java.awt.Graphics;

import net.sf.nmedit.jpatch.PLight;
import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.event.PLightEvent;
import net.sf.nmedit.jpatch.event.PLightListener;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.util.RetargetMouseEventSupport;

public class JTLight extends JTComponent
implements PLightListener
{

    private static final long serialVersionUID = -3313838376842711969L;

    public static final String uiClassId = "LightUI";
    
    public static final int LED = 1;
    public static final int LED_ARRAY = 2;
    public static final int METER = 3;

    private PLight light;
    private PLightDescriptor lightDescriptor;
    private int type = LED;
    private int onValue = Integer.MIN_VALUE;
    
    public JTLight(JTContext context)
    {
        super(context);
        setOpaque(false);
        
        RetargetMouseEventSupport rmes = RetargetMouseEventSupport.retargetToParent(this);
        rmes.install(this);
    }

    protected void renderBorder(Graphics g)
    {
        if (type == METER)
            super.renderBorder(g);
    }

    public void setLEDOnValue(int onValue)
    {
        this.onValue = onValue;
    }
    
    public boolean isLEDOn()
    {
        if (onValue != Integer.MIN_VALUE)
            return onValue == getValue();
        else
            return getValue()!=getDefaultValue(); 
    }

    public String getUIClassID()
    {
        return uiClassId;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        if (this.type != type)
        {
            this.type = type;
            repaint();
        }
    }
    
    public int getMinValue()
    {
        return lightDescriptor == null ? 0
                : lightDescriptor.getMinValue();
    }

    public int getMaxValue()
    {
        return lightDescriptor == null ? 1
                : lightDescriptor.getMaxValue();
    }

    public int getDefaultValue()
    {
        return lightDescriptor == null ? 0
                : lightDescriptor.getDefaultValue();
    }
    
    public int getValue()
    {
        if (light != null)
            return light.getValue();
        else return getDefaultValue();
    }

    public void setLight(PLightDescriptor d)
    {
        if (this.lightDescriptor != d)
        {
            this.lightDescriptor = d;
            repaint();
        }
    }

    public void setLight(PLight light)
    {
        PLight oldLight = this.light;
        if (oldLight != light)
        {
            if (oldLight != null)
                uninstall(oldLight);
            this.light = light;
            if (light != null)
            {
                install(light);
                setLight(light.getDescriptor());
            }
            repaint();
        }
    }

    private void install(PLight light)
    {
        light.addLightListener(this);
    }

    private void uninstall(PLight light)
    {
        light.removeLightListener(this);
    }

    public void lightChanged(PLightEvent e)
    {
        repaint();
    }

}
