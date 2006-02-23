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
 * Created on Feb 13, 2006
 */
package org.nomad.patch;

import org.nomad.xml.dom.module.DCustom;

public class Custom {

	private DCustom info;
	private int value;
	private Module module;

	public Module getModule() {
		return module;
	}

	public Custom(DCustom info, Module module) {
		this.module = module;
		this.info = info;
	}

	public DCustom getInfo() {
		return info;
	}

	public void setValue(int value) {
		if (this.value!=value) {
			getModule().fireCustomChangeEvent(this);
			this.value = value;
		}
	}
	
	public int getValue() {
		return value;
	}
	
}
