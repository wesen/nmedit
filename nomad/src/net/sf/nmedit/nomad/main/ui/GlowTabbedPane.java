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
 * Created on Apr 30, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GlowTabbedPane extends JComponent implements ActionListener 
{

    private ButtonGroup buttonGroup;
    private Object selection = null;
    private LinkedList<ChangeListener> changeListenerList
        = new LinkedList<ChangeListener>();
    
    public GlowTabbedPane()
    {
        buttonGroup = new ButtonGroup();
        setFont(new Font("SansSerif",Font.PLAIN,12));
        setOpaque(false);
        setBackground(HeaderSection.gradT2);
        setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
    }

    public void addTab( String label, Object data )
    {
        DataButton btn = createButton(data);
        btn.setText(label);
        buttonGroup.add(btn);
        add(btn);
        btn.addActionListener(this);
        
        if (buttonGroup.getButtonCount()==1)
            setSelection(data);
    }

    public Object getSelection()
    {
        return selection;
    }
    
    private DataButton createButton(Object data)
    {
        DataButton btn = new DataButton(data);
        GlowButtonUI ui = (GlowButtonUI) GlowButtonUI.createUI(btn);
        btn.setUI(ui);
        btn.setBackground(HeaderSection.gradT2);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        btn.setBorderPainted(false);
        btn.setMargin(new Insets(4,4,4,4));
        return btn;
    }

    public void actionPerformed( ActionEvent e )
    {
        if (e.getSource() instanceof DataButton)
        {
            DataButton btn = (DataButton) e.getSource();
            setSelectionInternal(btn.data);
        }        
    }
    
    public void setSelection( Object data )
    {
        Enumeration en = buttonGroup.getElements();
        while (en.hasMoreElements())
        {
            DataButton btn = (DataButton)en.nextElement();
            if (btn.data.equals(data))
            {
                setSelectionInternal(data);
                btn.setSelected(true);
                return ;
            }
        }
    }
    
    private void setSelectionInternal( Object data )
    {
        this.selection = data;
        fireChangeEvent();
    }

    
    private void fireChangeEvent()
    {
        ChangeEvent event = new ChangeEvent(this);
        for (int i=changeListenerList.size()-1;i>=0;i--)
            changeListenerList.get(i).stateChanged(event);
    }

    public void addChangeListener(ChangeListener l)
    {
        if (!changeListenerList.contains(l))
            changeListenerList.add(l);
    }
    
    public void removeChangeListener(ChangeListener l)
    {
        changeListenerList.remove(l);
    }
    
    private static class DataButton extends JToggleButton
    {

        public Object data;

        public DataButton( Object data )
        {
            this.data =data;
        }
        
    }
    
}
