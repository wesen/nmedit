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
 * Created on Jun 28, 2006
 */
package net.sf.nmedit.nomad.main.ui.sidebar;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public interface Sidebar
{

    /**
     * Returns an icon for the sidebar
     */
    public ImageIcon getIcon();
    
    /**
     * Returns a discription of the sidebar used for tooltip text.
     */
    public String getDescription();

    /**
     * Creates the component displayed as sidebar.
     */
    public JComponent createView();
    
    /**
     * Disposes the view and associated data.
     */
    public void disposeView();

    public void setSize( int i );
    
}
