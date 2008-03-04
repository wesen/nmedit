package net.sf.nmedit.jpatch.dnd;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.util.Collection;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PPatch;

public interface PModuleTransferData extends Transferable {
	PModuleContainer getSourceModuleContainer();
	PPatch getSourcePatch();
	Collection<? extends PModule> getModules();
	Point getDragStartLocation();
	Rectangle getBoundingBox();
	Rectangle getBoundingBox(Rectangle r);
}
