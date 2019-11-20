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
  #include <cstdio>
  #include <fstream>

  #include "pdl/parser.h"
  #include "pdl/protocol.h"
  #include "pdl/packetparser.h"
  #include "pdl/condition.h"
  #include "pdl/pdllexer.h"
  #include "pdl/pdlexception.h"

  int pdllex(void);
  void pdlerror(char*);
  int pdlstoi(string);

  ifstream* pdlinstream = 0;
  istream* pdlin = 0;
  PDLLexer* pdllexer;

  int pdlline = 1;

  Protocol* protocol;
  PacketParser* packetParser;
  Condition* condition = 0;
  bool optional = false;

  int pdllex(void)
  {
	return pdllexer->pdllex();
  }

// Work around a bug in the relation between bison and GCC 3.x:
#if defined (__GNUC__) && 3 <= __GNUC__
#define __attribute__(arglist)
#endif

%}

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
	  packetParser = protocol->newPacketParser($1, pdlstoi($3));
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
        { condition = new Condition($2, pdlstoi($4), false);
	  optional = false; } |

	CONDITIONAL IDENTIFIER NOT_COMPARE NUMBER CHOOSE
        { condition = new Condition($2, pdlstoi($4), true);
	  optional = false; } |

	OPTIONAL
        { condition = 0; optional = true; } |

        { condition = 0; optional = false; }
        ;

matcherType:
	IDENTIFIER BIND IDENTIFIER
        { packetParser->addPacketMatcher("", $1, $3, condition, optional); } |

	IDENTIFIER TIMES IDENTIFIER BIND IDENTIFIER
        { packetParser->addPacketMatcher($1, $3, $5, condition, optional); } |

	IDENTIFIER SIZE NUMBER
        { packetParser->addVariableMatcher(1, $1, pdlstoi($3), 0,
					   condition, optional); } |

	NUMBER TIMES IDENTIFIER SIZE NUMBER STOP NUMBER
        { packetParser->addVariableMatcher(pdlstoi($1), $3, pdlstoi($5), pdlstoi($7),
					   condition, optional); } |

	NUMBER SIZE NUMBER
        { packetParser->addConstantMatcher(pdlstoi($1), pdlstoi($3),
					   condition, optional); }
	;

%%

void pdlerror(char* s) {
    char buf[200];
    snprintf(buf, 200, "%s at line %d\n", s, pdlline);
    throw PDLException(string(buf), 0);
}

bool init_pdl_parser(const char* filename, Protocol* p)
{
  protocol = p;
  
  if (pdlinstream) {
    pdlinstream->close();
    delete pdlinstream;
    delete pdlin;
  }
  pdlinstream = new ifstream();
  pdlinstream->open(filename);
  pdlin = new istream(pdlinstream->rdbuf());

  pdllexer = new PDLLexer(pdlin);
  return pdlinstream->good();
}

int pdlstoi(string param)
{
  return atoi(param.c_str());
}
  
