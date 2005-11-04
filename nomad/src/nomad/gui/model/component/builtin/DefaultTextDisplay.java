package nomad.gui.model.component.builtin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

import nomad.graphics.BackgroundRenderer;
import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.model.descriptive.DParameter;

public class DefaultTextDisplay extends AbstractUIControl {

	private DisplayPort displayPort = null;
	private JComponent container = null;
	private JLabel displayLabel = null;
	private final Color defaultFGColor = Color.WHITE;
	private final Color defaultBGColor = Color.decode("#392F7D");
	private BackgroundRenderer renderer = null;
	
	public DefaultTextDisplay(UIFactory factory) {
		super(factory);
		displayPort = new DisplayPort();
		displayLabel = new JLabel("text");
		displayLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		displayLabel.setForeground(defaultFGColor);

		container = new JComponent() {
			protected void paintComponent(Graphics g) {
				//super.paintComponent(g);
				if (renderer==null) {
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
				} else {
					renderer.drawTo(this, getSize(), g);
				}
			}
		};
		container.setLayout(new BorderLayout());
		container.setBorder(BorderFactory.createLoweredBevelBorder());
		container.add(displayLabel, BorderLayout.CENTER);
		container.setBackground(defaultBGColor);
		container.setSize(50, 16);

		/*container.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent event) {
				if (renderer!=null)
				{
					renderer.render(container, container.getSize());
					container.repaint();
				}
			}
		});*/
		
		setComponent(container);
	}
	
	public JLabel getLabel() {
		return displayLabel;
	}
	
	public JComponent getContainer() {
		return container;
	}
	
	public void setBackgroundRenderer(BackgroundRenderer renderer) {
		this.renderer = renderer;
		container.repaint();
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
			if (param!=parameterInfo) {
				param = parameterInfo;
			}
		}

		public int getParameterValue() {
			return 0;
		}

		public void setParameterValue(int value) {
			//
		}
		
	}
	
}
