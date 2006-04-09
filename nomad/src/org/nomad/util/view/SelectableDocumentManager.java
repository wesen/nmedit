package org.nomad.util.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SelectableDocumentManager implements DocumentManager, ChangeListener {

	private DocumentManager theManager = null;
	private JComponent contentPane = null;
	private ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();
	
	public SelectableDocumentManager(JComponent contentPane) {
		this.contentPane = contentPane; 
		theManager = new TabbedPaneDocumentManager();
		theManager.addDocumentSelectionListener(this);
		contentPane.add(theManager.getDocumentContainer(), BorderLayout.CENTER);
	}
	
	public void setEnabled(boolean enable) {
		theManager.setEnabled(enable);
	}
	
	public boolean isEnabled() {
		return theManager.isEnabled();
	}
	
	public boolean usesTabbedDocumentManager() {
		return theManager instanceof TabbedPaneDocumentManager;
	}
	
	public boolean usesDesktopDocumentManager() {
		return theManager instanceof DesktopDocumentManager;
	}
	
	public void switchDocumentManager(boolean tabbedManager) {
		if (tabbedManager && !usesTabbedDocumentManager())
			transferTo(new TabbedPaneDocumentManager());
		else if (!tabbedManager && !usesDesktopDocumentManager())
			transferTo(new DesktopDocumentManager());
		contentPane.validate();
	}

	private void transferTo(DocumentManager newManager) {
		
		theManager.removeDocumentSelectionListener(this);
		
		int selectedIndex = theManager.getSelectedDocumentIndex();
		
		// remove old manager
		contentPane.remove(theManager.getDocumentContainer());
		
		// transfer documents
		while (theManager.getDocumentCount()>0) {
			String title = theManager.getTitleAt(0);
			JComponent component = theManager.getDocumentAt(0);
			theManager.removeDocument(component);
			newManager.addDocument(title, component);
		}

		newManager.setEnabled(theManager.isEnabled());
		
		// add new manager
		contentPane.add(newManager.getDocumentContainer(), BorderLayout.CENTER);
		theManager = newManager;
		newManager.getDocumentContainer().validate();
		if (selectedIndex>0)
			newManager.setSelectedDocument(selectedIndex);
		
		newManager.addDocumentSelectionListener(this);
	}

	/**
	 * dont use this
	 */
	public JComponent getDocumentContainer() {
		return theManager.getDocumentContainer();
	}

	public int getDocumentCount() {
		return theManager.getDocumentCount();
	}

	public String getTitleAt(int index) {
		return theManager.getTitleAt(index);
	}

	public void setTitleAt(int index, String title) {
		theManager.setTitleAt(index, title);
	}

	public void addDocument(String title, JComponent component) {
		theManager.addDocument(title, component);
	}

	public JComponent getDocumentAt(int index) {
		return theManager.getDocumentAt(index);
	}

	public int getDocumentIndex(JComponent component) {
		return theManager.getDocumentIndex(component);
	}

	public void setSelectedDocument(int index) {
		theManager.setSelectedDocument(index);
	}

	public void setSelectedDocument(JComponent component) {
		theManager.setSelectedDocument(component);
	}

	public void removeDocumentAt(int index) {
		theManager.removeDocumentAt(index);
	}

	public void removeDocument(JComponent component) {
		theManager.removeDocument(component);
	}

	public int getSelectedDocumentIndex() {
		return theManager.getSelectedDocumentIndex();
	}

	public JComponent getSelectedDocument() {
		return theManager.getSelectedDocument();
	}

	public void addDocumentSelectionListener(ChangeListener l) {
		if (!changeListenerList.contains(l))
			changeListenerList.add(l);
	}

	public void removeDocumentSelectionListener(ChangeListener l) {
		changeListenerList.remove(l);
	}

	public void stateChanged(ChangeEvent event) {
		ChangeEvent evt = new ChangeEvent(this);
		for (ChangeListener l : changeListenerList)
			l.stateChanged(evt);
	}

}
