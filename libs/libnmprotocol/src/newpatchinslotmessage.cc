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

#include "nmprotocol/newpatchinslotmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

NewPatchInSlotMessage::NewPatchInSlotMessage()
{
  cc = 0x14;
  slot = 0;
  pid = 0;
}

NewPatchInSlotMessage::NewPatchInSlotMessage(Packet* packet)
{
  cc = packet->getVariable("cc");
  slot = packet->getVariable("slot");
  pid = packet->getVariable("data:pid");
}

NewPatchInSlotMessage::~NewPatchInSlotMessage()
{
}

void NewPatchInSlotMessage::getBitStream(BitStreamList* bitStreamList)
{
  throw
    MidiException("NewPatchInSlotMessage::getBitStream not implemented.", 0);
}

void NewPatchInSlotMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

int NewPatchInSlotMessage::getPid()
{
  return pid;
}
