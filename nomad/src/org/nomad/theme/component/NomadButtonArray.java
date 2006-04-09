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
 * Created on Jan 12, 2006
 */
package org.nomad.theme.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.border.Border;

import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;

import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.component.model.NomadButtonArrayBehaviour;
import org.nomad.theme.component.model.NomadButtonArrayModel;
import org.nomad.theme.property.BooleanValue;
import org.nomad.theme.property.Property;
import org.nomad.theme.property.PropertySet;
import org.nomad.theme.property.Value;
import org.nomad.theme.property.editor.CheckBoxEditor;
import org.nomad.theme.property.editor.Editor;
import org.nomad.theme.property.editor.TextEditor;
import org.nomad.util.graphics.ImageTracker;
import org.nomad.util.misc.NomadUtilities;

public class NomadButtonArray extends NomadControl implements NomadButtonArrayModel {

	public final static boolean DEFAULT_CYCLIC = true;
	public final static boolean DEFAULT_LANDSCAPE = false;
	private NomadButtonArrayBehaviour behaviour = new NomadButtonArrayBehaviour(this);
	private boolean flagIsCylicDisplay = DEFAULT_CYCLIC;
	private boolean flagLandscape = DEFAULT_LANDSCAPE;
	//private ImageString[] labelList = new ImageString[6];
	private final int FIELD_COUNT = 6;
	private String[] ltext = new String[FIELD_COUNT];
	private Image[] limage = new Image[FIELD_COUNT];
	
	private static MouseAdapter buttonArrayMouseListener = new MouseAdapter(){
		public void mouseClicked(MouseEvent event) {
			if (event.getSource() instanceof NomadButtonArray) {
				NomadButtonArray nba = (NomadButtonArray) event.getSource();
				nba.requestFocus();
				nba.behaviour.calculateMetrics();
				
				if (nba.flagIsCylicDisplay) { 
					int v = nba.getValue()+1;
					if (v>nba.getMaxValue()) v = nba.getMinValue();
					nba.setValue(v);
				} else {
					nba.setValue(nba.behaviour.getButtonIndexAt(event.getPoint()));
				}
			}
		}
	};
	
	private static Dimension prefSize = new Dimension(30,10);
	private static Border DEFAULT_SEL_Border = NomadBorderFactory.createNordEditor311LoweredButtonBorder();
	private static Border DEFAULT_Border = NomadBorderFactory.createNordEditor311RaisedButtonBorder();
	
	private final static Font defaultFont = new Font("SansSerif", Font.PLAIN, 10);
	
	//private ImageTracker itracker;
	
	public NomadButtonArray() {
		//itracker=NomadEnvironment.sharedInstance().getImageTracker();
		setBackground(NomadClassicColors.BUTTON_BACKGROUND);
		//setForeground(NomadClassicColors.BUTTON_FOREGROUND);
		setFont(defaultFont);
		setDynamicOverlay(true);
		setFocusable(true);

		Arrays.fill(ltext, null);
		Arrays.fill(limage, null);

		setSelectionBorder(DEFAULT_SEL_Border);
		setDefaultBorder(DEFAULT_Border);
		addMouseListener(buttonArrayMouseListener);
		//autoResize(false);
		setSize(prefSize);
	}

	public void registerProperties(PropertySet set) {
		super.registerProperties(set);
		for (int i=FIELD_COUNT-1;i>=0;i--) {
			set.add(new BtnTextProperty(i));
		}
		set.add(new OrientationProperty());
		set.add(new CyclicProperty());
	}

	public void autoResize(boolean force) {
		/*if (force||(lastW!=getWidth())||(lastH!=getHeight())) {
/*			behaviour.calculateMetrics();
			Dimension d = behaviour.getPreferredSize();
			setSize(d);
			lastW = getWidth();
			lastH = getHeight(); 
		}*/
	}

	public int getButtonCount() {
		return flagIsCylicDisplay ? 1 : getRange()+1;
	}

	private Dimension getLabelSize(int index) {
		Image img = limage[index];
		if (img!=null)
			return new Dimension(img.getWidth(null),img.getHeight(null));
		else if (ltext!=null) 
			return NomadLabel
				.getStringBounds(this, ltext[index])
				.getSize();
		else
			return new Dimension(0,0);
	}
	
