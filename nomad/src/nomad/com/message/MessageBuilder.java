package nomad.com.message;

import nomad.patch.Patch;

public interface MessageBuilder {
	public AckMessage newAckMessage();
	public GetPatchMessage newGetPatchMessage(int slot, int pid);
	public IAmMessage newIAmMessage();
	public LightMessage newLightMessage();
	public NewPatchInSlotMessage newNewPatchInSlotMessage();	
	public ParameterMessage newParameterMessage(int pid, int section, int module, int parameter, int value);
	public PatchListMessage newPatchListMessage(int section, int position);
	public PatchMessage newPatchMessage(Patch patch);
	public RequestPatchMessage newRequestPatchMessage();
	public SlotActivatedMessage newSlotActivatedMessage();
	public SlotsSelectedMessage newSlotsSelectedMessage();
	public VoiceCountMessage newVoiceCountMessage();
}
