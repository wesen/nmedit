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

#include "nmpatch/morph.h"

Morph::~Morph()
{
  for (MorphMapList::iterator m = morphMaps.begin();
       m != morphMaps.end();
       m++) {
    delete (*m);
  }
}

void Morph::setValue(int value)
{
  this->value = value;
}

int Morph::getValue()
{
  return value;
}

MorphMap* Morph::newMorphMap(ModuleSection::Type section, Module* module,
			     Module::Parameter parameter)
{
  morphMaps.push_back(new MorphMap(section, module, parameter));
  return morphMaps.back();
}

Morph::MorphMapList Morph::getMorphMaps() const
{
  return morphMaps;
}

void Morph::removeMorphMap(MorphMap* morphMap)
{
  morphMaps.remove(morphMap);
  delete morphMap;
}

void Morph::setKeyboardAssignment(Morph::KeyboardAssignment keyboardAssignment)
{
  this->keyboardAssignment = keyboardAssignment;
}

Morph::KeyboardAssignment Morph::getKeyboardAssignment()
{
  return keyboardAssignment;
}
