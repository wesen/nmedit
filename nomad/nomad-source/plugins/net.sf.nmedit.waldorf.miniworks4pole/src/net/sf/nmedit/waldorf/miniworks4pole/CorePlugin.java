package net.sf.nmedit.waldorf.miniworks4pole;

import org.java.plugin.Plugin;

public class CorePlugin extends Plugin
{

    @Override
    protected void doStart() throws Exception
    {
        MWData.init();
    }

    @Override
    protected void doStop() throws Exception
    {
        //
    }

}
