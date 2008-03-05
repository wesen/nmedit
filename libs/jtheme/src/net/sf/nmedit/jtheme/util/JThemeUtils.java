package net.sf.nmedit.jtheme.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MenuDragMouseEvent;

import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.dnd.PDragDrop;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.nmutils.dnd.DefaultEyeCandyTransferable;
import net.sf.nmedit.nmutils.dnd.EyeCandyTransferable;

public class JThemeUtils {
    
    public static EyeCandyTransferable createTransferable(JTContext context, PModuleDescriptor moduleDescriptor)
    {
        Transferable transferable = PDragDrop.createTransferable(moduleDescriptor);
        DefaultEyeCandyTransferable t = new DefaultEyeCandyTransferable(transferable);
        if (context != null)
        {
            try
            {
                t.setTransferImage(ModuleImageRenderer.render(context, moduleDescriptor,
                        true));
            } 
            catch (JTException e)
            {
                // ignore
                // TODO log exception
            }
        }
        return t;
    }

    public static String setColorKey(String title, int colorkey)
    {
        if (colorkey<=0) return getTitleNoColorKey(title);
        return getTitleNoColorKey(title)+'$'+colorkey;
    }

    private static int getColorKeySeparator(String title)
    {
        for (int i=title.length()-1;i>=0;i--)
        {
            char c = title.charAt(i);
            if ('0'<=c && c<='9')
                continue;
            if ((c == '$') && (i+1<title.length()))
                return i; // $ and at least one digit
            break;
        }
        return -1;
    }
    
    public static boolean isColorKeyDefined(String title)
    {
        return getColorKeySeparator(title)>=0;
    }
    
    public static int getColorKey(String title)
    {
        int separator = getColorKeySeparator(title);
        return separator < 0 ? 0 : Integer.parseInt(title.substring(separator+1));
    }
    
    public static String getTitleNoColorKey(String title)
    {
        int separator = getColorKeySeparator(title);
        return separator < 0 ? title : title.substring(0, separator);
    }
    
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
