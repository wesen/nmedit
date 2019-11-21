/*
    Nord Modular patch file format 3.03 parser
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

#include "nmpatch/moduletype.h"
#include "nmpatch/module.h"
#include "ppf/ppfexception.h"
#include "ppf/boundbundle.h"

#include <stdlib.h>

ModuleType::ModuleType()
{
}

ModuleType::ModuleType(int id,
		       ppf::BoundBundle* properties,
		       ppf::BoundBundle* maps)
{
  this->id = id;
  this->properties = properties;
  this->maps = maps;
}

ModuleType::~ModuleType()
{
  delete properties;
  delete maps;
}

int ModuleType::getId()
{
  return id;
}
  
string ModuleType::getName()
{
  return properties->getProperty("name");
}

double ModuleType::getCycles()
{
  atof(properties->getProperty("cycles").c_str());
}

double ModuleType::getProgmem()
{
  atof(properties->getProperty("progmem").c_str());
}

double ModuleType::getXmem()
{
  atof(properties->getProperty("xmem").c_str());
}

double ModuleType::getYmem()
{
  atof(properties->getProperty("ymem").c_str());
}

double ModuleType::getZeropage()
{
  atof(properties->getProperty("zeropage").c_str());
}

double ModuleType::getDynmem()
{
  atof(properties->getProperty("dynmem").c_str());
}

int ModuleType::getHeight()
{
  atoi(properties->getProperty("height").c_str());
}
  
int ModuleType::numberOfParameters()
{
  return countItems("parameter"); 
}

string ModuleType::getParameterName(Parameter param)
{
  return getItemString("parameter", param, "name");
}

int ModuleType::getParameterMin(Parameter param)
{
  return atoi(getItemString("parameter", param, "min").c_str());
}

int ModuleType::getParameterMax(Parameter param)
{
  return atoi(getItemString("parameter", param, "max").c_str());
}

string ModuleType::mapParameter(Parameter param, int value,
				Module* module)
{
  ppf::BoundBundle map =
    maps->getBoundBundle(getItemString("parameter", param, "map"));
  try {
    Parameter mapparam =
      atoi(getItemString("parameter", param, "mapparam").c_str());
    string mapparamvalue = 
      mapParameter(mapparam, module->getParameter(mapparam), module);
    map = map.getBoundBundle(mapparamvalue);
  }
  catch (ppf::PPFException& exception) {
    // No mapparam for this one.
  }
  return map.getProperty(value);
}

int ModuleType::numberOfPorts(ConnectorType type)
{
  return countItems(type == INPUT ? "input" : "output");
}

string ModuleType::getPortName(ConnectorType type, Port port)
{
  return getItemString(type == INPUT ? "input" : "output",
		       port, "name");
}

ModuleType::IOType ModuleType::getPortType(ConnectorType type, Port port)
{
  string ioType = getItemString(type == INPUT ? "input" : "output",
				port, "name");
  if (ioType == "audio") {
    return AUDIO;
  }
  else if (ioType == "slave") {
    return SLAVE;
  }
  else if (ioType == "control") {
    return CONTROL;
  }
  else {
    return LOGIC;
  }
}

int ModuleType::numberOfCustomValues()
{
  return countItems("custom");
}

string ModuleType::getCustomValueName(CustomValue customValue)
{
  return getItemString("custom", customValue, "name");
}

int ModuleType::getCustomValueMin(CustomValue customValue)
{
  return atoi(getItemString("custom", customValue, "min").c_str());
}

int ModuleType::getCustomValueMax(CustomValue customValue)
{
  return atoi(getItemString("custom", customValue, "max").c_str());
}

string ModuleType::mapCustomValue(CustomValue customValue, int value)
{
  ppf::BoundBundle map =
    maps->getBoundBundle(getItemString("custom", customValue, "map"));
  return map.getProperty(value);
}

string ModuleType::getItemString(string item, int index, string property)
{   
  return properties->getBoundBundle(item)
    .getBoundBundle(index).getProperty(property);
}

int ModuleType::countItems(string item)
{
  int i = 0;
  try {
    ppf::BoundBundle parameters = properties->getBoundBundle(item);
    for (i=0; ; i++) {
      parameters.getBoundBundle(i);
    }
  }
  catch (ppf::PPFException& exception) {
    // We have reached the end of items.
  }
  return i;
}
