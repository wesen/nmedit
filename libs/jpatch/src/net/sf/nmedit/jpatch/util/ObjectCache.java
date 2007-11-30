 package net.sf.nmedit.jpatch.util;

import java.util.Collections;
import java.util.List;

public abstract class ObjectCache<E>
{
    
    private int maxCapacity;
    
    private Object[] cache;
    
    public ObjectCache()
    {
        this(8);
    }

    public ObjectCache(int maxCapacity)
    {
        super();
        this.maxCapacity = maxCapacity;
        cache = new Object[maxCapacity*2];
    }
    
    protected int getMaxCapacity()
    {
        return maxCapacity;
    }

    @SuppressWarnings("unchecked")
    public List<E> getItems(ObjectFilter<E> filter)
    {
        // get id / compute hash value
        Object id = filter.getIdentifier();
        // 0x7fFfFfFf removes '-'-sign
        int hash = (id.hashCode()&0x7fFfFfFf) % maxCapacity;
        int hash2 = hash*2;
        int empty = -1;
        
        // lookup cached result
        for (int i=hash2;i<cache.length;i+=2)
        {
            Object _id = cache[i];
            if (_id == null) empty = i;
            else if (_id == id || _id.equals(id))
                return (List<E>) cache[i+1];
        }
        // not found yet
        for (int i=0;i<hash2;i+=2)
        {
            Object _id = cache[i];
            if (_id == null) empty = i;
            else if (_id == id || _id.equals(id))
                return (List<E>) cache[i+1];
        }
        // item not in cache
        // empty points to an empty filed, otherwise we let it point to the field hash2 and overwrite the current value
        if (empty<0) empty = hash2;
        // create filtered list
        List<E> filteredList = applyFilter(filter);
        if (filteredList.isEmpty())
            filteredList = Collections.<E>emptyList(); // use static Collections.EMPTY_LIST
        cache[empty] = id;
        cache[empty+1] = filteredList;
        return filteredList;
    }

    protected abstract List<E> applyFilter(ObjectFilter<E> filter);
    
}
