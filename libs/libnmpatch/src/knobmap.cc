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

#include "nmpatch/knobmap.h"

KnobMap::KnobMap(ModuleSection::Type section, Module* module,
		 ModuleType::Parameter parameter)
{
  this->section = section;
  this->module = module;
  this->parameter = parameter;
}

ModuleSection::Type KnobMap::getModuleSectionType()
{
  return section;
}

Module* KnobMap::getModule()
{
  return module;
}

ModuleType::Parameter KnobMap::getParameter()
{
  return parameter;
}

void KnobMap::setKnob(KnobMap::Knob knob)
{
  this->knob = knob;
}

KnobMap::Knob KnobMap::getKnob()
{
  return knob;
}
