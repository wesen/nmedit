/*
    nmEdit
    Copyright (C) 2004 Marcus Andersson

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

#ifndef NMFRAME_H
#define NMFRAME_H

#include <wx/wx.h>

class NMFrame : public wxFrame
{
 public:

  enum {
    NM_OPEN = 1,
    NM_SAVE,
    NM_SAVEAS,
    NM_CLOSE,
    NM_LOAD,
    NM_STORE,
    NM_EXIT,
    NM_ABOUT
  };

  NMFrame(const wxChar* title, 
	  int xpos, int ypos, 
	  int width, int height);

  virtual ~NMFrame();

 private:

  wxMenuBar* menuBar;
  wxMenu* fileMenu;
  wxMenu* helpMenu;
  
};

#endif
