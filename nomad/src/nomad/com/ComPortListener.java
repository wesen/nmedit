package nomad.com;

import nomad.com.message.*;

/**
 * 
 * @author Christian Schneider
 * @see ComPort
 */
public interface ComPortListener extends AbstractSynthListener {
	void messageReceived(IAmMessage message);
	void messageReceived(LightMessage message);
	void messageReceived(PatchMessage message);
	void messageReceived(AckMessage message);
	void messageReceived(PatchListMessage message);
	void messageReceived(NewPatchInSlotMessage message);
	void messageReceived(VoiceCountMessage message);
	void messageReceived(GetPatchMessage message);
	void messageReceived(SlotsSelectedMessage message);
	void messageReceived(SlotActivatedMessage message);
	void messageReceived(RequestPatchMessage message);
	void messageReceived(ParameterMessage message);
}
