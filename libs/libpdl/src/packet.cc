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

#include "pdl/packet.h"

Packet::Packet()
{
}

Packet::~Packet()
{
  for (PacketMap::iterator n = packets.begin(); n != packets.end(); n++) {
    delete ((*n).second);
  }

  for (PacketListMap::iterator m = packetLists.begin();
       m != packetLists.end(); m++) {
    for (PacketList::iterator n = (*m).second.begin();
	 n != (*m).second.end(); n++) {
      delete (*n);
    }
  }
}

void Packet::bind(Packet* packet, string name)
{
  packets[name] = packet;
}

void Packet::bind(int number, string name)
{
  variables[name] = number;
  allVariables.push_back(number);
}

void Packet::bind(PacketList list, string name)
{
  packetLists[name] = list;
}

void Packet::bind(VariableList list, string name)
{
  intLists[name] = list;
}

Packet* Packet::getPacket(string name)
{
  return packets[name];
}

int Packet::getVariable(string name)
{
  return variables.find(name) != variables.end() ? variables[name] : -1;
}

Packet::VariableList Packet::getAllVariables()
{
  return allVariables;
}

Packet::PacketList Packet::getPacketList(string name)
{
  return packetLists[name];
}

Packet::VariableList Packet::getVariableList(string name)
{
  return intLists[name];
}

void Packet::clear()
{
  packets.clear();
  variables.clear();
  packetLists.clear();
  intLists.clear();
}

string Packet::getName()
{
  return name;
}

void Packet::setName(string name)
{
  this->name = name;
}

bool Packet::contains(string packetName)
{
  if (getName() == packetName) {
    return true;
  }
  else {
    for (PacketMap::iterator i = packets.begin(); i != packets.end(); i++) {
      if ((*i).second->contains(packetName)) {
	return true;
      }
    }
    for (PacketListMap::iterator m = packetLists.begin();
	 m != packetLists.end(); m++) {
      for (PacketList::iterator n = (*m).second.begin();
	   n != (*m).second.end(); n++) {
	if ((*n)->contains(packetName)) {
	  return true;
	}
      }
    }
  }
  return false;
}
