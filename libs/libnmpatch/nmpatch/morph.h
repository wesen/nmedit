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

#ifndef MORPH_H
#define MORPH_H

#include "nmpatch/module.h"
#include "nmpatch/morphmap.h"
#include "nmpatch/modulesection.h"
#include <list>

class Morph
{
 public:

  enum Type {
    MORPH1 = 0,
    MORPH2,
    MORPH3,
    MORPH4
  };

  enum KeyboardAssignment {
    NONE = 0,
    VELOCITY,
    NOTE
  };

  typedef list<MorphMap*> MorphMapList;

  Morph();

  virtual ~Morph();

  void setValue(int);
  int getValue();

  MorphMap* newMorphMap(ModuleSection::Type, Module*, ModuleType::Parameter);
  MorphMapList getMorphMaps() const;
  void removeMorphMap(MorphMap*);

  void setKeyboardAssignment(KeyboardAssignment);
  KeyboardAssignment getKeyboardAssignment();

 private:

  int value;
  MorphMapList morphMaps;
  KeyboardAssignment keyboardAssignment;
};

#endif
