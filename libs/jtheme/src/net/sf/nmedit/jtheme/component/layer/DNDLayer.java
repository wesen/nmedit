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
package net.sf.nmedit.jtheme.component.layer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.dnd.ModulesBoundingBox;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTLayerRoot;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.plaf.SelectionPainter;
import net.sf.nmedit.nmutils.dnd.DefaultEyeCandyTransferable;

public class DNDLayer extends JTLayer
{

    private Point relativeStart = new Point(0,0);
    private Point origin = new Point(0,0);
    private Point lastCursorLocation = new Point(0,0);
    public static final String DND_BOUNDINGBOX_BORDER = "dnd.boundingbox.border";

    private Image image;
    private ModulesBoundingBox modulesBoundingBox;
    private JViewport view;
    
    public DNDLayer()
    {
        super();
    }
    
    public DNDLayer(JTContext context)
    {
        super(context);
    }
    
    public void resetView()
    {
        this.view = null;
    }
    
    public JViewport findView()
    {
        if (view == null)
        {
            Container c = getParent();
            while (c != null)
            {
                if (c instanceof JViewport)
                {
                    view = (JViewport) c;
                    break;
                }
                c = c.getParent();
            }
        }
        return view;
    }
    
    public static DNDLayer forTransferImage(JTContext context, Transferable t)
    {
        Image transferImage = DefaultEyeCandyTransferable.getTransferImage(t);
        if (transferImage == null)
            return null;
        else
        {
            DNDLayer layer = new DNDLayer(context);
            layer.setImage(transferImage);
            layer.setSize(transferImage.getWidth(null), transferImage.getHeight(null));
            return layer;
        }
    }
    
    public Point getCursorPosition()
    {
        return new Point(
                getX()+relativeStart.x,
                getY()+relativeStart.y
        );
    }

    public Point getDelta()
    {
        return new Point(
                lastCursorLocation.x-(getX()+relativeStart.x),
                lastCursorLocation.y-(getY()+relativeStart.y)
        );
    }
    
    public void setPositionFromCursor(Point cursor)
    {
        lastCursorLocation.setLocation(
                getX()+relativeStart.x,
                getY()+relativeStart.y
        );
        setLocation(
        cursor.x-relativeStart.x,
        cursor.y-relativeStart.y
        );
        updateScrollPosition(cursor);
    }

    public void updateScrollPosition(Point location) {
        JComponent parent = (JComponent)getParent();
        if (parent == null)
            return;
        Rectangle visible = parent.getVisibleRect();
        if (visible == null) 
            return;
        
        Dimension dim = parent.getSize();
//      System.out.println("visible " + visible.width + "x" + visible.height + " dim " + dim.width + "x" + dim.height);
        int wScroll = Math.min(visible.width / 3, 40);
        int hScroll = Math.min(visible.height / 3, 40);
        // System.out.println("w " + wScroll + " h " + hScroll);
        
        Rectangle scrollTo = new Rectangle(location);
        if (location.x < (visible.x + wScroll))
            scrollTo.x = Math.max(location.x - wScroll, 0);
        if (location.x > (visible.x + visible.width - wScroll))
            scrollTo.x = location.x + wScroll;
        scrollTo.x = Math.min(scrollTo.x, dim.width);
        
        if (location.y < (visible.y + hScroll))
            scrollTo.y = Math.max(location.y - hScroll, 0);
        if (location.y > (visible.y + visible.height - hScroll))
            scrollTo.y = location.y + hScroll;
        scrollTo.y = Math.min(scrollTo.y, dim.height);
        
//         System.out.println("location " + location.x + " " + location.y + " visible " + visible.x + " " + visible.y + " scrollto " + scrollTo.x + " " + scrollTo.y);
        
        parent.scrollRectToVisible(scrollTo);
    }

