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
#include "nmprotocol/midiexception.h"
#include "pdl/protocol.h"
#include "pdl/packetparser.h"
#include "pdl/tracer.h"
#include "nmpatch/patch.h"
#include "nmpatch/modulesection.h"

#ifndef LIBPATH
#define LIBPATH ""
#endif

string PatchMessage::patchPdlFile = string(LIBPATH) + "/patch.pdl";
Protocol* PatchMessage::patchProtocol = 0;
PacketParser* PatchMessage::patchParser = 0;

void PatchMessage::usePDLFile(string filename, Tracer* tracer)
{
  patchPdlFile = filename;
  delete patchProtocol;
  patchProtocol = new Protocol(patchPdlFile);
  patchParser = patchProtocol->getPacketParser("Patch");
  patchProtocol->useTracer(tracer);
}

void PatchMessage::init()
{
  patch = 0;
  cc = 0x1c;
  slot = 0;
  pid = 0;

  if (patchProtocol == 0) {
    patchProtocol = new Protocol(patchPdlFile);
    patchParser = patchProtocol->getPacketParser("Patch");
  }
}

PatchMessage::PatchMessage(Patch* patch)
{
  init();
  this->patch = patch;
}

PatchMessage::PatchMessage()
{
  init();
}

void PatchMessage::append(Packet* packet)
{
  slot = packet->getVariable("slot");
  pid = packet->getPacket("data")->getVariable("pid");
  
  packet = packet->getPacket("data")->getPacket("next");
  while (packet != 0) {
    patchStream.append(packet->getVariable("data"), 7);
    packet = packet->getPacket("next");
  }
  // Remove checksum
  patchStream.setSize(patchStream.getSize()-7);
  // Remove padding
  patchStream.setSize((patchStream.getSize()/8)*8);
}

PatchMessage::~PatchMessage()
{
}

void PatchMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  PositionList sectionEndPositions;

  // Create patch bitstream

  // Name section
  intStream.append(55);
  appendName(patch->getName(), intStream);
  storeEndPosition(intStream, &sectionEndPositions);
  
  // Header section
  intStream.append(33);
  intStream.append(patch->getKeyboardRange(Patch::MIN));
  intStream.append(patch->getKeyboardRange(Patch::MAX));
  intStream.append(patch->getVelocityRange(Patch::MIN));
  intStream.append(patch->getVelocityRange(Patch::MAX));
  intStream.append(patch->getBendRange());
  intStream.append(patch->getPortamentoTime());
  intStream.append(patch->getPortamento());
  intStream.append(1);
  intStream.append(patch->getRequestedVoices() - 1);
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
  intStream.append(0xf);
  intStream.append(0);
  storeEndPosition(intStream, &sectionEndPositions);

  // Module section
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
    storeEndPosition(intStream, &sectionEndPositions);
  }

  // Note section
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
  storeEndPosition(intStream, &sectionEndPositions);
  
  // Cable section
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
    storeEndPosition(intStream, &sectionEndPositions);
  }

  // Parameter section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(77);
    
    intStream.append(s);

    ModuleSection::ModuleList modules =
      patch->getModuleSection((ModuleSection::Type)s)->getModules();
    
    int nmodules = 0;
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->numberOfParameters() > 0) {
	nmodules++;
      }
    }
    intStream.append(nmodules);
    
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->numberOfParameters() > 0) {
	intStream.append((*m)->getIndex());
	intStream.append((*m)->getType());
	for (int p = 0; p < (*m)->numberOfParameters(); p++) {
	  intStream.append((*m)->getParameter((Module::Parameter)p));
	}
      }
    }
    storeEndPosition(intStream, &sectionEndPositions);
  }  

  // Morph section
  intStream.append(101);
  int nknobs = 0;
  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = patch->getMorph((Morph::Type)i);
    intStream.append(morph->getValue());

    Morph::MorphMapList morphMaps = morph->getMorphMaps();
    nknobs += morphMaps.size();
  }
  intStream.append(nknobs);

  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = patch->getMorph((Morph::Type)i);
    Morph::MorphMapList morphMaps = morph->getMorphMaps();
    for (Morph::MorphMapList::iterator m = morphMaps.begin();
	 m != morphMaps.end();
	 m++) {
      intStream.append((*m)->getModuleSectionType());
      intStream.append((*m)->getModule()->getIndex());
      intStream.append((*m)->getParameter());
      intStream.append(i);
      intStream.append((*m)->getRange());
    }
  }
  storeEndPosition(intStream, &sectionEndPositions);

  // Knob section
  intStream.append(98);
  for (int i = 0; i <= 22; i++) {
    bool found = false;
    Patch::KnobMapList knobMaps = patch->getKnobMaps();
    for (Patch::KnobMapList::iterator k = knobMaps.begin();
	 k != knobMaps.end();
	 k++) {
      if ((*k)->getKnob() == i) {
	found = true;
	intStream.append(1);
	intStream.append((*k)->getModuleSectionType());
	if ((*k)->getModuleSectionType() == ModuleSection::MORPH) {
	  intStream.append(1);
	}
	else {
	  intStream.append((*k)->getModule()->getIndex());
	}      
	intStream.append((*k)->getParameter());
      }
    }
    if (!found) {
      intStream.append(0);
    }
  }
  storeEndPosition(intStream, &sectionEndPositions);

  // Control section
  intStream.append(96);
  Patch::CtrlMapList ctrlMaps = patch->getCtrlMaps();
  intStream.append(ctrlMaps.size());
  for (Patch::CtrlMapList::iterator k = ctrlMaps.begin();
       k != ctrlMaps.end();
       k++) {
    intStream.append((*k)->getCC());
    intStream.append((*k)->getModuleSectionType());
    if ((*k)->getModuleSectionType() == ModuleSection::MORPH) {
      intStream.append(1);
    }
    else {
      intStream.append((*k)->getModule()->getIndex());
    }      
    intStream.append((*k)->getParameter());
  }
  storeEndPosition(intStream, &sectionEndPositions);

  // Custom section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(91);

    intStream.append((ModuleSection::Type)s);

    ModuleSection::ModuleList modules =
      patch->getModuleSection((ModuleSection::Type)s)->getModules();

    int nmodules = 0;
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->numberOfCustomValues() > 0) {
	nmodules++;
      }
    }
    intStream.append(nmodules);

    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->numberOfCustomValues() > 0) {
	intStream.append((*m)->getIndex());
	intStream.append((*m)->numberOfCustomValues());
	for (int p = 0; p < (*m)->numberOfCustomValues(); p++) {
	  intStream.append((*m)->getCustomValue((Module::CustomValue)p));
	}
      }
    }
    storeEndPosition(intStream, &sectionEndPositions);
  }

  // Module name section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(90);

    intStream.append((ModuleSection::Type)s);

    ModuleSection::ModuleList modules =
      patch->getModuleSection((ModuleSection::Type)s)->getModules();
    
    intStream.append(modules.size());

    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      intStream.append((*m)->getIndex());
      appendName((*m)->getName(), intStream);
    }
    storeEndPosition(intStream, &sectionEndPositions);
  }

  BitStream patchStream;
  patchParser->generate(&intStream, &patchStream);
  

  // Create sysex messages
  int first = 1;
  int last = 0;
  int messageNumber = 0;
  while (patchStream.isAvailable(8)) {

    // Get data for one sysex packet
    BitStream partialPatchStream;
    int sectionsEnded = 0;
    int n = 0;
    while (patchStream.isAvailable(8) && n < 166) {
      partialPatchStream.append(patchStream.getInt(8), 8);
      if (n == (sectionEndPositions.front() - 166*messageNumber)) {
	sectionsEnded++;
	sectionEndPositions.pop_front();
      }
      n++;
    }
    messageNumber++;

    if (!patchStream.isAvailable(8)) {
      last = 1;
      first = 0;
    }

    // Pad. Extra bits are ignored later.
    partialPatchStream.append(0, 6);

    // Generate sysex bistream with fake checksum
    IntStream intStream;
    intStream.append(cc + first + 2*last);
    first = 0;
    intStream.append(slot);
    intStream.append(0x40 + sectionsEnded);
    while (partialPatchStream.isAvailable(7)) {
      intStream.append(partialPatchStream.getInt(7));
    }
    int checksum = 0;
    intStream.append(checksum);

    BitStream bitStream;
    MidiMessage::getBitStream(intStream, &bitStream);

    // Calculate checksum
    checksum = MidiMessage::calculateChecksum(bitStream);
    intStream.setSize(intStream.getSize()-1);
    intStream.append(checksum);

    // Generate sysex bitstream with checksum
    bitStream.setSize(0);
    MidiMessage::getBitStream(intStream, &bitStream);
    
    bitStreamList->push_back(bitStream);
  }
}

void PatchMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

