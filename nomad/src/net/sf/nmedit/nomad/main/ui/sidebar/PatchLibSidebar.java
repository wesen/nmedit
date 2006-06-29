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
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.io.FileSource;
import net.sf.nmedit.jpatch.io.PatchDecoder;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.resources.AppIcons;
import net.sf.nmedit.nomad.main.ui.DashBorder;
import net.sf.nmedit.nomad.util.NomadUtilities;

public class PatchLibSidebar extends JPanel implements Sidebar, SidebarListener
{
    
    private JList listView;
    private JToolBar bar;
    private File dir = new File("data/patch");
    private PatchFileListModel model ;
    private PatchImplementation pi = PatchImplementation.getImplementation("Clavia Nord Modular Patch", "3.03");
    private Patch[] patches = new Patch[0];
    private File[] files = new File[0];
    private Nomad nomad;
    private SidebarControl sbcontrol;

    public PatchLibSidebar( Nomad nomad, SidebarControl sbcontrol  )
    {
        this.nomad = nomad;
        this.sbcontrol = sbcontrol;
        sbcontrol.addSidebarListener(this);
        
        if (nomad==null) throw new NullPointerException();
        //setBorder(BorderFactory.createEtchedBorder());
        setPreferredSize(new Dimension(200,0));
        setLayout(new BorderLayout());
        listView = new JList();
        add(new JScrollPane(listView), BorderLayout.CENTER);
        bar = new JToolBar();
        bar.setFloatable(false);
        add(bar, BorderLayout.NORTH);

        updateView();
        
        bar.add(new JButton(AppIcons.REFRESH));
        
        listView.setModel(model = new PatchFileListModel());
        listView.addMouseListener(new ListMouseAction());
        listView.setCellRenderer(new PatchCellRenderer());
    }
    
    public void setDirectory(File dir)
    {
        if (!dir.exists()) throw new RuntimeException("directory does not exist");
        if (!dir.isDirectory()) throw new RuntimeException("not a directory: "+dir);
        this.dir  = dir;
        
        updateView();
    }

    private void updateView()
    {
        java.util.List<Patch> patchList = new ArrayList<Patch>();
        files = dir.listFiles();
        for (File file : files)
        {
            if (file.isFile())
            {
                try
                {
                PatchDecoder decoder = pi.createPatchDecoder(FileSource.class);
                FileSource fs = new FileSource(new FileReader(file));
                decoder.decode(fs);
                Patch p = (Patch)decoder.getPatch();
                String name = file.getName();
                
                int slash = name.lastIndexOf('/');
                if (slash==-1) slash = name.lastIndexOf('\\');
                if (slash!=-1) name = name.substring(slash+1);
                if (name.endsWith(".pch")) name = name.substring(0, name.length()-4);
                
                p.setName(name);
                patchList.add(p);
                } catch (Exception e)
                {
                    
                }
            }
        }
        patches = patchList.toArray(new Patch[patchList.size()]);
    }
    
    private class PatchFileListModel extends AbstractListModel
    {

        public int getSize()
        {
            return patches.length;
        }

        public Object getElementAt( int index )
        {
            return patches[index];
        }
        
    }
    
    private static Border border
    = BorderFactory.createCompoundBorder(
      DashBorder.create(false, false, false, true),
      BorderFactory.createEmptyBorder(2, 2, 2, 2)
    );
    
    private class PatchView extends JPanel
    {
        private boolean isSelected;
        

        public PatchView(Patch p, boolean isSelected)
        {
            this.isSelected = isSelected;
            setOpaque(true);
            setBorder(border);

            setBackground(isSelected ? UIManager.getColor("List.selectionBackground") : listView.getBackground());
            
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            add(label(p.getName(),true));
            String n = p.getNote();
            if (n!=null && n.length()>0)
            {
                n = NomadUtilities.minimalText(n, 30, isSelected ? 4 : 1);
                
                n = "<html>"+ n.replaceAll("\\n", "<br>")+"</html>";
                
                add(label(n,false));
            }
            
        }
        
        private JLabel label(String text, boolean heading)
        {
            JLabel label = new JLabel(text);
            label.setFont(new Font(heading?"SansSerif":"monospaced",heading ? Font.BOLD : Font.ITALIC, heading?11:9));
            label.setForeground(isSelected ? UIManager.getColor("List.selectionForeground") : 
                UIManager.getColor("List.foreground"));
            return label;
        }
        
    }
    
    private class ListMouseAction extends MouseAdapter
    {     
        public void mouseClicked(MouseEvent e)
        {
            if(e.getClickCount() == 2)
            {
                int index = listView.locationToIndex(e.getPoint());
                if (index>=0 && index<files.length)
                {
                    listView.ensureIndexIsVisible(index);
                    nomad.openPatchFiles(new File[]{files[index]});
                }
            }
      }
    }
    
    private class PatchCellRenderer implements ListCellRenderer
    {

        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            if (value !=null)
            return new PatchView((Patch)value, isSelected|cellHasFocus);
            else throw new NullPointerException();
        }
        
    }

    private ImageIcon icon = null;
    
    public ImageIcon getIcon()
    {
        return (icon == null) ? icon = AppIcons.getImageIcon("patches") : null;
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
    }

    public void sidebarActivated( SidebarEvent e )
    {
        
    }

    public void sidebarDeactivated( SidebarEvent e )
    {
        setPreferredSize(getSize());
    }

}
