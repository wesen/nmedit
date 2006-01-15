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

%token NUMBER
%token IDENTIFIER
%token PAD
%token ASSIGN
%token CHOOSE
%token TIMES
%token STOP
%token OPTIONAL
%token COMPARE
%token NOT_COMPARE
%token BIND
%token SIZE
%token END
%token CONDITIONAL

%%

protocol:
	packet protocol |
	;

packet:
	IDENTIFIER PAD NUMBER ASSIGN
        {
	  packetParser = protocol.newPacketParser($1, $3);
	}
	matchers END
	;

matchers:
	matcher matchers |
	;

matcher:
	matcherOption matcherType
	;

matcherOption:
	CONDITIONAL IDENTIFIER COMPARE NUMBER CHOOSE
        { condition = new Condition($2, $4, false);
	  optional = false; } |

	CONDITIONAL IDENTIFIER NOT_COMPARE NUMBER CHOOSE
        { condition = new Condition($2, $4, true);
	  optional = false; } |

	OPTIONAL
        { condition = null; optional = true; } |

        { condition = null; optional = false; }
        ;

matcherType:
	IDENTIFIER BIND IDENTIFIER
        { packetParser.addPacketMatcher("", $1, $3, condition, optional); } |

	IDENTIFIER TIMES IDENTIFIER BIND IDENTIFIER
        { packetParser.addPacketMatcher($1, $3, $5, condition, optional); } |

	IDENTIFIER SIZE NUMBER
        { packetParser.addVariableMatcher(1, $1, $3, 0,
					  condition, optional); } |

	NUMBER TIMES IDENTIFIER SIZE NUMBER STOP NUMBER
        { packetParser.addVariableMatcher($1, $3, $5, $7,
					  condition, optional); } |

	NUMBER SIZE NUMBER
        { packetParser.addConstantMatcher($1, $3, condition, optional); }
	;

%%

public int pdlline = 1;

private Protocol protocol;
private PacketParser packetParser;
private Condition condition;
private boolean optional = false;
private java.io.Reader reader;
private PdlLex yylex;

private int yylex()
{
  return yylex.yylex();
}

void yyerror(String s) {
    throw PDLException(s + " at line " + pdlline, 0);
}

boolean init(String filename, Protocol p)
{
  protocol = p;
  
  if (reader != null) {
    reader.close();
  }
  reader = new java.io.FileReader(filename);
  yylex = new PdlLex(reader, this);

  return true;
}
