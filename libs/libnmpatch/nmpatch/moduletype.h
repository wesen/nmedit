/*
    Nord Modular patch file format 3.03 parser
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

#ifndef MODULETYPE_H
#define MODULETYPE_H

#include <string>

using namespace std;

namespace ppf {
  class BoundBundle;
}

class Module;

class ModuleType
{
 public:
  
  typedef int TypeId;
  typedef int Port;
  typedef int Parameter;
  typedef int CustomValue;

  enum IOType {
    AUDIO = 0,
    SLAVE,
    CONTROL,
    LOGIC};

  enum ConnectorType {
    INPUT = 0,
    OUTPUT
  };

  ModuleType();

  virtual ~ModuleType();

  ModuleType(TypeId, ppf::BoundBundle*, ppf::BoundBundle*);

  TypeId getId();
  
  string getName();
  double getCycles();
  double getProgmem();
  double getXmem();
  double getYmem();
  double getZeropage();
  double getDynmem();
  int getHeight();
  
  int numberOfParameters();
  string getParameterName(Parameter);
  int getParameterMin(Parameter);
  int getParameterMax(Parameter);
  string mapParameter(Parameter, int, Module*);
  
  int numberOfPorts(ConnectorType);
  string getPortName(ConnectorType, Port);
  IOType getPortType(ConnectorType, Port);

  int numberOfCustomValues();
  string getCustomValueName(CustomValue);
  int getCustomValueMin(CustomValue);
  int getCustomValueMax(CustomValue);
  string mapCustomValue(CustomValue, int);
  
 private:
  
  TypeId id;
  ppf::BoundBundle* properties;
  ppf::BoundBundle* maps;

  string getItemString(string, int, string);
  int countItems(string);

};
    
#endif
