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
 * Created on Apr 29, 2006
 */
package net.sf.nmedit.nomad.core.swing.document;

import java.io.File;

import javax.swing.Icon;
import javax.swing.JComponent;

public interface Document
{

    public static final String ACTION_COMMAND_SAVE = "save";
    public static final String ACTION_COMMAND_SAVE_AS = "save as";
    public static final String ACTION_COMMAND_CLOSE = "close"; 
    public static final String ACTION_COMMAND_PROPERTIES = "properties"; 
    
    public File getFile();
    public String getTitle();
    public String getTitleExtended();
    public JComponent getComponent();
    Icon getIcon();
    void dispose();
    
    <T> T getFeature(Class<T> featureClass);
    
    Object getProperty(String name);
 
    public boolean isModified();
}
