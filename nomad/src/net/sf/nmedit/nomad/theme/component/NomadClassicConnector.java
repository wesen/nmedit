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
package net.sf.nmedit.nomad.theme.component;

import java.awt.Graphics;
import java.awt.Graphics2D;

import net.sf.nmedit.nomad.theme.component.model.ConnectorPaintManager;
import net.sf.nmedit.nomad.theme.component.model.ConnectorPainter;
import net.sf.nmedit.nomad.theme.component.model.ConnectorPaintManager.CachedConnectorGraphics;

public class NomadClassicConnector extends NomadConnector {

	protected final static ConnectorPaintManager paintManager = new ConnectorPaintManager();
	private CachedConnectorGraphics graphics = null;
	private static boolean gradients = true;
	
	public NomadClassicConnector() {
		super();
		setConnectorType(false);
		setConnectedState(false);
		//setBackground(NomadClassicColors.MORPH_YELLOW);
		setGraphicsGradient(true);
	}
	
	protected static void setGraphicsGradient(boolean enable) {
		if (gradients!=enable) {
			gradients = enable;
			paintManager.setConnectorPainter(new ConnectorPainter(gradients));
		}
	}
	
	public void paintComponent(Graphics g) {
		/* We have our custom double buffer and need no background buffer,
		 * so we override paintComponent.
		 */
		
		graphics=paintManager.obtainGraphics(this, graphics);
		graphics.paint(this, (Graphics2D) g);
	}

	/*
	public void paintDecoration(Graphics2D g2) {
		// nothing to do here
	}
	
	public void paintDynamicOverlay(Graphics2D g2) {
		// nothing to do here
	} */
	
	protected void finalize() throws Throwable {
		if (graphics!=null)
			graphics.unregister();
		super.finalize();
	}
	
}
