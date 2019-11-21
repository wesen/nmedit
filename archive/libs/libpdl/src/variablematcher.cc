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

#include "pdl/variablematcher.h"
#include "pdl/condition.h"
#include "pdl/packet.h"
#include "pdl/bitstream.h"
#include "pdl/intstream.h"
#include "pdl/protocol.h"

VariableMatcher::VariableMatcher(int count, string variable, int size,
				 int terminal,
				 Condition* condition, bool optional)
  : Matcher(condition, optional)
{
  this->count = count;
  this->variable = variable;
  this->size = size;
  this->terminal = terminal;
}

VariableMatcher::~VariableMatcher()
{
}

bool VariableMatcher::match(Protocol* protocol, BitStream* data,
			    Packet* result)
{
  Packet::VariableList variableList;

  trace(protocol);

  for (int i = 0; i < count; i++) {
    if (data->isAvailable(size)) {
      int value = data->getInt(size);
      variableList.push_back(value);

      if (value == terminal) {
	break;
      }
    }
    else {
      return false;
    }
  }
  
  if (count == 1) {
    result->bind(*variableList.begin(), variable);
  }
  else {
    result->bind(variableList, variable);
  }
  return true;
}

bool VariableMatcher::apply(Protocol* protocol, Packet* packet,
			    IntStream* data, BitStream* result)
{
  Packet::VariableList variableList;

  trace(protocol);
  
  for (int i = 0; i < count; i++) {
    if (data->isAvailable(1)) {
      int value = data->getInt();
      variableList.push_back(value);
      result->append(value, size);

      if (value == terminal) {
	break;
      }
    }
    else {
      return false;
    }
  }
  
  if (count == 1) {
    packet->bind((*variableList.begin()), variable);
  }
  else {
    packet->bind(variableList, variable);
  }
  return true;
}

void VariableMatcher::trace(Protocol* protocol)
{
  char buf[100];
  if (count == 1) {
    snprintf(buf, 100, "%s:%d", variable.c_str(), size);
  }
  else {
    snprintf(buf, 100, "%d*%s:%d/%d", count, variable.c_str(), size, terminal);
  }
  protocol->trace(string(buf));
}
