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
#include <stdio.h>

extern bool init_parser(const char* filename, Patch* patch);
extern void nmparse();

Patch::Patch()
{
  moduleSection[ModuleSection::POLY] = new ModuleSection();
  moduleSection[ModuleSection::COMMON] = new ModuleSection();

  morph[Morph::MORPH1] = new Morph();
  morph[Morph::MORPH2] = new Morph();
  morph[Morph::MORPH3] = new Morph();
  morph[Morph::MORPH4] = new Morph();
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
			   Module::Parameter parameter)
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
			   Module::Parameter parameter)
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
      writeParameter(result, (*m)->getType());
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
      if ((*m)->numberOfParameters() > 0) {
	writeParameter(result, (*m)->getIndex());
	writeParameter(result, (*m)->getType());
	writeParameter(result, (*m)->numberOfParameters());
	for (int p = 0; p < (*m)->numberOfParameters(); p++) {
	  writeParameter(result, (*m)->getParameter((Module::Parameter)p));
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
      if ((*m)->numberOfCustomValues() > 0) {
	writeParameter(result, (*m)->getIndex());
	writeParameter(result, (*m)->numberOfCustomValues());
	for (int p = 0; p < (*m)->numberOfCustomValues(); p++) {
	  writeParameter(result, (*m)->getCustomValue((Module::CustomValue)p));
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
