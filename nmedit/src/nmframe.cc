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

#include "nmframe.h"

NMFrame::NMFrame(const wxChar *title,
		 int xpos, int ypos,
		 int width, int height)
  : wxFrame((wxFrame *) NULL,
	    -1,
	    title,
	    wxPoint(xpos, ypos),
	    wxSize(width, height))
{
  menuBar = 0;
  fileMenu = 0;
  helpMenu = 0;

  fileMenu = new wxMenu;
  fileMenu->Append(NM_OPEN, "Open...");
  fileMenu->Append(NM_SAVE, "Save");
  fileMenu->Append(NM_SAVEAS, "Save As...");
  fileMenu->Append(NM_CLOSE, "Close");
  fileMenu->AppendSeparator();
  fileMenu->Append(NM_LOAD, "Load...");
  fileMenu->Append(NM_STORE, "Store...");
  fileMenu->AppendSeparator();
  fileMenu->Append(NM_EXIT, "Exit");
  
  helpMenu = new wxMenu;
  helpMenu->Append(NM_ABOUT, "About...");

  menuBar = new wxMenuBar;
  menuBar->Append(fileMenu, "File");
  menuBar->Append(helpMenu, "Help");
  SetMenuBar(menuBar);
  
  CreateStatusBar(1);
}

NMFrame::~NMFrame()
{

}
