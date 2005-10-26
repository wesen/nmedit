/*  
 * Adapted DKnob.java by Joakim Eriksson
 */

// TODO -Combine with JModKnob.

package nomad.gui.model.component.builtin.implementation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;



public class JModKnobGrafix extends JModParameterObject
{
    private final static float START = 225;
    private final static float LENGTH = 270;
    private final static float PI = (float) Math.PI;
    private final static float START_ANG = (START/360)*PI*2;
    private final static float LENGTH_ANG = (LENGTH/360)*PI*2;
    private final static float MULTIP = 180 / PI; 
    private final static Color DEFAULT_FOCUS_COLOR = new Color(0x8080ff);
    
    private final static Color BOTTOM_COLOR = new Color(0xB0B0B0);
    private final static Color MIDDLE_COLOR = new Color(0xD0D0D0);
    private final static Color TOP_COLOR = new Color(0x707070);

    private int SHADOWX = 1;
    private int SHADOWY = 1;
    
    private float DRAG_SPEED;
    private float CLICK_SPEED;
    private float DRAG_RES;
    
    private int size;
    private int middle;
    
    public boolean indicator = false;
    public boolean label = false;
    
    public final static int HOR = 0;
    public final static int VER = 1;
    public final static int ROUND  = 2;
    public final static int DIA = 3;
    private int dragType = DIA;
    
    private final static Dimension MIN_SIZE = new Dimension(40, 40);
    private final static Dimension PREF_SIZE = new Dimension(50, 50);
    
    private final static RenderingHints AALIAS = 
	new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    private Arc2D hitArc = new Arc2D.Float(Arc2D.PIE);
    
    private float ang = (float) START_ANG;
    private int dragpos = -1;
    private float startVal = 0;
    private Color focusColor = null;
    private double lastAng = 0;
    
    Font f = new Font("Dialog", Font.PLAIN, 10);
    
    public JModKnobGrafix(int x, int y, int min_val, int max_val/*, Parameter newPar*/) {
        super(min_val, max_val/*, newPar*/);

    	DRAG_SPEED = 1/getRange();
        DRAG_RES = 1/getRange();
    	CLICK_SPEED = 1/getRange();
        
    	focusColor = DEFAULT_FOCUS_COLOR;
    	
    	setPreferredSize(PREF_SIZE);
    	hitArc.setAngleStart(235); // Degrees ??? Radians???
        
    	addMouseListener(new MouseAdapter() {
       		public void mousePressed(MouseEvent me) {
       		    dragpos = me.getX() + me.getY();
       		    startVal = getInternalValue();
        
       		    // Fix last angle
       		    int xpos = middle - me.getX();
       		    int ypos = middle - me.getY();
       		    lastAng = Math.atan2(xpos, ypos);
        
       		    requestFocus();
       		}
        		
       		public void mouseClicked(MouseEvent me) {
                // TODO -Doesn't work well.
       		    hitArc.setAngleExtent(-(LENGTH + 20));
       		    if  (hitArc.contains(me.getX(), me.getY())) {	   
       		        hitArc.setAngleExtent(MULTIP * (ang-START_ANG)-10);
       		        if  (hitArc.contains(me.getX(), me.getY())) {
       		            decValue();
       		        }
                    else
                        incValue();	
       		    }
       		}
        });
        
    	// Let the user control the knob with the mouse
    	addMouseMotionListener(new MouseMotionAdapter() {
    		public void mouseDragged(MouseEvent me) {
                if (dragType == HOR) {
                    float f = DRAG_SPEED * (float)(me.getX());
                    setInternalValue(startVal + f, true);
                }
                else
                    if (dragType == VER) {
                        float f = DRAG_SPEED * (float)(me.getY());
                        setInternalValue(startVal + f, true);
                    }
                        if (dragType == DIA) {
                            float f = DRAG_SPEED * (float)(me.getX() - me.getY());
                            setInternalValue(startVal + f, true);
                        }
                        else
                            if (dragType == ROUND) {
                                // Measure relative the middle of the button! 
                                int xpos = middle - me.getX();
                                int ypos = middle - me.getY();
                                double ang = Math.atan2(xpos, ypos);
                                double diff = lastAng - ang;
                                setInternalValue((float) (getValue() + (diff / LENGTH_ANG)), true);
          
                                lastAng = ang;
                            }
    		}
    		
    		public void mouseMoved(MouseEvent me) {
    		}
        });
        
    	// Let the user control the knob with the keyboard
    	addKeyListener(new KeyListener() {
    		
    		public void keyTyped(KeyEvent e) {}
    		public void keyReleased(KeyEvent e) {} 
    		public void keyPressed(KeyEvent e) { 
    		    int k = e.getKeyCode();
    		    if (k == KeyEvent.VK_RIGHT)
    			incValue();
    		    else if (k == KeyEvent.VK_LEFT)
    			decValue();
    		}		
        });
        	
    	// Handle focus so that the knob gets the correct focus highlighting.
    	addFocusListener(new FocusListener() {
    		public void focusGained(FocusEvent e) {
    		    repaint();
    		}
    		public void focusLost(FocusEvent e) {
    		    repaint();
    		}
        });
    }

