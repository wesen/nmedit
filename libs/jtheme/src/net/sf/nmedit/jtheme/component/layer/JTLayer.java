/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jtheme.component.layer;

import java.awt.Graphics;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.component.JTBaseComponent;

public class JTLayer extends JTBaseComponent
{
    
    private boolean ignoreEvents = true;

    public JTLayer()
    {
        this(null);
    }

    public JTLayer(JTContext context)
    {
        super(context);
        setOpaque(false);
        setJTFlag(FLAG_PROPERTY_SUPPORT, false);
        setJTFlag(FLAG_INVALIDATE, true);
        setJTFlag(FLAG_VALIDATE, true);
        setJTFlag(FLAG_REVALIDATE, true);
        setJTFlag(FLAG_VALIDATE_TREE, true);
    }
    
    public boolean contains(int x, int y)
    {
        return (!ignoreEvents) && super.contains(x, y);
    }
    
    public void setIgnoresEvents(boolean enable)
    {
        this.ignoreEvents = enable;
    }
    
    public boolean getIgnoresEvents()
    {
        return ignoreEvents;
    }
    
    protected void paintComponent(Graphics g)
    {
        // nothing to paint
    }
    
    protected void paintChildren(Graphics g)
    {
        // nothing to paint
    }
    
}
