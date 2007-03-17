package net.waldorf.miniworks4pole;

import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jpatch.spec.formatter.Formatter;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMStorageContext;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
public class MWHelper
{

    public static ModuleDescriptions createModuleDescriptions()
    {
        ModuleDescriptions md = new ModuleDescriptions();
        md.getFormatterRegistry().putFormatter("GateTimeFormatter", new GateTimeFormatter() );
     
        return md;
    }

    public static class GateTimeFormatter implements Formatter
    {

        public String getString(Parameter parameter, int value)
        {
            return Integer.toString(value);
        }

    }
    
    public static DefaultStorageContext createStorageContext(ClassLoader loader)
    {
        return new NMStorageContext(loader);
    }
    
}
