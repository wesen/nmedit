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

#ifndef INTSTREAM_H
#define INTSTREAM_H

#include <vector>

using namespace std;

class IntStream
{
 public:
  
  IntStream();

  virtual ~IntStream();

  void append(int data);

  int getPosition();
  int getSize();

  void setPosition(int position);
  void setSize(int size);

  bool isAvailable(int size);

  int getInt();

 private:
  vector<int> ints;
  int position;
};

#endif
