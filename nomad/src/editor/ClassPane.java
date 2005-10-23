package editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import nomad.gui.BasicUI;
import nomad.gui.UIFactory;

public class ClassPane extends JPanel {

	private UIFactory factory = null;
	private JComboBox uiComboBox = null;
	private JButton createAction = null;
	private Vector createListener = new Vector();
	
	public ClassPane(UIFactory factory) {
		this.factory = factory;
		uiComboBox = new JComboBox();
		
		String[] names = factory.getUIClassNames();
		for (int i=0;i<names.length;i++) {
			uiComboBox.addItem(names[i]);
		}
		
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
	
	private void createRequested(BasicUI uiElement) {
		for (int i=0;i<createListener.size();i++)
			((CreateUIElementListener)createListener.get(i)).newUIElement(uiElement);
	}
	
	private class CreateActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String name = (String) uiComboBox.getSelectedItem();
			BasicUI uiElement = factory.newUIInstance(name);
			if (uiElement!=null)
				createRequested(uiElement);
		}
	}
}
