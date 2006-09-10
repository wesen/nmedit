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
 * Created on Jun 22, 2006
 */
package net.sf.nmedit.nomad.main.ui.sidebar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.resources.AppIcons;
import net.sf.nmedit.nomad.main.ui.HeaderLabel;
import net.sf.nmedit.nomad.main.ui.LimitedText;
import net.sf.nmedit.nomad.main.ui.fix.StripeEnabledListCellRenderer;
import net.sf.nmedit.nomad.main.ui.fix.TreeStripes;

public class PatchLibSidebar extends JPanel implements Sidebar, SidebarListener
{

    public ImageIcon getIcon()
    {
        return AppIcons.IC_FILE_FOLDER;
    }

    public String getDescription()
    {
        return "Patch file browser";
    }

    public JComponent createView()
    {
        return this;
    }

    public void disposeView()
    {
        // nothing to do
    }

    public void sidebarActivated( SidebarEvent e )
    {
        // nothing to do
    }

    public void sidebarDeactivated( SidebarEvent e )
    {
        setPreferredSize(getSize());
    }
    
    private Nomad nomad;
    private SidebarControl sbcontrol;
    private File directory;
    private File[] files ;
    private JList listView;
    private  JTextField tfFilter;

    public final static String KEY_CURRENT_DIRECTORY = "custom.patch.directory.default";
    
    public static String getDefaultPatchDirectory()
    {
        String value = Application.getProperty(KEY_CURRENT_DIRECTORY);
        return value == null ? "data/patch" : value;
    }

    public static File getDefaultPatchDirectoryFile()
    {
        return (new File(getDefaultPatchDirectory())).getAbsoluteFile();
    }

    public static void setDefaultPatchDirectory( File currentDirectory )
    {
        if (currentDirectory.isDirectory())
            Application.setProperty(KEY_CURRENT_DIRECTORY, currentDirectory.getAbsolutePath());
    }
    
    public PatchLibSidebar( Nomad nomad, SidebarControl sbcontrol  )
    {
        this.nomad = nomad;
        if (nomad==null) throw new NullPointerException();
        
        this.sbcontrol = sbcontrol;
        sbcontrol.addSidebarListener(this);


        setPreferredSize(new Dimension(200,0));
        setLayout(new BorderLayout());
        listView = new JList();
        
        tfFilter = new JTextField(new LimitedText(20), null, 20);
        
        JPanel top = new JPanel(new BorderLayout());
        JLabel lblDescription = new HeaderLabel("Files");
        lblDescription.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        top.add(lblDescription, BorderLayout.NORTH);
        top.add(tfFilter, BorderLayout.CENTER);
        
        tfFilter.addKeyListener(new TextFieldListener());
        
        add(top, BorderLayout.NORTH);
        
        add(new JScrollPane(listView), BorderLayout.CENTER);

        listView.setModel(new PatchFileListModel());
        listView.addMouseListener(new ListMouseAction());
        listView.setCellRenderer(new PatchFileCellRenderer());
        
        setDirectory(getDefaultPatchDirectoryFile());
    }
    
    private class TextFieldListener implements KeyListener, Runnable
    {
        
        private boolean changed = false;
        
        public void keyTyped( KeyEvent e )
        { }

        public void keyPressed( KeyEvent e )
        { }

        public void keyReleased( KeyEvent e )
        {
            changed = true;
            SwingUtilities.invokeLater(this);
        }

        public void run()
        {
            if (!changed)
                return ;
            
            setFilterString(tfFilter.getText());
            
            changed = false;
        }
    }
    
    private String filterString = null;
    
    public void setFilterString(String filter)
    {
        if (filter!=null && filter.length()==0)
            filter = null;
        
        if (filterString==filter)
            return;
        
        if (filterString!=null && filterString.equals(filter))
            return;
        
        filterString = filter;

        updateFiles();
    }
    
    public File getDirectory()
    {
        return directory;
    }
    
