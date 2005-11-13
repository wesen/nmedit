/*
    Nord Modular patch file format 3.03 parser
    Copyright (C) 2002 Marcus Andersson

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

#include "nmpatch/patch.h"
#include "nmpatch/modulesection.h"
#include "nmpatch/patchexception.h"
#include "pdl/protocol.h"
#include "pdl/packetparser.h"
#include "pdl/tracer.h"
#include <stdio.h>

#ifndef LIBPATH
#define LIBPATH ""
#endif

string Patch::patchPdlFile = string(LIBPATH) + "/patch.pdl";
Protocol* Patch::patchProtocol = 0;
PacketParser* Patch::patchParser = 0;

extern bool init_parser(const char* filename, Patch* patch);
extern void nmparse();

void Patch::usePDLFile(string filename, Tracer* tracer)
{
  patchPdlFile = filename;
  delete patchProtocol;
  patchProtocol = new Protocol(patchPdlFile);
  patchParser = patchProtocol->getPacketParser("Patch");
  patchProtocol->useTracer(tracer);
}

Patch::Patch()
{
  moduleSection[ModuleSection::POLY] = new ModuleSection();
  moduleSection[ModuleSection::COMMON] = new ModuleSection();

  morph[Morph::MORPH1] = new Morph();
  morph[Morph::MORPH2] = new Morph();
  morph[Morph::MORPH3] = new Morph();
  morph[Morph::MORPH4] = new Morph();

  if (patchProtocol == 0) {
    patchProtocol = new Protocol(patchPdlFile);
    patchParser = patchProtocol->getPacketParser("Patch");
  }
}

Patch::Patch(string filename)
{
  moduleSection[ModuleSection::POLY] = new ModuleSection();
  moduleSection[ModuleSection::COMMON] = new ModuleSection();

  morph[Morph::MORPH1] = new Morph();
  morph[Morph::MORPH2] = new Morph();
  morph[Morph::MORPH3] = new Morph();
  morph[Morph::MORPH4] = new Morph();

  if (init_parser(filename.c_str(), this)) {
    nmparse();
  }
  else {
    throw PatchException(string("File not found: ") + filename, 0);
  }

  if (patchProtocol == 0) {
    patchProtocol = new Protocol(patchPdlFile);
    patchParser = patchProtocol->getPacketParser("Patch");
  }
}

Patch::~Patch()
{
  delete moduleSection[ModuleSection::POLY];
  delete moduleSection[ModuleSection::COMMON];

  delete morph[Morph::MORPH1];
  delete morph[Morph::MORPH2];
  delete morph[Morph::MORPH3];
  delete morph[Morph::MORPH4];

  for (NoteList::iterator n = midiNotes.begin(); n != midiNotes.end(); n++) {
    delete (*n);
  }

  for (KnobMapList::iterator k = knobMaps.begin(); k != knobMaps.end(); k++) {
    delete (*k);
  }

  for (CtrlMapList::iterator c = ctrlMaps.begin(); c != ctrlMaps.end(); c++) {
    delete (*c);
  }

}

void Patch::setName(string name)
{
  this->name = name;
}

string Patch::getName()
{
  return name;
}

void Patch::setNotes(string notes)
{
  this->notes = notes;
}

string Patch::getNotes()
{
  return notes;
}

void Patch::setPortamento(Patch::Portamento portamento)
{
  this->portamento = portamento;
}

Patch::Portamento Patch::getPortamento()
{
  return portamento;
}

void Patch::setCableVisibility(Cable::Color color,
			       Patch::CableVisibility visibility)
{
  cableVisibility[color] = visibility;
}

Patch::CableVisibility Patch::getCableVisibility(Cable::Color color)
{
  return cableVisibility[color];
}

void Patch::setKeyboardRange(Patch::RangeLimit limit, int value)
{
  keyboardRange[limit] = value;
}

int Patch::getKeyboardRange(Patch::RangeLimit limit)
{
  return keyboardRange[limit];
}

void Patch::setVelocityRange(Patch::RangeLimit limit, int value)
{
  velocityRange[limit] = value;
}

int Patch::getVelocityRange(Patch::RangeLimit limit)
{
  return velocityRange[limit];
}

void Patch::setBendRange(int range)
{
  bendRange = range;
}

int Patch::getBendRange()
{
  return bendRange;
}

void Patch::setPortamentoTime(int time)
{
  portamentoTime = time;
}

int Patch::getPortamentoTime()
{
  return portamentoTime;
}

void Patch::setRequestedVoices(int voices)
{
  requestedVoices = voices;
}

int Patch::getRequestedVoices()
{
  return requestedVoices;
}

void Patch::setSectionSeparationPosition(int position)
{
  sectionSeparationPosition = position;
}

int Patch::getSectionSeparationPosition()
{
  return sectionSeparationPosition;
}

void Patch::setOctaveShift(int shift)
{
  octaveShift = shift;
}

int Patch::getOctaveShift()
{
  return octaveShift;
}

ModuleSection* Patch::getModuleSection(ModuleSection::Type type)
{
  return moduleSection[type];
}

Note* Patch::newMIDINote(int noteNumber,
			 int attackVelocity,
			 int releaseVelocity)
{
  midiNotes.push_back(new Note(noteNumber, attackVelocity, releaseVelocity));
  return midiNotes.back();
}

Patch::NoteList Patch::getMIDINotes() const
{
  return midiNotes;
}

void Patch::removeMIDINote(Note* note)
{
  midiNotes.remove(note);
  delete note;
}

Morph* Patch::getMorph(Morph::Type type)
{
  return morph[type];
}

KnobMap* Patch::newKnobMap(ModuleSection::Type section,
			   Module* module,
			   ModuleType::Parameter parameter)
{
  knobMaps.push_back(new KnobMap(section, module, parameter));
  return knobMaps.back();
}

Patch::KnobMapList Patch::getKnobMaps() const
{
  return knobMaps;
}

void Patch::removeKnobMap(KnobMap* knobMap)
{
  knobMaps.remove(knobMap);
  delete knobMap;
}  

CtrlMap* Patch::newCtrlMap(ModuleSection::Type section,
			   Module* module,
			   ModuleType::Parameter parameter)
{
  ctrlMaps.push_back(new CtrlMap(section, module, parameter));
  return ctrlMaps.back();
}

Patch::CtrlMapList Patch::getCtrlMaps() const
{
  return ctrlMaps;
}

void Patch::removeCtrlMap(CtrlMap* ctrlMap)
{
  ctrlMaps.remove(ctrlMap);
  delete ctrlMap;
}  

void Patch::writeParameter(string& result, int value)
{
  char buffer[5];
  snprintf(buffer, 5, "%d", value);
  result += buffer;
  result += " ";
}

string Patch::write()
{
  string result;

  result += "[Header]\n";
  result += "Version=Nord Modular patch 3.0\n";
  writeParameter(result, keyboardRange[MIN]);
  writeParameter(result, keyboardRange[MAX]);
  writeParameter(result, velocityRange[MIN]);
  writeParameter(result, velocityRange[MAX]);
  writeParameter(result, bendRange);
  writeParameter(result, portamentoTime);
  writeParameter(result, portamento);
  writeParameter(result, requestedVoices);
  writeParameter(result, sectionSeparationPosition);
  writeParameter(result, octaveShift);
  writeParameter(result,
		 moduleSection[ModuleSection::POLY]->getVoiceRetrigger());
  writeParameter(result,
		 moduleSection[ModuleSection::COMMON]->getVoiceRetrigger());
  writeParameter(result, 1);
  writeParameter(result, 1);
  writeParameter(result, 1);
  writeParameter(result, 1);
  for (int color = Cable::RED; color <= Cable::WHITE; color++) {
    writeParameter(result, cableVisibility[(Cable::Color)color]);
  }
  result += "\n";
  result += "[/Header]\n";

  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    result += "[ModuleDump]\n";
    writeParameter(result, (ModuleSection::Type)s);
    result += "\n";
    ModuleSection::ModuleList modules =
      moduleSection[(ModuleSection::Type)s]->getModules();
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      writeParameter(result, (*m)->getIndex());
      writeParameter(result, (*m)->getType()->getId());
      writeParameter(result, (*m)->getXPosition());
      writeParameter(result, (*m)->getYPosition());
      result += "\n";
    }
    result += "[/ModuleDump]\n";
  }

  result += "[CurrentNoteDump]\n";
  for (NoteList::iterator i = midiNotes.begin(); i != midiNotes.end(); i++) {
    writeParameter(result, (*i)->getNoteNumber());
    writeParameter(result, (*i)->getAttackVelocity());
    writeParameter(result, (*i)->getReleaseVelocity());
  }
  result += "\n";
  result += "[/CurrentNoteDump]\n";
  
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    result += "[CableDump]\n";
    writeParameter(result, (ModuleSection::Type)s);
    result += "\n";
    ModuleSection::CableList cables =
      moduleSection[(ModuleSection::Type)s]->getCables();
    for (ModuleSection::CableList::iterator c = cables.begin();
	 c != cables.end();
	 c++) {
      writeParameter(result, (*c)->getColor());
      writeParameter(result, (*c)->getDestinationModule()->getIndex());
      writeParameter(result, (*c)->getDestinationConnector());
      writeParameter(result, (*c)->getDestinationConnectorType());
      writeParameter(result, (*c)->getSourceModule()->getIndex());
      writeParameter(result, (*c)->getSourceConnector());
      writeParameter(result, (*c)->getSourceConnectorType());
      result += "\n";  
    }
    result += "[/CableDump]\n";
  }

  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    result += "[ParameterDump]\n";
    writeParameter(result, (ModuleSection::Type)s);
    result += "\n";
    ModuleSection::ModuleList modules =
      moduleSection[(ModuleSection::Type)s]->getModules();
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->getType()->numberOfParameters() > 0) {
	writeParameter(result, (*m)->getIndex());
	writeParameter(result, (*m)->getType()->getId());
	writeParameter(result, (*m)->getType()->numberOfParameters());
	for (int p = 0; p < (*m)->getType()->numberOfParameters(); p++) {
	  writeParameter(result, (*m)->getParameter((ModuleType::Parameter)p));
	}
	result += "\n";  
      }
    }
    result += "[/ParameterDump]\n";
  }

  result += "[KnobMapDump]\n";
  for (KnobMapList::iterator k = knobMaps.begin();
       k != knobMaps.end();
       k++) {
    writeParameter(result, (*k)->getModuleSectionType());
    if ((*k)->getModuleSectionType() == ModuleSection::MORPH) {
      writeParameter(result, 1);
    }
    else {
      writeParameter(result, (*k)->getModule()->getIndex());
    }      
    writeParameter(result, (*k)->getParameter());
    writeParameter(result, (*k)->getKnob());
    result += '\n';
  }
  result += "[/KnobMapDump]\n";

  result += "[CtrlMapDump]\n";
  for (CtrlMapList::iterator k = ctrlMaps.begin();
       k != ctrlMaps.end();
       k++) {
    writeParameter(result, (*k)->getModuleSectionType());
    if ((*k)->getModuleSectionType() == ModuleSection::MORPH) {
      writeParameter(result, 1);
    }
    else {
      writeParameter(result, (*k)->getModule()->getIndex());
    }      
    writeParameter(result, (*k)->getParameter());
    writeParameter(result, (*k)->getCC());
    result += '\n';
  }
  result += "[/CtrlMapDump]\n";

  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    result += "[CustomDump]\n";
    writeParameter(result, (ModuleSection::Type)s);
    result += "\n";
    ModuleSection::ModuleList modules =
      moduleSection[(ModuleSection::Type)s]->getModules();
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->getType()->numberOfCustomValues() > 0) {
	writeParameter(result, (*m)->getIndex());
	writeParameter(result, (*m)->getType()->numberOfCustomValues());
	for (int p = 0; p < (*m)->getType()->numberOfCustomValues(); p++) {
	  writeParameter(result,
			 (*m)->getCustomValue((ModuleType::CustomValue)p));
	}
	result += "\n";  
      }
    }
    result += "[/CustomDump]\n";
  }

  string morphMapString;
  bool emptyMorphMap = true;
  morphMapString += "[MorphMapDump]\n";
  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = getMorph((Morph::Type)i);
    writeParameter(morphMapString, morph->getValue());
  }
  morphMapString += '\n';
  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = getMorph((Morph::Type)i);
    Morph::MorphMapList morphMaps = morph->getMorphMaps();
    for (Morph::MorphMapList::iterator m = morphMaps.begin();
	 m != morphMaps.end();
	 m++) {
      emptyMorphMap = false;
      writeParameter(morphMapString, (*m)->getModuleSectionType());
      writeParameter(morphMapString, (*m)->getModule()->getIndex());
      writeParameter(morphMapString, (*m)->getParameter());
      writeParameter(morphMapString, i);
      writeParameter(morphMapString, (*m)->getRange());
    }
  }
  morphMapString += '\n';
  morphMapString += "[/MorphMapDump]\n";
  if (!emptyMorphMap) {
    result += morphMapString;
  }

  string keyboardString;
  bool keyboardAssigned = false;
  keyboardString += "[KeyboardAssignment]\n";
  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = getMorph((Morph::Type)i);
    if (morph->getKeyboardAssignment() != Morph::NONE) {
      keyboardAssigned = true;
    }
    writeParameter(keyboardString, morph->getKeyboardAssignment());
  }
  keyboardString += '\n';
  keyboardString += "[/KeyboardAssignment]\n";
  if (keyboardAssigned) {
    result += keyboardString;
  }

  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    result += "[NameDump]\n";
    writeParameter(result, (ModuleSection::Type)s);
    result += "\n";
    ModuleSection::ModuleList modules =
      moduleSection[(ModuleSection::Type)s]->getModules();
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      writeParameter(result, (*m)->getIndex());
      result += (*m)->getName() + '\n';
    }
    result += "[/NameDump]\n";
  }

  if (notes.length() > 0) {
    result += "[Notes]\n";
    result += notes + "\n";
    result += "[/Notes]\n";
  }

  return result;
}


BitStream Patch::serialize(PositionList* sectionEndPositions)
{
  IntStream intStream;

  // Create patch bitstream

  // Name section
  intStream.append(55);
  appendName(this->getName(), intStream);
  storeEndPosition(intStream, sectionEndPositions);
  
  // Header section
  intStream.append(33);
  intStream.append(this->getKeyboardRange(Patch::MIN));
  intStream.append(this->getKeyboardRange(Patch::MAX));
  intStream.append(this->getVelocityRange(Patch::MIN));
  intStream.append(this->getVelocityRange(Patch::MAX));
  intStream.append(this->getBendRange());
  intStream.append(this->getPortamentoTime());
  intStream.append(this->getPortamento());
  intStream.append(1);
  intStream.append(this->getRequestedVoices() - 1);
  intStream.append(0);
  intStream.append(this->getSectionSeparationPosition());
  intStream.append(this->getOctaveShift());
  intStream.append(this->getCableVisibility(Cable::RED));
  intStream.append(this->getCableVisibility(Cable::BLUE));
  intStream.append(this->getCableVisibility(Cable::YELLOW));
  intStream.append(this->getCableVisibility(Cable::GRAY));
  intStream.append(this->getCableVisibility(Cable::GREEN));
  intStream.append(this->getCableVisibility(Cable::PURPLE));
  intStream.append(this->getCableVisibility(Cable::WHITE));
  intStream.append
    (this->getModuleSection(ModuleSection::COMMON)->getVoiceRetrigger());
  intStream.append
    (this->getModuleSection(ModuleSection::POLY)->getVoiceRetrigger());
  intStream.append(0xf);
  intStream.append(0);
  storeEndPosition(intStream, sectionEndPositions);

  // Module section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(74);
    
    intStream.append(s);

    ModuleSection::ModuleList modules =
      this->getModuleSection((ModuleSection::Type)s)->getModules();

    intStream.append(modules.size());

    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      intStream.append((*m)->getType()->getId());
      intStream.append((*m)->getIndex());
      intStream.append((*m)->getXPosition());
      intStream.append((*m)->getYPosition());
    }
    storeEndPosition(intStream, sectionEndPositions);
  }

  // Note section
  intStream.append(105);
  Patch::NoteList midiNotes = this->getMIDINotes();
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
  storeEndPosition(intStream, sectionEndPositions);
  
  // Cable section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(82);
    
    intStream.append(s);

    ModuleSection::CableList cables =
      this->getModuleSection((ModuleSection::Type)s)->getCables();

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
    storeEndPosition(intStream, sectionEndPositions);
  }

  // Parameter section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(77);
    
    intStream.append(s);

    ModuleSection::ModuleList modules =
      this->getModuleSection((ModuleSection::Type)s)->getModules();
    
    int nmodules = 0;
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->getType()->numberOfParameters() > 0) {
	nmodules++;
      }
    }
    intStream.append(nmodules);
    
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->getType()->numberOfParameters() > 0) {
	intStream.append((*m)->getIndex());
	intStream.append((*m)->getType()->getId());
	for (int p = 0; p < (*m)->getType()->numberOfParameters(); p++) {
	  intStream.append((*m)->getParameter((ModuleType::Parameter)p));
	}
      }
    }
    storeEndPosition(intStream, sectionEndPositions);
  }  

  // Morph section
  intStream.append(101);
  int nknobs = 0;
  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = this->getMorph((Morph::Type)i);
    intStream.append(morph->getValue());

    Morph::MorphMapList morphMaps = morph->getMorphMaps();
    nknobs += morphMaps.size();
  }
  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = this->getMorph((Morph::Type)i);
    intStream.append(morph->getKeyboardAssignment());
  }

  intStream.append(nknobs);

  for (int i = Morph::MORPH1; i <= Morph::MORPH4; i++) {
    Morph* morph = this->getMorph((Morph::Type)i);
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
  storeEndPosition(intStream, sectionEndPositions);

  // Knob section
  intStream.append(98);
  for (int i = 0; i <= 22; i++) {
    bool found = false;
    Patch::KnobMapList knobMaps = this->getKnobMaps();
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
  storeEndPosition(intStream, sectionEndPositions);

  // Control section
  intStream.append(96);
  Patch::CtrlMapList ctrlMaps = this->getCtrlMaps();
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
  storeEndPosition(intStream, sectionEndPositions);

  // Custom section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(91);

    intStream.append((ModuleSection::Type)s);

    ModuleSection::ModuleList modules =
      this->getModuleSection((ModuleSection::Type)s)->getModules();

    int nmodules = 0;
    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->getType()->numberOfCustomValues() > 0) {
	nmodules++;
      }
    }
    intStream.append(nmodules);

    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      if ((*m)->getType()->numberOfCustomValues() > 0) {
	intStream.append((*m)->getIndex());
	intStream.append((*m)->getType()->numberOfCustomValues());
	for (int p = 0; p < (*m)->getType()->numberOfCustomValues(); p++) {
	  intStream.append((*m)->getCustomValue((ModuleType::CustomValue)p));
	}
      }
    }
    storeEndPosition(intStream, sectionEndPositions);
  }

  // Module name section
  for (int s = ModuleSection::POLY; s >= ModuleSection::COMMON; s--) {
    intStream.append(90);

    intStream.append((ModuleSection::Type)s);

    ModuleSection::ModuleList modules =
      this->getModuleSection((ModuleSection::Type)s)->getModules();
    
    intStream.append(modules.size());

    for (ModuleSection::ModuleList::iterator m = modules.begin();
	 m != modules.end();
	 m++) {
      intStream.append((*m)->getIndex());
      appendName((*m)->getName(), intStream);
    }
    storeEndPosition(intStream, sectionEndPositions);
  }

  BitStream patchStream;
  patchParser->generate(&intStream, &patchStream);

  return patchStream;
}

void Patch::update(BitStream patchStream)
{
  Packet* packet = new Packet();
  if (patchParser->parse(&patchStream, packet)) {
    while (packet != 0) {
      Packet* section = packet->getPacket("section");
      Packet* sectionData = section->getPacket("data");
      switch (section->getVariable("type")) {

	// Name section
      case 55:
      case 39:
	this->setName(extractName(sectionData->getPacket("name")));
	break;

	// Header section
      case 33:
	this->setKeyboardRange(Patch::MIN,
				sectionData->getVariable("krangemin"));
	this->setKeyboardRange(Patch::MAX,
				sectionData->getVariable("krangemax"));
	this->setVelocityRange(Patch::MIN,
				sectionData->getVariable("vrangemin"));
	this->setVelocityRange(Patch::MAX,
				sectionData->getVariable("vrangemax"));
	this->setBendRange(sectionData->getVariable("brange"));
	this->setPortamentoTime(sectionData->getVariable("ptime"));
	this->setPortamento((Patch::Portamento)
			     sectionData->getVariable("portamento"));
	this->setRequestedVoices(sectionData->getVariable("voices"));
	this->setSectionSeparationPosition(sectionData->getVariable("sspos"));
	this->setOctaveShift(sectionData->getVariable("octave"));
	this->setCableVisibility(Cable::RED,
				  (Patch::CableVisibility)
				  sectionData->getVariable("red"));
	this->setCableVisibility(Cable::BLUE,
				  (Patch::CableVisibility)
				  sectionData->getVariable("blue"));
	this->setCableVisibility(Cable::YELLOW,
				  (Patch::CableVisibility)
				  sectionData->getVariable("yellow"));
	this->setCableVisibility(Cable::GRAY,
				  (Patch::CableVisibility)
				  sectionData->getVariable("gray"));
	this->setCableVisibility(Cable::GREEN,
				  (Patch::CableVisibility)
				  sectionData->getVariable("green"));
	this->setCableVisibility(Cable::PURPLE,
				  (Patch::CableVisibility)
				  sectionData->getVariable("purple"));
	this->setCableVisibility(Cable::WHITE,
				  (Patch::CableVisibility)
				  sectionData->getVariable("white"));
	this->getModuleSection(ModuleSection::COMMON)->
	  setVoiceRetrigger((ModuleSection::VoiceRetrigger)
			    sectionData->getVariable("cretrigger"));
	this->getModuleSection(ModuleSection::POLY)->
	  setVoiceRetrigger((ModuleSection::VoiceRetrigger)
			    sectionData->getVariable("pretrigger"));
	break;

	// Module section
      case 74:
	{
	  ModuleSection* moduleSection =
	    this->getModuleSection((ModuleSection::Type)
				    sectionData->getVariable("section"));
	  Packet::PacketList modules = sectionData->getPacketList("modules");
	  for (Packet::PacketList::iterator i = modules.begin();
	       i != modules.end(); i++) {
	    Module* module =
	      moduleSection->newModule((ModuleType::TypeId)
				       (*i)->getVariable("type"),
				       (*i)->getVariable("index"));
	    module->setPosition((*i)->getVariable("xpos"),
				(*i)->getVariable("ypos"));
	  }
	}
	break;

	// Note section
      case 105:
	{
	  Packet* note = sectionData->getPacket("note1");
	  this->newMIDINote(note->getVariable("value"),
			     note->getVariable("attack"),
			     note->getVariable("release"));
	  note = sectionData->getPacket("note2");
	  this->newMIDINote(note->getVariable("value"),
			     note->getVariable("attack"),
			     note->getVariable("release"));
	  Packet::PacketList notes = sectionData->getPacketList("notes");
	  for (Packet::PacketList::iterator i = notes.begin();
	       i != notes.end(); i++) {
	    this->newMIDINote((*i)->getVariable("value"),
			       (*i)->getVariable("attack"),
			       (*i)->getVariable("release"));
	  }
	}
	break;
	
	// Cable section
      case 82:
	{
	  ModuleSection* moduleSection =
	    this->getModuleSection((ModuleSection::Type)
				    sectionData->getVariable("section"));
	  Packet::PacketList cables = sectionData->getPacketList("cables");
	  for (Packet::PacketList::iterator i = cables.begin();
	       i != cables.end(); i++) {
	    moduleSection->newCable
	      ((Cable::Color)(*i)->getVariable("color"),
	       (*i)->getVariable("destination"),
	       (ModuleType::Port)(*i)->getVariable("input"),
	       ModuleType::INPUT,
	       (*i)->getVariable("source"),
	       (ModuleType::Port)(*i)->getVariable("inputOutput"),
	       (ModuleType::ConnectorType)(*i)->getVariable("type"));
	  }
	}
	break;

	// Parameter section
      case 77:
	{
	  ModuleSection::Type section =
	    (ModuleSection::Type)sectionData->getVariable("section");
	  Packet::PacketList modules =
	    sectionData->getPacketList("parameters");
	  for (Packet::PacketList::iterator i = modules.begin();
	       i != modules.end(); i++) {
	    Module* module =
	      getModule(this, section,
			(*i)->getVariable("index"), "parameter");
	    Packet::VariableList parameters =
	      (*i)->getPacket("parameters")->getAllVariables();
	    int n = 0;
	    for (Packet::VariableList::iterator p = parameters.begin();
		 p != parameters.end(); p++, n++) {
	      module->setParameter((ModuleType::Parameter)n, (*p));
	    }
	  }
	}
	break;

	// Morphmap section
      case 101:
	{
	  this->getMorph(Morph::MORPH1)->setValue
	    (sectionData->getVariable("morph1"));
	  this->getMorph(Morph::MORPH2)->setValue
	    (sectionData->getVariable("morph2"));
	  this->getMorph(Morph::MORPH3)->setValue
	    (sectionData->getVariable("morph3"));
	  this->getMorph(Morph::MORPH4)->setValue
	    (sectionData->getVariable("morph4"));

	  this->getMorph(Morph::MORPH1)->setKeyboardAssignment
	    ((Morph::KeyboardAssignment)sectionData->getVariable("keyboard1"));
	  this->getMorph(Morph::MORPH2)->setKeyboardAssignment
	    ((Morph::KeyboardAssignment)sectionData->getVariable("keyboard2"));
	  this->getMorph(Morph::MORPH3)->setKeyboardAssignment
	    ((Morph::KeyboardAssignment)sectionData->getVariable("keyboard3"));
	  this->getMorph(Morph::MORPH4)->setKeyboardAssignment
	    ((Morph::KeyboardAssignment)sectionData->getVariable("keyboard4"));

	  Packet::PacketList morphs =
	    sectionData->getPacketList("morphs");
	  for (Packet::PacketList::iterator i = morphs.begin();
	       i != morphs.end(); i++) {
	    Morph* morph =
	      this->getMorph((Morph::Type)(*i)->getVariable("morph"));
	    Module* module =
	      getModule(this,
			(ModuleSection::Type)(*i)->getVariable("section"),
			(*i)->getVariable("module"),
			"morph");
	    MorphMap* morphMap = morph->newMorphMap
	      ((ModuleSection::Type)(*i)->getVariable("section"),
	       module,
	       (ModuleType::Parameter)(*i)->getVariable("parameter"));
	    int range = (*i)->getVariable("range");
	    morphMap->setRange(range - (range > 127 ? 256 : 0));
	  }
	}
	break;

	// Knobmap section
      case 98:
	{
	  for (int i = 0; i < 23; i++) {
	    char buffer[7];
	    snprintf(buffer, 7, "knob%d", i);
	    Packet* knob = sectionData->getPacket(string(buffer));
 	    bool assigned = knob->getVariable("assigned");
	    if (assigned) {
	      Packet* assignment = knob->getPacketList("assignment").front();
	      ModuleSection::Type section =
		(ModuleSection::Type)assignment->getVariable("section");
	      Module* module =
		getModule(this, section,
			  assignment->getVariable("module"), "knob");
	      KnobMap* knobMap =
		this->newKnobMap
		(section,
		 module,
		 (ModuleType::Parameter)assignment->getVariable("parameter"));
	      knobMap->setKnob((KnobMap::Knob)i);
	    }
	  }
	}
	break;

	// Controlmap section
      case 96:
	{
	  Packet::PacketList controls = sectionData->getPacketList("controls");
	  for (Packet::PacketList::iterator i = controls.begin();
	       i != controls.end(); i++) {
	    ModuleSection::Type section =
	      (ModuleSection::Type)(*i)->getVariable("section");
	    Module* module = getModule(this, section,
				       (*i)->getVariable("module"), "control");
	    CtrlMap* ctrlMap = this->newCtrlMap
	      (section,
	       module,
	       (ModuleType::Parameter)(*i)->getVariable("parameter"));
	    ctrlMap->setCC((*i)->getVariable("control"));
	  }	  
	}
	break;

	// Custom section
      case 91:
	{
	  ModuleSection::Type section =
	    (ModuleSection::Type)sectionData->getVariable("section");
	  Packet::PacketList modules =
	    sectionData->getPacketList("customModules");
	  for (Packet::PacketList::iterator m = modules.begin();
	       m != modules.end(); m++) {
	    Module* module = getModule(this, section,
				       (*m)->getVariable("index"), "custom");
	    Packet::PacketList values = (*m)->getPacketList("customValues");
	    int n = 0;
	    for (Packet::PacketList::iterator v = values.begin();
		 v != values.end(); v++, n++) {
	      module->setCustomValue((ModuleType::CustomValue)n,
				     (*v)->getVariable("value"));
	    }
	  }
	}
	break;

	// Namedump section
      case 90:
	{
	  ModuleSection::Type section =
	    (ModuleSection::Type)sectionData->getVariable("section");
	  Packet::PacketList modules =
	    sectionData->getPacketList("moduleNames");
	  for (Packet::PacketList::iterator i = modules.begin();
	       i != modules.end(); i++) {
	    Module* module = getModule(this, section,
				       (*i)->getVariable("index"), "name");
	    module->setName(extractName((*i)->getPacket("name")));
	  }
	}
	break;

      }
      packet = packet->getPacket("next");
    }
  }
  else {
    delete packet;
    throw PatchException("Illegal patch format.", 0);
  }
  delete packet;
}

void Patch::appendName(string name, IntStream& intStream)
{
  int i;

  for (i = 0; i < 16 && i < name.length(); i++) {
    intStream.append(name[i]);
  }
  if (i < 16) {
    intStream.append(0);
  }
}

void Patch::storeEndPosition(IntStream intStream,
			     PositionList* sectionEndPositions)
{
  BitStream patchStream;
  patchParser->generate(&intStream, &patchStream);
  sectionEndPositions->push_back(patchStream.getSize()/8-1);
}

Module* Patch::getModule(Patch* patch, ModuleSection::Type section,
			 int index, string context)
{
  Module* module;
  if (section == ModuleSection::MORPH) {
    module = 0;
  }
  else {
    module = patch->getModuleSection(section)->getModule(index);
    if (module == 0) {
      throw PatchException(string("Can not find module for ") +
			   string(context) + ".",
			   index);
    }
  }
  return module;
}

string Patch::extractName(Packet* name)
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