	private Dimension getMaxLabelSize() {
		Dimension d = new Dimension(6,6);
		for (int i=0;i<FIELD_COUNT;i++) {
			Dimension c = getLabelSize(i);
			d.width=Math.max(d.width,c.width);
			d.height=Math.max(d.height,c.height);
		}
		return d;
	}

	public Dimension getPreferredCellSize(int index) {
		if (flagIsCylicDisplay) {
			return getMaxLabelSize();
		} else {
			return getLabelSize(index);
		}
	}

	private Pattern btnNamePattern = Pattern.compile("btn#(\\d+)");

	private static String encodeButtonName(int index) {
		return "btn#"+index;
	}
	
	public int decodeButtonIndex(String buttonName) {
		if (btnNamePattern.matcher(buttonName).matches()) {
			String[] btn = getName().split("#");
			return Integer.parseInt(btn[1]);
		} else {
			return -1;
		}
	}
	/*
	private void setLabel(int index, String text) {
        ltext[index] = text;
        String key = NomadUtilities.extractKeyFromImageString(text);
        ImageTracker itracker = NomadEnvironment.sharedInstance().getImageTracker();
        if (key!=null&&itracker!=null)
            limage[index]=itracker.getImage(key);
	}*/
    
	/*
	private ImageString newLabel(String label) {
		ImageString istring = new ImageString(label);
		if (getEnvironment()!=null)
			istring.loadImage(getEnvironment().getImageTracker());
		return istring;
	}*/

	public void setButton(int index, String label) {
		if (0<=index && index<FIELD_COUNT) {
			if (label.length()==0) {
				ltext[index] = null;
				limage[index] = null;
			} else {
				//setLabel(index, label);
                ltext[index] = label;
                String key = NomadUtilities.extractKeyFromImageString(label);
                ImageTracker itracker = NomadEnvironment.sharedInstance().getImageTracker();
                if (key!=null&&itracker!=null)
                    limage[index]=itracker.getImage(key);
			}
			repaint();
		}
	}

	public void removeButton(int index) {
		if (0<=index && index<FIELD_COUNT) {
			ltext[index] = null;
			limage[index] = null;
			repaint();
		}
	}

	public boolean isLandscape() {
		return flagLandscape;
	}
	
	public void setLandscape(boolean landscape) {
		if (this.flagLandscape!=landscape) {
			this.flagLandscape = landscape;
			autoResize(true);
		}
	}

	public void paintDecoration(Graphics2D g2) {
	}

	public void setSelectionBorder(Border border) {
		selBorder = border;
		repaint();
	}
	
	public void setDefaultBorder(Border border) {
		defBorder = border;
		repaint();
	}

	private Color focusedColor = NomadClassicColors.BUTTON_FOCUSED_BACKGROUND;
	private Color selectedColor= NomadClassicColors.BUTTON_SELECTED_BACKGROUND;
	
	public void setButtonFocusedColor(Color color) {
		if (this.focusedColor!=color) {
			this.focusedColor = color;
			repaint();
		}
	}
	
	public void setButtonSelectedColor(Color color) {
		if (this.selectedColor!=color) {
			this.selectedColor = color;
			repaint();
		}
	}

	private Border defBorder = null;
	private Border selBorder = null;
	