void PatchMessage::getPatch(Patch* patch)
{
  Packet* packet = new Packet();
  if (patchParser->parse(&patchStream, packet)) {
    while (packet != 0) {
      Packet* section = packet->getPacket("section");
      Packet* sectionData = section->getPacket("data");
      switch (section->getVariable("type")) {

      case 55:
	patch->setName(getName(sectionData->getPacket("name")));
	break;

      case 33:
	patch->setKeyboardRange(Patch::MIN,
				sectionData->getVariable("krangemin"));
	patch->setKeyboardRange(Patch::MAX,
				sectionData->getVariable("krangemax"));
	patch->setVelocityRange(Patch::MIN,
				sectionData->getVariable("vrangemin"));
	patch->setVelocityRange(Patch::MAX,
				sectionData->getVariable("vrangemax"));
	patch->setBendRange(sectionData->getVariable("brange"));
	patch->setPortamentoTime(sectionData->getVariable("ptime"));
	patch->setPortamento((Patch::Portamento)
			     sectionData->getVariable("portamento"));
	patch->setRequestedVoices(sectionData->getVariable("voices"));
	patch->setSectionSeparationPosition(sectionData->getVariable("sspos"));
	patch->setOctaveShift(sectionData->getVariable("octave"));
	patch->setCableVisibility(Cable::RED,
				  (Patch::CableVisibility)
				  sectionData->getVariable("red"));
	patch->setCableVisibility(Cable::BLUE,
				  (Patch::CableVisibility)
				  sectionData->getVariable("blue"));
	patch->setCableVisibility(Cable::YELLOW,
				  (Patch::CableVisibility)
				  sectionData->getVariable("yellow"));
	patch->setCableVisibility(Cable::GRAY,
				  (Patch::CableVisibility)
				  sectionData->getVariable("gray"));
	patch->setCableVisibility(Cable::GREEN,
				  (Patch::CableVisibility)
				  sectionData->getVariable("green"));
	patch->setCableVisibility(Cable::PURPLE,
				  (Patch::CableVisibility)
				  sectionData->getVariable("purple"));
	patch->setCableVisibility(Cable::WHITE,
				  (Patch::CableVisibility)
				  sectionData->getVariable("white"));
	patch->getModuleSection(ModuleSection::COMMON)->
	  setVoiceRetrigger((ModuleSection::VoiceRetrigger)
			    sectionData->getVariable("cretrigger"));
	patch->getModuleSection(ModuleSection::POLY)->
	  setVoiceRetrigger((ModuleSection::VoiceRetrigger)
			    sectionData->getVariable("pretrigger"));
	break;

      case 74:
	{
	  ModuleSection* moduleSection =
	    patch->getModuleSection((ModuleSection::Type)
				    sectionData->getVariable("section"));
	  Packet::PacketList modules = sectionData->getPacketList("modules");
	  for (Packet::PacketList::iterator i = modules.begin();
	       i != modules.end(); i++) {
	    Module* module =
	      moduleSection->newModule((Module::Type)(*i)->getVariable("type"),
				       (*i)->getVariable("index"));
	    module->setPosition((*i)->getVariable("xpos"),
				(*i)->getVariable("ypos"));
	  }
	}
	break;
	
      case 82:
	{
	  ModuleSection* moduleSection =
	    patch->getModuleSection((ModuleSection::Type)
				    sectionData->getVariable("section"));
	  Packet::PacketList cables = sectionData->getPacketList("cables");
	  for (Packet::PacketList::iterator i = cables.begin();
	       i != cables.end(); i++) {
	    moduleSection->newCable
	      ((Cable::Color)(*i)->getVariable("color"),
	       (*i)->getVariable("destination"),
	       (Module::Port)(*i)->getVariable("input"),
	       Cable::INPUT,
	       (*i)->getVariable("source"),
	       (Module::Port)(*i)->getVariable("inputOutput"),
	       (Cable::ConnectorType)(*i)->getVariable("type"));
	  }
	}
	break;

      case 77:
	{
	  ModuleSection* moduleSection =
	    patch->getModuleSection((ModuleSection::Type)
				    sectionData->getVariable("section"));
	  Packet::PacketList modules =
	    sectionData->getPacketList("parameters");
	  for (Packet::PacketList::iterator i = modules.begin();
	       i != modules.end(); i++) {
	    Module* module =
	      moduleSection->getModule((*i)->getVariable("index"));
	    printf("%d %d ", (*i)->getVariable("index"),
		   (*i)->getVariable("type"));
	    Packet::VariableList parameters =
	      (*i)->getPacket("parameters")->getAllVariables();
	    int n = 0;
	    for (Packet::VariableList::iterator p = parameters.begin();
		 p != parameters.end(); p++, n++) {
	      printf("%d ", (*p));
	      //module->setParameter((Module::Parameter)n, (*p));
	    }
	    printf("\n");
	  }
	}
	break;

      }
      packet = packet->getPacket("next");
    }
  }
  else {
    delete packet;
    throw MidiException("Illegal patch format.", 0);
  }
  delete packet;
}

int PatchMessage::getPid()
{
  return pid;
}

void PatchMessage::setPid(int pid)
{
  this->pid = pid;
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

void PatchMessage::storeEndPosition(IntStream intStream,
				    PositionList* sectionEndPositions)
{
  BitStream patchStream;
  patchParser->generate(&intStream, &patchStream);
  sectionEndPositions->push_back(patchStream.getSize()/8-1);
}

string PatchMessage::getName(Packet* name)
{
  string result;
  Packet::VariableList chars = name->getVariableList("chars");
  for (Packet::VariableList::iterator i = chars.begin();
       i != chars.end(); i++) {
    result += (char)*i;
  }
  return result;
}
