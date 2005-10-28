package nomad.gui.model.component.builtin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.model.descriptive.DParameter;

public class DefaultTextDisplay extends AbstractUIControl {

	private DisplayPort displayPort = null;
	private JComponent container = null;
	private JLabel displayLabel = null;
	private final Color defaultFGColor = Color.WHITE;
	private final Color defaultBGColor = Color.decode("#392F7D");
	
	public DefaultTextDisplay() {
		super();
		displayPort = new DisplayPort();
		displayLabel = new JLabel("text");
		displayLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		displayLabel.setForeground(defaultFGColor);

		container = new JComponent() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(defaultBGColor);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		container.setLayout(new BorderLayout());
		container.setBorder(BorderFactory.createLoweredBevelBorder());
		container.add(BorderLayout.CENTER, displayLabel);
		container.setSize(50, 16);
		setComponent(container);
	}
	
	protected void registerPorts() {
		registerControlPort(displayPort);
	}

	public String getName() {
		return "TextDisplay";
	}
	
	private class DisplayPort extends AbstractControlPort {

		public DisplayPort() {
			super(DefaultTextDisplay.this);
		}

		private DParameter param = null;
		
		public DParameter getParameterInfoAdapter() {
			return param;
		}

		public void setParameterInfoAdapter(DParameter parameterInfo) {
			param = parameterInfo;
		}

		public int getParameterValue() {
			return 0;
		}

		public void setParameterValue(int value) {
			//
		}
		
	}
	
}
