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

#include "nmpatch/modulesection.h"
#include "ppf/parser.h"
#include "ppf/boundbundle.h"

#ifndef LIBPATH
#define LIBPATH ""
#endif

string ModuleSection::ppfFile = string(LIBPATH) + "/module.ppf";
ppf::Parser* ModuleSection::parser = 0;
ppf::BoundBundle* ModuleSection::moduleProperties = 0;

void ModuleSection::usePPFFile(string filename)
{
  ppfFile = filename;
  delete parser;
  delete moduleProperties;
  parser = new ppf::Parser();
  moduleProperties = new ppf::BoundBundle;
  *moduleProperties = parser->parse(ppfFile);
}

ModuleSection::ModuleSection()
{
  nextIndex = 1;

  if (parser == 0) {
    parser = new ppf::Parser();
    moduleProperties = new ppf::BoundBundle;
    *moduleProperties = parser->parse(ppfFile);
  }
}

ModuleSection::~ModuleSection()
{
  for (ModuleList::iterator m = modules.begin(); m != modules.end(); m++) {
    delete (*m);
  }

  for (CableList::iterator c = cables.begin(); c != cables.end(); c++) {
    delete (*c);
  }
}

void ModuleSection::setVoiceRetrigger(ModuleSection::VoiceRetrigger retrigger)
{
  voiceRetrigger = retrigger;
}

ModuleSection::VoiceRetrigger ModuleSection::getVoiceRetrigger()
{
  return voiceRetrigger;
}

Module* ModuleSection::newModule(ModuleType::TypeId type, int index )
{
  if (index == 0) {
    index = nextIndex++;
  }
  else {
    nextIndex = nextIndex > index+1 ? nextIndex : index+1;
  }
  
  ppf::BoundBundle* parameters = new ppf::BoundBundle();
  ppf::BoundBundle* maps = new ppf::BoundBundle();
  *parameters =
    moduleProperties->getBoundBundle("moduletype").getBoundBundle(type);
  *maps =  moduleProperties->getBoundBundle("map");
  modules.push_back(new Module(new ModuleType(type, parameters, maps), index));
  return modules.back();
}

ModuleSection::ModuleList ModuleSection::getModules() const
{
  return modules;
}

Module* ModuleSection::getModule(int index)
{
  for (ModuleList::iterator m = modules.begin(); m != modules.end(); m++) {
    if ((*m)->getIndex() == index) {
      return (*m);
    }
  }
  return 0;
}

void ModuleSection::removeModule(Module* module)
{
  modules.remove(module);
  delete module;
}

Cable* ModuleSection::newCable(Cable::Color color,
			       int destinationModuleIndex,
			       ModuleType::Port destinationConnector,
			       ModuleType::ConnectorType destinationConnectorType,
			       int sourceModuleIndex,
			       ModuleType::Port sourceConnector,
			       ModuleType::ConnectorType sourceConnectorType)
{
  cables.push_back(new Cable(color, getModule(destinationModuleIndex),
			     destinationConnector, destinationConnectorType,
			     getModule(sourceModuleIndex), sourceConnector,
			     sourceConnectorType));
  return cables.back();
}

ModuleSection::CableList ModuleSection::getCables() const
{
  return cables;
}

void ModuleSection::removeCable(Cable* cable)
{
  cables.remove(cable);
  delete cable;
}
