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
 * Created on Sep 10, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Format;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Header;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Signal;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.HeaderListener;

public class CableToggler extends JPanel implements HeaderListener, ActionListener
{
    
    private JCheckBox[] toggler = new JCheckBox[7];
    private Patch patch = null;

    public CableToggler()
    {
        setLayout(new GridLayout(1,0));
        for (int i=0;i<7;i++)
            add(toggler[i] = checkbox(Signal.bySignalID(i)));
    }
    
    private JCheckBox checkbox(Signal signal)
    {
        JCheckBox cb = new JCheckBox();
        cb.setText(null);
        cb.setToolTipText(signal.toString());
        cb.setBackground(signal.getDefaultColor());
        cb.setForeground(signal.getDefaultColor());
        cb.setEnabled(false);
        cb.addActionListener(this);
        return cb;
    }
    
    public Patch getPatch()
    {
        return patch;
    }

    public void setPatch(Patch patch)
    {
        if (this.patch!=patch)
        {
            if (patch!=null)
                patch.getHeader().removeListener(this);
            this.patch = patch;
            if (patch!=null)
                patch.getHeader().addHeaderListener(this);
            updateTogglers();
        }
    }

    public void headerValueChanged( Event e )
    {
        switch (e.getIndex())
        {
            case Format.HEADER_CABLE_VISIBILITY_BLUE:
            case Format.HEADER_CABLE_VISIBILITY_RED:
            case Format.HEADER_CABLE_VISIBILITY_YELLOW:
            case Format.HEADER_CABLE_VISIBILITY_GRAY:
            case Format.HEADER_CABLE_VISIBILITY_GREEN:
            case Format.HEADER_CABLE_VISIBILITY_PURPLE:
            case Format.HEADER_CABLE_VISIBILITY_WHITE:
                updateTogglers();
        }
    }

    private void updateTogglers()
    {
        if (patch==null)
        {
            for (int i=0;i<toggler.length;i++)
                toggler[i].setEnabled(false);
        }
        else
        {
            Header h = patch.getHeader();

            for (int i=0;i<toggler.length;i++)
            {
                toggler[i].setEnabled(true);
                toggler[i].setSelected(h.isCableVisible(Signal.bySignalID(i)));
            }
        }
    }

    public void actionPerformed( ActionEvent e )
    {
        if (patch==null)
            return;
        
        Object s = e.getSource();
        for (int i=toggler.length-1;i>=0;i--)
        {
            if (toggler[i]==s)
            {
                Header h = patch.getHeader();
                h.setCableVisible(Signal.bySignalID(i), toggler[i].isSelected());
                return;
            }
        }
    }
    
}
