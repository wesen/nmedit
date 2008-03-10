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
package net.sf.nmedit.nomad.core.helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;

import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.fileService.FileService;
import net.sf.nmedit.nomad.core.service.synthService.NewSynthService;
import net.sf.nmedit.nomad.core.swing.ExtensionFilter;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.FileContext;

public class RuntimeMenuBuilder
{   
    private final static String FILE_NEW_ROOT_KEY = "nomad.menu.file.new";
    private final static String SYNTH_ROOT_KEY = "nomad.menu.file.new.global.synth";
    private final static String NEW_LOCATION_ROOT_KEY = "nomad.menu.file.new.global.location";
    
    private static transient Icon newFileIcon;
    
    private static Icon getNewFileIcon()
    {
        if (newFileIcon == null)
            newFileIcon = getIcon("/icons/tango/16x16/actions/document-new.png");
        return newFileIcon;
    }
    
    private static ImageIcon getIcon(String name)
    {
        return new ImageIcon(RuntimeMenuBuilder.class.getResource(name));
    }
    
    private static MLEntry getEntry(MenuLayout layout, String key)
    {
        MLEntry entry = layout.getEntry(key);
        if (entry == null)
            throw new RuntimeException("Could not find menu entry: "+key);
        return entry;
    }
    
    public static Action getNewLocationAction(MenuLayout layout)
    {
        return layout.getEntry(NEW_LOCATION_ROOT_KEY);
    }
    
    public static void buildNewMenuEntries(MenuLayout layout, String mainPatchName, String mainSynthName)
    {
        {
            // new location
            MLEntry entry = getEntry(layout, NEW_LOCATION_ROOT_KEY);
            
            entry.addActionListener(new NewLocationAction());
            
        }
        
        {
            // new file
            
            MLEntry root = getEntry(layout, FILE_NEW_ROOT_KEY);

            Icon newFileIcon = getNewFileIcon();
            
            Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
            
            while (iter.hasNext())
            {
                FileService service = iter.next();
                
                if (service.isNewFileOperationSupported())
                {
                    MLEntry entry = new MLEntry(uniqueKey(root));
                    
                    if (service.getName().equals(mainPatchName)) {
                    	String keyBinding = null;
                    	if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
                    		keyBinding = "META+N";
                    	} else {
                    		keyBinding = "CTRL+N";
                    	}
                    	entry.putValue(MLEntry.ACCELERATOR_KEY, MenuBuilder.extractKeyStroke(keyBinding));
                    }
                    
                    entry.putValue(MLEntry.NAME, service.getName());
                    entry.putValue(MLEntry.SHORT_DESCRIPTION, service.getDescription());
                    Icon icon = service.getIcon();
                    if (icon == null)
                        icon = newFileIcon;
                    entry.putValue(MLEntry.SMALL_ICON, icon);
                    
                    entry.addActionListener(new FileNewBridge(service));
                    root.add(0, entry);
                }
            }
        }
        {
            // new synth
            
            MLEntry root = getEntry(layout, SYNTH_ROOT_KEY);
            
            Iterator<NewSynthService> iter = ServiceRegistry.getServices(NewSynthService.class);
            
            while (iter.hasNext())
            {
                MLEntry entry = new MLEntry(uniqueKey(root));
                
                NewSynthService service = iter.next();
                
                if (service.getSynthName().equals(mainSynthName)) {
                	String keyBinding = null;
                	if (Platform.isFlavor(Platform.OS.MacOSFlavor)) {
                		keyBinding = "SHIFT+META+N";
                	} else {
                		keyBinding = "SHIFT+CTRL+N";
                	}
                	entry.putValue(MLEntry.ACCELERATOR_KEY, MenuBuilder.extractKeyStroke(keyBinding));
                }
             
                entry.putValue(MLEntry.NAME, service.getSynthName());

                entry.putValue(MLEntry.SHORT_DESCRIPTION, synthTooltip(service));
                
                entry.addActionListener(new NewSynthBridge(service));
                root.add(entry);
            }
        }
    }

    private static String synthTooltip(NewSynthService service)
    {
        return "<html><body><table>"
        +"<tr><td><b>Synth</b></td><td>"+service.getSynthName()
        +" "+service.getSynthVersion()+"</td></tr>"
        +"<tr><td><b>Vendor</b></td><td>"+service.getSynthVendor()+"</td></tr>"
        +"<tr><td><b>Description</b></td><td>"+service.getSynthDescription()+"</td></tr>"
        +"</table></body></html>";
    }

    private static String uniqueKey(MLEntry parent)
    {
        return "service#"+parent.size();
    }

    private static class NewSynthBridge implements ActionListener, Runnable
    {
        private NewSynthService service;

        public NewSynthBridge(NewSynthService service)
        {
            this.service = service;
        }

        public void actionPerformed(ActionEvent e)
        {
            SwingUtilities.invokeLater(this);
        }
        
        public void run()
        {
            service.newSynth();
        }
        
    }

    private static class FileNewBridge implements ActionListener, Runnable
    {
        private FileService service;

        public FileNewBridge(FileService service)
        {
            this.service = service;
        }

        public void actionPerformed(ActionEvent e)
        {
            SwingUtilities.invokeLater(this);
        }
        
        public void run()
        {
            Nomad n = Nomad.sharedInstance();
            DefaultDocumentManager pageContainer = n.getDocumentManager();
            int count = pageContainer.getDocumentCount();
            
            service.newFile();
            int newCount = pageContainer.getDocumentCount(); 
            if (newCount>count)
            {
                // condition may be false if fs.newFile() creates the document
                // on the event dispatch thread
                pageContainer.setSelectedIndex(newCount-1);
            }

        }
        
    }
    
    private static class NewLocationAction implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            File location;
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            ExtensionFilter filter = null;
            
            Iterator<FileService> iter = ServiceRegistry.getServices(FileService.class);
            
            while (iter.hasNext())
            {
                FileService service = iter.next();
                if (service.isOpenFileOperationSupported())
                {
                    chooser.addChoosableFileFilter(service.getFileFilter());
                }
            }
            
            if (chooser.showOpenDialog(Nomad.sharedInstance().getWindow())
                    == JFileChooser.APPROVE_OPTION)
            {
                location = chooser.getSelectedFile();
                
                javax.swing.filechooser.FileFilter f = chooser.getFileFilter();
                if (f instanceof ExtensionFilter)
                    filter = (ExtensionFilter) f;
            }
            else return;
            
            if (location == null)
                return;
            
            if (!location.isDirectory())
                return;
            
            Nomad n = Nomad.sharedInstance();
            
            ExplorerTree explorer = n.getExplorer();
            
            TreeNode root = explorer.getRoot();
            for (int i=root.getChildCount()-1;i>=0;i--)
            {
                TreeNode context = root.getChildAt(i);
                if (context instanceof FileContext)
                {
                    File existingLocation = ((FileContext) context).getFile();
                    if ( location.equals(existingLocation) )
                    {
                        JOptionPane.showMessageDialog(n.getWindow().getRootPane(), 
                                "Location already exists."
                        );
                        return;
                    }
                }
            }
            
            FileContext c = new FileContext(explorer, location);
            c.setFileFilter( filter );
            
            n.getExplorer().addRootNode(c);
            
        }
        
    }
    
}
