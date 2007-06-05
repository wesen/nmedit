package net.sf.nmedit.jtheme;

import java.awt.event.MouseEvent;

import net.sf.nmedit.jtheme.component.JTComponent;

public interface JTPopupHandler
{

    void showPopup(MouseEvent e, JTComponent component);
    
}
