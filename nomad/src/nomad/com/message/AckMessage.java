package nomad.com.message;

import nomad.com.BitInputStream;

/**
 * @author Christian Schneider
 * @hidden
 */
public class AckMessage extends MidiMessage {

	public AckMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
/*
	  public AckMessage(Packet packet)
	  public void setPid1(int pid) 
	  public void setPid2(int pid) 
	  public void getBitStream(BitStreamList bitStreamList)
	  public void notifyListener(NMProtocolListener listener)
	  public int getPid1()
	  public int getPid2()
*/

	protected BitInputStream getRawData() {
		// TODO Auto-generated method stub
		return null;
	} 
}
