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

%{
  #include <stdio.h>
  #include <fstream>
  #include "nmpatch/parser.h"
  #include "nmpatch/patch.h"
  #include "nmpatch/modulesection.h"
  #include "nmpatch/nmlexer.h" 
  #include "nmpatch/patchexception.h"

  int nmlex(YYSTYPE *);
  void yyerror(char*);
  int pchstoi(string);

  ifstream* yyinstream = 0;
  istream* yyin = 0;

  int pchline = 1;
  Patch* patch;
  ModuleSection* section;
  Module *module;
  int parameterIndex;
  int customValueIndex;

  NMLexer* nmlexer;

  int nmlex(void)
  {
  	return nmlexer->nmlex();
  }

// Work around a bug in the relation between bison and GCC 3.x:
#if defined (__GNUC__) && 3 <= __GNUC__
#define __attribute__(arglist)
#endif

%}

%token PARAM
%token NMVERSION
%token HEADER
%token HEADER_END
%token MODULE_DUMP
%token MODULE_DUMP_END
%token CURRENT_NOTE_DUMP
%token CURRENT_NOTE_DUMP_END
%token CABLE_DUMP
%token CABLE_DUMP_END
%token PARAMETER_DUMP
%token PARAMETER_DUMP_END
%token CUSTOM_DUMP
%token CUSTOM_DUMP_END
%token MORPH_MAP_DUMP
%token MORPH_MAP_DUMP_END
%token KEYBOARD_ASSIGNMENT
%token KEYBOARD_ASSIGNMENT_END
%token KNOB_MAP_DUMP
%token KNOB_MAP_DUMP_END
%token CTRL_MAP_DUMP
%token CTRL_MAP_DUMP_END
%token NAME_DUMP
%token NAME_DUMP_END
%token NOTES

%%

patch:
       patch patch_part
       |
       ;

patch_part:
        header |
	module_dump |
	current_note_dump |
	cable_dump |
	parameter_dump |
	custom_dump |
	morph_map_dump |
	keyboard_assignment |
	knob_map_dump |
	ctrl_map_dump |
	name_dump |
	notes |
	'\n'
        ;

header:
	HEADER '\n'
	NMVERSION '\n'
	PARAM PARAM PARAM PARAM PARAM PARAM PARAM PARAM
	PARAM PARAM PARAM PARAM PARAM PARAM PARAM PARAM
	PARAM PARAM PARAM PARAM PARAM PARAM PARAM '\n'
	HEADER_END '\n'
        {
	  patch->setKeyboardRange(Patch::MIN, pchstoi($5));
	  patch->setKeyboardRange(Patch::MAX, pchstoi($6));
	  patch->setVelocityRange(Patch::MIN, pchstoi($7));
	  patch->setVelocityRange(Patch::MAX, pchstoi($8));
	  patch->setBendRange(pchstoi($9));
	  patch->setPortamentoTime(pchstoi($10));
	  patch->setPortamento((Patch::Portamento)pchstoi($11));
	  patch->setRequestedVoices(pchstoi($12));
	  patch->setSectionSeparationPosition(pchstoi($13));
	  patch->setOctaveShift(pchstoi($14));
	  patch->getModuleSection(ModuleSection::POLY)
	    ->setVoiceRetrigger((ModuleSection::VoiceRetrigger)pchstoi($15));
	  patch->getModuleSection(ModuleSection::COMMON)
	    ->setVoiceRetrigger((ModuleSection::VoiceRetrigger)pchstoi($16));
	  patch->setCableVisibility(Cable::RED,
				    (Patch::CableVisibility)pchstoi($21));
	  patch->setCableVisibility(Cable::BLUE,
				    (Patch::CableVisibility)pchstoi($22));
	  patch->setCableVisibility(Cable::YELLOW,
				    (Patch::CableVisibility)pchstoi($23));
	  patch->setCableVisibility(Cable::GRAY,
				    (Patch::CableVisibility)pchstoi($24));
	  patch->setCableVisibility(Cable::GREEN,
				    (Patch::CableVisibility)pchstoi($25));
	  patch->setCableVisibility(Cable::PURPLE,
				    (Patch::CableVisibility)pchstoi($26));
	  patch->setCableVisibility(Cable::WHITE,
				    (Patch::CableVisibility)pchstoi($27));
	}
	;

