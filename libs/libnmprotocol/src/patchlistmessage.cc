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
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

const int PatchListMessage::END_OF_SECTION = 3;
const int PatchListMessage::EMPTY_POSITION = 2;

PatchListMessage::PatchListMessage(int section, int position)
{
  cc = 0x17;
  slot = 0;
  this->section = section;
  this->position = position;
  endoflist = false;

  isreply = true;
}

PatchListMessage::PatchListMessage(Packet* packet)
{
  slot = packet->getVariable("slot");
  Packet* patchList = packet->getPacket("data:patchList:data");

  if (patchList != 0) {
    section = patchList->getVariable("section");
    position = patchList->getVariable("position");

    Packet* list = patchList->getPacket("names");
    while (list != 0) {
      string name;
      name += list->getVariable("first");
      if (name[0] != 0x02) {
	name += extractName(list->getPacket("name"));
      }
      names.push_back(name);
      list = list->getPacket("next");
    }
    endoflist = false;
  }
  else {
    section = 0;
    position = 0;
    endoflist = true;
  }

  isreply = true;
}

PatchListMessage::~PatchListMessage()
{
}

void PatchListMessage::getBitStream(BitStreamList* bitStreamList)
{
  throw MidiException("PatchListMessage::getBitStream not implemented.", 0);
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

bool PatchListMessage::endOfList()
{
  return endoflist;
}

bool PatchListMessage::endOfSection(string name)
{
  return name[0] == END_OF_SECTION;
}

bool PatchListMessage::emptyPosition(string name)
{
  return name[0] == EMPTY_POSITION;
}

PatchListMessage::StringList PatchListMessage::getNames()
{
  return names;
}
