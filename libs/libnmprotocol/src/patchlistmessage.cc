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

#include "nmprotocol/patchlistmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "pdl/packet.h"

PatchListMessage::PatchListMessage(int section, int position)
{
  cc = 0x17;
  slot = 0;
  this->section = section;
  this->position = position;
  wantAck = false;
}

PatchListMessage::PatchListMessage(Packet* packet)
{
  slot = packet->getVariable("slot");
  section =
    packet->getPacket("data")->getPacket("patchList")->getVariable("section");
  section =
    packet->getPacket("data")->getPacket("patchList")->getVariable("position");

  Packet* list =
    packet->getPacket("data")->getPacket("patchList")->getPacket("names");
  while (list != 0) {
    names.push_back(getName(list->getPacket("name")));
    list = list->getPacket("next");
  }
}

PatchListMessage::~PatchListMessage()
{
}

void PatchListMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  
  BitStream bitStream;
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);
}

void PatchListMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

int PatchListMessage::getSection()
{
  return section;
}

int PatchListMessage::getPosition()
{
  return position;
}

PatchListMessage::StringList PatchListMessage::getNames()
{
  return names;
}
