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

#ifndef NMPROTOCOL_H
#define NMPROTOCOL_H

#include <list>

#include "nmprotocol/mididriver.h"

class MidiDriver;
class NMProtocolListener;
class MidiMessage;

class NMProtocol
{
 public:

  NMProtocol();
  virtual ~NMProtocol();
  
  void useMidiDriver(MidiDriver* midiDriver);

  void addListener(NMProtocolListener* listener);
  void removeListener(NMProtocolListener* listener);
  void notifyListeners(MidiMessage* midiMessage);

  void heartbeat();

  void send(MidiMessage* midiMessage);

 private:
  
  typedef list<NMProtocolListener*> ListenerList;
  typedef list<MidiDriver::Bytes> MessageList;

  MidiDriver* midiDriver;
  ListenerList listeners;
  MessageList sendQueue;
};

#endif
