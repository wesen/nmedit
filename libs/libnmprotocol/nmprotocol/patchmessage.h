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

#ifndef PATCHMESSAGE_H
#define PATCHMESSAGE_H

#include "nmprotocol/midimessage.h"
#include "pdl/packet.h"
#include "nmpatch/modulesection.h"

using namespace std;

class Patch;
class Protocol;
class Tracer;

class PatchMessage : public virtual MidiMessage
{
 public:

  static void usePDLFile(string filename, Tracer* tracer);

  PatchMessage(Patch* patch);
  PatchMessage();
  virtual ~PatchMessage();

  virtual void getBitStream(BitStreamList* bitStreamList);

  virtual void notifyListener(NMProtocolListener* listener);
    
  void append(Packet* packet);

  void getPatch(Patch* patch);

  int getPid();

 private:

  typedef list<int> PositionList;

  Patch* patch;
  BitStream patchStream;
  int pid;
  int checksum;

  static string patchPdlFile;
  static Protocol* patchProtocol;
  static PacketParser* patchParser;

  void init();
  void appendName(string name, IntStream& patchStream);
  void storeEndPosition(IntStream intStream, PositionList* endPositions);
  Module* getModule(Patch* patch, ModuleSection::Type section,
		    int index, string context);};

#endif
