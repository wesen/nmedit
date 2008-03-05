/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;

import net.sf.nmedit.nmutils.dnd.DefaultEyeCandyTransferable;
import net.sf.nmedit.nmutils.dnd.EyeCandyTransferable;

public class PaintableTransfer extends PaintableSelection
{
    
    private Image transferImage = null;

    public PaintableTransfer(Transferable t)
    {
        this.transferImage = DefaultEyeCandyTransferable.getTransferImage(t);
    }

    public PaintableTransfer(EyeCandyTransferable t)
    {
        this.transferImage = t.getTransferImage();
    }

    public PaintableTransfer(Image transferImage)
    {
        this.transferImage = transferImage;
    }

    public void paint(JComponent c, Graphics g)
    {
        if (transferImage != null)
            g.drawImage(transferImage, getPaintX(), getPaintY(), null);
        else
            super.paint(c, g);
    }
    
    public void repaint(JComponent c)
    {
        if (transferImage != null)
        {
            int w = transferImage.getWidth(null);
            int h = transferImage.getWidth(null);
            if (w>0 && h>0) // can be <= 0
                c.repaint(getPaintX(), getPaintY(), w, h);
        }
        else
        {
            super.repaint(c);
        }
    }
    
    public static PaintableTransfer get(Transferable t)
    {
        Image transferImage = DefaultEyeCandyTransferable.getTransferImage(t);
        if (transferImage == null)
            return null;
        else
            return new PaintableTransfer(transferImage);
    }
    
}
