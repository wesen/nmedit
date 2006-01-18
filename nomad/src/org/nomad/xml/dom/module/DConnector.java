package org.nomad.xml.dom.module;

import java.awt.Image;

import org.nomad.util.graphics.ImageTracker;


/**
 * A object describing the properties of a connector.
 * 
 * @author Christian Schneider
 */
public class DConnector {

	/** The connector is an input. */
	public final static int CONNECTOR_TYPE_INPUT = 0;
	
	/** The connector is an output. */
	public final static int CONNECTOR_TYPE_OUTPUT = 1;

	/** The connector has an audio signal as input/output */
	public final static int SIGNAL_AUDIO = 0;
	/** The connector has an controlling signal as input/output */
	public final static int SIGNAL_CONTROL = 1;
	/** The connector has an logic signal as input/output */
	public final static int SIGNAL_LOGIC = 2;
	/** The connector has an slave signal as input/output */
	public final static int SIGNAL_SLAVE = 3;

	/** the module this connector belongs to */
	private DModule parent;
	/** the connector type, either CONNECTOR_TYPE_INPUT or CONNECTOR_TYPE_OUTPUT */
	private int cnType;
	/** the connector signal, one of SIGNAL_* values */
	private int cnSignal;
	/** the name of this connector */
	private String cnName;
	/** the module-unique id of this connector */
	private int cnId;
	/** the image tracker that is used to get icons from */
	private static ImageTracker imageTracker = null;
	
	/**
	 * Creates a new connector.
	 * @param parent the parent module
	 * @param connectionId the id of this connector
	 * @param connectionType the type of this connector
	 * @param connectionSignal the signal type of this connector
	 * @param name the name of this connector
	 */
	public DConnector(DModule parent,
			int connectionId, int connectionType, 
			int connectionSignal, String name) {
		this.parent = parent;
		if (parent==null) 
			throw new NullPointerException("'parent' must not be null");

		this.cnId = connectionId;
		this.cnType=connectionType;
		this.cnSignal=connectionSignal;
		this.cnName = name;
	}

	/**
	 * Returns the parent module.
	 * @return the parent module
	 */
	public DModule getParent() {
		return parent;
	}

	/**
	 * Returns the connector's name.
	 * @return the connector's name.
	 */
	public String getName() {
		return cnName;
	}
	
	/**
	 * Returns the connector's type. This is one of CONNECTOR_TYPE_INPUT or CONNECTOR_TYPE_OUTPUT
	 * @return the connector's type.
	 */
	public int getType() {
		return cnType;
	}
	
	/**
	 * Returns the connector's signal type. This is one of SIGNAL_AUDIO, SIGNAL_CONTROL, SIGNAL_SLAVE, SIGNAL_LOGIC
	 * @return the connector's signal type.
	 */
	public int getSignal() {
		return cnSignal;
	}
	
	/**
	 * Returns true if the connector is an input
	 * @return true if the connector is an input
	 */
	public boolean isInput() {
		return cnType==DConnector.CONNECTOR_TYPE_INPUT;
	}
	
	/**
	 * Returns true if the connector is an output
	 * @return true if the connector is an output
	 */
	public boolean isOutput() {
		return cnType==DConnector.CONNECTOR_TYPE_OUTPUT;
	}
	
	/**
	 * Returns true if the object is an audio-signal connector.
	 * @return true if the object is an audio-signal connector.
	 */
	public boolean isSignalAudio() {
		return cnSignal==DConnector.SIGNAL_AUDIO;
	}
	
	/**
	 * Returns true if the object is an control-signal connector.
	 * @return true if the object is an control-signal connector.
	 */
	public boolean isSignalControl() {
		return cnSignal==DConnector.SIGNAL_CONTROL;
	}
	
	/**
	 * Returns true if the object is an logic-signal connector.
	 * @return true if the object is an logic-signal connector.
	 */
	public boolean isSignalLogic() {
		return cnSignal==DConnector.SIGNAL_LOGIC;
	}
	
	/**
	 * Returns true if the object is an slave-signal connector.
	 * @return true if the object is an slave-signal connector.
	 */
	public boolean isSignalSlave() {
		return cnSignal==DConnector.SIGNAL_SLAVE;
	}
	
