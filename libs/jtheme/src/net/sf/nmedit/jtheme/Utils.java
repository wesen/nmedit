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
 * Created on Sep 8, 2006
 */
package net.sf.nmedit.jtheme;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Utils
{

    public static boolean isReturnTypeVoid(Method m)
    {
        return Void.TYPE.isAssignableFrom(m.getReturnType()); 
    }
    
    public static boolean isSetter(Method m)
    {
        return 
        (m.getParameterTypes().length==1) // no arguments, any return type
        && isReturnTypeVoid(m) // void return type
        ;
    }
    
    public static boolean isGetter(Method m)
    {
        return 
        (m.getParameterTypes().length==0) // no arguments 
        && !isReturnTypeVoid(m) // no void return type
        ;
    }

    private static String getterSetterMatch( Method getter, Method setter )
    {
        Class<?> retType = getter.getReturnType();
        Class<?> setType = setter.getParameterTypes()[0];
        
        if (!setType.isAssignableFrom(retType))
            return null;
        
        String propertyName = null;
        
        String gn = getter.getName();
        String sn = setter.getName();
        if (gn.startsWith("get") && sn.startsWith("set"))
        {
            gn = gn.substring(3);
            sn = sn.substring(3);
            if (gn.equals(sn))
            {
                return propertyName = gn;
            }
        }
        
        return propertyName;
    }
    
    private static Map<Class<?>,Map<String, Property>>
        registeredProperties = new HashMap<Class<?>,Map<String,Property>>();
    
    public static Map<String,Property> getProperties( Class<?> c )
    {
        Map<String,Property> properties = registeredProperties.get(c);
        if (properties==null)
        {
            properties = new HashMap<String,Property>();
            addProperties(properties, c);
            registeredProperties.put(c, properties);
        }
        return properties;
    }

    private static void addProperties( Map<String, Property> map, Class<?> c )
    {
        Method[] methods = c.getMethods();
        for (int i=0;i<methods.length;i++)
        {
            Method m = methods[i];
            if (m!=null)
            {
                methods[i] = null;

                PropertyName am = m.getAnnotation(PropertyName.class);
                if (am!=null)
                {
                    Method g = null;
                    Method s = null;
    
                    if (isGetter(m)) g = m; 
                    else if (isSetter(m)) s = m; 
                    else throw new RuntimeException("method "+m+" is neither a getter nor a setter");
                    
                    // find
                    for (int j=i+1;j<methods.length;j++)
                    {
                        Method m2 = methods[j];
                        if (m2!=null)
                        {
                            PropertyName am2 = m2.getAnnotation(PropertyName.class);
                            if (am2.name().equals(am.name()))
                            {
                                // we found the second annotation
                                if (g!=null)
                                {
                                    if (!isSetter(m2))
                                        throw new RuntimeException("method "+m2+" is not a setter for "+m);
                                    s = m2;
                                }
                                else if (s!=null)
                                {
                                    if (!isGetter(m2)) 
                                        throw new RuntimeException("method "+m2+" is not a getter for "+m);
                                    g = m2;
                                }
                                methods[j]=null;
                                j = methods.length; // abort loop
                                break;
                            }
                        }
                    }
                    
                    if (g==null||s==null)
                        throw new RuntimeException("second annotation not found for method "+m);
                    
                    Property p = new Property(am.name().toLowerCase(), g, s);
                    map.put(p.getPropertyName(), p);
                }
                else
                {
                    Method g = null;
                    Method s = null;
                    String propertyName = null;
    
                    if (isGetter(m))
                    {
                        g = m;
                        for (int j=i+1;j<methods.length;j++)
                        {
                            Method m2 = methods[j];
                            if (m2!=null && isSetter(m2) && (propertyName=getterSetterMatch(g, m2))!=null
                                    && m2.getAnnotation(PropertyName.class)==null)
                            {
                                s = m2;
                                methods[j] = null;
                                break ;
                            }
                        }
                    }
                    else if (isSetter(m))
                    {
                        int choice = -1;
                        s = m;
                        for (int j=i+1;j<methods.length;j++)
                        {
                            Method m2 = methods[j];
                            if (m2!=null && isGetter(m2) && (propertyName=getterSetterMatch(m2, s))!=null
                                    && m2.getAnnotation(PropertyName.class)==null)
                            {
                                g = m2;
                                methods[j] = null;
                                break ;
                            }
                        }

                        if (choice>=0)
                            methods[choice] = null;
                    }
                    
                    if (propertyName!=null) // implies g!=null && s!=null
                    {
                        Property p = new Property(propertyName.toLowerCase(), g, s);
                        map.put(p.getPropertyName(), p);
                    }
                }
            }
        }
    }

}
