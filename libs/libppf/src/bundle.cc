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

#include "ppf/bundle.h"
#include "ppf/programmablepropertyexception.h"


Bundle::Bundle(Tcl_interp* interp)
{
  this->interp = interp;
}

Bundle* Bundle::newBundle(string regexp)
{
  Bundle* bundle = new Bundle(level + 1, interp);
  bundles[regexp] = bundle;
}

Bundle* Bundle::getBundle(string name, string bindings)
{
  for (BundleMap::iterator n = bundles.begin(); n != bundles.end(); n++) {
    Tcl_Eval(interp,
	     (bindings + " regexp " + (*n).first + " " + name).c_str());
    if (string("1") == interp->result) {
      return (*n).second;
    }
  }
  throw ProgrammablePropertyException(string("Missing bundle: ") + name, 0);
  return 0;
}

void Bundle::newProperty(string regexp, string expr)
{
  properties[regexp] = expr;
}

string Bundle::getProperty(string name, int level, string bindings)
{
  for (PropertyMap::iterator n = properties.begin();
       n != properties.end(); n++) {
    Tcl_Eval(interp,
	     (bindings + " regexp " + (*n).first + " " + name).c_str());
    if (string("1") == interp->result) {
      Tcl_Eval(interp,
	       (bindings + " set $" + level + " " + name + ";" +
		" return " + (*n).second ";").c_str());
      return string(interp->result);
    }
  }
  throw ProgrammablePropertyException(string("Missing property: ") + name, 1);
  return "";
}

void Bundle::~Bundle()
{
  for (BundleMap::iterator n = bundles.begin(); n != bundles.end(); n++) {
    delete (*n).second;
  }
}
