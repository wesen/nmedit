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

#include "sectionview.h"

#include "moduleview.h"

#include "nmpatch/modulesection.h"

#include "FL/Fl.H"
#include "FL/Fl_Group.H"

SectionView::SectionView(ModuleSection* section, Fl_Group* parent)
{
  this->section = section;
  this->parent = parent;
  section->addListener(this);

  //group = new Fl_Group(0, 0, 1024, 768, 0);
  
  ModuleSection::ModuleList moduleList = section->getModules();
  for (ModuleSection::ModuleList::iterator i = moduleList.begin();
       i != moduleList.end(); i++) {
    ModuleView* moduleView = new ModuleView(*i, parent);
  }
  
  //group->end();
}

SectionView::~SectionView()
{
  delete group;
}

void SectionView::newModule(Module* module, int index)
{
  ModuleView* moduleView = new ModuleView(module, parent);
}
