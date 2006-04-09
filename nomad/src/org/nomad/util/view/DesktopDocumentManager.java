package org.nomad.util.view;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class DesktopDocumentManager extends JDesktopPane implements
		DocumentManager, InternalFrameListener {

	private ArrayList<JInternalFrame> frames = new ArrayList<JInternalFrame>();
	private ArrayList<JComponent> components = new ArrayList<JComponent>();
	private ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();
	
	private JInternalFrame getFrameAt(int index) {
		return frames.get(index);
	}
	
	public JComponent getDocumentContainer() {
		return this;
	}

	public int getDocumentCount() {
		return frames.size();
	}

	public String getTitleAt(int index) {
		return getFrameAt(index).getTitle();
	}

	public void setTitleAt(int index, String title) {
		getFrameAt(index).setTitle(title);
	}

	public JComponent getDocumentAt(int index) {
		return (JComponent)components.get(index);
	}

	public int getDocumentIndex(JComponent component) {
		return components.indexOf(component);
	}

	public void setSelectedDocument(int index) {
		try {
			getFrameAt(index).setSelected(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSelectedDocument(JComponent component) {
		setSelectedDocument(components.indexOf(component));
	}

	public void addDocument(String title, JComponent component) {
		JInternalFrame frame = new JInternalFrame(title, true, false, true, true);
		frame.add(component, BorderLayout.CENTER);
		frame.addInternalFrameListener(this);
		
		frames.add(frame);
		components.add(component);
		
		frame.setSize(400, 300);
		frame.setVisible(true);
		add(frame);
	}

	public void removeDocumentAt(int index) {
		JInternalFrame frame = getFrameAt(index);
		JComponent component = components.get(index);
		
		
		frame.removeInternalFrameListener(this);
		frames.remove(index);
		components.remove(index);
		frame.getContentPane().remove(component);
		remove(frame);
		updateUI();
	}

	public void removeDocument(JComponent component) {
		removeDocumentAt(getDocumentIndex(component));
	}

	public int getSelectedDocumentIndex() {
		return frames.indexOf(getSelectedFrame());
	}

	public JComponent getSelectedDocument() {
		int index = getSelectedDocumentIndex();
		if (index>=0)
			return getDocumentAt(index);
		else
			return null;
	}

	public void addDocumentSelectionListener(ChangeListener l) {
		if (!changeListenerList.contains(l))
			changeListenerList.add(l);
	}

	public void removeDocumentSelectionListener(ChangeListener l) {
		changeListenerList.remove(l);
	}

	public void internalFrameOpened(InternalFrameEvent event) { }

	public void internalFrameClosing(InternalFrameEvent event) { }

	public void internalFrameClosed(InternalFrameEvent event) { }

	public void internalFrameIconified(InternalFrameEvent event) { }

	public void internalFrameDeiconified(InternalFrameEvent event) { }

	public void internalFrameActivated(InternalFrameEvent event) {
		ChangeEvent evt = new ChangeEvent(this);
		for (ChangeListener l : changeListenerList)
			l.stateChanged(evt);
	}

	public void internalFrameDeactivated(InternalFrameEvent event) { }

}
