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
package net.sf.nmedit.jpatch.impl;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PModuleMetrics;

/**
 * The reference implementation of interface {@link PModuleMetrics} using
 * a grid layout.
 * @author Christian Schneider
 */
public class PBasicModuleMetrics implements PModuleMetrics
{

    private int gridWidth;
    private int gridHeight;
    private int maxInternalX;
    private int maxInternalY;
    private int maxScreenX;
    private int maxScreenY;

    public PBasicModuleMetrics(int gridWidth, int gridHeight, int maxInternalX, int maxInternalY)
    {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.maxInternalX = maxInternalX;
        this.maxInternalY = maxInternalY;
        this.maxScreenX = (maxInternalX==Integer.MAX_VALUE) ? Integer.MAX_VALUE : internalToScreenX(maxInternalX);
        this.maxScreenY = (maxInternalY==Integer.MAX_VALUE) ? Integer.MAX_VALUE : internalToScreenY(maxInternalY);
    }

    public int getInternalHeight(PModuleDescriptor module)
    {
        return module.getIntAttribute("height", 1);
    }

    public int getInternalHeight(PModule module)
    {
        return module.getIntAttribute("height", 1);
    }

    public int getInternalWidth(PModuleDescriptor module)
    {
        return module.getIntAttribute("width", 1);
    }

    public int getInternalWidth(PModule module)
    {
        return module.getIntAttribute("width", 1);
    }

    public int getScreenHeight(PModuleDescriptor module)
    {
        return internalToScreenY(getInternalHeight(module));
    }

    public int getScreenHeight(PModule module)
    {
        return internalToScreenY(getInternalHeight(module));
    }

    public int getScreenWidth(PModuleDescriptor module)
    {
        return internalToScreenX(getInternalWidth(module));
    }

    public int getScreenWidth(PModule module)
    {
        return internalToScreenX(getInternalWidth(module));
    }

    public int internalToScreenX(int x)
    {
        return x*gridWidth;
    }

    public int internalToScreenY(int y)
    {
        return y*gridHeight;
    }

    public int screenToInternalX(int x)
    {
        return (x+gridWidth/2)/gridWidth;
    }

    public int screenToInternalY(int y)
    {
        return (y+gridHeight/2)/gridHeight;
    }

    public int getMaxInternalX()
    {
        return maxInternalX;
    }

    public int getMaxInternalY()
    {
        return maxInternalY;
    }

    public int getMaxScreenX()
    {
        return maxScreenX;
    }

    public int getMaxScreenY()
    {
        return maxScreenY;
    }

    public int alignScreenX(int x)
    {
        return internalToScreenX(screenToInternalX(x));
    }

    public int alignScreenY(int y)
    {
        return internalToScreenY(screenToInternalY(y));
    }

}
