// http://www.javaworld.com/javaworld/jw-03-1999/jw-03-dragndrop-p1.html

package nomad.patch;

import nomad.model.descriptive.DConnector;

public class Connector {

	private DConnector dConnector = null;
	
	private int conX, conY;

	public Connector(DConnector dConnector, int newX, int newY) {
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
}
