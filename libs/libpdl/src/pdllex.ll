/*
    Protocol Definition Language
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

%{
  #include "pdl/pdllexer.h"
  #include "pdlparser.h"

  extern int pdlline;

  int value;
  char svalue[16];
  int i;
%}

%option prefix="pdl"
%option c++

%%

"0x"[0-9a-f]* {
  value = 0;
  for(i = 2; i < strlen(yytext); i++) {
    switch(yytext[i]) {
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
      value = value*16 + yytext[i]-48;
      break;
    case 'a':
      value = value*16 + 10;
      break;
    case 'b':
      value = value*16 + 11;
      break;
    case 'c':
      value = value*16 + 12;
      break;
    case 'd':
      value = value*16 + 13;
      break;
    case 'e':
      value = value*16 + 14;
      break;
    case 'f':
      value = value*16 + 15;
      break;
    }
  }
  snprintf(svalue, 16, "%d", value);
  pdllval = svalue;
  return NUMBER;
}

[0-9]* {
  pdllval = yytext;
  return NUMBER;
}

[a-zA-Z][a-zA-Z0-9]* {
  pdllval = yytext;
  return IDENTIFIER;
}

% return PAD;

":=" return ASSIGN;

"=>" return CHOOSE;

\* return TIMES;

\/ return STOP;

= return COMPARE;

! return NOT_COMPARE;

\$ return BIND;

: return SIZE;

\; return END;

\? return OPTIONAL;

# return CONDITIONAL;

[ \t]+

\n pdlline++;

"//".*$ {
  // // comment (to end of line)
};

"{"((.*)\n)*"}" {
  // { Comment }
};

"/*"((.*)\n)*"*/" {
  // /* Comment */
};

%%

int pdlwrap(void) {
    return 1;
}
