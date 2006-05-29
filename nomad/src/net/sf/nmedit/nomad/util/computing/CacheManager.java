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
package net.sf.nmedit.nomad.util.computing;

import java.util.HashMap;


/**
 * A CacheManager keeps track of objects that contain content that is costly to compute.
 * 
 * Usage: 
 * <code>CacheManager manager = new CacheManader();
 * CachedState computedState = null;
 * StateIdendity definition = createDefinition();
 * 
 * ... (where you make use of the cached object do)
 * 
 * computedState = manager.obtain(definition, computedState);
 * doSomething(computedState) </code> 
 * 
 * If you do not want to make use of the cached object any more do:
 * <code>computedState.unregister();
 * computedState=null;</code>
 * 
 * @author Christian Schneider
 * 
 * @see net.sf.nmedit.nomad.util.computing.CachedState
 * @see org.nomad.theme.cache.CacheModel
 * @see net.sf.nmedit.nomad.util.computing.StateIdendity
 */
public abstract class CacheManager {

	/**
	 * Contains paires CachedState cs:(cs,cs)
	 */
	private HashMap<Integer,CachedState> cache = new HashMap<Integer,CachedState>();
	
	public CacheManager() { }

	/**
	 * Returns a object that is in the same state as <code>expectedState</code> and contains the computed data.
	 * If <code>currentState</code> is <code>null</code>, or is not in the expected state a new <code>CachedState</code> object will be created
	 * by invoking <code>expectedState.newCacheObject()</code>.
	 * 
	 * If <code>currentState</code> has the same state as <code>expectedState</code>, <code>currentState</code> will be returned.
	 * The return value <code>result</code> will fullfill the formal condition <code>result.hasState(expectedState)==true</code>.
	 *
	 * If a new <code>CachedState</code> object is created and perhaps the old one is not needed any more, they will
	 * be (un-)registered to the manager.
	 * 
	 * Only if a new <code>CachedState</code> object is created it's <code>compute()</code> method will be invoked. 
	 * 
	 * @param expectedState
	 * 	 The expected state.
	 * 
	 * @param currentState
	 * 	 A <code>CachedState</code> object that either was previously obtained by this manager instance or is <code>null</code>.
	 *
	 * @return
	 * 	 <code>CachedState</code> object that is in the same state as <code>expectedState</code> and contains the computed data.
	 *
	 * @see CachedState#getReferenceCount()
	 * @see CachedState#unregister()
	 * @see CachedState#removeFromCache()
	 */
	protected CachedState obtain(Object expectedState, CachedState currentState) {
		if (currentState != null) {
			if (hasState(currentState, expectedState)) {
				return currentState;
			} else {
				unregister(currentState);
			}
		}

		Integer key = new Integer(computeHash(expectedState));
		CachedState co = cache.get(key);
		if (co == null) {
			co = newCacheObject(expectedState);
			co.compute();
			co.key = key;
		}
		register (co);
		
		return co;
	}

	/**
	 * Registers the state. If there are no references yet, the
	 * state will be put in the hash map.
	 * @param state the state to be registered.
	 */
	private void register(CachedState state) {
		if (state.referenceCount==0) {
			cache.put(state.key, state);
		}
		state.referenceCount++;
	}
	
	/**
	 * Unregisters the state. If there are no references to the state
	 * anymore, it will be removed from the hashmap.
	 * @param state
	 * @see #remove(CachedState)
	 */
	void unregister(CachedState state) {
		state.referenceCount--;
		if (state.referenceCount==0) {
			cache.remove(state.key);
		}
	}

	/**
	 * Removes the CachedState completely from the map. 
	 * @param state the state to be removed
	 * @see #unregister(CachedState)
	 */
	void remove(CachedState state) {
		cache.remove(state);
		state.referenceCount = 0;
	}

	/**
	 * Removes eache CachedObject from the map.
	 */
	public void reset() {
		final Integer nullKey = new Integer(0);
		for (CachedState state : cache.values()) {
			state.referenceCount = 0;
			state.key=nullKey;	
		}
		cache.clear();
	}

	public boolean hasState(CachedState cache, Object state) {
		return computeHash(state) == cache.hashCode();
	}
	
	protected abstract int computeHash(Object state);

	/**
	 * Returns a instance of CachedState that has the same state as this StateIdendity instance.
	 * The created instance's compute() operation must not be called.
	 * @return a new CachedState instance that has the same state as this StateIdendity instance
	 */
	public abstract CachedState newCacheObject(Object state);

}
