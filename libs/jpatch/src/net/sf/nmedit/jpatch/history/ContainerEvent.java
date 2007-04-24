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
package net.sf.nmedit.jpatch.history;

import java.util.LinkedList;
import java.util.List;

public class ContainerEvent implements Event
{
    
    private List<Event> events;
    private String title;

    public ContainerEvent(String title)
    {
        this.title = title;
        events = new LinkedList<Event>();
    }
    
    public void perform()
    {
        for (Event e: events)
            e.perform();
    }

    public String getTitle()
    {
        return title;
    }

    public void add(Event event)
    {
        events.add(0, event);
    }

    public boolean isEmpty()
    {
        return events.isEmpty();
    }
    
}
