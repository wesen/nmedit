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

#include "moduleview.h"

#include "nmpatch/module.h"
#include "nmpatch/moduletype.h"

#include "FL/Fl.H"
#include "FL/Fl_Group.H"
#include "FL/Fl_Window.H"
#include "FL/Fl_Slider.H"
#include "FL/Fl_Output.H"
#include "FL/Fl_Input.H"

#include <typeinfo>
#include <utility>

void modified_cb(Fl_Widget* w, void* v) {
  Fl_Slider* s = dynamic_cast<Fl_Slider*>(w);
  pair<Module*, int>* p = (pair<Module*, int>*)v;
  Module* m = (Module*)p->first;
  int parameter = p->second;

  m->setParameter(parameter, (int)s->value());
}

ModuleView::ModuleView(Module* module, Fl_Group* parent)
{
  this->module = module;
  this->parent = parent;

  type = module->getType();
  module->addListener(this);

  int x = module->getXPosition()*300;
  int y = module->getYPosition()*30;

  //group = new Fl_Group(x, y, 400, 500, 0);

  string* label = new string(type->getName());
  output = new Fl_Output(x+2, y, 146, 14, 0);
  output->value(label->c_str());
  output->textsize(9);
  parent->add(output);
  output->show();

  string* name = new string(module->getName());
  input = new Fl_Input(x+2+150, y, 146, 14, 0);
  input->value(name->c_str());
  input->textsize(9);
  parent->add(input);
  input->show();

  sliders = (Fl_Slider**)malloc(sizeof(Fl_Slider*)*type->numberOfParameters());

  for (int i = 0; i < type->numberOfParameters(); i++) {
    string* foo = new string();
    *foo = type->getParameterName(i);
    sliders[i] = 
      new Fl_Slider(x+2+(i%3)*100, y+(i/3+1)*19, 96, 8, foo->c_str());
    sliders[i]->labelsize(9);
    sliders[i]->type(FL_HOR_FILL_SLIDER);
    sliders[i]->precision(0);
    sliders[i]->minimum(type->getParameterMin(i));
    sliders[i]->maximum(type->getParameterMax(i));
    sliders[i]->value(module->getParameter(i));
    sliders[i]->callback((Fl_Callback*)modified_cb,
			 new pair<Module*, int>(module, i));
    parent->add(sliders[i]);
    sliders[i]->show();
  }
  
  //group->end();
}

ModuleView::~ModuleView()
{
  delete group;
}

void ModuleView::parameterChanged(ModuleType::Parameter param, int value)
{
  if (sliders[param]->value() != value) {
    sliders[param]->value(value);
  }
}

void ModuleView::positionChanged(int x, int y)
{
  x = x * 300;
  y = y * 30;

  output->position(x+2, y);
  output->damage(1);

  input->position(x+2+150, y);
  input->damage(1);

  for (int i = 0; i < type->numberOfParameters(); i++) {
    sliders[i]->position(x+2+(i%3)*100, y+(i/3+1)*19);
    sliders[i]->damage(1);
  }  

  parent->redraw();
}

