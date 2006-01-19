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
 * Created on Jan 9, 2006
 */
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.patch.Module;
import org.nomad.patch.Parameter;
import org.nomad.theme.property.IntegerProperty;
import org.nomad.theme.property.ParameterProperty;
import org.nomad.xml.dom.module.DParameter;

/**
 * @author Christian Schneider
 */
public class NomadActiveLabel extends NomadLabel {

	private int padding = 2;
	Border b = NomadBorderFactory.createNordEditor311Border();
	Insets nomadInsets = b.getBorderInsets(this);
	private boolean isInitialized = false;
	private DParameter parameterInfo = null;
	
	public NomadActiveLabel() {
		super();
		isInitialized = true;
		getAccessibleProperties().add(new PaddingProperty(this));
		setDynamicOverlay(true);
		setBackground(Color.decode("#372C7B"));
		setForeground(Color.WHITE);
		autoResize();
		setPreferredSize(getSize());
		setAutoResize(false);
		
		getAccessibleProperties().add(new ParameterProperty(this) {

			public void setDParameter(DParameter p) {
				parameterInfo = p;
				
				if (parameterInfo!=null) {
					setText(parameterInfo.getName());
				}
				
			}
			
			public DParameter getDParameter() {
				return ((NomadActiveLabel)getComponent()).getParameterInfo();
			}

		});

	}
	
	public DParameter getParameterInfo() {
		return parameterInfo;
	}
	
	public Dimension getFittingSize() {
		if (!isInitialized) {
			b = NomadBorderFactory.createNordEditor311Border();
			nomadInsets = b.getBorderInsets(this);
			isInitialized=true;
		}
		
		Dimension d = getTextDimensions();
		d.width+=2*padding-1+nomadInsets.left+nomadInsets.right;
		d.height+=2*padding-1+nomadInsets.bottom+nomadInsets.top;
		return d;
	}

	protected void autoResize() {
		if (isAutoResizeEnabled()) {
			Dimension d = getFittingSize();
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
			setSize(d);
		}
	}

	public void paintDecoration(Graphics2D g2) {
		if (getBackground()!=null) {
			g2.setColor(getBackground());
			g2.fillRect(nomadInsets.left, nomadInsets.top, 
					getWidth()-1-nomadInsets.bottom, getHeight()-1-nomadInsets.right);
		}
		b.paintBorder(this, g2, 0, 0, getWidth(), getHeight());
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {		
		{ // text
			Graphics2D gtext = (Graphics2D) g2.create();
			gtext.translate(padding+nomadInsets.left, padding+nomadInsets.top);
			gtext.setClip(new Rectangle(0, 0, getWidth()-2*padding-(+nomadInsets.left+nomadInsets.right), 
					getHeight()-2*padding-(nomadInsets.bottom+nomadInsets.top)));
			super.paintDecoration(gtext);
			gtext.dispose();
		}
	}
	
	public void setPadding(int padding) {
		if (this.padding!=padding) {
			this.padding = padding;
			fireTextUpdateEvent();
		}
	}
	
	public int getPadding() {
		return padding;
	}

	private class PaddingProperty extends IntegerProperty {
		public PaddingProperty(NomadComponent component) {
			super(component);
			setName("padding");
		}
		public void setIntegerValue(int integer) { if (integer>=0) setPadding(integer); }
		public int getIntegerValue() { return ((NomadActiveLabel)getComponent()).getPadding(); }
	}
		
	public void link() {
		Module module = getModule();
		if (module!=null) {
			parameter = module.findParameter(getParameterInfo());
			if (parameter!=null) {
				plistener = new ParameterChangeListener();
				parameter.addChangeListener(plistener);
				updateParamText();
			}
		}
	}

	public void unlink() {
		//TODO revert link()
	}
	private Parameter parameter = null;
	
	
	private ParameterChangeListener plistener = null;
	
	private void updateParamText() {
		if (parameter!=null) {
			setText(parameter.getInfo().getFormattedValue(parameter.getValue()));
		}
	}
	
	private class ParameterChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			updateParamText();
			repaint();
		}
	}
	
}
