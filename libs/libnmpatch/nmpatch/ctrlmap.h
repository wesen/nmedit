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

#ifndef CTRLMAP_H
#define CTRLMAP_H

#include "nmpatch/module.h"
#include "nmpatch/modulesection.h"

class CtrlMap
{
 public:

  CtrlMap(ModuleSection::Type, Module*, Module::Parameter);

  ModuleSection::Type getModuleSectionType();
  Module* getModule();
  Module::Parameter getParameter();

  void setCC(int);
  int getCC();

 private:

  ModuleSection::Type section;
  Module* module;
  Module::Parameter parameter;
  int CC;
};

#endif
