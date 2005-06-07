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

#ifndef PATCH_H
#define PATCH_H

#include <string>
#include <map>

#include "nmpatch/note.h"
#include "nmpatch/modulesection.h"
#include "nmpatch/cable.h"
#include "nmpatch/morph.h"
#include "nmpatch/knobmap.h"
#include "nmpatch/ctrlmap.h"

/**
 * This is the top class in the patch data structure. It represents a 
 * Nord Modular patch. It can read and write the PCH file format.
 */
class Patch
{
public:

  enum Portamento {
    NORMAL = 0,
    AUTO
  };

  enum CableVisibility {
    INVISIBLE = 0,
    VISIBLE
  };

  enum RangeLimit {
    MIN = 0,
    MAX
  };

  typedef list<Note*> NoteList;
  typedef list<KnobMap*> KnobMapList;
  typedef list<CtrlMap*> CtrlMapList;

  Patch();
  Patch(string);

  virtual ~Patch();
  
  string write();
  void writeParameter(string&, int);

  void setName(string name);
  string getName();

  void setNotes(string notes);
  string getNotes();

  void setPortamento(Portamento portamento);
  Portamento getPortamento();

  void setCableVisibility(Cable::Color, CableVisibility);
  CableVisibility getCableVisibility(Cable::Color);

  void setKeyboardRange(RangeLimit, int);
  int getKeyboardRange(RangeLimit);

  void setVelocityRange(RangeLimit, int);
  int getVelocityRange(RangeLimit);
  
  void setBendRange(int);
  int getBendRange();

  void setPortamentoTime(int);
  int getPortamentoTime();
  
  void setRequestedVoices(int);
  int getRequestedVoices();

  void setSectionSeparationPosition(int);
  int getSectionSeparationPosition();

  void setOctaveShift(int);
  int getOctaveShift();

  ModuleSection* getModuleSection(ModuleSection::Type);

  Note* newMIDINote(int, int, int);
  NoteList getMIDINotes() const;
  void removeMIDINote(Note*);

  Morph* getMorph(Morph::Type);

  KnobMap* newKnobMap(ModuleSection::Type, Module*, ModuleType::Parameter);
  KnobMapList getKnobMaps() const;
  void removeKnobMap(KnobMap*);

  CtrlMap* newCtrlMap(ModuleSection::Type, Module*, ModuleType::Parameter);
  CtrlMapList getCtrlMaps() const;
  void removeCtrlMap(CtrlMap*);

private:

  string name;
  string notes;
  Portamento portamento;
  map<Cable::Color, CableVisibility> cableVisibility;
  map<RangeLimit, int> keyboardRange;
  map<RangeLimit, int> velocityRange;
  int bendRange;
  int portamentoTime;
  int requestedVoices;
  int sectionSeparationPosition;
  int octaveShift;
  map<ModuleSection::Type, ModuleSection*> moduleSection;
  NoteList midiNotes;
  map<Morph::Type, Morph*> morph;
  KnobMapList knobMaps;
  CtrlMapList ctrlMaps;
};

#endif
