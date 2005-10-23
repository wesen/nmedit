package editor;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Draggable extends MouseAdapter implements MouseMotionListener {
    Point mLastPoint;
    Component mWindow;
    public Draggable(Component w) {
        w.addMouseMotionListener(this);
        w.addMouseListener(this);
        mWindow = w;
    }
    public void mousePressed(MouseEvent me) {
        mLastPoint = me.getPoint();
    }
    
    public void mouseReleased(MouseEvent me) {
        mLastPoint = null;
    }
    public void mouseMoved(MouseEvent me) {}
    public void mouseDragged(MouseEvent me) {
    	if (!javax.swing.SwingUtilities.isRightMouseButton(me))
    		return;
    	
        int x, y;
        
        if (mLastPoint != null) {
            x = mWindow.getX() + (me.getX() - (int)mLastPoint.getX());
            y = mWindow.getY() + (me.getY() - (int)mLastPoint.getY());
            mWindow.setLocation(x, y);
        }
    }
}