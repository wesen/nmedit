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
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.sf.nmedit.nomad.core.NomadLoader.LocaleHandler;
import net.sf.nmedit.nomad.core.helpers.DocumentActionActivator;
import net.sf.nmedit.nomad.core.helpers.RuntimeMenuBuilder;
import net.sf.nmedit.nomad.core.i18n.LocaleConfiguration;
import net.sf.nmedit.nomad.core.jpf.PluginView;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.fileService.FileService;
import net.sf.nmedit.nomad.core.service.fileService.FileServiceTool;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.swing.ButtonBarBuilder;
import net.sf.nmedit.nomad.core.swing.FileTransferHandler;
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

    private static final String MENU_FILE_SAVE = "nomad.menu.file.save.save";
    private static final String MENU_FILE_SAVEAS = "nomad.menu.file.save.saveas";
    private static final String MENU_FILE_PROPERTIES = "nomad.menu.file.properties";
    
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
    
    public Nomad(NomadPlugin plugin, MenuLayout menuLayout) throws Exception
    {

        // before menu builder is used
        RuntimeMenuBuilder.buildNewMenuEntries(menuLayout);

        ResourceBundle localizedMessages = NomadLoader.getResourceBundle();

        menuLayout.getEntry("nomad.menu.file.open")
        .addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                fileOpen();
            }});

        menuLayout.getEntry(MENU_FILE_SAVE)
        .addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                fileSave(false);
            }});

        menuLayout.getEntry(MENU_FILE_SAVEAS)
        .addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                fileSave(true);
            }});

        menuLayout.getEntry(MENU_FILE_PROPERTIES)
        .addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                fileProperties();
            }});

        menuBuilder = new MenuBuilder(menuLayout);
        menuBuilder.setResourceBundle(localizedMessages);
        LocaleConfiguration.getLocaleConfiguration().addLocaleChangeListener(new LocaleHandler(menuBuilder));
        final JMenuBar mainMenuBar = menuBuilder.createMenuBar("nomad.menu");

        JFrame main = new JFrame("Nomad");
        mainWindow = main;
        main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        main.setJMenuBar(mainMenuBar);

        // finally install actions/listeners
        final ExitHandler exitHandler = new ExitHandler();
        menuBuilder.addActionListener("nomad.menu.file.exit", exitHandler);
        main.addWindowListener(exitHandler);
        
        
        this.pluginInstance = plugin;
        Nomad.instance = this;
        
        this.menuLayout = menuLayout;

        menuLayout.getEntry("nomad.menu.help.plugins")
        .addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                PluginView view = new PluginView();
                Nomad.sharedInstance().getDocumentManager()
                .add(view);
            }});

        MenuLayout.disableGhosts(menuLayout);

        setupUI();
        
        DocumentSelectionHandler dsh = new DocumentSelectionHandler();
        pageContainer.addListener(dsh);
        dsh.setMenuForDocument(pageContainer.getSelection());

        //pageContainer.getTabBar()
        pageContainer
        .setTransferHandler(new FileTransferHandler(){
            /**
             * 
             */
            private static final long serialVersionUID = 7177859593782278190L;

            protected boolean importFiles(JComponent c, List<File> filelist)
            {
                if (c!=pageContainer)
                    return false;
                
                for (File f: filelist)
                    openOrSelect(f);
                return true;
            }
        });
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
        }
        
    }

    void fileSave(boolean saveAs)
    {
        Document d = pageContainer.getSelection();
        if (d == null) return;

        JFileChooser chooser = new JFileChooser(d.getFile());
        chooser.setMultiSelectionEnabled(false);

        Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
        while (iter.hasNext())
        {
            FileService fs = iter.next();
            
            boolean add = 
                (saveAs && fs.isSaveOperationSupported(d))
                || ((!saveAs)&&fs.isSaveOperationSupported(d));
            
            if (add)
                chooser.addChoosableFileFilter(fs.getFileFilter());
        }

        
        if (!(chooser.showSaveDialog(mainWindow)==JFileChooser.APPROVE_OPTION)) return;
        
        FileService service = FileServiceTool.lookupFileService(chooser);
        
        if (service != null)
        {
            File newFile = chooser.getSelectedFile();
            if (newFile == null) return;
            service.save(d, newFile);
        }
    }

    void fileProperties()
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
    
    void fileOpen()
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
        
    }
    
    public ExplorerTree getExplorer()
    {
        return explorerTree;
    }
    
    private void setupUI()
    {
        /*
        pageContainer = new JPanel();
        pageContainer.setLayout(new BorderLayout());
        pageContainer.add(pageTabs, BorderLayout.NORTH);

        */
        
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
  /*      
        left.setMinimumSize(new Dimension(200,110));
        left.setPreferredSize(new Dimension(200,110));
*/
        pageContainer = new DefaultDocumentManager();
        /*
        ImageIcon helpIcon = getImage("/icons/etool16/help_contents.gif");
        
        toolPane.addTab("Help", helpIcon, new HelpPane(helpIcon));*/

        new DocumentActionActivator(pageContainer, menuLayout);
        
        JSplitPane splitLR = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitLR.setResizeWeight(0);
        splitLR.setDividerLocation(200);
        splitLR.setRightComponent(pageContainer);
        splitLR.setLeftComponent(toolPane);
                
        contentPane.setLayout(new BorderLayout());
        contentPane.add(splitLR, BorderLayout.CENTER);
/*
 * 
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        contentPane.add(toolbar, BorderLayout.NORTH);
        
        JButton btn = new JButton(getImage("/icons/etool16/new_untitled_text_file.gif"));
        toolbar.add(btn);*/
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

    private static class ExitHandler extends WindowAdapter implements ActionListener, ExitListener
    {

        public void windowClosing( WindowEvent e )
        {
            Nomad.sharedInstance().handleExit();
        }

        public void actionPerformed(ActionEvent e)
        {
            Nomad.sharedInstance().handleExit();
        }

        public void actionExit( ActionEvent e )
        {
            Nomad.sharedInstance().handleExit();
        }
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
                    if (pageContainer.getDocumentCount()>count)
                        pageContainer.setSelectedIndex(pageContainer.getDocumentCount()-1);
                    return;
                }
            }
        }
    }
    
}
