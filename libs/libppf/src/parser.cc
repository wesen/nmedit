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
#include "ppf/bundle.h"
#include "ppf/ppfexception.h"

#include <fstream>

using namespace ppf;

Parser::Parser()
{
  interp = Tcl_CreateInterp();
  if (Tcl_Init(interp) == TCL_ERROR) {
    throw PPFException("Failed to init TCL interpreter.", 3);
  }
  rootBundle = new Bundle(interp);  
}

BoundBundle Parser::parse(string filename)
{
  string line;
  ifstream file(filename.c_str());
  Bundle* currentBundle;
  
  if (file.fail()) {
    throw
      PPFException(string("Failed to open file: ") + filename,
				    5);
  }

  while (!file.eof()) {
    getline(file, line);
    line = trim(line);
    if (line.length() > 0 && line[0] != '#') {
      StringList bundles;
      string property;
      string expression;
      parse(line, &bundles, &property, &expression);
      currentBundle = rootBundle;
      for (StringList::iterator name = bundles.begin();
	   name != bundles.end(); name++) {
	currentBundle = currentBundle->newBundle(*name);
      }
      currentBundle->newProperty(property, expression);
    }
  }
  file.close();
  return BoundBundle(rootBundle, 0, "");
}

Parser::~Parser()
{
  delete rootBundle;
}

string Parser::trim(string line)
{
  while (line.length() > 0 && (line[0] == ' ' || line[0] == '\t')) {
    line = line.substr(1);
  }
  return line.substr(0, line.length() - 1);
}

void Parser::parse(string line,
		   StringList* bundles, string* property, string* expression)
{
  string token;
  while (line.find(" ") < line.length()) {
    token = line.substr(0, line.find(" "));
    replace(&token, "[", "\\[");
    replace(&token, "]", "\\]");
    line = line.substr(line.find(" ") + 1);
    if (token == "=") {
      break;
    }
    bundles->push_back(token);
  }
  if (token != "=") {
    throw
      PPFException(string("Missing '=' sign: ") + line, 2);
  }
  *expression = line;
  *property = bundles->back();
  bundles->pop_back();
}

void Parser::replace(string* token, string match, string replacement)
{
  string::size_type pos = token->find(match);
  while(pos < token->length()) {
    *token = token->replace(pos, match.length(), replacement);
    pos = token->find(match, pos + replacement.length());
  }
}
