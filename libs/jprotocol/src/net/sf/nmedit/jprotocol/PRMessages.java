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
package net.sf.nmedit.jprotocol;

import java.util.Collection;

/**
 * A collection of message descriptors.
 */
public interface PRMessages
{

    /**
     * Returns an immutable collection of the message descriptors.
     */
    Collection<? extends PRMessageDescriptor> getMessages();
    
    /**
     * Returns the number of messages.
     */
    int getMessageCount();

    /**
     * Returns the message descriptor with the specified name.
     * If no message descriptor is found, <code>null</code> is returned.
     * @param name the name of the message descriptor
     * @return the message descriptor
     */
    PRMessageDescriptor getMessageByName(String name);

    /**
     * Returns the message descriptor with the specified id.
     * If no message descriptor is found, <code>null</code> is returned.
     * @param id the id of the message descriptor
     * @return the message descriptor
     */
    PRMessageDescriptor getMessageById(int messageId);
    
    /**
     * Returns true if a message with the specified name exists in this collection.
     */
    boolean containsMessage(String name);
    
}
