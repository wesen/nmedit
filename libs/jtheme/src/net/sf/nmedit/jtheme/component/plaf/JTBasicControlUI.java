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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;

import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.swing.NMLazyActionMap;

public abstract class JTBasicControlUI extends JTControlUI
{

    protected static final String INCREASE = "increase";
    protected static final String DECREASE = "decrease";
    protected static final String INCREASE_FAST = "increase.fast";
    protected static final String DECREASE_FAST = "decrease.fast";
    protected static final String DEFAULTVALUE = "default.value";
    protected static final String MOVE_UP = "move.up";
    protected static final String MOVE_DOWN = "move.down";
    protected static final String MOVE_RIGHT = "move.right";
    protected static final String MOVE_LEFT = "move.left";
    
   
    
    public static final String knobActionMapKey = "knob.actionMap";

    private boolean defaultsInitialized = false;

    protected static final String CONTROL_LISTENER_CLASS_KEY = JTBasicControlUI.class.getName()
    +".CONTROL_LISTENER_CLASS";
    
    private JTControl control;
    
    public void installUI(JComponent c)
    {
        control = (JTControl) c;
        
        if (!defaultsInitialized)
        {
            initUIDefaults(control.getContext().getUIDefaults());
            defaultsInitialized = true;
        }
        
        installDefaults(control);
        

        BasicControlListener listener;
	
		listener = createControlListener(control);
		if (listener != null)
        {
            installListeners(listener, control);
            installKeyboardActions(listener, control); 
        }
	
	
       
        c.setFocusable(true);
    }

    protected void initUIDefaults(UIDefaults defaults)
    {
    	 // read the defaults here
    }

    public void uninstallUI(JComponent c)
    {
        JTControl control = (JTControl) c;
        uninstallDefaults(control);
        BasicControlListener listener = getControlListener(c);
        if (listener != null)
        {
            uninstallListeners(listener, control);
            uninstallKeyboardActions(listener, control);
        }
    }

    protected void installDefaults( JTControl control )
    {
    	
    }

    protected void uninstallDefaults( JTControl control )
    {
        // no op
    }
    
    private void installListeners( BasicControlListener listener, JTControl c )
    {
        c.addMouseListener(listener);
        c.addMouseMotionListener(listener);
        c.addFocusListener(listener);
        // component repaints itself c.addChangeListener(listener);
    }

    private void uninstallListeners( BasicControlListener listener, JTControl c )
    {
        c.removeMouseListener(listener);
        c.removeMouseMotionListener(listener);
        c.removeFocusListener(listener);
        // omponent repaints itself c.removeChangeListener(listener);
    }

    private void installKeyboardActions( BasicControlListener listener, JTControl control )
    {
        listener.installKeyboardActions(control); 
    }

    protected void uninstallKeyboardActions( BasicControlListener listener, JTControl b) 
    {
        listener.uninstallKeyboardActions(b);
    }