	public void paintDynamicOverlay(Graphics2D g2) {
		behaviour.calculateMetrics();

		g2.setFont(getFont());
		Border b = defBorder;

		if (flagIsCylicDisplay) {
			int i = getValue();
			if (getValue()!=0)
				b=selBorder;
			
			String text = "";
			Image img = null;
			
			if (0<=i && i<FIELD_COUNT && ltext[i]!=null) {
				img = limage[i];
				text= ltext[i];
			}
			
			Rectangle bounds;
			
			if (img!=null)
				bounds = new Rectangle(0,0,img.getWidth(null),img.getHeight(null));
			else if (text!=null)
				bounds = NomadLabel.getStringBounds(this, text);
			else
				bounds = new Rectangle(0,0,0,0);
			
			Point cell = behaviour.getCell(0);

			if (getValue()==1)
				g2.setColor(selectedColor);
			else
				g2.setColor(hasFocus()?focusedColor:getBackground());

			g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			
			g2.setColor(Color.WHITE);

			b.paintBorder(this, g2, cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			//g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
			
			bounds.x += (behaviour.getCellWidth()-bounds.width)/2;
			bounds.y += (behaviour.getCellHeight()-bounds.height)/2 ;

			if (img!=null) {
				g2.drawImage(img, bounds.x, bounds.y, this);
			} else if (text!=null) {
				g2.setColor(Color.BLACK);
				g2.drawString(text, cell.x+bounds.x, cell.y+bounds.y+1);
			}
		}	else {	

			for (int i=getButtonCount()-1;i>=0;i--) {


				String text = "";
				Image img = null;
				
				if (0<=i && i<FIELD_COUNT && ltext[i]!=null) {
					img = limage[i];
					text= ltext[i];
				}
				
				Rectangle bounds;

				if (img!=null)
					bounds = new Rectangle(0,0,img.getWidth(null),img.getHeight(null));
				else if (text!=null)
					bounds = NomadLabel.getStringBounds(this, text);
				else
					bounds = new Rectangle(0,0,0,0);
				
				Point cell = behaviour.getCell(i);

				if (getValue()==i)
					g2.setColor(selectedColor);
				else
					g2.setColor(hasFocus()?focusedColor:getBackground());

				g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				
				g2.setColor(Color.WHITE);
				if (i==getValue()) {
					b=selBorder;
				} else {
					b=defBorder;
				}
				b.paintBorder(this, g2, cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				//g2.fillRect(cell.x, cell.y, behaviour.getCellWidth(), behaviour.getCellHeight());
				
				bounds.x += (behaviour.getCellWidth()-bounds.width)/2 ;
				bounds.y += (behaviour.getCellHeight()-bounds.height)/2 ;
				
				if (img!=null) {
					g2.drawImage(img, cell.x+bounds.x, cell.y+bounds.y, this);
				} else if (text!=null) {
					g2.setColor(Color.BLACK);
					g2.drawString(text, cell.x+bounds.x, cell.y+bounds.y+1);
				}
			}
			
		}
	}
    

    private static class BtnTextProperty extends Property {

        private int buttonIndex;

        public BtnTextProperty( int buttonIndex )
        {
            super( NomadButtonArray.encodeButtonName(buttonIndex) );
            this.buttonIndex = buttonIndex ;
        }

        @Override
        public Value decode( String value )
        {
            return new BtnTextValue(this, value);
        }

        @Override
        public Value encode( NomadComponent component )
        {
            return new BtnTextValue(this, ((NomadButtonArray)component).ltext[getButtonIndex()]);
        }

        private int getButtonIndex()
        {
            return buttonIndex;
        }

        @Override
        public Editor newEditor( NomadComponent component )
        {
            return new TextEditor(this, component);
        }
        
    }
    
    private static class BtnTextValue extends Value
    {
        public BtnTextValue( BtnTextProperty property, String representation )
        {
            super( property, representation );
            setDefaultState(representation==null);
        }
        
        public BtnTextProperty getBtnTextProperty()
        {
            return (BtnTextProperty) getProperty();
        }

        @Override
        public void assignTo( NomadComponent component )
        {
            NomadButtonArray b = (NomadButtonArray) component;
            b.setButton( getBtnTextProperty().getButtonIndex(), getRepresentation() );
        }
        
    }
    
/*
	private static class BtnTextProperty extends Property {
		int index;
		public BtnTextProperty(int index) {
			this.index=index;
			setName(NomadButtonArray.encodeButtonName(index));
		}

		public String getValue(NomadComponent component) {
			return ((NomadButtonArray)component).ltext[index];
		}

		public void setValue(NomadComponent component, String value) {
			if (0<=index && index<((NomadButtonArray)component).ltext.length) {
				((NomadButtonArray)component).setButton(index, value);
			}
		}

		public boolean isExportable(NomadComponent component) {
			return ((NomadButtonArray)component).ltext[index]!=null;
		}

		public PropertyValue getValue(String value) {
			return new StringValue(this, value);
		}
		
	}
*/
    
    private static class OrientationProperty extends Property
    {

        public OrientationProperty(  )
        {
            super( "landscape" );
        }

        @Override
        public Value decode( String value )
        {
            return new OrientationValue(this, value);
        }

        @Override
        public Value encode( NomadComponent component )
        {
            return new OrientationValue(this, ((NomadButtonArray) component).isLandscape());
        }

        @Override
        public Editor newEditor( NomadComponent component )
        {
            CheckBoxEditor editor = new CheckBoxEditor(this, component);
            editor.setCheckedValue(new OrientationValue(OrientationProperty.this, true));
            editor.setUncheckedValue(new OrientationValue(OrientationProperty.this, false));
            editor.setSelected( ((NomadButtonArray)component).isLandscape() );
            return editor;
        }
        
    }

    private static class OrientationValue extends BooleanValue
    {

        public OrientationValue( Property property, boolean value )
        {
            super( property, value );
        }

        public OrientationValue( Property property, String representation )
        {
            super( property, representation );
        }

        @Override
        public void assignTo( NomadComponent component )
        {
            ((NomadButtonArray) component).setLandscape(getBooleanValue());
        }
        
    }
    
    /*
	private static class OrientationProperty extends BooleanProperty {

		public OrientationProperty() { setName("landscape"); }

		public void setBoolean(NomadComponent component, boolean value) {
			((NomadButtonArray)component).setLandscape(value);
		}

		public boolean getBoolean(NomadComponent component) {
			return ((NomadButtonArray)component).isLandscape();
		}

		public boolean isInDefaultState(NomadComponent component) {
			return ((NomadButtonArray)component).flagLandscape==DEFAULT_LANDSCAPE;
		}

		public PropertyValue getCurrentValue(NomadComponent component) {
			return new BooleanValue(this, getBoolean(component));
		}
	}
	*/
    

    private static class CyclicProperty extends Property
    {

        public CyclicProperty()
        {
            super( "cyclic" );
        }

        @Override
        public Value decode( String value )
        {
            return new CyclicValue(this, value);
        }

        @Override
        public Value encode( NomadComponent component )
        {
            return new CyclicValue(this, ((NomadButtonArray) component).isCylicDisplay());
        }

        @Override
        public Editor newEditor( NomadComponent component )
        {
            CheckBoxEditor editor = new CheckBoxEditor(this, component);
            editor.setCheckedValue(new CyclicValue(CyclicProperty.this, true));
            editor.setUncheckedValue(new CyclicValue(CyclicProperty.this, false));
            editor.setSelected( ((NomadButtonArray)component).isCylicDisplay() );
            return editor;
        }
        
    }

    private static class CyclicValue extends BooleanValue
    {

        public CyclicValue( Property property, boolean value )
        {
            super( property, value );
        }

        public CyclicValue( Property property, String representation )
        {
            super( property, representation );
        }

        @Override
        public void assignTo( NomadComponent component )
        {
            ((NomadButtonArray) component).setIsCylicDisplay(getBooleanValue());
        }
        
    }
    
    /*
	private static class CyclicProperty extends BooleanProperty {

		public CyclicProperty() {
			setName("cyclic");
		}

		public void setBoolean(NomadComponent component, boolean value) {
			((NomadButtonArray)component).setIsCylicDisplay(value);
		}

		public boolean getBoolean(NomadComponent component) {
			return ((NomadButtonArray)component).isCylicDisplay();
		}

		public boolean isInDefaultState(NomadComponent component) {
			return ((NomadButtonArray)component).flagIsCylicDisplay==DEFAULT_CYCLIC;
		}

		public PropertyValue getCurrentValue(NomadComponent component) {
			return new BooleanValue(this, getBoolean(component));
		}
	}
*/
	public boolean isCylicDisplay() {
		return flagIsCylicDisplay;
	}

	public void setIsCylicDisplay(boolean isCylicDisplay) {
		this.flagIsCylicDisplay = isCylicDisplay;
	}
}
