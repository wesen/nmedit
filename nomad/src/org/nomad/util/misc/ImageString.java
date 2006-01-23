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
 * Created on Jan 13, 2006
 */
package org.nomad.util.misc;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nomad.theme.component.NomadComponent;
import org.nomad.util.graphics.ImageTracker;

public class ImageString {

	private String string = "";
	private Image image = null;
	private boolean isImageString = false;
	private final static Pattern pattern = Pattern.compile("\\{@(.+)\\}");
	private int lineCount = 1;
	
	public ImageString() {
		super();
	}

	public ImageString(String string) {
		setString(string);
	}

	public void setString(String string) {
		if (string==null) string="";
		String key = matchImageString(string);
		this.image = null;
		if (key!=null) {
			this.string = key;
			this.isImageString = true;
		} else {
			this.string=string;
			isImageString=false;
		}
		lineCount=1;
		for (int i=string.indexOf("\\\\n");i>=0;i=string.indexOf("\\\\n",i+1)) 
			lineCount++;
	}
	
	public boolean matchedImageString() {
		return isImageString;
	}
	
	public int getLineCount() {
		return lineCount;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Rectangle getImageBounds(NomadComponent c) {
		
		if (image!=null) {
			
			return new Rectangle(0,0,image.getWidth(c),image.getHeight(c));
			
		} else {
			
			return null;
			
		}
		
	}
	
	public boolean loadImage(ImageTracker iTracker) {
		
		if (isImageString)
			image = iTracker.getImage(string);
		else
			image = null;
		
		
		return image!=null;
	}
	
	public final static String matchImageString(String string) {
		if (string==null) return null;
		Matcher m = pattern.matcher(string);
		if (!m.matches()) return null;
		return m.group(1);
	}

	public String getString() {
		return isImageString ? "{@"+string+"}" : string;
	}
	
	public String toString() {
		return getString();
	}
	
}
