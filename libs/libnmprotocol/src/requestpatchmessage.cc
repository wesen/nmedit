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

#include "nmprotocol/requestpatchmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

RequestPatchMessage::RequestPatchMessage()
{
  cc = 0x17;
  slot = 0;
  pp = 0x41;
  ssc = 0x35;

  expectsreply = true;
}

RequestPatchMessage::RequestPatchMessage(Packet* packet)
{
  throw MidiException
    ("RequestPatchMessage(Packet* packet) not implemented", 0);
}

RequestPatchMessage::~RequestPatchMessage()
{
}

void RequestPatchMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  intStream.append(cc);
  intStream.append(slot);
  intStream.append(pp);
  intStream.append(ssc);
  MidiMessage::addChecksum(&intStream);
  
  BitStream bitStream;
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);
}

void RequestPatchMessage::notifyListener(NMProtocolListener* listener)
{
  throw MidiException
    ("RequestPatchMessage::notifyListener() not implemented", 0);
}
