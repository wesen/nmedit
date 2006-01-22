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
 * Created on Jan 19, 2006
 */
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.Border;

import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.component.model.NomadButtonArrayBehaviour;
import org.nomad.theme.component.model.NomadButtonArrayModel;

public class NomadVocoderController extends NomadComponent implements NomadButtonArrayModel {

	private VocoderBandDisplay display = null;
	private NomadButtonArrayBehaviour behaviour = null;
	private String[] label = new String[] {"-2","-1","0","+1","+2","Inv","Rnd"};
	private int cs=0;
	private Dimension maxStringBound = null;
	
	public NomadVocoderController() {
		super();
		setDynamicOverlay(true);

		setFont(new Font("SansSerif", Font.PLAIN, 9));
		FontMetrics fm = getFontMetrics(getFont());
		cs = fm.getHeight();
		maxStringBound = new Dimension(cs*2,cs);
		
		setOpaque(true);
		setBackground(NomadClassicColors.MODULE_BACKGROUND);
		
		setDynamicOverlay(true);
		
		behaviour = new NomadButtonArrayBehaviour(this);
		behaviour.calculateMetrics();
		Dimension d = behaviour.getPreferredSize();
		setMinimumSize(d);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event) {
				if (display!=null) {
					behaviour.calculateMetrics();
					int index = behaviour.getButtonIndexAt(event.getPoint());
					switch (index) {
						case 0: display.setBands(-2); break;
						case 1: display.setBands(-1); break;
						case 2: display.setBands( 0); break;
						case 3: display.setBands(+1); break;
						case 4: display.setBands(+2); break;
						case 5: display.setBandsInv(); break;
						case 6: display.setBandsRnd(); break;
					}
				}
			}});
		
		addHierarchyListener(new HierarchyListener() {

			public void hierarchyChanged(HierarchyEvent event) {
				if (getParent()!=null) {
					/*
					if (locate()) {
						System.out.println("what");
						NomadVocoderController.this.removeHierarchyListener(this);
					}*/
					NomadVocoderController.this.removeHierarchyListener(this);
					if (!locate()) {
						getParent().addContainerListener(new ContainerAdapter() {
							public void componentAdded(ContainerEvent event) {
								System.out.println(" "+display+" "+event.getChild());
								if (event.getChild() instanceof VocoderBandDisplay) {
									getParent().removeContainerListener(this);
									// found
									setDisplay((VocoderBandDisplay ) event.getChild());
								}
							}
							});
						
					}
				}			
			}});
		
	}
	
	private Border border = NomadBorderFactory.createNordEditor311RaisedButtonBorder();
	
	public void paintDynamicOverlay(Graphics2D g2) {
		behaviour.calculateMetrics();

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setFont(getFont());
		
		FontMetrics fm = g2.getFontMetrics();
		
		g2.setColor(Color.BLACK);
		Point cell ;
		for (int i=0;i<getButtonCount();i++) {
			cell = behaviour.getCell(i);
			Rectangle b = fm.getStringBounds(label[i], g2).getBounds();
			g2.drawString(label[i], cell.x+(behaviour.getCellWidth()-b.width)/2, cell.y+cs);
			
			border.paintBorder(this, g2, cell.x, cell.y, behaviour.getCellWidth()+(i==getButtonCount()-1?-1:0), behaviour.getCellHeight()-1);
			//g2.drawRect(cell.x, cell.y, behaviour.getCellWidth()+(i==getButtonCount()-1?-1:0), behaviour.getCellHeight()-1);
		}
	}

	public int getButtonCount() {
		return 7;
	}

	public boolean isLandscape() {
		return true;
	}

	public Dimension getPreferredCellSize(int index) {
		return new Dimension(maxStringBound);
	}
	

	public void setDisplay(VocoderBandDisplay display) {
		this.display = display;
	}

	private boolean locate() {
		if (getParent()!=null) {
			Component[] components = getParent().getComponents();
			for (int i=components.length-1;i>=0;i--)
				if (components[i] instanceof VocoderBandDisplay) {
					setDisplay((VocoderBandDisplay)components[i]);
					return true;
				}
		}
		return false;
	}

}
