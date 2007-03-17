package net.waldorf.miniworks4pole.jsynth;

import net.sf.nmedit.jsynth.AbstractSlot;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.worker.RequestPatchWorker;
import net.sf.nmedit.jsynth.worker.SendPatchWorker;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;
import net.waldorf.miniworks4pole.jpatch.MWPatch;

public class MWSlot extends AbstractSlot implements Slot
{
    
    private Miniworks4Pole synth;
    private MWPatch patch;

    public MWSlot(Miniworks4Pole synth)
    {
        this.synth = synth;
    }

    public void setPatch(MWPatch patch)
    {
        if (this.patch != patch)
        {
            this.patch = patch;
            fireNewPatchInSlotEvent();
        }
    }
    
    public RequestPatchWorker createRequestPatchWorker()
    {
        return new RequestPatchWorker()
        {
            public void requestPatch() throws SynthException
            {
                throw new SynthException("operation not supported, change bank at device");
            }   
        };
    }

    public SendPatchWorker createSendPatchWorker()
    {
        throw new UnsupportedOperationException();
    }

    public StorePatchWorker createStorePatchWorker()
    {
        throw new UnsupportedOperationException();
    }

    public String getName()
    {
        return "Slot "+getSlotIndex();
    }

    public String getPatchName()
    {
        return "Patch";
    }

    public int getSlotIndex()
    {
        return 1;
    }

    public Miniworks4Pole getSynthesizer()
    {
        return synth;
    }

    public MWPatch getPatch()
    {
        return patch;
    }

}
