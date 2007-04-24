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
package net.sf.nmedit.jtheme.clavia.nordmodular.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jtheme.clavia.nordmodular.NMNoteSeqEditor;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.plaf.JTComponentUI;

public class NoteSeqEditorUI extends JTComponentUI
{
    
    public static final Color DEFAULT_BACKGROUND = new Color(0xDCDCDC);
    public static final Color DEFAULT_LINE_COLOR = new Color(0xC0C0C0);
    
    private Color noteColor = Color.black;
    private Color lineColor = DEFAULT_LINE_COLOR;

    private int octave_height = 36;
    private int column_width = 12;
    
    public static ComponentUI createUI(JComponent c)
    {
        return new NoteSeqEditorUI();
    }
    
    public void installUI(JComponent c)
    {
        c.setBackground(DEFAULT_BACKGROUND);
    }
    
    public Dimension getPreferredSize(JComponent c)
    {
        return new Dimension(204,72);
    }
    
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        NMNoteSeqEditor ed = (NMNoteSeqEditor) c;
        
        Insets insets = ed.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = ed.getWidth()-(insets.left+insets.right);
        int h = ed.getHeight()-(insets.top+insets.bottom);
        
        paintBackground(g, ed, x, y, w, h);
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        NMNoteSeqEditor ed = (NMNoteSeqEditor) c;
        
        Insets insets = ed.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = ed.getWidth()-(insets.left+insets.right);
        int h = ed.getHeight()-(insets.top+insets.bottom);
        
        paintForeground(g, ed, x, y, w, h);
    }

    private void paintBackground(Graphics g, NMNoteSeqEditor ed, int x, int y, int w, int h)
    {
        g.setColor(ed.getBackground());
        g.fillRect(0, 0, ed.getWidth(), ed.getHeight());

        int mid = h/2;
        
        // 11 octave lines
        
        int nr = x+ed.getNoteCount()*column_width;
        
        g.setColor(lineColor);
        
        for (int octave = -4; octave<=6; octave++)
        {
            int oy = mid + octave * octave_height;
            g.drawLine(x, oy, nr, oy);   
        }
        
        for (int i=1;i<=ed.getNoteCount()-1;i++)
        {
            int nx = i*column_width;
            g.drawLine(nx, y, nx, y+h);
        }
    }
    

    private void paintForeground(Graphics g, NMNoteSeqEditor ed, int x, int y, int w, int h)
    {
        g.setColor(noteColor);
        
        int row_height = ed.getRowHeight();

        int mid = h/2;
        
        int cw = column_width-2;
        for (int i=0;i<=ed.getNoteCount()-1;i++)
        {
            int nx = i*column_width+1;
            int note = ed.getNote(i);
            int ny = mid+note-64;
            
            if (0<=ny && ny<y+h)
            {
                g.fillRect(nx, ny, cw+1, row_height);
            }
            else 
            {
                g.setColor(lineColor);
                paintArrow(g, ny<0, nx, cw, h);
                g.setColor(noteColor);
            }
        }
    }

    private void paintArrow(Graphics g, boolean uparrow, int nx, int cw, int h)
    {
        int aheight = 5;

        int dy1 = 0;
        int dy2 = 0;
        
        if (!uparrow)
        {
            dy1 = h-aheight-2-aheight;
            dy2 = h-2;
        }

        g.drawLine(nx, aheight+1+dy1, nx+(cw/2), 1+dy2);
        g.drawLine(nx+cw, aheight+1+dy1, nx+(cw/2), 1+dy2);
        g.drawLine(nx, aheight+1+dy1, nx+cw, aheight+1+dy1);
    }

}
