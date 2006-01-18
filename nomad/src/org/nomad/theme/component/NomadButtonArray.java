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
 * Created on Jan 12, 2006
 */
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.theme.ImageString;
import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.component.model.NomadButtonArrayBehaviour;
import org.nomad.theme.component.model.NomadButtonArrayModel;
import org.nomad.theme.property.BooleanProperty;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.editor.PropertyEditor;
import org.nomad.xml.dom.module.DParameter;

public class NomadButtonArray extends NomadControl implements NomadButtonArrayModel {

	private NomadButtonArrayBehaviour behaviour = new NomadButtonArrayBehaviour(this);
	private ArrayList labelList = new ArrayList();
	private boolean flagIsCylicDisplay = true;
	private boolean flagLandscape = false;
	private int lastW = 0;
	private int lastH = 0;
	private int buttonsAreObsolete = 0;
	private boolean allowTextPropertyExport = true;
	
	public NomadButtonArray() {
		setBackground(NomadClassicColors.MODULE_BACKGROUND);
		setOpaque(false);
		setFont(new Font("SansSerif", Font.PLAIN, 10));
		setDynamicOverlay(true);
		
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event) {
				behaviour.calculateMetrics();
				
				if (flagIsCylicDisplay) { 
					int v = getValue()+1;
					if (v>getMaxValue()) v = getMinValue();
					setValue(v);
				} else {
					int index = behaviour.getButtonIndexAt(event.getPoint());
					if (0<=index && index<labelList.size()) {
						setValue(index);
					}
				}
				repaint();
			}
		});	

		getAccessibleProperties().byName("parameter#0")
			.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event) {
				DParameter param = getParameterInfo();
				
				labelList.clear();
				allowTextPropertyExport = false;
				
				if (param!=null) {
					for (int i=getMinValue();i<=getMaxValue();i++) {
						addButton(param.getFormattedValue(i));
					}
				}
			}});

		getAccessibleProperties().setFallbackProperty(new Fallback(this));
		
		getAccessibleProperties().add(new NewButtonProperty(this));
		getAccessibleProperties().add(new RemoveButtonProperty(this));
		getAccessibleProperties().add(new OrientationProperty(this));
		getAccessibleProperties().add(new CyclicProperty(this));

		addButton(encodeButtonName(labelList.size()));
		//addButton(createPropertyName(labelList.size()));
		autoResize(false);
		Dimension d = new Dimension(30,10);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		setSize(d);
		
		buttonsAreObsolete = labelList.size();
	}

	public void autoResize(boolean force) {
		/*if (force||(lastW!=getWidth())||(lastH!=getHeight())) {
/*			behaviour.calculateMetrics();
			Dimension d = behaviour.getPreferredSize();
			setMinimumSize(d);
			setMaximumSize(d);
			setPreferredSize(d);
			setSize(d);
			lastW = getWidth();
			lastH = getHeight(); 
		}*/
	}

	public int getButtonCount() {
		return flagIsCylicDisplay ? 1 : labelList.size();
	}

	private Dimension getLabelSize(int index) {
		ImageString istr = (ImageString) labelList.get(index);
		if (istr.getImage()!=null) {
			Image i = istr.getImage();
			return new Dimension(i.getWidth(null),i.getHeight(null));
		}
		Rectangle bounds = NomadLabel.getStringBounds(this, istr.toString());
		return bounds.getSize();
	}
	
	private Dimension getMaxLabelSize() {
		Dimension d = new Dimension(6,6);
		for (int i=0;i<labelList.size();i++) {
			Dimension c = getLabelSize(i);
			d.width=Math.max(d.width,c.width);
			d.height=Math.max(d.height,c.height);
		}
		return d;
	}

	public Dimension getPreferredCellSize(int index) {
		
		if (flagIsCylicDisplay) {
			return getMaxLabelSize();
		} else {
			return getLabelSize(index);
		}
	}

	private Pattern btnNamePattern = Pattern.compile("btn#(\\d+)");

	private String encodeButtonName(int index) {
		return "btn#"+index;
	}
	
	public int decodeButtonIndex(String buttonName) {
		if (btnNamePattern.matcher(buttonName).matches()) {
			String[] btn = getName().split("#");
			return Integer.parseInt(btn[1]);
		} else {
			return -1;
		}
	}
	
	private ImageString newLabel(String label) {
		ImageString istring = new ImageString(label);
		if (getEnvironment()!=null)
			istring.loadImage(getEnvironment().getImageTracker());
		return istring;
	}

	public void addButton(String label) {
		while (buttonsAreObsolete>0) {
			buttonsAreObsolete--;
			removeButton(0);
		}
		
		int index = labelList.size();
		labelList.add(newLabel(label));
		deleteOnScreenBuffer();
		autoResize(true);
		getAccessibleProperties().add(new BtnTextProperty(this, index));
	}
	
	public void setButton(int index, String label) {
		while (buttonsAreObsolete>0) {
			buttonsAreObsolete--;
			removeButton(0);
		}
		
		if (index<labelList.size()) {
			getAccessibleProperties().remove(getAccessibleProperties().byName(encodeButtonName(index)));
			labelList.set(index, newLabel(label));
			getAccessibleProperties().add(new BtnTextProperty(this, index));
			deleteOnScreenBuffer();
		} else {
			while (index+1<labelList.size())
				addButton(encodeButtonName(labelList.size()));

			addButton(label);
		}
	}
	
	public void removeButton(int index) {
		if (0<=index && index<labelList.size()) {
			getAccessibleProperties().remove(getAccessibleProperties().byName(encodeButtonName(index)));
			labelList.remove(index);
		}
		deleteOnScreenBuffer();
		autoResize(true);
	}
	

	public boolean isLandscape() {
		return flagLandscape;
	}
	
	public void setLandscape(boolean landscape) {
		if (this.flagLandscape!=landscape) {
			this.flagLandscape = landscape;
			autoResize(true);
		}
	}

	
	public void paintDecoration(Graphics2D g2) {
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {
		behaviour.calculateMetrics();

		g2.setFont(getFont());

		Border bbutton = NomadBorderFactory.createNordEditor311RaisedBorder(2);
		Border bselection = NomadBorderFactory.createNordEditor311LoweredBorder(2);
		Border b = bbutton;

		if (flagIsCylicDisplay) {
			int i = getValue();
			if (getValue()==0)
				b=bselection;
			
			ImageString istr;
			if (0<=i && i<labelList.size())
				istr = (ImageString) labelList.get(i);
			else if (labelList.size()>0)
				istr = (ImageString) labelList.get(labelList.size()-1);
			else
				istr = new ImageString("");
				
			Rectangle bounds;
			
			if (istr.getImage()!=null)
				bounds = istr.getImageBounds(this);
			else
				bounds = NomadLabel.getStringBounds(this, istr.getString());
			Point cell = behaviour.getCell(0);
			
			g2.setColor(getBackground());
			g2.fillRect(cell.x, cell.y, behaviour.getCellWidth()-2, behaviour.getCellHeight()-2);
			
			g2.setColor(Color.WHITE);

			b.paintBorder(this, g2, cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			//g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			
			bounds.x += (behaviour.getCellWidth()-bounds.width)/2;
			bounds.y += (behaviour.getCellHeight()-bounds.height)/2;

			if (istr.getImage()!=null) {
				
				g2.drawImage(istr.getImage(), bounds.x, bounds.y, this);
				
			} else {
				g2.setColor(Color.BLACK);
				g2.drawString(istr.getString(), cell.x+bounds.x, cell.y+bounds.y);
			}
		}	else {	

			for (int i=getButtonCount()-1;i>=0;i--) {
				ImageString istr;
				if (0<=i && i<labelList.size()) {
					istr = (ImageString) labelList.get(i);
				} else
					istr = new ImageString("");

				Rectangle bounds;

				if (istr.getImage()!=null)
					bounds = istr.getImageBounds(this);
				else
					bounds = NomadLabel.getStringBounds(this, istr.getString());
				Point cell = behaviour.getCell(i);

				g2.setColor(getBackground());
				g2.fillRect(cell.x, cell.y, behaviour.getCellWidth()-2, behaviour.getCellHeight()-2);
				
				g2.setColor(Color.WHITE);
				if (i==getValue()) {
					b=bselection;
				} else {
					b=bbutton;
				}
				b.paintBorder(this, g2, cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				//g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				
				bounds.x += (behaviour.getCellWidth()-bounds.width)/2;
				bounds.y += (behaviour.getCellHeight()-bounds.height)/2;

				if (istr.getImage()!=null) {
					
					g2.drawImage(istr.getImage(), cell.x+bounds.x, cell.y+bounds.y, this);
					
				} else {
					g2.setColor(Color.BLACK);
					g2.drawString(istr.getString(), cell.x+bounds.x, cell.y+bounds.y);
				}
			}
			
		}
	}

	private class BtnTextProperty extends Property {
		int index;
		public BtnTextProperty(NomadComponent component, int index) {
			super(component);
			this.index=index;
			setName(encodeButtonName(index));
		}

		public Object getValue() {			
			ArrayList labelList = ((NomadButtonArray)getComponent()).labelList;
			if (index>=0 && index<labelList.size())
				return labelList.get(index);
			else
				return null;
		}

		public void setValueFromString(String value) {
			if (index>=0 && index<labelList.size()) {
				setButton(index, value);
			} else 
				addButton(value);
			allowTextPropertyExport = true;
		}
		
		public boolean isExportable() {
			return allowTextPropertyExport;
		}
		
	}

	private class NewButtonProperty extends Property {

		public NewButtonProperty(NomadComponent component) {
			super(component);
			setValidatingName(false);
			setName(":add");
			setExportable(false);
		}

		public Object getValue() {
			return "<add>";
		}

		public void setValueFromString(String value) {
			addButton(value);
		}
		
	}
	private class RemoveButtonProperty extends Property {

		public RemoveButtonProperty(NomadComponent component) {
			super(component);
			setValidatingName(false);
			setName(":remove");
			setExportable(false);
		}

		public Object getValue() {
			return "<remove>";
		}

		public void setValueFromString(String value) {
			// ignore
		}

		public PropertyEditor getEditor() {
			return new PropertyEditor(this) {

				private JButton btn = new JButton("action");
				
				 {
					 btn.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent event) {
							int index = NomadButtonArray.this.getValue();
							NomadButtonArray.this.decValue();
							removeButton(index);
							fireEditingCanceled();
						}});
				}
				
				public Object getEditorValue() {
					return "";
				}

				public JComponent getEditorComponent() {
					return btn;
				}

				
			};
		}
		
	}
	
	private class OrientationProperty extends BooleanProperty {

		public OrientationProperty(NomadComponent component) {
			super(component);
			setName("landscape");
		}

		public void setBooleanValue(boolean value) {
			setLandscape(value);
		}

		public boolean getBoolean() {
			return ((NomadButtonArray)getComponent()).isLandscape();
		}
		
	}
	
	private class CyclicProperty extends BooleanProperty {

		public CyclicProperty(NomadComponent component) {
			super(component);
			setName("cyclic");
		}

		public void setBooleanValue(boolean value) {
			setIsCylicDisplay(value);
		}

		public boolean getBoolean() {
			return ((NomadButtonArray)getComponent()).isCylicDisplay();
		}
		
	}
	
	private class Fallback extends Property {

		public Fallback(NomadComponent component) {
			super(component);
			setValidatingName(false);
		}

		public Object getValue() {
			return "<fallback>";
		}
		
		private Pattern p = Pattern.compile("btn#(\\d+)");

		public void setValueFromString(String value) {
			if (p.matcher(getName()).matches()) {
				String[] btn = getName().split("#");
				int index = Integer.parseInt(btn[1]);
				setName(null);			
				setButton(index, value);
			} else 
				throw new IllegalArgumentException("Property "+this+" does not handle value "+value+".");
		}
		
	}

	public boolean isCylicDisplay() {
		return flagIsCylicDisplay;
	}

	public void setIsCylicDisplay(boolean isCylicDisplay) {
		this.flagIsCylicDisplay = isCylicDisplay;
	}
}
