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

#ifndef KNOBMAP_H
#define KNOBMAP_H

#include "nmpatch/module.h"
#include "nmpatch/modulesection.h"

class KnobMap
{
 public:

  enum Knob {
    KNOB1 = 0,
    KNOB2,
    KNOB3,
    KNOB4,
    KNOB5,
    KNOB6,
    KNOB7,
    KNOB8,
    KNOB9,
    KNOB10,
    KNOB11,
    KNOB12,
    KNOB13,
    KNOB14,
    KNOB15,
    KNOB16,
    KNOB17,
    KNOB18,
    PEDAL = 19,
    AFTERTOUCH,
    SWITCH = 22
  };

  KnobMap(ModuleSection::Type, Module*, Module::Parameter);

  ModuleSection::Type getModuleSectionType();
  Module* getModule();
  Module::Parameter getParameter();

  void setKnob(Knob);
  Knob getKnob();

 private:

  ModuleSection::Type section;
  Module* module;
  Module::Parameter parameter;
  Knob knob;
};

#endif
