/*
    Protocol Definition Language
    Copyright (C) 2003-2006 Marcus Andersson

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

package net.sf.nmedit.jpdl;

%%

%class PdlLex
%integer
%eofval{
  return 0;
%eofval}
%eofclose
%unicode
%line
%column

%{
  /* store a reference to the parser object */
  private PdlParse yyparser;

  /* constructor taking an additional parser object */
  public PdlLex(java.io.Reader r, PdlParse yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}


%%

<YYINITIAL> {

"0x"[0-9a-f]* {
  int value = 0;
  for(int i = 2; i < yytext().length(); i++) {
    switch(yytext().charAt(i)) {
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
      value = value*16 + Integer.parseInt(yytext().substring(i, i+1));
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
  yyparser.yylval = new PdlParseVal(value);
  return PdlParse.NUMBER;
}

[0-9]* {
  yyparser.yylval = new PdlParseVal(Integer.parseInt(yytext()));
  return PdlParse.NUMBER;
}

[a-zA-Z][a-zA-Z0-9]* {
  yyparser.yylval = new PdlParseVal(yytext());
  return PdlParse.IDENTIFIER;
}

% { return PdlParse.PAD; }

":=" { return PdlParse.ASSIGN; }

"=>" { return PdlParse.CHOOSE; }

\* { return PdlParse.TIMES; }

\/ { return PdlParse.STOP; }

= { return PdlParse.COMPARE; }

\! { return PdlParse.NOT_COMPARE; }

\$ { return PdlParse.BIND; }

: { return PdlParse.SIZE; }

\; { return PdlParse.END; }

\? { return PdlParse.OPTIONAL; }

# { return PdlParse.CONDITIONAL; }

[ \t\r]+ { }

\n { yyparser.pdlline++; }

"//".*$ {
  // // comment (to end of line)
}

"{"((.*)\n)*"}" {
  // { Comment }
}

"/*"((.*)\n)*"*/" {
  // /* Comment */
}

}
