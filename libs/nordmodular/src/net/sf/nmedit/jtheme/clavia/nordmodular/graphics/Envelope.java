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

/*
 * Created on Nov 27, 2006
 */
package net.sf.nmedit.jtheme.clavia.nordmodular.graphics;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;



public class Envelope implements Shape
{
    private boolean modified = true;
    
    public void setModified(boolean modified)
    {
        this.modified = modified;
    }

    public boolean isModified()
    {
        return modified;
    }

    public static class ADEnvelope extends Envelope
    {

        protected ADEnvelope(int nbPoints)
        {
            super(nbPoints);
            setNbSegment(2);
            
            // attack
            setTime(0, 0);
            setLevel(0, RANGE_MAX);
            
            // decay 
            setTime(1, RANGE_MAX);
            setLevel(1, 0);
            setTime(2, RANGE_MAX);
            setLevel(2, RANGE_MAX);
            setTime(3,RANGE_MAX);
            setLevel(3, RANGE_MAX);
            
            setAttackCurve(LIN);
            setDecayCurve(LOG);
        }
        
        public ADEnvelope()
        {
            this( 4 );            
        }
        
        public void setAttackCurve(int ct)
        {
            setCurveType(1, ct);
        }
        
        public int getAttackCurve()
        {
            return getCurveType(1);
        }
        
        public void setDecayCurve(int ct)
        {
            setCurveType(2, ct);
        }
        
        public int getDecayCurve()
        {
            return getCurveType(2);
        }
        
        public void setAttackTime(int f)
        {
            setTime(1, f);
            setTime(2, getDecayTime());
        }
        
        public int getAttackTime()
        {
            return getTime(1);
        }
        
        public void setDecayTime(int f)
        {
            setTime(2, f);
        }
        
        public int getDecayTime()
        {  return getTime(2); }
        
    }

    public static class ADSREnvelope extends ADEnvelope
    {

        protected ADSREnvelope(int nbPoints)
        {
            super(nbPoints);
            setNbSegment(4);
            
            setTime(0, 0);
            setLevel(0, RANGE_MAX);
            
            // decay 
            setTime(1, RANGE_MAX);
            setLevel(1, 0);
            setTime(2, RANGE_MAX);
            
            setLevel(2,63);
            // sustain
            setTime(3, RANGE_MAX);
            setLevel(3, 63);
            // release
            setTime(4, RANGE_MAX);
            setLevel(4, RANGE_MAX);
            setReleaseCurve(LOG);
            
        }
        
        public ADSREnvelope()
        {
            this( 5 );            
        }
        
        public void setDecayTime(int f)
        {      	
            setTime(2, f);           
        }
        
        public int getDecayTime()
        {  
            return getTime(2); 
        }
        
        public void setReleaseCurve(int ct)
        {
            setCurveType(4, ct);
        }
        
        public int getReleaseCurve()
        {
            return getCurveType(4);
        }
        
        public void setSustainValue(int v)
        {
            setLevel(3, v);
            setLevel(2, v);
        }
        
        public float getSustainValue()
        {
            return getLevel(3);
        }
        

        public void setReleaseTime(int f)
        {
        	/* to set the release time we actually set the time of the
        	 * sustain time, which is 4 - attackTime - decayTime - releaseTime
        	 * The principle here is to use the sustain segment to fill the 
        	 * space so that the release segment finishes in the bottom 
        	 * right corner*/
            setTime(3, 4*RANGE_MAX - (getAttackTime()+getDecayTime()+f));
        }
        
        public float getReleaseTime()
        {  
        	//System.out.println(4 - (getTime(3)+getAttackTime()+getDecayTime()));
            return  4*RANGE_MAX - (getTime(3)+getAttackTime()+getDecayTime()); 
        }
        
    }

    public static class AHDEnvelope extends Envelope
    {

        protected AHDEnvelope(int nbPoints)
        {
            super(nbPoints);
            setNbSegment(3);
            // attack
            setTime(0, 0);
            setLevel(0, RANGE_MAX);
            // hold
            setTime(1, RANGE_MAX);
            setLevel(1, 0);
            // decay time
            setTime(2, RANGE_MAX);
            setLevel(2, 0);
            setLevel(3, RANGE_MAX);
            setTime(3,RANGE_MAX);            
            setDecayCurve(LOG);
            setTime(4,RANGE_MAX);
            setLevel(4, RANGE_MAX);
        }
        
        public AHDEnvelope()
        {
            this( 5 );            
        }
        
        public void setAttackCurve(int ct)
        {
            setCurveType(0, ct);
        }
        
        public int getAttackCurve()
        {
            return getCurveType(0);
        }
        
        public void setDecayCurve(int ct)
        {
            setCurveType(3, ct);
        }
        
