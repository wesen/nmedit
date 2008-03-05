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
package net.sf.nmedit.jtheme.clavia.nordmodular.plaf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMNoteSeqEditor;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.plaf.JTBasicControlUI;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.swing.NMLazyActionMap;


public class NoteSeqEditorUI extends JTBasicControlUI
{
	protected static final String COL_LEFT = "col.right";
	protected static final String COL_RIGHT = "col.left";
	protected static final String INCREASE_EXT = "increase.ext";
	protected static final String DECREASE_EXT = "decrease.ext";
	protected static final String INCREASE_EXT_FAST = "increase.ext.fast";
	protected static final String DECREASE_EXT_FAST = "decrease.ext.fast";
	
	
	protected NMNoteSeqEditor control;
	
    public static final Color DEFAULT_BACKGROUND = new Color(0xDCDCDC);
    public static final Color DEFAULT_LINE_COLOR = new Color(0xC0C0C0);

    public static final String borderKey = "NoteSeqEditorUI.border";
    
    private Color noteColor = new Color(0x2e43ad);
    private Color lineColor = DEFAULT_LINE_COLOR;

    private int editedNote = -1; // index of the note curently edited with the keyboard
    
    private int columnWidth = 12;
    private int noteHeight = 4; 
    
    
	public NoteSeqEditorUI(NMNoteSeqEditor control) {
    	this.control = control;
    }
    
    public static ComponentUI createUI(JComponent c)
    {
    	
        return new NoteSeqEditorUI((NMNoteSeqEditor)c);
    }
    
    public void installUI(JComponent c)
    {
    	super.installUI(c);
    	
        JTContext context = ((JTComponent) c).getContext();
        Border border = context.getUIDefaults().getBorder(borderKey);
        
        c.setBorder(border);
        if (border != null)
            c.setOpaque(border.isBorderOpaque());
        else
            c.setOpaque(false);
        
        c.setBackground(DEFAULT_BACKGROUND);
                
    }
    
    public void uninstallUI(JComponent c)
    {
    	super.uninstallUI(c);
        // remove border
        c.setBorder(null);

    }
    
    protected NoteSeqEditorListener createNoteSeqEditorListener() {
//    	 return NoteSeqEditorListener.createListener(this);
    	
    	return new NoteSeqEditorListener(this);
		
	}
    
    protected NoteSeqEditorListener getNoteSeqEditorListener(JComponent c)
    {    	
    	return seqControlListenerInstance == null ? createNoteSeqEditorListener(): (NoteSeqEditorListener) seqControlListenerInstance;

    }

	public Dimension getPreferredSize(JComponent c)
    {
        return new Dimension(204,72);
    }
    
    private Insets cachedInsets = new Insets(0,0,0,0);
	
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        NMNoteSeqEditor ed = control;
        
        Insets insets = (cachedInsets=ed.getInsets(cachedInsets));
        
        int x = insets.left;
        int y = insets.top;
        int w = ed.getWidth()-(insets.left+insets.right);
        int h = ed.getHeight()-(insets.top+insets.bottom);
        
        noteHeight = ed.getZoom();//h/(12*(ed.getMaxZoom()+1-ed.getZoom()));
                
        paintBackground(g, ed, x, y, w, h);
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        NMNoteSeqEditor ed = control;
        
        Insets insets = (cachedInsets=ed.getInsets(cachedInsets));
        int x = insets.left;
        int y = insets.top;
        int w = ed.getWidth()-(insets.left+insets.right);
        int h = ed.getHeight()-(insets.top+insets.bottom);

        // at maximum zoom level we display one octave
        noteHeight = ed.getZoom() ;//h/(12*(ed.getMaxZoom()+1-ed.getZoom()));
        
