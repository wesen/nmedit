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

#include "nmprotocol/nmprotocol.h"
#include "nmprotocol/mididriver.h"
#include "nmprotocol/midimessage.h"

NMProtocol::NMProtocol()
{
}

NMProtocol::~NMProtocol()
{
}

void NMProtocol::useMidiDriver(MidiDriver* midiDriver)
{
  this->midiDriver = midiDriver;
}

void NMProtocol::addListener(NMProtocolListener* listener)
{
  listeners.push_back(listener);
}

void NMProtocol::removeListener(NMProtocolListener* listener)
{
  listeners.remove(listener);
}

void NMProtocol::notifyListeners(MidiMessage* message)
{
  for (ListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    message->notifyListener(*i);
  }
}

void NMProtocol::heartbeat()
{
  MidiDriver::Bytes receiveBytes;
  BitStream bitStream;

  if (sendQueue.size() > 0) {
    midiDriver->send(sendQueue.front());
    sendQueue.pop_front();
  }

  midiDriver->receive(receiveBytes);

  if (receiveBytes.size() > 0) {
    for (MidiDriver::Bytes::iterator i = receiveBytes.begin();
	 i != receiveBytes.end(); i++) {
      bitStream.append(*i, 8);
    }
    MidiMessage* midiMessage = MidiMessage::create(&bitStream);
    if (midiMessage != 0) {
      notifyListeners(midiMessage);
      delete midiMessage;
    }
  }
}

void NMProtocol::send(MidiMessage* midiMessage)
{
  BitStream bitStream;
  MidiDriver::Bytes sendBytes;

  midiMessage->getBitStream(&bitStream);
  while(bitStream.isAvailable(8)) {
    sendBytes.push_back((unsigned char)bitStream.getInt(8));
  }
  sendQueue.push_back(sendBytes);
}
