/*
    Nord Modular patch file format 3.03 parser
    Copyright (C) 2003 Marcus Andersson

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

#ifndef PATCHEXCEPTION_H
#define PATCHEXCEPTION_H

#include <string>

using namespace std;

class PatchException
{
 public:

  PatchException(string message, int error) {
    this->message = message;
    this->error = error;
  }

  virtual ~PatchException() {
  }
  
  string getMessage() {
    return message;
  }

  int getError() {
    return error;
  }

 private:
  
  string message;
  int error;

};

#endif
