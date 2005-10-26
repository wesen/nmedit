package editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractUIComponent;

public class ClassPane extends JPanel {

	private UIFactory factory = null;
	private JComboBox uiComboBox = null;
	private JButton createAction = null;
	private Vector createListener = new Vector();
	
	public ClassPane(UIFactory factory) {
		this.factory = factory;
		uiComboBox = new JComboBox();
		
		String[] names = factory.getUIClassNames();
		for (int i=0;i<names.length;i++) 
			uiComboBox.addItem(new ClassInfo(names[i]));
		
		createAction = new JButton("Create");
		createAction.addActionListener(new CreateActionListener());
		
		JPanel container = new JPanel(new  GridLayout(2,1));
		container.add(uiComboBox);
		container.add(createAction);
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, container);
	}
	
	public void addCreateUIElementListener(CreateUIElementListener listener) {
		if (!createListener.contains(listener))
			createListener.add(listener);
	}
	
	public void removeCreateUIElementListener(CreateUIElementListener listener) {
		if (createListener.contains(listener))
			createListener.remove(listener);
	}
	
	private void createRequested(AbstractUIComponent uiElement) {
		for (int i=0;i<createListener.size();i++)
			((CreateUIElementListener)createListener.get(i)).newUIElement(uiElement);
	}
	
	private class CreateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ClassInfo cinfo = (ClassInfo) uiComboBox.getSelectedItem();
			AbstractUIComponent uiElement = factory.newUIInstance(cinfo.className);
			if (uiElement!=null)
				createRequested(uiElement);
		}
	}
	
	private class ClassInfo {
		public String className;
		public String shortName;
		
		public ClassInfo(String className) {
			this.className = className;
			String[] splitted = className.split("\\.");
			shortName = splitted[splitted.length-1];
		}
		
		public String toString() {
			return shortName;
		}
		
	}
	
}
