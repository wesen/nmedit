package net.sf.nmedit.jpatch.dnd;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PPatch;

public class PModuleTransferDataWrapper implements PModuleTransferData {
    private PModuleContainer sourceContainer;
    
    ModulesBoundingBox boundingBox;
    private Image transferImage = null;

    public PModuleTransferDataWrapper(PModuleContainer delegate, Collection<? extends PModule> modules) {
    	this(delegate, modules, new Point(0, 0));
    }

    public PModuleTransferDataWrapper(PModuleContainer delegate, Collection<? extends PModule> modules, Point dragStartLocation)
    {
        this.sourceContainer = delegate;
        this.boundingBox = new ModulesBoundingBox(modules, dragStartLocation);
    }

    public Point getDragStartLocation()
    {
        return boundingBox.getDragStartLocation();
    }

    public Collection<? extends PModule> getModules()
    {
        return boundingBox.getModules();
    }
    
    public PModuleContainer getSourceModuleContainer()
    {
        return sourceContainer;
    }
    
	public PPatch getSourcePatch() {
		return sourceContainer.getPatch();
	}


    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (!isDataFlavorSupported(flavor))
            throw new UnsupportedFlavorException(flavor);
        if (flavor.equals(PDragDrop.ModuleSelectionFlavor))
        	return this;
        if (flavor.equals(PDragDrop.PatchFileFlavor) || flavor.equals(PDragDrop.PatchStringFlavor)) {
        	PPatch newPatch = sourceContainer.createPatchWithModules(getModules());
        	String str = newPatch.patchFileString();
        	if (flavor.equals(PDragDrop.PatchFileFlavor))
        		return new ByteArrayInputStream(str.getBytes());
        	else
        		return str;
        }
        return null;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] flavors = {PDragDrop.ModuleSelectionFlavor, PDragDrop.PatchFileFlavor, PDragDrop.PatchStringFlavor};
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (DataFlavor f: getTransferDataFlavors())
            if (f.equals(flavor))
                return true;
        return false;
    }

	public Rectangle getBoundingBox() {
		return boundingBox.getBoundingBox();
	}

	public Rectangle getBoundingBox(Rectangle r) {
		return boundingBox.getBoundingBox(r);
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