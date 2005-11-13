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

#include "nmprotocol/patchmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"

#include "pdl/packet.h"

void PatchMessage::init()
{
  cc = 0x1c;
  slot = 0;
  pid = 0;

  expectsreply = true;
  isreply = true;
}

PatchMessage::PatchMessage(BitStream patchStream,
			   PositionList sectionEndPositions)
{
  IntStream intStream;

  init();

  // Create sysex messages
  int first = 1;
  int last = 0;
  int messageNumber = 0;
  while (patchStream.isAvailable(8)) {

    // Get data for one sysex packet
    BitStream partialPatchStream;
    int sectionsEnded = 0;
    int n = 0;
    while (patchStream.isAvailable(8) && n < 166) {
      partialPatchStream.append(patchStream.getInt(8), 8);
      if (n == (sectionEndPositions.front() - 166*messageNumber)) {
	sectionsEnded++;
	sectionEndPositions.pop_front();
      }
      n++;
    }
    messageNumber++;

    if (!patchStream.isAvailable(8)) {
      last = 1;
    }

    // Pad. Extra bits are ignored later.
    partialPatchStream.append(0, 6);

    // Generate sysex bistream
    IntStream intStream;
    intStream.append(cc + first + 2*last);
    first = 0;
    intStream.append(slot);
    intStream.append(0x01);
    intStream.append(sectionsEnded);
    while (partialPatchStream.isAvailable(7)) {
      intStream.append(partialPatchStream.getInt(7));
    }
    appendChecksum(&intStream);

    // Generate sysex bitstream
    BitStream bitStream;
    MidiMessage::getBitStream(intStream, &bitStream);
    bitStreamList.push_back(bitStream);
  }
}

PatchMessage::PatchMessage()
{
  init();
}

void PatchMessage::append(Packet* packet)
{
  slot = packet->getVariable("slot");
  pid = packet->getVariable("data:pid");
  
  packet = packet->getPacket("data:next");
  while (packet != 0) {
    patchStream.append(packet->getVariable("data"), 7);
    packet = packet->getPacket("next");
  }
  // Remove checksum
  patchStream.setSize(patchStream.getSize()-7);
  // Remove padding
  patchStream.setSize((patchStream.getSize()/8)*8);
}

PatchMessage::~PatchMessage()
{
}

void PatchMessage::getBitStream(BitStreamList* bitStreamList)
{
  *bitStreamList = this->bitStreamList;
}

void PatchMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

BitStream PatchMessage::getPatchStream()
{
  return patchStream;
}

int PatchMessage::getPid()
{
  return pid;
}
