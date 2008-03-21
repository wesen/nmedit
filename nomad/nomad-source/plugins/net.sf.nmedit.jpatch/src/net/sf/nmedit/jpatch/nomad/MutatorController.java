package net.sf.nmedit.jpatch.nomad;

import javax.swing.JFrame;

import net.sf.nmedit.nomad.core.swing.document.Document; 

import net.sf.nmedit.jpatch.PPatch;
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
            mutator.getFrame().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
    }

    public void openMutatorWindow()
    {
        ensureMutatorCreated();
        mutator.getFrame().setVisible(true);
    }

	public void documentAdded(DocumentEvent e) {
		ensureMutatorCreated();
		PPatch patch = (PPatch) e.getDocument().getProperty("patch");
		mutator.addPatch(patch);
		mutator.selectPatch(patch);
	}

	public void documentRemoved(DocumentEvent e){
		ensureMutatorCreated();
		PPatch patch = (PPatch) e.getDocument().getProperty("patch");
		mutator.removePatch(patch);	
		
	}

	public void documentSelected(DocumentEvent e) {
		ensureMutatorCreated();
		Document d = e.getDocument();
		
		if (d != null) {
			PPatch patch = (PPatch) d.getProperty("patch");
			mutator.selectPatch(patch);	
		} else {
			mutator.selectPatch(null);
		}
	
	}

}
