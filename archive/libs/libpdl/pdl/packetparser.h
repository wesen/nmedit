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

#ifndef PACKETPARSER_H
#define PACKETPARSER_H

#include <string>
#include <list>

using namespace std;

class Protocol;
class Packet;
class BitStream;
class IntStream;
class Matcher;
class Condition;

class PacketParser
{
 public:
  
  typedef list<Matcher*> MatcherList;

  PacketParser(string name, int padding, Protocol* protocol);
  virtual ~PacketParser();

  bool parse(BitStream* data, Packet* result);
  bool generate(IntStream* data, BitStream* result);

  void addPacketMatcher(string count, string packetName,
			string binding, Condition* condition,
			bool optional);
  void addVariableMatcher(int count, string variable, int size,
			  int terminal, Condition* condition,
			  bool optional);
  void addConstantMatcher(int constant, int size,
			  Condition* condition, bool optional);

 private:
  
  string name;
  int padding;
  Protocol* protocol;
  MatcherList matchers;
};

#endif
