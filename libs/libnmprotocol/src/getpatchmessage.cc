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

#include "nmprotocol/getpatchmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

GetPatchMessage::GetPatchMessage(int slot, int pid)
{
  cc = 0x17;
  this->slot = slot;
  this->pid = pid;

  expectsreply = true;
}

GetPatchMessage::GetPatchMessage(Packet* packet)
{
  throw MidiException("GetPatchMessage(Packet*) not implemented.", 0);
}

GetPatchMessage::~GetPatchMessage()
{
}

void GetPatchMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  BitStream bitStream;

  intStream.append(cc);
  intStream.append(slot);
  intStream.append(pid);
  intStream.append(0x20);
  intStream.append(0x28);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x4b);
  intStream.append(0x01);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x4b);
  intStream.append(0x00);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x53);
  intStream.append(0x01);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x53);
  intStream.append(0x00);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x4c);
  intStream.append(0x01);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x4c);
  intStream.append(0x00);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x66);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x63);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x61);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x4e);
  intStream.append(0x01);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x4e);
  intStream.append(0x00);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  

  intStream.setSize(3);
  bitStream.setSize(0);
  intStream.append(0x68);
  MidiMessage::addChecksum(&intStream);
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);  
}

void GetPatchMessage::notifyListener(NMProtocolListener* listener)
{
  throw MidiException("GetPatchMessage::notifyListener not implemented", 0);
}
