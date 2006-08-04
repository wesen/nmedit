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
package net.sf.nmedit.nomad.editor.views.classes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

import net.sf.nmedit.nomad.main.ui.DashBorder;

/**
 * List item that shows a component a long with the components name.
 * 
 * @author Christian Schneider
 */
class ComponentClassListItem extends JPanel {

	private JComponent component = null;
	public final static int PADDING = 4; // px
	private boolean flagItemIsSelected = false;
	private boolean flagItemHasFocus = false;
	private Color bgColor = Color.WHITE;
	private Color selColor = Color.YELLOW;

    private static Border border
    = BorderFactory.createCompoundBorder(
      DashBorder.create(false, false, false, true),
      BorderFactory.createEmptyBorder(2, 2, 2, 2)
    );
    
	public ComponentClassListItem(JComponent component) {
		
		Color c;
		c=UIManager.getColor("List.selectionBackground");
		if (c!=null) selColor=c;
		
		c = UIManager.getColor("List.background");
		if (c!=null) bgColor=c;
		
        setBorder(border);
		setBackground(bgColor);
		
		// check+store argument
		if (component==null) throw new NullPointerException("Component must not be null");
		this.component = component;

		// adding components+configure layout
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// component 
		component.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        Dimension d = component.getPreferredSize();
        if (d.width<=0 || d.height<= 0) d.setSize(20, 20);
        
        component.setMinimumSize(d);
        component.setMaximumSize(d);
        component.setPreferredSize(d);
        
		//component.setSize(component.getPreferredSize());

		// label
		JLabel lblCompName = new JLabel("("+getComponentClassName()+")") {
			
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				super.paintComponent(g);
			}
			
		};
		lblCompName.setFont(new Font("SansSerif", Font.PLAIN, 9));
		lblCompName.setForeground(Color.DARK_GRAY);
		lblCompName.setAlignmentX(Component.CENTER_ALIGNMENT);

		add(createSpace());
		add(component);
		add(createSpace());
		add(lblCompName);
		
		//validate();
	}
	
	public void setItemIsSelected(boolean selected) {
		if (flagItemIsSelected!=selected) {
			flagItemIsSelected=selected;
			if (flagItemIsSelected) {
				setBackground(selColor);
			} else {
				setBackground(bgColor);
			}
			repaint();
		}
	}
	
	public void setItemHasFocus(boolean focused) {
		if (flagItemHasFocus!=focused) {
			flagItemHasFocus=focused;
			// no visualisation yet
		}
	}
	public boolean getItemIsSelected() {
		return flagItemIsSelected;
	}
	
	public boolean getItemHasFocus() {
		return flagItemHasFocus;
	}

	protected JComponent createSpace() {
		JPanel space = new JPanel();
		Dimension sp = new Dimension(PADDING,PADDING);
		space.setMinimumSize(sp);
		space.setMaximumSize(sp);
		space.setPreferredSize(sp);
		space.setSize(sp);
		space.setOpaque(false);
		
		return space;
	}

	public Class getComponentClass() {
		return component.getClass();
	}
	
	public String getComponentClassName() {
		String name = getComponentClass().getName();
		int index = name.lastIndexOf('.');
		if (index<0||name.length()<=index)
			return name;
		else
			return name.substring(index+1);
	}
	
	/*
	private class CenteredComponent extends JPanel {

		public CenteredComponent(Component componentToCenter, boolean isSelected, boolean cellHasFocus) {
			setOpaque(true);
			setLayout(new BorderLayout());

			JLabel label = new NiceLabel(getDescription(componentToCenter));
			label.setFont(new Font("SansSerif", Font.PLAIN, 9));
			
			JPanel compContainer = new JPanel();
			compContainer.setOpaque(true);
			compContainer.setBackground(Color.LIGHT_GRAY);
			compContainer.add(componentToCenter);
			
			JPanel labelContainer = new BottomDashPane(isSelected, cellHasFocus);
			labelContainer.add(label);
			
			add(compContainer, BorderLayout.CENTER);
			add(labelContainer,BorderLayout.SOUTH);
			setMinimumSize(componentToCenter.getSize());
		}
		
		private String getDescription(Component component) {
			String className = component.getClass().getName();
			String[] piecewise = className.trim().split("\\.");
			return " ("+(piecewise.length==0?"unknown":piecewise[piecewise.length-1])+")";
		}
		
		class NiceLabel extends JLabel {
		    public NiceLabel(String text) {
		        super(text);
		    }
		    public void paintComponent(Graphics g) {
		        Graphics2D g2 = (Graphics2D)g;
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        super.paintComponent(g);
		    }
		}
		
	}
	
	private class BottomDashPane extends JPanel {

		boolean isSelected = false;
		boolean cellHasFocus = false;
		
		public BottomDashPane(boolean isSelected, boolean cellHasFocus) {
			setOpaque(true);
			this.isSelected = isSelected;
			this.cellHasFocus = cellHasFocus;
			setBackground(isSelected?Color.YELLOW:Color.WHITE);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g.create();

			// state
			g2.setColor(getBackground());
			g2.fill(new Rectangle(getSize()));
			
			// bottom dash line
			g2.setColor(cellHasFocus?Color.BLUE:Color.BLACK);
			
			//Stroke stroke = g2.getStroke();
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2.setStroke(new BasicStroke( 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{ 1, 2 }, 0 ));
			g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
			//g2.setStroke(stroke);
			
			g2.dispose();
		}
	}*/
	
}
