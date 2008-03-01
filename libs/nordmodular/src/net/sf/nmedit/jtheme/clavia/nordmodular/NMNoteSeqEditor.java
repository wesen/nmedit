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
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JScrollBar;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.NoteSeqEditorUI;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTControlAdapter;
import net.sf.nmedit.jtheme.store2.BindParameter;

public class NMNoteSeqEditor extends JTControl
{
    
    private static final long serialVersionUID = -737910030163291008L;
    
    public static final String uiClassID = "NoteSeqEditorUI";
    private static final int STEPS = 16;
    
    private int zoom = 5, maxZoom = 5, minZoom = 1;
    private JTControlAdapter[] controlAdapters = new JTControlAdapter[STEPS];
    private JTControlAdapter[] controlExtensionAdapters = new JTControlAdapter[STEPS];
    
    private int maxTranslation = 10;
    private int translation = 0;
    private boolean modified = true;

    public NMNoteSeqEditor(JTContext context)
    {
        super(context);
        computeMaxTranslation();
        setTranslation(maxTranslation/2);
        clear();
    }
    
    protected void setModified(boolean modified)
    {
        this.modified = true;
    }
    
    protected void paintComponent(Graphics g)
    {
        if (modified)
        {
            modified = false;
            setDoubleBufferNeedsUpdate();
        }
        
        super.paintComponentWithDoubleBuffer(g);
    }
    
    private void computeMaxTranslation()
    {
        // original component
        // zoom | maxTranslation
        //  1   | 127
        //  2   | 57 
        //  3   | 37 
        //  4   | 27 
        //  5   | 10
        final int mint = 10;
        final int maxt = 127;        
        setMaxTranslation( mint+(((maxt-mint)*(zoom-1))/4) );
    }
    
    private void setMaxTranslation(int max)
    {
        if (max<=0) max = 1;
        if (this.maxTranslation != max)
        {
            // scale translation value
            translation = (max*translation)/maxTranslation;
            this.maxTranslation = max;
            setModified(true);
            fireStateChanged(); // invokes repaint()
        }
    }
    
    public int getMaxTranslation()
    {
        return maxTranslation;
    }
    
    public int getTranslation()
    {
        return translation;
    }
    
    public void setTranslation(int t)
    {
        t = Math.max(0, Math.min(t, maxTranslation));
        if (this.translation != t)
        {
            this.translation = t;
            setModified(true);
            fireStateChanged(); // invokes repaint()
        }
    }

//    protected class NoteListener implements ChangeListener{
//    	int note;
//    	boolean isPlus;
//    	public NoteListener(int note, boolean isPlus) {
//    		this.note = note;
//    		this.isPlus = isPlus;
//		}
//		public void stateChanged(ChangeEvent e) {
//			if (isPlus)
//				setNote(note, getNote(note)+1);
//			else 
//				setNote(note, getNote(note)-1);
//		}
//
//    }
    
    @BindParameter(name="step",count=16)
    public void setControlAdapter(int i, JTControlAdapter adapter)
    {
        JTControlAdapter oldAdapter = controlAdapters[i];
        if (oldAdapter != null)
        {
            uninstallAdapter(oldAdapter);
        }
        
        controlAdapters[i] = adapter;
        
        if (adapter != null)
            installAdapter(adapter);
    }
    
    public JTControlAdapter getControlAdapter(int i){
    	return controlAdapters[i];
    }
    
    @BindParameter(name="stepExtension",count=16)
    public void setControlExtensionAdapter(int i, JTControlAdapter adapter)
    {
    	JTControlAdapter oldAdapter = controlExtensionAdapters[i];
        if (oldAdapter != null)
        {
            uninstallAdapter(oldAdapter);
        }
        
        controlExtensionAdapters[i] = adapter;
        
        if (adapter != null)
            installAdapter(adapter);
    }
    
    
    private void uninstallAdapter(JTControlAdapter adapter)
    {
        adapter.setComponent(null);
        adapter.setChangeListener(null);
    }
    
    private void installAdapter(JTControlAdapter adapter)
    {
        adapter.setComponent(this);
        adapter.setChangeListener(this);
    }

    public String getUIClassID()
    {
        return uiClassID;
    }
    
    public int getMinZoom()
    {
        return minZoom;
    }
    
    public int getMaxZoom()
    {
        return maxZoom;
    }
    
    public int getZoom()
    {
        return zoom;
    }

    public void setZoom(int z)
    {
        if (minZoom<=z && z<= maxZoom && this.zoom != z)
        {
            this.zoom = z;
            computeMaxTranslation();
            setModified(true);
            repaint();
        }
    }
    
    public void zoomIn(){
    	setZoom(zoom + 1);
    }
    
    public void zoomOut(){
    	setZoom(zoom - 1);
    }
    
    public int getRowHeight()
    {
        return getZoom();
    }
    
    public void randomize()
    {
    	int amp = (maxZoom - getZoom()+1)*12;
        for (int i=0;i<getNoteCount();i++)
        	
            setNote(i, (int)(Math.random()*amp)+ 60 - amp/2);
    }
    
    public void clear()
    {
    	
    	for (int i=0;i<getNoteCount();i++)
            setNote(i, 64);
    }
    
    public int getNoteCount()
    {
        return controlAdapters.length;
    }

    public int getNote(int index)
    {
        JTControlAdapter ca = controlAdapters[index];
        
        return ca == null ? 0 : ca.getValue();
    }
    
    public void setNote(int index, int value)
    {
        JTControlAdapter ca = controlAdapters[index];
        if (ca != null)
        {
            if (ca.getValue() != value)
            {
                ca.setValue(value);
                setModified(true);
                repaint();
            }
        }
    }
    
    public int getExtension(int index)
    {
        JTControlAdapter ca = controlExtensionAdapters[index];
        
        return ca == null ? 0 : ca.getValue();
    }
    
    public void setExtension(int index, int value)
    {
        JTControlAdapter ca = controlExtensionAdapters[index];
        if (ca != null)
        {
            if (ca.getValue() != value)
            {
                ca.setValue(value);
                setModified(true);
                repaint();
            }
        }
    }
    
    public void showControlPopup(MouseEvent e, int parameterIndex)
    {
        JTContext c = getContext();
        if (c == null) return;
        ControlPopupHandler popup = (ControlPopupHandler) c.getPopupHandler(this);
        if (popup != null) popup.showPopup(e, this, parameterIndex);
    }
    
    public static void main(String[] args)
    {
        JFrame f = new JFrame(NMNoteSeqEditor.class.getName());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(0, 0, 400, 360);
        
        f.getContentPane().setLayout(null);
        
        final NMNoteSeqEditor ed = new NMNoteSeqEditor(null);
        ed.setUI(NoteSeqEditorUI.createUI(ed));
        
        f.getContentPane().add( ed );
        
        final JScrollBar sb = new JScrollBar(JScrollBar.VERTICAL, ed.getZoom(),1, ed.getMinZoom(), ed.getMaxZoom()+1);
        
        sb.addAdjustmentListener(new AdjustmentListener(){

            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                ed.setZoom(sb.getValue());
            }});
        
        ed.setLocation(0, 0);
        ed.setSize(ed.getPreferredSize());
        
        sb.setLocation(ed.getWidth()+2, 0);
        sb.setSize(13, ed.getHeight());
        
        f.getContentPane().addMouseListener(new MouseAdapter(){
            
            public void mouseClicked(MouseEvent e)
            {
              ed.randomize();  
            }
        });
        
        f.getContentPane().add(sb);
        
        f.setVisible(true);
    }


	public int getSteps() {
		return STEPS;
	}
    
}
