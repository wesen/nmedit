package net.sf.nmedit.jsynth.worker;

import net.sf.nmedit.jsynth.SynthException;

public interface StorePatchWorker extends Worker
{

    void store() throws SynthException;
    
}
