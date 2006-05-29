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
 * Created on May 1, 2006
 */
package net.sf.nmedit.nomad.main.background;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Transparency;

public interface Background extends Transparency
{

    /**
     * Paints the background for the specified component with
     * the specified position and size
     * 
     * @param c the component for which this background is being painted
     * @param g the paint graphics
     * @param x the x position of the painted background
     * @param y the y position of the painted background
     * @param width the width of the painted background
     * @param height the height of the painted background
     */
    void paintBackground(Component c, Graphics g, int x, int y, int width, int height);

}
