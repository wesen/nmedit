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
 * Created on Jun 23, 2006
 */
package net.sf.nmedit.nomad.main.ui.sidebar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DragGestureEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.jmisc.math.Math2;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DGroup;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DSection;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.ModuleDescriptions;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.resources.AppIcons;
import net.sf.nmedit.nomad.main.ui.HeaderLabel;
import net.sf.nmedit.nomad.main.ui.ModuleDragSource;
import net.sf.nmedit.nomad.main.ui.fix.StripeEnabledTreeCellRenderer;
import net.sf.nmedit.nomad.main.ui.fix.TreeStripes;
import net.sf.nmedit.nomad.main.ui.fix.TreeStripesContainer;
import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.util.document.Document;
import net.sf.nmedit.nomad.util.document.DocumentListener;

public class ModuleSidebar extends JPanel implements SwingConstants, Sidebar, SidebarListener, DocumentListener
{ 

    private Nomad nomad;
    private JTree tree;
    private JToolBar bar ;

    private ModulePreview prev = new ModulePreview();
    private SidebarControl sbcontrol;
    
    public ModuleSidebar(Nomad nomad, SidebarControl sbcontrol)
    {
        this.nomad = nomad;
        this.sbcontrol = sbcontrol;
        nomad.getDocumentContainer().addListener(this);
        sbcontrol.addSidebarListener(this);
        setLayout(new BorderLayout());
        
        bar = new JToolBar();
        bar.setFloatable(false);

        JToggleButton btnSelect = new JToggleButton(AppIcons.getImageIcon("draw-selection"));
        JToggleButton btnDraw = new JToggleButton(AppIcons.getImageIcon("draw-modules"));
        btnSelect.setSelected(true);
        btnSelect.setToolTipText("Select Module(s)");
        btnDraw.setToolTipText("Draw Module(s)");
        btnDraw.setEnabled(false);

        btnSelect.setBorderPainted(false);
        btnDraw.setBorderPainted(false);
        btnSelect.setBorder(null);
        btnDraw.setBorder(null);
        
        ButtonGroup group = new ButtonGroup();
        group.add(btnSelect);
        group.add(btnDraw);
        
        bar.add(btnSelect);
        bar.add(btnDraw);
        bar.add(Box.createHorizontalGlue());
        
        JButton jbExpand = new JButton(new ExpandAllAction());
        JButton jbCollapse = new JButton(new CollapseAllAction());

        jbExpand.setBorder(null);
        jbCollapse.setBorder(null);

        jbExpand.setFocusable(false);
        jbCollapse.setFocusable(false);
        
        jbExpand.setBorderPainted(false);
        jbCollapse.setBorderPainted(false);
        jbExpand.setPreferredSize(jbExpand.getMinimumSize());
        jbCollapse.setPreferredSize(jbCollapse.getMinimumSize());
        
        bar.add(jbExpand);
        bar.add(jbCollapse);
        
        add(build(), BorderLayout.CENTER);
        
        //setPreferredSize(getMinimumSize());
        //setPreferredSize(new Dimension(200,400));

        prev.setVerticalAutoresizeEnabled(false);
        prev.setHorizontalAutoresizeEnabled(false);
        prev.setMinimumSize(new Dimension(0, 80));
        prev.setPreferredSize(prev.getMinimumSize());
        prev.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        JPanel top = new JPanel();
        top.setBorder(null);
        top.setLayout(new BorderLayout());
        JLabel lblSelection = new HeaderLabel("Selection");
        lblSelection.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        top.add(lblSelection, BorderLayout.NORTH);
        top.add(prev, BorderLayout.CENTER);
        top.add(bar, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
    }
    
    private class ExpandAllAction extends AbstractAction
    {
        public ExpandAllAction()
        {
            putValue(SMALL_ICON, AppIcons.getImageIcon("eclipse-expandall"));
            putValue(NAME, null);
            putValue(SHORT_DESCRIPTION, "Expand All");
        }
        
        public void actionPerformed( ActionEvent e )
        {
            NomadUtilities.expandAll(tree);
        }
    }
    
    private class CollapseAllAction extends AbstractAction
    {
        public CollapseAllAction()
        {
            putValue(SMALL_ICON, AppIcons.getImageIcon("eclipse-collapseall"));
            putValue(NAME, null);
            putValue(SHORT_DESCRIPTION, "Collapse All");
        }
        
        public void actionPerformed( ActionEvent e )
        {
            NomadUtilities.collapseAll(tree);
        }
    }
    
    private class STreeCellRenderer extends StripeEnabledTreeCellRenderer implements TreeCellRenderer
    {

        public Component getTreeCellRendererComponent( 
                JTree tree, 
                Object value, 
                boolean selected, 
                boolean expanded, boolean leaf, int row, boolean hasFocus )
        {
            JLabel res = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            if (value instanceof TreeNode)
            {
                value = ((DefaultMutableTreeNode)value).getUserObject();
            }
            
            if (value instanceof DGroup)            
                setText(((DGroup)value).getName());
            else if (value instanceof DModule)
            {
                DModule spec = (DModule)value;
                
                String txt = spec.getName()
                +" ("
                +Math2.roundTo(spec.getCycles(), -2)
                +"%)";
                
                setText(txt);
                ImageIcon icon = new ImageIcon(spec.getIcon()); 
                setIcon(icon);
                setDisabledIcon(icon);
            } 
            
            return res;
        }
        
    }
    
    private TreeNode buildT()
    {
        
        DefaultMutableTreeNode n = new DefaultMutableTreeNode();

        ModuleDescriptions md = ModuleDescriptions.sharedInstance();
        
        for (int g=0;g<md.getGroupCount();g++)
        {
            DGroup dg = md.getGroup(g);
            
            DefaultMutableTreeNode ng = new DefaultMutableTreeNode(dg);
            
            n.add(ng);
            for (int s=0;s<dg.getSectionCount();s++)
            {
                DSection ds = dg.getSection(s);
                for (int m=0;m<ds.getModuleCount();m++)
                {
                    DModule dm = ds.getModule(m);
                    DefaultMutableTreeNode nm = new DefaultMutableTreeNode(dm);
                    
                    ng.add(nm);
                }
                
            }
        }
        
        return n;
    }
    
    private JComponent build()
    {
        tree = new JTree(buildT());
        tree.setEditable(false);
        tree.setEnabled(false);
        tree.setRootVisible(false);
        tree.setCellRenderer(new STreeCellRenderer());
        tree.setOpaque(true);
        tree.setBackground(null);
        //tree.setDragEnabled(true);
        tree.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

        tree.addMouseListener(new MouseAdapter(){
           public void mousePressed(MouseEvent e)
           {
               if (!SwingUtilities.isLeftMouseButton(e))
                   return;
               
               DModule sel = null;
               TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
               if (e.getY()<tree.getRowCount()*tree.getRowHeight())
                   tree.setSelectionPath(path); // select when cursor is not above label
               Object o = path.getLastPathComponent();
               if (o instanceof DefaultMutableTreeNode)
               {
                   o = ((DefaultMutableTreeNode)o).getUserObject();
                   if (o instanceof DModule) sel = (DModule) o;
               }

               if (sel==null)
               {
                   if (!tree.isExpanded(path)) tree.expandPath(path);
                   else tree.collapsePath(path);
                   prev.setPreview(null);
               }
               else
               {
                   prev.setPreview(sel);
               }
               revalidate();
           }
        });
        
        new ModuleDragSource(tree)
        {
            @Override
            public DModule locateModuleInfo( DragGestureEvent dge )
            {
                return getSelectedModule();
            }
            
        };

        return(new JScrollPane(new TreeStripesContainer(tree, TreeStripes.AlternatingStripes.createSoftBlueStripes())));
    }
    
    private DModule getSelectedModule()
    {
        Object o = tree.getLastSelectedPathComponent();
        
        if (o != null && o instanceof DefaultMutableTreeNode)
        {
            o = ((DefaultMutableTreeNode) o).getUserObject();
            if (o != null && o instanceof DModule)
                return (DModule) o;
        }
        return null;
    }
    /*
    private DModule getModuleAtCursor(int x, int y)
    {
        TreePath path = tree.getClosestPathForLocation(x, y);
        if (path == null) return null;
        
        Object o = path.getLastPathComponent();
        
        if (o != null && o instanceof DefaultMutableTreeNode)
        {
            o = ((DefaultMutableTreeNode) o).getUserObject();
            if (o != null && o instanceof DModule)
                return (DModule) o;
        }
        return null;
    }*/
   
    private ImageIcon icon = null;

    public ImageIcon getIcon()
    {
        return (icon == null) ? icon = AppIcons.getImageIcon("module-icon") : null;
    }

    public String getDescription()
    {
        return "Modules";
    }

    public JComponent createView()
    {
        return this;
    }

    public void disposeView()
    {
        // 
    }

    public void sidebarActivated( SidebarEvent e )
    {
        
    }

    public void sidebarDeactivated( SidebarEvent e )
    {
        setPreferredSize(getSize());
    }

    public void documentSelected( Document document )
    {
    }

    public void documentRemoved( Document document )
    {
        tree.setEnabled(nomad.getDocumentContainer().getDocumentCount()>0);   
    }

    public void documentAdded( Document document )
    {
        tree.setEnabled(true);   
    }

    public void setSize( int x )
    {
        Dimension d = new Dimension(x, getHeight());
        setPreferredSize(d);
        setSize(d);
    }
    

}
