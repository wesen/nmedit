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
package net.sf.nmedit.jsynth.nomad.forms;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.synthService.NewSynthService;

public class NomadSynthDeviceDialogHandler extends JPanel
  implements ActionListener
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -7101533839902727059L;

    public static final String UNKNOWN_VENDOR = "unknown";
    
    private JTree deviceTree;
    private JPanel btnPane;
    private JButton btnCreate;
    private JButton btnCancel;

    public NomadSynthDeviceDialogHandler()
    {
        setLayout(new BorderLayout());
        deviceTree = new JTree(createModel());
        
        deviceTree.setRootVisible(false);
        
        btnPane = new JPanel();
        add(deviceTree, BorderLayout.CENTER);
        add(btnPane, BorderLayout.SOUTH);
        
        
        btnCreate = new JButton("Create");
        btnCancel = new JButton("Cancel");

        btnPane.setLayout(new BoxLayout(btnPane, BoxLayout.X_AXIS));
        btnPane.add(Box.createHorizontalGlue());
        btnPane.add(btnCreate);
        btnPane.add(Box.createHorizontalStrut(6));
        btnPane.add(btnCancel);
    }
    
    private TreeModel createModel()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        populate(root);
        return new DefaultTreeModel(root);
    }
    
    private void populate(DefaultMutableTreeNode root)
    {
        List<NewSynthService> list =
            new ArrayList<NewSynthService>();

        {
            Iterator<NewSynthService> i =
                ServiceRegistry.getServices(NewSynthService.class);
    
            while (i.hasNext())
                list.add(i.next());
        }
        
        Collections.sort(list, new SortByVendor());
        
        int index = 0;
        
        while (index<list.size())
        {
            NewSynthService s = list.get(index);
            
            String vendorName = vendor(s);
            int end = index;
            while (++end<list.size() && vendorName.equals(vendor(list.get(end))));
            
            DefaultMutableTreeNode vendorNode
                = new DefaultMutableTreeNode(vendorName);
            
            root.add(vendorNode);
            
            for (int i=index;i<end;i++)
                vendorNode.add(new SynthNode(list.get(i)));
            
            index = end;
        }
    }
    
    private static class SynthNode extends DefaultMutableTreeNode
    {
        /**
         * 
         */
        private static final long serialVersionUID = 8011062922142830497L;

        public SynthNode(NewSynthService s)
        {
            super(s);
        }
        
        public NewSynthService getService()
        {
            return (NewSynthService) getUserObject();
        }
        
        public String toString()
        {
            return getService()
                .getSynthName();
        }
    }
    
    private static String vendor(NewSynthService s)
    {
        String name = s.getSynthVendor();
        if (name == null)
            name = UNKNOWN_VENDOR;
        return name;
    }

    private class SortByVendor implements Comparator<NewSynthService>
    {
        public int compare(NewSynthService o1, NewSynthService o2)
        {
            return vendor(o1).compareTo(vendor(o2));
        }
    }

    public static void main(String[] args)
    {
        NomadSynthDeviceDialogHandler form
            = new NomadSynthDeviceDialogHandler();
        
        JFrame f = new JFrame("Devices");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(10, 10, 400, 300);

        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(form, BorderLayout.CENTER);
        
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnCreate)
        {
            createSelected();
        }
    }

    private void createSelected()
    {
        TreePath selectedPath = deviceTree.getSelectionPath();
        
        if (selectedPath == null)
            return;
        
        //DefaultTreeModel model = ((DefaultTreeModel)deviceTree.getModel());
        TreeNode node = (TreeNode) selectedPath.getLastPathComponent();
        
        if (node == null)
            return ;
        
        SynthNode snode = (SynthNode) node;
        
        snode.getService().newSynth();
    }

}
