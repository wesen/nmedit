package net.sf.nmedit.jpatch.nomad;

import javax.swing.JFrame;

import net.sf.nmedit.jpatch.randomizer.Mutator;
import net.sf.nmedit.nomad.core.swing.document.DocumentEvent;
import net.sf.nmedit.nomad.core.swing.document.DocumentListener;

public class MutatorController implements DocumentListener
{

    private static MutatorController instance = new MutatorController();
    
    private static final boolean DEBUG= false;
    
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

	public void documentAdded(DocumentEvent e) {
		if (DEBUG) System.out.println("add");
		// TODO Auto-generated method stub
		
	}

	public void documentRemoved(DocumentEvent e) {
		if (DEBUG) System.out.println("removed");
		// TODO Auto-generated method stub
		
	}

	public void documentSelected(DocumentEvent e) {
		if (DEBUG) System.out.println("selected");
		// TODO Auto-generated method stub
		
	}

}
