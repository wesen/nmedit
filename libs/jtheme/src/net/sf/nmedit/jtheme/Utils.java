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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static List<Method> getSettersList(Class<?> clazz)
    {
        List<Method> setters = new ArrayList<Method>();
        
        for (Method m : clazz.getMethods())
        {
            if (isSetter(m))
                setters.add(m);
        }
        return setters;
    }

    public static List<Method> getGettersList(Class<?> clazz)
    {
        List<Method> getters = new ArrayList<Method>();
        
        for (Method m : clazz.getMethods())
        {
            if (isGetter(m))
                getters.add(m);
        }
        return getters;
    }
    
    public static Method[] getSetters(Class<?> clazz)
    {
        List<Method> setters = getSettersList(clazz);
        return setters.toArray(new Method[setters.size()]);
    }
    
    public static Method[] getGetters(Class<?> clazz)
    {
        List<Method> getters = getGettersList(clazz);
        return getters.toArray(new Method[getters.size()]);
    }

    public static int commonSubstringScore(String a, String b)
    { 
        String sub =maxCommonSubsequence(a, b); 
        int score =  sub == null ? 0 : sub.length();
        if (score>0)
        {
            // score higher when both have same size
            if (a.length()==b.length())
                score+=1;
        }

        return score;
    }
    
    private static String maxCommonSubsequence(String a, String b)
    {
        if (a.length()<b.length())
        {
            String c = a;
            a = b;
            b = c;
        }
        
        /*
        int score = 0;
        for (int i=0;i<b.length()-1 && b.length()-i>score;i++)
        {
            for (int j=b.length()-i;j>score;j--)
            {
                if (a.contains(b.substring(i, i+j)))
                    score = j;
            }
        }*/
        
        String s = null;
        for (int i=0;i<b.length();i++)
            if (a.endsWith(s=b.substring(i)))
                break;
        return s;
    }
    
    public static int getterSetterMatch( Method getter, Method setter )
    {
        if (!isGetter(getter))
            return 0;
        if (!isSetter(setter))
            return 0;

        Class<?> retType = getter.getReturnType();
        Class<?> setType = setter.getParameterTypes()[0];
        
        if (!retType.equals(setType))
            return 0;

        String gn = getter.getName();
        String sn = setter.getName();
        if (gn.startsWith("set") || sn.startsWith("get"))
            return 0;
        /*
        if ((!gn.startsWith("get")) || (!sn.startsWith("set")))
            return 0;*/

        return commonSubstringScore(gn, sn);
    }
    
    public static Map<Method, Method> getSettersParts(Class<?> clazz, Method[] getters)
    {
        Map<Method,Method> gsMap = new HashMap<Method, Method>(); // getters => setters
        Set<Method> setters = new HashSet<Method>(getSettersList(clazz));

        for (Method m : getters)
        {
            Method setter = null;
            int score = 0;
            for (Method s : setters)
            {
                int nscore = getterSetterMatch(m, s); 
                
                if (nscore>score&&nscore>1)
                {
                    setter = s;
                    score = nscore;
                }
            }
            
            if (score>0)
            {
                // now we have to check if there is not a better choice setter=>getter
                boolean bestChoice = true;
                for (Method g : getters)
                    if ((g!=m)&&getterSetterMatch(g, setter)>score)
                    {
                        // there is a better combination - we will wait
                        bestChoice = false;
                        break;
                    }
                
                if (bestChoice)
                {
                    setters.remove(setter);
                    gsMap.put(m, setter);
                }
            }
        }
        
        return gsMap;
    }
    
    private static Map<Class,Map<Method,Method>> ClassgsMap = new HashMap<Class,Map<Method,Method>>();
    
    public static Map<Method, Method> getGettersAndSetters(Class<?> clazz)
    {
        Map<Method,Method> gsMap = ClassgsMap.get(clazz);
        if (gsMap!=null)
            return gsMap;
        
        Method[] getters = getGetters(clazz);
        gsMap = Collections.unmodifiableMap(Utils.getSettersParts(clazz, getters));
        ClassgsMap.put(clazz, gsMap);
        return gsMap;
    }
    /*
    private static Map<String,Method[]> propertyAccess = new HashMap<String,Method[]>();
    
    private static Class[] NOARGS = new Class[0]; 

    public static Method[] getPropertyAccess(Class<?> clazz, String propertyName)
        throws SecurityException, NoSuchMethodException
    {
        String accessName = clazz.getName()+"#"+propertyName;
        Method[] gs = propertyAccess.get(accessName);
        if (gs!=null&&gs[0]!=null&&gs[1]!=null)
            return gs;
        
        Map<Method,Method> gsMap = getGettersAndSetters(clazz);
        Method g = clazz.getMethod("get"+propertyName, NOARGS);
        
    }*/
    
    private static Map<Class<?>, Map<String,Property>>
    properties = new HashMap<Class<?>,Map<String,Property>>();
    
    public static Map<String, Property> getProperties(Class<?> clazz)
    {
        Map<String,Property> pmap = properties.get(clazz);
        if (pmap!=null)
            return pmap;

        pmap = new HashMap<String, Property>();
        Map<Method, Method> gs = getGettersAndSetters(clazz);
        for (Method g : gs.keySet())
        {
            Method s = gs.get(g);
            
            String ng = g.getName();
            String ns = s.getName();

            if (ng.startsWith("get"))
                ng = ng.substring(3);
            else if (ng.startsWith("is"))
                ng = ng.substring(2);
            if (ns.startsWith("set"))
                ns = ns.substring(3);
            else if (ns.startsWith("has"))
                ns = ns.substring(3);
            String propertyName = maxCommonSubsequence(ng, ns);
            propertyName = propertyName.toLowerCase();
            pmap.put(propertyName, new Property(propertyName, g, s));
        }
        properties.put(clazz, pmap);
        return Collections.unmodifiableMap(pmap);
    }
    
}
