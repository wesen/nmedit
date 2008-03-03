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
package net.sf.nmedit.nmutils.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class WorkIndicator
{
    
    private WorkIndicator()
    {
        super(); // do not allow subclassing
    }

    public static Runnable create(Component component, Runnable r)
    {
        return new WrappedRunnable(component, r);
    }
    
    private static class WrappedRunnable implements Runnable
    {
        
        private Component component;
        private Runnable r;
        
        public WrappedRunnable(Component component, Runnable r)
        {
            this.component = component;
            this.r = r;
        }

        public void run()
        {
            SetCursor newCursor = new SetCursor(component, Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
            SetCursor oldCursor = new SetCursor(component, component.getCursor(), true);
            try
            {
                newCursor.run();
                r.run();
            }
            finally
            {
                oldCursor.run();
            }
        }
        
    }
    
    private static class SetCursor implements Runnable
    {
        private Component component;
        private Cursor cursor;
        private boolean invokeLater;

        public SetCursor(Component component, Cursor cursor, boolean invokeLater)
        {
            this.component = component;
            this.cursor = cursor;
            this.invokeLater = invokeLater;
        }

        public void run()
        {
            if (!EventQueue.isDispatchThread())
            {
                if (invokeLater)
                {
                    SwingUtilities.invokeLater(this);
                }
                else
                {
                    try
                    {
                        SwingUtilities.invokeAndWait(this);
                    } 
                    catch (InterruptedException e)
                    {
                        // ignore
                    } 
                    catch (InvocationTargetException e)
                    {
                        // ignore
                    }
                }
            }
            else
            {
                component.setCursor(cursor);
            }
        }
    }
    
}
