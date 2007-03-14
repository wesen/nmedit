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
 * Created on Oct 31, 2006
 */
package net.sf.nmedit.nomad.core.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WizardDialog extends JDialog
{

    private Wizard wizard;
    private JButton btnBack;
    private JButton btnNext;
    private JButton btnFinish;
    private JButton btnCancel;
    private JComponent wizardComponent = null;
    
    public WizardDialog(Wizard wizard) throws HeadlessException
    {
        super();
        initComponent(wizard);
    }

    public WizardDialog( Wizard wizard, Frame owner ) throws HeadlessException
    {
        super( owner );
        initComponent(wizard);
    }

    public WizardDialog( Wizard wizard, Frame owner, boolean modal ) throws HeadlessException
    {
        super( owner, modal );
        initComponent(wizard);
    }

    public WizardDialog( Wizard wizard, Dialog owner ) throws HeadlessException
    {
        super( owner );
        initComponent(wizard);
    }

    public WizardDialog( Wizard wizard, Dialog owner, boolean modal ) throws HeadlessException
    {
        super( owner, modal );
        initComponent(wizard);
    }

    private void initComponent( Wizard wizard )
    {
        this.wizard = wizard;
        
        ButtonListener bl = new ButtonListener();
        
        btnBack = new JButton( "Back");
        btnBack.addActionListener(bl);
        updateBtnBack();

        btnNext = new JButton("Next");
        btnNext.addActionListener(bl);
        btnNext.setDefaultCapable(true);
        updateBtnNext();
        
        btnFinish = new JButton("Finish");
        btnFinish.addActionListener(bl);
        btnFinish.setDefaultCapable(true);
        updateBtnFinish();
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(bl);
        

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(btnBack);
        btnPanel.add(btnNext);
        btnPanel.add(Box.createHorizontalStrut(4));
        btnPanel.add(btnFinish);
        btnPanel.add(Box.createHorizontalStrut(4));
        btnPanel.add(btnCancel);
        
        Border border = 
        BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),   
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        );
        
        btnPanel.setBorder(border);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        
        WizardListener l = new WizardListener();
        wizard.addChangeListener(l);
        wizard.addActionListener(l);
        
        updateWizardComponent();
    }
    
    private void updateBtnBack()
    {
        btnBack.setEnabled(wizard.canGoBack());
    }
    
    private void updateBtnFinish()
    {
        btnFinish.setEnabled(wizard.canFinish());
    }
    
    private void updateBtnNext()
    {
        btnNext.setEnabled(wizard.canGoNext());
    }

    private void updateWizardComponent()
    {
        
        if (wizardComponent!=null)
        {
            // remove component
            getContentPane().remove(wizardComponent);
            wizardComponent = null;
        }
        
        wizardComponent = wizard.createCurrentUI();
        setTitle(wizard.getCurrentDescription());
        
        // install new component
        getContentPane().add(wizardComponent, BorderLayout.CENTER);
        
        updateBtnBack();
        updateBtnNext();
        updateBtnFinish();
        
        validate();
        repaint();
    }
    
    private class WizardListener implements ChangeListener, ActionListener
    {

        public void stateChanged( ChangeEvent e )
        {
            if (e.getSource() == wizard)
            {
                updateBtnBack();
                updateBtnNext();
                updateBtnFinish();
            }
        }

        public void actionPerformed( ActionEvent e )
        {
            if (e.getSource() instanceof Wizard)
            {
                switch (e.getID())
                {
                    case Wizard.CANCEL:
                        cancel();
                        break;
                    case Wizard.NEXTFINISH:
                        if (wizard.canGoNext())
                            next();
                        else if (wizard.canFinish())
                            finish();
                        break;
                    case Wizard.BACK:
                        back();
                        break;
                        
                }
            }
        }
        
    }

    private void back()
    {
        if (wizard.canGoBack())
        {
            wizard.back();
            updateWizardComponent();
        }
    }

    private void next()
    {
        if (wizard.canGoNext())
        {
            wizard.next();
            updateWizardComponent();
        }
    }

    private void finish()
    {
        if (wizard.canFinish())
        {
            wizard.finish();
            WizardDialog.this.dispose();
        }
    }

    boolean isCanceled = false;
    private void cancel()
    {
        if (!isCanceled)
        {
            isCanceled = true;
            wizard.cancel();
            WizardDialog.this.dispose();
        }
    }
    
    private class ButtonListener implements ActionListener
    {

        public void actionPerformed( final ActionEvent e )
        {
            SwingUtilities.invokeLater(new Runnable(){
                public void run()
                {
                    if (e.getSource()==btnNext)
                        next();
                    else if (e.getSource()==btnFinish)
                        finish();
                    else if (e.getSource()==btnBack)
                        back();
                    else if (e.getSource()==btnCancel)
                        cancel();
                }
            }
            );
        }
        
    }
    
}
