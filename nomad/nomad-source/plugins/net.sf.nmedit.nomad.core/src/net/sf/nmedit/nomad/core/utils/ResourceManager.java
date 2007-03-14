/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Nov 24, 2006
 */
package net.sf.nmedit.nomad.core.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Formatter;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.nmedit.nomad.core.i18n.LocaleConfiguration;

public class ResourceManager implements PropertyChangeListener
{
    
    private ResourceBundle resourceBundle;
    private String resourceName;
    private ClassLoader classLoader;
    private Locale locale = null;
    private Formatter formatter = null;
    private StringBuilder stringbuilder = null;

    public ResourceManager(String resourceName)
    {
        this(resourceName, null);
    }
    
    public ResourceManager(String resourceName, ClassLoader classLoader)
    {
        this.resourceName = resourceName;
        this.classLoader = classLoader;
        LocaleConfiguration conf =
        LocaleConfiguration.getLocaleConfiguration();
        conf.addLocaleChangeListener(this);
    }
    
    public String getString(String key)
    {
        return getBundle().getString(key);
    }
    
    public Locale getLocale()
    {
        if (locale == null)
        {
            locale = LocaleConfiguration
                .getLocaleConfiguration()
                .getCurrentLocale();
        }
        return locale;
    }
    
    private void ensureFormatterInitialized()
    {
        if (stringbuilder == null)
        {
            stringbuilder = new StringBuilder();
            formatter = new Formatter(stringbuilder);
        }
        else
        {
            stringbuilder.setLength(0);
        }
    }
    
    public String getMessage(String key, Object ... args)
    {
        String format = getBundle().getString(key);
        // String:format is not null
        ensureFormatterInitialized();
        formatter.format(getLocale(), format, args);            
        return stringbuilder.toString();
    }
    
    public String getResourceName()
    {
        return resourceName;
    }

    public ClassLoader getResourceClassLoader()
    {
        if (classLoader == null)
            classLoader = getClass().getClassLoader();
        return classLoader;
    }
    
    public ResourceBundle getBundle()
    {
        if (resourceBundle  == null)
        {
            ClassLoader loader = getResourceClassLoader();
            
            Locale locale = LocaleConfiguration
                .getLocaleConfiguration()
                .getCurrentLocale();
            
            resourceBundle =
            ResourceBundle.getBundle(resourceName, locale, loader);
        }
        
        return resourceBundle;
    }
    
    public void dispose()
    {
        formatter = null;
        resourceBundle = null;
        LocaleConfiguration conf =
            LocaleConfiguration.getLocaleConfiguration();
        conf.removeLocaleChangeListener(this);
    }

    public void propertyChange( PropertyChangeEvent evt )
    {
        if (evt.getPropertyName()==LocaleConfiguration.LOCALE_PROPERTY)
        {
            resourceBundle = null;
            locale = null;
        }
    }
    
}


