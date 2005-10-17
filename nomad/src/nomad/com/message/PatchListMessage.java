package nomad.com.message;

import nomad.com.BitInputStream;

/**
 * @author Christian Schneider
 * @hidden
 */
public class PatchListMessage extends MidiMessage {

	public PatchListMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected BitInputStream getRawData() {
		// TODO Auto-generated method stub
		return null;
	}
/*
	  public PatchListMessage(int section, int position)
	  public PatchListMessage(Packet packet)
	  public void getBitStream(BitStreamList bitStreamList) 
	  public void notifyListener(NMProtocolListener listener)
	  public int getSection()
	  public int getPosition() 
	  public StringList getNames() 
	  public boolean endOfList() 
	  public boolean endOfSection(String name)
	  public boolean emptyPosition(String name) 
*/
}
