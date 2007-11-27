/* Copyright (C) 2006-2007 Christian Schneider
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
package net.sf.nmedit.jtheme.store2;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControlAdapter;

/**
 * Caches all methods void (JTControlAdapter) of a specified class
 * which are annotated by BindParameter.
 * 
 * @see BindParameter
 * @author Christian Schneider
 */
public class BindParameterInfo implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 5053594052110313904L;

    private static SoftReference<Map<Class<? extends JTComponent>, BindParameterInfo>> mapref = new SoftReference<Map<Class<? extends JTComponent>,BindParameterInfo>>(null);

    private transient Map<String, Method> adapterSetters;
    private transient Class<? extends JTComponent> jtclass;
    
    protected BindParameterInfo(Class<? extends JTComponent> jtclass)
    {
        this.jtclass = jtclass;
    }
    
    protected void lazyInitialisation()
    {
        if (adapterSetters != null)
            return;
        
        for (Method m: jtclass.getMethods())
        {   
            if (Void.TYPE.equals(m.getReturnType()) && m.isAnnotationPresent(BindParameter.class))
            {
                Class<?>[] params = m.getParameterTypes();
                if (params.length == 1 && JTControlAdapter.class.equals(params[0]))
                {
                    // arguments ok
                    if (adapterSetters == null)
                        adapterSetters = new HashMap<String, Method>();
                    
                    BindParameter binding = m.getAnnotation(BindParameter.class);
                    
                    adapterSetters.put(binding.name(), m);
                }
            }
        }
    }
    
    public int getAdapterCount()
    {
        lazyInitialisation();
        return adapterSetters.size();
    }
    
    public Iterator<String> parameters()
    {
        lazyInitialisation();
        return adapterSetters.keySet().iterator();
    }
    
    public Method getSetter(String parameterName)
    {
        lazyInitialisation();
        return adapterSetters.get(parameterName);
    }
    
    private static Map<Class<? extends JTComponent>, BindParameterInfo> getMap()
    {
        Map<Class<? extends JTComponent>, BindParameterInfo> map = mapref.get();
        if (map == null)
        {
            map = new HashMap<Class<? extends JTComponent>, BindParameterInfo>();
            mapref = new SoftReference<Map<Class<? extends JTComponent>,BindParameterInfo>>(map);
        }
        return map;
    }
    
    public static BindParameterInfo forClass(Class<? extends JTComponent> jtclass)
    {
        Map<Class<? extends JTComponent>, BindParameterInfo> map = getMap();
        BindParameterInfo info = map.get(jtclass);
        if (info == null)
        {
            info = new BindParameterInfo(jtclass);
            map.put(jtclass, info);
        }
        return info;
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        out.writeObject(jtclass);
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        jtclass = (Class<? extends JTComponent>) in.readObject();
        getMap().put(jtclass, this);
    }
}
