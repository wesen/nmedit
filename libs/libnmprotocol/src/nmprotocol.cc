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
#include "nmprotocol/patchmessage.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/enqueuedpacket.h"

int NMProtocol::TIMEOUT_INTERVAL = 3; /* seconds */

NMProtocol::NMProtocol(MidiDriver* midiDriver)
{
  if (midiDriver == 0) {
    throw MidiException("Null MidiDriver not allowed.", 0);
  }

  timeout = 0;
  this->midiDriver = midiDriver;
}

NMProtocol::~NMProtocol()
{
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

  if (timeout != 0 && time(0) > timeout) {
    sendQueue.clear();
    throw MidiException("Communication timed out.", timeout);
    timeout = 0;
  }

  if (sendQueue.size() > 0 && timeout == 0) {
    midiDriver->send(sendQueue.front().getContent());

    // Set up timer for expected reply message
    if (sendQueue.front().expectsReply()) {
      timeout = time(0) + TIMEOUT_INTERVAL;
    }
    else {
      timeout = 0;
      sendQueue.pop_front();
    }
  }

  midiDriver->receive(receiveBytes);

  while (receiveBytes.size() > 0) {
    for (MidiDriver::Bytes::iterator i = receiveBytes.begin();
	 i != receiveBytes.end(); i++) {
      bitStream.append(*i, 8);
    }
    MidiMessage* midiMessage = MidiMessage::create(&bitStream);
    if (midiMessage != 0) {
      // Check if message is a reply
      if (midiMessage->isReply()) {
	if (sendQueue.size() > 0 && timeout != 0) {
	  sendQueue.pop_front();
	  timeout = 0;
	}
	else {
	  throw MidiException("Unexpected reply message received.", 0);
	}
      }
      notifyListeners(midiMessage);
      delete midiMessage;
    }

    receiveBytes.clear();
    bitStream.setSize(0);
    midiDriver->receive(receiveBytes);
  }
}

void NMProtocol::send(MidiMessage* midiMessage)
{
  MidiMessage::BitStreamList bitStreamList;

  midiMessage->getBitStream(&bitStreamList);
  for(MidiMessage::BitStreamList::iterator i = bitStreamList.begin();
      i != bitStreamList.end(); i++) {
    MidiDriver::Bytes sendBytes;
    BitStream bitStream = (*i);
    while(bitStream.isAvailable(8)) {
      sendBytes.push_back((unsigned char)bitStream.getInt(8));
    }
    sendQueue.push_back(EnqueuedPacket(sendBytes,
				       midiMessage->expectsReply()));
  }
}

bool NMProtocol::sendQueueIsEmpty()
{
  return sendQueue.size() == 0;
}
 
