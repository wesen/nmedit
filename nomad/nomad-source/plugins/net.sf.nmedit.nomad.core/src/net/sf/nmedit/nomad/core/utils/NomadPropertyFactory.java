package net.sf.nmedit.nomad.core.utils;

import java.util.Properties;

import net.sf.nmedit.nmutils.properties.RootSystemProperties;
import net.sf.nmedit.nmutils.properties.SystemProperties;
import net.sf.nmedit.nmutils.properties.SystemPropertyFactory;

public class NomadPropertyFactory extends SystemPropertyFactory
{

    private RootSystemProperties properties;
    
    public NomadPropertyFactory(RootSystemProperties properties)
    {
        this.properties = properties;
    }
    
    public Properties getProperties()
    {
        return properties.getProperties();
    }
    
    protected String getIdFor(Class<?> forClass)
    {
        return forClass.getName()+"."; 
    }
    
    public SystemProperties getPropertiesForClass(Class<?> forClass)
    {
        return new SystemProperties(properties, getIdFor(forClass));
    }

}
