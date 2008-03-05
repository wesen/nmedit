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

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import net.sf.nmedit.jpatch.PComponent;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PRuntimeException;
import net.sf.nmedit.jpatch.util.ObjectFilter;

public class Helper
{
    
    private static class ParameterClassFilter implements ObjectFilter<PParameter>, Comparator<PParameter>,
        Serializable
    
    {
        /**
         * 
         */
        private static final long serialVersionUID = 7277838392533967421L;
        private String classname;
        
        private ParameterClassFilter(String classname)
        {
            this.classname = classname;
        }
        public boolean accepts(PParameter o)
        {
            return classname.equals(o.getAttribute("class"));
        }

        public Object getIdentifier()
        {
            return this;
        }
        
        public int hashCode()
        {
            return classname.hashCode();
        }
        
        public boolean equals(Object o)
        {
            if (o == null) return false;
            ParameterClassFilter f2;
            try
            {
                f2 = (ParameterClassFilter) o;
            }
            catch(ClassCastException e)
            {
                return false;
            }
            return f2.classname.equals(classname);
        }
        
        public String toString()
        {
            return getClass().getName()+"[class="+classname+"]";
        }
        
        public int compare(PParameter a, PParameter b)
        {
            return a.getComponentIndex() - b.getComponentIndex();
        }

        private void writeObject(java.io.ObjectOutputStream out)
            throws IOException
        {
            out.defaultWriteObject();
        }
        
        private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException
        {
            in.defaultReadObject();
        }
        
    }

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
        List<PParameter> list = getParametersByClass(module, paramClass);
        if (index<0 || index>list.size())
            throw new PRuntimeException("parameter[class="+paramClass+",index="+index+"] not found in "+module);
        return list.get(index);
    }
    
    public static int getParameterClassCount(PModule module, String name)
    {
        return getParametersByClass(module, name).size();
    }

    public static List<PParameter> getParametersByClass(PModule module, String name)
    {
        return module.getParameters(new ParameterClassFilter(name));
    }

    public static int[] paramValues(PModule module, String name)
    {
        List<PParameter> list = getParametersByClass(module, name);
        int[] data = new int[list.size()];
        ListIterator<PParameter> iter = list.listIterator();
        while (iter.hasNext())
            data[iter.nextIndex()] = iter.next().getValue();
        return data;
    }

}
