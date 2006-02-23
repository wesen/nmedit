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
 * Created on Jan 20, 2006
 */
package org.nomad.editor.views.visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListDataListener;

import org.nomad.editor.views.classes.DashPane;
import org.nomad.theme.component.NomadLabel;
import org.nomad.util.graphics.ImageTracker;

public class ImageBrowserDialog extends JDialog {

	private JList imageList=null;
	private VisualEditor editor = null;
	private ArrayList<ImageItem> itemList = new ArrayList<ImageItem>();
	
	public ImageBrowserDialog(VisualEditor editor) throws HeadlessException {
		this.editor = editor;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		fillList( editor.getEnvironment().getImageTracker() );

		imageList = new JList();
		JScrollPane pane = new JScrollPane(imageList);
		getContentPane().add(pane);
		
		imageList.setModel(new ImageModel());
		imageList.setCellRenderer(new ImageItemRenderer());

		imageList.addMouseListener(new MouseAdapter(){

			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount()==2) {
					createImageLabel((ImageItem) imageList.getSelectedValue());
				}
			}});
	}
	
	public static void showDialog(VisualEditor editor) {
		ImageBrowserDialog dialog = new ImageBrowserDialog(editor);
		dialog.setTitle("Choose an image...");
		dialog.setSize(300,300);
		dialog.setVisible(true);
	}
	
	protected void fillList(ImageTracker itracker) {
		for (String key : itracker.getKeys()) {
			Image image = itracker.getImage(key);
			ImageItem item = new ImageItem(image, key);
			itemList.add(item);	
		}
	}

	protected void createImageLabel(ImageItem item) {
		createImageLabel(item.key);
	}
	
	protected void createImageLabel(String imageKey) {
		NomadLabel label = new NomadLabel();
		/**
		 *  do  this here, otherwise the new text value will be the default value
		 *  and not exported to the dom
		 */
		label.setEnvironment(editor.getEnvironment());
		label.setText("{@" + imageKey + "}");
		label.setLocation(10,10);
		label.setSize(label.getPreferredSize());
		editor.add(label);
		//editor.repaint();
	}

	private class ImageItem extends JPanel {
		
		private final Color defBG = UIManager.getColor("List.selectionBackground");;
		Image image ;
		String key ;
		
		public ImageItem(Image image, String key) {
			this.image = image;
			this.key = key;
			setBackground(Color.WHITE);
			JLabel imgLabel = new JLabel(new ImageIcon(image));
			JLabel keyLabel = new JLabel("key:"+key);
			
			setLayout(new GridLayout(3,1));
			imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			keyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(imgLabel);
			add(keyLabel);
			add(new DashPane());
		}

		public void setItemHasFocus(boolean cellHasFocus) {
		}

		public void setItemIsSelected(boolean isSelected) {
			if (isSelected)
				setBackground(defBG);
			else
				setBackground(Color.WHITE);
		}
	}

	private class ImageModel implements ListModel {

		public int getSize() {
			return itemList.size();
		}

		public Object getElementAt(int index) {
			return itemList.get(index);
		}

		public void addListDataListener(ListDataListener l) { }

		public void removeListDataListener(ListDataListener l) { }
		
	}
	
	private class ImageItemRenderer implements ListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			ImageItem item = (ImageItem) value;
			item.setItemHasFocus(cellHasFocus);
			item.setItemIsSelected(isSelected);
			return item;
			
		}
		
	}
	
}
