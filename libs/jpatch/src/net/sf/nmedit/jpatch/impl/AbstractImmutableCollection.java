package net.sf.nmedit.jpatch.impl;

import java.util.AbstractCollection;
import java.util.Collection;

public abstract class AbstractImmutableCollection<E> extends AbstractCollection<E> implements Collection<E>
{

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

}
