/*
    Programmable Property Files
    Copyright (C) 2004 Marcus Andersson

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

#include "ppf/boundbundle.h"
#include "ppf/bundle.h"

using namespace ppf;

BoundBundle::BoundBundle()
{
}

BoundBundle::BoundBundle(Bundle* bundle, int level, string bindings)
{
  this->bundle = bundle;
  this->level = level;
  this->bindings = bindings;
}

BoundBundle BoundBundle::getBoundBundle(string name)
{
  char slevel[11];
  snprintf(slevel, 10, "%d", level);
  return BoundBundle(bundle->getBundle(name, bindings),
		     level + 1,
		     bindings + " set " + slevel + " " + name + ";");
}

string BoundBundle::getProperty(string name)
{
  return bundle->getProperty(name, level + 1, bindings);
}

BoundBundle::~BoundBundle()
{
}
