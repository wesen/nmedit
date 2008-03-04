package net.sf.nmedit.jpatch.dnd;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collection;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;

public class PModuleTransferDataWrapper implements PModuleTransferData {
    private Collection<? extends PModule> modules;
    private PModuleContainer sourceContainer;
    private Point dragStartLocation;
    
    public PModuleTransferDataWrapper(PModuleContainer delegate, Collection<? extends PModule> modules, Point dragStartLocation)
    {
        this.modules = modules;
        this.dragStartLocation = dragStartLocation;
        this.sourceContainer = delegate;
    }

    public Point getDragStartLocation()
    {
        return new Point(dragStartLocation);
    }

    public Collection<? extends PModule> getModules()
    {
        return modules;
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
            Collection<? extends PModule> modules = getModules();
            
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
    
    public PModuleContainer getSourceModuleContainer()
    {
        return sourceContainer;
    }

    public PModuleTransferData getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (!isDataFlavorSupported(flavor))
            throw new UnsupportedFlavorException(flavor);
        if (flavor.equals(PDragDrop.ModuleSelectionFlavor))
        	return this;
        if (flavor.equals(PDragDrop.PatchFileFlavor)) {
            // NMData data2 = NMData.sharedInstance();

        	PModuleContainer jmc = sourceContainer;
        	// construct new patch and add modules XXX
        	// System.out.println("patch file " + patch.patchFileString());

        	return null;
        }
        
        return null;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] flavors = {PDragDrop.ModuleSelectionFlavor, PDragDrop.PatchFileFlavor};
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