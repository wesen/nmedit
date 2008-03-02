package net.sf.nmedit.jtheme.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class SimpleSelectionBorder implements Border
{

    private Border border;
    private Color color;
    
    public SimpleSelectionBorder(Border notSelectedBorder, Color color)
    {
        this.border = notSelectedBorder;
        this.color = color;
    }
    
    public Insets getBorderInsets(Component c)
    {
        Insets insets = border.getBorderInsets(c);
        insets.left = Math.max(1, insets.left);
        insets.right = Math.max(1, insets.right);
        insets.top = Math.max(1, insets.top);
        insets.bottom = Math.max(1, insets.bottom);
        return insets;
    }

    public boolean isBorderOpaque()
    {
        return border.isBorderOpaque();
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height)
    {
        border.paintBorder(c, g, x, y, width, height);
        // paint selection
        g.setColor(color);
        g.drawRect(x, y, width-1, height-1);
    }

}