    public void setDragType(int type) {
        dragType = type;
    }
    
    public int getDragType() {
        return dragType;
    }

    private void incValue() {
        setInternalValue(getInternalValue() + CLICK_SPEED, true);
    }
    
    private void decValue() {
        setInternalValue(getInternalValue() - CLICK_SPEED, true);
    }

    public Dimension getMinimumSize() {
        return MIN_SIZE;
    }

    public void calcMisc(boolean custom) {
        // in this case we only want to calculate if there is going ot be repainted.
        if (custom) ang = START_ANG - (float) LENGTH_ANG * getInternalValue();
    }
    
    public void updateKnob() {
        repaint();
    }
    
    // Paint the DKnob
    public void paint(Graphics g) {
        calcMisc(true); // TODO ?It wil only calculate if it's in the paint method?

        int x = 0;
        int y = 0;
        
    	int width = getWidth();
        int height = getHeight();
        int offset = 2;
        
    	size = Math.min(width, height) - (offset*2);
    	middle = offset + size/2;
    
    	if (g instanceof Graphics2D) {
    	    Graphics2D g2d = (Graphics2D) g;
    	    g2d.setBackground(getParent().getBackground());
    	    g2d.addRenderingHints(AALIAS);
    	    
    	    // For the size of the "mouse click" area
            hitArc.setFrame(4, 4, size+12, size+12);
    	}
    	
//    	// Paint the "markers"
//    	for (float a2 = START_ANG; a2 >= START_ANG - LENGTH_ANG; a2=a2 -(float)(LENGTH_ANG/10.01))
//    	    {
//        		int x = 10 + size/2 + (int)((6+size/2) * Math.cos(a2));
//        		int y = 10 + size/2 - (int)((6+size/2) * Math.sin(a2));
//        		g.drawLine(10 + size/2, 10 + size/2, x, y);
//    	    }

//    	// You use the modular to much
//        g.setColor(BOTTOM_COLOR);
//        g.fillOval(offset-2, offset-2, size+4, size+4);

        // Outside circle
        if (hasFocus())
            if (indicator && isHalfway())
                g.setColor(Color.GREEN);
            else
                g.setColor(focusColor);
        else
            if (indicator && isHalfway())
                g.setColor(Color.GREEN.darker());
            else
                g.setColor(Color.gray);
        g.drawArc(offset, offset, size, size, 315, 270);

        // Circle top..?
        g.setColor(BOTTOM_COLOR);
        g.drawOval(offset+2, offset+2, size-6, size-6);

        // Min / Max markers
        g.setColor(Color.black);
        x = offset + size/2 + (int)((6+size/3) * Math.cos(START_ANG));
        y = offset + size/2 - (int)((6+size/3) * Math.sin(START_ANG));
        g.drawLine(offset + size/2, offset + size/2, x, y);
        
        x = offset + size/2 + (int)((6+size/3) * Math.cos(START_ANG - LENGTH_ANG));
        y = offset + size/2 - (int)((6+size/3) * Math.sin(START_ANG - LENGTH_ANG));
        g.drawLine(offset + size/2, offset + size/2, x, y);
        
        // Top
        g.setColor(MIDDLE_COLOR);
        g.fillOval(offset+3, offset+3, size-6, size-6);
        g.setColor(TOP_COLOR);
        g.fillOval(offset+4, offset+4, size-8, size-8);
        
        if (label && hasFocus()) {
            drawCustomString(g);
        }
        
        // Inside needle
    	g.setColor(Color.white);
    	x = offset + size/2 + (int)(size/2 * Math.cos(ang));
    	y = offset + size/2 - (int)(size/2 * Math.sin(ang));
    	g.drawLine(offset + size/2, offset + size/2, x, y);
        
        // Outside needle
        g.setColor(Color.gray);
    	int dx = (int)(2 * Math.sin(ang));
    	int dy = (int)(2 * Math.cos(ang));
    	g.drawLine(offset + dx + size/2, offset + dy + size/2, x, y);
    	g.drawLine(offset-dx + size/2, offset-dy + size/2, x, y);
    }
    