        public int getDecayCurve()
        {
            return getCurveType(2);
        }
        
        public void setAttackTime(int f)
        {
            setTime(1, f);
            setHoldTime(getHoldTime());
        }
        
        public float getAttackTime()
        {
            return getTime(1);
        }
        
        public void setHoldTime(int f)
        {
            setTime(2, f);
            setDecayTime(getDecayTime());
        }
        
        public int getHoldTime()
        {  
            return getTime(2); 
        }
        
        public void setDecayTime(int f)
        {
            setTime(3, f);
        }
        
        public int getDecayTime()
        {  
            return getTime(3); 
        }
        
    }
    
    public final static int LIN = 0;
    public final static int EXP = 1;
    public final static int LOG = 2;
    public final static int RANGE_MAX = 127;
    
    private boolean inverse = false;
    private Point[] points;
    protected int nbSegment;
    private boolean fill = false;
    
    public Envelope(int nbPoints)
    {
        this.nbSegment = nbPoints-1;
        points = new Point[nbPoints+1]; 
        for ( int i= 0; i < nbPoints+1; i++ ) {
        	points[i]=new Point();
        	points[i].setPoint_type(EnvelopePathIterator.SEG_LINETO);
        	points[i].setCurve_type(LIN);
        }
        points[0].setPoint_type(EnvelopePathIterator.SEG_MOVETO);
        points[0].setWeight(0);
        points[0].setTime(0);
        
        //final point to close the curve (needed for filling)
        points[nbPoints].setWeight(1);
        points[nbPoints].setTime(0);
        points[nbPoints].setPoint_type(EnvelopePathIterator.SEG_MOVETO);               
    }
    
    
    
    public void setFillEnabled(boolean enable)
    {
        this.fill = enable;
        setModified(true);
    }
    
    public boolean isFillEnabled()
    {
        return fill;
    }

    protected Rectangle bounds = new Rectangle(0,0,1,1);
    
    public void setBounds(int x, int y, int width, int height)
    {
        bounds.setBounds(x, y, width, height);
    }

    public Rectangle getBounds()
    {
        return new Rectangle(bounds);
    }

    public Rectangle2D getBounds2D()
    {
        return getBounds();
    }

    public boolean contains( double x, double y )
    {
        return bounds.contains(x, y);
    }

    public boolean contains( Point2D p )
    {
        return bounds.contains(p);
    }

    public boolean intersects( double x, double y, double w, double h )
    {
        return bounds.intersects(x, y, w, h); 
    }

    public boolean intersects( Rectangle2D r )
    {
        return bounds.intersects(r);
    }

    public boolean contains( double x, double y, double w, double h )
    {
        return bounds.contains(x, y, w, h);
    }

    public boolean contains( Rectangle2D r )
    {
        return bounds.contains(r);
    }
    
    private void checkSegIndex(int segIndex, int max)
    {
        if (segIndex<0 || segIndex>max)
            throw new IllegalArgumentException("no such segment: "+segIndex);
    }
    
    private int checkBounds(int v)
    {
        return v<0 ? 0 : (v>RANGE_MAX ? RANGE_MAX : v);
    }
    
    /*set the time of a segment. 
     * Basically this set up the actual x coordinate of the corresponding point
     * The x coordinates is set up with respect to the x coordinate of the 
     * preceding point. This imply that when a point is modified, the following point
     *  should be updated. Updating a point consist in making a call such as: 
     *  setTime(pointIndex, getTime(pointIndex));*/
    public void setTime(int segIndex, int t)
    {
        setModified(true);
    	//System.out.println(segIndex +"=index" + points.length );
    	checkSegIndex(segIndex, points.length-1);
    	points[segIndex].setTime(t);
    	if (segIndex==0) {
    		points[segIndex].setX(t/((nbSegment)*(float)RANGE_MAX));
    	} else {
    		int previous = 0; //length of segment preceding the current segment
    		for(int i= 0 ; i< segIndex ; i++) {
    			previous += points[i].getTime();
    		}
    		points[segIndex].setX((t+ previous)/((nbSegment)*(float)RANGE_MAX) );
    	}
    }
    
    public void setX(int pointIndex, float t){
        setModified(true);
    	points[pointIndex].setX(t/(nbSegment));
    }
    
    public float getX(int pointIndex){
        setModified(true);
    	return points[pointIndex].getX();
    }
    
    public int getTime(int segIndex)
    {    	
        checkSegIndex(segIndex, points.length-1);        
        return points[segIndex].getTime();
    }
    
    public void setCurveType(int segIndex, int type)
    {
        setModified(true);
        checkSegIndex(segIndex, points.length-1);
        
       // if (type>=0 && type<3)
            points[segIndex].setCurve_type(type);
       if(type== LOG || type == EXP) {
    	   points[segIndex].setPoint_type(PathIterator.SEG_CUBICTO);
       } else {
    	   points[segIndex].setPoint_type(EnvelopePathIterator.SEG_LINETO);
       }
    }
    
