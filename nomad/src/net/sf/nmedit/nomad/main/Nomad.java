// http://nmedit.sourceforge.net

package net.sf.nmedit.nomad.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.PatchFilePreviewComponent;
import net.sf.nmedit.jpatch.io.FileTarget;
import net.sf.nmedit.jpatch.io.PatchEncoder;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.NordModular;
import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.main.dialog.PatchSettingsDialog;
import net.sf.nmedit.nomad.main.task.PatchLoader;
import net.sf.nmedit.nomad.main.task.ThemePluginSwitcher;
import net.sf.nmedit.nomad.main.ui.FileSection;
import net.sf.nmedit.nomad.main.ui.HeaderArea;
import net.sf.nmedit.nomad.main.ui.PatchSection;
import net.sf.nmedit.nomad.main.ui.SynthSection;
import net.sf.nmedit.nomad.main.ui.sidebar.ModuleSidebar;
import net.sf.nmedit.nomad.main.ui.sidebar.PatchLibSidebar;
import net.sf.nmedit.nomad.main.ui.sidebar.Sidebar;
import net.sf.nmedit.nomad.main.ui.sidebar.SidebarControl;
import net.sf.nmedit.nomad.patch.ui.PatchDocument;
import net.sf.nmedit.nomad.patch.ui.PatchUI;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginProvider;
import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.util.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;
import net.sf.nmedit.nomad.util.document.DocumentManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class Nomad extends JFrame implements DocumentListener
{
    private NordModular device = null;
    private JFileChooser fileChooser;
    private DefaultDocumentManager documents = null;
    private PatchLoader loader = new PatchLoader( this );
    private HeaderArea panelTop = null;
    private PatchImplementation patchImplementation;
    private NomadActionControl nomadActions;
    private PatchSection patchSection;

    public final static String KEY_FORM_X = "custom.nomad.form.x";
    public final static String KEY_FORM_Y = "custom.nomad.form.y";
    public final static String KEY_FORM_WIDTH = "custom.nomad.form.width";
    public final static String KEY_FORM_HEIGHT = "custom.nomad.form.height";
    
    public Nomad( String name )
    {
        super( name );

        patchImplementation = PatchImplementation.getImplementation(
                "Clavia Nord Modular Patch", "3.03" );

        if (patchImplementation == null)
            throw new RuntimeException( "Patch implementation not found" );

        
        int slotCount = Application.getIntegerProperty("nordmodular.slots", 4);
        slotCount = Math.max(1, Math.min(slotCount, 4));
        
        
        device = //(NordModular) SynthesizerDeviceManager.getSynthesizer(
                //"Nord Modular", "3.03" );
            new NordModular(slotCount);

        if (device == null)
            throw new RuntimeException( "Synthesizer implementation not found" );

        documents = new DefaultDocumentManager();
        documents.addListener( this );

        panelTop = new HeaderArea();

        nomadActions = new NomadActionControl( this );
        JMenuBar menuBar =  nomadActions.createMenu();
        setJMenuBar(menuBar);
        menuBar.putClientProperty(PlasticLookAndFeel.IS_3D_KEY,Boolean.TRUE);
        

        panelTop.add(new FileSection(this, nomadActions, "File"));
        panelTop.addSeparator();
        patchSection = new PatchSection( this, "Patch", nomadActions.showNoteDialogAction );
        patchSection.setDocumentManager( documents );
        panelTop.add( patchSection );
        panelTop.addSeparator();
        panelTop.add(new SynthSection(this, device, "Synth"));
        /*
        panelTop.addSeparator();
        panelTop.add( new CableSection( "Cables" ) );*/

        panelTop.addHSpace();

        SidebarControl sbcontrol = new SidebarControl();
        sbcontrol.getSplitPane().setLeftComponent( documents.getContainer() );

        Sidebar defaultSidebar =  new ModuleSidebar( this, sbcontrol ) ;
        sbcontrol.addSidebar(defaultSidebar);
        sbcontrol.addSidebar( new PatchLibSidebar( this, sbcontrol ) );

        Container cpane = getContentPane();
        cpane.setLayout( new BorderLayout() );

        cpane.add( sbcontrol.getSplitPane(), BorderLayout.CENTER );
        cpane.add( sbcontrol, BorderLayout.EAST );
        cpane.add( panelTop, BorderLayout.NORTH );

        {
            int w = Application.getIntegerProperty(KEY_FORM_WIDTH, 1024);
            int h = Application.getIntegerProperty(KEY_FORM_HEIGHT, 800);
            this.setSize( Math.max(w, 100), Math.max(h, 100) );
        }
        
        NomadUtilities.center(this);
        
        {
            int x = Application.getIntegerProperty(KEY_FORM_X, getX());
            int y = Application.getIntegerProperty(KEY_FORM_Y, getY());
            this.setLocation( Math.max(x, 0), Math.max(y, 0) );
        }

        validate();

        fileChooser = new JFileChooser( PatchLibSidebar.getDefaultPatchDirectoryFile() );
        fileChooser.setAccessory( new PatchFilePreviewComponent( fileChooser ) );
        fileChooser.addChoosableFileFilter( FileFilterBuilder.createFileFilter("pch", "Nord Modular Patch v3.03") );

        sbcontrol.setCurrentSidebar(defaultSidebar);
        defaultSidebar.setSize(200);
        addComponentListener(new FrameTracker());
    }
    
    private class FrameTracker extends ComponentAdapter
    {
        public void componentMoved(ComponentEvent e)
        {
            Application.setProperty(KEY_FORM_X, Integer.toString(getX()));
            Application.setProperty(KEY_FORM_Y, Integer.toString(getY()));
        }
        public void componentResized(ComponentEvent e)
        {
            Application.setProperty(KEY_FORM_WIDTH, Integer.toString(getWidth()));
            Application.setProperty(KEY_FORM_HEIGHT, Integer.toString(getHeight()));
        }
    }
    
    public void shutdown()
    {
        if (device.isConnected())
        {
            try
            {
                device.setConnected( false );
            }
            catch (SynthException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean fullScreenMode = false;

    private Dimension oldSize = new Dimension( 0, 0 );

    private Point oldLocation = new Point( 0, 0 );

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

    public void addPatchDocument( PatchDocument doc )
    {
        documents.add( doc );
    }

    public void newPatch()
    {
        PatchUI ui = PatchUI.newInstance( (Patch) patchImplementation.createPatch() );        

        PatchDocument doc = new PatchDocument(ui);
        documents.add( doc );
        documents.setSelection( doc );
    }

    public boolean exitNomad()
    {
        if (closeEachPatch())
        {
            Application.stop();
            return true;
        }
        return false;
    }

    public void openPatchFile()
    {
        fileChooser.setMultiSelectionEnabled( true );
        if (fileChooser.showOpenDialog( Nomad.this ) == JFileChooser.APPROVE_OPTION)
        {
            openPatchFiles( fileChooser.getSelectedFiles() );

            PatchLibSidebar.setDefaultPatchDirectory
            (
                    fileChooser.getCurrentDirectory()
            );
        }
        fileChooser.setMultiSelectionEnabled( false );
    }

    public void openPatchFiles( File[] files )
    {
        loader.loadPatch( files );
    }

    public void savePatchAs()
    {
        PatchDocument doc = getActivePatch();
        if (doc == null)
        {
            System.err.println( "No patch selected." );
            return;
        }

        File newFileSuggestion = doc.getFile();
        if (newFileSuggestion == null)
        {
            newFileSuggestion = new File(doc.getPatch().getName()+".pch");
        }

        fileChooser.setSelectedFile(newFileSuggestion);

        if (fileChooser.showSaveDialog( null ) == JFileChooser.APPROVE_OPTION)
        {
            savePatchAs(doc, fileChooser.getSelectedFile());

            PatchLibSidebar.setDefaultPatchDirectory
            (
                    fileChooser.getCurrentDirectory()
            );
        }
    }
    
    private boolean savePatchAs(PatchDocument pdoc, File f)
    {

        try
        {
            PatchEncoder enc = patchImplementation
                    .createPatchEncoder( FileTarget.class );

            f = NomadUtilities.assureFileExtensionExists(f, "pch");
            enc.setSource( pdoc.getPatch() );
            Writer writer = new BufferedWriter( new FileWriter(f) );
            enc.encode( new FileTarget( writer ) );
            pdoc.setFile(f);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canClose()
    {
        return documents.getDocumentCount() > 0;
    }

    public boolean closePatch()
    {
        if (documents.getSelection() != null)
        {
            PatchDocument doc = (PatchDocument) documents.getSelection();
            
            int code = JOptionPane.YES_OPTION;
            if (doc.getPatch().getHistory().isModified())
            {
                code = confirmPatchClosingYNC(doc.getPatch());
                if (code == JOptionPane.YES_OPTION)
                {
                    if (!savePatch())
                        code = JOptionPane.CANCEL_OPTION;
                    
                }
            }
            if (code==JOptionPane.YES_OPTION||code==JOptionPane.NO_OPTION)
            {
                documents.remove( documents.getSelection() );
                return true;
            }
        }
        return false;
    }
    
    private int confirmPatchClosingYNC(Patch p)
    {
        return JOptionPane.showOptionDialog
        (
                this,
                "Patch '"+p.getName()+"' has been modified. Save changes ?",
                "Closing '"+p.getName()+"'",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new Object[]{"Yes", "No", "Cancel"},
                "Yes"
        );
    }

    /*
    private boolean isPatchCloseDataLossConfirmed()
    {
        return JOptionPane.showOptionDialog(
                this,
                "Modifications will be lost, do you really want to close?",
                "Closing",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new Object[]{"Yes", "No"},
                "No"
        ) == JOptionPane.YES_OPTION;
    }*/

    public boolean closeEachPatch()
    {
        while (documents.getDocumentCount()>0)
        {
            if (!closePatch())
                return false;
        }
        return true;
    }

    void noDialogYet()
    {
        NomadUtilities.infoDialog( this, "The dialog is not implemented, yet." );
    }

    void changeSynthInterface( ThemePluginProvider plugin )
    {
        ThemePluginSwitcher switcher = new ThemePluginSwitcher( this, plugin );
        switcher.switchPlugin();
    }

    public PatchDocument getActivePatch()
    {
        return (PatchDocument) documents.getSelection();
    }

    public void editPatchSettings()
    {
        PatchDocument patch = getActivePatch();
        if (patch != null)
        {
            PatchSettingsDialog dialog = new PatchSettingsDialog( patch
                    .getPatch() );
            dialog.invoke();
            
            patchSection.updateView();
        }
    }

    public void documentSelected( Document document )
    {}

    public void documentRemoved( Document document )
    {
        nomadActions.setDocumentActionsEnabled( canClose() );
    }

    public void documentAdded( Document document )
    {
        nomadActions.setDocumentActionsEnabled( canClose() );
    }

    public PatchImplementation getPatchImplementation()
    {
        return patchImplementation;
    }

    public NordModular getNordModular()
    {
        return device;
    }

    public void updateTitle( Patch patch )
    {
        for (Document d : documents.getDocuments())
        {
            PatchDocument doc = (PatchDocument) d;
            if (doc.getPatch() == patch)
            {
                documents.updateTitle((Document)doc);
                return;
            }
        }
    }

    public boolean savePatch()
    {
        PatchDocument doc = getActivePatch();
        File f = doc.getFile();
        if (f==null) savePatchAs();
        else
        {
            if (savePatchAs(doc, f))
            {
                doc.getPatch().getHistory().setModified(false);
                return true;
            }
        }
        return false;
    }

    public void tryRedo()
    {
        // TODO Auto-generated method stub
        
    }

    public void tryUndo()
    {
        // TODO Auto-generated method stub
        
    }

}
