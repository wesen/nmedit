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

#include "nmprotocol/slotsselectedmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

#include <stdio.h>

SlotsSelectedMessage::SlotsSelectedMessage()
{
  slot = 0;
  sc = 0x07;
  selected[0] = false;
  selected[1] = false;
  selected[2] = false;
  selected[3] = false;

  expectsreply = true;
}

SlotsSelectedMessage::SlotsSelectedMessage(Packet* packet)
{
  slot = packet->getVariable("slot");
  selected[0] =
    packet->getPacket("data")->getPacket("data")->getVariable("slot0");
  selected[1] =
    packet->getPacket("data")->getPacket("data")->getVariable("slot1");
  selected[2] =
    packet->getPacket("data")->getPacket("data")->getVariable("slot2");
  selected[3] =
    packet->getPacket("data")->getPacket("data")->getVariable("slot3");
}

SlotsSelectedMessage::~SlotsSelectedMessage()
{
}

void SlotsSelectedMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  intStream.append(0x17);
  intStream.append(slot);
  intStream.append(0x41);
  intStream.append(sc);
  intStream.append(selected[0]);
  intStream.append(selected[1]);
  intStream.append(selected[2]);
  intStream.append(selected[3]);
  addChecksum(&intStream);

  BitStream bitStream;
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  
}

void SlotsSelectedMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

bool SlotsSelectedMessage::isSelected(int slot)
{
  return selected[slot];
}

void SlotsSelectedMessage::setSelected(int slot, bool state)
{
  selected[slot] = state;
}

