/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.sf.nmedit.jnmprotocol2.utils;

import net.sf.nmedit.jnmprotocol2.NmProtocol;

public class ProtocolRunner implements Runnable
{

    public static class ProtocolErrorHandler
    {
        public void handleError(Throwable t) throws Throwable
        {
            throw t;
        }
    }
    
    private NmProtocol protocol;
    private ProtocolErrorHandler errorHandler;

    public ProtocolRunner(NmProtocol protocol)
    {
        this(protocol, null);
    }

    public ProtocolRunner(NmProtocol protocol, ProtocolErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
        if (errorHandler == null)
            this.errorHandler = new ProtocolErrorHandler();
        this.protocol = protocol;
    }
    
    public NmProtocol getProtocol()
    {
        return protocol;
    }
    
    public void run()
    {
        try
        {
            protocol.heartbeat();
        }
        catch (Throwable t)
        {
            try
            {
                errorHandler.handleError(t);
            }
            catch (Throwable notIgnorableT)
            {
                if (notIgnorableT instanceof RuntimeException)
                    throw (RuntimeException) notIgnorableT;
                else
                    throw new RuntimeException(notIgnorableT);
            }
        }
    }

}
