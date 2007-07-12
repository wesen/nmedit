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
package net.sf.nmedit.nomad.core.forms;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.nomad.core.swing.explorer.ContainerNode;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;

public class PropertyDialogForm extends JPanel implements TreeSelectionListener
{

    /**
     * 
     */
    private static final long serialVersionUID = -1138963228471158691L;
    private ExplorerTree propertiesTree;
    private JPanel dialogPane ;
    protected JPanel buttonPane;
    protected JLabel titleLabel;
    protected JComponent editor;
    
    public PropertyDialogForm()
    {
        propertiesTree = new ExplorerTree();
        dialogPane = new JPanel();
        buttonPane = new JPanel();
        setLayout(new BorderLayout());
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(propertiesTree),
                dialogPane);
        split.setResizeWeight(0);
        split.setDividerLocation(160);
        
        JPanel titlePane = new JPanel();
        titleLabel = new JLabel();
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        titleLabel.setFont(new Font("sansserif", Font.BOLD, 12));
        titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.Y_AXIS));
        titlePane.add(titleLabel);
        titlePane.add(new JSeparator(JSeparator.HORIZONTAL));
        titlePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        
        dialogPane.setLayout(new BorderLayout());
        dialogPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        dialogPane.add(titlePane, BorderLayout.NORTH);

        add(split, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);
        
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        
        propertiesTree.addTreeSelectionListener(this);
    }
    
    public boolean setSelectedPath(String path)
    {
        TreeNode e = getEntry(path, false);
        if (e == null)
            return false;
        
        List<Object> c = new LinkedList<Object>();
        
        while (e != null)
        {
            c.add(0, e);
            e = e.getParent();
        }
        
        propertiesTree.setSelectionPath(new TreePath(c.toArray()));
        TreePath s = propertiesTree.getSelectionPath();
        return s != null && s.getLastPathComponent() == e;
    }
    
    public JPanel getButtonPane()
    {
        return buttonPane;
    }
    
    public JComponent getEditor()
    {
        return editor;
    }

    public void setEditor(JComponent c)
    {
        if (this.editor == c)
            return;
        if (this.editor != null)
            dialogPane.remove(this.editor);
        this.editor = c;
        if (this.editor != null)
            dialogPane.add(this.editor, BorderLayout.CENTER);
        dialogPane.revalidate();
        dialogPane.invalidate();
        dialogPane.repaint();
    }
    
    public void addButton(Action a)
    {
        buttonPane.add(new JButton(a));
    }
    
    public void addActionListener(ActionListener l)
    {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l)
    {
        listenerList.add(ActionListener.class, l);
    }
    
    protected void fireEntrySelectedEvent(String path)
    {
        ActionEvent actionEvent = null;
        
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                // Lazily create the event:
                if (actionEvent == null)
                    actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, path);
                ((ActionListener)listeners[i+1]).actionPerformed(actionEvent);
            }
        }
    }
    
    private Map<String, Entry> paths = new HashMap<String, Entry>();
    
    protected Entry getEntry(String path, boolean create)
    {
        Entry e = paths.get(path);
        if (e == null && create)
        {
            int slash = path.lastIndexOf('/');
            if (slash >= 0)
            {
                String parentPath = path.substring(0, slash);
                Entry parent =  getEntry(parentPath, true);
                e = new Entry(parent, path);
                parent.addChild(e);
                paths.put(path, e);
            }
            else
            {
                e = new Entry(propertiesTree.getRoot(), path);
                propertiesTree.getRoot().add(e);
                paths.put(path, e);
            }
        }
        return e;
    }

    public String getTitleAt(String path)
    {
        Entry e = getEntry(path, false);
        return e == null ? null : e.getTitle();
    }
    
    public void setTitleAt(String path, String title)
    {
        Entry e = getEntry(path, false);
        if (e != null)
            e.setTitle(title);
    }

    public Icon getIconAt(String path)
    {
        Entry e = getEntry(path, false);
        return e == null ? null : e.getIcon();
    }
    
    public void setIconAt(String path, Icon icon)
    {
        Entry e = getEntry(path, false);
        if (e != null)
            e.setIcon(icon);
    }

    public Entry addEntry(String path, String title)
    {
        Entry e = getEntry(path, true);
        e.setTitle(title);
        propertiesTree.fireNodeStructureChanged(e.getParent());
        return e;
    }

    public static void main(String[] args)
    {
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(10, 10, 400, 360);
        
        final PropertyDialogForm pdf = new PropertyDialogForm();
        
        pdf.addEntry("file/info", "Info");
        pdf.addEntry("file/info2", "Info2");
        pdf.addEntry("somewhere/info3", "Info3");
        pdf.addEntry("somewhere", "SOME");
        
        pdf.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                pdf.setEditor(new JLabel(e.getActionCommand()));
                
            }});
        
        Action c = new AbstractAction()
        {
            /**
             * 
             */
            private static final long serialVersionUID = -1632392266734922822L;

            public void actionPerformed(ActionEvent e)
            {
                f.setVisible(false);
                f.dispose();
            }  
        };
        
        c.putValue(Action.NAME, "Close");
        
        pdf.addButton(c);

        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(pdf, BorderLayout.CENTER);

        f.setVisible(true);
    }
    
    protected static class Entry extends ContainerNode
    {
        private String path;

        public Entry(TreeNode parent, String path)
        {
            super(parent, null);
            this.path = path;
            
            int slash = path.lastIndexOf('/');
            if (slash>=0)
                setTitle(path.substring(slash));
            else
                setTitle(path);
        }
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        Entry entry = null;
        TreePath path = e.getPath();
        if (path != null)
        {
            Object c = path.getLastPathComponent();
            if (c != null && c instanceof Entry)
                entry = (Entry) c;
        }
        
        selectionChanged(entry);
    }

    private void selectionChanged(Entry entry)
    {
        if (entry != null)
        {
            titleLabel.setText(entry.getTitle());
            setEditor(null);
            fireEntrySelectedEvent(entry.path);
        }
        else
        {
            setEditor(null);
            titleLabel.setText("");
        }
    }

    public void dispose()
    {
        
    }

}
