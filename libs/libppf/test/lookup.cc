/*
    Programmable Property Files
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

#include "ppf/parser.h"
#include "ppf/programmablepropertyexception.h"

using namespace ppf;

int main(int argc, char** argv)
{
  if (argc < 3) {
    printf("usage: %s ppf_file {bundle*} property\n", argv[0]);
    exit(1);
  }

  try {
    Parser parser;
    BoundBundle bb = parser.parse(argv[1]);
    int n = 0;
    while (argc > (3+n)) {
      bb = bb.getBoundBundle(argv[2+n]);
      n++;
    }
    printf("\"%s\"\n", bb.getProperty(argv[2+n]).c_str());
  }
  catch(ProgrammablePropertyException& exception) {
    printf("ProgrammablePropertyException: %s (%d)\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
}
