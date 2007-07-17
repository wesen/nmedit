package net.sf.nmedit.jsynth.event;

import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.Synthesizer;

public class BankUpdateEvent extends SynthesizerEvent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1465573513123819310L;
    private int beginIndex;
    private int endIndex;

    public BankUpdateEvent(Bank<? extends Synthesizer> bank, int beginIndex, int endIndex)
    {
        super(bank.getSynthesizer(), SYNTH_BANK_UPDATE, bank);
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }
    
    public Bank<? extends Synthesizer> getBank()
    {
        return (Bank) arg;
    }
    
    public int getBeginIndex()    
    {
        return beginIndex;
    }
    public int getEndIndex()    
    {
        return endIndex;
    }
    
}
