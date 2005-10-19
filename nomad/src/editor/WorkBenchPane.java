package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.model.descriptive.DConnector;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;
import nomad.model.descriptive.DToolbarGroup;
import nomad.patch.ModuleSection.ModulePixDimension;

public class WorkBenchPane extends JPanel {

	DModule module = null;
	private JPanel modulePane = null;
	
	public WorkBenchPane() {
		this.setBackground(Color.LIGHT_GRAY);//.DARK_GRAY);
		this.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
	}
	
	public void setModule(DModule module) {
		this.module=module;

		if (modulePane!=null) {
			this.remove(modulePane);
		}
		
		modulePane = new JPanel();
		modulePane.setForeground(Color.GRAY);
		modulePane.setSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				module.getHeight()*ModulePixDimension.PIXHEIGHT*2));
		modulePane.setPreferredSize(new Dimension(ModulePixDimension.PIXWIDTH, 
				module.getHeight()*ModulePixDimension.PIXHEIGHT*2));
		modulePane.setBorder(BorderFactory.createRaisedBevelBorder());
		
		addUIComponents();
		
		this.add(BorderLayout.CENTER, modulePane);
		this.updateUI();
	}

	void addUIComponents() {
		int pad=5;
		int line=0;
		int lineHeight=17;
		//modulePane.setLayout(new BoxLayout(modulePane, BoxLayout.Y_AXIS));
		modulePane.setLayout(null);
		JLabel label;
		label=new JLabel(new ImageIcon(module.getIcon()));
		label.setLocation(pad,pad);
		label.setSize(16,16);
		modulePane.add(label);
		label=new JLabel(module.getName());
		label.setFont(new Font("Dialog", Font.BOLD, 10));
		label.setSize(10*module.getName().length(),16);
		label.setLocation(pad+16+10,pad);
		modulePane.add(label);
		
		line++;
		
		for (int i=0;i<module.getParameterCount();i++) {
			DParameter info = module.getParameter(i);
			ParamPane pp = new ParamPane(info, info.getNumStates());
			pp.setSize(modulePane.getWidth()-2*pad,16);
			pp.setLocation(pad,pad+line*lineHeight);
			pp.validate();
			modulePane.add(pp);
			line++;
		}
		
		if (module.getConnectorCount()>0) {
			JPanel pp = new ConnectorRow(module);
			pp.setSize(modulePane.getWidth()-2*pad,16);
			pp.setLocation(pad,pad+line*lineHeight);
			pp.validate();
			modulePane.add(pp);
		}
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
		//this.setPreferredSize(new Dimension(60, this.getHeight()));
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
		this.setLayout(null);
		{
			lbl=new JLabel(info.getName()+":");
			lbl.setFont(new Font("Dialog", Font.PLAIN, 10));
			lbl.setLocation(0,0);
			lbl.setSize((lbl.getText().length()+5)*10,14);
			this.add(lbl);
		}
		{
			slider=new ParamSlider(info);
			slider.setLocation(110,0);
			slider.setSize(100, 16);
			this.add(slider);
		}
		slider.addChangeListener(this);
		stateChanged(null);
	}

	public void stateChanged(ChangeEvent arg0) {
		lbl.setText(slider.toString(slider.getValue()));
	}
}

class ConnectorRow extends JPanel {

	public ConnectorRow(DModule module) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		for (int i=0;i<module.getConnectorCount();i++) 
			add(new ConnectorPane(module.getConnector(i)));
	}
}

class ConnectorPane extends JPanel {
	private DConnector connector ;
	private int w;
	private int h;
	public ConnectorPane(DConnector connector) {
		this.connector = connector;
		Image img = connector.getIcon(true);
		w = img.getWidth(null);
		h = img.getHeight(null);
		this.setPreferredSize(new Dimension(w, h));
		this.setToolTipText(connector.getName()+","+connector.getConnectionTypeName()+","+
				connector.getSignalName());
	}
	
	public void paint(Graphics g) {
		g.drawImage(connector.getIcon(true),0,0,w,h,null);
	}
	
}
