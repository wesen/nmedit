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

#include "pdl/constantmatcher.h"
#include "pdl/condition.h"
#include "pdl/bitstream.h"
#include "pdl/protocol.h"

ConstantMatcher::ConstantMatcher(int value, int size,
				 Condition* condition, bool optional)
  : Matcher(condition, optional)
{
  this->value = value;
  this->size = size;
}

ConstantMatcher::~ConstantMatcher()
{
}

bool ConstantMatcher::match(Protocol* protocol, BitStream* data,
			    Packet* result)
{
  trace(protocol);
  return data->isAvailable(size) && data->getInt(size) == value;
}

bool ConstantMatcher::apply(Protocol* protocol, Packet* packet,
			    IntStream* data, BitStream* result)
{
  trace(protocol);
  result->append(value, size);
  return true;
}

void ConstantMatcher::trace(Protocol* protocol)
{
  char buf[100];
  snprintf(buf, 100, "%d:%d", value, size);
  protocol->trace(string(buf));
}
