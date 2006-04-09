package org.nomad.util.view;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

public interface DocumentManager {

	public JComponent getDocumentContainer();
	public int getDocumentCount();
	public String getTitleAt(int index);
	public void setTitleAt(int index, String title);
	public void addDocument(String title, JComponent component);
	public JComponent getDocumentAt(int index);
	public int getDocumentIndex(JComponent component);
	public void setSelectedDocument(int index);
	public void setSelectedDocument(JComponent component);
	public void removeDocumentAt(int index);
	public void removeDocument(JComponent component);
	public int getSelectedDocumentIndex();
	public JComponent getSelectedDocument();

	public void setEnabled(boolean enable);
	public boolean isEnabled();
	
	public void addDocumentSelectionListener(ChangeListener l);
	public void removeDocumentSelectionListener(ChangeListener l);
	
}