    private void updateFiles()
    {
        directory = directory.getAbsoluteFile();
        files = directory.listFiles(new PatchFileFilter(filterString));
        Arrays.<File>sort(files, new FileComparator());
        fireModelChanged();
    }
    
    public void setDirectory(File dir)
    {
        if (dir==null) return;
        this.directory = dir;
        
        updateFiles();
    }
    
    protected void fireModelChanged() 
    {
        final ListDataListener[] list = 
            ((AbstractListModel)listView.getModel()).getListDataListeners();
        if (list.length==0) return;
        final ListDataEvent lde = new ListDataEvent(listView.getModel(), 
                ListDataEvent.CONTENTS_CHANGED, 0, listView.getModel().getSize());
        SwingUtilities.invokeLater(new Runnable(){public void run() 
        {
            for(ListDataListener ldl:list) ldl.contentsChanged(lde);
        }});
    }
    
    private class FileComparator implements Comparator<File>
    {

        public int compare( File a, File b )
        {
            if (a.isDirectory() && !(b.isDirectory()))
            {
                return -1;
            }
            else if ((!a.isDirectory()) && b.isDirectory())
            {
                return 1;
            }
            else
            {
                return a.toString().compareTo(b.toString());
            }
        }
        
    }
    
    private static class PatchFileFilter implements FileFilter
    {
        private String filter;
        
        public PatchFileFilter(String filter)
        {   this.filter = filter == null ? null : filter.toLowerCase(); }
        
        public boolean accept( File pathname )
        {
            if (pathname.isDirectory())
                return true;
            
            String name = pathname.getName().toLowerCase();
            if (!name.endsWith(".pch"))
                return false;
            
            if (filter == null)
                return true;
            
            return filter == null ? true : name.contains(filter);
        }
    }
    
    private class PatchFileListModel extends AbstractListModel 
    {

        public int getSize()
        {
            return files.length+1;
        }

        public Object getElementAt( int index )
        {
            return index==0 ? directory.getParentFile() : files[index-1];
        }
        
    }

    private class ListMouseAction extends MouseAdapter
    {     
        public void mouseClicked(MouseEvent e)
        {
            if(e.getClickCount() == 2)
            {
                int index = listView.locationToIndex(e.getPoint());
                if (index>=0 && index<listView.getModel().getSize())
                {
                    listView.ensureIndexIsVisible(index);
                    if (index == 0)
                    {
                        setDirectory(directory.getAbsoluteFile().getParentFile());
                    }
                    else
                    {
                        File file = files[index-1];
                        if (file.isDirectory())
                        {
                            setDirectory(file);
                        }
                        else
                        {
                            nomad.openPatchFiles(new File[]{file});
                        }
                    }
                }
            }
      }
    }
    
    private class PatchFileCellRenderer extends StripeEnabledListCellRenderer
    {

        public PatchFileCellRenderer( )
        {
            super( TreeStripes.AlternatingStripes.createSoftBlueStripes() );
        }

        private ImageIcon fileIcon = AppIcons.IC_DOCUMENT_NEW; // TODO use other constant
        private ImageIcon dirIcon = AppIcons.IC_FILE_FOLDER;
        
        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            if (value == null)
            {
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
            File file = (File) value;
            String label = file.getName();
            int sep = label.lastIndexOf('/');
            if (sep<0) sep = label.lastIndexOf('\\');
            if (sep>0) label = label.substring(sep);
            /*if (label.toLowerCase().endsWith(".pch"))
                label = label.substring(0, label.length()-4);*/
            
            super.getListCellRendererComponent(list, index == 0 ? ".." : label, index, isSelected, cellHasFocus);
            
            setIcon((index == 0 || file.isDirectory())?dirIcon:fileIcon);
            
            /*if (value !=null)
            return new PatchView((Patch)value, isSelected|cellHasFocus);
            else throw new NullPointerException();*/
            
            return this;
        }
        
    }

    public void setSize( int x )
    {
        Dimension d = new Dimension(x, getHeight());
        setPreferredSize(d);
        setSize(d);
    }
}
