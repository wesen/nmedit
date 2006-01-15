// http://www.javaworld.com/javaworld/jw-03-1999/jw-03-dragndrop-p1.html

package org.nomad.patch;

import org.nomad.theme.component.NomadConnector;
import org.nomad.xml.dom.module.DConnector;


public class Connector {

	private DConnector dConnector = null;
	private NomadConnector connectorUI = null;
	
	private int conX, conY;

	public Connector(DConnector dConnector, int newX, int newY) {
		this.dConnector = dConnector;
		conX = newX;
		conY = newY;
	}

	public String getConnectionName() {
		return "unknown name";
	}

	public int getConnectionType() {
		return 0;
	}

	public int getX() {
		return conX;
	}
	
	public int getY() {
		return conY;
	}
	
	/**
	 * Returns the associated DConnector object
	 * @return the associated DConnector object
	 */
	public DConnector getInfo() {
		return dConnector;
	}

	/**
	 * Sets ui component for this connector.
	 * @param connectorUI the connector ui
	 */
	public void setUI(NomadConnector connectorUI) {
		if (this.connectorUI!=null) {
			// uninstall from previous ui component
		}
		this.connectorUI = connectorUI;
		if (this.connectorUI!=null) {
			// install to new ui component
		}
	}
	
}