    private void drawCustomString(Graphics g) {
        g.setFont(f);
        String s = String.valueOf(getValue());
        if (info!=null)
        	s=info.getFormattedValue(getValue());
        FontMetrics fontMetrics=getFontMetrics(g.getFont());
        Rectangle2D r = fontMetrics.getStringBounds(s, g);

        if (indicator && isHalfway())
            g.setColor(Color.GREEN);
        else
            g.setColor(focusColor.brighter());
        g.fillRect((int)((getWidth()/2)-r.getCenterX()) - 1, 0, (int)r.getWidth() + 2, 10);
        
        if (indicator && isHalfway())
            g.setColor(Color.GREEN.darker());
        else
            g.setColor(focusColor);
        g.drawRect((int)((getWidth()/2)-r.getCenterX()) - 1, 0, (int)r.getWidth() + 2, 10);
        
        g.setColor(Color.BLACK);
        g.drawString(s, (int)((getWidth()/2)-r.getCenterX()) + 1, 9);
    }
}


//public float getInternalValue() {
//return val;
//}
//
//public int getValue() {
//return (int)(val * MAX_VAL);
//}

//public void setInternalValue(float val, boolean fire) {
//if (val < 0) val = 0;
//if (val > 1) val = 1;
//this.val = val;
//ang = START_ANG - (float) LENGTH_ANG * val;
//repaint();
//fireChangeEvent();
//}

//public void setValue(float val) {
//setInternalValue(val / MAX_VAL, true);
//}

//public void setValueWithoutFireStarter(float val) {
//setInternalValue(val / MAX_VAL, false);
//}

//public void setMaxValue(float val) {
//MAX_VAL = val;
//}

//public void addChangeListener(ChangeListener cl) {
//listenerList.add(ChangeListener.class, cl);
//}
//
//public void removeChangeListener(ChangeListener cl) {
//listenerList.remove(ChangeListener.class, cl);        
//}
//protected void fireChangeEvent() {
//// Guaranteed to return a non-null array
//Object[] listeners = listenerList.getListenerList();
//// Process the listeners last to first, notifying
//// those that are interested in this event
//for (int i = listeners.length-2; i>=0; i-=2) {
//    if (listeners[i] == ChangeListener.class) {
//  // Lazily create the event:
//  if (changeEvent == null)
//      changeEvent = new ChangeEvent(this);
//  ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
//    }
//}
//}
