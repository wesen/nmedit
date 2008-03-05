package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class PaintableSelection
{

    public Point start = new Point(0,0);
    public Point origin = new Point(0,0);
    public Rectangle bounds = new Rectangle(0,0,0,0);

    public void paint(JComponent c, Graphics g)
    {
        SelectionPainter.paintSelectionBox(g, 
                bounds.x-origin.x, 
                bounds.y-origin.y,
                bounds.width, 
                bounds.height);
    }
    
    public void repaint(JComponent c)
    {
        c.repaint(bounds.x-origin.x, 
                bounds.y-origin.y,
                bounds.width, 
                bounds.height);
    }
    
}
