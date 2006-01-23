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
 * Created on Jan 22, 2006
 */
package org.nomad.util.computing;

/**
 * Base class for the classes <code>CachedState</code> and <code>StateIdendity</code> which
 * implements the formal conditions both classes have to fullfill.
 * 
 * The conditions are:
 * <p>Two CachedState instances must have the same hashcode if they are in the same state.</p>
 * <p>Two CachedState instances must be equal if they have the same hash code (otherwise they are not)</p>
 * 
 * @author Christian Schneider
 * @see org.nomad.util.computing.StateIdendity
 * @see org.nomad.util.computing.CachedState
 */
/**
 * A CachedState object is able to compute the data that should be cached.
 * 
 * @author Christian Schneider
 */
public abstract class CachedState {

	private CacheManager manager;
	Integer key;

	/**
	 * Returns the CacheManager
	 * @return the CacheManager
	 */
	protected CacheManager getManager() {
		return manager;
	}

	public boolean equals(Object obj) {
		return key.equals(obj);
	}

	/**
	 * Invokes the managers computeHash() method to calculate the hash for this object. 
	 */
	public int hashCode() {
		return key.intValue();
	}

	/**
	 * @see #getReferenceCount()
	 */
	int referenceCount = 0; // 0 also means not cached

	/**
	 * Creates a new CachedState instance that might be managed by manager.
	 * @param manager
	 */
	public CachedState(CacheManager manager) {
		this.manager = manager;
	}

	/**
	 * Unregisteres the current subscription to the manager.
	 */
	public void unregister() {
		if (referenceCount>0)
			getManager().unregister(this);
	}

	/**
	 * Removes this CachedState from the manager.
	 */
	public void removeFromCache() {
		if (referenceCount>0)
			getManager().remove(this);
	}

	/**
	 * Returns the value of the reference counter that indicates whether there
	 * are subscribers to this CachedState instance or not.
	 * 
	 * @return The number of subscribers to this CachedState or 0 if there are no subscribers.
	 */
	public int getReferenceCount() {
		return referenceCount;
	}

	/**
	 * Performs the objects computation and returns the hash key for this state
	 */
	protected abstract void compute();

	public String toString() {
		return "CachedState[references="+getReferenceCount()+",hash="+hashCode()+"]";
	}

	public Integer getKey() {
		return key;
	}
	
}
