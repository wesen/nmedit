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
package net.sf.nmedit.jtheme.component.misc;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.jtheme.component.JTComponent;

public class CallDescriptor
{

    private String component;
    private String method;
    private JTComponent owner;
    private transient JTComponent target;
    private transient Method tmethod;

    public CallDescriptor(JTComponent owner, String component, String method)
    {
        this.component = component;
        this.method = method;
        this.owner = owner;
    }
    
    private JTComponent getTarget()
    {
        if (target == null)
        {
            Container c = owner.getParent();
            if (c != null)
            {
                for (int i=c.getComponentCount()-1;i>=0;i--)
                {
                    Component t = c.getComponent(i);
                    if (t instanceof JTComponent && component.equals(t.getName()))
                    {
                        target = (JTComponent) t;
                        break;
                    }
                }
            }
        }
        return target;
    }
    
    private Method getMethod(JTComponent target)
    {
        if (tmethod == null)
        {
            try
            {
                tmethod = target.getClass().getDeclaredMethod(method, new Class[0]);
            }
            catch (Exception e)
            {
                Log l = LogFactory.getLog(getClass());
                if (l.isDebugEnabled())
                    l.debug("Method not found: "+method, e);
            }
        }
        return tmethod;
    }

    public void call()
    {
        JTComponent t = getTarget();
        if (t == null) return;
        Method m = getMethod(t);
        if (m == null) return;
        try
        {
            m.invoke(t, new Object[0]);
        }
        catch (Exception e)
        {
            Log l = LogFactory.getLog(getClass());
            if (l.isDebugEnabled())
                l.debug("call failed on method: "+method, e);
        }
    }
    
    public String toString()
    {
        return getClass().getName()+"[component="+component+",method="+method+"]";
        
    }
    
}
