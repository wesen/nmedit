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

#ifndef MIDIMESSAGE_H
#define MIDIMESSAGE_H

#include "pdl/bitstream.h"
#include "pdl/intstream.h"

#include <string>
#include <list>

using namespace std;

class PacketParser;
class Protocol;
class NMProtocolListener;
class PatchMessage;
class Tracer;

class MidiMessage
{
 public:

  typedef list<BitStream> BitStreamList;

  static void usePDLFile(string filename, Tracer* tracer);

  static int calculateChecksum(BitStream bitStream);
  static bool checksumIsCorrect(BitStream bitStream);

  static MidiMessage* create(BitStream* bitStream);

  virtual ~MidiMessage();

  virtual void getBitStream(BitStreamList* bitStreamList) = 0;

  virtual void notifyListener(NMProtocolListener* listener) = 0;

 protected:

  MidiMessage();
  
  void getBitStream(IntStream intStream, BitStream* bitStream);

  int cc;
  int slot;

 private:

  static string pdlFile;
  static Protocol* protocol;
  static PacketParser* packetParser;

  static PatchMessage* patchMessage;

};

#endif
