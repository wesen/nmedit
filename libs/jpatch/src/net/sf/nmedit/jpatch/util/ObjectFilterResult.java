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