	/**
	 * Returns the name of the connectors signal. For example if the signal
	 * type is SIGNAL_AUDIO, this returns the string 'audio'
	 * @return the name of the connectors signal
	 */
	public String getSignalName() {
		return getSignalName(cnSignal);
	}

	public final static String getSignalName(int cnSignal) {
		switch (cnSignal) {
			case DConnector.SIGNAL_AUDIO: return "audio";
			case DConnector.SIGNAL_CONTROL: return "control";
			case DConnector.SIGNAL_LOGIC: return "logic";
			case DConnector.SIGNAL_SLAVE: return "slave"; 
			default: return "invalid";
		}
	}

	public final static int getSignalId(String name) {
		if ("audio".equals(name))
			return DConnector.SIGNAL_AUDIO;
		else if ("control".equals(name))
			return DConnector.SIGNAL_CONTROL;
		else if ("logic".equals(name))
			return DConnector.SIGNAL_LOGIC;
		else if ("slave".equals(name))
			return DConnector.SIGNAL_SLAVE;
		else return -1;
	}
	
	/**
	 * Returns the name of the connectors type. For example if the
	 * type is CONNECTOR_TYPE_INPUT, this returns the string 'input'
	 * @return the name of the connectors type
	 */
	public String getConnectionTypeName() {
		switch (cnType) {
			case DConnector.CONNECTOR_TYPE_INPUT: return "input";
			case DConnector.CONNECTOR_TYPE_OUTPUT: return "output";
			default: return "invalid";
		}
	}
	
	/**
	 * Returns the string <code>"connector."+getId()+"."+getConnectionTypeName()</code>.
	 * For example, if the id is 2, and the connector is an input this returns
	 *  the string 'connector.2.input'. 
	 */
	public String toString() {
		/*
		return super.toString()+"[type:"+getConnectionTypeName()
		+",signal:"+getSignalName()+"]";
		*/
		return "connector."+getId()+"."+getConnectionTypeName();
	}

	/**
	 * Returns the id of this connector
	 * @return the id of this connector
	 */
	public int getId() {
		return cnId;
	}

	/**
	 * Returns the appropriate icon for this connector from the image tracker.
	 * The icon's key depends of the state of this connector.
	 * The key has the form <code>(audio|control|logic|slave) '.' (in|out) '.' (free|used)</code>.
	 * 	 
	 * @param itracker
	 * @param free if true, then 'free' is used, otherwise 'used' is used
	 * @return the icon representing the state of this connector
	 */
	public Image getIcon(ImageTracker itracker, boolean free) {
		String key = "";
		switch (cnSignal) {
			case SIGNAL_AUDIO: key+="audio."; break;
			case SIGNAL_CONTROL: key+="control."; break;
			case SIGNAL_LOGIC: key+="logic."; break;
			case SIGNAL_SLAVE: key+="slave."; break;
		}
		switch (cnType) {
			case CONNECTOR_TYPE_INPUT: key+="in."; break;
			case CONNECTOR_TYPE_OUTPUT: key+="out."; break; 
		}
		key+=free?"free":"used";
		return itracker.getImage(key);
	}
	
	/**
	 * Sets the image tracker that will be used by the <code>getIcon()</code> method.
	 * @param itracker the image tracker
	 */
	public static void setImageTracker(ImageTracker itracker) {
		DConnector.imageTracker = itracker;
	}
	
	/**
	 * Returns an icon representing the current state using the image tracker set by
	 * <code>setImageTracker(ImageTracker)</code>
	 * @param free true if the connector is free, false if the connector is linked to an cable
	 * @return an icon representing the current state
	 */
	public Image getIcon(boolean free) {
		return imageTracker==null?null:getIcon(imageTracker, free);
		//return images[cnSignal][cnType][free?CONNECTOR_FREE:CONNECTOR_IN_USE];
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof DConnector) {
			DConnector comp = (DConnector) obj;
			return 
			    // same module
				(comp.getParent().getModuleID() == getParent().getModuleID())
				// same id
			  &&(comp.getId()==getId())
			    // same type (input|output). ids are only unique together with their input/output type
			  &&(comp.getType()==getType())
			;
		} else
			return super.equals(obj);
	}
	
}
