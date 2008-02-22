
/* JFlex example: part of Java language lexer specification */

/**
 * This class is a simple example lexer.
 */
package net.sf.nmedit.jpdl2.format;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

@SuppressWarnings({"unused", "static-access"})
%%


%class PDL2Lexer 
%byaccj
%integer
%unicode

%line
%column

%{

  /* store a reference to the parser object */
  private PDL2Parser yyparser;

  /* constructor taking an additional parser object */
  public PDL2Lexer(java.io.Reader r, PDL2Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }

  StringBuffer string = new StringBuffer();

  public int getLineNumber()
  {
    return yyline;
  }
  
  public int getColumnNumber()
  {
    return yycolumn;
  }

%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

Identifier = [:jletter:] [:jletterdigit:]*

PacketRef = [:jletter:] [:jletterdigit:]* \$ [:jletter:] [:jletterdigit:]*

InlinePacketRef = [:jletter:] [:jletterdigit:]* \$ \$

LabelName = @ {Identifier}

DecIntegerLiteral = 0 | [1-9][0-9]*
HexIntegerLiteral = "0x" [0-9a-fA-F] {1,8}
DualIntegerLiteral = [01] {1,65} [dD]  /* more than 32 digits error is handled by parser */

Brackets = [\[\]\{\}\(\)]

%state STRING

%%

/* keywords */
<YYINITIAL> "if"         {  return yyparser.IF;}
/* boolean literal */
<YYINITIAL> "true"         { yyparser.yylval = new PDL2ParserVal(1);
                             return yyparser.BOOLEAN_LITERAL;}
<YYINITIAL> "false"        { yyparser.yylval = new PDL2ParserVal(0);
                             return yyparser.BOOLEAN_LITERAL;}
<YYINITIAL> "messageId"    { return yyparser.TK_MESSAGEID;}
<YYINITIAL> "fail"         { return yyparser.TK_FAIL;}
<YYINITIAL> "switch"       { return yyparser.TK_SWITCH;}
<YYINITIAL> "case"       { return yyparser.TK_CASE;}
<YYINITIAL> "default"       { return yyparser.TK_DEFAULT;}

<YYINITIAL> "(int)"        { return yyparser.CAST_TO_INT; }
<YYINITIAL> "(boolean)"    { return yyparser.CAST_TO_BOOLEAN; }
  
<YYINITIAL> {
  \$                           { return yyparser.TKDOLLAR; }
  
  {InlinePacketRef}        { yyparser.yylval = new PDL2ParserVal(yytext());
                             return yyparser.INLINEPACKETREF; }
  {PacketRef}              { yyparser.yylval = new PDL2ParserVal(yytext());
                             return yyparser.PACKETREF; }
  
  /* identifiers */ 
  {Identifier}                   { yyparser.yylval = new PDL2ParserVal(yytext());
                                   return yyparser.IDENTIFIER; }
 
  /* literals */
  {DecIntegerLiteral}            { yyparser.yylval = new PDL2ParserVal(Integer.parseInt(yytext()));
                                   return yyparser.DEC_INTEGER_LITERAL; }
  {HexIntegerLiteral}            { yyparser.yylval = new PDL2ParserVal(PDLUtils.parseHex(yytext()));
                                   return yyparser.HEX_INTEGER_LITERAL; }
  {DualIntegerLiteral}           { yyparser.yylval = new PDL2ParserVal(PDLUtils.parseDual(yytext()));
                                   return yyparser.DUAL_INTEGER_LITERAL; }
  \"                             { string.setLength(0); yybegin(STRING); }

  {LabelName}                    { yyparser.yylval = new PDL2ParserVal(yytext().substring(1));
                                   return yyparser.LABEL; }

  /* operators */
  "+"                            { return yyparser.TK_PLUS; }
  "-"                            { return yyparser.TK_MINUS; }
  "*"                            { return yyparser.TK_TIMES; }
  "/"                            { return yyparser.TK_DIVIDE; }
  "%"                            { return yyparser.TK_PERCENT; }
  "&"                            { return yyparser.TK_AND; }
  "|"                            { return yyparser.TK_OR; }
  "\^"                            { return yyparser.TK_XOR; }
  "~"                            { return yyparser.TK_TILDE; }
  "!"                            { return yyparser.TK_NOT; }
  
  /* special operators */
  "<<"                           { return yyparser.TK_LSHIFT; }
  ">>>"                          { return yyparser.TK_URSHIFT; }
  ">>"                           { return yyparser.TK_RSHIFT; }
  "=="                           { return yyparser.CMP_EQ; }
  "!="                           { return yyparser.CMP_NEQ; }
  ">="                           { return yyparser.CMP_GEQ; }
  "<="                           { return yyparser.CMP_LEQ; }
  ">"                           { return yyparser.CMP_GT; }
  "<"                           { return yyparser.CMP_LT; }
  ":="                           { return yyparser.ASSIGN; }
  "="                           { return yyparser.TK_EQ; }
  "?"                           { return yyparser.TK_INTERROGATIONMARK; }
  
  /* special chars */
  ";"                            { return (int) yycharat(0); }
  ":"                            { return (int) yycharat(0); }
  "#"                            { return (int) yycharat(0); }

  /* brackets */
  {Brackets}                     { return (int) yycharat(0); }

  /* comments */
  {Comment}                      { /* ignore */ }
 
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); 
                                   yyparser.yylval = new PDL2ParserVal(string.toString());
                                   return yyparser.STRING_LITERAL; }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

/* error fallback */
.|\n                             { throw new Error("[line "+yyline+":"+yycolumn+"] Illegal character <"+
                                                    yytext()+">"); }
