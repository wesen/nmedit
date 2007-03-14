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

/*
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.swing.JTLazyActionMap;

public abstract class JTBasicControlUI extends JTControlUI
{

    protected static final String INCREASE = "increase";
    protected static final String DECREASE = "decrease";
    protected static final String INCREASE_FAST = "increase.fast";
    protected static final String DECREASE_FAST = "decrease.fast";
    protected static final String DEFAULTVALUE = "default.value";

    public static final String knobActionMapKey = "knob.actionMap";
    
    public void installUI(JComponent c)
    {
        installDefaults((JTControl) c);
        installListeners((JTControl) c);
        installKeyboardActions((JTControl) c); 
        c.setFocusable(true);
    }

    public void uninstallUI(JComponent c)
    {
        uninstallDefaults((JTControl) c);
        uninstallListeners((JTControl) c);
        uninstallKeyboardActions((JTControl) c);
    }

    protected void installDefaults( JTControl control )
    {
        // no op
    }

    protected void uninstallDefaults( JTControl control )
    {
        // no op
    }
    
    private void installListeners( JTControl c )
    {
        BasicControlListener listener = createControlListener(c);
        if(listener != null) 
        {
            c.addMouseListener(listener);
            c.addMouseMotionListener(listener);
            c.addFocusListener(listener);
            c.addChangeListener(listener);
        }
    }

    private void uninstallListeners( JTControl c )
    {
        BasicControlListener listener = getControlListener(c);
        if(listener != null) 
        {
            c.removeMouseListener(listener);
            c.removeMouseMotionListener(listener);
            c.removeFocusListener(listener);
            c.removeChangeListener(listener);
        }
    }

    private void installKeyboardActions( JTControl control )
    {
        BasicControlListener listener = getControlListener(control);
        
        if(listener != null) 
        {
            listener.installKeyboardActions(control);
        }    
    }

    protected void uninstallKeyboardActions(JTControl b) 
    {
        BasicControlListener listener = getControlListener(b);
        if(listener != null) 
        {
            listener.uninstallKeyboardActions(b);
        }
    }

    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        // no op
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        // no op
    }

    protected static double getDouble(UIDefaults defaults, String key, double defaultValue)
    {
        Object obj = defaults.get(key);
        
        if (obj != null)
        {
            if (obj instanceof Float)
                return (Float) obj;
            if (obj instanceof Double)
                return (Double) obj;
        }
        return defaultValue;
    }

    /**
     * Returns the KnobListener for the passed in Knob, or null if one
     * could not be found.
     */
    private BasicControlListener getControlListener(JTControl b) 
    {
        MouseMotionListener[] listeners = b.getMouseMotionListeners();

        if (listeners != null) {
            for (int counter = 0; counter < listeners.length; counter++) {
                if (listeners[counter] instanceof BasicControlListener) {
                    return (BasicControlListener)listeners[counter];
                }
            }
        }
        return null;
    }

    protected BasicControlListener createControlListener(JTControl control) 
    {
        return new BasicControlListener(control);
    }

    public static class BasicControlListener implements MouseListener, MouseMotionListener, 
    FocusListener, ChangeListener
    {
        
        protected JTControl control;
        protected double pressedValue;
        protected int pressedModifier;

        public static void loadActionMap(JTLazyActionMap map) 
        {  
            map.put(new Actions(INCREASE));
            map.put(new Actions(DECREASE));
            map.put(new Actions(INCREASE_FAST));
            map.put(new Actions(DECREASE_FAST));
            map.put(new Actions(DEFAULTVALUE));
        }
        
        public BasicControlListener( JTControl control )
        {
            this.control = control;
        }

        public void mouseClicked( MouseEvent e )
        {
            // no op
        }
        
        protected int getValueModifier(MouseEvent e)
        {
            return getValueModifier(e.getX(), e.getY());
        }

        private int getValueModifier(int x, int y)
        {
            return control.getOrientation() != SwingConstants.VERTICAL ? x : y;
        }

        protected InputMap createInputMapWhenFocused()
        {
            InputMap map = new InputMap();
            fillInputMap(map);
            return map;
        }
        
        protected void fillInputMap(InputMap map)
        {
            KeyStroke increaseValue = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
            map.put(increaseValue, INCREASE);
            
            KeyStroke decreaseValue = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
            map.put(decreaseValue, DECREASE);
            
            KeyStroke increaseValueFast = KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK);
            map.put(increaseValueFast, INCREASE_FAST);

            KeyStroke decreaseValueFast = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK);
            map.put(decreaseValueFast, DECREASE_FAST);

            addDefaultValueKS(map);
        }
        
        protected void addDefaultValueKS(InputMap map)
        {
            KeyStroke defaultValue = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
            map.put(defaultValue, DEFAULTVALUE);
        }
        
        public void installKeyboardActions( JTControl control )
        {
            JTLazyActionMap.installLazyActionMap(control.getContext().getUIDefaults(), 
                    control, BasicControlListener.class, knobActionMapKey);

            InputMap im = createInputMapWhenFocused();
            SwingUtilities.replaceUIInputMap(control, JComponent.WHEN_FOCUSED, im);
        }

        public void uninstallKeyboardActions(JTControl control)
        {
            SwingUtilities.replaceUIInputMap(control, JComponent.WHEN_IN_FOCUSED_WINDOW, null);
            SwingUtilities.replaceUIInputMap(control, JComponent.WHEN_FOCUSED, null);
            SwingUtilities.replaceUIActionMap(control, null);
        }

        public void mousePressed( MouseEvent e )
        {
            pressedValue = control.getNormalizedValue();
            pressedModifier = getValueModifier(e);
            
            if (!control.hasFocus())
                control.requestFocus();
        }

        public void mouseReleased( MouseEvent e )
        {
            if (e.getClickCount()>=2)
                control.setValue(control.getDefaultValue());
        }

        public void mouseEntered( MouseEvent e )
        {
            // no op
        }

        public void mouseExited( MouseEvent e )
        {
            // no op
        }

        public void mouseDragged( MouseEvent e )
        {
            if (SwingUtilities.isLeftMouseButton(e))
            {
                int currentModifier = getValueModifier(e);
                
                if (control.getOrientation()!=SwingConstants.VERTICAL)
                    updateValue(currentModifier, pressedModifier, pressedValue);
                else // horizontal
                    updateValue(pressedModifier, currentModifier, pressedValue);
            }
        }

        public void updateValue(double currentModifier, double pressedModifier, double pressedValue)
        {
            double modifier = (currentModifier-pressedModifier)/200d;
            // assure value in range [0..1]
            double nvalue = Math.max(0, Math.min(pressedValue+modifier, 1));
            control.setNormalizedValue(nvalue);
        }

        public void mouseMoved( MouseEvent e )
        {
            // no op
        }

        public void focusGained( FocusEvent e )
        {
            control.repaint();
        }

        public void focusLost( FocusEvent e )
        {
            control.repaint();   
        }

        public static class Actions extends AbstractAction 
        {
            
            // private String action;

            public Actions(String name)
            {
                super(name);
            }

            public String getName()
            {
                return (String) super.getValue(NAME);
            }
            
            public void actionPerformed(ActionEvent e)
            {
                JTControl control = (JTControl) e.getSource();
                
                String key = getName();
                
                if (key == INCREASE)
                    control.setValue(control.getValue()+1);
                else if (key == DECREASE)
                    control.setValue(control.getValue()-1);
                else if (key == INCREASE_FAST)
                    control.setValue(control.getValue()+5);
                else if (key == DECREASE_FAST)
                    control.setValue(control.getValue()-5);
                else if (key == DEFAULTVALUE)
                    control.setValue(control.getDefaultValue());
            }

            public boolean isEnabled(Object sender) 
            {
                
                if (sender != null && (sender instanceof JTControl)
                        && !((JTControl)sender).isEnabled())
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }

        public void stateChanged(ChangeEvent e)
        {
            control.repaint();
        }
        
        /*
        // should be in UIDefaults
        public static final int MOUSE_WHEEL_INCREMENT = 2;

        public void mouseWheelMoved(MouseWheelEvent e)
        {
            int rotation = e.getWheelRotation() * MOUSE_WHEEL_INCREMENT;
            
            int newvalue = control.getValue()-rotation;
            newvalue = Math.max(control.getMinValue(), Math.min(newvalue, control.getMaxValue()));
            
            control.setValue(newvalue);
        }
        */
    }

}
