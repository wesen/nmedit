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
package net.sf.nmedit.jprotocol.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jprotocol.PRMessageDescriptor;
import net.sf.nmedit.jprotocol.PRMessages;

public class PRMessagesImpl implements PRMessages
{

    private Map<String, PRMessageDescriptor> messageMap;
    private Map<Integer, PRMessageDescriptor> messageIdMap;
    private transient Collection<? extends PRMessageDescriptor> messages;
    
    public PRMessagesImpl()
    {
        this.messageMap = new HashMap<String, PRMessageDescriptor>();
        this.messageIdMap = new HashMap<Integer, PRMessageDescriptor>();
    }

    public void add(PRMessageDescriptor message)
    {
        this.messageMap.put(message.getName(), message);
        this.messageIdMap.put(message.getId(), message);
        messages = null; 
    }
    
    public PRMessageDescriptor getMessageByName(String name)
    {
        return messageMap.get(name);
    }

    public int getMessageCount()
    {
        return messageMap.size();
    }

    public Collection<? extends PRMessageDescriptor> getMessages()
    {
        if (messages == null)
            messages = Collections.unmodifiableCollection(messageMap.values());
        return messages;
    }

    public boolean containsMessage(String name)
    {
        return messageMap.containsKey(name);
    }

    public PRMessageDescriptor getMessageById(int messageId)
    {
        return messageIdMap.get(messageId);
    }

}
