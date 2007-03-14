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
 * Created on Nov 19, 2006
 */
package net.sf.nmedit.nomad.core.i18n;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.event.EventListenerList;

public class LocaleConfiguration
{
    
    public final static String LOCALE_PROPERTY = "locale.property";
    
    private Locale vmDefaultLocale;
    
    private EventListenerList eventListeners = null;

    private static LocaleConfiguration conf = null;
    
    public static LocaleConfiguration getLocaleConfiguration()
    {
        if (conf == null)
            conf = new LocaleConfiguration();
        return conf;
    }
    
    private LocaleConfiguration()
    {
        vmDefaultLocale = Locale.getDefault();
    }
    
    public Locale getVMDefaultLocale()
    {
        return vmDefaultLocale;
    }
    
    public Locale getCurrentLocale()
    {
        return Locale.getDefault();
    }

    public void setCurrentLocale(Locale locale)
    {
        Locale prev = getCurrentLocale();
        
        if ( (locale!=null) && ((prev==null)||(!prev.equals(locale))))
        {
            Locale.setDefault(locale);
            
            fireLocaleChangeEvent(prev, locale);
        }
    }
    
    private void fireLocaleChangeEvent(Locale prevLocale, Locale newLocale)
    {
        if (eventListeners != null)
        {
            PropertyChangeEvent pcEvent = null;
            Object[] listeners = eventListeners.getListenerList();
            for (int i=listeners.length-2;i>=0;i-=2)
            {
                if (listeners[i]==PropertyChangeListener.class) {
                    // Lazily create the event:
                    if (pcEvent == null)
                        pcEvent = new PropertyChangeEvent(this, LOCALE_PROPERTY, prevLocale, newLocale);
                    ((PropertyChangeListener)listeners[i+1]).propertyChange(pcEvent);
                }
            }
        }
    }
    
    public void addLocaleChangeListener(PropertyChangeListener l)
    {
        if (eventListeners == null)
            eventListeners = new EventListenerList();
        eventListeners.add(PropertyChangeListener.class, l);
    }
    
    public void removeLocaleChangeListener(PropertyChangeListener l)
    {
        if (eventListeners != null)
            eventListeners.remove(PropertyChangeListener.class, l);
    }
    
}
