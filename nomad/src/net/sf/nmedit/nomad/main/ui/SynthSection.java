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
 * Created on Apr 29, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.NordModular;
import net.sf.nmedit.jsynth.event.SynthStateChangeEvent;
import net.sf.nmedit.jsynth.event.SynthStateListener;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.resources.AppIcons;

public class SynthSection extends HeaderSection implements SynthStateListener
{

    private NordModular device;
    private JLabel LED;
    private ImageIcon LED_ON;
    private ImageIcon LED_OFF;

    public SynthSection( Nomad nomad, NordModular device, String title )
    {
        super( title );
        this.device = device;
        device.addSynthStateListener(this);

        LED_ON = AppIcons.getImageIcon("light-on");
        LED_OFF = AppIcons.getImageIcon("light-off");
        
        JComponent pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(LED = new JLabel());

        updateLED();
    }
    
    private void updateLED()
    {
        LED.setIcon(device.isConnected() ? LED_ON : LED_OFF);
        LED.repaint();
    }

    public void synthStateChanged( SynthStateChangeEvent e )
    {
        updateLED();
    }

}
