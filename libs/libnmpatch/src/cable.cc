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

#include "nmpatch/cable.h"

Cable::Cable(Cable::Color color,
	     Module* destinationModule,
	     ModuleType::Port destinationConnector,
	     ModuleType::ConnectorType destinationConnectorType,
	     Module* sourceModule,
	     ModuleType::Port sourceConnector,
	     ModuleType::ConnectorType sourceConnectorType)
{
  this->color = color;
  this->destinationModule = destinationModule;
  this->destinationConnector = destinationConnector;
  this->destinationConnectorType = destinationConnectorType;
  this->sourceModule = sourceModule;
  this->sourceConnector = sourceConnector;
  this->sourceConnectorType = sourceConnectorType;
}

Cable::Color Cable::getColor()
{
  return color;
}

Module* Cable::getDestinationModule()
{
  return destinationModule;
}

ModuleType::Port Cable::getDestinationConnector()
{
  return destinationConnector;
}

ModuleType::ConnectorType Cable::getDestinationConnectorType()
{
  return destinationConnectorType;
}

Module* Cable::getSourceModule()
{
  return sourceModule;
}

ModuleType::Port Cable::getSourceConnector()
{
  return sourceConnector;
}

ModuleType::ConnectorType Cable::getSourceConnectorType()
{
  return sourceConnectorType;
}
