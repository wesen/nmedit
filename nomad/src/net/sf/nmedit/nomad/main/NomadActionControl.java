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
 * Created on Jul 5, 2006
 */
package net.sf.nmedit.nomad.main;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nomad.main.action.AboutDialogAction;
import net.sf.nmedit.nomad.main.action.AppExitAction;
import net.sf.nmedit.nomad.main.action.ExportImageAction;
import net.sf.nmedit.nomad.main.action.FileCloseAction;
import net.sf.nmedit.nomad.main.action.FileCloseAllAction;
import net.sf.nmedit.nomad.main.action.FileNewAction;
import net.sf.nmedit.nomad.main.action.FileOpenAction;
import net.sf.nmedit.nomad.main.action.FileSaveAction;
import net.sf.nmedit.nomad.main.action.FileSaveAsAction;
import net.sf.nmedit.nomad.main.action.FullscreenAction;
import net.sf.nmedit.nomad.main.action.LicenseDialogAction;
import net.sf.nmedit.nomad.main.action.PatchSettingsAction;
import net.sf.nmedit.nomad.main.action.RedoAction;
import net.sf.nmedit.nomad.main.action.ShowNoteDialogAction;
import net.sf.nmedit.nomad.main.action.SynthDeviceActions;
import net.sf.nmedit.nomad.main.action.ThemePluginSelector;
import net.sf.nmedit.nomad.main.action.UndoAction;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginProvider;

public class NomadActionControl
{

    public final FileNewAction fileNewAction;
    public final FileOpenAction fileOpenAction;
    public final FileCloseAction fileCloseAction;
    public final FileCloseAllAction fileCloseAllAction;
    public final FileSaveAction fileSaveAction;
    public final FileSaveAsAction fileSaveAsAction;
    public final AppExitAction appExitAction;
    public final PatchSettingsAction patchSettingsAction;
    public final FullscreenAction fullscreenAction;
    public final SynthDeviceActions synthDeviceActions;
    public final AboutDialogAction aboutDialogAction;
    public final ThemePluginSelector  tpm;
    public final ShowNoteDialogAction showNoteDialogAction; 
    public final ExportImageAction exportImageAction;

    public final UndoAction undoAction;
    public final RedoAction redoAction;
    public final LicenseDialogAction licenseDialogAction;
    
    private Nomad nomad;

    public NomadActionControl( Nomad nomad )
    {
        fileNewAction = new FileNewAction( nomad );
        fileOpenAction = new FileOpenAction( nomad );
        fileCloseAction = new FileCloseAction( nomad );
        fileCloseAllAction = new FileCloseAllAction( nomad );
        fileSaveAction = new FileSaveAction( nomad );
        fileSaveAsAction = new FileSaveAsAction( nomad );
        appExitAction = new AppExitAction( nomad );
        patchSettingsAction = new PatchSettingsAction( nomad );
        fullscreenAction = new FullscreenAction(nomad);
        synthDeviceActions = new SynthDeviceActions(nomad.getNordModular(), nomad);
        showNoteDialogAction = new ShowNoteDialogAction(nomad);
        aboutDialogAction = new AboutDialogAction(nomad);
        tpm = new ThemePluginSelector();
        licenseDialogAction = new LicenseDialogAction(nomad);
        exportImageAction = new ExportImageAction(nomad);

        undoAction = new UndoAction(nomad);
        redoAction = new RedoAction(nomad);

        undoAction.setEnabled(false);
        redoAction.setEnabled(false);
        
        this.nomad = nomad;
        setDocumentActionsEnabled(false);
    }

    public JMenuBar createMenu()
    {
        JMenuBar mnBar = new JMenuBar();
        //mnBar.putClientProperty(PlasticLookAndFeel.IS_3D_KEY,Boolean.TRUE);

        JMenu mnMenu = null;

        // file

        mnMenu = new JMenu( "File" );
        mnMenu.setMnemonic( KeyEvent.VK_F );
        mnMenu.add( fileNewAction );
        mnMenu.add( fileOpenAction );
        mnMenu.addSeparator();
        mnMenu.add( fileCloseAction );
        mnMenu.add( fileCloseAllAction );
        mnMenu.addSeparator();
        mnMenu.add( fileSaveAction );
        mnMenu.add( fileSaveAsAction );
        mnMenu.addSeparator();
        mnMenu.add( exportImageAction );
        mnMenu.addSeparator();
        mnMenu.add( appExitAction );
        mnBar.add( mnMenu );
        
        mnMenu = new JMenu( "Edit" );
        mnMenu.setMnemonic( KeyEvent.VK_E );
        mnMenu.add( undoAction );
        mnMenu.add( redoAction );
        mnBar.add( mnMenu );
        
        // synth
        mnMenu = new JMenu( "Synth" );
        mnMenu.setMnemonic( KeyEvent.VK_S );
        synthDeviceActions.createSynthMenuItems(mnMenu);

        mnBar.add( mnMenu );

        // patch
        mnMenu = new JMenu( "Patch" );
        mnMenu.setMnemonic( KeyEvent.VK_P );
        mnMenu.add( patchSettingsAction );
        mnMenu.add( showNoteDialogAction ).setText("Edit/Show Note...");
        mnMenu.addSeparator();
        synthDeviceActions.createPatchMenuItems(mnMenu);
        
        mnBar.add( mnMenu );

        // appearance

        mnMenu = new JMenu( "Appearance" );

        mnMenu.add( fullscreenAction );

        // appearance -> theme

//        JMenu mnTheme = new JMenu( "Theme" );
//        tpm.createMenu(mnTheme);
//        tpm.addPluginSelectionListener( new SelectThemeAction() );

//        mnMenu.add( mnTheme );

        // appearance -> look and feel

        mnBar.add( mnMenu );

        // help

        mnMenu = new JMenu( "Help" );
        mnMenu.add( "Help" ).setEnabled(false);
        mnMenu.addSeparator();

        mnMenu.add( aboutDialogAction);
        mnMenu.add(licenseDialogAction);
        mnBar.add( mnMenu );

        mnBar.validate();
        return mnBar;
    }
    
    public void setDocumentActionsEnabled( boolean hasDocuments )
    {
        fileCloseAction.setEnabled( hasDocuments );    
        fileCloseAllAction.setEnabled( hasDocuments );

        fileSaveAction.setEnabled( hasDocuments );
        fileSaveAsAction.setEnabled( hasDocuments );
        patchSettingsAction.setEnabled( hasDocuments );
        showNoteDialogAction.setEnabled( hasDocuments );
        exportImageAction.setEnabled(hasDocuments);
        
        undoAction.setEnabled( false );
        redoAction.setEnabled( false );
    }


    class SelectThemeAction implements ChangeListener
    {
        public void stateChanged( ChangeEvent event )
        {
            ThemePluginProvider plugin = tpm.getSelectedPlugin();
            if (plugin != null) nomad.changeSynthInterface( plugin );
        }
    }
    
}
