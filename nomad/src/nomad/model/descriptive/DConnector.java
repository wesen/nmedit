package nomad.model.descriptive;

public class DConnector {

	public final static int CONNECTOR_TYPE_INPUT = 0;
	public final static int CONNECTOR_TYPE_OUTPUT = 1;

	public final static int SIGNAL_AUDIO = 0;
	public final static int SIGNAL_CONTROL = 1;
	public final static int SIGNAL_LOGIC = 2;
	public final static int SIGNAL_SLAVE = 3;

	public DModule parent;

	private int cnType;
	private int cnSignal;
	private String cnName;
	private String cnId;
	
	public DConnector(DModule parent,
			String connectionId, int connectionType, 
			int connectionSignal, String name) {
		this.parent = parent;
		if (parent==null) 
			throw new NullPointerException("'parent' must not be null");
		

		this.cnId = connectionId;
		this.cnType=connectionType;
		this.cnSignal=connectionSignal;
		this.cnName = name;
	}

	public DModule getParent() {
		return parent;
	}

	public String getName() {
		return cnName;
	}
	
	public int getType() {
		return cnType;
	}
	
	public int getSignal() {
		return cnSignal;
	}
	
	public boolean isInput() {
		return cnType==DConnector.CONNECTOR_TYPE_INPUT;
	}
	
	public boolean isOutput() {
		return cnType==DConnector.CONNECTOR_TYPE_OUTPUT;
	}
	
	public boolean isSignalAudio() {
		return cnSignal==DConnector.SIGNAL_AUDIO;
	}
	
	public boolean isSignalControl() {
		return cnSignal==DConnector.SIGNAL_CONTROL;
	}
	
	public boolean isSignalLogic() {
		return cnSignal==DConnector.SIGNAL_LOGIC;
	}
	
	public boolean isSignalSlave() {
		return cnSignal==DConnector.SIGNAL_SLAVE;
	}
	
	public String getSignalName() {
		switch (cnSignal) {
			case DConnector.SIGNAL_AUDIO: return "audio";
			case DConnector.SIGNAL_CONTROL: return "control";
			case DConnector.SIGNAL_LOGIC: return "logic";
			case DConnector.SIGNAL_SLAVE: return "slave"; 
			default: return "invalid";
		}
	}
	
	public String getConnectionTypeName() {
		switch (cnType) {
			case DConnector.CONNECTOR_TYPE_INPUT: return "input";
			case DConnector.CONNECTOR_TYPE_OUTPUT: return "output";
			default: return "invalid";
		}
	}
	
	public String toString() {
		return super.toString()+"[type:"+getConnectionTypeName()
		+",signal:"+getSignalName()+"]";
	}

	public String getId() {
		return cnId;
	}
	
}
