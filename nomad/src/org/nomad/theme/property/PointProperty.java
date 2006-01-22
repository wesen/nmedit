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
package org.nomad.theme.property;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nomad.theme.component.NomadComponent;

/**
 * @author Christian Schneider
 */
public abstract class PointProperty extends Property {

	private final static Pattern xypattern = Pattern.compile("([+\\-]?\\d+),([+\\-]?\\d+)");

	/**
	 * @param component
	 */
	public PointProperty(NomadComponent component) {
		super(component);
		setName("x,y");
	}
	public void setupForEditing() {
		super.setupForEditing();
		setHandler(Point.class, new PropertyValueHandler(){
			public void writeValue(Object value) throws IllegalArgumentException {
				try {
					Point loc = (Point) value;
					setXY(loc.x, loc.y);
				} catch (ClassCastException e) {
					throw new IllegalArgumentException(e);
				}
			}});
	}
	
	public abstract void setXY(int x, int y) ;
	public abstract int getX();
	public abstract int getY();

	public Object getValue() {
		return getLocation();
	}
	
	public Point getLocation() {
		return new Point(getX(), getY());
	}

	public String getValueString() {
		return PointProperty.getXYString(getX(),getY());
	}

	public static String getXYString(int x, int y) {
		return x+","+y;
	}

	public static int[] parseXYString(String s) {
		if (s==null) return null;
		Matcher m = xypattern.matcher(s);
		if (!m.matches()) return null;
		return new int[] {Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))};
	}

	public void setValueFromString(String value) {
		//63ms
		int x=0;int y=0;int i=0;int end = value.length()-1;
		boolean xset = false;
		boolean yset = false;
		boolean signx= false;
		boolean signy= false;
		char c;
		for (;i<=end;i++) {
			c = value.charAt(i);
			if ('0'<=c && c<='9') { 
				x=(x*10) + (c-'0');
				xset = true;
			} else if (c==',') {
				i++; break; // x, finished
			} else if (c=='-') {
				if (xset) throw new IllegalArgumentException("Illegal argument in '"+PointProperty.this+"': "+value);
				else signx=true;
			} else {
				throw new IllegalArgumentException("Illegal argument in '"+PointProperty.this+"': "+value);
			}
		}
		for (;i<=end;i++) {
			c = value.charAt(i);
			if ('0'<=c && c<='9') {
				y=(y*10) + (c-'0');
				yset=true;
			} else if (c=='-') {
				if (yset) throw new IllegalArgumentException("Illegal argument in '"+PointProperty.this+"': "+value);
				else signy=true;
			} else
				throw new IllegalArgumentException("Illegal argument in '"+PointProperty.this+"': "+value);
		}
		if (xset&&yset) {
			if (signx) x=-x;
			if (signy) y=-y;
			setXY(x,y);
		} else
			throw new IllegalArgumentException("Illegal argument in '"+PointProperty.this+"': "+value);
		/*s
		
		System.out.println(value+"  ---- x,y="+x+","+y);
		setXY(x,y);
		/*
		try {
			Matcher m = xypattern.matcher(value);
			if (!m.matches())
				throw new IllegalArgumentException("Illegal argument in '"+PointProperty.this+"': "+value);  
			setXY(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
		} catch (Throwable t) {
			if (!(t instanceof IllegalArgumentException))
				throw new IllegalArgumentException("An error occured in '"+PointProperty.this+"': "+value, t);
		}*/
	}
	
}
