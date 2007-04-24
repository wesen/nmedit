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

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.NoteSeqEditorUI;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControlAdapter;

public class NMNoteSeqEditor extends JTComponent implements ChangeListener
{
    
    public static final String uiClassID = "NoteSeqEditorUI";
    private static final int STEPS = 16;
    
    private int zoom = 3;
    private JTControlAdapter[] controlAdapters = new JTControlAdapter[STEPS];
    
    public NMNoteSeqEditor(JTContext context)
    {
        super(context);
        setUI(NoteSeqEditorUI.createUI(this));
        randomize();
    }


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


    private void uninstallAdapter(JTControlAdapter adapter)
    {
        adapter.setChangeListener(null);
    }
    
    private void installAdapter(JTControlAdapter adapter)
    {
        adapter.setChangeListener(this);
    }

    public void stateChanged(ChangeEvent e)
    {
        repaint();
    }

    public String getUIClassID()
    {
        return uiClassID;
    }
    
    public int getMinZoom()
    {
        return 1;
    }
    
    public int getMaxZoom()
    {
        return 5;
    }
    
    public int getZoom()
    {
        return zoom;
    }

    public void setZoom(int z)
    {
        if (1<=z && z<=5 && this.zoom != z)
        {
            this.zoom = z;
            repaint();
        }
    }
    
    public int getRowHeight()
    {
        return getZoom();
    }
    
    public void randomize()
    {
        for (int i=0;i<getNoteCount();i++)
            setNote(i, (int)(Math.random()*127));
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
            ca.setValue(value);
            //repaint();
        }
    }
    
    public static void main(String[] args)
    {
        JFrame f = new JFrame(NMNoteSeqEditor.class.getName());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(0, 0, 400, 360);
        
        f.getContentPane().setLayout(null);
        
        final NMNoteSeqEditor ed = new NMNoteSeqEditor(null);
        
        f.getContentPane().add( ed );
        
        final JScrollBar sb = new JScrollBar(JScrollBar.VERTICAL, ed.getZoom(),1, ed.getMinZoom(), ed.getMaxZoom());
        
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
    
}