module_dump:
	MODULE_DUMP '\n'
	PARAM '\n'
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($3));
	}
	module_list
	MODULE_DUMP_END '\n'
	|
	MODULE_DUMP '\n'
	PARAM
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($3));
	}
	module_list
	MODULE_DUMP_END '\n'

	;

module_list:
	module_list PARAM PARAM PARAM PARAM '\n'
        {
	  module = section->newModule((Module::Type)pchstoi($3), pchstoi($2));
	  module->setPosition(pchstoi($4), pchstoi($5));
	}
	| '\n'
	|
	;

current_note_dump:
	CURRENT_NOTE_DUMP '\n'
	note_list '\n'
	CURRENT_NOTE_DUMP_END '\n'
	;

note_list:
	note_list PARAM PARAM PARAM
        {
	  patch->newMIDINote(pchstoi($2), pchstoi($3), pchstoi($4));
	}
	|
	;

cable_dump:
	CABLE_DUMP '\n'
	PARAM '\n'
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($3));
	}
	cable_list
	CABLE_DUMP_END '\n'
	|
	CABLE_DUMP '\n'
	PARAM
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($3));
	}
	cable_list
	CABLE_DUMP_END '\n'
	;

cable_list:
	cable_list PARAM PARAM PARAM PARAM PARAM PARAM PARAM '\n'
        {
	  section->newCable((Cable::Color)pchstoi($2),
			    pchstoi($3),
			    (Module::Port)pchstoi($4),
			    (Cable::ConnectorType)pchstoi($5),
			    pchstoi($6),
			    (Module::Port)pchstoi($7),
			    (Cable::ConnectorType)pchstoi($8));
	}
	| '\n'
	|
	;

parameter_dump:
	PARAMETER_DUMP '\n'
	PARAM '\n'
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($3));
	}
	module_parameter_list
	PARAMETER_DUMP_END '\n'
	;

module_parameter_list:
	module_parameter_list PARAM PARAM PARAM
        {
	  module = section->getModule(pchstoi($2));
	  parameterIndex = 0;
	}
	parameter_list '\n'
	| '\n'
	|
	;

parameter_list:
	parameter_list PARAM
        {
	  module->setParameter((Module::Parameter)parameterIndex++, pchstoi($2));
	}
	|
	;

custom_dump:
	CUSTOM_DUMP '\n'
	PARAM
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($3));
	}
	module_custom_list
	CUSTOM_DUMP_END '\n'
	;

module_custom_list:
	module_custom_list PARAM PARAM
        {
	  module = section->getModule(pchstoi($2));
	  customValueIndex = 0;
	}
        custom_list '\n'
	| '\n'
	|
	;

custom_list:
	custom_list PARAM
        {
	  module->setCustomValue((Module::CustomValue)customValueIndex++,
				 pchstoi($2));
	}
	|
	;

morph_map_dump:
	MORPH_MAP_DUMP '\n'
	PARAM PARAM PARAM PARAM '\n'
        {
	  patch->getMorph(Morph::MORPH1)->setValue(pchstoi($3));
	  patch->getMorph(Morph::MORPH2)->setValue(pchstoi($4));
	  patch->getMorph(Morph::MORPH3)->setValue(pchstoi($5));
	  patch->getMorph(Morph::MORPH4)->setValue(pchstoi($6));
	}
	morph_map_list '\n'
	MORPH_MAP_DUMP_END '\n'
	;

