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
package net.sf.nmedit.jtheme.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jtheme.annotation.ParameterBinding;

public class PropertyDatabase
{

    private PropertyExclusionFilter exclusionFilter;
    private Map<Class<?>, PropertyAccessGroup> dbMap = 
        new HashMap<Class<?>,PropertyAccessGroup>(100);
    
    public PropertyDatabase()
    {
        super();
    }
    
    public PropertyExclusionFilter getExclusionFilter()
    {
        return exclusionFilter;
    }
    
    public void setExclusionFilter(PropertyExclusionFilter filter)
    {
        if (exclusionFilter != filter)
        {
            dbMap.clear();
            this.exclusionFilter = filter;
        }
    }

    public PropertyAccessGroup getPropertyAccessGroup(Class<?> target)
    {
        PropertyAccessGroup group = dbMap.get(target);
        if (group == null)
        {
            group = getProperties(target);
            dbMap.put(target, group);
        }
        
        return group;
    }

    protected PropertyAccessGroup getProperties(Class<?> target)
    {
        Map<String, PropertyAccess> properties = new HashMap<String, PropertyAccess>(100);
        listProperties(target, properties);
        return new PropertyAccessGroup(target, properties);
    }

    private static final int VALID_MODIFIERS = Modifier.PUBLIC;
    private static final int INVALID_MODIFIERS = Modifier.ABSTRACT|Modifier.STATIC;

    protected void listProperties(Class<?> target, Map<String, PropertyAccess> properties)
    {
        Method[] methods = target.getMethods();
        Map<String,Method> getters = new HashMap<String,Method>(methods.length);
        Map<String,Method> setters = new HashMap<String,Method>(methods.length);
     
        for (int i=0;i<methods.length;i++)
        {
            Method m = methods[i];

            if ((m.getModifiers() & VALID_MODIFIERS) > 0 && ((m.getModifiers() & INVALID_MODIFIERS) == 0))
            {
                ParameterBinding pbinding = m.getAnnotation(ParameterBinding.class);

                if (pbinding != null)
                {
                    if (m.getReturnType() == Void.TYPE && m.getParameterTypes().length==1)
                    {
                        setters.put(pbinding.name(), m);
                    }
                    else if (m.getReturnType() != Void.TYPE && m.getParameterTypes().length == 0)
                    {
                        getters.put(pbinding.name(), m);
                    }
                }
                else
                {
                    String name = m.getName();
                    
                    if ( name.length()>3 && name.startsWith("set") )
                    {
                        if ( m.getReturnType() == Void.TYPE && m.getParameterTypes().length==1)
                        {
                            putSafely(setters, extractPropertyName(name, 3), m);
                        }
                    }
                    else if (m.getReturnType() != Void.TYPE && m.getParameterTypes().length==0)  
                    {
                        String n = m.getName();
                        if (name.length()>3 && n.startsWith("has")|n.startsWith("get"))
                        {
                            putSafely(getters, extractPropertyName(name, 3), m);
                        }
                        else if (name.length()>2 && n.startsWith("is"))
                        {
                            putSafely(getters, extractPropertyName(name, 2), m);
                        }
                    }
                }
            }
        }
        
        // now find getter/setter pairs
        
        for (String property : getters.keySet())
        {
            Method setter = setters.get(property);
            
            if (setter != null)
            {
                Method getter = getters.get(property);
                
                if (setter.getParameterTypes()[0]==getter.getReturnType())
                {
                    properties.put(property, new PropertyAccess(property, getter, setter));
                }
            }
        }
    }
    
    private void putSafely(Map<String, Method> map, String property, Method method)
    {
        if (exclusionFilter != null && exclusionFilter.exlusive(property))
            return;

        Method oldMethod = map.get(property);
        if (oldMethod != null && oldMethod.isAnnotationPresent(ParameterBinding.class))
        {
            return;
        }
        
        map.put(property, method);
    }

    private String extractPropertyName(String methodName, int prefixLen)
    {
        if (methodName.length() == prefixLen+1)
        {
            return Character.toString(Character.toLowerCase(methodName.charAt(prefixLen)));
        }
        
        if (Character.isUpperCase(methodName.charAt(prefixLen+1)))
            return methodName.substring(prefixLen);
        else
            return Character.toLowerCase(methodName.charAt(prefixLen))+methodName.substring(prefixLen+1);
    }
    
    public static final int RETURNTYPE_INVALID = -1;
    public static final int RETURNTYPE_VOID = 0;
    public static final int RETURNTYPE_HASRETURNTYPE = 1;
    
    public static int getReturnTypeInfo(Method method, Class<?> type)
    {
        Class<?> returnType = method.getReturnType();
        
        if (returnType == Void.TYPE)
            return RETURNTYPE_VOID;
        else if (returnType == type)
            return RETURNTYPE_HASRETURNTYPE;
        else
            return RETURNTYPE_INVALID;
    }

    public static final int PARAMETERLIST_INVALID = -1;
    public static final int PARAMETERLIST_NOPARAMETER = 0;
    public static final int PARAMETERLIST_HASPARAMETER = 1;
    
    public static int getParameterInfo(Method method, Class<?> type)
    {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 0)
            return PARAMETERLIST_NOPARAMETER;
        else if (paramTypes.length == 1 && paramTypes[0] == type) 
            return PARAMETERLIST_HASPARAMETER;
        else
            return PARAMETERLIST_INVALID;
    }
 
    /*
    // for testing this class
    public static void main(String[] args)
    {
        PropertyDatabase pd = new PropertyDatabase();
        PropertyAccessGroup group = pd.getPropertyAccessGroup(TestClass.class);
        System.out.println(group.getTarget()+":");
        for (PropertyAccess pa : group)
        {
            System.out.println(pa);
        }
    }
    
    public static class TestClass 
    {
        public static int getShouldBeIgnored()
        {
            return 0;
        }
        
        public static void getShouldBeIgnored(int inte)
        { }
        
        @ParameterBinding(name="silly")
        public int getSillyGetterName()
        {
            return 0;
        }

        @ParameterBinding(name="silly")
        public void setSillySetterName(int s)
        { }
     
        // this should not overwrite setSillySetterName(int)
        public void setSilly(int silly)
        {
            // 
        }
        
        public boolean isPropertyAEnabled()
        {
            return false;
        }
        
        public void setPropertyAEnabled(boolean e)
        {
            
        }
        
    }
    */
}

