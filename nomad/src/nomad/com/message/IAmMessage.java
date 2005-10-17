package nomad.com.message;

import nomad.com.BitInputStream;

/**
 * @author Christian Schneider
 * @hidden
 */
public class IAmMessage extends MidiMessage {

	public IAmMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected BitInputStream getRawData() {
		// TODO Auto-generated method stub
		return null;
	}
/*
	  public IAmMessage() 
	  public IAmMessage(Packet packet)
	  public void setVersion(int high, int low)
	  public void getBitStream(BitStreamList bitStreamList) 
	  public void notifyListener(NMProtocolListener listener)
	  
	  // Sender = enum:PC | MODULAR
	  public IAmMessage.Sender getSender()
	  public int getVersionHigh()
	  public int getVersionLow()
*/
}
