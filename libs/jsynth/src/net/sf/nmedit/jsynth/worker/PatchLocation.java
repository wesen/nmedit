package net.sf.nmedit.jsynth.worker;

public class PatchLocation
{
    
    private int bank = -1;
    private int position = -1;
    private int slot = -1;
    
    public PatchLocation(int bank, int position)
    {
        this.bank = bank;
        this.position = position;
    }
    
    public PatchLocation(int slot)
    {
        this.slot = slot;
    }
    
    public boolean inSlot()
    {
        return slot>=0;
    }
    
    public boolean inBank()
    {
        return bank>=0 && position>=0;
    }

    public int getBank()
    {
        return bank;
    }

    public int getPosition()
    {
        return position;
    }
    
    public int getSlot()
    {
        return slot;
    }
    
}
