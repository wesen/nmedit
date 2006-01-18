package org.nomad.util.misc;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.nomad.xml.dom.module.DParameter;


public class ParameterToolTip extends PermanentToolTip {

	private DParameter info = null;

	public ParameterToolTip(JComponent component, DParameter info) {
		super(component);
		this.info = info;

		JLabel icon = new JLabel(new ImageIcon(info.getParent().getIcon()));
		addOneLine(icon, createValueLabel(info.getName()));
		/*
		
		JLabel lblModule = addProperty("Module:", " - "+info.getParent().getName());
		lblModule.setIcon(new ImageIcon(info.getParent().getIcon()));
		addProperty("Parameter:", info.getName());
		*/
	}

	
	public ParameterToolTip(JComponent component, DParameter info, int value) {
		this(component, info);
		addProperty("State:",info.getFormattedValue(value));
	}

	public DParameter getInfo() {
		return info;
	}
	
}
