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
 * Created on Jan 12, 2006
 */
package org.nomad.theme.component.model;

import java.awt.Dimension;
import java.awt.Point;

public class NomadButtonArrayBehaviour {

	private NomadButtonArrayModel model;

	private int cell_width = 0;
	private int cell_height = 0;
	public int padding = 3;
	
	public NomadButtonArrayBehaviour(NomadButtonArrayModel model) {
		this.model = model;
	}

	public NomadButtonArrayModel getModel() {
		return model;
	}
	
	public int getCellWidth() {
		return cell_width;
	}
	
	public int getCellHeight() {
		return cell_height;
	}
	
	public int getButtonIndexAt(Point p) {
		return getButtonIndexAt(p.x, p.y);
	}
	
	private int getButtonIndexAt(int x, int y) {
		int index;
		if (model.isLandscape()) {
			index= x/cell_width;
		} else {
			index= y/cell_height;
		}
		
		if (index<0||index>=model.getButtonCount())
			return -1;
		
		return index;
	}

	public void calculateMetrics() {
		Dimension size = model.getSize();
		
		if (model.getButtonCount()==0)
			return;
		
		if (model.isLandscape()) {
			cell_width = size.width / model.getButtonCount();
			cell_height= size.height;
		}
		else {
			cell_width = size.width;
			cell_height= size.height/ model.getButtonCount();
		}

		/*
		cell_height+=+2*padding;
		cell_height+=+2*padding;*/
	}
	
	public Point getCell(int index) {

		if (model.isLandscape()) {

			return new Point(cell_width*index, 0);
			
		} else {
			
			return new Point(0, cell_height*index);
			
		}
		
	}
	
	public Dimension getPreferredSize() {
		Dimension maxCellSize = new Dimension(padding*2,padding*2);
		for (int i=model.getButtonCount()-1;i>=0;i--) {
			Dimension cell = model.getPreferredCellSize(i);
			maxCellSize.width = Math.max(maxCellSize.width, cell.width+2*padding);
			maxCellSize.height = Math.max(maxCellSize.height, cell.height+2*padding);
		}
		
		Dimension preferredSize = maxCellSize;
		
		if (model.isLandscape()) {
			
			preferredSize.width*=model.getButtonCount();
			
		} else {
			
			preferredSize.height*=model.getButtonCount();
			
		}
		
		return preferredSize;
	}
	
}