    protected Color getExtensionColor(PParameter parameter) {

    	JTContext context = control.getContext();
    	UIDefaults defaults = (context != null) ? context.getUIDefaults() : null;

    	if (defaults != null) {
    		String colorKey = "morph.color$"+parameter.getMorphGroup();
    		Color color = defaults.getColor(colorKey);
    		//System.out.println(color.toString());	
    		if (color != null)
    			return color;
    		else
    			return Color.magenta;
    	} else
    		return Color.magenta;	
    	//return Helper.getMorphColor(parameter, c);

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
    private BasicControlListener getControlListener(JComponent b) 
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

    
    protected static final String CONTROL_LISTENER_KEY = JTBasicControlUI.class.getName()
        +".CONTROL_LISTENER";
    
    
    private transient BasicControlListener bclInstance;
    
    //protected <T extends BasicControlListener > BasicControlListener createControlListener( JTControl control) throws JTException
    protected  BasicControlListener createControlListener( JTControl control) 
    {
        if (bclInstance != null)
            return  bclInstance; 
        
        JTContext context = control.getContext();
        UIDefaults defaults = (context != null) ? context.getUIDefaults() : null;
        
        if (defaults != null)
        {
        	
            Object l = defaults.get(CONTROL_LISTENER_KEY);
            if ((l != null) && (l instanceof BasicControlListener))
            {
                bclInstance = (BasicControlListener) l;
            }
            else
            {     
                bclInstance = new BasicControlListener();
                defaults.put(CONTROL_LISTENER_KEY, bclInstance);
         
            }
        }
        else
        {
            bclInstance = new BasicControlListener();
        }
        
        return bclInstance;
    }

    public static class BasicControlListener implements MouseListener, MouseMotionListener, 
    FocusListener
    {
        
        protected double pressedNormalizedValue;
        protected int pressedModifier;
        protected boolean selectExtensionAdapter = false;

        public static void loadActionMap(NMLazyActionMap map) 
        {  
            map.put(new Actions(INCREASE));
            map.put(new Actions(DECREASE));
            map.put(new Actions(INCREASE_FAST));
            map.put(new Actions(DECREASE_FAST));
            map.put(new Actions(DEFAULTVALUE));
            map.put(new Actions(MOVE_UP));
            map.put(new Actions(MOVE_DOWN));
            map.put(new Actions(MOVE_RIGHT));
            map.put(new Actions(MOVE_LEFT));
        }
        
        public void mouseClicked( MouseEvent e )
        {
            // no op
        }
        
        protected int getValueModifier(JTControl control, MouseEvent e)
        {
            return getValueModifier(control, e.getX(), e.getY());
        }

        private int getValueModifier(JTControl control, int x, int y)
        {
            return control.getOrientation() != SwingConstants.VERTICAL ? x : y;
        }

        private transient InputMap inputMapWhenFocused ;
		private int pressedValue;
        protected InputMap createInputMapWhenFocused()
        {
            if (inputMapWhenFocused == null)
            {
                inputMapWhenFocused = new InputMap();
                fillInputMap(inputMapWhenFocused);
            }
            return inputMapWhenFocused;
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

            addEditAction(map);
            
            addDefaultValueKS(map);
        }
        
        protected void addEditAction(InputMap map)
        {
        	 KeyStroke moveUp = KeyStroke.getKeyStroke(KeyEvent.VK_U, 0);
             map.put(moveUp, MOVE_UP);
             
             KeyStroke moveDown = KeyStroke.getKeyStroke(KeyEvent.VK_J, 0);
             map.put(moveDown, MOVE_DOWN);
             
             KeyStroke moveRight = KeyStroke.getKeyStroke(KeyEvent.VK_K, 0);
             map.put(moveRight, MOVE_RIGHT);
             
             KeyStroke moveLeft = KeyStroke.getKeyStroke(KeyEvent.VK_H, 0);
             map.put(moveLeft, MOVE_LEFT);
        }
        
        protected void addDefaultValueKS(InputMap map)
        {
            KeyStroke defaultValue = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
            map.put(defaultValue, DEFAULTVALUE);
        }
        
        public void installKeyboardActions( JTControl control )
        {
            NMLazyActionMap.installLazyActionMap(control.getContext().getUIDefaults(), 
                    control, BasicControlListener.class, knobActionMapKey);

            InputMap im = createInputMapWhenFocused();
            SwingUtilities.replaceUIInputMap(control, JComponent.WHEN_FOCUSED, im);
        }

        public void uninstallKeyboardActions(JTControl control)
        {
            SwingUtilities.replaceUIInputMap(control, JComponent.WHEN_IN_FOCUSED_WINDOW, null);
            SwingUtilities.replaceUIInputMap(control, JComponent.WHEN_FOCUSED, null);

            // TODO this line shouldn't be necessary, but if setUI() was called twice
            // each time with a new ui instance then the input map will cause a StackOverflowError
            // if a key was pressed
            control.setInputMap(JComponent.WHEN_FOCUSED, new InputMap());
            
            SwingUtilities.replaceUIActionMap(control, null);
        }
        
        public boolean isExtensionSelected(MouseEvent e)
        {
            if (Platform.isFlavor(Platform.OS.UnixFlavor))
                return e.isControlDown();
            return e.isAltDown();
        }

        public void mousePressed( MouseEvent e )
        {
            selectExtensionAdapter = false;
            
            JTControl control = controlFor(e);
            if (control == null) return;
            
            if (Platform.isPopupTrigger(e))
            {
                control.showControlPopup(e);
            } 
            else if (Platform.isLeftMouseButtonOnly(e))
            {
              
                selectExtensionAdapter = isExtensionSelected(e);
                
                pressedNormalizedValue = selectExtensionAdapter ? control.getExtNormalizedValue() :
                control.getNormalizedValue();
                pressedValue = control.getValue();
                pressedModifier = getValueModifier(control, e);
            }
            
            
            if (!control.hasFocus())
                control.requestFocus();
        }

        public void mouseReleased( MouseEvent e )
        {
            JTControl control = controlFor(e);
            if (control.getControlAdapter() != null) {
            	PParameter parameter = control.getControlAdapter().getParameter();
            	int newValue = control.getValue();
            	if (newValue != pressedValue && parameter.isUndoableEditSupportEnabled()) {
            		parameter.postEdit(parameter.createParameterValueEdit(pressedValue, newValue));
            	}
            }
            
            if (Platform.isPopupTrigger(e))
            {
                control.showControlPopup(e);
            }
            else if (e.getClickCount()>=2 && control != null)
            {
                if (selectExtensionAdapter)
                    control.setExtensionValue(control.getExtDefaultValue());
                else
                    control.setValue(control.getDefaultValue());   
            }  
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
            JTControl control = controlFor(e);

            if (Platform.isLeftMouseButtonOnly(e) && control != null)
            {
                int currentModifier = getValueModifier(control, e);

                try {
                	control.getControlAdapter().getParameter().disableUndo();
                if (control.getOrientation()!=SwingConstants.VERTICAL)
                    updateValue(control, currentModifier, pressedModifier, pressedNormalizedValue);
                else // horizontal
                    updateValue(control, pressedModifier, currentModifier, pressedNormalizedValue);
                } finally {
                	control.getControlAdapter().getParameter().enableUndo();
                }
            } 
        }

        public void updateValue(JTControl control, double currentModifier, double pressedModifier, double pressedValue)
        {
            double modifier = (currentModifier-pressedModifier)/128d;
            // assure value in range [0..1]
            double nvalue = Math.max(0, Math.min(pressedValue+modifier, 1));

            if (selectExtensionAdapter) {
                control.setExtNormalizedValue(nvalue);
                
            }
            else {
                control.setNormalizedValue(nvalue);
                
            }
        }

        public void mouseMoved( MouseEvent e )
        {
            // no op
        }

        public void focusGained( FocusEvent e )
        {
            e.getComponent().repaint();
        }

        public void focusLost( FocusEvent e )
        {
            e.getComponent().repaint();
        }

        public static class Actions extends AbstractAction 
        {
            
            // private String action;

            /**
             * 
             */
            private static final long serialVersionUID = -2104045982638755995L;

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
//                else if (key == MOVE_UP){
//                	Element docEl = control.getDocumentElement();
//                	Integer y = control.getLocation().y -1;
//                	control.setLocation(control.getLocation().x, y);
//                	docEl.setAttribute("y",y.toString());
//                }
//                else if (key == MOVE_DOWN){
//                	Element docEl = control.getDocumentElement();
//                	Integer y = control.getLocation().y + 1;
//                	control.setLocation(control.getLocation().x, y);
//                	docEl.setAttribute("y",y.toString());
//                }
//                else if (key == MOVE_RIGHT){
//                	Element docEl = control.getDocumentElement();
//                	Integer x = control.getLocation().x +1;
//                	control.setLocation(x,control.getLocation().y);
//                	docEl.setAttribute("x",x.toString());
//                }
//                else if (key == MOVE_LEFT){
//                	Element docEl = control.getDocumentElement();
//                	Integer x = control.getLocation().x -1;
//                	control.setLocation(x,control.getLocation().y);
//                	docEl.setAttribute("x",x.toString());
//                }
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

        /*
        public void stateChanged(ChangeEvent e)
        {
            JTControl c = controlFor(e);
            if (c != null) c.repaint();
        }
        */

        protected JTControl controlFor(EventObject e)
        {
            return castControl(e.getSource());
        }

        private JTControl castControl(Object src)
        {
            return (src instanceof JTControl) ? (JTControl) src : null;
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
