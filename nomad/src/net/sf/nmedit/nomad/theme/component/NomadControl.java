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

/*
 * Created on Jan 6, 2006
 */
package net.sf.nmedit.nomad.theme.component;

import java.awt.Color;
import java.awt.event.FocusEvent;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Morph;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Parameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ParameterListener;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DParameter;
import net.sf.nmedit.nomad.util.NomadUtilities;



/**
 * @author Christian Schneider
 */
public abstract class NomadControl extends NomadComponent implements ParameterListener {
	private int value = 0;
	private int minValue = 0;
	private int maxValue = 100;
	private Integer defaultValue = null;
	private Color morphForeground = new Color(1, 0, 0, 0.5f);
	private Color morphBackground = new Color(1, 0.25f, 0.25f, 0.25f);
	private Double morphValue = null;

	private Parameter parameter = null;
	
    public final static String PAR0 = "parameter#0";
    
    protected void processFocusEvent(FocusEvent event)
    {
        if (event.getComponent() instanceof NomadControl)
        {
            event.getComponent().repaint();
        }
        
        super.processFocusEvent(event);
    }
	
	public NomadControl() {
		super();
        enableEvents(FocusEvent.FOCUS_EVENT_MASK);
	}

    public void setParameterInfo(DParameter parameterInfo) 
    {
        setParameterInfo(PAR0,parameterInfo);
    }
	
    public void setParameterInfo(String name, DParameter info)
    {
        super.setParameterInfo(name, info);
        if (info!=null && PAR0.equals(name)) 
        {
            setMinValue(info.getMinValue());
            setMaxValue(info.getMaxValue());
            setValue(info.getDefaultValue());
            setDefaultValue(info.getDefaultValue());
        }
    }
    
    public Parameter getParameter()
    {
        return parameter;
    }
    
    public DParameter getParameterSpec() 
    {
        return getParameterInfo(PAR0);
    }
    
    public void setParameterSpec(DParameter p0) 
    {
        setParameterInfo(PAR0, p0);
    }
	
	public Double getMorphValue() {
		return morphValue;
	}
	
	public void incValue() {
		setValue(Math.min(getValue()+1, getMaxValue()));
	}
	
	public void decValue() {
		setValue(Math.max(getValue()-1, getMinValue()));
	}

	public double getValuePercentage() {
		return ((double) getValue())/(getRange());
	}
	
	public void incMorph() {
		if (morphValue==null) {
			setMorphValue(0);
		} else {
			setMorphValue(Math.min(morphValue.doubleValue()+(1.0d/(getRange()+1)), +1.0d));
		}
	}
	
	public void decMorph() {
		if (morphValue==null) {
			setMorphValue(0);
		} else {
			setMorphValue(Math.max(morphValue.doubleValue()-(1.0d/(getRange()+1)), -1.0d));
		}
	}
	
	/**
	 * -1 .. +1
	 * @param morphValue
	 */
	public void setMorphValue(Double morphValue) {
		if (this.morphValue!=morphValue) {
            if (morphValue==null)
            {
                this.morphValue = null;
                if (parameter!=null) 
                {
                    if (parameter.getAssignedMorph()!=null)
                    {
                        if (morphValue==null)
                            parameter.getAssignedMorph().remove(parameter);
                        else
                            parameter.setMorphRange((int)(getRange()*morphValue));
                    }
                    // TODO else:Assign to morph 
                }
                fireValueOptionChangeEvent();
            }
            else
            {
                // check range
                double d = value + morphValue.doubleValue()*getRange();
                d = Math.max(getMinValue(), Math.min(d, getMaxValue()));
                d = (d-value)/getRange();
                if (this.morphValue==null || this.morphValue.doubleValue()!=d)
                {
        			this.morphValue = d;
        			fireValueOptionChangeEvent();
                }
            }
		}
	}
	
	public void setMorphValue(double morphValue) {
		setMorphValue(new Double(morphValue));
	}
	
	public void disableMorph() {
		setMorphValue(null);
	}
	
	public Color getMorphForeground() {
		return morphForeground;
	}

    public void setMorphForeground(Color morphForeground) {
        if (morphForeground!=null) {
            this.morphForeground = NomadUtilities.alpha(morphForeground, 128);
        }
    }

	public void setMorphBackround(Color morphBackground) {
		if (morphBackground!=null) {
			this.morphBackground = NomadUtilities.alpha(morphBackground, 64);
			repaint();
		}
	}
	
	public Color getMorphBackground() {
		return morphBackground;
	}
	
	public void fireValueChangeEvent() {
		//fireValueChangeEvent(new ChangeEvent(this));
		repaint();
	}
	
	public void fireValueOptionChangeEvent() {
		//fireValueOptionChangeEvent(new ChangeEvent(this));
		repaint();
	}
	
	
	public void setMaxValue(int max) {
		if (this.maxValue!=max) {
			this.maxValue = max;
			fireValueOptionChangeEvent();
		}
	}
	
	public void setMinValue(int min) {
		if (this.minValue!=min) {
			this.minValue = min;
			fireValueOptionChangeEvent();
		}
	}	
	
	public void setDefaultValue(Integer value) {
		if (this.defaultValue!=value) {
			this.defaultValue = value;
			fireValueOptionChangeEvent();
		}
	}
	
	public void setValue(int value) {
        value = Math.max(getMinValue(), Math.min(value, getMaxValue()));
        
		if (this.value!=value) {
			this.value = value;
			if (parameter!=null) 
            {
                parameter.setValue(value);
            }
			fireValueChangeEvent();
		}
		//setValue(value, this);
	}
	/*
	public void setValue(int value, Object sender) {
		if (this.value!=value) {
			this.value = value;
			fireValueChangeEvent(new ChangeEvent(sender));
		}
	}*/

	public int getMaxValue() {
		return maxValue;
	}
	
	public int getMinValue() {
		return minValue;
	}
	
	public Integer getDefaultValue() {
		return defaultValue;
	}
	
	public boolean hasDefaultValue() {
		return defaultValue!=null;
	}

	public int getValue() {
		return value;
	}
	
	public int getRange() {
		return getMaxValue()-getMinValue();
	}
	
	public void link(Module module) {
		//addValueChangeListener(broadcast);

        if (getParameterInfo(PAR0)!=null)
            parameter = module.getParameter(getParameterInfo(PAR0).getContextId());
		if (parameter!=null) {
			setValue(parameter.getValue());
			parameter.addParameterListener(this);
            loadMorphSettings();
		}
	}

	public void unlink() {
//		removeValueChangeListener(broadcast);
		if (parameter!=null) {
            setMorphValue(null);
			parameter.removeParameterListener(this);
			parameter = null;
		}
	}
    
    protected void loadMorphSettings()
    {
        Morph m = parameter.getAssignedMorph();
        if (m==null)
            setMorphValue(null);
        else
        {
            Color color = m.getColor();
            setMorphValue(parameter.getMorphRange()/(double)getRange());
            setMorphBackround(color);
            setMorphForeground(color.brighter());
        }
    }
    public void parameterValueChanged( Event e )
    {
        setValue( e.getParameter().getValue() );
    }

    public void parameterMorphValueChanged( Event e )
    { 
        setMorphValue( e.getParameter().getMorphRange()/127.0d );
    }

    public void parameterKnobAssignmentChanged( Event e )
    { }

    public void parameterMorphAssignmentChanged( Event e )
    { 
        loadMorphSettings();
    }

    public void parameterMidiCtrlAssignmentChanged( Event e )
    { }

}
