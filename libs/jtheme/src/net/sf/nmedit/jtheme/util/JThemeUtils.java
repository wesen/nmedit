package net.sf.nmedit.jtheme.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MenuDragMouseEvent;

public class JThemeUtils {
	public static MouseEvent convertMouseEvent(Component source,
			MouseEvent sourceEvent,
			Component destination) {
		Point p = SwingUtilities.convertPoint(source,new Point(sourceEvent.getX(),
				sourceEvent.getY()),
				destination);
		Component newSource;

		if(destination != null)
			newSource = destination;
		else
			newSource = source;

		MouseEvent newEvent;
		if (sourceEvent instanceof MouseWheelEvent) {
			MouseWheelEvent sourceWheelEvent = (MouseWheelEvent)sourceEvent;
			newEvent = new MouseWheelEvent(newSource,
					sourceWheelEvent.getID(),
					sourceWheelEvent.getWhen(),
                    sourceEvent.getModifiers() | sourceEvent.getModifiersEx(),
					p.x,p.y,
					sourceWheelEvent.getClickCount(),
					sourceWheelEvent.isPopupTrigger(),
					sourceWheelEvent.getScrollType(),
					sourceWheelEvent.getScrollAmount(),
					sourceWheelEvent.getWheelRotation());
		}
		else if (sourceEvent instanceof MenuDragMouseEvent) {
			MenuDragMouseEvent sourceMenuDragEvent = (MenuDragMouseEvent)sourceEvent;
			newEvent = new MenuDragMouseEvent(newSource,
					sourceMenuDragEvent.getID(),
					sourceMenuDragEvent.getWhen(),
                    sourceEvent.getModifiers() | sourceEvent.getModifiersEx(),
					p.x,p.y,
					sourceMenuDragEvent.getClickCount(),
					sourceMenuDragEvent.isPopupTrigger(),
					sourceMenuDragEvent.getPath(),
					sourceMenuDragEvent.getMenuSelectionManager());
		}
		else {
			newEvent = new MouseEvent(newSource,
					sourceEvent.getID(),
					sourceEvent.getWhen(),
                    sourceEvent.getModifiers() | sourceEvent.getModifiersEx(),
					p.x,p.y,
					sourceEvent.getClickCount(),
					sourceEvent.isPopupTrigger(),
					sourceEvent.getButton());
		}
		return newEvent;
	}


}
