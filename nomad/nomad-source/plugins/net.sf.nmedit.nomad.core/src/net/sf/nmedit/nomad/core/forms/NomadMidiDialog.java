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
package net.sf.nmedit.nomad.core.forms;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.sf.nmedit.nomad.core.misc.NMUtilities;

public class NomadMidiDialog extends NomadJDialog
{

    /**
     * 
     */
    private static final long serialVersionUID = 4444127141483231080L;
    public final static int CANCEL_OPTION = 0;
    public final static int APPROVE_OPTION = 1;
    
    private NomadMidiDialogFrmHandler form;
    private int result = CANCEL_OPTION;

    public NomadMidiDialog()
    {
        super();
        initDialog();
    }

    public NomadMidiDialog(Frame owner)
    {
        super(owner);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner)
    {
        super(owner);
        initDialog();
    }

    public NomadMidiDialog(Frame owner, boolean modal)
    {
        super(owner, modal);
        initDialog();
    }

    public NomadMidiDialog(Frame owner, String title)
    {
        super(owner, title);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, boolean modal)
    {
        super(owner, modal);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, String title)
    {
        super(owner, title);
        initDialog();
    }

    public NomadMidiDialog(Frame owner, String title, boolean modal)
    {
        super(owner, title, modal);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, String title, boolean modal)
    {
        super(owner, title, modal);
        initDialog();
    }


    public NomadMidiDialog(Frame owner, String title, boolean modal,
            GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
        initDialog();
    }

    public NomadMidiDialog(Dialog owner, String title, boolean modal,
            GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
        initDialog();
    }
    public NomadMidiDialogFrmHandler getForm()
    {
        return form;
    }

    private void initDialog()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        form = new NomadMidiDialogFrmHandler();
        form.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JPanel panOptions = new JPanel();
        panOptions.setLayout(new BoxLayout(panOptions, BoxLayout.X_AXIS));
        panOptions.add(Box.createGlue());
        JButton btnCancel = new JButton(new Option(CANCEL_OPTION, "Cancel"));
        Option okOption = new Option(APPROVE_OPTION, "Ok");
        JButton btnOk = new JButton(okOption);
        

        btnCancel.setDefaultCapable(true);
        btnOk.setDefaultCapable(false);
        getRootPane().setDefaultButton(btnCancel);
        
        panOptions.add(btnOk);
        panOptions.add(Box.createHorizontalStrut(4));
        panOptions.add(btnCancel);
        panOptions.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        
        (new OptionToggler(okOption)).update();
        
        getContentPane().setLayout(new BorderLayout());
        createTitlePane("MIDI", loadIcon("midi-ill.png"));
        getContentPane().add(form, BorderLayout.CENTER);
        
        getContentPane().add(panOptions, BorderLayout.SOUTH);
    }

    private Icon loadIcon(String source)
    {
        URL url = getClass().getResource(source);
        
        if (url == null)
            return null;
        
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        if (image == null)
            return null;
        return new ImageIcon(image);
    }
    
    public int showDialog()
    {
        pack();
        Rectangle b = getBounds();        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        NMUtilities.fitRectangle(b, screen);
        NMUtilities.centerRectangle(b, screen);
        setBounds(b);
        setVisible(true);
        return result;
    }

    protected void setResult(int result)
    {
        this.result = result;
        dispose();
    }
        
    private class OptionToggler implements PropertyChangeListener
    {
        private Option[] options;
        
        public OptionToggler(Option ... options)
        {
            this.options = options;

            form.addPropertyChangeListener(NomadMidiDialogFrmHandler.INPUT_DEVICE_PROPERTY, this);
            form.addPropertyChangeListener(NomadMidiDialogFrmHandler.OUTPUT_DEVICE_PROPERTY, this);
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            update(); 
        }

        public void update()
        {
            setOptionsEnabled(isValidConfiguration());
        }
        
        private void setOptionsEnabled(boolean enabled)
        {
            for (Option o: options)
                o.setEnabled(enabled);
        }

        private boolean isValidConfiguration()
        {
            return form.getSelectedInput() != null &
            form.getSelectedOutput() != null;
        }
        
    }
    
    private class Option extends AbstractAction
    {
    
        /**
         * 
         */
        private static final long serialVersionUID = -3410297609487159670L;
        private int result;

        public Option(int result, String name)
        {
            putValue(NAME, name);
            this.result = result;
        }
        
        public void actionPerformed(ActionEvent e)
        {
            NomadMidiDialog.this.setResult(result);
        }
            
    }

}
