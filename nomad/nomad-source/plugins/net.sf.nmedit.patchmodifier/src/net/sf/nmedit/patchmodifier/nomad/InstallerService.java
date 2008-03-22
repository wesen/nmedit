package net.sf.nmedit.patchmodifier.nomad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.swing.document.DefaultDocumentManager;

public class InstallerService implements InitService
{

    public void init()
    {
        Nomad nomad = Nomad.sharedInstance();
        MLEntry mlPatch = nomad.getMenuLayout().getEntry("nomad.menu.patch");
        
        MLEntry mlMutator = new MLEntry("mutator"); // TODO i18n
        mlMutator.putValue(MLEntry.NAME, "Mutator");
        mlMutator.putValue(MLEntry.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));
        
    	DefaultDocumentManager dm = Nomad.sharedInstance().getDocumentManager();
       	dm.addListener(MutatorController.sharedInstance());
       	
        mlMutator.addActionListener(new ActionListener(){
       
            public void actionPerformed(ActionEvent e)
            {
                MutatorController.sharedInstance().openMutatorWindow();
            }
            
        });
        mlPatch.add(mlMutator);
        RandomizerTools.sharedInstance().install();
    }

    public void shutdown()
    {
        // TODO Auto-generated method stub

    }

    public Class<? extends Service> getServiceClass()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
