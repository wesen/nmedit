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

#ifndef MATCHER_H
#define MATCHER_H

class Protocol;
class Packet;
class BitStream;
class IntStream;
class Condition;

class Matcher
{
 public:
  
  Matcher(Condition* condition, bool optional);
  virtual ~Matcher();

  bool isConditional();
  bool isOptional();
  bool trueCondition(Packet* packet);
  
  virtual bool match(Protocol* protocol, BitStream* data, Packet* result) = 0;
  virtual bool apply(Protocol* protocol, Packet* packet,
		     IntStream* data, BitStream* result) = 0;

 private:

  bool optional;
  Condition* condition;
};

#endif
