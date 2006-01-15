package org.nomad.port;

import org.nomad.port.message.AckMessage;
import org.nomad.port.message.GetPatchMessage;
import org.nomad.port.message.IAmMessage;
import org.nomad.port.message.LightMessage;
import org.nomad.port.message.NewPatchInSlotMessage;
import org.nomad.port.message.ParameterMessage;
import org.nomad.port.message.PatchListMessage;
import org.nomad.port.message.PatchMessage;
import org.nomad.port.message.RequestPatchMessage;
import org.nomad.port.message.SlotActivatedMessage;
import org.nomad.port.message.SlotsSelectedMessage;
import org.nomad.port.message.VoiceCountMessage;

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
