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
package net.sf.nmedit.jpatch.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ObjectFilterResult
{

    public static <T> List<T> filter(Collection<T> src, ObjectFilter<T> filter)
    {
        if (src.isEmpty())
            return Collections.<T>emptyList();
        List<T> list = new ArrayList<T>(src.size());
        boolean empty = true;
        for (T t: src)
            if (filter.accepts(t))
            {
                empty = false;
                list.add(t);
            }
        if (empty) return Collections.<T>emptyList();
        if (filter instanceof Comparator)
        {
            Comparator<T> cmp = (Comparator<T>) filter;
            Collections.sort(list, cmp);
        }
        return list;
    }

    public static <T> List<T> filter(T[] src, ObjectFilter<T> filter)
    {
        if (src.length == 0)
            return Collections.<T>emptyList();
        // src array will not be modified
        return filter(Arrays.asList(src), filter);
    }

}
