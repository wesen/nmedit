package net.sf.nmedit.jtheme.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

import net.sf.nmedit.jtheme.component.plaf.SelectionPainter;

public class BoundingBoxBorder implements Border
{

    public Insets getBorderInsets(Component c)
    {
        return new Insets(1,1,1,1);
    }

    public boolean isBorderOpaque()
    {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height)
    {
        SelectionPainter.paintSelectionBox(g, x, y, width, height);
    }

}
