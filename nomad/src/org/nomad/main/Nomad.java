// http://nmedit.sourceforge.net

package org.nomad.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;

import org.nomad.dialog.NomadAboutDialog;
import org.nomad.dialog.PatchSettingsDialog;
import org.nomad.main.action.AppExitAction;
import org.nomad.main.action.FileCloseAction;
import org.nomad.main.action.FileCloseAllAction;
import org.nomad.main.action.FileNewAction;
import org.nomad.main.action.FileOpenAction;
import org.nomad.main.action.FileSaveAction;
import org.nomad.main.action.FileSaveAsAction;
import org.nomad.main.action.FullscreenAction;
import org.nomad.main.action.PatchSettingsAction;
import org.nomad.main.action.SynthSetupAction;
import org.nomad.patch.Patch;
import org.nomad.patch.format.PatchFilePreviewComponent;
import org.nomad.patch.format.PatchFileWriter;
import org.nomad.patch.ui.PatchUI;
import org.nomad.plugin.NomadPlugin;
import org.nomad.synth.Slot;
import org.nomad.synth.SlotListener;
import org.nomad.synth.SynthDevice;
import org.nomad.synth.SynthDeviceStateListener;
import org.nomad.util.misc.NomadUtilities;
import org.nomad.util.view.DocumentManager;
import org.nomad.util.view.SelectableDocumentManager;

