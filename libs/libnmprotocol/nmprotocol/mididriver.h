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

#ifndef MIDIDRIVER_H
#define MIDIDRIVER_H

#include <string>
#include <list>
#include <vector>

using namespace std;

class MidiDriver
{
 public:

  typedef list<string> StringList;
  typedef vector<unsigned char> Bytes;

  static const unsigned char SYSEX_END;

  virtual ~MidiDriver();

  /**
     Get a list with the available drivers for the platform. This call
     and the createDriver() call is configured at compile time to
     contain code that is relevant for the platform.
  */
  static StringList getDrivers();
  static MidiDriver* createDriver(string driverName);

  /**
     Get lists with midi i/o ports to be used in connect().
  */
  virtual StringList getMidiInputPorts() = 0;
  virtual StringList getMidiOutputPorts() = 0;

  /**
     Connect to the synthesizer.
  */
  virtual void connect(string midiInputPort, string midiOutputPort) = 0;
  virtual void disconnect() = 0;

  /**
     Send bytes to the outputPort given in connect(). The call blocks
     until all bytes are sent.
  */
  virtual void send(Bytes& bytes) = 0;

  /**
     Receive bytes from the inputPort given in connect(). The call is
     non-blocking and will return zero bytes if it fails to fetch one
     full sysex packet from the inputPort. No more than one sysex
     packet is returned each time receive() is called.
  */
  virtual void receive(Bytes& bytes) = 0;

 protected:

  MidiDriver();

};

#endif
