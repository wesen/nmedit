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
 * Created on Jun 27, 2006
 */
package net.sf.nmedit.nomad.main.ui.fix;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTree;

public class TreeStripesContainer extends JComponent
{

    private JTree tree;
    private TreeStripes stripes;

    public TreeStripesContainer(JTree tree, TreeStripes stripes)    
    {
        this.tree = tree;
        this.stripes = stripes;
        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
        setBorder(null);
        setOpaque(true);

        setBackground(tree.getBackground());
        tree.setBackground(null);
        tree.setOpaque(false);
    }
    
    public JTree getTree() 
    {
        return tree;
    }
    
    public TreeStripes getStripes() 
    {
        return stripes;
    }

    public void update(Graphics g)
    {
        paintEmptyRows(g);
        paintRows(g);
        paint(g);
    }
    
    public void paintComponent(Graphics g)
    {
        paintEmptyRows(g);
        paintRows(g);
    }
    
    protected void paintRows(Graphics g)
    {
        final int rowCount = tree.getRowCount();
        final int rowHeight = tree.getRowHeight();
        final Rectangle clip = g.getClipBounds();
        
        final int rStart = clip.y/rowHeight;
        final int rStop  = Math.min((clip.y+clip.height)/rowHeight, rowCount-1);
        
        for (int i=rStart;i<=rStop;i++)
        {
            g.setColor(stripes.getColorAt(i));
            g.fillRect(clip.x, i * rowHeight, clip.width, rowHeight);
        }
    }
    
    protected void paintEmptyRows(Graphics g)
    {
        final int rowCount = tree.getRowCount();
        final int rowHeight = tree.getRowHeight();
        final Rectangle clip = g.getClipBounds();
        
        if (rowCount * rowHeight < clip.y+clip.height) 
        {
            int lastVisible = (clip.y+clip.height)/rowHeight;
            for (int i = rowCount; i <= lastVisible; i++) 
            {
                g.setColor(stripes.getEmptyRowColorAt(i));
                g.fillRect(clip.x, i * rowHeight, clip.width, rowHeight);
            }
        }
    }
    
}
