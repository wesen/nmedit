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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.border.Border;

import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.component.model.NomadButtonArrayBehaviour;
import org.nomad.theme.component.model.NomadButtonArrayModel;
import org.nomad.theme.property.BooleanProperty;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.util.misc.ImageString;

public class NomadButtonArray extends NomadControl implements NomadButtonArrayModel {

	private NomadButtonArrayBehaviour behaviour = new NomadButtonArrayBehaviour(this);
	private boolean flagIsCylicDisplay = true;
	private boolean flagLandscape = false;
	private ImageString[] labelList = new ImageString[6];
	
	private static MouseAdapter buttonArrayMouseListener = new MouseAdapter(){
		public void mouseClicked(MouseEvent event) {
			if (event.getSource() instanceof NomadButtonArray) {
				NomadButtonArray nba = (NomadButtonArray) event.getSource();
				nba.requestFocus();
				nba.behaviour.calculateMetrics();
				
				if (nba.flagIsCylicDisplay) { 
					int v = nba.getValue()+1;
					if (v>nba.getMaxValue()) v = nba.getMinValue();
					nba.setValue(v);
				} else {
					nba.setValue(nba.behaviour.getButtonIndexAt(event.getPoint()));
				}
			}
		}
	};
	
	private static Dimension prefSize = new Dimension(30,10);
	private static Border DEFAULT_SEL_Border = NomadBorderFactory.createNordEditor311LoweredButtonBorder();
	private static Border DEFAULT_Border = NomadBorderFactory.createNordEditor311RaisedButtonBorder();
	
	private final static Font defaultFont = new Font("SansSerif", Font.PLAIN, 10);
	
	public NomadButtonArray() {
		setBackground(NomadClassicColors.BUTTON_BACKGROUND);
		//setForeground(NomadClassicColors.BUTTON_FOREGROUND);
		setOpaque(false);
		setFont(defaultFont);
		setDynamicOverlay(true);
		setFocusable(true);

		Arrays.fill(labelList, null);

		setSelectionBorder(DEFAULT_SEL_Border);
		setDefaultBorder(DEFAULT_Border);
		addMouseListener(buttonArrayMouseListener);
		//autoResize(false);
		setMinimumSize(prefSize);
		setMaximumSize(prefSize);
		setPreferredSize(prefSize);
		setSize(prefSize);
	}

	public void registerProperties(PropertySet set) {
		super.registerProperties(set);
		for (int i=labelList.length-1;i>=0;i--) {
			set.add(new BtnTextProperty(i));
		}
		set.add(new OrientationProperty());
		set.add(new CyclicProperty());
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
		return flagIsCylicDisplay ? 1 : getRange()+1;
	}

	private Dimension getLabelSize(int index) {
		ImageString istr = labelList[index];
		if (istr==null)
			return new Dimension(0,0);
		
		if (istr.getImage()!=null) {
			Image i = istr.getImage();
			return new Dimension(i.getWidth(null),i.getHeight(null));
		}
		Rectangle bounds = NomadLabel.getStringBounds(this, istr.toString());
		return bounds.getSize();
	}
	
