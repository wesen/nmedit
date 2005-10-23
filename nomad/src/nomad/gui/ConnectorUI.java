package nomad.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.property.ConnectorProperty;
import nomad.misc.FontInfo;
import nomad.model.descriptive.DConnector;

public class ConnectorUI extends DisplayUI {

	private ConnectorProperty pConnector = new ConnectorProperty();
	private JLabel label = null;
	
	public ConnectorUI() {
		super();
		label = new JLabel();
		getProperties().putProperty("connector", pConnector);
		pConnector.addChangeListener(new ConnectorChangedHandler());
		this.setComponent(label);
	}

	public ConnectorProperty getConnectorProperty() {
		return pConnector;
	}

	private class ConnectorChangedHandler implements ChangeListener {

		public void stateChanged(ChangeEvent event) {
			ConnectorProperty p = getConnectorProperty();
			DConnector connector = p.getSelectedConnector();
			if (connector == null) {
				label.setIcon(null);
				label.setText("<<no connector assigned>>");
				label.setSize(FontInfo.getTextRect(label.getText(),label.getFont(),label));
				label.setToolTipText("No info Available");
			} else {
				label.setText(null);
				ImageIcon icon = new ImageIcon(connector.getIcon(true));
				label.setIcon(icon);
				label.setSize(icon.getIconWidth(), icon.getIconHeight());
				label.setToolTipText(connector.getName()+","+connector.getConnectionTypeName()+","+
						connector.getSignalName());
			}
		}
		
	}

	public String getName() {
		return "Connector";
	}
	
}
