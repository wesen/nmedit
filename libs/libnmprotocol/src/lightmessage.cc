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
#include "pdl/packet.h"

LightMessage::LightMessage()
{
  cc = 0x14;
  slot = 0;
  pid = 0;
  sc = 0x39;
  startIndex = 0;
}

LightMessage::LightMessage(Packet* packet)
{
  cc = packet->getVariable("cc");
  slot = packet->getVariable("slot");
  pid = packet->getPacket("data")->getVariable("pid");
  sc = packet->getPacket("data")->getVariable("sc");
  startIndex =
    packet->getPacket("data")->getPacket("data")->getVariable("startIndex");

  lights[0] =
    packet->getPacket("data")->getPacket("data")->getVariable("l0");
  lights[1] =
    packet->getPacket("data")->getPacket("data")->getVariable("l1");
  lights[2] =
    packet->getPacket("data")->getPacket("data")->getVariable("l2");
  lights[3] =
    packet->getPacket("data")->getPacket("data")->getVariable("l3");
  lights[4] =
    packet->getPacket("data")->getPacket("data")->getVariable("l4");
  lights[5] =
    packet->getPacket("data")->getPacket("data")->getVariable("l5");
  lights[6] =
    packet->getPacket("data")->getPacket("data")->getVariable("l6");
  lights[7] =
    packet->getPacket("data")->getPacket("data")->getVariable("l7");
  lights[8] =
    packet->getPacket("data")->getPacket("data")->getVariable("l8");
  lights[9] =
    packet->getPacket("data")->getPacket("data")->getVariable("l9");
  lights[10] =
    packet->getPacket("data")->getPacket("data")->getVariable("l10");
  lights[11] =
    packet->getPacket("data")->getPacket("data")->getVariable("l11");
  lights[12] =
    packet->getPacket("data")->getPacket("data")->getVariable("l12");
  lights[13] =
    packet->getPacket("data")->getPacket("data")->getVariable("l13");
  lights[14] =
    packet->getPacket("data")->getPacket("data")->getVariable("l14");
  lights[15] =
    packet->getPacket("data")->getPacket("data")->getVariable("l15");
  lights[16] =
    packet->getPacket("data")->getPacket("data")->getVariable("l16");
  lights[17] =
    packet->getPacket("data")->getPacket("data")->getVariable("l17");
  lights[18] =
    packet->getPacket("data")->getPacket("data")->getVariable("l18");
  lights[19] =
    packet->getPacket("data")->getPacket("data")->getVariable("l19");

  checksum = packet->getPacket("data")->getVariable("checksum");
}

LightMessage::~LightMessage()
{
}

void LightMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  intStream.append(cc);
  intStream.append(slot);
  intStream.append(pid);
  intStream.append(sc);
  intStream.append(startIndex);
  for (int i = 0; i < 20; i++) {
    intStream.append(lights[i]);
  }
  intStream.append(checksum);

  BitStream bitStream;
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);
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
