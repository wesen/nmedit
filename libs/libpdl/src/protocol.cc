/*
    Protocol Definition Language
    Copyright (C) 2003 Marcus Andersson

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

#include "pdl/protocol.h"
#include "pdl/packetparser.h"
#include "pdl/tracer.h"

extern int init_pdl_parser(const char* filename, Protocol* protocol);
extern void pdlparse();

Protocol::Protocol(string filename)
{
  tracer = 0;

  if (init_pdl_parser(filename.c_str(), this)) {
    pdlparse();
  }
}

Protocol::~Protocol()
{
  for (PacketMap::iterator n = packetParsers.begin();
       n != packetParsers.end(); n++) {
    delete ((*n).second);
  }
}

PacketParser* Protocol::newPacketParser(string name, int padding)
{
  PacketParser* packetParser = new PacketParser(name, padding, this);
  packetParsers[name] = packetParser;
  return packetParser;
}

PacketParser* Protocol::getPacketParser(string name)
{
  return packetParsers[name];
}

void Protocol::useTracer(Tracer* tracer)
{
  this->tracer = tracer;
}

void Protocol::trace(string message)
{
  if (tracer != 0) {
    tracer->trace(message);
  }
}