public class Nomad extends JFrame implements SynthDeviceStateListener,
        SlotListener
{

    private NomadEnvironment     env                         = null;

    private SynthDevice          synthConnection             = null;

    private JFileChooser         fileChooser                 = new JFileChooser(
                                                                     "data/patch/" );

    private JPanel               panelMain                   = null;

    private DocumentManager      documents                   = null;

    private PatchLoader          loader                      = new PatchLoader(
                                                                     this );

    private JMenuItem            menuSynthConnectionMenuItem = null;

    private JMenuItem            menuSynthUploadCurrentSlot  = null;

    private JRadioButtonMenuItem menuDocumentViewTabbed      = null;

    private JRadioButtonMenuItem menuDocumentViewMDI         = null;

    private ThemePluginMenu      tpm                         = null;

    private PatchUI[]            slotDocs                    = new PatchUI[4];

    private JPanel               panelTop                    = null;

    private JPanel               panelPatchSettings          = null;

    private JPanel               colorSelector;

    private FileNewAction        fileNewAction               = new FileNewAction(
                                                                     this );

    private FileOpenAction       fileOpenAction              = new FileOpenAction(
                                                                     this );

    private FileCloseAction      fileCloseAction             = new FileCloseAction(
                                                                     this );

    private FileCloseAllAction   fileCloseAllAction          = new FileCloseAllAction(
                                                                     this );

    private FileSaveAction       fileSaveAction              = new FileSaveAction(
                                                                     this );

    private FileSaveAsAction     fileSaveAsAction            = new FileSaveAsAction(
                                                                     this );

    private AppExitAction        appExitAction               = new AppExitAction(
                                                                     this );

    private PatchSettingsAction  patchSettingsAction         = new PatchSettingsAction(
                                                                     this );

    public Nomad( String name )
    {
        super( name );

        env = NomadEnvironment.sharedInstance();
        /*
        // load environment
        env.loadAll();*/

        // frame setup

        // components
        panelMain = new JPanel();
        panelMain.setLayout( new BorderLayout() );

        panelTop = new JPanel();
        panelTop.setLayout( new GridLayout( 1, 2 ) );
        env.getToolbar().setAlignmentX( 0 );
        panelTop.add( env.getToolbar() );

        panelPatchSettings = new JPanel();
        panelTop.add( panelPatchSettings );
        panelPatchSettings.add( colorSelector = CableColorSelectorFactory
                .newSelector() );

        this.getContentPane().add( panelMain, BorderLayout.CENTER );
        this.getContentPane().add( panelTop, BorderLayout.NORTH );
        this.setSize( 1024, 768 );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // further setup
        setJMenuBar( createMenu() );
        ToolTipManager.sharedInstance().setInitialDelay( 100 );
        // newSynth(ComPort.getDefaultComPortInstance());
        synthConnection = new SynthDevice();
        synthConnection.addStateListener( this );
        documents = new SelectableDocumentManager( panelMain );
        synthConnection.getSynth().addSlotListener( this );

        addWindowListener( new AutomaticSynthShutdownAction() );

        // file chooser
        fileChooser.setAccessory( new PatchFilePreviewComponent( fileChooser ) );

        Arrays.fill( slotDocs, null );

        documents.addDocumentSelectionListener( new ChangeListener() {
            public void stateChanged( ChangeEvent event )
            {
                documentSelected( documents.getSelectedDocument() );
            }
        } );

        validate();
    }

    private void documentSelected( JComponent d )
    {
        if (d instanceof PatchUI)
        {
            CableColorSelectorFactory.setPatchUI( colorSelector, (PatchUI) d );
        }
    }

    public JMenuBar createMenu()
    {
        JMenuBar mnBar = new JMenuBar();
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
        mnMenu.add( appExitAction );
        mnBar.add( mnMenu );

        // synth

        mnMenu = new JMenu( "Synth" );
        mnMenu.setMnemonic( KeyEvent.VK_S );

        mnMenu.add( new SynthSetupAction( this ) );
        menuSynthConnectionMenuItem = mnMenu.add( "Connect" );
        menuSynthConnectionMenuItem
                .addActionListener( new SynthConnectionMenuItemListener() );
        mnMenu.addSeparator();
        menuSynthUploadCurrentSlot = mnMenu.add( "Upload Active Slot" );
        menuSynthUploadCurrentSlot
                .addActionListener( new UploadPatchFromCurrentSlotAction() );
        mnBar.add( mnMenu );

        // patch
        mnMenu = new JMenu( "Patch" );
        mnMenu.setMnemonic( KeyEvent.VK_P );
        mnMenu.add( patchSettingsAction );
        mnBar.add( mnMenu );

        // appearance

        mnMenu = new JMenu( "Appearance" );

        mnMenu.add( new FullscreenAction( this ) );

        // appearance -> theme

        tpm = new ThemePluginMenu( "Theme" );
        tpm.addPluginSelectionListener( new SelectThemeAction() );

        mnMenu.add( tpm );

        // appearance -> document view

        JMenu mnSubMenu = new JMenu( "Document View" );
        menuDocumentViewTabbed = new JRadioButtonMenuItem( "Tabbed" );
        menuDocumentViewTabbed.setSelected( true );
        menuDocumentViewTabbed
                .addActionListener( new SwitchDocumentViewAction() );
        mnSubMenu.add( menuDocumentViewTabbed );

        menuDocumentViewMDI = new JRadioButtonMenuItem( "MDI" );
        menuDocumentViewMDI.addActionListener( new SwitchDocumentViewAction() );
        mnSubMenu.add( menuDocumentViewMDI );

        ButtonGroup group = new ButtonGroup();
        group.add( menuDocumentViewTabbed );
        group.add( menuDocumentViewMDI );

        mnMenu.add( mnSubMenu );

        // appearance -> look and feel

        mnBar.add( mnMenu );

        // help

        mnMenu = new JMenu( "Help" );
        mnMenu.add( "Plugins..." ).addActionListener(
                new ShowPluginDialogAction() );

        mnMenu.addSeparator();
        mnMenu.add( "Debug: gc()" ).addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent event )
            {
                System.gc();
            }
        } );

        mnMenu.add( "About" ).addActionListener( new ShowAboutDialogAction() );
        mnBar.add( mnMenu );

        mnBar.validate();
        return mnBar;
    }

    private boolean   fullScreenMode = false;

    private Dimension oldSize        = new Dimension( 0, 0 );

    private Point     oldLocation    = new Point( 0, 0 );

    public boolean isFullScreenModeEnabled()
    {
        return fullScreenMode;
    }

    public void setFullScreenModeEnabled( boolean enable )
    {
        if (this.fullScreenMode != enable)
        {
            this.fullScreenMode = enable;
            if (enable)
            {
                oldSize = getSize();
                oldLocation = getLocation();

                setLocation( 0, 0 );
                setSize( java.awt.Toolkit.getDefaultToolkit().getScreenSize() );
            }
            else
            {
                setSize( oldSize );
                setLocation( oldLocation );
            }
            // setUndecorated( !enable );
        }
    }

    public DocumentManager getDocumentManager()
    {
        return documents;
    }

    public void addPatchUI( String name, PatchUI patchui )
    {
        getDocumentManager().addDocument( name, patchui );
    }

    public void newPatch()
    {
        PatchUI tab = PatchUI.newInstance( new Patch() );
        documents.addDocument( "new" + ( documents.getDocumentCount() + 1 ),
                tab );
        documents.setSelectedDocument( tab );
        documents.getSelectedDocument().setName(
                documents.getTitleAt( documents.getSelectedDocumentIndex() ) );

        fileCloseAction.setEnabled( true );
        fileCloseAllAction.setEnabled( true );
    }

    public void exitNomad()
    {
        Application.stop();
    }

    public void openPatchFile()
    {
        fileChooser.setMultiSelectionEnabled( true );
        if (fileChooser.showOpenDialog( Nomad.this ) == JFileChooser.APPROVE_OPTION)
        {
            File[] files = fileChooser.getSelectedFiles();
            loader.loadPatch( files );

            fileCloseAction.setEnabled( true );
            fileCloseAllAction.setEnabled( true );
        }
        fileChooser.setMultiSelectionEnabled( false );
    }

    public void savePatchAs()
    {
        PatchUI patchui = getActivePatch();
        if (patchui == null)
        {
            System.err.println( "No patch selected." );
            return;
        }

        fileChooser.setSelectedFile( new File( documents.getSelectedDocument()
                .getName()
                + "_new.pch" ) );

        if (NomadUtilities.isConfirmedByUser( this,
                "Saving does not produce valid files yet. Shall I go on ?" ))

            if (fileChooser.showSaveDialog( null ) == JFileChooser.APPROVE_OPTION)
            {
                try
                {

                    PrintWriter out = new PrintWriter( new FileWriter(
                            fileChooser.getSelectedFile() ) );
                    PatchFileWriter writer = new PatchFileWriter( out, patchui
                            .getPatch() );
                    writer.write();
                    out.flush();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
    }

    public boolean canClose()
    {
        return documents.getDocumentCount() > 0;
    }

    public void closePatch()
    {
        if (documents.getSelectedDocument() != null)
        {
            documents.removeDocumentAt( documents.getSelectedDocumentIndex() );
        }

        fileCloseAction.setEnabled( canClose() );
        fileCloseAllAction.setEnabled( canClose() );
    }

    public void closeEachPatch()
    {
        while (documents.getDocumentCount() > 0)
        {
            documents.removeDocumentAt( 0 );
        }

        fileCloseAction.setEnabled( false );
        fileCloseAllAction.setEnabled( false );
    }

    void noDialogYet()
    {
        NomadUtilities.infoDialog( this, "The dialog is not implemented, yet." );
    }

    void changeSynthInterface( NomadPlugin plugin )
    {
        ThemePluginSwitcher switcher = new ThemePluginSwitcher( this, plugin );
        switcher.switchPlugin();
    }

    PatchUI getActivePatch()
    {
        return (PatchUI) getDocumentManager().getSelectedDocument();
    }

    public void editPatchSettings()
    {
        PatchUI patch = getActivePatch();
        if (patch != null)
        {
            PatchSettingsDialog dialog = new PatchSettingsDialog( patch
                    .getPatch() );
            dialog.invoke();
        }
    }

    public void setupSynth()
    {
        synthConnection.setup();
    }

    public void newPatchInSlot( Slot slot )
    {
        int index = slot.getId();
        if (slotDocs[index] != null)
        {
            documents.removeDocument( slotDocs[index] );
            slotDocs[index] = null;
        }
        Patch p = slot.getPatch();
        if (p != null)
        {
            PatchUI tab = PatchUI.newInstance( p );
            documents.addDocument( tab.getPatch().getName() + " (slot:" + index
                    + ")", tab );
            documents.setSelectedDocument( tab );
            documents.getSelectedDocument()
                    .setName(
                            documents.getTitleAt( documents
                                    .getSelectedDocumentIndex() ) );
            slotDocs[index] = tab;

            fileCloseAction.setEnabled( true );
            fileCloseAllAction.setEnabled( true );
        }
    }

    public void slotSelected( Slot slot )
    {
        PatchUI doc = slotDocs[slot.getId()];
        if (doc != null) documents.setSelectedDocument( doc );
    }

    public void synthConnectionStateChanged( SynthDevice synth )
    {
        this.menuSynthConnectionMenuItem
                .setText( synth.isConnected() ? "Disconnect" : "Connect" );
        this.menuSynthUploadCurrentSlot.setEnabled( synth.isConnected() );
    }

    class AutomaticSynthShutdownAction extends WindowAdapter
    {
        public void windowClosing( WindowEvent event )
        {
            if (synthConnection.isConnected())
            {
                System.out.println( "Closing midi connection." );
                synthConnection.disconnect();
            }
        }
    }

    class SelectThemeAction implements ChangeListener
    {
        public void stateChanged( ChangeEvent event )
        {
            if (event.getSource() instanceof ThemePluginMenu)
            {
                NomadPlugin plugin = ( (ThemePluginMenu) event.getSource() )
                        .getSelectedPlugin();
                if (plugin != null) changeSynthInterface( plugin );

            }
        }
    }

    class UploadPatchFromCurrentSlotAction implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            Patch p = synthConnection.getSynth().getActiveSlot();

            if (p == null)
            {
                System.err.println( "no patch data" );
                return;
            }

            PatchUI tab = PatchUI.newInstance( p );
            documents.addDocument( tab.getPatch().getName(), tab );
            documents.setSelectedDocument( tab );
            documents.getSelectedDocument()
                    .setName(
                            documents.getTitleAt( documents
                                    .getSelectedDocumentIndex() ) );

        }
    }

    class SwitchDocumentViewAction implements ActionListener
    {
        public void actionPerformed( ActionEvent event )
        {
            if (event.getSource() == menuDocumentViewMDI)
                ( (SelectableDocumentManager) documents )
                        .switchDocumentManager( false );
            else if (event.getSource() == menuDocumentViewTabbed)
                ( (SelectableDocumentManager) documents )
                        .switchDocumentManager( true );
        }
    }

    class SynthConnectionMenuItemListener implements ActionListener
    {
        public void actionPerformed( ActionEvent arg0 )
        {
            if (!synthConnection.isConnected())
                synthConnection.connect();
            else synthConnection.disconnect();
        }
    }

    class ShowPluginDialogAction implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            noDialogYet();
        }
    }

    class ShowAboutDialogAction implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            NomadAboutDialog dlg = new NomadAboutDialog();
            dlg.invoke();
        }
    }

}
