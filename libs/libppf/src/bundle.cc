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

using namespace ppf;

Bundle::Bundle(Tcl_Interp* interp)
{
  this->interp = interp;
}

Bundle* Bundle::newBundle(string regexp)
{
  Bundle* bundle;
  if (bundles.find(regexp) == bundles.end()) {
    bundle = new Bundle(interp);
    bundles[regexp] = bundle;
  }
  else {
    bundle = bundles[regexp];
  }
  return bundle;
}

Bundle* Bundle::getBundle(string name, string bindings)
{
  for (BundleMap::iterator n = bundles.begin(); n != bundles.end(); n++) {
    Tcl_Eval(interp,
	     (char*)(bindings + " regexp ^" + (*n).first + "$ " + name).c_str());
    if (string("1") == interp->result) {
      return (*n).second;
    }
  }
  throw ProgrammablePropertyException(string("Missing bundle: ") +
				      bindings + " " + name, 0);
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
	     (char*)(bindings + " regexp ^" + (*n).first + "$ " + name).c_str());
    if (string("1") == interp->result) {
      char slevel[11];
      snprintf(slevel, 10, "%d", level);
      Tcl_Eval(interp,
	       (char*)(bindings + " set " + slevel + " " + name + ";" +
		" return " + (*n).second + ";").c_str());
      return string(interp->result);
    }
  }
  throw ProgrammablePropertyException(string("Missing property: ") +
				      bindings + " " + name, 1);
  return "";
}

Bundle::~Bundle()
{
  for (BundleMap::iterator n = bundles.begin(); n != bundles.end(); n++) {
    delete (*n).second;
  }
}
