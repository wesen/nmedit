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
package org.nomad.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Christian Schneider
 */
public class JFontChooser extends JComponent {

	private Font font = null;
	private JTextField txtPreview = new JTextField();
	private JList liFont = null;
	private JList liStyle = null;
	private JList liSize = null;
	private Integer[] sizes = new Integer[24-8+1];

	public JFontChooser() {
		setLayout(new BorderLayout());

		JPanel panTop = new JPanel();
		panTop.setLayout(new GridLayout(1,3));
		panTop.add(new JLabel("Fontname:"));
		panTop.add(new JLabel("Fontstyle:"));
		panTop.add(new JLabel("Fontsize:"));		

		JPanel panCenter = new JPanel();
		panCenter.setLayout(new GridLayout(1,3));
		panCenter.add(new JScrollPane(liFont=new JList(JFontChooser.getAvailableFontNames())));
		panCenter.add(new JScrollPane(liStyle=new JList(FontStyle.styles)));

		for (int i=8;i<=24;i++) sizes[i-8]=new Integer(i);
		panCenter.add(new JScrollPane(liSize= new JList(sizes)));

		add(panTop, BorderLayout.NORTH);
		add(panCenter, BorderLayout.CENTER);
		add(txtPreview, BorderLayout.SOUTH);

		liFont.setCellRenderer(new DefaultListCellRenderer(){
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				c.setFont(new Font((String) value, Font.PLAIN, 13));
				return c;
			}});
		
		liFont.setSelectedValue("SansSerif", true); // select sans-serif
		liStyle.setSelectedValue(new FontStyle(), true); // select plain font
		liSize.setSelectedValue(new Integer(10), true); // select size 10
		
		ListSelectionListener selectionListener = new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				loadSelectedFont();
			}};

		liFont.addListSelectionListener(selectionListener);
		liStyle.addListSelectionListener(selectionListener);
		liSize.addListSelectionListener(selectionListener);

		txtPreview.setEditable(false);
		
		loadSelectedFont();
		revalidate();
	}
	
	public JFontChooser(Font font) {
		this();
		setFont(font);
	}

	private void loadSelectedFont() {
		String fName = (String) liFont.getSelectedValue();
		FontStyle fs = (FontStyle) liStyle.getSelectedValue();
		Integer fz = (Integer) liSize.getSelectedValue();
		font = new Font(fName, fs.style, fz.intValue());
		updateTextPreview();
	}
	
	public void setFont(Font font) {
		this.font = font;
		liFont.setSelectedValue(font.getName(), true);
		liStyle.setSelectedValue(new FontStyle(font.getStyle()), true);
		liSize.setSelectedValue(new Integer(font.getSize()), true);
		updateTextPreview();
	}
	
	protected void updateTextPreview() {
		txtPreview.setFont(font);
		txtPreview.setText(font.getName()+", "+new FontStyle(font.getStyle())+", "+font.getSize());
		//txtPreview.setSize(txtPreview.getPreferredSize());
		revalidate();
	}

	public static String stringEncodeFont(Font f) {
		return null;
	}

	public Font getFont() {
		return font;
	}
	
	public static String[] getAvailableFontNames() {
	     return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}
	
	private static class FontStyle {
		public final static FontStyle[] styles = new FontStyle[] {
			new FontStyle(), new FontStyle(Font.ITALIC), new FontStyle(Font.BOLD), new FontStyle(Font.ITALIC|Font.BOLD)
		};

		public int style;
		
		public FontStyle() {
			this(0);
		}
		
		public FontStyle(int style) {
			this.style = style;
		}

		public boolean isItalicSet() {
			return (style & Font.ITALIC) != 0;
		}
		
		public boolean isBoldSet() {
			return (style & Font.BOLD) != 0;
		}
		
		public boolean isPlainSet() {
			return !isItalicSet() && !isBoldSet();
		}
		
		public String toString() {
			if (isItalicSet() && isBoldSet()) return "bold, italic";
			else if (isItalicSet()) return "italic";
			else if (isBoldSet()) return "bold";
			else return "plain";
			
		}
		
		public boolean equals(Object obj) {
			return obj instanceof FontStyle && ((FontStyle)obj).style==style;
		}
		
	}
	
}
