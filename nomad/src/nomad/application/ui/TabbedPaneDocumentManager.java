package nomad.application.ui;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

public class TabbedPaneDocumentManager extends JTabbedPane implements DocumentManager {

	public JComponent getDocumentContainer() {
		return this;
	}

	public int getDocumentCount() {
		return getTabCount();
	}

	public void addDocument(String title, JComponent component) {
		addTab(title, component);
	}

	public JComponent getDocumentAt(int index) {
		return (JComponent) getComponentAt(index);
	}

	public int getDocumentIndex(JComponent component) {
		for (int i=0;i<getDocumentCount();i++)
			if (getDocumentAt(i)==component)
				return i;
		return -1;
	}

	public void setSelectedDocument(int index) {
		super.setSelectedIndex(index);
	}

	public void setSelectedDocument(JComponent component) {
		setSelectedDocument(getDocumentIndex(component));
	}

	public void removeDocumentAt(int index) {
		removeTabAt(index);
	}

	public void removeDocument(JComponent component) {
		removeDocumentAt(getDocumentIndex(component));
	}

	public int getSelectedDocumentIndex() {
		return getSelectedIndex();
	}

	public JComponent getSelectedDocument() {
		int index = getSelectedDocumentIndex();
		if (index>=0)
			return getDocumentAt(index);
		else
			return null;
	}

}
