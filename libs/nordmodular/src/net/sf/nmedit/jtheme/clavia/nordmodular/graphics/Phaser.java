/* Copyright (C) 2007 Julien Pauty
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

package net.sf.nmedit.jtheme.clavia.nordmodular.graphics;

import java.awt.geom.PathIterator;


public class Phaser extends Curve{
	int spreadAbs; //value comming from the GUI
	float spread ;
	float spreadMax ;
	int nbPeaks;
	
	int feedBack;
	float feedbackOdd, feedbackEven ;
	
	public Phaser(){
		super(18);
	
		for(int i = 0; i < points.length ; i++)
		{
			points[i] = new Point();
			points[i].setPoint_type(PathIterator.SEG_LINETO);
			points[i].setCurve_type(LIN);
		}
		// Starting and ending points
		points[0].setPoint_type(PathIterator.SEG_MOVETO);
		points[0].setLocation(-0.1f,1.1f);
		points[1].setLocation(-0.1f,0.5f);
		points[15].setLocation(1.1f,0.5f);
		points[16].setLocation(1.1f,1.1f);
		points[17].setLocation(-0.1f,1.1f);
		
		setNbPeaks(3);
		setSpread(63);
		setFeedBack(63);
		
		update_peaks();
	}

	private void update_peaks(){
		//points[2].setCurve_type(EXP);
		setModified(true);
		for (int i = 0; i < nbPeaks ; i++){
			points[i*2+2].setLocation((1-spread)/2 + spread*i*2/(nbPeaks*2f), feedbackEven);
			points[i*2+2].setPoint_type(PathIterator.SEG_CUBICTO);
			points[i*2+2].setCurve_type(LOG);
			points[i*2+3].setLocation((1-spread)/2 + spread*(i*2+1)/(nbPeaks*2f), feedbackOdd);
			points[i*2+3].setPoint_type(PathIterator.SEG_CUBICTO);
			points[i*2+3].setCurve_type(EXP);
		}
		points[nbPeaks*2+2].setLocation((1-spread)/2 + spread, feedbackEven);
		points[nbPeaks*2+2].setPoint_type(PathIterator.SEG_CUBICTO);
		points[nbPeaks*2+2].setCurve_type(LOG);
		
		points[2].setPoint_type(PathIterator.SEG_CUBICTO);
		points[2].setCurve_type(EXP);
		points[nbPeaks*2+3].setPoint_type(PathIterator.SEG_CUBICTO);
		points[nbPeaks*2+3].setCurve_type(LOG);
		
		for (int i = nbPeaks*2+3 ; i <15 ; i++ )
			points[i].setLocation(1.1f,0.5f);
	}
	
	public int getNb_peaks() {
		return nbPeaks;
	}

	public void setNbPeaks(int nb_peaks) {
		this.nbPeaks = nb_peaks;
		// spread_max goes from 0.25 to 1 when nb_peaks goes from 1 to 6
		spreadMax = 0.25f+0.75f*(nb_peaks-1)/5;  
		spread = spreadMax*spreadAbs/127f;
		update_peaks();
	}

	public int getSpread() {
		return spreadAbs;
	}

	public void setSpread(int spread_abs) {
		this.spreadAbs = spread_abs;
		this.spread = this.spreadMax*spread_abs/127f;
		update_peaks();
	}

	public int getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(int feedBack) {
		this.feedBack = feedBack;
		if(feedBack < 77) {
			feedbackOdd = feedBack/77f;
			feedbackEven = 0.5f;
		} else {
			feedbackOdd = 1f;
			feedbackEven = 0.5f-0.3f*(feedBack-77)/50f;
		}
		update_peaks();
	}
}
