package nomad.gui.model.component.builtin;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractConnectorUI;
import nomad.model.descriptive.DConnector;

public class DefaultConnectorUI extends AbstractConnectorUI {

	private DConnector connector = null;
	private ConnectorChangedListener ccl = new ConnectorChangedListener();
	private JLabel theLabel = null;
	
	public DefaultConnectorUI(UIFactory factory) {
		super(factory);
		theLabel = new JLabel();
		setComponent(theLabel);
		getConnectorProperty().addChangeListener(ccl);
	}
	
	protected void installSizeProperty(boolean install) {	
		// don't want to install the size property
	}
	
	
	public DConnector getConnectorInfoAdapter() {
		return connector;
	}

	public void setConnectorInfoAdapter(DConnector connector) {
		if (this.connector==connector)
			return ;
		this.connector = connector;
		connectorChanged();
	}
	
	public boolean isConnectorFree() {
		return true;
	}
	
	protected void updateLabel() {
		if (connector==null) {
			theLabel.setIcon(null);
			theLabel.setText("<<connector>>");
			theLabel.setSize(theLabel.getPreferredSize());
		} else {
			ImageIcon icon = new ImageIcon(connector.getIcon(isConnectorFree()));
			theLabel.setIcon(icon);
			theLabel.setText(null);
			theLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
			theLabel.setPreferredSize(theLabel.getSize());
			theLabel.setToolTipText(connector.getName()+","+connector.getConnectionTypeName()+","+
					connector.getSignalName());
		}
	}

	private class ConnectorChangedListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			// connector changed
			updateLabel();
		}
	}
	
}
