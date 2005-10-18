import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.model.descriptive.DConnector;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;
import nomad.model.descriptive.DToolbarGroup;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.model.descriptive.substitution.XMLSubstitutionReader;

public class ModuleFormTest extends JFrame implements ItemListener {

	JComboBox cbModules;
	
	public ModuleFormTest(ModuleDescriptions mic) {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(640, 380);
		
		Container content = this.getContentPane();
		
		content.add(BorderLayout.NORTH, cbModules = new JComboBox());
		cbModules.addItemListener(this);

		Collection modules = mic.getModules();
		for (Iterator i = modules.iterator(); i.hasNext();) {
			DModule module = (DModule) i.next();
			if (module.getParameterCount()>0)
				addModule(module);
		}
		
		selectModule("control sequencer");
	}
	
	private void selectModule(String name) {
		name=name.toLowerCase();
		for (int i=0;i<cbModules.getItemCount();i++) {
			String cmp = cbModules.getItemAt(i).toString();
			if (cmp.toLowerCase().endsWith(name)) {
				cbModules.setSelectedIndex(i);
				return;
			}
		}
	}

	private void addModule(DModule module) {
		DToolbarGroup group = module.getParent().getParent();
		JPanel modPane = new TextPanel(group.getName()+":"+module.getName());
		cbModules.addItem(modPane);

		//modPane.setBackground(Color.DARK_GRAY);
		modPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		modPane.setLayout(new BoxLayout(modPane, BoxLayout.Y_AXIS));
		JLabel lblName = new JLabel(module.getName());
		lblName.setForeground(Color.RED);
		modPane.add(lblName);
		modPane.setAlignmentX(10);
		
		for (int i=0;i<module.getParameterCount();i++) {
			DParameter info = module.getParameter(i);
			ParamPane pp = new ParamPane(info, info.getNumStates());
			modPane.add(pp);
		}
		
		for (int i=0;i<module.getConnectorCount();i++) {
			DConnector ci = module.getConnector(i);
			JLabel lbl ;
			modPane.add(lbl = new JLabel(ci.getConnectionTypeName()+","+ci.getSignalName()));
			
			switch (ci.getSignal()) {
				case DConnector.SIGNAL_AUDIO:lbl.setForeground(Color.RED); break;
				case DConnector.SIGNAL_CONTROL:lbl.setForeground(Color.magenta); break;
				case DConnector.SIGNAL_LOGIC:lbl.setForeground(Color.ORANGE); break;
				case DConnector.SIGNAL_SLAVE:lbl.setForeground(Color.DARK_GRAY); break;
			}
			
		}
	}

	public static void main(String[] args) {
		XMLSubstitutionReader subs = new XMLSubstitutionReader("./src/data/xml/substitutions.xml");
		ModuleDescriptions mic = new ModuleDescriptions("./src/data/xml/modules.xml", subs);
		ModuleFormTest test = new ModuleFormTest(mic);
		test.pack();
		test.show();
	}
	
	JPanel current=null;

	public void itemStateChanged(ItemEvent event) {
		JPanel pane = (JPanel) event.getItem();
		
		if (current!=null)
			this.remove(current);
		this.getContentPane().add(BorderLayout.CENTER, pane);
		current=pane;
		pack();
	}

}

class TextPanel extends JPanel {
	String text;
	public TextPanel(String text) {
		this.text=text;
	}
	public String toString() {
		return text;
	}
}

class ParamSlider extends JSlider {
	
	private DParameter info;

	public ParamSlider(DParameter info) {
		//this.setBackground(Color.DARK_GRAY);
		this.info = info;
		this.setOrientation(JSlider.HORIZONTAL);
		this.setMinimum(0);
		this.setMaximum(info.getNumStates()-1);
		this.setValue(info.getDefaultValue());
		this.setPreferredSize(new Dimension(60, this.getHeight()));
	}

	public String toString() {
		return toString(this.getValue());
	}
	public String toString(int value) {
		return info.getName()+": "+info.getFormattedValue(value);
	}
	
}

class ParamPane extends JPanel implements ChangeListener {
	
	private ParamSlider slider;
	private JLabel lbl;
	
	public ParamPane(DParameter info, int states) {
		//this.setBackground(Color.DARK_GRAY);
		this.setLayout(new GridLayout(1, 2));
		this.add((lbl=new JLabel(info.getName()+":")));
		this.add((slider=new ParamSlider(info)));
		slider.addChangeListener(this);
		stateChanged(null);
	}

	public void stateChanged(ChangeEvent arg0) {
		lbl.setText(slider.toString(slider.getValue()));
	}
}

