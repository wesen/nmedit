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
 * Created on Jul 10, 2006
 */
package net.sf.nmedit.nomad.main.background;

import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public class GradientSectorBackground implements Background
{

    private GradientPaint gp;

    public GradientSectorBackground(GradientPaint gp)
    {
        this.gp = gp;
    }

    public void paintBackground( Component c, Graphics g, int x, int y,
            int width, int height )
    {
        Graphics2D g2 = (Graphics2D) g;
        Paint p = g2.getPaint();

        float x1 = (float) gp.getPoint1().getX();
        float y1 = (float) gp.getPoint1().getY();

        float x2 = (float) gp.getPoint2().getX();
        float y2 = (float) gp.getPoint2().getY();
        
        g2.setPaint(new GradientPaint(
                x1-c.getX(), y1-c.getY(),
                gp.getColor1(),
                x2-c.getX(), y2-c.getY(),
                gp.getColor2()
        ));
        g2.fillRect(x, y, width, height);
        g2.setPaint(p);
    }

    public int getTransparency()
    {
        return (GraphicsToolkit.isTransparent(gp.getColor1())
        || GraphicsToolkit.isTransparent(gp.getColor2()))
            ? TRANSLUCENT : OPAQUE;
    }

}
