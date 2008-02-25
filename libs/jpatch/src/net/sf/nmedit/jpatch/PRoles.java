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

    boolean isEmpty();
    
    int size();
    
    PRoles union(PRoles roles);

    PRoles difference(PRoles roles);
    
    PRoles intersection(PRoles roles);

    boolean intersects(PRoles roles);
    
}
