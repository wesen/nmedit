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
 * Created on Jan 11, 2006
 */
package org.nomad.theme.component;

import java.awt.Graphics;
import java.awt.Graphics2D;

import org.nomad.theme.component.model.NomadClassicConnectorGraphics;
import org.nomad.theme.component.model.NomadClassicConnectorGraphics.ConnectorCachedGraphics;

public class NomadClassicConnector extends NomadConnector {

	private ConnectorCachedGraphics connectorGraphics = null;
	
	public NomadClassicConnector() {
		super();
		setConnectorType(false);
		setConnectedState(false);
		//setBackground(NomadClassicColors.MORPH_YELLOW);
	}
/*
	protected void createProperties(PropertySet set) {
		super.createProperties(set);
		//getAccessibleProperties().rewriteDefaults();
	}*/

	public void paintComponent(Graphics g) {
		/* We have our custom double buffer and need no background buffer,
		 * so we override paintComponent.
		 */
		connectorGraphics = NomadClassicConnectorGraphics
			.obtainGraphicsCache(this, connectorGraphics);
		connectorGraphics.paint(this, (Graphics2D) g);
	}

	/*
	public void paintDecoration(Graphics2D g2) {
		// nothing to do here
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {
		// nothing to do here
	} */
	
	protected void finalize() throws Throwable {
		if (connectorGraphics!=null)
			connectorGraphics.dispose();
		super.finalize();
	}
	
}