        paintForeground(g, ed, x, y, w, h);
    }

    private static final boolean BLACK_KEY_12[] = 
        {
           false, // C
           true,  // C#
           false, // D
           true,  // D#
           false, // E
           false, // F
           true,  // F#
           false, // G
           true,  // G#
           false, // A
           true,  // B
           false, // H
        };
    
    private boolean isBlackKey(int midiNote)
    {
        // midiNote == 0 <=> midiNote == C

        midiNote = midiNote%12;
        if (midiNote<0) // this should never be true 
            midiNote = 12-midiNote;

        return BLACK_KEY_12[midiNote];
    }
    
    private void paintBackground(Graphics2D g, NMNoteSeqEditor ed, int x, int y, int w, int h)
    {
        
        {
            Insets is = ed.getInsets();
            g.setColor(ed.getBackground());
            g.fillRect(is.left, is.top,
                    ed.getWidth()-is.left-is.right,
                    ed.getHeight()-is.top-is.bottom);
        }
        
        int mid = h/2;
        
        // 11 octave lines
        
        int nr = x+ed.getNoteCount()*columnWidth;
        
        for (int octave = -4; octave<=6; octave++)
        {
        	int oy = mid - octave * noteHeight*12 + (ed.getTranslation()- ed.getMaxTranslation()/2)*noteHeight;
        	
        	// draw keyboard
        	if (noteHeight>=3)
            {
                g.setColor(new Color(0x9a9a9a));
	        	for (int i = 0; i < 12 ; i ++){
	        		if (isBlackKey(i))
	        		{   
                        g.fillRect(x, oy-i*noteHeight, nr, noteHeight);     			
	        		} 
	        	}        	
            }
            g.setColor(lineColor);
        	// draw line separating octaves            
            //g.drawLine(x, oy, nr, oy);   
        }
    
        for (int i=1;i<=ed.getNoteCount()-1;i++)
        {
            int nx = (i*(w+1))/16;
            g.drawLine(nx+x, y, nx+x, y+h);
        }
        
        // highlight edited note
        if (editedNote > -1 && editedNote < 16){
            Composite oldComposite = g.getComposite();
        	g.setComposite(NOTE_HIGHLIGHT_COMPOSITE);
	        g.setColor(noteColor);
	        g.fillRect(x+ (editedNote * (w+1)/16)+(editedNote>0?1:0), y, (w+1)/16, h-y);
            g.setComposite(oldComposite); // restore previous composite
        }
    }
    
    private static final AlphaComposite NOTE_HIGHLIGHT_COMPOSITE 
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
	

    private void paintForeground(Graphics2D g, NMNoteSeqEditor ed, int x, int y, int w, int h)
    {
        g.setColor(noteColor);
        
        //int row_height = ed.getRowHeight();

        int mid = h/2;
        
        int cw = (w+1)/16;

        for (int i=0;i<=ed.getNoteCount()-1;i++)
        {
            int nx = ((i*(w+1))/16) + (i>0?1:0);
            int note = ed.getNote(i);
            
            int ny = mid-(note-60- ed.getTranslation()+ ed.getMaxTranslation()/2)*noteHeight ;
            
            if (0<=ny && ny<y+h)
            {
            	g.setColor(noteColor);
                g.fillRect(nx+x, ny, cw, noteHeight);
            }
            else 
            {
                g.setColor(lineColor);
                paintArrow(g, ny<0, nx+x, cw, h);
                
            }
            
            Composite oldComposite = g.getComposite();
        	g.setComposite(NOTE_HIGHLIGHT_COMPOSITE);
        	
        	
	        g.setColor(getExtensionColor(ed.getControlAdapter(i).getParameter()));
	        
	        int extension = ed.getExtension(i);	 
	        if (extension >=0)
	        	g.fillRect(x+ (i * (w+1)/16)+(i>0?1:0), ny-extension*noteHeight, (w+1)/16, extension*noteHeight);
	        else
	        	g.fillRect(x+ (i * (w+1)/16)+(i>0?1:0), ny+noteHeight, (w+1)/16, -extension*noteHeight);
            g.setComposite(oldComposite); // restore previous composite            
            
        }
    }

    private void paintArrow(Graphics g, boolean uparrow, int nx, int cw, int h)
    {
        int arrowHeight = 5;

        int dy1 = 0;
        int dy2 = 0;
        
        if (!uparrow)
        {
            dy1 = h-arrowHeight-2-arrowHeight;
            dy2 = h-2;
        }
        
        int x[]= {nx,nx+cw/2,nx+cw,nx};
        int y[] = {arrowHeight+1+dy1,1+dy2,arrowHeight+1+dy1,arrowHeight+1+dy1};
        
        g.setColor(noteColor);
        g.fillPolygon(x,y,x.length);
        
    }
    
    private transient BasicControlListener seqControlListenerInstance;
    
    protected BasicControlListener createControlListener(JTControl control) 
    {
        if (seqControlListenerInstance == null)
            seqControlListenerInstance = new NoteSeqEditorListener(this);
       
        return seqControlListenerInstance;
    }
    
    public static class NoteSeqEditorListener extends BasicControlListener
    {

    	NoteSeqEditorUI controlUI;
    	public NoteSeqEditorListener(NoteSeqEditorUI controlUI)
	    {
	    	this.controlUI = controlUI;
	    }
    	
        public static void loadActionMap(NMLazyActionMap map) 
        {  
        	map.put(new NoteSeqActions(DEFAULTVALUE)); 
            map.put(new NoteSeqActions(INCREASE));
            map.put(new NoteSeqActions(DECREASE));
            map.put(new NoteSeqActions(INCREASE_FAST));
            map.put(new NoteSeqActions(DECREASE_FAST));
        }

        public void installKeyboardActions( JTControl control )
        {
        	ActionMap map = new ActionMap();
        	map.put(DEFAULTVALUE,new NoteSeqActions(DEFAULTVALUE)); 
            map.put(INCREASE,new NoteSeqActions(INCREASE));
            map.put(DECREASE,new NoteSeqActions(DECREASE));
            map.put(INCREASE_FAST,new NoteSeqActions(INCREASE_FAST));
            map.put(DECREASE_FAST,new NoteSeqActions(DECREASE_FAST));
            map.put(COL_LEFT,new NoteSeqActions(COL_LEFT));
            map.put(COL_RIGHT,new NoteSeqActions(COL_RIGHT));
            map.put(INCREASE_EXT,new NoteSeqActions(INCREASE_EXT));
            map.put(DECREASE_EXT,new NoteSeqActions(DECREASE_EXT));
            map.put(INCREASE_EXT_FAST,new NoteSeqActions(INCREASE_EXT_FAST));
            map.put(DECREASE_EXT_FAST,new NoteSeqActions(DECREASE_EXT_FAST));

            SwingUtilities.replaceUIActionMap(control, map);
            
            InputMap im = createInputMapWhenFocused();
            addSeqEditorKS(im);
            SwingUtilities.replaceUIInputMap(control, JComponent.WHEN_FOCUSED, im);
        }
        
        protected void addSeqEditorKS(InputMap map)
        {
        	 KeyStroke left = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
             map.put(left, COL_LEFT);
             
             KeyStroke right= KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
             map.put(right, COL_RIGHT);
             
             KeyStroke upExt = KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK);
             map.put(upExt, INCREASE_EXT);
             
             KeyStroke downExt = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK);
             map.put(downExt, DECREASE_EXT);
             
             KeyStroke upExtFast = KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK);
             map.put(upExtFast, INCREASE_EXT_FAST);
             
             KeyStroke downExtFast = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK);
             map.put(downExtFast, DECREASE_EXT_FAST);
             
             
        }
        
        protected NMNoteSeqEditor getControl(ComponentEvent e)
	    {
	        Component c = e.getComponent();
	        if (c!= null && c instanceof NMNoteSeqEditor)
	            return (NMNoteSeqEditor) c;
	        return null;
	    }
        
        
        private int oldNote = 0, oldExtension = 0;
        private int y;
        
        private void popupTriggered(MouseEvent e)
        {
            NMNoteSeqEditor ed = getControl(e);
            
            controlUI.editedNote = e.getX() / controlUI.columnWidth;
            
             if (!e.getComponent().hasFocus())
                 e.getComponent().requestFocus();
             e.getComponent().repaint();
             
             ed.showControlPopup(e, controlUI.editedNote);
        }
        
        public void mousePressed(MouseEvent e)
	    {
        	if (Platform.isPopupTrigger(e))
        	{
        	    popupTriggered(e);
        		
        	}
        	else if (Platform.isLeftMouseButtonOnly(e))
        	{
	    		controlUI.editedNote = e.getX() / controlUI.columnWidth;
	    		y = e.getY();
	    		oldNote = getControl(e).getNote(controlUI.editedNote);
	    		oldExtension = getControl(e).getExtension(controlUI.editedNote);
	    		
	    		 if (!e.getComponent().hasFocus())
	                 e.getComponent().requestFocus();
	    		 e.getComponent().repaint();
	    	}
	    }
        
        public void mouseReleased(MouseEvent e)
	    {
            if (Platform.isPopupTrigger(e))
        	{
                popupTriggered(e);
        	}
	    }
        
        public void mouseDragged(MouseEvent e)
	    {
	    	//System.out.print("dragged");
	    	NMNoteSeqEditor ed = getControl(e);

	    	if (isExtensionSelected(e)) {
	    		ed.setExtension(controlUI.editedNote, oldExtension - (e.getY()-y)/controlUI.getNoteHeight());
	    	} else {
	    		ed.setNote(controlUI.editedNote, oldNote - (e.getY()-y)/controlUI.getNoteHeight());
	    	}
	    }
        
        
        
        @Override
		public void focusLost(FocusEvent e) {
			controlUI.editedNote = -1;
			super.focusLost(e);
			
		}



		public static class NoteSeqActions extends AbstractAction 
        {
            
            // private String action;

            /**
             * 
             */
            private static final long serialVersionUID = -2104045982638755995L;

            public NoteSeqActions(String name)
            {
                super(name);
            }

            public String getName()
            {
                return (String) super.getValue(NAME);
            }
            
            public void actionPerformed(ActionEvent e)
            {
                NMNoteSeqEditor ed = (NMNoteSeqEditor) e.getSource();
                NoteSeqEditorUI ui = ((NoteSeqEditorUI)ed.getUI());
                int editedNote = ui.getEditedNote();
                
                String key = getName();
                                
                if (key == INCREASE){
                	ed.setNote(editedNote, ed.getNote(editedNote) + 1 );
                }
                else if (key == DECREASE) {
                	ed.setNote(editedNote, ed.getNote(editedNote) - 1 );
                
                }
                else if (key == INCREASE_FAST) {
                	ed.setNote(editedNote, ed.getNote(editedNote) + 12 );                	
                }
                else if (key == DECREASE_FAST)
                {
                	ed.setNote(editedNote, ed.getNote(editedNote) - 12 );
                }
                else if (key == INCREASE_EXT)
                {
                	ed.setExtension(editedNote, ed.getExtension(editedNote)+1);
                }
                else if (key == DECREASE_EXT)
                {
                	ed.setExtension(editedNote, ed.getExtension(editedNote)-1);
                }
                else if (key == INCREASE_EXT_FAST)
                {
                	ed.setExtension(editedNote, ed.getExtension(editedNote)+12);
                }
                else if (key == DECREASE_EXT_FAST)
                {
                	ed.setExtension(editedNote, ed.getExtension(editedNote)-12);
                }
                else if (key == COL_LEFT){
                	ui.editedNote--;
                	if (ui.editedNote < 0 ) ui.editedNote = 0;
                	ed.repaint();
                }
                else if (key == COL_RIGHT){
                	ui.editedNote++;
                	if (ui.editedNote >15 ) ui.editedNote = 15;
                	ed.repaint();
                }
                else if (key == DEFAULTVALUE)
                {
                	ed.setNote(editedNote, 64 );
                }
            }
        }
        
    }

//	    public void stateChanged(ChangeEvent e)
//	    {
//	        Object o = e.getSource();
//	        if (o instanceof Component)
//	            ((Component)o).repaint();
//	    }
//	

    
	public int getNoteHeight() {
		return this.noteHeight;
	}
	
	public void setNoteHeight(int noteHeight) {
		this.noteHeight = noteHeight;

	}

	public int getEditedNote() {
		return editedNote;
	}

	public void setEditedNote(int editedNote) {
		this.editedNote = editedNote;
	}
}
