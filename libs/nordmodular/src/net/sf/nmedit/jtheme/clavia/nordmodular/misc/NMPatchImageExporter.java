/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.jtheme.clavia.nordmodular.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.util.PatchImageExporter;

public class NMPatchImageExporter extends PatchImageExporter
{
    
    private Color background = new Color(0, true);
    private Color titleBG = new Color(0xFFE678);
    private JTNMPatch patch;

    public NMPatchImageExporter(JTNMPatch patch)
    {
        this.patch = patch;
    }

    @Override
    public Image export()
    {

        JTModuleContainer mcPoly = patch.getPolyVoiceArea();
        JTModuleContainer mcCommon = patch.getCommonVoiceArea();
        
        List<JTModuleContainer> renderList = new ArrayList<JTModuleContainer>(2);
        List<String> titles = new ArrayList<String>();

        if (addToRenderList(renderList, mcPoly))
        {
            titles.add("Poly Voicearea");
        }
        if (addToRenderList(renderList, mcCommon))
        {
            titles.add("Common Voicearea");
        }
        
        Image[] parts = renderModuleContainer(renderList.toArray(new JTModuleContainer[renderList.size()]));
        
        int w = 1;
        int h = 1;
        for (int i=0;i<parts.length;i++)
        {
            Image img = parts[i];
            w = Math.max(img.getWidth(null), w);
            h += img.getHeight(null)+20;
        }
        
        BufferedImage bi = new BufferedImage(w, h, isTransparent() ? BufferedImage.TYPE_INT_ARGB:
            BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g = bi.createGraphics();
        try
        {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            
            g.setFont(createFont("sansserif", 20-4));
            FontMetrics fm = g.getFontMetrics();
            
            if (!isTransparent())
            {
                g.setColor(background);
                g.fillRect(0, 0, w, h);                
            }
            
            g.setColor(Color.BLACK);
            int y = 0;
            for (int i=0;i<parts.length;i++)
            {
                renderTitle(g, 2, y, w, fm, titles.get(i));
                y += 20;
                Image img = parts[i];
                g.drawImage(img, 0, y, null);
                y += img.getHeight(null);
            }
        }
        finally
        {
            g.dispose();
        }
        
        return bi;
    }
    
    private void renderTitle(Graphics2D g, int x, int top, int w, FontMetrics fm, String string)
    {
        g.setColor(titleBG);
        g.setPaint(new GradientPaint(0, 0, titleBG, w, 0, new Color(titleBG.getRGB()&0xFfFfFf, true)));
        g.fillRect(0, top, w, 20);
        
        g.setColor(Color.BLACK);
        g.drawString(string, x, top+2+fm.getAscent()-fm.getDescent());
    }

    private boolean addToRenderList(Collection<JTModuleContainer> list, JTModuleContainer m)
    {   
        if (m.getModuleContainer() != null && m.getModuleContainer().getModuleCount() > 0
                && m.getComponentCount()>0)
        {
            list.add(m);
            return true;
        }
        return false;
    }

    private static int pixelsToPoint(int pixels, int dpi) 
    {
        return (int) Math.floor((pixels * 72.0) / dpi);
    }
    
    private static Font createFont(String fontname, int heightInPixel)
    {
        int fontsize;
        try
        {
            int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
            fontsize = pixelsToPoint(heightInPixel, dpi);
        }
        catch (HeadlessException e)
        {
            return null;
        }
        
        return fontsize>0 ? new Font(fontname, Font.PLAIN, fontsize) : null;
    }

}
