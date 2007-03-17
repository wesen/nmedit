package net.waldorf.miniworks4pole.jprotocol;

import java.util.EventListener;

public abstract class MWMidiListener implements EventListener
{

    public void parameterMessage(MiniworksMidiMessage message) {};
    public void bankChangeMessage(MiniworksMidiMessage message) {};
    public void programDumpMessage(MiniworksMidiMessage message) {};
    public void programBulkDumpMessage(MiniworksMidiMessage message) {};
    public void allDumpMessage(MiniworksMidiMessage message) {}
    public void aliveMessage(MiniworksMidiMessage message) { };

}
