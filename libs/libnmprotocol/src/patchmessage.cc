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

#include "nmprotocol/patchmessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "pdl/protocol.h"
#include "pdl/packetparser.h"
#include "nmpatch/patch.h"
#include "nmpatch/modulesection.h"
#include "pdl/tracer.h"

Protocol* PatchMessage::patchProtocol;
PacketParser* PatchMessage::patchParser;

class TestTracer : public virtual Tracer
{
public:
  void trace(string message)
  {
    printf("TRACE: %s\n", message.c_str());
  }
};

void PatchMessage::usePDLFile(string filename)
{
  patchProtocol = new Protocol(filename);
  patchParser = patchProtocol->getPacketParser("Patch");
  TestTracer* tracer = new TestTracer();
  patchProtocol->useTracer(tracer);
}

PatchMessage::PatchMessage(Patch* patch)
{
  this->patch = patch;
}

PatchMessage::PatchMessage(Packet* packet)
{
  patch = 0;
}

PatchMessage::~PatchMessage()
{
}

void PatchMessage::getBitStream(BitStream* bitStream)
{
  IntStream intStream;

  intStream.append(55);
  appendName(patch->getName(), intStream);
  
  intStream.append(33);
  intStream.append(patch->getKeyboardRange(Patch::MIN));
  intStream.append(patch->getKeyboardRange(Patch::MAX));
  intStream.append(patch->getVelocityRange(Patch::MIN));
  intStream.append(patch->getVelocityRange(Patch::MAX));
  intStream.append(patch->getBendRange());
  intStream.append(patch->getPortamentoTime());
  intStream.append(patch->getPortamento());
  intStream.append(0);
  intStream.append(patch->getRequestedVoices());
  intStream.append(0);
  intStream.append(patch->getSectionSeparationPosition());
  intStream.append(patch->getOctaveShift());
  intStream.append(patch->getCableVisibility(Cable::RED));
  intStream.append(patch->getCableVisibility(Cable::BLUE));
  intStream.append(patch->getCableVisibility(Cable::YELLOW));
  intStream.append(patch->getCableVisibility(Cable::GRAY));
  intStream.append(patch->getCableVisibility(Cable::GREEN));
  intStream.append(patch->getCableVisibility(Cable::PURPLE));
  intStream.append(patch->getCableVisibility(Cable::WHITE));
  intStream.append
    (patch->getModuleSection(ModuleSection::COMMON)->getVoiceRetrigger());
  intStream.append
    (patch->getModuleSection(ModuleSection::POLY)->getVoiceRetrigger());
  intStream.append(0);
  intStream.append(0);

  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(74);
    
    intStream.append(s);

    ModuleSection::ModuleList modules =
      patch->getModuleSection((ModuleSection::Type)s)->getModules();

    intStream.append(modules.size());

    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      intStream.append((*m)->getType());
      intStream.append((*m)->getIndex());
      intStream.append((*m)->getXPosition());
      intStream.append((*m)->getYPosition());
    }
  }

  intStream.append(105);
  Patch::NoteList midiNotes = patch->getMIDINotes();
  Patch::NoteList::iterator nl = midiNotes.begin();
  intStream.append((*nl)->getNoteNumber());
  intStream.append((*nl)->getAttackVelocity());
  intStream.append((*nl)->getReleaseVelocity());
  nl++;
  intStream.append(midiNotes.size()-2);
  for (; nl != midiNotes.end(); nl++) {
    intStream.append((*nl)->getNoteNumber());
    intStream.append((*nl)->getAttackVelocity());
    intStream.append((*nl)->getReleaseVelocity());
  }
  
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(82);
    
    intStream.append(s);

    ModuleSection::CableList cables =
      patch->getModuleSection((ModuleSection::Type)s)->getCables();

    intStream.append(cables.size());

    for (ModuleSection::CableList::iterator c = cables.begin();
	 c != cables.end();
	 c++) {
      intStream.append((*c)->getColor());
      intStream.append((*c)->getSourceModule()->getIndex());
      intStream.append((*c)->getSourceConnector());
      intStream.append((*c)->getSourceConnectorType());
      intStream.append((*c)->getDestinationModule()->getIndex());
      intStream.append((*c)->getDestinationConnector());
    }
  }

  

  
  BitStream patchStream;
  patchParser->generate(&intStream, &patchStream);
  
  while(patchStream.isAvailable(8)) {
    printf("%d ", patchStream.getInt(8));
  }
  printf("\n");

  
  while (patchStream.isAvailable(8)) {
    intStream.setSize(0);
    intStream.append(cc);
    intStream.append(slot);
    int n = 0;
    while (patchStream.isAvailable(7) && n < 165) {
      intStream.append(patchStream.getInt(7));
      n++;
    }
    int residue = (n*7) % 8;
    if (residue != 0) {
      intStream.append(patchStream.getInt(8-residue) << (residue-1));
    }
  }
}

void PatchMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

Patch* PatchMessage::getPatch()
{
  return patch;
}

void PatchMessage::appendName(string name, IntStream& intStream)
{
  int i;

  for (i = 0; i < 16 && i < name.length(); i++) {
    intStream.append(name[i]);
  }
  if (i < 16) {
    intStream.append(0);
  }
}
