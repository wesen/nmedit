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

#include "nmprotocol/lightmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

LightMessage::LightMessage()
{
  cc = 0x14;
  slot = 0;
  pid = 0;
  startIndex = 0;
}

LightMessage::LightMessage(Packet* packet)
{
  cc = packet->getVariable("cc");
  slot = packet->getVariable("slot");
  pid = packet->getVariable("data:pid");
  startIndex = packet->getVariable("data:data:startIndex");
  lights[0] = packet->getVariable("data:data:l0");
  lights[1] = packet->getVariable("data:data:l1");
  lights[2] = packet->getVariable("data:data:l2");
  lights[3] = packet->getVariable("data:data:l3");
  lights[4] = packet->getVariable("data:data:l4");
  lights[5] = packet->getVariable("data:data:l5");
  lights[6] = packet->getVariable("data:data:l6");
  lights[7] = packet->getVariable("data:data:l7");
  lights[8] = packet->getVariable("data:data:l8");
  lights[9] = packet->getVariable("data:data:l9");
  lights[10] = packet->getVariable("data:data:l10");
  lights[11] = packet->getVariable("data:data:l11");
  lights[12] = packet->getVariable("data:data:l12");
  lights[13] = packet->getVariable("data:data:l13");
  lights[14] = packet->getVariable("data:data:l14");
  lights[15] = packet->getVariable("data:data:l15");
  lights[16] = packet->getVariable("data:data:l16");
  lights[17] = packet->getVariable("data:data:l17");
  lights[18] = packet->getVariable("data:data:l18");
  lights[19] = packet->getVariable("data:data:l19");
}

LightMessage::~LightMessage()
{
}

void LightMessage::getBitStream(BitStreamList* bitStreamList)
{
  throw MidiException("LightMessage::getBitStream not implemented.", 0);
}

void LightMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

int LightMessage::getStartIndex()
{
  return startIndex;
}

int LightMessage::getLightStatus(int lightNo)
{
  if (lightNo >= 0 && lightNo <= 19) {
    return lights[lightNo];
  }
  return 0;
}

int LightMessage::getPid()
{
  return pid;
}
