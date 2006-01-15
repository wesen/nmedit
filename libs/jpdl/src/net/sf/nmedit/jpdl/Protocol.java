/*
    Protocol Definition Language
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

package net.sf.nmedit.jpdl;

import java.util.*;

public class Protocol
{
    public Protocol(String filename)
	throws Exception
    {
	tracer = null;
	
	PdlParse parser = new PdlParse();
	parser.init(filename, this);
	parser.yyparse();
    }

    public PacketParser newPacketParser(String name, int padding)
    {
	PacketParser packetParser = new PacketParser(name, padding, this);
	packetParsers.put(name, packetParser);
	return packetParser;
	
    }

    public PacketParser getPacketParser(String name)
    {
	return (PacketParser)packetParsers.get(name);
    }

    public void useTracer(Tracer tracer)
    {
	this.tracer = tracer;
    }

    public void trace(String message)
    {
	if (tracer != null) {
	    tracer.trace(message);
	}	
    }

    private HashMap packetParsers = new HashMap();
    private Tracer tracer;
}
