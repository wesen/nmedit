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
package net.sf.nmedit.patchmodifier.randomizer;

import java.util.List;

import net.sf.nmedit.jpatch.PParameter;

public class GaussianRandomizerAlgorithm implements RandomizerAlgorithm
{
	private static int flattenedDistribution[];
	
	static 
	{
		int gaussianDistribution[] = new int[128];
		for (int i=0; i < 128 ; i++){
			double x = i/127.0 * 6 - 3 ; 
			gaussianDistribution[i] = (int)(100*Math.exp(-x*x/2)/Math.sqrt(2*Math.PI));			
		}
		
		int sizeDistribution = 0;
		for (int i : gaussianDistribution) sizeDistribution += i;
		
		flattenedDistribution = new int[sizeDistribution];
		
		int index = 0;
		for (int x = 0; x < 128 ; x ++)
		{
			for (int nbX= 0 ; nbX < gaussianDistribution[x] ;nbX++)
			{
				flattenedDistribution[index] = x;
				index++;
			}
		}
	}

    public void randomize(List<PParameter> parameterList)
    {
        for (PParameter param: parameterList)
        {
            int max = param.getMaxValue();
            int min = param.getMinValue();
            if (max-min ==127)
                param.setValue(flattenedDistribution[(int)(Math.random()*flattenedDistribution.length)]);
        }
    }
}
