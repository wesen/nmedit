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
 * Created on Jan 8, 2006
 */
package org.nomad.theme.property;

import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;

import org.nomad.dialog.JFontChooser;
import org.nomad.theme.component.NomadComponent;
import org.nomad.theme.property.editor.PropertyEditor;

/**
 * @author Christian Schneider
 */
public abstract class FontProperty extends Property {

	private final static String defaultFontString =
		getFontString(new Font("SansSerif", Font.PLAIN, 9));
	
	/**
	 * @param component
	 */
	public FontProperty(NomadComponent component) {
		super(component);
		setName("font");
		setIsInlineEditor(false);
	}

	/**
	 * i=italic
	 * b=bold
	 * Example: 8,i,Arial means Font Arial , size 8 , bold
	 */
	private final static Pattern p = Pattern.compile("(\\d+),(([ibIB]),)?(([ibIB]),)?([^,]+)");
	
	public static boolean isValidFontString(String fString) {
		return p.matcher(fString).matches();
	}
	
	public boolean isInDefaultState() {
		return defaultFontString.equals(getValue());
	}
	
	public static Font parseFont(String fString) {
		Matcher m = p.matcher(fString);
		if (!m.matches()) {
			return null;
		} else {
			int size = Integer.parseInt(m.group(1));
			String fontName = m.group(m.groupCount()).trim();
			boolean i = false;
			boolean b = false;
			for (int j=0;j<=m.groupCount();j++) {
				String g = m.group(j);
				if (g!=null) {
					g = g.toLowerCase();
					if (g.equals("b")) b=true;
					else if (g.equals("i")) i=true;
				}
			}
			int style = (i?Font.ITALIC:0)|(b?Font.BOLD:0);
			return new Font(fontName, style, size);
		}
	}

	public static String getFontString(Font f) {
		return f.getSize()+","+ (f.isBold()?"b,":"")+(f.isItalic()?"i,":"")+f.getName();
	}
	
	public abstract Font getFont();
	public abstract void setFont(Font f);

	public String getValue() {
		return getFontString(getFont());
	}

	public void setValue(String value) {
		Font f = parseFont(value);
		if (f==null) throw new IllegalArgumentException("Invalid Font String in "+this+" :"+value);
		setFont(f);
	}

	
	public PropertyEditor getEditor() {
		return new FontEditor(this);
	}

	private static class FontEditor extends PropertyEditor {

		private JFontChooser fontChooser = null;
		
		public FontEditor(Property p) {
			super(p);
		}

		public String getEditorValue() {		
			return fontChooser==null?null: getFontString(fontChooser.getFont());
		}
		
		public Font getFont() {
			return this.getFont();
		}

		public JComponent getEditorComponent() {
			return fontChooser= new JFontChooser(((FontProperty) getProperty()).getFont());
		}
		
	}
}
