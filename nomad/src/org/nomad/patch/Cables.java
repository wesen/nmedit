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
 * Created on Feb 2, 2006
 */
package org.nomad.patch;

import java.util.ArrayList;

import org.nomad.patch.ui.Cable;
import org.nomad.util.array.TransitionChangeListener;
import org.nomad.util.array.TransitionMatrix;
import org.nomad.xml.dom.module.DConnector;

public class Cables extends TransitionMatrix<Connector, Cable> {
	
	public Cables() {
		addChangeListener(new Colorizer());
	}
	
	protected Cable[] newArray(int size) {
		return new Cable[size];
	}

	public boolean canHaveTransition(Connector c1, Connector c2) {
		if (c1==c2) return false;
		if (hasTransition(c1, c2)) return false;

		return getOutput(c1)==null || getOutput(c2)==null; // at least one output has to be null
	}

	public Cable addTransition(Connector c1, Connector c2) {
		if (canHaveTransition(c1, c2)) {
			Cable c = new Cable(c1,c2);
			super.addTransition(c);
			return c;
		}
		return null;
	}

	public void addTransition(Cable t) {
		if (canHaveTransition(t.getC1(),t.getC2())) {
			super.addTransition(t);
		}
	}
	
	public Connector getOutput(Connector c) {
		if (c.getInfo()==null) return null;
		if (c.getInfo().isOutput()) return c;
		for (Connector candidate : getLinked(c)) {
			if (candidate.getInfo().isOutput()) return candidate;
		}
		return null;
	}

	public void setCableColor(Connector connector, int color) {
		for (Cable curve : getLinkedT(connector)) 
			curve.setColor(color);
	}
	
	private class Colorizer implements TransitionChangeListener<Cable> {
		public void transitionChanged(Cable curve, boolean transition_added) {
			Connector a = curve.getC1();
			Connector b = curve.getC2();
			

			int color = Header.CABLE_WHITE;
			if (transition_added) {
				Connector out = getOutput(a);
				if (out!=null) {
					color = getColor(out.getInfo());
					setCableColor(b, color);
				} else {
					out = getOutput(b);
					if (out!=null) {
						color = getColor(out.getInfo());
						setCableColor(a, color);
					}
				}
				
				curve.setColor(color);
				
				if (!a.isConnected()) a.setConnected(true);
				if (!b.isConnected()) b.setConnected(true);			
			} else {
				
				// TODO undo coloring
				
				Connector removeColors = null;
				if (getOutput(a) == null) removeColors = a;
				else
				if (getOutput(b) == null) removeColors = b;
				
				if (removeColors!=null) { // should always be true
					setCableColor(removeColors, Header.CABLE_WHITE);
				}
				
				if (!hasTransition(a)) a.setConnected(false);
				if (!hasTransition(b)) b.setConnected(false);
			}
		}
	}
	
	private int getColor(DConnector connector) {
		switch (connector.getSignal()) {
			case DConnector.SIGNAL_AUDIO:
				return Header.CABLE_RED;
			case DConnector.SIGNAL_CONTROL:
				return Header.CABLE_BLUE;
			case DConnector.SIGNAL_LOGIC:
				return Header.CABLE_YELLOW;
			case DConnector.SIGNAL_SLAVE:
				return Header.CABLE_GRAY;
			default:
				return Header.CABLE_WHITE;
		}
	}

	public int determineColor(Connector c1, Connector c2) 
	{
		if (c1!=null) c1 = getOutput(c1);
		if (c1!=null) return getColor(c1.getInfo());
		if (c2!=null) c2 = getOutput(c2);
		if (c2!=null) return getColor(c2.getInfo());
		return Header.CABLE_WHITE;
	}

	public void remove(ArrayList<Cable> transitions) {
		for (Cable c : transitions) {
			removeTransition(c);
		}
	}

}
