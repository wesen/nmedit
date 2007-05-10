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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.nmedit.jpdl.Packet;
import net.sf.nmedit.jprotocol.PRException;
import net.sf.nmedit.jprotocol.PRMessage;
import net.sf.nmedit.jprotocol.PRMessageDescriptor;
import net.sf.nmedit.jprotocol.PRMessageFactory;
import net.sf.nmedit.jprotocol.PRMessages;

public class PRMessageFactoryImpl implements PRMessageFactory
{

    private PRMessages messages;
    private Map<Integer, Class<? extends PRMessage>> messageMap;
    private Class<? extends PRMessage> defaultMessageClass;
    private transient Map<Class<?>, Method> methodMap;
    
    public PRMessageFactoryImpl(PRMessages messages)
    {
        this.messages = messages;
        this.messageMap = new HashMap<Integer, Class<? extends PRMessage>>();
        this.defaultMessageClass = PRMessageImpl.class;
    }
    
    public Class<? extends PRMessage> getDefaultMessageClass()
    {
        return defaultMessageClass;
    }

    public <T extends PRMessage> void setDefaultMessageClass(Class<T> messageClass)
    {
        if (messageClass == null)
            throw new NullPointerException("message class must not be null");
        this.defaultMessageClass = messageClass;
    }

    public <T extends PRMessage> void setMessageClass(int messageId, Class<T> messageClass)
    {
        if (methodMap != null)
        {
            Class<?> clazz = messageMap.get(messageId);
            if (clazz != null)
                methodMap.remove(clazz);
        }
        messageMap.put(messageId, messageClass);
    }

    public Class<? extends PRMessage> getMessageClass(int messageId)
    {
        return messageMap.get(messageId);
    }

    public <T extends PRMessage> void setMessageClass(PRMessageDescriptor descriptor, Class<T> messageClass)
    {
        setMessageClass(descriptor, messageClass);
    }

    public Class<? extends PRMessage> getMessageClass(PRMessageDescriptor descriptor)
    {
        return getMessageClass(descriptor);
    }

    private Class<? extends PRMessage> getMessageClass2(int messageId)
    {
        Class<? extends PRMessage> messageClass = getMessageClass(messageId);
        if (messageClass == null)
            messageClass = defaultMessageClass;
        return messageClass;
    }
    
    public PRMessages getMessages()
    {
        return messages;
    }

    private <T extends PRMessage> T createMessage(Class<T> messageClass, PRMessageDescriptor descriptor) 
        throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        if (methodMap == null)
            methodMap = new HashMap<Class<?>, Method>();
        Method createMessage = methodMap.get(messageClass);
        if (createMessage == null)
        {
            createMessage = messageClass.getMethod("createMessage", new Class[] {PRMessageDescriptor.class});
            methodMap.put(messageClass, createMessage);
        } 
        return (T) createMessage.invoke(null, new Object[] {descriptor});
    }
    
    private <T extends PRMessage> T createMessage2(Class<T> messageClass, PRMessageDescriptor descriptor)
        throws PRException
    {
        try
        {
            return createMessage(messageClass, descriptor);
        }
        catch (Exception e)
        {
            throw new PRException(e);
        }
    }

    public PRMessage createMessage(int messageId) throws PRException
    {
        PRMessageDescriptor descriptor = messages.getMessageById(messageId);
        Class<? extends PRMessage> messageClass = getMessageClass2(messageId);
        return createMessage2(messageClass, descriptor);
    }

    public PRMessage createMessage(String name) throws PRException
    {
        PRMessageDescriptor descriptor = messages.getMessageByName(name);
        if (descriptor == null)
            throw new PRException("unknown message: "+name);
        Class<? extends PRMessage> messageClass = getMessageClass2(descriptor.getId());
        return createMessage2(messageClass, descriptor);
    }

    public PRMessage createMessage(Packet packet) throws PRException
    {
        for (PRMessageDescriptor descriptor: messages.getMessages())
        {
            if (packet.contains(descriptor.getName()))
            {
                return createMessage(packet, descriptor);
            }
        }
        
        throw new PRException("no message found for packet: "+packet);
    }

    private PRMessage createMessage(Packet packet, PRMessageDescriptor descriptor)
        throws PRException
    {
        Class<? extends PRMessage> messageClass = getMessageClass2(descriptor.getId());
        PRMessage message = createMessage2(messageClass, descriptor);
        message.setValues(packet);
        return message;
    }

}
