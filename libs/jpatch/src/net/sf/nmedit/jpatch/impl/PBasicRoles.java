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
package net.sf.nmedit.jpatch.impl;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import net.sf.nmedit.jpatch.PRoles;

public class PBasicRoles extends AbstractImmutableCollection<String> implements PRoles
{
    
    private static EMPTY EMPTY = new EMPTY();
    private Set<String> set;
    
    private PBasicRoles(Set<String> set)
    {
        this.set = set;
    }
    
    public static PRoles empty()
    {
        return PBasicRoles.EMPTY;
    }

    public static String toString(PRoles roles)
    {
        if (roles.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
     
        int index = 0;
        for (String s: roles)
        {
            if (index>0)
                sb.append(",");
            sb.append(s);
            index++;
        }
        return sb.toString();
    }
    
    public static PRoles parseRoles(String roles)
    {
        if (roles.indexOf(',')<0)
        {
            roles = roles.trim();
            if (roles.length() == 0) return EMPTY;
            else return PBasicRoles.setOf(roles); // single role
        }
        String[] splitted = roles.split(",");

        Set<String> set = new HashSet<String>(splitted.length);
        for (String s: splitted)
        {
            s = s.trim();
            if (s.length()>0)
                set.add(s.toLowerCase());
        }
        if (set.isEmpty())
            return EMPTY;
        else
            return PBasicRoles.wrap(set);
    }
    
    public static PRoles setOf(String role)
    {
        if (role == null)
            throw new NullPointerException();
        return new PBasicRoles(new SingleElementSet<String>(role.toLowerCase()));
    }
    
    public static PRoles setOf(Collection<String> roles)
    {
        if (roles.isEmpty()) return PBasicRoles.EMPTY;
        Set<String> newSet = new HashSet<String>(roles.size());
        for(String s: roles)
        {
            newSet.add(s.toLowerCase());
        }
        return new PBasicRoles(newSet);        
    }

    public static PRoles setOf(String ... roles)
    {
        if (roles.length==0) return PBasicRoles.EMPTY;
        Set<String> set = new HashSet<String>(roles.length);
        for (String role: roles)
        {
            if (role == null)
                throw new NullPointerException();
            set.add(role.toLowerCase());
        }
        return new PBasicRoles(set);
    }

    private static PRoles wrap(Set<String> set)
    {
        if (set.isEmpty()) return PBasicRoles.EMPTY;
        return new PBasicRoles(set);
    }

    public boolean containsAll(Collection<?> c)
    {
        return set.containsAll(c);
    }
    
    public boolean contains(Object o) 
    {
        return set.contains(o);
    }
    
    public int size()
    {
        return set.size();
    }

    public boolean isEmpty()
    {
        // we can guarantee that the set is not empty
        return false;
    }

    public Iterator<String> iterator()
    {
        return new Iterator<String>()
        {
            private Iterator<String> iter = set.iterator();
            
            public boolean hasNext()
            {
                return iter.hasNext();
            }

            public String next()
            {
                return iter.next();
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public boolean intersects(PRoles roles)
    {
        if (roles.isEmpty()) return false;
        for (String role: roles)
            if (set.contains(role))
                return true;
        return false;
    }

    public PRoles difference(PRoles roles)
    {
        if (roles.isEmpty()) return this;
        if (roles == this) return PBasicRoles.EMPTY;
        if (!intersects(roles)) return this;
        Set<String> newSet = new HashSet<String>(set);
        newSet.removeAll(roles);
        return wrap(newSet);
    }

    public PRoles intersection(PRoles roles)
    {
        if (roles.isEmpty()) return EMPTY;
        if (roles == this) return this;
        if (!intersects(roles)) return EMPTY;
        Set<String> newSet = new HashSet<String>(set);
        newSet.retainAll(roles);
        return wrap(newSet);
    }

    public PRoles union(PRoles roles)
    {
        if (roles.isEmpty() || roles==this) return this;
        if (equals(roles)) return this;
        Set<String> newSet = new HashSet<String>(set);
        newSet.retainAll(roles);
        return wrap(newSet);
    }
 
    public Object[] toArray() 
    {
        return set.toArray();
    }

    public <T> T[] toArray(T[] a) 
    {
        return set.toArray(a);
    }

    public void clear() 
    {
        throw new UnsupportedOperationException();
    }
    
    public int hashCode()
    {
        return set.hashCode();
    }
    
    public boolean equals(Object o)
    {
        if (o == null) return false;
        try
        {
            PRoles roles = (PRoles)o;
            if (roles.isEmpty()) return false;
            else if (roles.size() != size()) return false;
            else return set.containsAll(roles);
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }
    
    public String toString()
    {
        return "Roles("+PBasicRoles.toString(this)+")";
    }
    
    private static class EMPTY extends AbstractImmutableCollection<String> implements PRoles
    {

        private Set<String> EMPTY_SET = Collections.<String>emptySet();
        
        public PRoles difference(PRoles roles)
        {
            return this;
        }

        public PRoles intersection(PRoles roles)
        {
            return this;
        }

        @Override
        public boolean isEmpty()
        {
            return true;
        }

        public PRoles union(PRoles roles)
        {
            return roles;
        }

        @Override
        public Iterator<String> iterator()
        {
            return EMPTY_SET.iterator();
        }

        @Override
        public int size()
        {
            return 0;
        }
        
        public int hashCode()
        {
            return 0;
        }
        
        public boolean equals(Object o)
        {
            if (o == null) return false;
            try
            {
                return ((PRoles)o).isEmpty();
            }
            catch (ClassCastException e)
            {
                return false;
            }
        }

        public boolean intersects(PRoles roles)
        {
            return false;
        }

        public String toString()
        {
            return "Roles("+PBasicRoles.toString(this)+")";
        }
    }
    
    private static class SingleElementSet<E> extends AbstractSet<E>
    {

        private E element;
        
        public SingleElementSet(E element)
        {
            this.element = element;
        }

        public boolean add(E e)
        {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> c)
        {
            throw new UnsupportedOperationException();
        }

        public void clear()
        {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object o)
        {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c)
        {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c)
        {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Iterator<E> iterator()
        {
            return new Iterator<E>()
            {
                
                E e = element;

                public boolean hasNext()
                {
                    return e != null;
                }

                public E next()
                {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    E next = e;
                    e = null;
                    return next;
                }

                public void remove()
                {
                    throw new UnsupportedOperationException();
                }
                
            };
        }

        @Override
        public int size()
        {
            return 1;
        }
        
    }
    
}
