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


public class MultiEnvelope extends Envelope{
	
	protected int sustainSeg = 2;
	protected int curveType = 0;
	
	protected MultiEnvelope(int nbPoints)
    {
        super(nbPoints);
        setNbSegment(6);
        // attack
        setTime(0, 0);
        setLevel(0, 0);        
        
        setTime(1, RANGE_MAX);
        setLevel(1, 64);       
        
        setTime(2, RANGE_MAX);
        setLevel(2, 63);       
        
        setTime(3,RANGE_MAX);  
        setLevel(3, RANGE_MAX);        
        
        setTime(4,RANGE_MAX);
        setLevel(4, 0);        
        		
        setTime(5,RANGE_MAX);
        setLevel(5, 0);        
        
        setTime(6,RANGE_MAX);
        setLevel(6, 0);    
    }
    
    public MultiEnvelope()
    {
        this( 7);            
    }
    
    /* 
     * This function update the curve type when the level of a point is changed
     */    
    private void update_curve(){
        setModified(true);
    	for (int i = 1; i <= nbSegment; i++){
    		// we must iterate over ALL segments, so we use the getLevel method 
    		// of the Enveloppe class and not the method of the multiEnvClass
    		if (curveType == 0) { //set all linear
    			setCurveType(i, LIN);
    		}
    		else {
	    		if (super.getLevel(i) > super.getLevel(i-1)){
	    			setCurveType(i, LOG);
	    		} else {
	    			if (curveType == 1)
	    				setCurveType(i, EXP);
	    			else if (curveType == 2)
	    				setCurveType(i, LIN);
	    		}
    		}
    	}   	
    }
    
    public void setLevel(int segment, int level){
    	//inverse level so that level = 0 corresponds to the top of the graph
    	level = 127 - level;
    	
    	if(segment < sustainSeg) {
    		super.setLevel(segment, level);    		
    	}
    	else if (segment == sustainSeg)
    	{
    		super.setLevel(segment, level);
    		super.setLevel(segment+1, level);
    	} else {
    		super.setLevel(segment + 1, level);    		
    	}
    	
    	
    	update_curve();
    	    	
    }
    
    public int getLevel(int segment){
    	if(segment <= sustainSeg)
    		return super.getLevel(segment);
    	else {
    		return super.getLevel(segment+1);
    	}    	
    }
    
    private int getPointIndex(int point){
    	if (point <= sustainSeg) return point;
    	else return point+1;    	
    }
    
    public void setTime(int point, int time){    	
    	int pointIndex = getPointIndex(point);
    	super.setTime(pointIndex, time);
    	
    	//calculate the total length of all segments except the sustaint segment
    	int sum=0;
		for(int i = 1; i < nbSegment; i ++) {
				sum += getTime(i);      				
		}     		
		// update length of sustain segment
		super.setTime(sustainSeg+1, nbSegment*RANGE_MAX-sum);
				
		// since we changed several segemnt times, we update all the segments:
		for(int i = 1; i <= nbSegment; i++) {						
			super.setTime(i, super.getTime(i));
		}
		//System.out.println(nbSegment + " " + super.getTime(sustainSeg+1) + " "+ sum + " "+(sum+super.getTime(sustainSeg+1)) );
    }
    
    public void refresh(){
    	for(int i = 1; i <= nbSegment; i++) {						
			super.setTime(i, super.getTime(i));
			if(i != sustainSeg)
				setLevel(i, getLevel(i));
		}    
    	setLevel(sustainSeg, getLevel(sustainSeg));
    }
    
    /* getTime never returns the time of the second point of the sustain
     * segment (point sustainSeg+1). Indeed this point is not considered 
     * in the segement numbering. So iterating over segment 1 to 5 should 
     * not access to this point. 
     *  
     * When a segment following the sustain segment is modified, the time of the point
     * sustainSeg+1 is set to fill the space see setTime 
     *  */ 
    public int getTime(int segment){    	
    	if(segment <= sustainSeg)
    		return super.getTime(segment);
    	else {
    		return super.getTime(segment+1);
    	}
    }

	public int getSustainSeg() {
		// in the NM editor the -- value actually corresponds to the 5th segment
		if (sustainSeg == 5)
			return 0;
		else return sustainSeg;
	}

	public void setSustainSeg(int sustainSeg) {
		System.out.println(sustainSeg);
		
		
        // in the NM editor the -- value actually corresponds to the 5th segment
		if(sustainSeg == 0)
			sustainSeg = 5;		
		
		int levels[] = new int[6];
		int times[] = new int[6];
		int sustainTime = super.getTime(this.sustainSeg+1);
		
		//save the old times and levels
		for(int i = 0 ; i <= 5; i++ ) {
			levels[i] = getLevel(i);
			times[i] = getTime(i);
		}
		
		// update them with respect to the new sustain segment
		for(int i = 0 ; i <= nbSegment ; i++){		
			if(i < sustainSeg ){
				super.setLevel(i,levels[i]);
				super.setTime(i, times[i]);
			} else if(i == sustainSeg) {
				super.setLevel(i,levels[i]);				
				super.setLevel(i+1,levels[i]);
				
				super.setTime(i, times[i]);
				super.setTime(i+1, sustainTime);
				i++;
			} else{
				super.setLevel(i,levels[i-1]);
				super.setTime(i,times[i-1]);
			}
			
		}

		update_curve();
		this.sustainSeg = sustainSeg;			
	}
	
	public int getCurve(){
		return this.curveType;
	}
	
	public void setCurve(int curve){
		this.curveType = curve;
		switch (curve) {
		case 0:
			super.setLevel(0,64);
			super.setLevel(nbSegment,64);
			break;
		case 1:			
		case 2:
			super.setLevel(0,127);
			super.setLevel(nbSegment,127);
						
			break;
		}
		update_curve();
	}
}