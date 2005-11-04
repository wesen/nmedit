package nomad.gui.model.component.builtin.implementation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.misc.JPaintComponent;

public class SimpleSlider extends JPaintComponent {

	public final static Color bgDefault = Color.decode("#DCDCDC"); 
	public final static Color clGreenDark = Color.decode("#008C00");
	public final static Color clGreenLight = Color.decode("#ACD0AC");

	public final static Color clRedDark = Color.decode("#D34B4B");
	public final static Color clRedLight = Color.decode("#DCAEAE");

	public final static Color clBlueDark = Color.decode("#5052CD");
	public final static Color clBlueLight = Color.decode("#B7B9C8");

	public final static Color clYellowDark = Color.decode("#CEB800");
	public final static Color clYellowLight = Color.decode("#DDCC9E");

	public final static int MORPH_COLOR_PAIR_GREEN = 1;
	public final static int MORPH_COLOR_PAIR_RED = 2;
	public final static int MORPH_COLOR_PAIR_BLUE = 3;
	public final static int MORPH_COLOR_PAIR_YELLOW = 4;
	
	private int minValue = 0;
	private int maxValue = 127;
	private int value = 0;
	private int morphVector = 0;
	private boolean morphEnabled = false;
	private ArrayList changeListeners = new ArrayList();

	private Color fgMorph = clRedDark;
	private Color bgMorph = clRedLight;
	private Border grabBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	private boolean modern = false;
	private Color barColor = Color.BLUE;
	
	public SimpleSlider() {
		setBackground(bgDefault);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		SliderMouseEventHandler hnd = new SliderMouseEventHandler();
		addMouseListener(hnd);
		addMouseMotionListener(hnd);
	}
	
	public void useModernStyle(Color barColor) {
		// TODO colors
		modern = true;
		this.barColor = barColor; 
		setOpaque(false);
		setBorder(null);
		repaint();
	}
	
	public int getInnerX() {
		Border border = getBorder();
		return border==null?0: getBorder().getBorderInsets(this).left;
	}
	
	public int getInnerY() {
		Border border = getBorder();
		return border==null?0: getBorder().getBorderInsets(this).top;
	}
	
	public int getInnerW() {
		Border border = getBorder();
		if (border!=null) {
			Insets insets = getBorder().getBorderInsets(this);
			return getWidth() - (insets.left+insets.right);
		} else
			return getWidth();
	}

	public int getInnerH() {
		Border border = getBorder();
		if (border!=null) {
			Insets insets = getBorder().getBorderInsets(this);
			return getHeight() - (insets.top+insets.bottom);
		} else
			return getHeight();
	}
	
	public void setMorhVisible(boolean visible) {
		if (morphEnabled!=visible) {
			this.morphEnabled = visible;
			morphVector=10;
			repaint();
		}
	}
	
	public void setMorphColorPair(Color bgMorph, Color fgMorph) {
		this.fgMorph = fgMorph;
		this.bgMorph = bgMorph;
		repaint(); 
	}

	private boolean isMorphVisible() {
		return this.morphEnabled;
	}
	
	public void setMorphColorPair(int morph_color_pair) {
		switch (morph_color_pair) {
			case MORPH_COLOR_PAIR_GREEN:
				setMorphColorPair(clGreenLight, clGreenDark);
				break;
			case MORPH_COLOR_PAIR_RED:
				setMorphColorPair(clRedLight, clRedDark);
				break;
			case MORPH_COLOR_PAIR_BLUE:
				setMorphColorPair(clBlueLight, clBlueDark);
				break;
			case MORPH_COLOR_PAIR_YELLOW:
				setMorphColorPair(clYellowLight, clYellowDark);
				break; 
		}
	}
	
	private final int slHeight = 4; // slider height

	public void paintBuffer(Graphics g) {
		paintSlider(getSliderY(value), g, false);
	}

