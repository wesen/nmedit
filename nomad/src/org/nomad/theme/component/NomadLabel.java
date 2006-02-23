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
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import org.nomad.env.Environment;
import org.nomad.theme.property.BooleanProperty;
import org.nomad.theme.property.FontProperty;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.util.misc.ImageString;

/**
 * @author Christian Schneider
 */
public class NomadLabel extends NomadComponent {

	private String text = null;
	private String ikey = null;
	private Image image = null;
	private boolean iconSupport = true;
	private boolean flagVertText = false;
	private boolean flagTextAntialiasing = true;
	private boolean flagAutoResize = true;
	private Dimension contentSize = new Dimension(0, 0);
	private int ty = 0;

	private final static Font defaultLabelFont = new Font("SansSerif", Font.PLAIN, 9);
	
	public NomadLabel() {
		super();
		setFont(defaultLabelFont);
		setOpaque(false);
		setForeground(Color.BLACK);
		setText("label");
	}

	public Dimension getContentSize() {
		return contentSize ;
	}
	
	public void setContentSize(Dimension size) {
		contentSize = new Dimension(size);
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
	
	protected void tryLoadIcon() {
		if (iconSupport) {
			ikey = ImageString.extractKeyFromImageString(text);
			if (ikey!=null) {
				image = Environment.sharedInstance().getImageTracker().getImage(ikey);
			}
		}
	}
	
	public void setText(String text) {
		if (this.text==text) return;
		if (this.text!=null && text!=null && this.text.equals(text)) return;
		this.text = text;
		tryLoadIcon();
		fireTextUpdateEvent();
	}
	
	public void setIconSupportEnabled(boolean enable) {
		//if (iconSupport!=enable) {
			iconSupport = enable;
		//	if (enable)
		//		tryLoadIcon();
		//	fireTextUpdateEvent();
		//}
	}
	
	protected void recalculateSize() {
		if (iconSupport && image!=null) {
			contentSize.setSize(image.getWidth(null), image.getHeight(null));
		}
		
		FontMetrics fm = getFontMetrics(getFont());
		
		if (isVertical()) {
			contentSize.setSize(fm.getMaxAdvance(), (fm.getHeight()+1)*text.length());
			ty = fm.getHeight();
		} else {
			contentSize.setSize(fm.stringWidth(text), fm.getHeight());
			ty = contentSize.height-fm.getDescent();
		}
		
		useContentSize();
	}

	protected void useContentSize() {
		if (flagAutoResize) {
			setMinimumSize(contentSize);
			setMaximumSize(contentSize);
			setPreferredSize(contentSize);
			setSize(contentSize);
		}
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
	
	// TODO replace with property event integration
	protected void fireTextUpdateEvent() {
		recalculateSize();
		fullRepaint();
	}
	
	public String getText() {
		return text;
	}

	public void registerProperties(PropertySet set) {
		super.registerProperties(set);
		set.add(new LabelTextProperty());
		set.add(new VerticalTextProperty());
		set.add(new LabelFontProperty());
		set.add(new AntialiasTextProperty());
	}
	
	private static class AntialiasTextProperty extends BooleanProperty {

		public AntialiasTextProperty() {
			setName("antialiasing");
		}

		public void setBoolean(boolean value) {
			((NomadLabel)getComponent()).setTextAntialiased(value);
		}

		public boolean getBoolean() {
			return ((NomadLabel)getComponent()).isTextAntialiased();
		}
		
	}

	private static class LabelTextProperty extends Property {

		public LabelTextProperty() {
			setName("text");
		}

		public String getValue() {
			return ((NomadLabel)getComponent()).getText();
		}

		public void setValue(String value) {
			((NomadLabel)getComponent()).setText(value);
		}
	}
	
	private static class VerticalTextProperty extends BooleanProperty {
		public VerticalTextProperty() {
			setName("vertical");
		}

		public void setBoolean(boolean value) {
			((NomadLabel)getComponent()).setVertical(value);
		}

		public boolean getBoolean() {
			return ((NomadLabel)getComponent()).isVertical();
		}
	}
	
	private static class LabelFontProperty extends FontProperty {

		public LabelFontProperty() { }
		public Font getFont() { return getComponent().getFont(); }
		public void setFont(Font f) {
			getComponent().setFont(f);
			((NomadLabel)getComponent()).fireTextUpdateEvent();
		}
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
	
	public void paintDecoration(Graphics2D g2) {
		if (image!=null) {
			g2.drawImage(image, 0, 0, this);
		} else {
			if (flagTextAntialiasing) {
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}
			
			g2.setFont(getFont());
			g2.setColor(getForeground());
			
			if (isVertical()) {
				FontMetrics fm = getFontMetrics(getFont());
				for (int i=0;i<text.length();i++)
					g2.drawString(Character.toString(text.charAt(i)), 0, (fm.getHeight()+1)*(i+1)-1);
			} else {
				g2.drawString(text, 0, ty);
			}
		}
	}
	
	/*
	
	private ImageString imageString = new ImageString("label");
	private int sx = 0;
	private int sy = 0;
	private Dimension textDim = null;
	
	

	private Dimension getTextDimensionsX() {
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
	
	protected Dimension getTextDimensions() {
		if (textDim == null)
			textDim = getTextDimensionsX();
		return new Dimension(textDim);
	}

	public void setText(String text) {
		
		textDim = null;
		
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
				// Comparing strings by reference is ok, since
				// we have never another source

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
	
	public void paintDecoration(Graphics2D g2) {
		if (flagTextAntialiasing) {
			//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
	
	*/
}
