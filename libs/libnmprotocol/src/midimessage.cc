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

#include <stdio.h>

#include "nmprotocol/midimessage.h"
#include "nmprotocol/iammessage.h"
#include "nmprotocol/lightmessage.h"
#include "nmprotocol/patchmessage.h"
#include "nmprotocol/ackmessage.h"
#include "nmprotocol/patchlistmessage.h"
#include "nmprotocol/newpatchinslotmessage.h"
#include "nmprotocol/voicecountmessage.h"
#include "nmprotocol/slotsselectedmessage.h"
#include "nmprotocol/slotactivatedmessage.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packetparser.h"
#include "pdl/protocol.h"
#include "pdl/packet.h"
#include "pdl/tracer.h"

#ifndef LIBPATH
#define LIBPATH ""
#endif

string MidiMessage::pdlFile = string(LIBPATH) + "/midi.pdl";
Protocol* MidiMessage::protocol = 0;
PacketParser* MidiMessage::packetParser = 0;

PatchMessage* MidiMessage::patchMessage = 0;

void MidiMessage::usePDLFile(string filename, Tracer* tracer)
{
  pdlFile = filename;
  delete protocol;
  protocol = new Protocol(pdlFile);
  packetParser = protocol->getPacketParser("Sysex");
  protocol->useTracer(tracer);
}

MidiMessage::MidiMessage()
{
  patchMessage = 0;
  expectsreply = false;
  isreply = false;

  if (protocol == 0) {
    protocol = new Protocol(pdlFile);
    packetParser = protocol->getPacketParser("Sysex");
  }
}

MidiMessage::~MidiMessage()
{
}

void MidiMessage::getBitStream(IntStream intStream, BitStream* bitStream)
{
  bool success = packetParser->generate(&intStream, bitStream);

  if (!success || intStream.isAvailable(1)) {
    throw MidiException("Information mismatch in generate.", success);
  }
}

MidiMessage* MidiMessage::create(BitStream* bitStream)
{
  Packet packet;
  bool success = packetParser->parse(bitStream, &packet);
  bitStream->setPosition(0);
  
  if (success) {
    switch (packet.getVariable("cc")) {

    case 0x00:
      return new IAmMessage(&packet);
      break;

    case 0x14:
      if (checksumIsCorrect(*bitStream)) {
	switch (packet.getPacket("data")->getVariable("sc")) {
	  
	case 0x05:
	  return new VoiceCountMessage(&packet);
	  break;
	  
	case 0x07:
	  return new SlotsSelectedMessage(&packet);
	  break;
	  
	case 0x09:
	  return new SlotActivatedMessage(&packet);
	  break;
	  
	case 0x38:
	  return new NewPatchInSlotMessage(&packet);
	  break;
	  
	case 0x39:
	  return new LightMessage(&packet);
	  break;
	  
	default:
	  printf("unsupported packet: ");
	  break;
	}
      }
      break;

    case 0x16:
      if (checksumIsCorrect(*bitStream)) {
	if (packet.getPacket("data")->getVariable("type") == 0x13) {
	  return new PatchListMessage(&packet);
	}
	else {
	  return new AckMessage(&packet);
	}
      }
      break;
      
    case 0x1d:
	patchMessage = new PatchMessage();
    case 0x1c:
      if (checksumIsCorrect(*bitStream)) {
	patchMessage->append(&packet);
	return 0;
      }
      break;

    case 0x1e:
      if (checksumIsCorrect(*bitStream)) {
	if (patchMessage == 0) {
	  patchMessage = new PatchMessage();
	}
	patchMessage->append(&packet);
	
	PatchMessage* returnValue = patchMessage;
	patchMessage = 0;
	return returnValue;
      }
      break;

    case 0x22:
      if (checksumIsCorrect(*bitStream)) {
	  return new PatchListMessage(&packet);
      }
      break;

    default:
      printf("unsupported packet: ");
      break;
    }
  }
  else {
    printf("parse failed: ");
  }

  while (bitStream->isAvailable(8)) {
    printf("%X ", bitStream->getInt(8));
  }
  printf("\n");

  return 0;
}

bool MidiMessage::checksumIsCorrect(BitStream bitStream)
{
  int checksum = calculateChecksum(bitStream);

  bitStream.setPosition(bitStream.getSize()-16);
  int messageChecksum = bitStream.getInt(8);
  
  if (messageChecksum == checksum) {
    return true;
  }
  
  printf("Checksum mismatch %d != %d.\n", messageChecksum, checksum);
  return false;
}

int MidiMessage::calculateChecksum(BitStream bitStream)
{
  int checksum = 0;
  bitStream.setPosition(0);
  
  while (bitStream.isAvailable(24)) {
    checksum += bitStream.getInt(8);
  }
  checksum = checksum % 128;

  return checksum;
}

void MidiMessage::addChecksum(IntStream* intStream)
{
  int checksum = 0;
  intStream->append(checksum);

  BitStream bitStream;
  getBitStream(*intStream, &bitStream);

  checksum = calculateChecksum(bitStream);

  intStream->setSize(intStream->getSize()-1);
  intStream->append(checksum);
}

bool MidiMessage::expectsReply()
{
  return expectsreply;
}

bool MidiMessage::isReply()
{
  return isreply;
}

void MidiMessage::setSlot(int slot)
{
  this->slot = slot;
}

int MidiMessage::getSlot()
{
  return slot;
}

string MidiMessage::getName(Packet* name)
{
  string result;
  Packet::VariableList chars = name->getVariableList("chars");
  for (Packet::VariableList::iterator i = chars.begin();
       i != chars.end(); i++) {
    if (*i != 0) {
      result += (char)*i;
    }
  }
  return result;
}
