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
 * Created on Jan 10, 2006
 */
package net.sf.nmedit.nomad.editor.views.classes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.component.NomadComponent;


public class NomadClassesView extends JPanel {
	
	private ArrayList<ComponentClassListItem> 
		listItemList = new ArrayList<ComponentClassListItem>();
	

	private JList classPreviewList = new JList();
	private JScrollPane scPane = new JScrollPane(classPreviewList);
	private ClassListModel listModel = new ClassListModel();
	// private ArrayList classList = new ArrayList();
	private ClassListCellRenderer listCellRenderer = new ClassListCellRenderer();
	
	public static final DataFlavor NomadComponentClassFlavor = new DataFlavor(NomadComponent.class, "NomadComponent DataFlavor");

	public NomadClassesView() {
		super();
		setLayout(new BorderLayout());
		add(scPane, BorderLayout.CENTER);
		
		classPreviewList.setModel(listModel);
		classPreviewList.setCellRenderer(listCellRenderer);
		
		classPreviewList.setDragEnabled(true);
		classPreviewList.setTransferHandler(new NomadComponentClassTransferHandler());
	}
	
	private class NomadComponentClassTransferHandler extends TransferHandler {    
		private JList source = null;

		public NomadComponentClassTransferHandler() { }

	    protected Transferable createTransferable(JComponent c) {
	        if (c instanceof JList) {
	            source = (JList)c;
	            if (source.isSelectionEmpty()) return null;
	            Object selection = source.getSelectedValue();
	            return new NomadComponentClassTransferable(((ComponentClassListItem) selection).getComponentClass());
	        }
	        return null;
	    }

	    public int getSourceActions(JComponent c) {
	        return COPY;//COPY_OR_MOVE;
	    }

	    public class NomadComponentClassTransferable implements Transferable {
	        Class data;
	        public NomadComponentClassTransferable(Class theClass) {
	        	data = theClass;
	        }

	        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
	            if (!isDataFlavorSupported(flavor)) {
	                throw new UnsupportedFlavorException(flavor);
	            }
	            return data;
	        }

	        public DataFlavor[] getTransferDataFlavors() {
	            return new DataFlavor[] { NomadComponentClassFlavor };
	        }

	        public boolean isDataFlavorSupported(DataFlavor flavor) {
	            if (NomadComponentClassFlavor.equals(flavor)) {
	                return true;
	            }
	            return false;
	        }
	    }
		
	}
	
	public void registerNomadComponentClass(Class nomadComponentClass) {
		if (!NomadComponent.class.isAssignableFrom(nomadComponentClass))
			throw new IllegalArgumentException("Class must be a delegate of NomadComponent");
		
		NomadComponent nc;
		try {
			nc = (NomadComponent) nomadComponentClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		
		ComponentClassListItem listItem = new ComponentClassListItem(nc);
		listItemList.add(listItem);
		listModel.fireContentsChanged(listItem, listItemList.size()-1);
	}
	
	protected ComponentClassListItem findItemByClass(Class componentClass) {
		for (Iterator i=listItemList.iterator();i.hasNext();) {
			ComponentClassListItem candidate = (ComponentClassListItem)i.next();
			if (candidate.getComponentClass().equals(componentClass))
				return candidate;
		}
		return null;
	}
	
	public void unregisterNomadComponentClass(Class nomadComponentClass) {
		ComponentClassListItem listItem = findItemByClass(nomadComponentClass);
		if (listItem!=null) {
			listItemList.remove(listItem);
			// does not work: listModel.notifyElementsChanged(null, index);
		}
	}

	private class ClassListModel extends AbstractListModel {

		public int getSize() { return listItemList.size(); }
		
		public void fireContentsChanged(Object data, int index) {
			fireContentsChanged(data, index, index);
		}
		
		public Object getElementAt(int index) {
			return listItemList.get(index);
		}
		
	}
	
	private class ClassListCellRenderer extends DefaultListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			ComponentClassListItem listItem = (ComponentClassListItem) value;
			listItem.setItemHasFocus(cellHasFocus);
			listItem.setItemIsSelected(isSelected);
			return listItem;
		}
		
	}

//	private UIFactory factory = null;
	
	/**
	 * TODO uninstall factory + update table
	 * @param theUIFactory
	 */
	public void setFactory(UIFactory theUIFactory) {
		
		if (theUIFactory==null) return;
		
		listItemList.clear();
		//this.factory = theUIFactory;		
		Class[] classList = theUIFactory.getInstalledClasses();
		for (int i=0;i<classList.length;i++)
			registerNomadComponentClass(classList[i]);
	}
	
}
