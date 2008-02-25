/**
 * 
 */
package net.sf.nmedit.jtheme.dnd;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.plaf.mcui.JTModuleContainerUI;
import net.sf.nmedit.jtheme.component.plaf.mcui.JTModuleContainerUI.EventHandler;

public class JTModuleTransferDataWrapper implements JTModuleTransferData
{
    private EventHandler delegate;
    private Point dragStartLocation;
    
    public JTModuleTransferDataWrapper(EventHandler delegate, Point dragStartLocation)
    {
        this.delegate = delegate;
        this.dragStartLocation = dragStartLocation;
    }

    public Point getDragStartLocation()
    {
        return new Point(dragStartLocation);
    }

    public JTModule[] getModules()
    {
        return delegate.getModules();
    }
    
    private transient Rectangle boundingBox;

    public Rectangle getBoundingBox()
    {
        return getBoundingBox(null);
    }
    
    public Rectangle getBoundingBox(Rectangle r)
    {
        if (boundingBox == null)
        {
            boundingBox = new Rectangle(0,0,0,0);
            
            JTModule[] modules = getModules();
            
            if (modules.length>0)
            {
                JTModule m = modules[0];
                boundingBox = m.getBounds(boundingBox);
                for (int i=modules.length-1;i>=1;i--)
                {
                    m = modules[i];
                    SwingUtilities.computeUnion(
                            m.getX(), m.getY(), 
                            m.getWidth(), m.getHeight(), 
                            boundingBox);
                }
            }
        }
        
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
    
    public JTModuleContainer getSource()
    {
        return delegate.getSource();
    }

    public JTModuleTransferData getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (!isDataFlavorSupported(flavor))
            throw new UnsupportedFlavorException(flavor);
        if (flavor.equals(JTDragDrop.ModuleSelectionFlavor))
        	return this;
        if (flavor.equals(JTDragDrop.PatchFileFlavor)) {
            // NMData data2 = NMData.sharedInstance();

        	JTModuleContainer jmc = delegate.getModuleContainer();
        	// construct new patch and add modules XXX
        	// System.out.println("patch file " + patch.patchFileString());

        	return null;
        }
        
        return null;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] flavors = {JTDragDrop.ModuleSelectionFlavor, JTDragDrop.PatchFileFlavor};
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (DataFlavor f: getTransferDataFlavors())
            if (f.equals(flavor))
                return true;
        return false;
    }
}