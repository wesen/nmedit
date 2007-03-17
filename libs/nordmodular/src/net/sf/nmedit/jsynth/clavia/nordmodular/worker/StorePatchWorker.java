package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import net.sf.nmedit.jnmprotocol.StorePatchMessage;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NmSlot;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;

public class StorePatchWorker
  implements net.sf.nmedit.jsynth.worker.StorePatchWorker
{

    private NmSlot slot;
    private int section;
    private int position;

    public StorePatchWorker(NmSlot source, int section, int position)
    {
        this.slot = source;
        this.section = section;
        this.position = position;
    }

    public NordModular getSynth()
    {
        return slot.getSynthesizer();
    }

    public void store() throws SynthException
    {
        StorePatchMessage message = new StorePatchMessage();

        message.set("storeslot", slot.getSlotId());
        message.set("section", section);
        message.set("position", position);
        
        getSynth().getScheduler().offer(new ScheduledMessage(getSynth(), message));
    }
    
}
