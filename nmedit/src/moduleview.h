/*
    nmEdit
    Copyright (C) 2005 Marcus Andersson

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

#ifndef MODULEVIEW_H
#define MODULEVIEW_H

#include "nmpatch/moduletype.h"
#include "nmpatch/modulelistener.h"

class Module;
class Fl_Widget;
class Fl_Group;
class Fl_Output;
class Fl_Input;
class Fl_Slider;

class ModuleView : public ModuleListener
{
 public:
  
  ModuleView(Module* module, Fl_Group* parent);
  
  virtual ~ModuleView();

  virtual void parameterChanged(ModuleType::Parameter, int);
  virtual void positionChanged(int, int);

 private:

  Module* module;
  ModuleType* type;
  Fl_Group* group;
  Fl_Group* parent;
  Fl_Output* output;
  Fl_Input* input;
  Fl_Slider** sliders;

};

#endif
