/*
    Nord Modular Midi Protocol 3.03 Library
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

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"

#ifdef ALSA
#include "nmprotocol/alsadriver.h"
#endif

const unsigned char MidiDriver::SYSEX_END = 0xf7;

MidiDriver::MidiDriver()
{
}

MidiDriver::~MidiDriver()
{
}

MidiDriver::StringList MidiDriver::getDrivers()
{
  StringList drivers;
  
#ifdef ALSA
  drivers.push_back("ALSA");
#endif

  return drivers;
}

MidiDriver* MidiDriver::createDriver(string driverName)
{
  #ifdef ALSA
  if (driverName == "ALSA") {
    return new ALSADriver();
  }
  #endif

  throw MidiException(string("Driver not available: ") + driverName, 0);

  return 0;
}
