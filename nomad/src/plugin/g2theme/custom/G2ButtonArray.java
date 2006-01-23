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
 * Created on Jan 22, 2006
 */
package plugin.g2theme.custom;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.nomad.theme.component.NomadButtonArray;


public class G2ButtonArray extends NomadButtonArray {

	private final static MouseListener mouseOverUpdater
		= new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				set(event, true);
			}

			public void mouseExited(MouseEvent event) {
				set(event, false);
			}
			
			public void set(MouseEvent event, boolean mouseOver) {
				((G2ButtonArray)event.getComponent())
					.setMouseOver(mouseOver);
			}
	};
	
	public G2ButtonArray() {
		setSelectionBorder(G2BorderFactory.createG2ButtonBorder());
		setDefaultBorder(G2BorderFactory.createG2ButtonBorder());
		setBackground(G2ColorConstants.BUTTON_BACKGROUND);
		addMouseListener(mouseOverUpdater);
		setButtonFocusedColor(G2ColorConstants.BUTTON_FOCUSED_BACKGROUND);
		setButtonSelectedColor(G2ColorConstants.BUTTON_SELECTED_BACKGROUND);
	}
	
	private boolean flagMouseOver = false;

	protected void setMouseOver(boolean mouseOver) {
		if (this.flagMouseOver != mouseOver) {
			flagMouseOver = mouseOver;
			repaint();
		}
	}

	public boolean isMouseOver() {
		return flagMouseOver;
	}
	
}
