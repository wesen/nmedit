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
 * Created on Sep 10, 2006
 */
package net.sf.nmedit.jtheme.clavia.nordmodular.misc;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class GradientProgressBar extends JProgressBar
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1792044626323606987L;

    private Color gradient = null;

    private int w = 0;
    private int h = 0;
    private int o = getOrientation();
    
    private GradientPaint gp = null;
    
    public GradientProgressBar()
    {
        super();
        
        setUI(new GradientProgressBarUI());
    }
    
    public void setUI(ProgressBarUI ui) {
        if (ui == null || (!(ui instanceof GradientProgressBarUI)))
            return;
        super.setUI(ui);
    }
    
    public void setGradient(Color c)
    {
        if (this.gradient!=c)
        {
            gp = null;
            this.gradient = c;
            repaint();
        }
    }
    
    public void setForeground(Color c)
    {
        gp = null;
        super.setForeground(c);
    }
    
    public Color getGradient()
    {
        return gradient;
    }
    /*
    protected void paintGradient(Graphics g)
    {
        Color a = getForeground();
        if (a==null || gradient == null || a.equals(gradient))
            return;
        
        Color b = gradient;
        
        Graphics2D g2 = (Graphics2D) g;
        Paint oldp = g2.getPaint();
 
        if (gp == null || getWidth()!=w || getHeight()!=h || o!= getOrientation())
        {
            w = getWidth();
            h = getHeight();
            o = getOrientation();
            if (o==HORIZONTAL)
                gp = new GradientPaint(0, 0, a, getWidth(), 0, b, false);
            else
                gp = new GradientPaint(0, 0, b, 0, getHeight(), a, false);
        }

        g2.setPaint(getBackground());
        g2.fillRect(0, 0, w, h);
        
        g2.setPaint(gp);
        
        float p = getValue()/((float)getMaximum()-getMinimum());
        if (o==HORIZONTAL)
            g2.fillRect(0, 0, (int)(w*p), h);
        else
            g2.fillRect(0, 0, w, (int)(h*p));
        
        g2.setPaint(oldp);
    }
    */
    private static class GradientProgressBarUI extends BasicProgressBarUI
    {
        protected void paintDeterminate(Graphics g, JComponent c) 
        {
            if (!(g instanceof Graphics2D)) {
                return;
            }
            GradientProgressBar gpb = (GradientProgressBar) c;

            Insets b = progressBar.getInsets(); // area for border
            int barRectWidth = progressBar.getWidth() - (b.right + b.left);
            int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
            int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
                
                if (barRectWidth <= 0 || barRectHeight <= 0) {
                    return;
                }

                Graphics2D g2 = (Graphics2D)g;

                    Color ca = gpb.getForeground();
                    Color cb = gpb.gradient;
                    
                    Paint oldp = g2.getPaint();
             
                    if (gpb.gp == null || barRectWidth!=gpb.w || barRectHeight!=gpb.h || gpb.o!= gpb.getOrientation())
                    {
                        gpb.w = barRectWidth;
                        gpb.h = barRectHeight;
                        gpb.o = gpb.getOrientation();
                        if (gpb.o==HORIZONTAL)
                            gpb.gp = new GradientPaint(0, 0, ca, barRectWidth, 0, cb, false);
                        else
                            gpb.gp = new GradientPaint(0, 0, cb, 0, barRectHeight, ca, false);
                    }

                    g2.setPaint(gpb.getBackground());
                    g2.fillRect(0, 0, gpb.w, gpb.h);
                    
                    g2.setPaint(gpb.gp);
                    
                    float p = gpb.getValue()/((float)gpb.getMaximum()-gpb.getMinimum());
                    if (gpb.o==HORIZONTAL)
                        g2.fillRect(b.left, b.top, (int)(barRectWidth*p), barRectHeight);
                    else
                        g2.fillRect(b.left, b.top, barRectWidth, (int)(barRectHeight*p));
                    
                    g2.setPaint(oldp);
                    

            // Deal with possible text painting
                    // Deal with possible text painting
                    if (progressBar.isStringPainted()) {
                        
                        paintString(g, b.left, b.top,
                            barRectWidth, barRectHeight,
                            amountFull, b);
                    }
        }
    }
    
}
