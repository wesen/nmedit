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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpatch.event.PParameterListener;
import net.sf.nmedit.jpatch.PParameter;

public class JTParameterControlAdapter implements JTControlAdapter, PParameterListener
{
    
    private ChangeListener changeListener;
    private PParameter parameter;
    private JTComponent component;

    public JTParameterControlAdapter(PParameter parameter)
    {
        this.parameter = parameter;
        install();
    }
    
    private void install()
    {
        parameter.addParameterListener(this);   
    }
    
    public void uninstall()
    {
        parameter.removeParameterListener(this);
    }

    public void parameterValueChanged(PParameterEvent e)
    {
        notifyChangeListener();
    }
    
    protected void notifyChangeListener()
    {
        if (changeListener != null)
        {
            changeListener.stateChanged(new ChangeEvent(this));
        }
    }

    public ChangeListener getChangeListener()
    {
        return changeListener;
    }

    public void setChangeListener(ChangeListener l)
    {
        this.changeListener = l;
    }

    public int getDefaultValue()
    {
        return parameter.getDefaultValue();
    }

    public int getMaxValue()
    {
        return parameter.getMaxValue();
    }

    public int getMinValue()
    {
        return parameter.getMinValue();
    }

    public double getNormalizedValue()
    {
        // TODO Auto-generated method stub
        return parameter.getDoubleValue();
    }

    public int getValue()
    {
        return parameter.getValue();
    }

    public void setDefaultValue(int defaultValue)
    { }

    public void setMaxValue(int maxValue)
    { }

    public void setMinValue(int minValue)
    { }

    public void setNormalizedValue(double value)
    { 
        parameter.setDoubleValue(value);
        notifyChangeListener();
    }

    public void setValue(int value)
    { 
        parameter.setValue(value);
        notifyChangeListener();
    }

    public PParameter getParameter()
    {
        return parameter;
    }

    public void focusRequested(PParameterEvent e)
    {
        if (component != null)
            component.requestFocus();
    }

    public JTComponent getComponent()
    {
        return this.component;
    }

    public void setComponent(JTComponent c)
    {
        this.component = c;
    }
}

