package net.sf.nmedit.jsynth.worker;

import net.sf.nmedit.jsynth.SynthException;

public interface RequestPatchWorker extends Worker
{
    
    void requestPatch()
        throws SynthException;
    
}
