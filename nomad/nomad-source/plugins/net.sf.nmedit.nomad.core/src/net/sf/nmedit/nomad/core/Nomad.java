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
 * Created on Nov 23, 2006
 */
package net.sf.nmedit.nomad.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import net.sf.nmedit.nomad.core.NomadLoader.LocaleHandler;
import net.sf.nmedit.nomad.core.helpers.DocumentActionActivator;
import net.sf.nmedit.nomad.core.helpers.RuntimeMenuBuilder;
import net.sf.nmedit.nomad.core.i18n.LocaleConfiguration;
import net.sf.nmedit.nomad.core.jpf.PluginView;
import net.sf.nmedit.nomad.core.menulayout.ActionHandler;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.fileService.FileService;
import net.sf.nmedit.nomad.core.service.fileService.FileServiceTool;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.swing.ButtonBarBuilder;
import net.sf.nmedit.nomad.core.swing.Factory;
import net.sf.nmedit.nomad.core.swing.JDropDownButtonControl;
import net.sf.nmedit.nomad.core.swing.URIListDropHandler;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentEvent;
import net.sf.nmedit.nomad.core.swing.document.DocumentListener;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.tabs.JTabbedPane2;
import net.sf.nmedit.nomad.core.utils.ClonedAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.Boot;

public class Nomad
{

    private static final String MENU_FILE_OPEN = "nomad.menu.file.open";
    private static final String MENU_FILE_SAVE = "nomad.menu.file.save.save";
    private static final String MENU_FILE_SAVEAS = "nomad.menu.file.save.saveas";
    private static final String MENU_FILE_PROPERTIES = "nomad.menu.file.properties";
    private static final String MENU_FILE_EXPORT = "nomad.menu.file.ie.export";
    
    
    private static Nomad instance;
    private JFrame mainWindow = null;
    private NomadPlugin pluginInstance;
    
    private boolean stopped = false;
    
    private DefaultDocumentManager pageContainer ;
    private MenuLayout menuLayout;
    private MenuBuilder menuBuilder;
    private ExplorerTree explorerTree;
    private JTabbedPane2 toolPane;
    
    public DefaultDocumentManager getDocumentManager()
    {
        return pageContainer;
    }
    
    public Nomad(NomadPlugin plugin, MenuLayout menuLayout) 
    {
        mainWindow = new JFrame("Nomad");
        menuBuilder = new MenuBuilder(menuLayout);
        this.pluginInstance = plugin;
        Nomad.instance = this;
        this.menuLayout = menuLayout;
    }
    
    public void pluginsHelp()
    {
        Document d = new PluginView();
        getDocumentManager().add(d);
        getDocumentManager().setSelection(d);
    }
    
    private class DocumentSelectionHandler implements DocumentListener
    {
        public void documentAdded(DocumentEvent e)
        {
            // no op
        }

        public void documentRemoved(DocumentEvent e)
        {
            // no op
        }

        public void documentSelected(DocumentEvent e)
        {
            setMenuForDocument(e.getDocument());
        }
        
        public void setMenuForDocument(Document d)
        {
            boolean saveEnabled = false;
            boolean saveAsEnabled = false;
            boolean propertiesEnabled = false;
            if (d != null)
            {
                Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
                while (iter.hasNext() && (!saveEnabled) && (!saveAsEnabled) && (!propertiesEnabled))
                {
                    FileService fs = iter.next();
                    if (!saveEnabled) saveEnabled = fs.isDirectSaveOperationSupported(d);
                    if (!saveAsEnabled) saveAsEnabled = fs.isSaveOperationSupported(d);
                    if (!propertiesEnabled) propertiesEnabled = fs.isEditPropertiesSupported(d);
                }
            }
            menuLayout.getEntry(MENU_FILE_SAVE).setEnabled(saveEnabled);
            menuLayout.getEntry(MENU_FILE_SAVEAS).setEnabled(saveAsEnabled);
            menuLayout.getEntry(MENU_FILE_PROPERTIES).setEnabled(propertiesEnabled);
            menuLayout.getEntry(MENU_FILE_EXPORT).setEnabled(d instanceof Transferable);
        }
        
    }

