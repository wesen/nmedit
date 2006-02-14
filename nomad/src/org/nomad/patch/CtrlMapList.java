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

import java.util.Arrays;

public class CtrlMapList { 

	private CtrlMap[] ctrlMapList ;
	
	public CtrlMapList() {
		ctrlMapList = new CtrlMap[121];
		Arrays.fill(ctrlMapList, null);
		for (int i=0;i<=33;i++)
			ctrlMapList[i] = new CtrlMap(i);
		for (int i=33;i<=120;i++)
			ctrlMapList[i] = new CtrlMap(i);
	}

	protected boolean inBounds(int cc) {
		return (cc>=0) && (cc<ctrlMapList.length);
	}
	
	public CtrlMap getCtrl(int cc) {
		return inBounds(cc) ? ctrlMapList[cc] : null;
	}
	
}
