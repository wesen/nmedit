/*
    Nord Modular patch file format 3.03 parser
    Copyright (C) 2002 Marcus Andersson

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

#include "nmpatch/module.h"

Module::Module(Type type, int index)
{
  this->index = index;
  this->type = type;
}

int Module::getIndex()
{
  return index;
}

Module::Type Module::getType()
{
  return type;
}

void Module::setPosition(int x, int y)
{
  xpos = x;
  ypos = y;
}

int Module::getXPosition()
{
  return xpos;
}

int Module::getYPosition()
{
  return ypos;
}

void Module::setName(string name)
{
  this->name = name;
}

string Module::getName()
{
  return name;
}

void Module::setParameter(Module::Parameter param, int value)
{
  parameters[param] = value;
}

int Module::getParameter(Module::Parameter param)
{
  return parameters[param];
}

int Module::numberOfParameters() const
{
  return parameters.size();
}

void Module::setCustomValue(Module::CustomValue param, int value)
{
  customValues[param] = value;
}

int Module::getCustomValue(Module::CustomValue param)
{
  return customValues[param];
}

int Module::numberOfCustomValues() const
{
  return customValues.size();
}
