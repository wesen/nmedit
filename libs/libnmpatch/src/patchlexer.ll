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
  #include "nmpatch/nmlexer.h"
  #include "patchparser.h"

  extern int pchline;
%}
%option c++
%%

"[Header]"  { return HEADER; }
"[/Header]" { return HEADER_END; }

"[ModuleDump]"  { return MODULE_DUMP; }
"[/ModuleDump]" { return MODULE_DUMP_END; }

"[CurrentNoteDump]"  { return CURRENT_NOTE_DUMP; }
"[/CurrentNoteDump]" { return CURRENT_NOTE_DUMP_END; }

"[CableDump]"  { return CABLE_DUMP; }
"[/CableDump]" { return CABLE_DUMP_END; }

"[ParameterDump]"  { return PARAMETER_DUMP; }
"[/ParameterDump]" { return PARAMETER_DUMP_END; }

"[CustomDump]"  { return CUSTOM_DUMP; }
"[/CustomDump]" { return CUSTOM_DUMP_END; }

"[MorphMapDump]"  { return MORPH_MAP_DUMP; }
"[/MorphMapDump]" { return MORPH_MAP_DUMP_END; }

"[KeyboardAssignment]"  { return KEYBOARD_ASSIGNMENT; }
"[/KeyboardAssignment]" { return KEYBOARD_ASSIGNMENT_END; }

"[KnobMapDump]"  { return KNOB_MAP_DUMP; }
"[/KnobMapDump]" { return KNOB_MAP_DUMP_END; }

"[CtrlMapDump]"  { return CTRL_MAP_DUMP; }
"[/CtrlMapDump]" { return CTRL_MAP_DUMP_END; }

"[NameDump]"  { return NAME_DUMP; }
"[/NameDump]" { return NAME_DUMP_END; }

"Version=Nord Modular patch 3.0" {
  return NMVERSION;
}

[^\r\n ]*[ \t]* {
  nmlval = yytext;
  return PARAM;
}

"[Notes]"(.*(\r\n|\n))*"[/Notes]" { 
  yytext[strlen(yytext)-9] = '\0';
  nmlval = &yytext[yytext[8] != '\n' ? 8 : 9];
  return NOTES;
}

"[/NOTES]"(\r\n|\n)*"[Notes]"(.*(\r\n|\n))* { 
  nmlval = yytext;
  return NOTES;
}

(\r\n|\n) {
  pchline++;
  return '\n';
}

%%

int nmwrap(void) {
    return 1;
}
