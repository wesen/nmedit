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

#include "mainwindow.h"

#include "sectionview.h"

#include "nmpatch/patch.h"

#include <FL/Fl.H>
#include <FL/Fl_Window.H>
#include <FL/Fl_Tabs.H>

MainWindow::MainWindow()
{
  window = new Fl_Window(1024, 768);
  window->size_range(0, 0, 2000, 2000);
  window->show();
  sectionView = 0;
}

MainWindow::~MainWindow()
{
  delete sectionView;
  delete window;
}

void MainWindow::newPatch(Patch* patch)
{
  if (sectionView == 0) {
    window->begin();
    sectionView =
      new SectionView(patch->getModuleSection(ModuleSection::POLY), window);
    window->end();
  }
}
