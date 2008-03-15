/* Copyright (C) 2006 Christian Schneider
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
package net.sf.nmedit.nomad.core.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nomad.core.misc.FocusStroke;

public class LinkLabel extends JLabel
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -864966524391235612L;
    private boolean hovered = false;
    private String plainTitle;
    private String hoverTitle;

    public LinkLabel(String title)
    {
        super(title);
        setCursor(getHandCursor());
        setBorder(FocusStroke.getFocusStrokeBorder(this, Color.BLACK));
        this.plainTitle = title == null ? "" : title;
        this.hoverTitle = title == null ? "" : underline(title);
        setForeground(Color.BLUE);
        (new EventHandler()).install(this);
    }

    private static transient Cursor handCursor;
    
    private static Cursor getHandCursor()
    {
        if (handCursor  == null)
            handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        return handCursor;
    }

    public void addActionListener(ActionListener l)
    {
        listenerList.add(ActionListener.class, l);
    }
    
    public void removeActionListener(ActionListener l)
    {
        listenerList.remove(ActionListener.class, l);
    }
    
    protected void setHovered(boolean hovered)
    {
        if (this.hovered != hovered)
        {
            this.hovered = hovered;
            setText(hovered ? hoverTitle : plainTitle);
        }
    }

    public void jump()
    {
        ActionEvent actionEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                // Lazily create the event:
                if (actionEvent == null)
                    actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "jump");
                ((ActionListener)listeners[i+1]).actionPerformed(actionEvent);
            }
        }
    }
    
    private static class EventHandler extends MouseAdapter implements KeyListener
    {
        
        public void install(LinkLabel label)
        {
            label.setFocusable(true);
            label.addMouseListener(this);
            label.addKeyListener(this);
        }

        public void mousePressed(MouseEvent e)
        {
             ((LinkLabel) e.getComponent()).requestFocus();
        }
        
        public void mouseExited(MouseEvent e)
        {
            ((LinkLabel) e.getComponent()).setHovered(false);
        }
        
        public void mouseEntered(MouseEvent e)
        {
            ((LinkLabel) e.getComponent()).setHovered(true);
        }
        
        public void mouseClicked(MouseEvent e)
        {
            if (Platform.isLeftMouseButtonOnly(e))
                ((LinkLabel)e.getComponent()).jump();
        }
        
        public void keyPressed(KeyEvent e)
        {
            if (e.getModifiers() == 0 && e.getKeyCode() == KeyEvent.VK_ENTER)
                ((LinkLabel)e.getComponent()).jump();
        }

        public void keyReleased(KeyEvent e)
        {
            // no op
        }

        public void keyTyped(KeyEvent e)
        {
            // no op
        }

    }

    public static String underline(String title)
    {
        return styleLabel(false, true, false, title);
    }
    
    public static String styleLabel(boolean bold, boolean underline, boolean italic, String text)
    {
        StringBuilder sb = new StringBuilder("<html><body>");
        if (bold) sb.append("<b>");
        if (underline) sb.append("<u>");
        if (italic) sb.append("<i>");
        sb.append(text);
        if (italic) sb.append("</i>");
        if (underline) sb.append("</u>");
        if (bold) sb.append("</b>");
        sb.append("</body></html>");
        return sb.toString();
    }

}
