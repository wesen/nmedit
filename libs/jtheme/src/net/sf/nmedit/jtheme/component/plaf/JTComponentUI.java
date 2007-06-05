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
 * Created on Jan 20, 2007
 */
package net.sf.nmedit.jtheme.component.plaf;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import net.sf.nmedit.jtheme.component.JTComponent;

/**
 * The base class for all UI delegates used by 
 * {@link net.sf.nmedit.jtheme.component.JTComponent}s.
 */
public class JTComponentUI extends ComponentUI
{
    
    /**
     * Throws an unsupported operation exception.
     * 
     * The component is painted using
     * {@link #paintStaticLayer(Graphics2D, JComponent)} and
     * {@link #paintDynamicLayer(Graphics2D, JComponent)}.
     * 
     * @throws UnsupportedOperationException
     */
    public final void paint(Graphics g, JComponent c) 
    {
        throw new UnsupportedOperationException();
    }


    /**
     * Throws an unsupported operation exception.
     * 
     * The component is painted using
     * {@link #paintStaticLayer(Graphics2D, JComponent)} and
     * {@link #paintDynamicLayer(Graphics2D, JComponent)}.
     * 
     * @throws UnsupportedOperationException
     */
    public final void update(Graphics g, JComponent c) 
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Paints the static layer of the component.
     * 
     * @param g the <code>Graphics</code> context in which to paint
     * @param c the component being painted;
     *          this argument is often ignored,
     *          but might be used if the UI object is stateless
     *          and shared by multiple components
     */
    public void paintStaticLayer(Graphics2D g, JTComponent c)
    {
        
    }

    /**
     * Paints the dynamic layer of the component.
     * 
     * @param g the <code>Graphics</code> context in which to paint
     * @param c the component being painted;
     *          this argument is often ignored,
     *          but might be used if the UI object is stateless
     *          and shared by multiple components
     */
    public void paintDynamicLayer(Graphics2D g, JTComponent c)
    {
        
    }

}
