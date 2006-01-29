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

#ifndef RTMIDIDRIVER_H
#define RTMIDIDRIVER_H

#ifdef RTMIDI

#include "nmprotocol/RtMidi.h"
#include "nmprotocol/mididriver.h"

using namespace std;

class RtMidiDriver : public virtual MidiDriver
{
 public:

  RtMidiDriver();
  virtual ~RtMidiDriver();

  virtual StringList getMidiInputPorts();
  virtual StringList getMidiOutputPorts();

  virtual void connect(string midiInputPort, string midiOutputPort);
  virtual void disconnect();

  virtual void send(Bytes bytes);
  virtual void receive(Bytes& bytes);

 private:
  RtMidiIn *midi_in;
  RtMidiOut *midi_out;

  virtual void init();
  virtual StringList getMidiPortNames(RtMidi *port);
  virtual void openPortByName(RtMidi *port, string name);
};

#endif // ALSA

#endif
