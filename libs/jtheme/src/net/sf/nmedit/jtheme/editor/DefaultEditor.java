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
package net.sf.nmedit.jtheme.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.cable.ScrollListener;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.editor.misc.BooleanTableCellEditor;
import net.sf.nmedit.jtheme.xml.DefaultPropertyStringHandler;

public class DefaultEditor
{

    private JFrame editorFrame;
    private ComponentView componentView;
    private PropertyTableModel propertyTableModel;
    private JTContext context;
    private JTable propertyTable;
    private JList componentList;
    private ComponentListModel componentListModel;
    private PropertyTableCellEditor propertyEditor;
    private DefaultPropertyStringHandler stringHandler;
    
    public DefaultEditor()
    {
        createComponents();
    }

    public JFrame getFrame()
    {
        return editorFrame;
    }
    
    public ComponentView getComponentView()
    {
        return componentView;
    }
    
    public JTComponent getView()
    {
        return (JTComponent) componentView.getView();
    }
    
    public void setView(JTComponent view)
    {
        componentView.setView(view);
        if (view == null)
        {
            setContext(null);
        }
        else
        {
            setContext(view.getContext());
        }
    }
    
    private void setContext(JTContext context)
    {
        if (this.context != context)
        {
            this.context = context;
            propertyTableModel.setContext(context);
            componentListModel.setContext(context);
        }
    }

    protected JTContext getContext()
    {
        return context;
    }
    
    public DefaultPropertyStringHandler getDefaultPropertyStringHandler()
    {
        return stringHandler;
    }
    
    private void createComponents()
    {
        stringHandler = new DefaultPropertyStringHandler();
        
        editorFrame = new JFrame("Editor");
        editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editorFrame.setBounds(10, 10, 540, 360 );
        
        componentView = new ComponentView();
        
        propertyTableModel = new PropertyTableModel();
        
        propertyTable = new JTable(propertyTableModel);
        (new PropertyTableCellRenderer()).install(propertyTable);
        
        propertyEditor = new PropertyTableCellEditor(this);
        configurePropertyEditor(propertyEditor);
        propertyTable.setDefaultEditor(Object.class, propertyEditor);
        componentListModel = new ComponentListModel();
        componentList = new JComponentList(componentListModel);
        componentList.setCellRenderer(new ComponentListCellRenderer());
        
        install(editorFrame.getContentPane());
        
        createMenu();
    }
    
    protected class JComponentList extends JList
    {
        public JComponentList(ListModel model)
        {
            super(model);
        }
        
        public String getToolTipText( MouseEvent e )
        {
            int row = locationToIndex( e.getPoint() );
            Object o = getModel().getElementAt(row);
            
            if (o != null && o instanceof Class)
            {
                Class<?> clazz = (Class) o;
                
                return clazz.getName();
            }
            
            return super.getToolTipText(e);
        }
    }
    
    public PropertyTableCellEditor getPropertyEditor()
    {
        return propertyEditor;
    }

    private void configurePropertyEditor(PropertyTableCellEditor editor)
    {
        BooleanTableCellEditor btce = new BooleanTableCellEditor();

        editor.setEditor(Boolean.class, btce);
        editor.setEditor(Boolean.TYPE, btce);
    }

    private void install(Container contentPane)
    {
        contentPane.setLayout(new BorderLayout());
        
        //editedObject.setFont(new Font("monospace", Font.BOLD|Font.ITALIC,11));
        

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setLeftComponent(new JScrollPane(componentView));
        split.setResizeWeight(0.75);
        contentPane.add(split, BorderLayout.CENTER);
        
        JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split2.setResizeWeight(0.6);
        split2.setLeftComponent(new JScrollPane(propertyTable));
        split2.setRightComponent(new JScrollPane(componentList));

        split.setRightComponent(split2);
        
        SelectionListener sl = new SelectionListener(componentView);
        componentView.addSelectionChangeListener(sl);
        sl.setPropertyTableModel(propertyTableModel);

        new ComponentDragDrop(componentList, componentView);

    }

    
    private void createMenu()
    {
        JMenuBar bar = new JMenuBar();
        bar.add(new JMenu("File"));
        bar.add(new JMenu("Module"))
        .add(new JMenuItem("Show"))
        .addActionListener(new ShowModuleAction(this));
        editorFrame.setJMenuBar(bar);
    }

    private static class ShowModuleAction implements ActionListener,
    Runnable
    {

        private DefaultEditor editor;

        public ShowModuleAction(DefaultEditor editor)
        {
            this.editor = editor;
        }

        public void actionPerformed(ActionEvent e)
        {
            SwingUtilities.invokeLater(this);
        }

        public void run()
        {
            ComponentView cview = editor.getComponentView();
            
            JComponent view = cview.getView();
            if (view == null)
                return ;
            
            ModuleCloneTool cloneTool = new ModuleCloneTool((JTModule)view);
            JTModule clone;
            try
            {
                clone = cloneTool.getClone();
            }
            catch (JTException e)
            {
                e.printStackTrace();
                return ;
            }

            JTContext context = editor.getContext();
            Container moduleContainer = null;
            if (context != null)
            {
                try
                {
                    JTModuleContainer container = context.createModuleContainer();
                    moduleContainer = container;
                }
                catch (JTException e)
                {
                    // ignore
                }
            }
            
            if (moduleContainer == null)
            {
                moduleContainer = new JPanel();
                moduleContainer.setLayout(new BorderLayout());
                moduleContainer.setBackground(Color.DARK_GRAY);
            }
            
            JFrame showFrame = new JFrame("Module: "+view);
            showFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            showFrame.getContentPane().setLayout(new BorderLayout());
            showFrame.getContentPane().setBackground(Color.darkGray);
            
            JScrollPane scroll = new JScrollPane(moduleContainer);
            
            if (moduleContainer instanceof JTModuleContainer)
            {
                new ScrollListener(scroll, ((JTModuleContainer)moduleContainer).getCableManager());
            }
            
            showFrame.getContentPane().add(scroll, BorderLayout.CENTER);
            showFrame.setBounds(clone.getBounds());

            moduleContainer.add(clone);
            clone.setStaticLayerBackingStore(clone.renderStaticLayerImage());
            
            ((JComponent)(showFrame.getContentPane())).setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
            showFrame.setVisible(true);

        }
    }
    
    private static class SelectionListener implements ChangeListener
    {

        private ComponentView view;
        private PropertyTableModel propertyTable;
//        private JTable table;

        public SelectionListener(ComponentView view)
        {
            this.view = view;
        }
        
        public void setPropertyTableModel(PropertyTableModel model)
        {
            this.propertyTable = model;
        }
        
        public void stateChanged(ChangeEvent e)
        {
            Component[] selection = view.getSelection();
            
            Component selected = null;

            if (selection.length == 1)
                selected = selection[0];

            if (propertyTable != null)
                propertyTable.setEditedObject(selected);
        }

    }

    public PropertyTableModel getPropertyTableModel()
    {
        return propertyTableModel;
    }
    
}

