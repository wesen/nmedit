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
 * Created on Jan 3, 2006
 */
package org.nomad.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A class that manages ImageBuffer instances that share their image with others.
 * 
 * @see org.nomad.image.ImageBuffer 
 * @author Christian Schneider
 */
public class PersistenceManager {

	/**
	 * Containes the tupels (Object key, ArrayList) where ArrayList contains
	 * ImageBuffer instances each containing the same valid image.
	 */
	private HashMap map = new HashMap();
	
	/**
	 * @param key Key to identify the list.
	 * @return a list containing ImageBuffer instances which were registered with the given key 
	 */
	ArrayList getList(Object key) {
		if (map.containsKey(key))
			return (ArrayList) map.get(key);
		else
			return null;
	}

	/**
	 * Registers the ImageBuffer instance using its key property.
	 * @param buffer the ImageBuffer to register.
	 * @throws NullPointerException the ImageBuffer instance's getKey() returns null.
	 * @throws IllegalStateException the ImageBuffer is already registered with the given key
	 */
	void register(ImageBuffer buffer) {
		Object theKey = buffer.getKey();
		
		if (theKey==null)
			throw new NullPointerException("Cannot register:"+theKey+" is not a valid key");

		ArrayList subscriberList = getList(theKey);
		if (subscriberList==null) {
			subscriberList = new ArrayList();
			subscriberList.add(buffer);
			map.put(theKey, subscriberList);
		} else if(!subscriberList.contains(buffer)) {
			subscriberList.add(buffer);
		} else {
			throw new IllegalStateException("Element already registered.");
		}
	}
	
	/**
	 * Unregisters ImageBuffer instance using its key property.
	 * @param buffer the ImageBuffer to unregister.
	 * @throws NullPointerException the ImageBuffer instance's getKey() returns null.
	 * @throws NoSuchElementException the ImageBuffer is not registered with the given key
	 */
	void unregister(ImageBuffer buffer) {
		Object theKey = buffer.getKey();
		if (theKey==null)
			throw new NullPointerException("Cannot unregister:"+theKey+" is not a valid key");
		
		ArrayList subscriberList = getList(theKey);
		if ((subscriberList!=null) && (subscriberList.contains(buffer))) {
			subscriberList.remove(buffer);
			if (subscriberList.size()==0)
				map.remove(theKey);
		} else {
			throw new NoSuchElementException();
		}
	}
	
	/**
	 * @param key Identifier
	 * @return true, if at least one ImageBuffer is registered with the key. 
	 */
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	
	/**
	 * @param key Identifier for the ImageBuffer instance
	 * @return the first ImageBuffer instance that is registered with given key
	 * 	or null of no instance is registered
	 */
	public ImageBuffer get(Object key) {
		ArrayList subscriberList = getList(key);
		return (subscriberList==null) ? null : (ImageBuffer) subscriberList.get(0);
	}

	/**
	 * Note the returned iterators remove() method will not remove any registered ImageBuffer.
	 * You have to use the ImageBuffer's setInvalid() method to remove it from the manager. 
	 * @param key Identifier
	 * @return iterator that iterates over all ImageBuffer instances that are
	 * registered with the given key.
	 */
	public Iterator getSubscribers(Object key) {
		// we use a copy of the list
		ArrayList subscriberList = new ArrayList(getList(key));
		if (subscriberList==null) {
			return new Iterator() {
				public boolean hasNext(){	return false; }
				public Object next()	{	throw new NoSuchElementException(); }
				public void remove()	{	throw new UnsupportedOperationException(); }
			};
		} else return subscriberList.iterator();
	}

	/**
	 * @return set containing all registered keys. For each key at least on ImageBuffer is registered.
	 */
	public Set getKeys() {
		return map.keySet();
	}
	
	/**
	 * Sets all ImageBuffer instances invalid flag which are registered with the given key.
	 * This implies, that each of them will be removed from the manager. 
	 * @param key Identifier
	 */
	public void setAllInvalid(Object key) {
		for (Iterator iter = getSubscribers(key); iter.hasNext(); ((ImageBuffer) iter.next()).dispose());
	}
	
	/**
	 * Sets all ImageBuffer instances invalid flag. This implies, that each of them will be removed from the manager.
	 */
	public void setAllInvalid() {
		for (Iterator iter = getKeys().iterator();iter.hasNext(); setAllInvalid(iter.next()));
	}
	
}
