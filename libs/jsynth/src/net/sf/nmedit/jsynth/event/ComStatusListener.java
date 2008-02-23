package net.sf.nmedit.jsynth.event;

import java.util.EventListener;

public interface ComStatusListener extends EventListener
{

    public void comStatusChanged(ComStatusEvent e);
    
}
