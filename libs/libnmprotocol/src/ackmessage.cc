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

#include "nmprotocol/ackmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "pdl/packet.h"

AckMessage::AckMessage()
{
  cc = 0;
  slot = 0;
  pid1 = 0;
  type = 0x7f;
  pid2 = 0;
  
  isreply = true;
}

AckMessage::AckMessage(Packet* packet)
{
  cc = packet->getVariable("cc");
  slot = packet->getVariable("slot");
  pid1 = packet->getPacket("data")->getVariable("pid1");
  type = packet->getPacket("data")->getVariable("type");
  pid2 = packet->getPacket("data")->getVariable("pid2");

  isreply = true;
}

AckMessage::~AckMessage()
{
}

void AckMessage::setPid1(int pid)
{
  pid1 = pid;
}

void AckMessage::setPid2(int pid)
{
  pid2 = pid;
}

void AckMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  intStream.append(cc);
  intStream.append(slot);
  intStream.append(pid1);
  intStream.append(type);
  intStream.append(pid2);
  if (type == 0x36) {
    addChecksum(&intStream);
  }

  BitStream bitStream;
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);
}

void AckMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

int AckMessage::getPid1()
{
  return pid1;
}

int AckMessage::getPid2()
{
  return pid2;
}