    public void export()
    {
        Document doc = getDocumentManager().getSelection();
        if (!(doc instanceof Transferable))
            return;
        
        Transferable transferable = (Transferable) doc;
        
        String title = doc.getTitle();
        if (title == null)
            title = "Export";
        else
            title = "Export '"+title+"'";
        
        JComboBox src = new JComboBox(transferable.getTransferDataFlavors());
        src.setRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent(
                JList list,
            Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
            {
                String text;
                if (value instanceof DataFlavor)
                {
                    DataFlavor flavor = (DataFlavor) value;
                    String mimeType = flavor.getMimeType();
                    String humanRep = flavor.getHumanPresentableName();
                    String charset = flavor.getParameter("charset");
                    
                    if (mimeType == null)
                        text = "?";
                    else
                    {
                        text = mimeType;
                        int ix = text.indexOf(';');
                        if (ix>=0)
                            text = text.substring(0, ix).trim();
                    }
                    if (charset != null)
                        text+="; charset="+charset;
                    if (humanRep != null)
                        text+=" ("+humanRep+")";
                }
                else
                {
                    text = String.valueOf(value);
                }
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
        
        JComboBox dst = new JComboBox(new Object[]{"File", "Clipboard"});
        
        Object[] msg = {"Source:", doc.getTitle(), 
                "Export as:", src,
                "Export to:", dst};
        Object[] options = {"Ok", "Cancel"};
        
        JOptionPane op = new JOptionPane(
                msg, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
        
        JDialog dialog = op.createDialog(getWindow(), title);
        dialog.setModal(true);
        dialog.setVisible(true);
        
        boolean ok =  "Ok".equals(op.getValue());
        
        DataFlavor flavor = (DataFlavor) src.getSelectedItem();
        dialog.dispose();
        if(!ok) return;
        
        if (flavor == null)
            return;
        
        if ("Clipboard".equals(dst.getSelectedItem()))
        {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(new SelectedTransfer(flavor, transferable), null);
        }
        else
        {
            export(transferable, flavor);
        }
    }

    private static class SelectedTransfer implements Transferable
    {
        
        private DataFlavor selectedFlavor;
        private Transferable delegate;

        public SelectedTransfer(DataFlavor selectedFlavor, Transferable delegate)
        {
            this.selectedFlavor = selectedFlavor;
            this.delegate = delegate;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
            if (!selectedFlavor.match(flavor))
                throw new UnsupportedFlavorException(flavor);
            return delegate.getTransferData(flavor);
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            DataFlavor[] flavors = {selectedFlavor};
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return selectedFlavor.equals(flavor);
        }
        
    }
    
    private void export(Transferable transferable, DataFlavor flavor)
    {
        File file = getExportFile();
        if (file == null)
            return;
        
        Reader reader;
        try
        {
            reader = flavor.getReaderForText(transferable);
        }
        catch(Exception e)
        {
            reader = null;
        }
        
        if (reader != null)
        {
            try
            {
                FileOutputStream out = new FileOutputStream(file);
                try
                {
                    int data;
                    while ((data=reader.read())!=-1)
                    {
                        out.write(data);
                    }
                    out.flush();
                }
                finally
                {
                    out.close();
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            return ;
        }
        
        if (DataFlavor.imageFlavor.match(flavor))
        {
            Image image;
            try
            {
                image = (Image) transferable.getTransferData(flavor);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            
            try
            {
                ImageIO.write((RenderedImage) image, "png", file);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            
        }
        // else report unsupported flavor
    }

    private File getExportFile()
    {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(mainWindow)==JFileChooser.APPROVE_OPTION)
        {
            return chooser.getSelectedFile();
        }
        return null;
    }
    
    public void fileSave()
    {
        fileSave(false);
    }

    public void fileSaveAs()
    {
        fileSave(true);
    }
    
    public void fileSave(boolean saveAs)
    {
        Document d = pageContainer.getSelection();
        if (d == null) return;
        
        if (!saveAs)
        {
            Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
            FileService useService = null;
            while (iter.hasNext())
            {
                FileService fs = iter.next();
                if (fs.isDirectSaveOperationSupported(d))
                {
                    useService = fs;
                    break;
                }
            }
            
            if (useService != null)
            {
                useService.save(d, useService.getAssociatedFile(d));
                return ;
            }
        }
        

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);

        Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
        while (iter.hasNext())
        {
            FileService fs = iter.next();
            
            boolean add = 
                (saveAs && fs.isSaveOperationSupported(d))
                || ((!saveAs)&&fs.isDirectSaveOperationSupported(d));
            
            if (add)
                chooser.addChoosableFileFilter(fs.getFileFilter());
        }

        chooser.setSelectedFile(d.getFile());
        
        if (!(chooser.showSaveDialog(mainWindow)==JFileChooser.APPROVE_OPTION)) return;
        
        FileService service = FileServiceTool.lookupFileService(chooser);
        
        
        if (service != null)
        {
            File newFile = chooser.getSelectedFile();
            if (newFile == null) return;
            
            if (newFile.exists() && JOptionPane.showConfirmDialog(mainWindow, "Overwrite existing file '"+newFile.getAbsolutePath()
                    +"' ?", "File already exists.", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                return;

            service.save(d, newFile);
        }
        else
        {
            JOptionPane.showMessageDialog(mainWindow, "Could not find service to save file.");
        }
    }

    public void fileProperties()
    {
        Document d = pageContainer.getSelection();
        if (d == null) return;

        FileService service = null;
        
        Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
        while (iter.hasNext() && service == null)
        {
            FileService fs = iter.next();
            
            if (fs.isEditPropertiesSupported(d))
            {
                service = fs;
            }
        }

        service.editProperties(d);
    }
    
    public void fileOpen()
    {
        JFileChooser chooser = new JFileChooser(new File("/home/christian/Programme/nomad/data/patch/"));
        chooser.setMultiSelectionEnabled(true);
        FileServiceTool.addChoosableFileFilters(chooser);
        if (!(chooser.showOpenDialog(mainWindow)==JFileChooser.APPROVE_OPTION))
        return;
        
        FileService service =
        FileServiceTool.lookupFileService(chooser);
        
        if (service != null)
        {
            for (File file:chooser.getSelectedFiles())
            {
                service.open(file);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(mainWindow, "Could not find service to open file.");
        }
    }
    
    public ExplorerTree getExplorer()
    {
        return explorerTree;
    }
    
    void setupUI()
    {
        // before menu builder is used
        RuntimeMenuBuilder.buildNewMenuEntries(menuLayout);

        ResourceBundle localizedMessages = NomadLoader.getResourceBundle();

        menuLayout.getEntry(MENU_FILE_OPEN)
        .addActionListener(new ActionHandler(this, true, "fileOpen"));
        menuLayout.getEntry(MENU_FILE_SAVE)
        .addActionListener(new ActionHandler(this, true, "fileSave"));
        menuLayout.getEntry(MENU_FILE_SAVEAS)
        .addActionListener(new ActionHandler(this, true, "fileSaveAs"));
        menuLayout.getEntry(MENU_FILE_PROPERTIES)
        .addActionListener(new ActionHandler(this, true, "fileProperties"));
        menuLayout.getEntry("nomad.menu.help.plugins")
        .addActionListener(new ActionHandler(this, true, "pluginsHelp"));

        menuLayout.getEntry(MENU_FILE_EXPORT)
        .addActionListener(new ActionHandler(this, true, "export"));
        /*
        MLEntry mnLang = menuLayout.getEntry("nomad.menu.window.language");
        
        for (int i=0;i<mnLang.size();i++)
        {
            MLEntry lang = mnLang.getEntryAt(i);
            lang.addActionListener(new LocaleSwitcher(lang.getLocalEntryPoint()));
        }*/
        
        menuBuilder.setResourceBundle(localizedMessages);
        LocaleConfiguration.getLocaleConfiguration().addLocaleChangeListener(new LocaleHandler(menuBuilder));
        final JMenuBar mainMenuBar = menuBuilder.createMenuBar("nomad.menu");

        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainWindow.setJMenuBar(mainMenuBar);

        menuBuilder.addActionListener("nomad.menu.file.exit", new ActionHandler(this, true, "handleExit"));
        mainWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing( WindowEvent e )
                    {
                        Nomad.sharedInstance().handleExit();
                    }
                });
        
        


        MenuLayout.disableGhosts(menuLayout);


        pageContainer = new DefaultDocumentManager();

        DocumentSelectionHandler dsh = new DocumentSelectionHandler();
        pageContainer.addListener(dsh);
        dsh.setMenuForDocument(pageContainer.getSelection());

        Container contentPane = mainWindow.getContentPane();
        
        explorerTree = new ExplorerTree();
        explorerTree.setFont(new Font("Arial", Font.PLAIN, 11));
        JScrollPane explorerTreeScroller = new JScrollPane(explorerTree);
        toolPane = new JTabbedPane2();
        toolPane.setCloseActionEnabled(false);
        
        JPanel explorerPane = new JPanel();
        explorerPane.setLayout(new BorderLayout());
        
        ButtonBarBuilder ebuttonBar = new ButtonBarBuilder();
        
        Action newLocAction = new ClonedAction(RuntimeMenuBuilder.getNewLocationAction(menuLayout));
        newLocAction.putValue(Action.NAME, null);
        
        ebuttonBar.add(newLocAction);
        ebuttonBar.addBox();
        ebuttonBar.addFlatButton(explorerTree.createCollapseAllAction());
        
        explorerPane.add(ebuttonBar.getContainer(), BorderLayout.NORTH);
        explorerPane.add(explorerTreeScroller, BorderLayout.CENTER);
        
        toolPane.addTab("Explorer", getImage("/icons/eview16/filenav_nav.gif"), explorerPane);

        new DropTarget(contentPane, new URIListDropHandler() {
            public void uriListDropped(URI[] uriList)
            {
                for (URI uri: uriList)
                {
                    try
                    { 
                    File f = new File(uri);
                    openOrSelect(f);
                    }
                    catch (IllegalArgumentException e)
                    {
                        // ignore
                    }
                }
            }
        });
        
        new DocumentActionActivator(pageContainer, menuLayout);
        
        JSplitPane splitLR = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitLR.setResizeWeight(0);
        splitLR.setDividerLocation(200);
        splitLR.setRightComponent(pageContainer);
        splitLR.setLeftComponent(toolPane);
        
        contentPane.setLayout(new BorderLayout());
        contentPane.add(splitLR, BorderLayout.CENTER);
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(Factory.createToolBarButton(menuLayout.getEntry(MENU_FILE_OPEN)));
        toolbar.addSeparator();
        toolbar.add(Factory.createToolBarButton(menuLayout.getEntry(MENU_FILE_SAVE))) ;
        contentPane.add(toolbar, BorderLayout.NORTH);

        
        JPopupMenu pop = new JPopupMenu();
        Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
        
        JRadioButtonMenuItem rfirst = null;
        SelectedAction sa = new SelectedAction();
        
        
        sa.putValue(AbstractAction.SMALL_ICON, getImage("/icons/tango/16x16/actions/document-new.png"));
        
        while (iter.hasNext())
        {
            FileService fs = iter.next();
            if (fs.isNewFileOperationSupported())
            {
                JRadioButtonMenuItem rb = new JRadioButtonMenuItem(new AHAction(fs.getName(), fs.getIcon(), fs, "newFile"));
                sa.add(rb);
                pop.add(rb);
                if (rfirst == null)
                    rfirst = rb;
            }
        }

        JButton btn = Factory.createToolBarButton(sa);
        toolbar.add(btn);
        new JDropDownButtonControl(btn, pop);
    }
    
    private static class SelectedAction extends AbstractAction implements ItemListener
    {
        private ButtonGroup bg;
        private Action currentAction;

        public SelectedAction()
        {
            this.bg = new ButtonGroup();
            setEnabled(false);
        }
        
        public void add(AbstractButton ab)
        {
            ab.addItemListener(this);
            bg.add(ab);
            setEnabled(true);
            if (bg.getSelection() == null)
                ab.setSelected(true);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if (currentAction != null)
                currentAction.actionPerformed(e);
        }
        
        protected void setCurrentAction(Action o)
        {
            if (currentAction != o)
            {
                currentAction = o;
                
                Object actionCommand = null;
                if (currentAction != null)
                {
                    actionCommand = currentAction.getValue(ACTION_COMMAND_KEY);
                }
                putValue(ACTION_COMMAND_KEY, actionCommand);
            }
            setEnabled(currentAction != null);
        }
        
        protected Action getCurrentAction()
        {
            return currentAction;
        }

        public void itemStateChanged(ItemEvent e)
        {
            Object o = e.getSource();
            if (o instanceof AbstractButton)
                setCurrentAction(((AbstractButton)o).getAction());
            else
                setCurrentAction(null);
        }
        
    }
    
    private static class AHAction extends AbstractAction
    {
        
        private ActionHandler actionHandler;
        
        public AHAction(String title, Icon icon, Object imp, String method)
        {
            if (title != null)
                putValue(NAME, title);
            if (icon != null)
                putValue(SMALL_ICON, icon);
            actionHandler = new ActionHandler(imp, method);
        }

        public void actionPerformed(ActionEvent e)
        {
            actionHandler.actionPerformed(e);
        }
        
    }
    
    private ImageIcon getImage(String name)
    {
        return new ImageIcon(getClass().getResource(name));
    }

    public JTabbedPane2 getToolPane()
    {
        return toolPane;
    }
    
    public NomadPlugin getCorePlugin()
    {
        return pluginInstance;
    }
    
    public PluginManager getPluginManager()
    {
        return PluginManager.lookup(pluginInstance);
    }
    
    public static synchronized Nomad sharedInstance()
    {
        return instance;
    }

    public JFrame getWindow()
    {
        return mainWindow;
    }
    
    public boolean askStopApplication()
    {
        if (!stopped)
        {
            stop();
            return stopped;
        }
        return false;
    }
    
    private void stop()
    {
        stopped = true;
        getWindow().setVisible(false);

        for (Iterator<InitService> i=ServiceRegistry.getServices(InitService.class); i.hasNext();)
        {
            try
            {
                i.next().shutdown();
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
        shutDownPlugin();
        getPluginManager().shutdown();
        System.exit(0);
    }
    
    private void shutDownPlugin()
    {
        Log log = LogFactory.getLog(getClass());
        log.info("Shutting down "+pluginInstance);
        
        try
        {
            Boot.stopApplication(pluginInstance);
        }
        catch (Exception e)
        {
          //  log.warn(e);
            e.printStackTrace();
        }
    }
    
    public void handleExit()
    {
        askStopApplication();
    }

    public MenuLayout getMenuLayout()
    {
        return menuLayout;
    }
    
    public MenuBuilder getMenuBuilder()
    {
        return menuBuilder;
    }

    public void openOrSelect(File file)
    {
        if (file.isDirectory())
            return;
        
        // find out if file is already open
        
        for (Document d: getDocumentManager().getDocuments())
        {
            if (file.equals(d.getFile()))
            {
                getDocumentManager().setSelection(d);
                return ;
            }
        }
        
        
        Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
        while (iter.hasNext())
        {
            FileService fs = iter.next();
            if (fs.isOpenFileOperationSupported())
            {
                if (fs.getFileFilter().accept(file))
                {
                    int count = pageContainer.getDocumentCount();
                    
                    fs.open(file);
                    int newCount = pageContainer.getDocumentCount(); 
                    if (newCount>count)
                    {
                        // condition may be false if fs.open() creates the document
                        // on the event dispatch thread
                        pageContainer.setSelectedIndex(newCount-1);
                    }
                    return;
                }
            }
        }
    }
    
}
