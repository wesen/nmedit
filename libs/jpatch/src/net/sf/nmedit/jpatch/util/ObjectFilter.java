package net.sf.nmedit.jpatch.util;

public interface ObjectFilter<T>
{
    
    /**
     * Returns an object which identifies this filter and it's configuration.
     * This allows caching the filtered results.
     * 
     * If the filter does not depend on parameters than the filter class
     * should be returned.
     */
    Object getIdentifier();

    /**
     * Returns true if the specified object is accepted or false otherwise.
     */
    boolean accepts(T o);

}
