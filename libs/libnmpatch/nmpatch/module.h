/*
    Nord Modular patch file format 3.03 parser
    Copyright (C) 2002 Marcus Andersson

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

#ifndef MODULE_H
#define MODULE_H

#include <string>
#include <map>
#include <list>

using namespace std;

#include "nmpatch/moduletype.h"

class ModuleListener;

class Module
{
 public:
  
  typedef map<ModuleType::Parameter, int> ParameterMap;
  typedef map<ModuleType::CustomValue, int> CustomValueMap;

  Module(ModuleType*, int);

  virtual ~Module();

  int getIndex();
  ModuleType* getType();
  
  void setPosition(int, int);
  int getXPosition();
  int getYPosition();

  void setName(string);
  string getName();

  void setParameter(ModuleType::Parameter, int);
  int getParameter(ModuleType::Parameter);
  string getMappedParameter(ModuleType::Parameter);

  void setCustomValue(ModuleType::CustomValue, int);
  int getCustomValue(ModuleType::CustomValue);
  string getMappedCustomValue(ModuleType::CustomValue);

  void addListener(ModuleListener*);
  void removeListener(ModuleListener*);

  void notifyListeners(ModuleType::Parameter, int);

 private:
  
  typedef list<ModuleListener*> ModuleListenerList;

  ModuleType* type;
  int index;
  int xpos;
  int ypos;
  string name;
  ParameterMap parameters;
  CustomValueMap customValues;
  ModuleListenerList listeners;
  
};
    
#endif
