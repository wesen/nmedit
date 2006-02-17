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
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.patch.Module;
import org.nomad.patch.Parameter;
import org.nomad.theme.property.ParameterProperty;
import org.nomad.theme.property.PropertySet;
import org.nomad.xml.dom.module.DParameter;


/**
 * @author Christian Schneider
 */
public abstract class NomadControl extends NomadComponent {
	private ArrayList<ChangeListener> valueChangeListenerList = new ArrayList<ChangeListener>(4);
	private ArrayList<ChangeListener> valueOptionChangeListenerList = new ArrayList<ChangeListener>(4);
	private int value = 0;
	private int minValue = 0;
	private int maxValue = 100;
	private Integer defaultValue = null;
	private Color morphForeground = new Color(1, 0, 0, 0.5f);
	private Color morphBackground = new Color(1, 0.25f, 0.25f, 0.25f);
	private Double morphValue = null;
	private DParameter parameterInfo = null;
	private Parameter parameter = null;
	private ParameterControlBroadcast broadcast = null;
	private ParameterChangeListener paramListener = null;
	
	private final static Repainter repainter = new Repainter();
	
	private static class Repainter implements ChangeListener, FocusListener {

		public void stateChanged(ChangeEvent event) { repaint(event.getSource()); }		
		public void focusGained(FocusEvent event) { repaint(event.getSource()); }
		public void focusLost(FocusEvent event) { repaint(event.getSource()); }
		void repaint(Object source) {
			if (source instanceof NomadControl) ((NomadControl)source).repaint();
		}
		
	}
	
	public NomadControl() {
		super();	 
		addValueChangeListener(repainter);
		addValueOptionChangeListener(repainter);
		addFocusListener(repainter);
/*
		addMouseListener(new MouseAdapter(){

			public void mousePressed(MouseEvent event) {
				ParameterToolTip.removeTip();
			}
			public void mouseEntered(MouseEvent event) {
				if (parameterInfo!=null) {
					ParameterToolTip tip = new ParameterToolTip(NomadControl.this, parameterInfo, getValue());
					tip.showTip(500);
				}
			}

			public void mouseExited(MouseEvent event) {
				ParameterToolTip.removeTip();
			}});*/
	}

	protected void createProperties(PropertySet set) {
		super.createProperties(set);
		set.add(new ParameterProperty(this));
	}
	
	public void setParameterInfo(DParameter parameterInfo) {
		this.parameterInfo = parameterInfo;
		if (parameterInfo!=null) {
			setMinValue(parameterInfo.getMinValue());
			setMaxValue(parameterInfo.getMaxValue());
			setDefaultValue(new Integer(parameterInfo.getDefaultValue()));
		}
	}
	
	public DParameter getParameterInfo() {
		return parameterInfo;
	}

	public void setMorphForeground(Color morphForeground) {
		if (morphForeground!=null) {
			this.morphForeground = morphForeground;
		}
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
			this.morphValue = morphValue;
			fireValueOptionChangeEvent();
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

	public void setMorphBackround(Color morphBackground) {
		if (morphBackground!=null) {
			this.morphBackground = morphBackground;
			repaint();
		}
	}
	
	public Color getMorphBackground() {
		return morphBackground;
	}
	
	public void addValueChangeListener(ChangeListener l) {
		if (!valueChangeListenerList.contains(l))
			valueChangeListenerList.add(l);
	}
	
	public void removeValueChangeListener(ChangeListener l) {
		valueChangeListenerList.remove(l);
	}
	
	public void fireValueChangeEvent(ChangeEvent event) {
		for (ChangeListener l : valueChangeListenerList ) {
			l.stateChanged(event);
		}
	}
	
	public void fireValueChangeEvent() {
		fireValueChangeEvent(new ChangeEvent(this));
	}
	
	public void addValueOptionChangeListener(ChangeListener l) {
		if (valueOptionChangeListenerList==null)
			valueOptionChangeListenerList = new ArrayList<ChangeListener>();
		
		if (!valueOptionChangeListenerList.contains(l))
			valueOptionChangeListenerList.add(l);
	}
	
	public void removeValueOptionChangeListener(ChangeListener l) {
		if (valueOptionChangeListenerList==null)
			return;
		
		valueOptionChangeListenerList.remove(l);
		if (valueOptionChangeListenerList.size()==0)
			valueOptionChangeListenerList = null;
	}
	
	public void fireValueOptionChangeEvent(ChangeEvent event) {
		if (valueOptionChangeListenerList==null)
			return;
		
		for (ChangeListener l : valueOptionChangeListenerList) {
			if (event.getSource()!=l) 
				l.stateChanged(event);
		}
	}
	
	public void fireValueOptionChangeEvent() {
		fireValueOptionChangeEvent(new ChangeEvent(this));
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
		setValue(value, this);
	}
	
	public void setValue(int value, Object sender) {
		if (this.value!=value) {
			this.value = value;
			fireValueChangeEvent(new ChangeEvent(sender));
		}
	}

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
		if (broadcast==null)
			broadcast = new ParameterControlBroadcast();
		addValueChangeListener(broadcast);

		parameter = module.findParameter(getParameterInfo());
		if (parameter!=null) {
			setValue(parameter.getValue());
			paramListener = new ParameterChangeListener();
			parameter.addChangeListener(paramListener);
		}
	}

	public void unlink() {
		removeValueChangeListener(broadcast);
		broadcast = null;
		if (parameter!=null) {
			parameter.removeChangeListener(paramListener);
			parameter = null;
		}
	}
	
	private class ParameterChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			setValue(parameter.getValue());
		}
	}
	
	private class ParameterControlBroadcast implements ChangeListener {

		public void stateChanged(ChangeEvent event) {
			if (parameter!=null) {
				parameter.setValue(getValue());
			}
		}
		
	}
	
}
