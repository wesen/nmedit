/**
 * 
 */
package net.sf.nmedit.jtheme.dnd;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.util.Collection;

import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;

public interface JTModuleTransferData extends Transferable
{
    JTModuleContainer getSource();
    Collection<? extends JTModule> getModules();
    Point getDragStartLocation();
    Rectangle getBoundingBox();
    Rectangle getBoundingBox(Rectangle r);
}