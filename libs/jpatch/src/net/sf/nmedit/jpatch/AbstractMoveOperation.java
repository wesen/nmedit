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
import java.util.Iterator;
import java.util.LinkedList;

public abstract class AbstractMoveOperation implements MoveOperation
{

    protected LinkedList<PModule> modules = new LinkedList<PModule>();
    protected PModuleContainer destination;

    
    public boolean add(PModule module)
    {
        if (modules.contains(module))
            return false;
        return modules.add(module);
    }

    public void addAll(Collection<? extends PModule> modules)
    {
        this.modules.addAll(modules);
    }

    public int size()
    {
        return modules.size();
    }

    public <T extends PModule> void addAll(T[] modules)
    {
        for (PModule m: modules)
            add(m);
    }
    
    public abstract void move();

    public boolean isEmpty()
    {
        return modules.isEmpty();
    }

    public boolean remove(Object o)
    {
        return modules.remove(o);
    }

    public void setScreenOffset(Point offset)
    {
        setScreenOffset(offset.x, offset.y);
    }

    public boolean contains(Object o)
    {
        return modules.contains(o);
    }

    public PModule[] toArray()
    {
        return modules.toArray(new PModule[modules.size()]);
    }

    public Iterator<PModule> iterator()
    {
        return modules.iterator();
    }
    
    public Collection<? extends PModule> toCollection()
    {
        return (Collection<PModule>) modules.clone();
    }
    
    public Point getScreenOffset()
    {
        return getScreenOffset(null);
    }
    
	public PModuleContainer getDestination() {
		return destination;
	}

	public void setDestination(PModuleContainer destination) {
		this.destination = destination;
	}


}