morph_map_list:
	morph_map_list PARAM PARAM PARAM PARAM PARAM
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($2));
	  module = section->getModule(pchstoi($3));
	  Morph* morph = patch->getMorph((Morph::Type)pchstoi($5));
	  MorphMap* morphMap =
	    morph->newMorphMap((ModuleSection::Type)pchstoi($2),
			       module,
			       (Module::Parameter)pchstoi($4));
	  morphMap->setRange(pchstoi($6));
	}
	|
	;

keyboard_assignment:
	KEYBOARD_ASSIGNMENT '\n'
	PARAM PARAM PARAM PARAM '\n'
        {
	  patch->getMorph(Morph::MORPH1)->
	    setKeyboardAssignment((Morph::KeyboardAssignment)pchstoi($3));
	  patch->getMorph(Morph::MORPH2)->
	    setKeyboardAssignment((Morph::KeyboardAssignment)pchstoi($4));
	  patch->getMorph(Morph::MORPH3)->
	    setKeyboardAssignment((Morph::KeyboardAssignment)pchstoi($5));
	  patch->getMorph(Morph::MORPH4)->
	    setKeyboardAssignment((Morph::KeyboardAssignment)pchstoi($6));
	}
	KEYBOARD_ASSIGNMENT_END '\n'
	;

knob_map_dump:
	KNOB_MAP_DUMP '\n'
	knob_map_list
	KNOB_MAP_DUMP_END '\n'
	;

knob_map_list:
	knob_map_list PARAM PARAM PARAM PARAM '\n'
        {
	  if (pchstoi($2) == ModuleSection::MORPH) {
	    module = 0;
	  }
	  else {
	    section = patch->getModuleSection((ModuleSection::Type)pchstoi($2));
	    module = section->getModule(pchstoi($3));
	  }
	  KnobMap* knobMap =
	    patch->newKnobMap((ModuleSection::Type)pchstoi($2),
			      module,
			      (Module::Parameter)pchstoi($4));
	  knobMap->setKnob((KnobMap::Knob)pchstoi($5));
	}
	| '\n'
	|
	;
	
ctrl_map_dump:
	CTRL_MAP_DUMP '\n'
	ctrl_map_list
	CTRL_MAP_DUMP_END '\n'
	;
	
ctrl_map_list:
	ctrl_map_list PARAM PARAM PARAM PARAM '\n'
        {
	  if (pchstoi($2) == ModuleSection::MORPH) {
	    module = 0;
	  }
	  else {
	    section = patch->getModuleSection((ModuleSection::Type)pchstoi($2));
	    module = section->getModule(pchstoi($3));
	  }
	  CtrlMap* ctrlMap =
	    patch->newCtrlMap((ModuleSection::Type)pchstoi($2),
			      module,
			      (Module::Parameter)pchstoi($4));
	  ctrlMap->setCC(pchstoi($5));
	}
	| '\n'
	|
	;
	
name_dump:
	NAME_DUMP '\n'
	PARAM '\n'
        {
	  section = patch->getModuleSection((ModuleSection::Type)pchstoi($3));
	}
	name_list
	NAME_DUMP_END '\n'
	;

name_list:
	name_list PARAM name '\n'
        {
	  module = section->getModule(pchstoi($2));
	  module->setName($3);
	}
	|
	;

name:
	name PARAM
        {
	  $$ = $1 + $2;
	}
	|
        {
	  $$ = "";
	}
	;

notes:
	NOTES { patch->setNotes($1); }
	;

%%

void yyerror(char* s) {
    char buf[200];
    snprintf(buf, 200, "%s at line %d.", s, pchline);
    throw PatchException(string(buf), 0);
}

bool init_parser(const char* filename, Patch* p)
{
  patch = p;

  if (yyinstream) {
    yyinstream->close();
    delete yyinstream;
    delete yyin;
  }
  yyinstream = new ifstream();
  yyinstream->open(filename);
  yyin = new istream(yyinstream->rdbuf());

  nmlexer = new NMLexer(yyin);
  return yyinstream->good();
}

int pchstoi(string param)
{
  return atoi(param.c_str());
}
