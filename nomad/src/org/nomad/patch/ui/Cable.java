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
 * Created on Feb 4, 2006
 */
package org.nomad.patch.ui;

import java.awt.Color;

import org.nomad.patch.Connector;
import org.nomad.patch.Header;
import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.component.NomadConnector;
import org.nomad.util.array.Transition;

public class Cable extends Curve implements Transition<Connector> {

	private Connector c1;
	private Connector c2;
	private int colorCode = Header.CABLE_WHITE;
	private CablePanel cablePanel;
	
	public Cable(Connector c1, Connector c2) {
		super();
		
		if (c1==null||c2==null) throw new NullPointerException("Connectors can not be null.");
		
		this.c1 = c1;
		this.c2 = c2;
	}
	
	void setCablePanel(CablePanel cablePanel) {
		this.cablePanel = cablePanel;
	}
	
	CablePanel getCablePanel() {
		return cablePanel;
	}
	
	protected void update() { 
		if (cablePanel!=null)
			cablePanel.update(this);
	}
	
	public void setColor(int colorCode) {
		setColor(getColorByColorCode(this.colorCode=colorCode));
	}
	
	public final static Color getColorByColorCode(int colorCode) {
		switch (colorCode) {
			case Header.CABLE_RED: 		return NomadClassicColors.MORPH_RED;
			case Header.CABLE_BLUE: 	return NomadClassicColors.MORPH_BLUE;
			case Header.CABLE_YELLOW: 	return NomadClassicColors.MORPH_YELLOW;
			case Header.CABLE_GRAY: 	return NomadClassicColors.MORPH_GRAY;
			case Header.CABLE_GREEN: 	return Color.GREEN;
			case Header.CABLE_PURPLE: 	return Color.PINK;
			case Header.CABLE_WHITE: 	return Color.WHITE;
			default: return Color.WHITE;
		}
	}
	
	public int getColorCode() {
		return colorCode;
	}

	public Cable(NomadConnector start, NomadConnector stop) {
		this(start.getConnector(), stop.getConnector());
	}
	
	public Connector getC1() { return c1; }
	public Connector getC2() { return c2; }

	public Connector getN1() { return c1; }
	public Connector getN2() { return c2; }

}
