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

#include "nmprotocol/iammessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "pdl/packet.h"

IAmMessage::IAmMessage()
{
  cc = 0;
  slot = 0;
  sender = PC;
  versionHigh = 0;
  versionLow = 0;
}

IAmMessage::IAmMessage(Packet* packet)
{
  cc = packet->getVariable("cc");
  slot = packet->getVariable("slot");
  sender = Sender(packet->getPacket("data")->getVariable("sender"));
  versionHigh = packet->getPacket("data")->getVariable("versionHigh");
  versionLow = packet->getPacket("data")->getVariable("versionLow");
}

IAmMessage::~IAmMessage()
{
}

void IAmMessage::setVersion(int high, int low)
{
  versionHigh = high;
  versionLow = low;
}

void IAmMessage::getBitStream(BitStream* bitStream)
{
  IntStream intStream;
  intStream.append(cc);
  intStream.append(slot);
  intStream.append(sender);
  intStream.append(versionHigh);
  intStream.append(versionLow);
  MidiMessage::getBitStream(&intStream, bitStream);
}

void IAmMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

IAmMessage::Sender IAmMessage::getSender()
{
  return sender;
}

int IAmMessage::getVersionHigh()
{
  return versionHigh;
}

int IAmMessage::getVersionLow()
{
  return versionLow;
}
