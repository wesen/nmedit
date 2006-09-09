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
 * Created on Jul 7, 2006
 */
package net.sf.nmedit.nomad.theme.plugin;

import net.sf.nmedit.nomad.theme.NMTheme;

public abstract class ThemePluginProvider
{

    /**
     * Returns the name of the plugin.
     * @return the name of the plugin.
     */
    public abstract String getName();
    
    /**
     * Returns an array containing the author names.
     * @return array containing the author names.
     */
    public abstract String[] getAuthors();
    
    /**
     * Returns an description on the plugin.
     * @return an description on the plugin.
     */
    public abstract String getDescription();

    /**
     * Returns an instance of the factory. 
     * @return instance of the factory.
     */
    public abstract NMTheme getFactory();

    public abstract String getHomepage();
    public abstract String getVersion();
    
}
