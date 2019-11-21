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

#include "pdl/bitstream.h"

BitStream::BitStream()
{
  position = 0;
}

BitStream::~BitStream()
{
}

void BitStream::append(int data, int size)
{
  for (int i = size-1; i >= 0; i--) {
    bits.push_back((data >> i) & 1);
  }
}

int BitStream::getPosition()
{
  return position;
}

int BitStream::getSize()
{
  return bits.size();
}

void BitStream::setPosition(int position)
{
  this->position = position;
}

void BitStream::setSize(int size)
{
  bits.resize(size);
  if (position > size) {
    position = size;
  }
}

bool BitStream::isAvailable(int size)
{
  return position+size <= getSize();
}

int BitStream::getInt(int size)
{
  int result = 0;
  for (int i = 0; i < size; i++) {
    result = result << 1;
    result += bits[position++];
  }
  return result;
}

