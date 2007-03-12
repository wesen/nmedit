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
	  packetParser = protocol.newPacketParser($1.sval, $3.ival);
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
        { condition = new Condition($2.sval, $4.ival, false);
	  optional = false; } |

	CONDITIONAL IDENTIFIER NOT_COMPARE NUMBER CHOOSE
        { condition = new Condition($2.sval, $4.ival, true);
	  optional = false; } |

	OPTIONAL
        { condition = null; optional = true; } |

        { condition = null; optional = false; }
        ;

matcherType:
	IDENTIFIER BIND IDENTIFIER
        { packetParser.addPacketMatcher("", $1.sval, $3.sval,
                                        condition, optional); } |

	IDENTIFIER TIMES IDENTIFIER BIND IDENTIFIER
        { packetParser.addPacketMatcher($1.sval, $3.sval, $5.sval,
                                        condition, optional); } |

	IDENTIFIER SIZE NUMBER
        { packetParser.addVariableMatcher(1, $1.sval, $3.ival, 0,
					  condition, optional); } |

	NUMBER TIMES IDENTIFIER SIZE NUMBER STOP NUMBER
        { packetParser.addVariableMatcher($1.ival, $3.sval, $5.ival, $7.ival,
					  condition, optional); } |

	NUMBER SIZE NUMBER
        { packetParser.addConstantMatcher($1.ival, $3.ival,
                                          condition, optional); }
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
  try {
    return yylex.yylex();
  }
  catch(java.io.IOException e) {
    yyerror(e.toString());
  }
  return 0;
}

public void yyerror(String s)
{
  System.out.println(s + " at line " + pdlline);
}

public void init(String filename, Protocol p)
  throws java.io.IOException
{
  protocol = p;
  
  if (reader != null) {
    reader.close();
  }
  java.io.InputStream in = getClass().getResourceAsStream(filename);
  if (in != null) {
    reader = new java.io.InputStreamReader(in);
  }
  else {
    reader = new java.io.FileReader(filename);
  }
  yylex = new PdlLex(reader, this);
}

public void init(java.io.Reader reader, Protocol p)
  throws java.io.IOException
{
  protocol = p;
  
  if (this.reader != null) {
    this.reader.close();
  }
  this.reader = reader;
  yylex = new PdlLex(reader, this);
}
