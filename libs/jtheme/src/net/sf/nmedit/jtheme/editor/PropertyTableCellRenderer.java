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
package net.sf.nmedit.jtheme.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class PropertyTableCellRenderer implements TableCellRenderer
{
    
    private DefaultTableCellRenderer defaultRenderer;
    private transient Map<Class<?>, TableCellRenderer> cellRendererMap;
    
    public PropertyTableCellRenderer()
    {
        defaultRenderer = new CustomizedDefaultTableCellRenderer();
    }
    
    private static class CustomizedDefaultTableCellRenderer extends DefaultTableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object value,
                              boolean isSelected, boolean hasFocus, int row, int column) 
        {
            setIcon(null);
            
            return super.getTableCellRendererComponent(table, value, isSelected, 
                    hasFocus, row, column);
        }
    }
    
    protected Map<Class<?>, TableCellRenderer> getCellRendererMap()
    {
        if (cellRendererMap == null)
        {
            cellRendererMap = new HashMap<Class<?>, TableCellRenderer>();   
            createDefaults(cellRendererMap);
        }
        return cellRendererMap;
    }
    
    public void install(JTable table)
    {
        table.setDefaultRenderer(Object.class, this);
        
        Map<Class<?>, TableCellRenderer> crmap = getCellRendererMap();
        
        for (Class<?> clazz : crmap.keySet())
            table.setDefaultRenderer(clazz, crmap.get(clazz));
    }
    
    protected void createDefaults(Map<Class<?>, TableCellRenderer> cellRendererMap)
    {
        FontCellRenderer fontRenderer = new FontCellRenderer(this);
        cellRendererMap.put(Font.class, fontRenderer);
        cellRendererMap.put(FontUIResource.class, fontRenderer);
        
        PointRectangleDimensionCellRenderer prd = new PointRectangleDimensionCellRenderer(this);
        cellRendererMap.put(Point.class, prd);
        cellRendererMap.put(Point2D.class, prd);
        cellRendererMap.put(Rectangle.class, prd);
        cellRendererMap.put(Dimension.class, prd);
        
        ColorCellRenderer colorRenderer = new ColorCellRenderer(this);
        cellRendererMap.put(Color.class, colorRenderer);
        cellRendererMap.put(ColorUIResource.class, colorRenderer);
      //  cellRendererMap.put(PrintColorUIResource.class, colorRenderer);
        
    }

    public DefaultTableCellRenderer getDefaultRenderer()
    {
        return defaultRenderer;
    }
    
    private static final String NULLVALUE = "<html><body><i>null</i></body></html>";
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        TableCellRenderer renderer = defaultRenderer;
        
        if (value != null)
        {
            TableCellRenderer customRenderer = 
                getCellRendererMap().get(value.getClass());
            if (customRenderer != null)
                renderer = customRenderer;
        }
        
        if (value == null)
            value = NULLVALUE;
        
        return renderer.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
    }
    
    public static abstract class ChildCellRenderer implements TableCellRenderer
    {
        protected PropertyTableCellRenderer parent;

        public ChildCellRenderer(PropertyTableCellRenderer parent)
        {
            this.parent = parent;
        }
    }
    
    public static class PointRectangleDimensionCellRenderer extends ChildCellRenderer
    {

        public PointRectangleDimensionCellRenderer(PropertyTableCellRenderer parent)
        {
            super(parent);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            TableCellRenderer renderer = parent.getDefaultRenderer();
            

            if (value instanceof Point)
            {
                Point p = (Point) value;
                value = commaSeparated(p.x, p.y);
            }
            else if (value instanceof Point2D)
            {
                Point2D p = (Point2D) value;
                value = commaSeparated(p.getX(), p.getY());
            }
            else if (value instanceof Rectangle)
            {
                Rectangle r = (Rectangle) value;
                value = commaSeparated(r.x, r.y, r.width, r.height);
            }
            else if (value instanceof Dimension)
            {
                Dimension d = (Dimension) value;
                value = commaSeparated(d.width, d.height);
            }
            
            return renderer.getTableCellRendererComponent(table, value, isSelected, 
                    hasFocus, row, column);
        }

    }
    
    public static String commaSeparated(Object ... values)
    {
        StringBuilder sb = new StringBuilder();
        if (values.length>0)
        {
            sb.append(values[0]);

            for (int i=1;i<values.length;i++)
            {
                sb.append(",");
                sb.append(values[i]);
            }
        }
        return sb.toString();
    }

    public static class ColorCellRenderer extends ChildCellRenderer
    {
        
        private Color colorImageColor;
        private BufferedImage colorImage;

        public ColorCellRenderer(PropertyTableCellRenderer parent)
        {
            super(parent);
        }
        
        protected BufferedImage getColorImage(Color color)
        {
            if (colorImage == null || !color.equals(colorImageColor))
            {
                final int size = 13; 
                
                colorImage = 
                    GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration()
                    .createCompatibleImage(size, size);
                
                Graphics2D g2 = colorImage.createGraphics();
                try
                {
                    g2.setColor(color);
                    g2.fillRect(0, 0, size, size);
                    g2.setXORMode(Color.BLACK);
                    g2.drawRect(0, 0, size-1, size-1);
                }
                finally
                {
                    g2.dispose();
                }
            }
            return colorImage;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column)
        {
            DefaultTableCellRenderer renderer = parent.getDefaultRenderer();
            
            Color color;
            
            if (value instanceof Color)
            {
                color = (Color) value;
            }
            else if (value instanceof ColorUIResource)
            {
                color = (ColorUIResource) value;
            }
            else
            {
                return renderer.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
            }

            ImageIcon colorIcon = new ImageIcon(getColorImage(color));
            value = commaSeparated(color.getRed(), color.getGreen(), color.getBlue());
            
            JLabel rendererComponent = (JLabel) renderer.
                getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            renderer.setIcon(colorIcon);
            
            return rendererComponent;
        }
    }
    
    public static class FontCellRenderer extends ChildCellRenderer
    {

        public FontCellRenderer(PropertyTableCellRenderer parent)
        {
            super(parent);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column)
        {
            DefaultTableCellRenderer renderer = parent.getDefaultRenderer();
            
            Font font;
            
            if (value instanceof FontUIResource)
                font = (FontUIResource) value;
            else if (value instanceof Font)
                font = (Font) value;
            else
            {
                return renderer.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
            
            StringBuilder html = new StringBuilder();
            html.append("<html><body>");
            html.append("<font face=");
            html.append(font.getName());
            html.append(">");
            
            if (font.getName().indexOf(' ')>=0)
            {
                html.append("'");
                html.append(font.getName());
                html.append("'");   
            }
            else
            {
                html.append(font.getName());
            }
            html.append("</font>");

            html.append(",");
            html.append(font.getSize());
            
            if (font.isBold())
            {
                html.append(",");
                html.append("<b>bold</b>");
            }

            if (font.isItalic())
            {
                html.append(",");
                html.append("<i>italic</i>");
            }

            html.append("</body></html>");
            
            return renderer.getTableCellRendererComponent(table, 
                    html.toString(), isSelected, hasFocus, row, column);
        }
        
    }
    
}

