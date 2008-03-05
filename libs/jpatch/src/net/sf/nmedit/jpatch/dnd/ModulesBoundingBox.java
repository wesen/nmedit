/**
 * 
 */
package net.sf.nmedit.jpatch.dnd;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PModule;

public class ModulesBoundingBox {
	private Rectangle boundingBox;
    private Collection<? extends PModule> modules;
    private Point dragStartLocation;
    private Image transferImage ;
    
	public ModulesBoundingBox(Collection<? extends PModule> modules, Point dragStartLocation) {
		this.dragStartLocation = dragStartLocation;
		this.modules = modules; 
//		boundingBox = new Rectangle();
        for (PModule m : modules) {
        	if (boundingBox == null) {
        		boundingBox = m.getScreenBounds(boundingBox);
        	} else {
                SwingUtilities.computeUnion(
                        m.getScreenX(), m.getScreenY(), 
                        m.getScreenWidth(), m.getScreenHeight(), 
                        boundingBox);
        	}
        }
	}
	
    public Point getDragStartLocation()
    {
        return new Point(dragStartLocation);
    }

    public Rectangle getBoundingBox()
    {
        return getBoundingBox(null);
    }
    
    public Rectangle getBoundingBox(Rectangle r)
    {
        if (r == null)
        {
            return new Rectangle(boundingBox);
        }
        else
        {
            r.setRect(boundingBox);
            return r;
        }
    }

	public Collection<? extends PModule> getModules() {
		return modules;
	}
    
    public Image getTransferImage()
    {
        return transferImage;
    }
    
    public void setTransferImage(Image image)
    {
        this.transferImage = image;
    }
}