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

#include "pdl/packetmatcher.h"
#include "pdl/condition.h"
#include "pdl/packet.h"
#include "pdl/packetparser.h"
#include "pdl/protocol.h"
#include "pdl/bitstream.h"

PacketMatcher::PacketMatcher(string count, string parserName,
			     string binding, Condition* condition,
			     bool optional)
  : Matcher(condition, optional)
{
  this->count = count;
  this->parserName = parserName;
  this->binding = binding;
}

PacketMatcher::~PacketMatcher()
{
}

bool PacketMatcher::match(Protocol* protocol, BitStream* data, Packet* result)
{
  int iterations;
  Packet::PacketList packetList;
  PacketParser* packetParser = protocol->getPacketParser(parserName);

  trace(protocol);

  if (packetParser == 0) {
    return false;
  }

  if (count == "") {
    iterations = 1;
  }
  else {
    iterations = result->getVariable(count);
    if (iterations < 0) {
      return false;
    }
  }

  for (int i = 0; i < iterations; i++) {
    Packet* packet = new Packet();
    if (packetParser->parse(data, packet)) {
      packetList.push_back(packet);
    }
    else {
      delete packet;
      return false;
    }
  }
  
  if (count == "") {
    result->bind((*packetList.begin()), binding);
  }
  else {
    result->bind(packetList, binding);
  }
  return true;
}

bool PacketMatcher::apply(Protocol* protocol, Packet* packet,
			  IntStream* data, BitStream* result)
{
  int iterations;
  PacketParser* packetParser = protocol->getPacketParser(parserName);

  trace(protocol);

  if (packetParser == 0) {
    return false;
  }

  if (count == "") {
    iterations = 1;
  }
  else {
    iterations = packet->getVariable(count);
    if (iterations < 0) {
      return false;
    }
  }

  for (int i = 0; i < iterations; i++) {
    if (!packetParser->generate(data, result)) {
      return false;
    }
  }
  return true;
}

void PacketMatcher::trace(Protocol* protocol)
{
  protocol->trace((count == "" ? string("") : count + "*") +
		  parserName + "$" + binding);
}
