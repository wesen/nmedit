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

#include "pdl/intstream.h"

IntStream::IntStream()
{
  position = 0;
}

IntStream::~IntStream()
{
}

void IntStream::append(int data)
{
  ints.push_back(data);
}

int IntStream::getPosition()
{
  return position;
}

int IntStream::getSize()
{
  return ints.size();
}

void IntStream::setPosition(int position)
{
  this->position = position;
}

void IntStream::setSize(int size)
{
  ints.resize(size);
  if (position > size) {
    position = size;
  }
}

bool IntStream::isAvailable(int amount)
{
  return position+amount <= ints.size();
}

int IntStream::getInt()
{
  return ints[position++];
}
