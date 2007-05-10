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

import java.io.BufferedInputStream;
import java.io.InputStream;

import net.sf.nmedit.jprotocol.impl.PRMessageFactoryImpl;
import net.sf.nmedit.jprotocol.impl.PRMessageImpl;
import net.sf.nmedit.jprotocol.impl.PRParserImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;

public class PRTest
{

    static PRMessages messages;

    @BeforeClass
    public static void initMessages()
        throws Exception
    {
        InputStream in = new BufferedInputStream(PRTest.class.getResourceAsStream("example.messages.xml"));
        try
        {
            PRParser parser = new PRParserImpl();
            messages = parser.parseMessages(new InputSource(in));
        }
        finally
        {
            in.close();
        }
    }
    
    @AfterClass
    public static void disposeMessages()
    {
        messages = null;
    }

    @Test
    public void verifyMessageCount()
    {
        assertEquals(1, messages.getMessageCount());
    }
    
    @Test
    public void verifyMessage()
    {
        PRMessageDescriptor msgID =
            messages.getMessageById(1);
        PRMessageDescriptor msgName =
            messages.getMessageByName("DeleteModuleMessage");
        assertEquals(msgID, msgName);
    }
    
    @Test
    public void verifyParameters()
    {
        PRMessageDescriptor msg =
            messages.getMessageByName("DeleteModuleMessage");
        
        final Object[] data = new Object[] {
        // index | name  |  path  |  default value
                0, "cc"  , "cc"           ,  1*16+7,
                1, "slot", "slot"         ,  0,
                2, "pid" , "data:data:pid", null,
                3, "sc"  , "data:data:sc" ,  3*16+2
                
        };
        final int cols = 4;
        for (int i=0;i<data.length;i+=cols)
        {
            int index = (Integer) data[i+0];
            String name = (String) data[i+1];
            String path = (String) data[i+2];
            Integer defaultValue = (Integer) data[i+3];
            
            verifyParameter(msg, index, name, path, defaultValue);
        }
    }

    private void verifyParameter(PRMessageDescriptor msg, int index, String name, String path, Integer defaultValue)
    {
        PRParameterDescriptor parId = msg.getParameterAt(index);
        PRParameterDescriptor parName = msg.getParameterByName(name);
        assertNotNull(parId);
        assertNotNull(parName);
        assertEquals(parId, parName);
        PRParameterDescriptor par = parId;

        assertEquals(name, par.getName());
        assertEquals(path, par.getBinding());
        assertEquals(par.hasDefaultValue(), defaultValue != null);
        if (par.hasDefaultValue())
            assertEquals(par.getDefaultValue(), defaultValue);
    }
    
    @Test
    public void createMessageTest() throws PRException
    {
        PRMessageFactory factory = new PRMessageFactoryImpl(messages);
        PRMessage msgId = factory.createMessage(1);
        PRMessage msgName = factory.createMessage("DeleteModuleMessage");

        assertNotNull(msgId);
        assertNotNull(msgName);
        assertEquals(msgId.getDescriptor(), msgName.getDescriptor());
    }
    
    /**
     * Uses a custom message class. 
     * @throws PRException
     */
    @Test
    public void alternativeClassTest() throws PRException
    {
        PRMessageFactoryImpl factory = new PRMessageFactoryImpl(messages);
        factory.setMessageClass(1, CustomMessage.class);
        PRMessage message = factory.createMessage(1);
        assertNotNull(message);
        assertTrue(message instanceof CustomMessage);
    }
    
    public static class CustomMessage extends PRMessageImpl
    {

        protected CustomMessage(PRMessageDescriptor descriptor)
        {
            super(descriptor);
        }

        public static PRMessage createMessage(PRMessageDescriptor descriptor)
        {
            return new CustomMessage(descriptor);
        }

    }
    
}
