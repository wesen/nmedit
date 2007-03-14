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
 * Created on Nov 18, 2006
 */
package net.sf.nmedit.nomad.boot.splash;

/**
 * Simple splash screen.
 * 
 * Implementations must be thread safe.
 * 
 * @author Christian Schneider
 */
public interface SplashScreen extends ProgressCallback
{

    public static final String PREFIX = "";//"org.java.plugin.boot.splash.";
    
    /**
     * key for the location of the splash screen image
     * the value can either be a {@link java.lang.String} or an {@link java.net.URL}
     */
    public static final String LOCATION_KEY = PREFIX+".location";
    
    
    /**
     * Adds an arbitrary key/value "client property" to this component.
     * @param key
     * @param value
     */
    void putClientProperty(Object key, Object value);
    
    /**
     * Returns the value of the property with the specified key.  Only
     * properties added with <code>putClientProperty</code> will return
     * a non-<code>null</code> value.  
     * @param key
     * @return
     */
    Object getClientProperty(Object key);

    /**
     * Creates and shows the splash screen.
     * Can be called multiple times.
     */
    void advance();
    
    /**
     * Disposes the splash screen.
     * Can be called multiple times.
     */
    void dispose();
    
}
