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

import org.nomad.patch.CableColor;
import org.nomad.patch.Connector;
import org.nomad.theme.component.NomadConnector;
import org.nomad.util.array.Transition;

public class Cable extends Curve implements Transition<Connector> {

	private Connector c1;
	private Connector c2;
	private CableColor colorCode = CableColor.WHITE;
	private CableDisplay cablePanel;
	
	public Cable(Connector c1, Connector c2) {
		super();
		
		if (c1==null||c2==null) throw new NullPointerException("Connectors can not be null.");
		
		this.c1 = c1;
		this.c2 = c2;
	}
	
	void setCablePanel(CableDisplay cablePanel) {
		this.cablePanel = cablePanel;
	}
	
	CableDisplay getCablePanel() {
		return cablePanel;
	}
	
	protected void update() { 
		if (cablePanel!=null)
			cablePanel.update(this);
	}
	
	public void setColor(CableColor color) {
		colorCode = color;
		setColor(color.getColor());
	}

	public CableColor getColorCode() {
		return colorCode;
	}

	public Cable(NomadConnector start, NomadConnector stop) {
		this(start.getConnector(), stop.getConnector());
	}
	
	public Connector getC1() { return c1; }
	public Connector getC2() { return c2; }

	public Connector getN1() { return c1; }
	public Connector getN2() { return c2; }

	public void swapConnectors() {
		Connector c = c1;
		c1 = c2;
		c2 = c;
		swapPoints();
	}

}
