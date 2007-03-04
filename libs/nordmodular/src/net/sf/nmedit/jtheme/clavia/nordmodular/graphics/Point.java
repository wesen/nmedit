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

public class Point {
	private float x,y;
	// coordinated of the bezier control points
	private float x1,y1,x2,y2; 
	private int weight,time;
	private int point_type,curve_type;
	
	public void setLocation(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setBezier (float srcX, float srcY, float dist1, float dist2, float angle1, float angle2){
		this.x1 = srcX + dist1*(float)Math.cos(angle1*Math.PI/180f);
		this.y1 = srcY + dist1*(float)Math.sin(angle1*Math.PI/180f);
		this.x2 = x + dist2*(float)Math.cos(angle2*Math.PI/180f);
		this.y2 = y + dist2*(float)Math.sin(angle2*Math.PI/180f);
	}
	
	public void setBezier(float srcX, float srcY, float angle1, float angle2 ){
		//float dist = (float)Math.sqrt((x - srcX)*(x - srcX)+(y - srcY)*(y - srcY));
		float distX = (x - srcX) / 2;
		float distY = (y - srcY) / 2;
		this.x1 = srcX + distX*(float)Math.cos(angle1*Math.PI/180f);
		this.y1 = srcY + distY*(float)Math.sin(angle1*Math.PI/180f);
		this.x2 = x + distX*(float)Math.cos(angle2*Math.PI/180f);
		this.y2 = y + distY*(float)Math.sin(angle2*Math.PI/180f);
		
		//System.out.println("Point.setBezier()" + x1 + " " + y1 + " "  + x2 + " " + y2);
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getPoint_type() {
		return point_type;
	}

	public void setPoint_type(int curve_type) {
		this.point_type = curve_type;
	}

	public int getCurve_type() {
		return curve_type;
	}

	public void setCurve_type(int curve_type) {
		this.curve_type = curve_type;
	}

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}

	public float getY2() {
		return y2;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}
	
	
}
