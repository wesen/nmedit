package net.sf.nmedit.jsynth.event;

import net.sf.nmedit.jsynth.ComStatus;
import net.sf.nmedit.jsynth.Synthesizer;

public class ComStatusEvent extends SynthesizerEvent
{

    private static final long serialVersionUID = 5712107834925411980L;

    private ComStatus status;

    public ComStatusEvent(Synthesizer synth, ComStatus status)
    {
        super(synth, SYNTH_COM_STATUS_CHANGED, null);
        this.status = status;
    }

    public ComStatus getStatus()
    {
        return status;
    }
    
}
