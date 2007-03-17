package net.sf.nmedit.waldorf.miniworks4pole;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.service.synthService.NewSynthService;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.waldorf.miniworks4pole.jsynth.Miniworks4Pole;

public class NewMiniworks
    implements NewSynthService
{

    public String getSynthDescription()
    {
        return "http://www.waldorfmusic.de/";
    }

    public String getSynthName()
    {
        return "Miniworks 4 Pole";
    }

    public String getSynthVendor()
    {
        return "Waldorf";
    }

    public String getSynthVersion()
    {
        return "?";
    }

    public boolean isNewSynthAvailable()
    {
        return true;
    }

    public void newSynth()
    {
        ExplorerTree etree = Nomad.sharedInstance().getExplorer();
        Miniworks4Pole synth = MWData.createSynth();
        etree.addRootNode(new WMSynthDeviceContext(etree, synth, getSynthName()));
    }

    public Class<? extends Service> getServiceClass()
    {
        return NewSynthService.class;
    }

    
}
