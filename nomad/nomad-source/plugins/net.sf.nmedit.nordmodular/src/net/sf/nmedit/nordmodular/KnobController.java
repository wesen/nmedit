package net.sf.nmedit.nordmodular;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMKnobAssignmentWindow;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentEvent;
import net.sf.nmedit.nomad.core.swing.document.DocumentListener;

public class KnobController implements ActionListener, DocumentListener, WindowListener
{
    
    private NMKnobAssignmentWindow kaw = null;
    
    private void ensureCreated()
    {
        if (kaw != null) return;
        NMContextData data = NMContextData.sharedInstance();
        kaw = new NMKnobAssignmentWindow(data.getJTContext());
        kaw.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e)
    {
        ensureCreated();
        documentChanged(Nomad.sharedInstance().getDocumentManager().getSelection());
        kaw.getFrame().setVisible(true);
        kaw.getFrame().addWindowListener(this);
    }

    public void documentAdded(DocumentEvent e)
    {
        // no op
    }

    public void documentRemoved(DocumentEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    public void documentSelected(DocumentEvent e)
    {
        documentChanged(e.getDocument());
    }

    private void documentChanged(Document document)
    {
        if (kaw == null) return;
        
        NMPatch newPatch = null;
        if (document != null && document instanceof PatchDocument)
        {
            newPatch = ((PatchDocument) document).getPatch();
        }
        
        kaw.setPatch(newPatch);
    }

    public void windowClosed(WindowEvent e)
    {
        if (kaw != null)
        {
            kaw.getFrame().removeWindowListener(this);
            kaw.setPatch(null);
            kaw = null;
        }
    }
    
    public void windowActivated(WindowEvent e)
    {
        // no op
    }

    public void windowClosing(WindowEvent e)
    {
        // no op
    }

    public void windowDeactivated(WindowEvent e)
    {
        // no op
    }

    public void windowDeiconified(WindowEvent e)
    {
        // no op
    }

    public void windowIconified(WindowEvent e)
    {
        // no op
    }

    public void windowOpened(WindowEvent e)
    {
        // no op
    }

}
