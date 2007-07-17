package net.sf.nmedit.jtheme.randomizer;

import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.randomizer.GaussianRandomizer;
import net.sf.nmedit.jtheme.component.JTPatch;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.menulayout.ActionHandler;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.swing.document.Document;

public class RandomizerTools
{

    private static final String MENU_PATCH_RANDOMIZE = "nomad.menu.patch.randomize";
    
    private static RandomizerTools instance;
    
    public static RandomizerTools sharedInstance()
    {
        if (instance == null)
            instance = new RandomizerTools();
        return instance;
    }
    
    public void install()
    {
        // add randomize action
        MenuLayout menuLayout = Nomad.sharedInstance().getMenuLayout();        
        MLEntry e = menuLayout.getEntry(MENU_PATCH_RANDOMIZE);
        e.setEnabled(true);
        // ActionHandler invokes randomizePatch()
        e.addActionListener(new ActionHandler(this, true, "randomizePatch"));
    }
    
    public PPatch getSelectedPatch()
    {
        JTPatch jtpatch = getSelectedJTPatch();
        return jtpatch != null ? jtpatch.getPatch() : null;
    }
    
    public JTPatch getSelectedJTPatch()
    {
        Document doc = Nomad.sharedInstance().getDocumentManager().getSelection();
        if (doc == null) 
            return null;
        try
        {
            return (JTPatch) doc.getComponent();
        }
        catch (ClassCastException e)
        {
            return null; 
        }
    }

    public void randomizePatch() 
    {    
        PPatch patch = getSelectedPatch();
        if (patch == null)
            return;
            
        GaussianRandomizer randomizer = GaussianRandomizer.getRandomizer();
        randomizer.randomize(patch);
    }

}
