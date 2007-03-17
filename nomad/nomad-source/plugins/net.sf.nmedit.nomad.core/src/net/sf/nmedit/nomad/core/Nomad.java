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
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import net.sf.nmedit.nomad.core.NomadLoader.LocaleHandler;
import net.sf.nmedit.nomad.core.forms.NomadMidiDialogFrmHandler;
import net.sf.nmedit.nomad.core.helpers.DocumentActionActivator;
import net.sf.nmedit.nomad.core.helpers.RuntimeMenuBuilder;
import net.sf.nmedit.nomad.core.i18n.LocaleConfiguration;
import net.sf.nmedit.nomad.core.jpf.JPFPluginDialog;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.fileService.FileService;
import net.sf.nmedit.nomad.core.service.fileService.FileServiceTool;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.util.document.DefaultDocumentManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.Boot;

public class Nomad
{

    private static Nomad instance;
    private JFrame mainWindow = null;
    private NomadPlugin pluginInstance;
    
    private boolean stopped = false;
    
    private DefaultDocumentManager pageContainer ;
    private MenuLayout menuLayout;
    private MenuBuilder menuBuilder;
    private ExplorerTree explorerTree;
    
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

        MenuLayout.disableGhosts(menuLayout);
        
        menuLayout.getEntry("nomad.menu.help.plugins")
        .addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                JPFPluginDialog pd = new JPFPluginDialog(Nomad.sharedInstance().getWindow());
                
                pd.setBounds(0, 0, 300, 300);
                pd.setVisible(true);
            }});

        menuLayout.getEntry("nomad.menu.synth.setup")
        .addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                JDialog dialog = new JDialog(Nomad.this.mainWindow);
                
                dialog.setContentPane(new NomadMidiDialogFrmHandler());

                dialog.setBounds(0, 0, 400, 300);
                dialog.setVisible(true);
            }});

        setupUI();
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
        JTabbedPane left = new JTabbedPane();
        
        left.addTab("Explorer", explorerTreeScroller);
        left.addTab("Modules", new JPanel());
  /*      
        left.setMinimumSize(new Dimension(200,110));
        left.setPreferredSize(new Dimension(200,110));
*/
        pageContainer = new DefaultDocumentManager();

        new DocumentActionActivator(pageContainer, menuLayout);
        
        JSplitPane splitLR = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitLR.setResizeWeight(0);
        splitLR.setDividerLocation(200);
        splitLR.setRightComponent(pageContainer);
        splitLR.setLeftComponent(left);
                
        contentPane.setLayout(new BorderLayout());
       contentPane.add(splitLR, BorderLayout.CENTER);

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
    
}
