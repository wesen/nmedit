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

#include <stdio.h>

#include "pdl/packetparser.h"
#include "pdl/protocol.h"
#include "pdl/packet.h"
#include "pdl/bitstream.h"
#include "pdl/intstream.h"
#include "pdl/matcher.h"
#include "pdl/packetmatcher.h"
#include "pdl/variablematcher.h"
#include "pdl/constantmatcher.h"

PacketParser::PacketParser(string name, int padding, Protocol* protocol)
{
  this->name = name;
  this->padding = padding;
  this->protocol = protocol;
}

PacketParser::~PacketParser()
{
}

bool PacketParser::parse(BitStream* data, Packet* result)
{
  bool conditional = false;
  bool conditionalMatch = false;
  int dataPos = data->getPosition();

  protocol->trace(string("PARSE PACKET ") + name);

  result->setName(name);

  for (MatcherList::iterator matcher = matchers.begin();
       matcher != matchers.end(); matcher++) {

    if ((*matcher)->isConditional()) {
      conditional = true;
      if ((*matcher)->trueCondition(result)) {
	bool success = (*matcher)->match(protocol, data, result);
	if (success) {
	  conditionalMatch = true;
	}
      }
    }
    else {
      bool success = (*matcher)->match(protocol, data, result);
      if (!success && !(*matcher)->isOptional()) {
	goto parse_failed;
      }
    }
  }

  if (!conditional || (conditional && conditionalMatch)) {
    data->getInt((data->getPosition()-dataPos) % padding);
    protocol->trace(string("MATCHED ") + name);
    return true;
  }

 parse_failed:
  protocol->trace(string("FAILED ") + name);
  data->setPosition(dataPos);
  result->clear();
  return false;
}

bool PacketParser::generate(IntStream *data, BitStream* result)
{
  Packet* packet = new Packet();
  bool conditional = false;
  bool conditionalMatch = false;
  int dataPos = data->getPosition();
  int resultSize = result->getSize();

  protocol->trace(string("GENERATE PACKET ") + name);

  for (MatcherList::iterator matcher = matchers.begin();
       matcher != matchers.end(); matcher++) {
    
    if ((*matcher)->isConditional()) {
      conditional = true;
      if ((*matcher)->trueCondition(packet)) {
	bool success = (*matcher)->apply(protocol, packet, data, result);
	if (success) {
	  conditionalMatch = true;
	}
      }
    }
    else {
      bool success = (*matcher)->apply(protocol, packet, data, result);
      if (!success && !(*matcher)->isOptional()) {
	goto generate_failed;
      }
    }
  }

  if (!conditional || (conditional && conditionalMatch)) {
    result->append(0, (result->getSize()-resultSize) % padding);
    protocol->trace(string("MATCHED ") + name);
    return true;
  }

 generate_failed:
  protocol->trace(string("FAILED ") + name);
  data->setPosition(dataPos);
  result->setSize(resultSize);
  delete packet;
  return false;
}
    
void PacketParser::addPacketMatcher(string count, string packetName,
				    string binding, Condition* condition,
				    bool optional)
{
  matchers.push_back(new PacketMatcher(count, packetName, binding, condition,
				    optional));
}

void PacketParser::addVariableMatcher(int count, string variable, int size,
				      int terminal, Condition* condition,
				      bool optional)
{
  matchers.push_back(new VariableMatcher(count, variable, size, terminal,
					 condition, optional));
}

void PacketParser::addConstantMatcher(int constant, int size,
				      Condition* condition, bool optional)
{
  matchers.push_back(new ConstantMatcher(constant, size, condition, optional));
}
