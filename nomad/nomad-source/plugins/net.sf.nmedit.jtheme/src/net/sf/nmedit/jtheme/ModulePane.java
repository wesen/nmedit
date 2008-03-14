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
package net.sf.nmedit.jtheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.nmedit.jpatch.ImageSource;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.util.JThemeUtils;
import net.sf.nmedit.nomad.core.swing.ButtonBarBuilder;
import net.sf.nmedit.nomad.core.swing.explorer.ContainerNode;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.LeafNode;

public class ModulePane extends JPanel
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 6251114885486092481L;
    private static ModulePane instance;
    private ExplorerTree tree;
    private ModuleDescriptions modules;
    private ModulePreview preview;
    private JPanel options;
    private JCheckBox toggleMore;
    private JTextField tfFilter;
    private List<ContainerNode> categories = new ArrayList<ContainerNode>();
    List<ContainerNode> visibleCategories = new ArrayList<ContainerNode>();
    
    private ModulePane()
    {
        JPanel top = new JPanel(new BorderLayout());
        options = new JPanel();
        options.setVisible(false);
        options.setLayout(new BorderLayout());
        
        tree = new ExplorerTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	
        preview = new ModulePreview();
        
        Color c = preview.getBackground();
        if (c != null)
            preview.setBackground(c.darker());
        preview.setBorder(BorderFactory.createLoweredBevelBorder());
        
        Dimension previewSize = new Dimension(80, 60);
        preview.setMinimumSize(previewSize);
        preview.setPreferredSize(previewSize);

        ButtonBarBuilder btnBarBuilder = new ButtonBarBuilder();
        toggleMore = new JCheckBox("More");
        toggleMore.setSelected(options.isVisible());
        
        toggleMore.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                options.setVisible(toggleMore.isSelected());
                options.revalidate();
            }});
        
        btnBarBuilder.add(toggleMore);
        
        btnBarBuilder.addBox();
        btnBarBuilder.addSpace();
        btnBarBuilder.add(tree.createExpandAllAction());
        btnBarBuilder.addSpace();
        btnBarBuilder.add(tree.createCollapseAllAction());
        
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        top.add(btnBarBuilder.getContainer(), BorderLayout.CENTER);
        top.add(options, BorderLayout.SOUTH);

        tfFilter = new JTextField();
        (new TextFilterEventHandler()).install();
        options.add(preview, BorderLayout.CENTER);
        
        ButtonBarBuilder filterBar = new ButtonBarBuilder();
        filterBar.add(new JLabel("Filter"));
        filterBar.addSpace();
        filterBar.add(tfFilter);
        filterBar.addSpace();
        JButton btnClear = new JButton("Clear");
        btnClear.setToolTipText("clear");
        btnClear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                tfFilter.setText("");
            }});
        filterBar.add(btnClear);
        
        options.add(filterBar.getContainer(), BorderLayout.SOUTH);
        
        add(new JScrollPane(tree), BorderLayout.CENTER);
        
        (new TreeDnDHandler(tree, this)).install();
        
        TreeSelectionHandler selectionHandler = new TreeSelectionHandler();
        tree.addTreeSelectionListener(selectionHandler);
    }

    private class TextFilterEventHandler implements DocumentListener,
        Runnable
    {
        
        private String prevFilter = "";
        
        public void install()
        {
            tfFilter.getDocument().addDocumentListener(this);
        }

        public void changedUpdate(DocumentEvent e)
        {
            filterUpdated();
        }

        public void insertUpdate(DocumentEvent e)
        {
            filterUpdated();
        }

        public void removeUpdate(DocumentEvent e)
        {
            filterUpdated();
        }
        
        private void filterUpdated()
        {
            SwingUtilities.invokeLater(this);
        }

        public void run()
        {
            String t = tfFilter.getText();
            if (t == null) t = "";
            else t = t.trim().toLowerCase();
            
            if (prevFilter.equals(t)) return;
            prevFilter = t;
            filter();
        }

        private void filter()
        {
            for (ContainerNode n: visibleCategories)
                tree.getRoot().remove(n);
            visibleCategories.clear();
            
            for (ContainerNode csrc : categories)
            {
                ContainerNode filteredContainer = filter(csrc);
                if (filteredContainer != null)
                {
                    visibleCategories.add(filteredContainer);
                    tree.getRoot().add(filteredContainer);
                }
            }
            tree.fireRootChanged();
            tree.expandAll();
        }

        private ContainerNode filter(ContainerNode csrc)
        {
            ContainerNode cdst = null;
            if (matches(csrc.getTitle()))
                cdst = new ContainerNode(csrc.getParent(), csrc.getTitle());
            
            for (int i=0;i<csrc.getChildCount();i++)
            {
                ModuleDescriptorNode nsrc = 
                    (ModuleDescriptorNode) csrc.getChildAt(i);
                
                if (matches(nsrc.getSearchText()))
                {
                    if (cdst == null)
                        cdst = new ContainerNode(csrc.getParent(), csrc.getTitle());
                    
                    cdst.addChild(new ModuleDescriptorNode(
                            cdst, nsrc.getIcon(),
                            nsrc.getText(), 
                            nsrc.getDescriptor()));
                }
            }
            
            return cdst;
        }

        private boolean matches(String text)
        {
            if (prevFilter.length() == 0)
                return true;
            
            text = text.toLowerCase();
            
            if (text.contains(prevFilter))
                return true;
            else
                return false;
        }
        
    }
    
    public void setTheme(JTContext context)
    {
        preview.setUIContext(context);
    }
    
    public JTContext getTheme()
    {
        return preview.getUIContext();
    }
    
    private class TreeSelectionHandler implements TreeSelectionListener
    {

        public void valueChanged(TreeSelectionEvent e)
        {
            TreePath path = e.getNewLeadSelectionPath();
            if (path == null)
                return;
            Object last = path.getLastPathComponent();
            
            PModuleDescriptor descriptor = null;
            
            if (last instanceof ModuleDescriptorNode)
            {
                descriptor = ((ModuleDescriptorNode) last).getDescriptor();
            }
            
            setSelection(descriptor);
        }

        private void setSelection(PModuleDescriptor descriptor)
        {
            preview.setModule(descriptor);
        }
        
    }
    
    private static class TreeDnDHandler implements DragGestureListener
    {
        private ExplorerTree tree;
        private DragSource dragSource;
        private ModulePane pane;

        public TreeDnDHandler(ExplorerTree tree, ModulePane pane)
        {
            this.tree = tree;
            this.pane = pane;
        }

        public void install()
        {
            dragSource = DragSource.getDefaultDragSource();
            dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_COPY, this);
        }

        public void dragGestureRecognized(DragGestureEvent dge)
        {
            Point origin = dge.getDragOrigin();
            TreePath path = tree.getPathForLocation(origin.x, origin.y);
            if (path != null)
            {
                Object lpc = path.getLastPathComponent();
                if (lpc != null && lpc instanceof ModuleDescriptorNode)
                {
                    PModuleDescriptor descriptor =
                        ((ModuleDescriptorNode)lpc).getDescriptor();
                    
                    Transferable t = JThemeUtils.createTransferable(pane.getTheme(), descriptor);
                    
                    dge.startDrag(DragSource.DefaultCopyDrop, t);
                }
            }
        }

    }
    
    
    public static ModulePane getSharedInstance()
    {
        if (instance == null)
            instance = new ModulePane();
        return instance;
    }
    
    public void setModules(ModuleDescriptions modules)
    {
        if (this.modules != modules)
        {
            if (this.modules != null)
                uninstall(this.modules);
            this.modules = modules;
            preview.setModule(null);
            preview.setModules(modules);
            if (modules != null)
                install(modules);
        }
    }

    protected void uninstall(ModuleDescriptions modules)
    {
        tree.getRoot().removeAllChildren();
        tree.fireRootChanged();
    }

    protected void install(ModuleDescriptions modules)
    {
        visibleCategories.clear();
        categories.clear();
        Map<String, List<PModuleDescriptor>> categoryMap = new HashMap<String, List<PModuleDescriptor>>();
        buildCategories(categoryMap, modules);
        
        Comparator<PModuleDescriptor> order = new ModuleDescriptorOrder();
        List<String> categories = new ArrayList<String>();
        categories.addAll(categoryMap.keySet());
        Collections.sort(categories);
        
        TreeNode root = tree.getRoot();
        
        for (String cat: categories)
        {
            List<PModuleDescriptor> catList = categoryMap.get(cat);
            Collections.sort(catList, order);
            
            ContainerNode catNode = new ContainerNode(root, cat);
            tree.addRootNode(catNode);
            ModulePane.this.categories.add(catNode);
            visibleCategories.add(catNode);
            
            for (PModuleDescriptor m: catList)
            {
                ImageSource source = m.get16x16IconSource();
                Image img = modules.getImage(source);
                Icon icon = img != null ? new ImageIcon(img) : null;
                
                
             //   ImageSource.*/
                
                String text = m.getStringAttribute("fullname");
                double cycles = m.getDoubleAttribute("cycles", -1);
                if (cycles >=0)
                {
                    text += " ("+(Math.round(cycles*100d)/100d)+"%)";
                }

                LeafNode n = new ModuleDescriptorNode(catNode, icon, text, m);
                catNode.addChild(n);
            }   
        }
    }

    private void buildCategories(Map<String, List<PModuleDescriptor>> categoryMap, ModuleDescriptions modules)
    {
        for (PModuleDescriptor module : modules)
        {
            if (module.isInstanciable())
            {
                String cat = module.getCategory();
                List<PModuleDescriptor> catList = categoryMap.get(cat);
                if (catList == null)
                {
                    catList = new ArrayList<PModuleDescriptor>();
                    categoryMap.put(cat, catList);
                }
                catList.add(module);
            }
        }
    }
    
    private static class ModuleDescriptorOrder implements Comparator<PModuleDescriptor>
    {

        public int compare(PModuleDescriptor o1, PModuleDescriptor o2)
        {
            String n1 = o1.getName();
            String n2 = o2.getName();
            
            if (n1 == n2) return 0;
            if (n1 == null) return 1;
            if (n2 == null) return -1;
            
            return n1.compareTo(n2);
        }
        
    }
    
    private static class ModuleDescriptorNode extends LeafNode
    {

        private PModuleDescriptor descriptor;

        public ModuleDescriptorNode(TreeNode parent, Icon icon, String text, PModuleDescriptor m)
        {
            super(parent, icon, text);
            this.setToolTipText(m.getName());
            this.descriptor = m;
        }
        
        public String getSearchText()
        {
            return descriptor==null?getText():descriptor.getName() + ";" + descriptor.getStringAttribute("fullname");
        }

        public PModuleDescriptor getDescriptor()
        {
            return descriptor;
        }
        
    }

}
