// http://nmedit.sourceforge.net

package net.sf.nmedit.nomad.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.PatchFilePreviewComponent;
import net.sf.nmedit.jpatch.io.FileTarget;
import net.sf.nmedit.jpatch.io.PatchEncoder;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.NordModular;
import net.sf.nmedit.jsynth.spi.SynthesizerDeviceManager;
import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.main.action.AboutDialogAction;
import net.sf.nmedit.nomad.main.action.AppExitAction;
import net.sf.nmedit.nomad.main.action.FileCloseAction;
import net.sf.nmedit.nomad.main.action.FileCloseAllAction;
import net.sf.nmedit.nomad.main.action.FileNewAction;
import net.sf.nmedit.nomad.main.action.FileOpenAction;
import net.sf.nmedit.nomad.main.action.FileSaveAction;
import net.sf.nmedit.nomad.main.action.FileSaveAsAction;
import net.sf.nmedit.nomad.main.action.FullscreenAction;
import net.sf.nmedit.nomad.main.action.PatchSettingsAction;
import net.sf.nmedit.nomad.main.action.SynthDeviceActions;
import net.sf.nmedit.nomad.main.action.ThemePluginSelector;
import net.sf.nmedit.nomad.main.dialog.PatchSettingsDialog;
import net.sf.nmedit.nomad.main.task.PatchLoader;
import net.sf.nmedit.nomad.main.task.ThemePluginSwitcher;
import net.sf.nmedit.nomad.main.ui.CableSection;
import net.sf.nmedit.nomad.main.ui.HeaderArea;
import net.sf.nmedit.nomad.main.ui.PatchSection;
import net.sf.nmedit.nomad.main.ui.sidebar.ModuleSidebar;
import net.sf.nmedit.nomad.main.ui.sidebar.PatchLibSidebar;
import net.sf.nmedit.nomad.main.ui.sidebar.SidebarControl;
import net.sf.nmedit.nomad.patch.ui.PatchUI;
import net.sf.nmedit.nomad.plugin.NomadPlugin;
import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.util.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;
import net.sf.nmedit.nomad.util.document.DocumentManager;


public class Nomad extends JFrame implements DocumentListener
{

    //private NomadEnvironment     env                  = null;

    private NordModular          device             = null;
    private SynthDeviceActions   synthDeviceActions = null;

    private JFileChooser         fileChooser                 = new JFileChooser(
                                                                     "data/patch/" );

    private DefaultDocumentManager documents                   = null;

    private PatchLoader          loader                      = new PatchLoader(
                                                                     this );

    private ThemePluginSelector  tpm                         = null;

    private HeaderArea           panelTop                    = null;

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

    private PatchImplementation patchImplementation;
    
    public Nomad( String name )
    {
        super( name );

        patchImplementation = PatchImplementation.getImplementation("Clavia Nord Modular Patch", "3.03");

        if (patchImplementation == null)
            throw new RuntimeException("Patch implementation not found");
        
        //env = NomadEnvironment.sharedInstance();
        /*
        // load environment
        env.loadAll();*/

        // frame setup

        // components

        documents = new DefaultDocumentManager();
        documents.addListener(this);
        
        panelTop = new HeaderArea();
        
       // panelTop.add( env.getToolbar() );
        
        //panelTop.addSeparator();
        PatchSection ps = new PatchSection("Patch");
        ps.setDocumentManager(documents);
        panelTop.add(ps);
        panelTop.addSeparator();
        panelTop.add(new CableSection("Cables"));
        
        panelTop.addHSpace();
        
        //panelTop.setLayout( new GridLayout( 1, 2 ) );
       // env.getToolbar().setAlignmentX( 0 );

        
        this.getContentPane().setLayout(new BorderLayout());
        /*
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setLeftComponent(documents.getContainer());
        NomadSidebar sb = new NomadSidebar(this, split);
        split.setRightComponent(sb.getContentPane());*/
        
        SidebarControl sbcontrol = new SidebarControl();
        sbcontrol.getSplitPane().setLeftComponent(documents.getContainer());

        sbcontrol.addSidebar(new ModuleSidebar(this, sbcontrol));
        sbcontrol.addSidebar(new PatchLibSidebar(this, sbcontrol));
        
        //split.setDividerLocation(0);
        this.getContentPane().add( sbcontrol.getSplitPane(), BorderLayout.CENTER );
        this.getContentPane().add( sbcontrol, BorderLayout.EAST );
        this.getContentPane().add( panelTop, BorderLayout.NORTH );

        this.setSize( 1024, 768 );
        //this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        device = (NordModular) SynthesizerDeviceManager.getSynthesizer("Nord Modular", "3.03");
            
            
            new NordModular();
        synthDeviceActions = new SynthDeviceActions(device, this);

        // further setup
        setJMenuBar( createMenu() );
        ToolTipManager.sharedInstance().setInitialDelay( 100 );
        // newSynth(ComPort.getDefaultComPortInstance());

        // file chooser
        fileChooser.setAccessory( new PatchFilePreviewComponent( fileChooser ) );

        /*
        documents.addDocumentSelectionListener( new ChangeListener() {
            public void stateChanged( ChangeEvent event )
            {
                documentSelected( documents.getSelectedDocument() );
            }
        } );*/
        
        

        newPatch();
        validate();
    }


