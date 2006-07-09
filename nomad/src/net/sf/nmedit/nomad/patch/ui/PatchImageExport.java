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
 * Created on Jul 8, 2006
 */
package net.sf.nmedit.nomad.patch.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sf.nmedit.nomad.main.background.Background;
import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.util.graphics.GraphicsToolkit;

public class PatchImageExport
{

    public final static Color lblBackground1 = Color.decode("#FFD763");
    public final static Color lblBackground2 = NomadUtilities.alpha(lblBackground1, 0);
    
    protected PatchUI ui;
    private BufferedImage bi;
    private Color background;

    public PatchImageExport(PatchUI ui)
    {
        this.ui = ui;
        this.bi = null;
        background = null; // transparent
    }
    
    public void setBackground(Color c)
    {
        this.background = c;
        bi = null;
    }
    
    public void saveExportImageJPG(File file) throws IOException
    {
        saveExportImage(file, "jpg");
    }
    
    public void saveExportImagePNG(File file) throws IOException
    {
        saveExportImage(file, "png");
    }
    
    public void saveExportImage(File file, String formatName) throws IOException
    {
        ImageIO.write(getExportImage(), formatName, file);
    }

    public BufferedImage getExportImage()
    {
        if (bi != null) return bi;
        
        ModuleSectionUI common = ui.getCommonSection();
        ModuleSectionUI poly = ui.getPolySection();

        Rectangle bCommon = getBoundary(common);
        Rectangle bPoly = getBoundary(poly);
        
        bCommon.width = Math.max(bCommon.width, bPoly.width);
        bPoly.width = bCommon.width;
        
        Font font = new Font("sanserif", Font.BOLD, 12);
        FontMetrics fMetrics = ui.getFontMetrics(font);
        
        int pad = 2;
        int barHeight = fMetrics.getHeight()+(pad*2);
        
        Dimension sz = new Dimension(Math.max(bPoly.width, bCommon.width), bPoly.height+bCommon.height+barHeight*2);
        bi = GraphicsToolkit.createCompatibleBuffer(sz, background == null ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
        if (background!=null)
        {
            Graphics2D gBg = bi.createGraphics();
            gBg.setColor(background);
            gBg.fillRect(0, 0, sz.width, sz.height);
            gBg.dispose();
        }
        
        Graphics2D g2 = bi.createGraphics();


        Graphics2D gLabel = (Graphics2D) g2.create();
        gLabel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gLabel.setColor(Color.LIGHT_GRAY);
        
        gLabel.setPaint(new GradientPaint(0, 0, lblBackground1, sz.width, 0, lblBackground2));
        gLabel.fillRoundRect(0, 0, sz.width, barHeight, 8,8);
        gLabel.fillRoundRect(0, bPoly.height+barHeight, sz.width, barHeight, 8,8);
        
        //gLabel.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER))
        
        gLabel.setColor(Color.BLACK);
        gLabel.drawString("Poly Voice Area", 4, barHeight-pad-fMetrics.getDescent());
        gLabel.drawString("Common Voice Area", 4, bPoly.height+(barHeight*2)-pad-fMetrics.getDescent());
        gLabel.dispose();
        
        drawVoiceArea(g2, poly, 0, barHeight, bPoly);
        drawVoiceArea(g2, common, 0,bPoly.height+(barHeight*2), bCommon);
        
        g2.dispose();
        
        return bi;
    }
    
    private Rectangle getBoundary(ModuleSectionUI va)
    {
        Rectangle r = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
        for (Component c : va.getComponents())
        {
            r.x = Math.min(r.x, c.getX());
            r.y = Math.min(r.y, c.getY());
            r.width = Math.max(r.width, c.getX()+c.getWidth());
            r.height = Math.max(r.height, c.getY()+c.getHeight());
        }
        if (r.x!=Integer.MAX_VALUE) r.width-=r.x;
        if (r.y!=Integer.MAX_VALUE) r.height-=r.y;
        return r;
    }

    private void drawVoiceArea( Graphics2D g2, ModuleSectionUI va, int dx, int dy, Rectangle source )
    {
        final boolean opaque = va.isOpaque();
        final boolean caFilled = va.isContentAreaFilled();
        final Background background = va.getBackgroundB();
        final Color backgroundColor = va.getBackground();
        
        try
        {
            if (background==null)
            {
                va.setOpaque(false);
                va.setContentAreaFilled(false);
                va.setBackground(null);
                va.setBackgroundB(null);
            }
            
            Graphics2D gPrint = (Graphics2D) g2.create(dx, dy, source.width, source.height);
            gPrint.translate(-source.x, -source.y);
            va.printComponents(gPrint);
            va.printCables(gPrint);
            gPrint.dispose();
        }
        finally
        {
            if (background==null)
            {
                va.setOpaque(opaque);
                va.setContentAreaFilled(caFilled);
                va.setBackgroundB(background);
                va.setBackground(backgroundColor);
            }
        }
        
    }
    
}