	private Dimension getMaxLabelSize() {
		Dimension d = new Dimension(6,6);
		for (int i=0;i<labelList.length;i++) {
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

	private static String encodeButtonName(int index) {
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

	public void setButton(int index, String label) {
		if (0<=index && index<labelList.length) {
			if ("".equals(label))
				labelList[index] = null;
			else
				labelList[index] = newLabel(label);
			fullRepaint();
		}
	}

	public void removeButton(int index) {
		if (0<=index && index<labelList.length) {
			labelList[index] = null;
			fullRepaint();
		}
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

	public void setSelectionBorder(Border border) {
		selBorder = border;
		fullRepaint();
	}
	
	public void setDefaultBorder(Border border) {
		defBorder = border;
		fullRepaint();
	}

	private Color focusedColor = NomadClassicColors.BUTTON_FOCUSED_BACKGROUND;
	private Color selectedColor= NomadClassicColors.BUTTON_SELECTED_BACKGROUND;
	
	public void setButtonFocusedColor(Color color) {
		if (this.focusedColor!=color) {
			this.focusedColor = color;
			fullRepaint();
		}
	}
	
	public void setButtonSelectedColor(Color color) {
		if (this.selectedColor!=color) {
			this.selectedColor = color;
			fullRepaint();
		}
	}

	private Border defBorder = null;
	private Border selBorder = null;
	
	public void paintDynamicOverlay(Graphics2D g2) {
		behaviour.calculateMetrics();

		g2.setFont(getFont());
		Border b = defBorder;

		if (flagIsCylicDisplay) {
			int i = getValue();
			if (getValue()!=0)
				b=selBorder;
			
			ImageString istr;
			if (0<=i && i<labelList.length && labelList[i]!=null)
				istr = labelList[i];
			else
				istr = new ImageString("");
				
			Rectangle bounds;
			
			if (istr.getImage()!=null)
				bounds = istr.getImageBounds(this);
			else
				bounds = NomadLabel.getStringBounds(this, istr.getString());
			Point cell = behaviour.getCell(0);

			if (getValue()==1)
				g2.setColor(selectedColor);
			else
				g2.setColor(hasFocus()?focusedColor:getBackground());

			g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			
			g2.setColor(Color.WHITE);

			b.paintBorder(this, g2, cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			//g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			
			bounds.x += (behaviour.getCellWidth()-bounds.width)/2;
			bounds.y += (behaviour.getCellHeight()-bounds.height)/2 ;

			if (istr.getImage()!=null) {
				
				g2.drawImage(istr.getImage(), bounds.x, bounds.y, this);
				
			} else {
				g2.setColor(Color.BLACK);
				g2.drawString(istr.getString(), cell.x+bounds.x, cell.y+bounds.y+1);
			}
		}	else {	

			for (int i=getButtonCount()-1;i>=0;i--) {
				ImageString istr;
				if (0<=i && i<labelList.length && labelList[i]!=null) {
					istr = labelList[i];
				} else
					istr = new ImageString("");

				Rectangle bounds;

				if (istr.getImage()!=null)
					bounds = istr.getImageBounds(this);
				else
					bounds = NomadLabel.getStringBounds(this, istr.getString());
				Point cell = behaviour.getCell(i);

				if (getValue()==i)
					g2.setColor(selectedColor);
				else
					g2.setColor(hasFocus()?focusedColor:getBackground());

				g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				
				g2.setColor(Color.WHITE);
				if (i==getValue()) {
					b=selBorder;
				} else {
					b=defBorder;
				}
				b.paintBorder(this, g2, cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				//g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				
				bounds.x += (behaviour.getCellWidth()-bounds.width)/2 ;
				bounds.y += (behaviour.getCellHeight()-bounds.height)/2 ;
				
				if (istr.getImage()!=null) {
					
					g2.drawImage(istr.getImage(), cell.x+bounds.x, cell.y+bounds.y, this);
					
				} else {
					g2.setColor(Color.BLACK);
					g2.drawString(istr.getString(), cell.x+bounds.x, cell.y+bounds.y+1);
				}
			}
			
		}
	}

	private static class BtnTextProperty extends Property {
		int index;
		public BtnTextProperty(int index) {
			this.index=index;
			setName(NomadButtonArray.encodeButtonName(index));
		}

		public String getValue() {
			ImageString is = ((NomadButtonArray)getComponent()).labelList[index]; 
			return is == null ? "null" : is.getString();
		}

		public void setValue(String value) {
			if (0<=index && index<((NomadButtonArray)getComponent()).labelList.length) {
				((NomadButtonArray)getComponent()).setButton(index, value);
			}
		}

		public boolean isExportable() {
			return ((NomadButtonArray)getComponent()).labelList[index]!=null;
		}
		
	}

	private static class OrientationProperty extends BooleanProperty {

		public OrientationProperty() {
			setName("landscape");
		}

		public void setBoolean(boolean value) {
			((NomadButtonArray)getComponent()).setLandscape(value);
		}

		public boolean getBoolean() {
			return ((NomadButtonArray)getComponent()).isLandscape();
		}

	}
	
	private static class CyclicProperty extends BooleanProperty {

		public CyclicProperty() {
			setName("cyclic");
		}

		public void setBoolean(boolean value) {
			((NomadButtonArray)getComponent()).setIsCylicDisplay(value);
		}

		public boolean getBoolean() {
			return ((NomadButtonArray)getComponent()).isCylicDisplay();
		}
		
	}

	public boolean isCylicDisplay() {
		return flagIsCylicDisplay;
	}

	public void setIsCylicDisplay(boolean isCylicDisplay) {
		this.flagIsCylicDisplay = isCylicDisplay;
	}
}
