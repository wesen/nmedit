package net.sf.nmedit.jpatch.nomad;

import javax.swing.JFrame;

import net.sf.nmedit.jpatch.randomizer.Mutator;

public class MutatorController
{

    private static MutatorController instance = new MutatorController();
    
    public static MutatorController sharedInstance()
    {
        return instance;
    }
    
    private Mutator mutator;
    
    private void ensureMutatorCreated()
    {
        if (mutator == null)
        {
            mutator = new Mutator();
            mutator.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    public void openMutatorWindow()
    {
        ensureMutatorCreated();
        mutator.getFrame().setVisible(true);
    }

}
