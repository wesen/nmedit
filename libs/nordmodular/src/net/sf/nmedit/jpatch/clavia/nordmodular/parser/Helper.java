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
package net.sf.nmedit.jpatch.clavia.nordmodular.parser;

import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jpatch.PComponent;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PRuntimeException;

public class Helper
{

    public static String pclass(PParameter parameter)
    {
        return pclass(parameter.getDescriptor());
    }

    public static String pclass(PParameterDescriptor parameter)
    {
        String clazz = parameter.getStringAttribute("class");
        if (clazz == null) throw new PRuntimeException("parameter has no attribute 'class':"+parameter);
        return clazz;
    }
    
    public static int index(PComponent component)
    {
        return index(component.getDescriptor());
    }
    
    public static int index(PDescriptor component)
    {
        int index = component.getIntAttribute("index", -1);
        if (index<0) throw new PRuntimeException("attribute index missing: "+index);
        return index;
    }
    
    public static PParameter getParameter(PModule module, String paramClass, int index)
    {
        Object o = getParameterClassMap(module, paramClass).get(index);
        if (o == null || (! (o instanceof PParameter)))
            throw new PRuntimeException("parameter[class="+paramClass+",index="+index+"] not found in "+module);
        return (PParameter) o;
    }
    
    public static int getParameterClassCount(PModule module, String name)
    {
        return getParameterClassMap(module, name).size();
    }

    public static Map getParameterClassMap(PModule module, String name)
    {
        String key = PDescriptor.CACHE_KEY_PREFIX+"parameter-map="+name;
        Object value = module.getAttribute(key);
        if (value instanceof Map) return (Map) value;
        Map map = new HashMap<Object, Object>(module.getParameterCount()/2);
        for (int i=module.getParameterCount()-1;i>=0;i--)
        {
            PParameter p = module.getParameter(i);
            if (name.equals(p.getAttribute("class")))
                map.put(index(p), p);
        }
        module.setAttribute(key, map);        
        return map;
    }

    public static int[] paramValues(PModule module, String name)
    {
        Map<Object, Object> map = getParameterClassMap(module, name);
        int[] data = new int[map.size()];
        for (int i=data.length-1;i>=0;i--)
        {
            data[i]=((PParameter)map.get(i)).getValue();
        }
        return data;
    }

}
