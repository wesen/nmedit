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
#include "nmprotocol/midiexception.h"
#include "pdl/packetparser.h"
#include "pdl/protocol.h"
#include "pdl/packet.h"

Protocol* MidiMessage::protocol;
PacketParser* MidiMessage::packetParser;

void MidiMessage::usePDLFile(string filename)
{
  protocol = new Protocol(filename);
  packetParser = protocol->getPacketParser("Sysex");
}

MidiMessage::MidiMessage()
{
}

MidiMessage::~MidiMessage()
{
}

void MidiMessage::getBitStream(IntStream* intStream, BitStream* bitStream)
{
  bool success = packetParser->generate(intStream, bitStream);

  if (!success || intStream->isAvailable(1)) {
    throw MidiException("Information mismatch in generate.", 0);
  }
}

MidiMessage* MidiMessage::create(BitStream* bitStream)
{
  Packet packet;
  bool success = packetParser->parse(bitStream, &packet);
  
  if (success) {
    switch (packet.getVariable("cc")) {

    case 0x00:
      return new IAmMessage(&packet);
      break;

    case 0x14:
      bitStream->setPosition(0);
      if (checksumIsCorrect(bitStream)) {
	switch (packet.getPacket("data")->getVariable("sc")) {
	  
	case 0x39:
	  return new LightMessage(&packet);
	  break;
	  
	default:
	  break;
	}
      }

    default:
      printf("unsupported packet: ");
      break;
    }
  }
  else {
    printf("parse failed: ");
  }

  bitStream->setPosition(0);
  while (bitStream->isAvailable(8)) {
    printf("%X ", bitStream->getInt(8));
  }
  printf("\n");

  return 0;
}

bool MidiMessage::checksumIsCorrect(BitStream* bitStream)
{
  int checksum = 0;

  while (bitStream->isAvailable(24)) {
    checksum += bitStream->getInt(8);
  }
  checksum = checksum % 128;

  int messageChecksum = bitStream->getInt(8);
  
  if (messageChecksum == checksum) {
    return true;
  }
  
  printf("Checksum mismatch %d != %d.\n", messageChecksum, checksum);
  return false;
}
