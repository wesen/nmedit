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

#ifndef PACKET_H
#define PACKET_H

#include <string>
#include <list>
#include <map>

using namespace std;

class Packet
{
 public:
  
  typedef list<Packet*> PacketList;
  typedef list<int> VariableList;
  typedef map<string, int> VariableMap;
  typedef map<string, Packet*> PacketMap;
  typedef map<string, PacketList> PacketListMap;
  typedef map<string, VariableList> VariableListMap;

  Packet();
  virtual ~Packet();

  void clear();

  void setName(string name);
  string getName();
  bool contains(string packetName);

  void bind(Packet* packet, string name);
  void bind(int number, string name);
  void bind(PacketList list, string name);
  void bind(VariableList list, string name);

  Packet* getPacket(string name);
  int getVariable(string name);
  VariableList getAllVariables();
  PacketList getPacketList(string name);
  VariableList getVariableList(string name);

 private:

  VariableMap variables;
  VariableList allVariables;
  PacketMap packets;
  PacketListMap packetLists;
  VariableListMap intLists;
  string name;
};

#endif
