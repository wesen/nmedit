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
package net.sf.nmedit.jpatch;

import java.awt.Point;
import java.util.Collection;

public interface MoveOperation extends Iterable<PModule>
{
	void setDestination(PModuleContainer destination);
	
	PModuleContainer getDestination();

    boolean add(PModule module);
    
    boolean remove(Object o);
    
    int size();
    
    boolean isEmpty();
    
    boolean contains(Object o);
    
    PModule[] toArray();
   
    Collection<? extends PModule> toCollection();
    
    void addAll(Collection<? extends PModule> modules);
    
    <T extends PModule> void addAll(T[] modules);

    void setScreenOffset(Point offset);

    void setScreenOffset(int x, int y);
    
    Point getScreenOffset();
    
    Point getScreenOffset(Point dst);
    
    void move();
    
    Collection<? extends PModule> getMovedModules();
    
}
