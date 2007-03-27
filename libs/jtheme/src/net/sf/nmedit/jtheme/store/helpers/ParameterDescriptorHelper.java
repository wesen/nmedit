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
package net.sf.nmedit.jtheme.store.helpers;

import net.sf.nmedit.jpatch.InvalidDescriptorException;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.ParameterDescriptor;

import org.jdom.Attribute;
import org.jdom.Element;

public final class ParameterDescriptorHelper
{
    
    private static final ParameterDescriptorHelper INVALID
        = new ParameterDescriptorHelper(-1, null);

    private int paramIndex;
    private String paramClass;

    private transient ModuleDescriptor cachedModuleDescriptor;
    private transient ParameterDescriptor cachedParameterDescriptor;
    
    public ParameterDescriptorHelper(int paramIndex, String paramClass)
    {
        this.paramIndex = paramIndex;
        this.paramClass = paramClass;
    }
    
    public int getParameterIndex()
    {
        return paramIndex;
    }
    
    public String getParameterClass()
    {
        return paramClass;
    }
    
    public boolean hasParameterClassValue()
    {
        return paramClass != null;
    }
    
    public boolean isValid()
    {
        return paramIndex >= 0;
    }
    
    public ParameterDescriptor lookup(ModuleDescriptor moduleDescriptor)
    {
        if (cachedModuleDescriptor != moduleDescriptor)
        {
            if (!isValid())
                return null;
            
            ParameterDescriptor parameterDescriptor = hasParameterClassValue()
                        ? moduleDescriptor.getParameter(paramIndex, paramClass)
                        : moduleDescriptor.getParameter(paramIndex);
    
            cachedModuleDescriptor = moduleDescriptor;
            cachedParameterDescriptor = parameterDescriptor;
        }
        return cachedParameterDescriptor;
    }
    
    public Parameter lookup(Module module)
    {
        ParameterDescriptor descriptor = lookup(module.getDescriptor());
        if (descriptor == null)
            return null;

        try
        {
            return module.getParameter(descriptor);
        }
        catch (InvalidDescriptorException e)
        {
        	System.out.println(e);
            return null;
        }
    }
    
    public static ParameterDescriptorHelper invalidHelper()
    {
        return ParameterDescriptorHelper.INVALID;
    }

    public static ParameterDescriptorHelper createHelper(Element parameterElement)
    {
        if (parameterElement == null)
            return invalidHelper();
        
        // read the index
        Attribute indexAtt = parameterElement.getAttribute("index");
        if (indexAtt == null)
            return invalidHelper();
        int index = str2int(indexAtt.getValue());
        if (index < 0)
            return invalidHelper();
        // read the parameter class
        String paramClass = null;
        Attribute pclassAtt = parameterElement.getAttribute("class");
        if (pclassAtt != null)
            paramClass = pclassAtt.getValue();
        
        return new ParameterDescriptorHelper(index, paramClass);
    }

    private static int str2int(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NullPointerException e)
        {
            return -1;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
    
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof ParameterDescriptorHelper))
            return false;
        
        ParameterDescriptorHelper _o = (ParameterDescriptorHelper) o;
        
        return (getParameterIndex() == _o.getParameterIndex())
            && ((getParameterClass() == _o.getParameterClass()) ||
                    (getParameterClass() != null && (getParameterClass().equals(_o.getParameterClass()))));
    }
    
    public int hashCode()
    {
        return paramIndex + (paramClass == null ? 0 : paramClass.hashCode());
    }
    
}
