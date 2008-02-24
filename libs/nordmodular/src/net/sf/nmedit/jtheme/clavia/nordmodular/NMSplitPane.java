package net.sf.nmedit.jtheme.clavia.nordmodular;
import java.awt.AWTEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class NMSplitPane extends JSplitPane
{

    public static final String STORED_SPLIT_PROPERTY = "stored.split.property";
    
    /**
     * 
     */
    private static final long serialVersionUID = -4356755421821731144L;
    private int storedLocation; // we enforce this divider location
    private boolean allowOverwriteStoredLocation = false;
    private boolean wasHidden = false;

    public NMSplitPane(int verticalSplit, boolean b, JPanel panel,
            JPanel panel2)
    {
        super(verticalSplit, b, panel, panel2);
        enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
        updateUI();
        this.storedLocation = getDividerLocation();
    }

    public NMSplitPane(int newOrientation)
    {
        super(newOrientation);
    }

    protected void processComponentEvent(ComponentEvent e)
    {
        if (e.getID() == ComponentEvent.COMPONENT_RESIZED)
        {
            // restore divider location
            dividerUpdate();
        }
        super.processComponentEvent(e);
    }
    
    private void dividerUpdate()
    {
        if ((!isHidden()))//&&(getHeight()>prevHeight))
        {
            if (storedLocation>getDividerLocation())
            {
                int updateLocation = Math.min(getMaximumDividerLocation(), storedLocation);
                setLastDividerLocation(updateLocation);
                setDividerLocation(updateLocation);
            }
        }
    }

    public boolean isTopHidden()
    {
        return getDividerLocation()<getMinimumDividerLocation();
    }

    public boolean isBottomHidden()
    {
        return getDividerLocation()>getMaximumDividerLocation();
    }
    
    private boolean isHidden()
    {
        return isTopHidden()||isBottomHidden();
    }
    
    public int getStoredDividerLocation()
    {
        return storedLocation;
    }
    
    public void setStoredDividerLocation(int location)
    {
        int oldValue = this.storedLocation;
        int newValue = location;
        if (oldValue != newValue)
        {
            this.storedLocation = newValue;
            setDividerLocation(newValue);
            super.setLastDividerLocation(storedLocation);
            firePropertyChange(STORED_SPLIT_PROPERTY, oldValue, newValue);
        } 
    }

    public void setDividerLocation(int location) {
        super.setDividerLocation(location);

        boolean oldHiddenValue = wasHidden;
        boolean newHiddenValue = isHidden();
        wasHidden = newHiddenValue;
        if (allowOverwriteStoredLocation || (oldHiddenValue!=newHiddenValue))
        {
            int oldValue = this.storedLocation;
            int newValue = location;
            if (oldValue != newValue)
            {
                this.storedLocation = newValue;
                firePropertyChange(STORED_SPLIT_PROPERTY, oldValue, newValue);
            } 
        }
    }
    public void updateUI() {
        setUI(NMSplitPaneUI.createUI(this));
        revalidate();
    }

    private static class NMSplitPaneUI extends BasicSplitPaneUI
    {
        private NMSplitPane split;
        public NMSplitPaneUI(NMSplitPane split)
        {
             this.split = split;
        }
        
        public static ComponentUI createUI(JComponent x) {
            return new NMSplitPaneUI((NMSplitPane)x);
        }
        
        protected void finishDraggingTo(int location) {
            split.allowOverwriteStoredLocation = true;
            super.finishDraggingTo(location);
            split.allowOverwriteStoredLocation = false;
        }
    }
}