    private void autoScroll()
    {
        /*
        JViewport jv = findView();
        if (jv != null)
        {
            Point p = view.getViewPosition();
            Point d = getDelta();
           // if (Math.abs(d.x)>10 || Math.abs(d.y)>10)
            {
                System.out.println(getDelta());
            int newX = p.x + d.x;
            int newY = p.y + d.y;

            int maxX = jv.getWidth();
            int maxY = jv.getHeight();
            
            if (newX < 0) newX = 0;
            if (newX > maxX) newX = maxX;
            if (newY < 0) newY = 0;
            if (newY > maxY) newY = maxY;

            jv.setViewPosition(new Point(newX, newY));
            }
        }*/
    }

    public Point getLastCursorLocation()
    {
        return new Point(lastCursorLocation);
    }
    
    public void setLastCursorLocation(Point p)
    {
        setLastCursorLocation(p.x, p.y);
    }
    
    public void setLastCursorLocation(int x, int y)
    {
        lastCursorLocation.setLocation(x, y);
    }
    
    public ModulesBoundingBox getModulesBoundingBox()
    {
        return modulesBoundingBox;
    }
    
    public void setModulesBoundingBox(ModulesBoundingBox box)
    {
        this.modulesBoundingBox = box;
        if (box != null)
        {
            Rectangle r = box.getBoundingBox();
            setSize(r.width, r.height);
        }
    }
    
    public void install(JTModuleContainer container)
    {
        JTLayerRoot root = container.getLayerRoot();
        if (root != null)
            root.add(this);
    }
    
    public void uninstall()
    {
        Container parent = getParent();
        if (parent != null)
        {
            parent.remove(this);
            parent.invalidate();
            parent.repaint(getX(), getY(), getWidth(), getHeight());
        }
    }
    
    public void setRelativeStart(Point start)
    {
        setRelativeStart(start.x, start.y);
    }
    
    public void setRelativeStart(int x, int y)
    {
        relativeStart.setLocation(x, y);
    }
    
    public Point getRelativeStart()
    {
        return new Point(relativeStart);
    }
    
    public void setOrigin(Point origin)
    {
        setOrigin(origin.x, origin.y);
    }
    
    public void setOrigin(int x, int y)
    {
        origin.setLocation(x, y);
    }
    
    public Point getOrigin()
    {
        return new Point(origin);
    }
    
    public void setBoundingBoxBorder()
    {
        JTContext context = getContext();
        Border border = null;
        if (context != null)
        {
            border = context.getUIDefaults().getBorder(DND_BOUNDINGBOX_BORDER);
        }
        setBorder(border);
    }
    
    public void setImage(Image image)
    {
        this.image = image;
    }

    public Image getImage()
    {
        return image;
    }
    
    public void setBounds(int x, int y, int width, int height)
    {
        if (modulesBoundingBox != null)
        {
            Rectangle bbox = modulesBoundingBox.getBoundingBox();
            if (bbox != null) bbox.setBounds(x, y, width, height);
        }
        super.setBounds(x, y, width, height);
        
        Container parent = getParent();
        if (parent != null)
        {
            if (x+width>parent.getWidth() || y+height>parent.getHeight())
            {
                revalidate();
            }
        }
    }
    
    protected void paintComponent(Graphics g)
    {
        if (image != null)
        {
            g.drawImage(image, 0, 0, null);
        }
        else if (modulesBoundingBox != null)
        {
            Image transferImage = modulesBoundingBox.getTransferImage();

            if (transferImage != null)
            {
                g.drawImage(transferImage, 0, 0, null);
            }
            else
            {
                SelectionPainter.paintPModuleSelectionBox(g, 
                        modulesBoundingBox.getModules(), 
                        0, 0);
            }
        }
    }
    
    public String toString()
    {
        
        return getClass().getSimpleName()+"[rel.start=("+relativeStart.x+","+relativeStart.y+")"
        +",origin=("+origin.x+","+origin.y+")"
        +",bounds=("+getX()+","+getY()+","+getWidth()+","+getHeight()+")"
        +",modules="+modulesBoundingBox
        +",image="+image+"]";
    }
    
}
