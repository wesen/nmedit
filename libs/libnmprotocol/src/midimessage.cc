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
    if (packet.contains("IAm")) { 
      return new IAmMessage(&packet);
    }

    if (checksumIsCorrect(*bitStream)) {

      if (packet.contains("VoiceCount")) {
	return new VoiceCountMessage(&packet);
      }
      if (packet.contains("SlotsSelected")) {
	return new SlotsSelectedMessage(&packet);
      }
      if (packet.contains("SlotActivated")) {
	return new SlotActivatedMessage(&packet);
      }
      if (packet.contains("NewPatchInSlot")) {
	return new NewPatchInSlotMessage(&packet);
      }
      if (packet.contains("Lights")) {
	return new LightMessage(&packet);
      }

      if (packet.contains("PatchListResponse")) {
	return new PatchListMessage(&packet);
      }
      else if(packet.contains("ACK")) {
	return new AckMessage(&packet);
      }
      
      if (packet.contains("PatchPacket")) {
	PatchMessage* patchMessage = new PatchMessage();
	patchMessage->append(&packet);
	return patchMessage;
      }	  
    }
    printf("unsupported packet: ");
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

void MidiMessage::appendChecksum(IntStream* intStream)
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

string MidiMessage::extractName(Packet* name)
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
