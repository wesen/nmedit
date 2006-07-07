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
 * Created on Jan 6, 2006
 */
package net.sf.nmedit.nomad.theme.component;

import java.awt.Dimension;

import net.sf.nmedit.nomad.theme.laf.Knob;
import net.sf.nmedit.nomad.theme.laf.KnobUI;

/**
 * @author Christian Schneider
 */
public class NomadClassicKnob extends Knob {

	public NomadClassicKnob() 
    {
		Dimension d = new Dimension(24,24);
        setOpaque(false);
		setDefaultSize(d);
		setSize(d);
        setUI(createUI());
	}

    protected KnobUI createUI()
    {
        return (KnobUI) KnobUI.createUI(this);
    }

}