    public int getCurveType(int segIndex)
    {
        checkSegIndex(segIndex, points.length-1);
        return points[segIndex].getCurve_type();
    }
    
    public void setLevel(int segIndex, int weight)
    {
        setModified(true);
        checkSegIndex(segIndex, points.length-1);
        points[segIndex].setWeight(checkBounds(weight));
        points[segIndex].setY(weight/(float)RANGE_MAX);
    }
    
    public int getLevel(int segIndex)
    {
        checkSegIndex(segIndex, points.length-1);
        return points[segIndex].getWeight();
    }
    
    public int getSegmentCount()
    {
        return nbSegment;
    }
    
    public void setIsInverse(boolean inv)
    {
        setModified(true);
        this.inverse = inv;
        for(int i = 0; i < nbSegment; i++){
        	setLevel(i, RANGE_MAX - getLevel(i));
        }
    }
    
    public boolean isInverse()
    {
        return inverse;
    }

    public PathIterator getPathIterator( final AffineTransform at )
    {
        return new EnvelopePathIterator(at);
    }

    public PathIterator getPathIterator( AffineTransform at, double flatness )
    {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

    private class EnvelopePathIterator implements PathIterator
    {
        // affine transformation
        private AffineTransform at;
        
        // current index
        private int index = 0;

        private int maxpoint = points.length - (fill ? 0 : 1);
        
        public EnvelopePathIterator(AffineTransform at)
        {
            this.at = at;
        }

        public int getWindingRule()
        {
            return WIND_NON_ZERO;
        }

        public boolean isDone()
        {
            return index>=maxpoint;
        }

        public void next()
        {
            index ++;            
        }
  
        private void exp(float srcX, float srcY, float destX, float destY, float coords[])
        {
            coords[0] = (destX-srcX)/2+srcX; // ctrl 1
            coords[1] = srcY;
            coords[2] = destX; // ctrl 2
            coords[3] = (destY-srcY)/2+srcY;
            coords[4] = destX; // destination
            coords[5] = destY;
        }
        
        private void log(float srcX, float srcY, float destX, float destY, float coords[])
        {
            coords[0] = srcX; // ctrl 1
            coords[1] = (srcY-destY)/2+destY;
            coords[2] = (destX-srcX)/2+srcX; // ctrl 2
            coords[3] = destY;
            coords[4] = destX; // destination
            coords[5] = destY;
        }
        

        private void applytransform( float[] coords, int seg )
        {
            if (at != null) {
                at.transform(coords, 0, coords, 0, seg == SEG_CUBICTO ? 3 : 1);
            }
        }
        
        public int currentSegment( float[] fcoords )
        {
        	if(points[index].getCurve_type() == LIN) {
        		fcoords[0] = points[index].getX()*(bounds.width-1);
            	fcoords[1] = points[index].getY()*(bounds.height-1);
        	} else if(points[index].getCurve_type() == EXP) {
            	exp(points[index-1].getX(),points[index-1].getY(),
            			points[index].getX(),points[index].getY(),fcoords);
            	fcoords[0] = fcoords[0]*(bounds.width-1);
            	fcoords[1] = fcoords[1]*(bounds.height-1);
            	fcoords[2] = fcoords[2]*(bounds.width-1);
            	fcoords[3] = fcoords[3]*(bounds.height-1);
            	fcoords[4] = fcoords[4]*(bounds.width-1);
            	fcoords[5] = fcoords[5]*(bounds.height-1);
        	} else if(points[index].getCurve_type() == LOG) {
            	log(points[index-1].getX(),points[index-1].getY(),
            			points[index].getX(),points[index].getY(),fcoords);
            	fcoords[0] = fcoords[0]*(bounds.width-1);
            	fcoords[1] = fcoords[1]*(bounds.height-1);
            	fcoords[2] = fcoords[2]*(bounds.width-1);
            	fcoords[3] = fcoords[3]*(bounds.height-1);
            	fcoords[4] = fcoords[4]*(bounds.width-1);
            	fcoords[5] = fcoords[5]*(bounds.height-1);
        	}
        	
        	applytransform(fcoords, points[index].getPoint_type());
        	return points[index].getPoint_type();
        }

        public int currentSegment( double[] dcoords )
        {
        	dcoords[0] = (double) points[index].getX();
        	dcoords[1] = (double) points[index].getY();
        	//applytransform(dcoords, points[index].getCurve_type());
        	return points[index].getPoint_type();
        }
    }

	public int getNbSegment() {
		return nbSegment;
	}



	public void setNbSegment(int nbSegment) {
		this.nbSegment = nbSegment;
	}
    
}
