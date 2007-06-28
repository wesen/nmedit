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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMNoteSeqEditor;
import net.sf.nmedit.jtheme.component.JTButtonControl;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.plaf.JTBasicButtonControlUI;
import net.sf.nmedit.jtheme.component.plaf.JTComponentUI;


public class NoteSeqEditorUI extends JTComponentUI
{
    
	protected NMNoteSeqEditor control;
	
    public static final Color DEFAULT_BACKGROUND = new Color(0xDCDCDC);
    public static final Color DEFAULT_LINE_COLOR = new Color(0xC0C0C0);
    
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
        c.setBackground(DEFAULT_BACKGROUND);
        
        NoteSeqEditorListener noteSeqListenner= createNoteSeqEditorListener(control);
        if (noteSeqListenner != null)
        	noteSeqListenner.install(control);
    }
    
    protected NoteSeqEditorListener createNoteSeqEditorListener(NMNoteSeqEditor ed) {
//    	 return NoteSeqEditorListener.createListener(this);
    	
    	return new NoteSeqEditorListener(this);
		
	}

	public Dimension getPreferredSize(JComponent c)
    {
        return new Dimension(204,72);
    }
    
	
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        NMNoteSeqEditor ed = (NMNoteSeqEditor) c;
        
        Insets insets = ed.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = ed.getWidth()-(insets.left+insets.right);
        int h = ed.getHeight()-(insets.top+insets.bottom);
        
        noteHeight = ed.getZoom();//h/(12*(ed.getMaxZoom()+1-ed.getZoom()));
                
        paintBackground(g, ed, x, y, w, h);
    }

    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        NMNoteSeqEditor ed = (NMNoteSeqEditor) c;
        
        Insets insets = ed.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = ed.getWidth()-(insets.left+insets.right);
        int h = ed.getHeight()-(insets.top+insets.bottom);

        // at maximum zoom level we display one octave
        noteHeight = ed.getZoom() ;//h/(12*(ed.getMaxZoom()+1-ed.getZoom()));
        
        
        paintForeground(g, ed, x, y, w, h);
    }

    private void paintBackground(Graphics2D g, NMNoteSeqEditor ed, int x, int y, int w, int h)
    {
        g.setColor(ed.getBackground());
        g.fillRect(0, 0, ed.getWidth(), ed.getHeight());

        int mid = h/2;
        
        // 11 octave lines
        
        int nr = x+ed.getNoteCount()*columnWidth;
        
        for (int octave = -4; octave<=6; octave++)
        {
        	int oy = mid - octave * noteHeight*12;
        	
        	// draw keyboard
        	if (noteHeight>=3)
	        	for (int i = 0; i < 12 ; i ++){
	        		if (i == 1 ||i == 3 ||i == 6 ||i == 8 ||i == 10)
	        		{
	        			g.setColor(new Color(0x9a9a9a));        			
	        		} else 
	        			g.setColor(Color.WHITE);
	        		
	        		g.fillRect(x, oy-i*noteHeight, nr, noteHeight);
	        	}        	
        
            g.setColor(lineColor);
        	// draw line separating octaves            
            //g.drawLine(x, oy, nr, oy);   
        }
    
        for (int i=0;i<=ed.getNoteCount()-1;i++)
        {        	
            int nx = i*w/16;
            g.drawLine(nx+x, y, nx+x, y+h);
        }
        
        // highlight edited note
        if (editedNote > -1 && editedNote < 16){
        	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.25f));
	        g.setColor(noteColor);
	        g.fillRect(editedNote * w/16+1, y, w/16-1, h);
	    
        }
    }
    

    private void paintForeground(Graphics2D g, NMNoteSeqEditor ed, int x, int y, int w, int h)
    {
        g.setColor(noteColor);
        
        //int row_height = ed.getRowHeight();

        int mid = h/2;
        
        int cw = w/16-2;
                        
        
        for (int i=0;i<=ed.getNoteCount()-1;i++)
        {
            int nx = i*w/16+1;
            int note = ed.getNote(i);
            
            
            int ny = mid-(note-60)*noteHeight;
            
            if (0<=ny && ny<y+h)
            {
            	g.setColor(noteColor);
                g.fillRect(nx+x, ny, cw+1, noteHeight);
            }
            else 
            {
                g.setColor(lineColor);
                paintArrow(g, ny<0, nx+x, cw, h);
                
            }
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
    
    protected class NoteSeqEditorListener implements MouseListener, 
    MouseMotionListener, FocusListener, ChangeListener, KeyListener
    {
//
//	    public  NoteSeqEditorListener createListener(NoteSeqEditorUI controlUI2)
//	    {
//	        return new NoteSeqEditorListener();
//	    }
    	private NoteSeqEditorUI controlUI;
    	
	    public NoteSeqEditorListener(NoteSeqEditorUI controlUI)
	    {
	    	this.controlUI = controlUI;
	    }
    	
    	
	    protected NMNoteSeqEditor getControl(ComponentEvent e)
	    {
	        Component c = e.getComponent();
	        if (c!= null && c instanceof NMNoteSeqEditor)
	            return (NMNoteSeqEditor) c;
	        return null;
	    }
	    
	    protected NoteSeqEditorUI getUI(NMNoteSeqEditor control)
	    {
	        Object ui = control.getUI();
	        if (ui != null && ui instanceof NoteSeqEditorUI)
	            return (NoteSeqEditorUI) ui;
	        else
	            return null;
	    }
	    
	    public void install(NMNoteSeqEditor seq)
	    {
	        seq.addMouseListener(this);
	        seq.addMouseMotionListener(this);
	        seq.addFocusListener(this);
	        seq.addChangeListener(this);
	        seq.addKeyListener(this);
	    }
	    
	    public void uninstall(NMNoteSeqEditor  seq)
	    {
	        seq.removeMouseListener(this);
	        seq.removeMouseMotionListener(this);
	        seq.removeFocusListener(this);
	        seq.removeChangeListener(this);
	        seq.removeKeyListener(this);
	    }
	
	    transient JTButtonControl selectedControl;
	    transient JTBasicButtonControlUI selectedUI;
	    transient int internalSelectedButtonIndex;
	    
	    
	    
	    
	    public void mouseClicked(MouseEvent e)
	    {	// no op    	
	    }
	
	    public void mouseEntered(MouseEvent e)
	    {  // no op 
	    }
	
	    public void mouseExited(MouseEvent e)
	    {   // no op
	    }
	
	    private int y;
	    private int oldNote;
	
	    public void mousePressed(MouseEvent e)
	    {
	    	
	    	if (e.getButton() == e.BUTTON1){
	    		editedNote = e.getX() / columnWidth;
	    		y = e.getY();
	    		oldNote = getControl(e).getNote(editedNote);
	    		
	    		 if (!e.getComponent().hasFocus())
	                 e.getComponent().requestFocus();
	    		 e.getComponent().repaint();
	    	}
	    }
	
	    public void mouseReleased(MouseEvent e)
	    {
	    }
	
	    public void mouseDragged(MouseEvent e)
	    {
	    	//System.out.print("dragged");
	    	NMNoteSeqEditor ed = getControl(e);

	    	ed.setNote(editedNote, oldNote - (e.getY()-y)/controlUI.getNoteHeight());

	    }
	
	    public void mouseMoved(MouseEvent e)
	    {  }
	
	    public void focusGained(FocusEvent e)
	    {
	        e.getComponent().repaint();
	    }
	
	    public void focusLost(FocusEvent e)
	    {
	    	editedNote = -1;
	    	
	        e.getComponent().repaint();
	    }
	
	    public void stateChanged(ChangeEvent e)
	    {
	        Object o = e.getSource();
	        if (o instanceof Component)
	            ((Component)o).repaint();
	    }
	
	    public void keyPressed(KeyEvent e)
	    {
	        // if (e.getModifiers() == 0)
	        {
	            NMNoteSeqEditor control = getControl(e);
	            if (control != null)
	            {
	            	NMNoteSeqEditor ed = control;
	                switch (e.getKeyCode())
	                {
	                    case KeyEvent.VK_UP:	                    	
	                    	ed.setNote(editedNote, ed.getNote(editedNote) + 1 );	                    	
	                        break;
	                    case KeyEvent.VK_PAGE_UP:
	                    	ed.setNote(editedNote, ed.getNote(editedNote) + 12 );
	                    	break;
	                    case KeyEvent.VK_DOWN:
	                    	ed.setNote(editedNote, ed.getNote(editedNote) - 1 );
	                        break;
	                    case KeyEvent.VK_PAGE_DOWN:
	                    	ed.setNote(editedNote, ed.getNote(editedNote) - 12 );
	                    	break;
	                    case KeyEvent.VK_LEFT:	                    	
	                    	editedNote--;
	                    	if (editedNote < 0 ) editedNote = 15;
	                    	e.getComponent().repaint();
	                    	break;
	                    case KeyEvent.VK_RIGHT:
	                    	editedNote++;
	                    	if (editedNote > 15 ) editedNote = 0;
	                    	e.getComponent().repaint();
	                    	break;
	                    case KeyEvent.VK_SPACE:
	                    	ed.setNote(editedNote, 60);
	                        break;
	                }
	            }
	        }
	    }
	
	    public void keyReleased(KeyEvent e)
	    {
//	    	 no op
	    }
	
	    public void keyTyped(KeyEvent e)
	    {
	        // no op
	    }
	
	}

	public int getNoteHeight() {
		return this.noteHeight;
	}
	
	public void setNoteHeight(int noteHeight) {
		//System.out.println("set height" + noteHigh);
		this.noteHeight = noteHeight;
		//System.out.println("after set height" + this.noteHigh);
	}
}
