package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import net.sf.nmedit.jnmprotocol.DeletePatchMessage;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.NordModular;

public class DeletePatchWorker
{

    private int section;
    private int position;
    private NordModular synth;

    public DeletePatchWorker(NordModular synth, int section, int position)
    {
        this.synth = synth;
        this.section = section;
        this.position = position;
    }

    public void store() throws SynthException
    {
        DeletePatchMessage message = new DeletePatchMessage();

        message.set("section", section);
        message.set("position", position);
        
        synth.getScheduler().offer(new ScheduledMessage(synth, message));
    }
    
}
