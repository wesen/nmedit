package net.sf.nmedit.waldorf.miniworks4pole;

import javax.swing.JComponent;

import net.sf.nmedit.nomad.core.util.document.Document;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jtheme.JTMWPatch;

public class MWPatchDoc implements Document
{
    
    private JTMWPatch jtpatch;
    private MWPatch patch;
    
    public MWPatchDoc(MWPatch patch)
    {
        this.patch = patch;
    }

    public JComponent getComponent()
    {
        if (jtpatch == null)
            jtpatch = MWData.createPatchUI(patch);
                
        return jtpatch;
    }

    public String getTitle()
    {
        return "MiniWorks";
    }

}
