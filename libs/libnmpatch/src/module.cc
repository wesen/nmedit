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
#include "nmpatch/modulelistener.h"

Module::Module(ModuleType* type, int index)
{
  this->index = index;
  this->type = type;
}

Module::~Module()
{
  delete type;
}

int Module::getIndex()
{
  return index;
}

ModuleType* Module::getType()
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

void Module::setParameter(ModuleType::Parameter param, int value)
{
  parameters[param] = value;
}

int Module::getParameter(ModuleType::Parameter param)
{
  return parameters[param];
}

string Module::getMappedParameter(ModuleType::Parameter param)
{
  return type->mapParameter(param, parameters[param], this);
}

void Module::setCustomValue(ModuleType::CustomValue param, int value)
{
  customValues[param] = value;
}

int Module::getCustomValue(ModuleType::CustomValue param)
{
  return customValues[param];
}

string Module::getMappedCustomValue(ModuleType::CustomValue param)
{
  return type->mapCustomValue(param, customValues[param]);
}

void Module::addListener(ModuleListener* listener)
{
  listeners.push_back(listener);
}

void Module::removeListener(ModuleListener* listener)
{
  listeners.remove(listener);
}

void Module::notifyListeners(ModuleType::Parameter parameter, int value)
{
  for (ModuleListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    (*i)->parameterChanged(parameter, value);
  }
}