    public void shutdown()
    {
        if (device.isConnected()) 
        {
            try
            {
                device.setConnected(false);
            }
            catch (SynthException e)
            {
                e.printStackTrace();
            }
        }
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
        mnMenu.add( appExitAction );
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
        mnMenu.addSeparator();
        synthDeviceActions.createPatchMenuItems(mnMenu);
        
        mnBar.add( mnMenu );

        // appearance

        mnMenu = new JMenu( "Appearance" );

        mnMenu.add( new FullscreenAction( this ) );

        // appearance -> theme

        tpm = new ThemePluginSelector();
        JMenu mnTheme = new JMenu( "Theme" );
        tpm.createMenu(mnTheme);
        tpm.addPluginSelectionListener( new SelectThemeAction() );

        mnMenu.add( mnTheme );

        // appearance -> look and feel

        mnBar.add( mnMenu );

        // help

        mnMenu = new JMenu( "Help" );
        mnMenu.add( "Help" ).addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent event )
            {
                noDialogYet();
            }
        } );
        mnMenu.addSeparator();

        mnMenu.add( new AboutDialogAction(this));
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

    public DocumentManager getDocumentContainer()
    {
        return documents;
    }

    public void addPatchUI( String name, PatchUI patchui )
    {
        patchui.setName(name);
        documents.add( (Document) patchui );
    }

    public void newPatch()
    {
        PatchUI tab = PatchUI.newInstance( (Patch) patchImplementation.createPatch() );
        documents.add( (Document) tab);
        //documents.setSelectedDocument( tab );
        /*
        documents.getSelectedDocument().setName(
                documents.getTitleAt( documents.getSelectedDocumentIndex() ) );
        */
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
            openPatchFiles( fileChooser.getSelectedFiles() );
        }
        fileChooser.setMultiSelectionEnabled( false );
    }

    public void openPatchFiles(File[] files)
    {
        loader.loadPatch( files );
    }

    public void savePatchAs()
    {
        PatchUI patchui = getActivePatch();
        if (patchui == null)
        {
            System.err.println( "No patch selected." );
            return;
        }

        fileChooser.setSelectedFile( new File( documents.getSelection()
                .getTitle()
                + "_new.pch" ) );

        if (NomadUtilities.isConfirmedByUser( this,
                "Saving does not produce valid files yet. Shall I go on ?" ))

            if (fileChooser.showSaveDialog( null ) == JFileChooser.APPROVE_OPTION)
            {

                try
                {
                    PatchEncoder enc = patchImplementation.createPatchEncoder(FileTarget.class);
                    
                    enc.setSource(patchui.getPatch());
                    Writer writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));
                    enc.encode(new FileTarget(writer));
                    
                    /*
                    
                    enc

                    PrintWriter out = new PrintWriter( new FileWriter(
                            fileChooser.getSelectedFile() ) );
                    
                    VirtualTranscoder t = new VirtualTranscoder();
                    t.transcode(patchui.getPatch(), new StreamBuilder(out));
                    
                    out.flush();
                    */
                }
                catch (Exception e)
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
        if (documents.getSelection() != null)
        {
            documents.remove( documents.getSelection() );
        }
    }

    public void closeEachPatch()
    {
        for (Document d : documents.getDocuments())
        {
            documents.remove( d );
        }
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

    public PatchUI getActivePatch()
    {
        return (PatchUI) documents.getSelection();
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

    class SelectThemeAction implements ChangeListener
    {
        public void stateChanged( ChangeEvent event )
        {
            NomadPlugin plugin = tpm.getSelectedPlugin();
            if (plugin != null) changeSynthInterface( plugin );
        }
    }

    public void documentSelected( Document document )
    { }

    public void documentRemoved( Document document )
    {
        fileCloseAction.setEnabled( canClose() );    
        fileCloseAllAction.setEnabled( canClose() );
        
        fileSaveAction.setEnabled( canClose() );
        fileSaveAsAction.setEnabled( canClose() );
        patchSettingsAction.setEnabled( canClose() );
    }

    public void documentAdded( Document document )
    {
        fileCloseAction.setEnabled( canClose() );    
        fileCloseAllAction.setEnabled( canClose() );

        fileSaveAction.setEnabled( canClose() );
        fileSaveAsAction.setEnabled( canClose() );
        patchSettingsAction.setEnabled( canClose() );
    }


    public PatchImplementation getPatchImplementation()
    {
        return patchImplementation;
    }

}
