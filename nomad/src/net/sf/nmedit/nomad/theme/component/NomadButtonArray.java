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
package net.sf.nmedit.nomad.theme.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.border.Border;

import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.theme.NomadClassicColors;
import net.sf.nmedit.nomad.theme.component.model.NomadButtonArrayBehaviour;
import net.sf.nmedit.nomad.theme.component.model.NomadButtonArrayModel;
import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.util.graphics.ImageTracker;

public class NomadButtonArray extends NomadControl implements NomadButtonArrayModel {

	public final static boolean DEFAULT_CYCLIC = true;
	public final static boolean DEFAULT_LANDSCAPE = false;
	private NomadButtonArrayBehaviour behaviour = new NomadButtonArrayBehaviour(this);
	private boolean flagIsCylicDisplay = DEFAULT_CYCLIC;
	private boolean flagLandscape = DEFAULT_LANDSCAPE;
	//private ImageString[] labelList = new ImageString[6];
	
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
		setFocusable(true);

		setSelectionBorder(DEFAULT_SEL_Border);
		setDefaultBorder(DEFAULT_Border);
		addMouseListener(buttonArrayMouseListener);
		//autoResize(false);
		setSize(prefSize);
        setMinValue(0);
        setMaxValue(3);
        setValue(0);
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
		Image img = images.get(index);
        String s = buttons.get(index);
		if (img!=null)
			return new Dimension(img.getWidth(null),img.getHeight(null));
		else if (s!=null)
        {
			return NomadLabel.getStringBounds(this, s).getSize();
        }
		else
			return new Dimension(0,0);
	}
	
	private Dimension getMaxLabelSize() {
		Dimension d = new Dimension(6,6);
        for (int index : buttons.keySet()) {
			Dimension c = getLabelSize(index);
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

	public void setButton(int index, String label) {
		if (0<=index) 
        {
            if (label==null)
            {
                buttons.remove(index);
                images.remove(index);
            }
            else
            {
                buttons.put(index, label);
                String key = NomadUtilities.extractKeyFromImageString(label);
                ImageTracker itracker = NomadEnvironment.sharedInstance().getImageTracker();
                if (key!=null&&itracker!=null)
                    images.put(index,itracker.getImage(key));
                else
                    images.remove(index);
			}
			repaint();
		}
	}

    private Map<Integer,String> buttons = new HashMap<Integer,String>();
    private Map<Integer,Image> images = new HashMap<Integer,Image>();
    
    public String getButtonConfiguration()
    {
        StringBuffer conf = new StringBuffer();
        boolean first = true;
        for (Integer index : buttons.keySet())
        {
            String s = buttons.get(index);
            if (s!=null)
            {
                if (first) first = false;
                else conf.append(";");
                conf.append(index);
                conf.append("=");
                for (int i=0;i<s.length();i++)
                {
                    char c = s.charAt(i);
                    conf.append(c);
                    if (c==';')
                        conf.append(";");
                }
            }
        }
        if (!first) // at least one key,value pair
            conf.append(";");
        return conf.toString();
    }
    
    public void setButtonConfiguration(String s)
    {
        buttons.clear();
        images.clear();
        
        int index = 0;
        StringBuffer value = new StringBuffer();
        int pos = 0;
        ImageTracker itracker = NomadEnvironment.sharedInstance().getImageTracker();
        
        final int ST_NEW = 0;
        final int ST_INDEX = 1;
        final int ST_VALUE = 2;
        final int ST_END = 3; 
        
        int state = ST_NEW;
        
        while (pos<s.length())
        {
            char c = s.charAt(pos);
            switch (state)
            {
                case ST_END:
                    if (c==';') // two times ';' (==';;') means one is escaped
                    {
                        state = ST_VALUE;
                        value.append(c);
                        break;
                    }
                    else
                    {
                        String txt = value.toString();
                        value.setLength(0);
                        Integer i = new Integer(index);
                        
                        buttons.put(i, txt);

                        if (itracker!=null)
                        {
                            String key = NomadUtilities.extractKeyFromImageString(txt);
                            if (key!=null)
                                images.put(i,itracker.getImage(key));
                        }
                    }
                    // fall into st_new
                case ST_NEW:
                    index = 0;
                    state = ST_INDEX;
                    // fall into ST_INDEX => no break
                case ST_INDEX:
                    if (Character.isDigit(c))
                        index = (index*10) + (c-'0');
                    else if (c=='=')
                    {
                        state = ST_VALUE;
                    }
                    else
                        throw new RuntimeException("expected ([0-9]+|=) @"+pos);
                    break;
                case ST_VALUE:
                    if (c==';')
                        state=ST_END;
                    else
                        value.append(c);
                    break;
            }
            pos++;
        }
        
        if (value.length()>0)
        {
            // complete last
            String txt = value.toString();
            Integer i = new Integer(index);
            
            buttons.put(i, txt);

            if (itracker!=null)
            {
                String key = NomadUtilities.extractKeyFromImageString(txt);
                if (key!=null)
                    images.put(i,itracker.getImage(key));
            }
        }
        
    }
    
	public boolean getLandscape() {
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
    
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
		behaviour.calculateMetrics();
        
		g2.setFont(getFont());
		Border b = defBorder;

		if (flagIsCylicDisplay) {
			int i = getValue();
			if (getValue()!=0)
				b=selBorder;
			
			String text = "";
			Image img = null;
			
			if (0<=i  && buttons.get(i)!=null) {
				img = images.get(i);
				text= buttons.get(i);
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
				
				if (0<=i&& buttons.get(i)!=null) {
					img = images.get(i);
					text= buttons.get(i);
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
    
/*
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
    */
    
	public boolean getCyclic() {
		return flagIsCylicDisplay;
	}

	public void setCyclic(boolean isCylicDisplay) {
		this.flagIsCylicDisplay = isCylicDisplay;
	}

    public boolean isLandscape()
    {
        return getLandscape();
    }
}