	private void paintSlider(int sliderY, Graphics g, boolean paintBorder) {
		int w = getWidth();
		int h = getHeight();
		
		// background
		if (isOpaque()) {
			if (!morphEnabled) {			
				g.setColor(getBackground());
				g.fillRect(0, 0, w, h);
			} else {
				g.setColor(bgMorph);
				g.fillRect(0, 0, w, h);
			}
		}

		// slider - grabber
		
		int slY = getInnerY()+sliderY; 

		if (modern) {
			// draw bars
			final int barheight = 1;
			int il = getInnerX();
			int iw = getInnerW();
			g.setColor(barColor);
			if (morphEnabled) {
				if (morphVector!=0) {
					g.setColor(fgMorph);
					int mh = getSliderY(morphVector);
					if (mh<0) {
						for (int i=slY+slY%4;i>=slY+mh;i-=barheight*2) {
							g.fillRect(il+1,i, 4, barheight);
						}
					} else {
						for (int i=slY+slY%4;i<=slY+mh;i+=barheight*2) {
							g.fillRect(il+1,i, 4, barheight);
						}
					}
				}
				il+=4;
				iw-=4;
			}

			g.setColor(getForeground());
			for (int i=getHeight()-1-barheight;i>=slY;i-=barheight*2) {
				g.fillRect(il+1,i, iw-2, barheight);
			}
		} else {
			// draw morph rect
			if (morphEnabled) {
				g.setColor(fgMorph);
				if (morphVector!=0) {
					int mh = getSliderY(morphVector);
					g.fillRect(0, slY+(mh<0?mh:0), w, Math.abs(mh));
				}
			}
			
			// draw grabber
			g.setColor(getBackground());
			grabBorder.paintBorder(this, g, getInnerX(), slY, getInnerW(), slHeight);
		}

		if (paintBorder && getBorder()!=null)
			getBorder().paintBorder(this, g, 0, 0, w, h);
	}
	
	private int getSliderY(int value) {
		return getInnerH()-/*getInnerH()-*/( (value-minValue)*(getInnerH()-slHeight)/(maxValue-minValue+1) );
	}
	
	private int getValueFromY(int y) {
		//y-=getInnerY();
		int val = minValue+((getInnerH()-y)*(maxValue-minValue+1)/(getInnerH()-slHeight));
		if (val<minValue)
			return minValue;
		if (val>maxValue)
			return maxValue;
		else
			return val;
	}
	
	public void setValue(int value) {
		if (isValueInBounds(value)&&value!=this.value) {
			this.value = value;
			repaint();
			fireChangeEvent();
		}
	}
	
	public void fireChangeEvent() {
		ChangeEvent event = new ChangeEvent(this);
		for (int i=0;i<changeListeners.size();i++)
			((ChangeListener)changeListeners.get(i))
				.stateChanged(event);
	}
	
	public void addChangeListener(ChangeListener listener) {
		if (!changeListeners.contains(listener))
			changeListeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		if (changeListeners.contains(listener))
			changeListeners.remove(listener);
	}	
	
	public void setMorphVector(int relative) {
		if (morphVector!=relative) {
			morphVector=relative;
			repaint();
		}
	}
	
	private boolean isValueInBounds(int value) {
		return minValue<=value&&value<=maxValue;
	}
	
	private class SliderMouseEventHandler implements MouseListener,
		MouseMotionListener{
		
		private boolean isDragging = false;
		private boolean isModifiesValue = true;

		/* MouseListener */
		
		public void mouseClicked(MouseEvent event) {
			
		}

		public void mousePressed(MouseEvent event) {
			
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			int my = event.getY();
			int sy = getSliderY(my);
			
			isDragging = (Math.abs(my-sy)<5);
			isModifiesValue = !event.isControlDown();
			if (!isModifiesValue) {
				if (!isMorphVisible()) {
					setMorhVisible(true);
				}
			}
		}

		public void mouseReleased(MouseEvent event) {
			if (isDragging) {
				int my = event.getY();
				int newValue = getValueFromY(my);
				isDragging = false;
				setValue(newValue);
			}
		}
		
		public void mouseEntered(MouseEvent event) { }

		public void mouseExited(MouseEvent event) { }
		
		/* MouseMotionListener */

		public void mouseDragged(MouseEvent event) {
			if (isModifiesValue) {
				setValue(getValueFromY(event.getY()));
			} else {
				setMorphVector(getValueFromY(event.getY())-value);
			}
		}

		public void mouseMoved(MouseEvent event) {	}
		
	}

	public void setMinValue(int value) {
		if (minValue!=value) {
			this.minValue = value;
			repaint();
		} 
	}

	public void setMaxValue(int value) {
		if (maxValue!=value) {
			this.maxValue = value;
			repaint();
		} 
	}

	public int getValue() {
		return value;
	}
	
}
