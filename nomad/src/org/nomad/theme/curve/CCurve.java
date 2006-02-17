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
package org.nomad.theme.curve;

import org.nomad.patch.Connector;
import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.theme.component.NomadConnector;
import org.nomad.util.array.Transition;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.DModule;

public class CCurve extends Curve implements Transition<Connector> {

	private Connector c1;
	private Connector c2;
	
	public CCurve(Connector c1, Connector c2) {
		super();
		
		if (c1==null||c2==null) throw new NullPointerException("Connectors can not be null.");
		
		this.c1 = c1;
		this.c2 = c2;
	}

	public CCurve(NomadConnector start, NomadConnector stop) {
		this(start.getConnector(), stop.getConnector());
	}
	
	public Connector getC1() { return c1; }
	public Connector getC2() { return c2; }

	public Connector getN1() { return c1; }
	public Connector getN2() { return c2; }

	public static CCurve parse(ModuleSection msection, String params) {
		
		final int isInput = 0;
		
        String[] paramArray = params.split("\\s");
        // int color 	 = Integer.parseInt(paramArray[0]); // cable color
        int c1ModIdx = Integer.parseInt(paramArray[1]); // module index
        int c1ConIdx = Integer.parseInt(paramArray[2]); // connector index
        int c1Type   = Integer.parseInt(paramArray[3]); // 0=in

        int c2ModIdx = Integer.parseInt(paramArray[4]);
        int c2ConIdx = Integer.parseInt(paramArray[5]);
        int c2Type   = Integer.parseInt(paramArray[6]); // 0=in or 1=out
        
        if (c1Type != isInput) {
            System.err.println("IN CONNECTOR EXPECTED!!! PATCH NON 3.0 complient!");
            return null;
        }

        Module c1Mod = msection.get(c1ModIdx);
        Module c2Mod = msection.get(c2ModIdx);

        Connector c1 = c1Mod.findConnector(c1Mod.getInfo().getConnectorById(c1ConIdx, c1Type==isInput));
        Connector c2 = c2Mod.findConnector(c2Mod.getInfo().getConnectorById(c2ConIdx, c2Type==isInput));

        return new CCurve(c1, c2);
	}
	
	public String encode() {
		
		int color = 0;

		DConnector d1 = c1.getInfo();
		DConnector d2 = c2.getInfo();

		if (d1.isOutput()) { // exchange so that d1 is always input
			DConnector tmp = d1;
			d1 = d2;
			d2 = tmp;
		}
		
		DModule m1 = d1.getParent();
		DModule m2 = d2.getParent();
		
		return ""+color
			 +" "+m1.getModuleID()
			 +" "+d1.getId()
			 +" "+(d1.isInput()?0:1)
			 +" "+m2.getModuleID()
			 +" "+d2.getId()
			 +" "+(d2.isInput()?0:1)
			 +"\r\n";
		
		/*result.append("" + cab.getColor() + " " + 
				cab.getBeginModule() + " " + 
				cab.getBeginConnector() + " 0 " + 
				cab.getEndModule() + " " + 
				cab.getEndConnector() + " " + 
				cab.getEndConnectorType() + "\r\n");*/
	}
	
}
