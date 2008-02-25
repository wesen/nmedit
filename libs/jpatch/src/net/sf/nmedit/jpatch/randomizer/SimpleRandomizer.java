/* Copyright (C) 2006-2007 Julien Pauty
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
package net.sf.nmedit.jpatch.randomizer;

import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PPatch;

public class SimpleRandomizer {

    
	// Randomize all paramaters, except the morph parameters
	private static SimpleRandomizer randomizer = null;
	public static SimpleRandomizer getRandomizer(){
		if (randomizer == null)
			randomizer = new SimpleRandomizer();
		return randomizer;
	}
	
	private DefaultParameterIterator parameterIterator = new DefaultParameterIterator()
	{
        @Override
        protected void iterate(PParameter p)
        {
            double normalizedValue = p.getDoubleValue();
            normalizedValue = normalizedValue*Math.random();
            p.setDoubleValue(normalizedValue);
        }
	}
	;
	
	public void randomize(PPatch patch){
	    parameterIterator.iterate(patch);
	}
	
}
