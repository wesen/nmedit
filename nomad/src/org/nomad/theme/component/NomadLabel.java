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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.nomad.theme.ImageString;
import org.nomad.theme.property.BooleanProperty;
import org.nomad.theme.property.FontProperty;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;

/**
 * @author Christian Schneider
 */
public class NomadLabel extends NomadComponent {

	private ImageString imageString = new ImageString("label");
	private int sx = 0;
	private int sy = 0;
	private boolean flagVertText = false;
	private boolean flagAutoResize = true;
	private boolean flagTextAntialiasing = true;
	private final static Font defaultLabelFont = new Font("SansSerif", Font.PLAIN, 9);
	
	public NomadLabel() {
		super();
		setFont(defaultLabelFont);
		setOpaque(false);
		setForeground(Color.BLACK);
		autoResize();
	}
	
	protected void createProperties(PropertySet set) {
		super.createProperties(set);
		set.add(new LabelTextProperty(this));
		set.add(new VerticalTextProperty(this));
		set.add(new LabelFontProperty(this));
		set.add(new AntialiasTextProperty(this));
	}
	
	public static Rectangle getStringBounds(JComponent component, String string) {
		return getStringBounds(component, string, component.getFont());
	}

	public static Rectangle getStringBounds(JComponent component, String string, Font font) {
		return getStringBounds(component, string, font, false);
	}
	
	public static Rectangle getStringBounds(JComponent component, String string, Font font, boolean vertical) {
		FontMetrics fm = component.getFontMetrics(font);
		Rectangle bounds = new Rectangle(0,0,0,0);
		if (vertical) {
			bounds.width = fm.getMaxAdvance();
			bounds.height = (fm.getHeight()+1)*string.length();	
			bounds.y=fm.getHeight();
		} else {
			bounds.width = fm.stringWidth(string);
			bounds.height= fm.getHeight();
			bounds.y = bounds.height-fm.getDescent();
		} 
		return bounds;
	}

	public Dimension getTextDimensions() {
		Font font = getFont();
		FontMetrics fm = getFontMetrics(font);
		Dimension preferredSize;
		if (isVertical()) {
			preferredSize = new Dimension(fm.getMaxAdvance(), (fm.getHeight()+1)*imageString.getString().length());
			// sx = 0; sy = 0;			
			sy = fm.getHeight();
		} else {
			preferredSize = new Dimension(fm.stringWidth(imageString.getString()), fm.getHeight());
			sx = 0; sy = preferredSize.height-fm.getDescent();
		} 
		return preferredSize;
	}

	public boolean isTextAntialiased() {
		return flagTextAntialiasing;
	}
	
	public void setTextAntialiased(boolean enable) {
		if (flagTextAntialiasing!=enable) {
			flagTextAntialiasing=enable;
			fireTextUpdateEvent();
		}
	}
	
	public void setVertical(boolean enable) {
		if (this.flagVertText != enable) {
			this.flagVertText = enable;
			fireTextUpdateEvent();
		}
	}

	public boolean isVertical() {
		return flagVertText;
	}	

	public void setText(String text) {
		//if (!imageString.getString().equals(text)) {
			imageString.setString(text);
			if (getEnvironment()!=null) {
				imageString.loadImage(getEnvironment().getImageTracker());
			}
			fireTextUpdateEvent();
		//}
	}
	
	public String getText() {
		return imageString.toString();
	}
	
	public boolean isImageString() {
		return imageString.getImage()!=null;
	}
	
	private String lastString = "";
	private boolean lastNoImage=true;

	protected void autoResize() {
		if (flagAutoResize) {
			
			boolean hasNoImage = imageString.getImage()==null;
			
			if (	(lastNoImage!=hasNoImage)
				||  (lastString!=imageString.getString()) )
			{
				/**
				 * Comparing strings by reference is ok, since
				 * we have never another source
				 */

				lastNoImage=hasNoImage;
				lastString=imageString.getString();				
				Dimension d;
				
				if (hasNoImage) {
					d = getTextDimensions();
				} else {
					d =imageString.getImageBounds(this).getSize();
					if (d.width==-1||d.height==-1) {
						// do some trick if image is not loaded
						(new ImageIcon(imageString.getImage())).getIconWidth();
						d =imageString.getImageBounds(this).getSize();
					}
				}
				setMinimumSize(d);
				setMaximumSize(d);
				setPreferredSize(d);
				setSize(d);

			}
		}
	}
	
	public boolean isAutoResizeEnabled() {
		return flagAutoResize;
	}
	
	public void setAutoResize(boolean enabled) {
		if (flagAutoResize!=enabled) {
			flagAutoResize=enabled;
			if (flagAutoResize)
				fireTextUpdateEvent();
		}
	}
	
	/**
	 * TODO replace with property event integration
	 */
	protected void fireTextUpdateEvent() {
		autoResize();
		repaint();
	}
	
	public void paintDecoration(Graphics2D g2) {
		if (flagTextAntialiasing) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		g2.setFont(getFont());
		g2.setColor(getForeground());

		if (imageString.getImage()!=null) {
			
			g2.drawImage(imageString.getImage(),0,0,this);
			
		} else {
			if (isVertical()) {
				FontMetrics fm = getFontMetrics(getFont());
				for (int i=0;i<imageString.getString().length();i++)
					g2.drawString(Character.toString(imageString.getString().charAt(i)), 0, (fm.getHeight()+1)*(i+1)-1);
			} else {
				g2.drawString(imageString.getString(), sx, sy);
			}
		}
	}
	
	private class AntialiasTextProperty extends BooleanProperty {

		public AntialiasTextProperty(NomadComponent component) {
			super(component);
			setName("antialiasing");
		}

		public void setBooleanValue(boolean value) {
			setTextAntialiased(value);
		}

		public boolean getBoolean() {
			return ((NomadLabel)getComponent()).isTextAntialiased();
		}
		
	}

	private class LabelTextProperty extends Property {

		public LabelTextProperty(NomadComponent component) {
			super(component);
			setName("text");
		}

		public Object getValue() {
			return ((NomadLabel)getComponent()).getText();
		}

		public void setValueFromString(String value) {
			setText(value);
		}
	}
	
	private class VerticalTextProperty extends BooleanProperty {
		public VerticalTextProperty(NomadComponent component) {
			super(component);
			setName("vertical");
		}

		public void setBooleanValue(boolean value) {
			setVertical(value);
		}

		public boolean getBoolean() {
			return ((NomadLabel)getComponent()).isVertical();
		}
	}
	
	private class LabelFontProperty extends FontProperty {

		public LabelFontProperty(NomadComponent component) {
			super(component);
		}
		public Object getValue() { return getComponent().getFont(); }
		public Font getFont() { return getComponent().getFont(); }
		public void setFont(Font f) {
			getComponent().setFont(f);
			fireTextUpdateEvent();
		}
	}
	
}
