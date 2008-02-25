package net.sf.nmedit.jpatch;

import java.util.Collection;

/*
 * audio
 * in
 * out
 * level
 * mute
 * 
 * osc=oscillator
 * filter = filter
 * envelope
 * fx 
 * mixer
 * 
 * ui (affecting only the user interface)
 * assign (assignment of something to something else)
 */

/**
 * A set of strings defining the role of a component.
 */
public interface PRoles extends Collection<String>, Iterable<String>
{

    /**
     * Returns true if the set is empty.
     */
    boolean isEmpty();
    
    /**
     * Returns the number of strings in this set.
     */
    int size();
    
    /**
     * Computes the union of this and the specified set.
     * @param roles another set
     * @return the union of this and the specified set
     */
    PRoles union(PRoles roles);

    /**
     * Computes the difference of this and the specified set.
     * @param roles another set
     * @return the difference of this and the specified set
     */
    PRoles difference(PRoles roles);

    /**
     * Computes the intersection of this and the specified set.
     * @param roles another set
     * @return the intersection of this and the specified set
     */
    PRoles intersection(PRoles roles);

    /**
     * Returns true if this and the specified set intersect.
     * @param roles another set
     * @return true if this and the specified set intersect
     */
    boolean intersects(PRoles roles);
    
}
