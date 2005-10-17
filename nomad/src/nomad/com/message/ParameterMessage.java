package nomad.com.message;

import nomad.com.BitInputStream;

/**
 * @author Christian Schneider
 * @hidden
 */
public class ParameterMessage extends MidiMessage {

	public ParameterMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected BitInputStream getRawData() {
		// TODO Auto-generated method stub
		return null;
	}
/*
	  public ParameterMessage() 
	  public ParameterMessage(Packet packet) 
	  public ParameterMessage(int pid, int section, int module, int parameter, int value) 
	  public void getBitStream(BitStreamList bitStreamList)
	  public void notifyListener(NMProtocolListener listener) 
	  public ModuleSection.Type getSection()
	  public int getModule() 
	  public int getParameter()
	  public int getValue()
	  public int getPid() 
	  public void setParameter(int param)
	  public void setValue(int value)
*/
}
