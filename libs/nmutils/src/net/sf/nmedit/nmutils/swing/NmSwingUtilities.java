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
package net.sf.nmedit.nmutils.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

import gnu.classpath.javax.swing.plaf.basic.BasicGraphicsUtils;

public class NmSwingUtilities
{

    public static void drawString(Graphics g, String text,
                                                 int x, int y)
    {
        BasicGraphicsUtils.
        drawStringUnderlineCharAt(g, text, -1, x, y);
    }

    public static void drawStringUnderlineCharAt(Graphics g, String text,
                                                 int underlinedIndex,
                                                 int x, int y)
    {
        BasicGraphicsUtils.
        drawStringUnderlineCharAt(g, text, underlinedIndex, x, y);
    }

    
    public static <T extends Component> Collection<? extends T> getChildren(Class<T> clazz, Container parent)
    {
        Collection<T> dst = new ArrayList<T>(parent.getComponentCount());
        addChildren(clazz, dst, parent);
        return dst;
    }
    
    public static <T extends Component> void addChildren(Class<T> clazz, Collection<? super T> dst, Container parent)
    {
        for (Component c: parent.getComponents())
        {
            if (clazz.isInstance(c))
                dst.add(clazz.cast(c));
        }
    }

}

