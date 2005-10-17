// http://www.javaworld.com/javaworld/jw-03-1999/jw-03-dragndrop-p1.html

package nomad.patch;

import javax.swing.ImageIcon;

import nomad.gui.ConnectorGUI;
import nomad.gui.ModuleGUI;
import nomad.gui.helpers.ModuleGUIFactory;

public class Connection {

	public final static class ConnectionType {
	    public final static int AUDIO = 0;
	    public final static int CNTRL = 1;
	    public final static int LOGIC = 2;
	    public final static int SLAVE = 3;
	}

	private int index, type;
	private String name;
	private boolean input;
	private int conX, conY;
	private Module module;
	private ImageIcon icon;

    Cables cables = null;
    
    ConnectorGUI moduleConnection = null; 
	
	public Connection(Module newModule, boolean bInput, int newIndex, String newName, int newType, int newX, int newY) {
		index = newIndex;
		type = newType;
		input = bInput;
		name = newName;
		conX = newX;
		conY = newY;
		module = newModule;
		
		moduleConnection = new ConnectorGUI(name);
		moduleConnection.setLocation(conX, conY);
	}

// Getters

	public Module getModule() {
		return module;
	}

	public String getConnectionName() {
		return name;
	}

	public int getConnectionType() {
		return type;
	}

	public int getConnectionIndex() {
		return index;
	}

	public int getConnectionLocationX() {
		return conX;
	}
	
	public int getConnectionLocationY() {
		return conY;
	}

	public String getConnectionTypeName() {
		switch (type) {
			case ConnectionType.AUDIO: return "Audio";		// 24bit, min = -64, max = +64 - 96kHz.
			case ConnectionType.CNTRL: return "Control";	// 24bit, min = -64, max = +64 - 24kHz.
			case ConnectionType.LOGIC: return "Logic";		// 1bit, low =  0, high = +64.
			case ConnectionType.SLAVE: return "Slave";		// frequency reference between master and slave modules
			default: return "Wrong type...";
		}
	}

    public String toString() {
        return (input?"input ":"output ") + index + " " + getConnectionTypeName() + "(" + type + ") " + name;
    }
    
    public ConnectorGUI getConnectionGUI() {
        return moduleConnection;
    }
}
