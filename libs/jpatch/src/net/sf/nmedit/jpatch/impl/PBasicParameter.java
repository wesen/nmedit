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
package net.sf.nmedit.jpatch.impl;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoableEdit;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PUndoableEditFactory;
import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpatch.event.PParameterListener;

/**
 * The reference implementation of interface {@link PParameter}.
 * @author Christian Schneider
 */
public class PBasicParameter 
    extends PBasicComponent<PParameterDescriptor> 
    implements PParameter
{

    // the parent module
    private PModule parent;
    // the parameter value
    private int value;
    private int morphGroup; 
    private EventListenerList listenerList = new EventListenerList();
    private boolean undoEnabled = true;
    private int lastValueBeforeDisableUndo = 0;
    
    public boolean isUndoableEditSupportEnabled()
    {
    	return super.isUndoableEditSupportEnabled() && undoEnabled;
    }

    
    private PParameter extensionParameter;

    public PBasicParameter(PParameterDescriptor descriptor,
            PModule parent, int componentIndex)
    {
        super(descriptor, componentIndex);
        this.lastValueBeforeDisableUndo = this.value = getDefaultValue();
        this.parent = parent;
        // initially the parameter is not assigned to a morph group
        this.morphGroup = -1;
    }
    
    public PModule getParentComponent()
    {
        return parent;
    }

    public int getMaxValue()
    {
        return getDescriptor().getMaxValue();
    }

    public int getMinValue()
    {
        return getDescriptor().getMinValue();
    }

    public int getRange()
    {
        return getMaxValue()-getMinValue()+1;
    }

    public int getDefaultValue()
    {
        return getDescriptor().getDefaultValue();
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        int oldValue = this.value;
        if (value<getMinValue())
            value = getMinValue();
        else if (value>getMaxValue())
            value = getMaxValue();
        if (oldValue != value)
        {
            this.value = value;
            if (isUndoableEditSupportEnabled())
            {
            	int newValue = this.value;
            	UndoableEdit edit = createParameterValueEdit(oldValue, newValue);
                if (edit != null) postEdit(edit);
            }
            fireParameterValueChanged(oldValue, value);
        }
    }
    
    public UndoableEdit createParameterValueEdit(int oldValue, int newValue) 
    {
        PUndoableEditFactory factory = getUndoableEditFactory();
        if (factory != null)
            return factory.createParameterValueEdit(this, oldValue, newValue);
        return null;
    }

    public double getDoubleValue()
    {
        int min = getMinValue();
        int max = getMaxValue();
        return (min >= max) ? 0 : ((getValue()-min)/(double)(max-min));
    }

    public float getFloatValue()
    {
        int min = getMinValue();
        int max = getMaxValue();
        return (min >= max) ? 0 : ((getValue()-min)/(float)(max-min));
    }

    public void setDoubleValue(double value)
    {
        if (value<=0d)
            setValue(getMinValue());
        else if (value>=1d)
            setValue(getMaxValue());
        else
            setValue(getMinValue() + (int)(value*(double)(getMaxValue()-getMinValue())));
    }

    public void setFloatValue(float value)
    {
        if (value<=0f)
            setValue(getMinValue());
        else if (value>=1f)
            setValue(getMaxValue());
        else
            setValue(getMinValue() + (int)(value*(float)(getMaxValue()-getMinValue())));        
    }

    public String getDisplayValue()
    {
        return getDisplayValue(value);
    }

    public String getDisplayValue(int value)
    {
        if (value<getMinValue()) value = getMinValue();
        else if (value>getMaxValue()) value = getMaxValue();
        return getDescriptor().getDisplayValue(this, value);
    }

    public void addParameterListener(PParameterListener l)
    {
        listenerList.add(PParameterListener.class, l);
    }

    public void removeParameterListener(PParameterListener l)
    {
        listenerList.remove(PParameterListener.class, l);
    }

    protected void fireParameterValueChanged(int oldValue, int newValue) 
    {
        PParameterEvent parameterEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PParameterListener.class) 
            {
                // Lazily create the event:
                if (parameterEvent == null)
                    parameterEvent = new PParameterEvent(this, PParameterEvent.VALUE_CHANGED);
                ((PParameterListener)listeners[i+1]).parameterValueChanged(parameterEvent);
            }
        }
    }

    protected void fireFocusRequested() 
    {
        PParameterEvent parameterEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) 
        {
            if (listeners[i]==PParameterListener.class) 
            {
                // Lazily create the event:
                if (parameterEvent == null)
                    parameterEvent = new PParameterEvent(this, PParameterEvent.PARAMETER_FOCUS_REQUEST);
                ((PParameterListener)listeners[i+1]).focusRequested(parameterEvent);
            }
        }
    }

    public PParameter getExtensionParameter()
    {
        return extensionParameter;
    }
    
    void setExtensionParameter(PParameter p)
    {
        this.extensionParameter = p;
    }

    public void requestFocus()
    {
        PModuleContainer mc = getParentComponent().getParentComponent();
        if (mc != null)
        {
            PPatch p = mc.getPatch();
            if (p != null && p.getFocusedComponent()!=this && p.setFocusedComponent(this))
            {
                fireFocusRequested();
            }
        }
    }

    public int getMorphGroup()
    {
    	return morphGroup;
    }
    
	public void setMorphGroup(int group) {
		
		if (group != morphGroup)
		{
			morphGroup = group;

			PParameterEvent parameterEvent = null;
			// Guaranteed to return a non-null array
			Object[] listeners = listenerList.getListenerList();
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length-2; i>=0; i-=2) 
			{
				if (listeners[i]==PParameterListener.class) 
				{
					// Lazily create the event:
					if (parameterEvent == null)
						parameterEvent = new PParameterEvent(this, PParameterEvent.MORPH_GROUP_CHANGED);
					((PParameterListener)listeners[i+1]).parameterValueChanged(parameterEvent);
				}
			}
		}
	}

	public void disableUndo() {
		undoEnabled = false;
	}

	public void enableUndo() {
		undoEnabled = true;
	}



}
