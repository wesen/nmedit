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

#include "nmprotocol/voicecountmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

VoiceCountMessage::VoiceCountMessage()
{
  cc = 0;
  slot = 0;
  voices[0] = 0;
  voices[1] = 0;
  voices[2] = 0;
  voices[3] = 0;
}

VoiceCountMessage::VoiceCountMessage(Packet* packet)
{
  cc = packet->getVariable("cc");
  slot = packet->getVariable("slot");
  voices[0] = packet->getPacket("data")->getPacket("data")->getVariable("c0");
  voices[1] = packet->getPacket("data")->getPacket("data")->getVariable("c1");
  voices[2] = packet->getPacket("data")->getPacket("data")->getVariable("c2");
  voices[3] = packet->getPacket("data")->getPacket("data")->getVariable("c3");
}

VoiceCountMessage::~VoiceCountMessage()
{
}

void VoiceCountMessage::getBitStream(BitStreamList* bitStreamList)
{
  throw MidiException("VoiceCountMessage::getBitStream not implemented.", 0);
}

void VoiceCountMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

int VoiceCountMessage::getVoiceCount(int slot)
{
  return voices[slot];
}
