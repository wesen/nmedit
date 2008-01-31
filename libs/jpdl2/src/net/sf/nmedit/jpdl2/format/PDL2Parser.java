//### This file created by BYACC 1.8(/Java extension  1.14)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package net.sf.nmedit.jpdl2.format;



//#line 2 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
import java.io.*;
import net.sf.nmedit.jpdl2.impl.*;
import java.util.ArrayList;
import java.util.List;
import net.sf.nmedit.jpdl2.*;
//#line 23 "PDL2Parser.java"




public class PDL2Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class PDL2ParserVal is defined in PDL2ParserVal.java


String   yytext;//user variable to return contextual strings
PDL2ParserVal yyval; //used to return semantic vals from action routines
PDL2ParserVal yylval;//the 'lval' (result) I got from yylex()
PDL2ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new PDL2ParserVal[YYSTACKSIZE];
  yyval=new PDL2ParserVal();
  yylval=new PDL2ParserVal();
  valptr=-1;
}
void val_push(PDL2ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
PDL2ParserVal val_pop()
{
  if (valptr<0)
    return new PDL2ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
PDL2ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new PDL2ParserVal();
  return valstk[ptr];
}
//#### end semantic value section ####
public final static short NL=257;
public final static short DEC_INTEGER_LITERAL=258;
public final static short HEX_INTEGER_LITERAL=259;
public final static short DUAL_INTEGER_LITERAL=260;
public final static short STRING_LITERAL=261;
public final static short BOOLEAN_LITERAL=262;
public final static short LABEL=263;
public final static short PACKETREF=264;
public final static short IDENTIFIER=265;
public final static short LSHIFT=266;
public final static short RSHIFT=267;
public final static short URSHIFT=268;
public final static short CMP_EQ=269;
public final static short CMP_NEQ=270;
public final static short CMP_LT=271;
public final static short CMP_LEQ=272;
public final static short CMP_GT=273;
public final static short CMP_GEQ=274;
public final static short ASSIGN=275;
public final static short CAST_TO_INT=276;
public final static short CAST_TO_BOOLEAN=277;
public final static short IF=278;
public final static short TKDOLLAR=279;
public final static short RARROW=280;
public final static short TK_MESSAGEID=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,   25,   26,    4,    4,   36,    2,    2,
    3,    3,   15,   15,   15,   15,   34,   31,   31,   31,
   31,   31,   31,   31,   33,   18,   17,   21,   20,   20,
   19,   14,   14,   16,   24,   28,   28,   29,   37,   29,
   38,   22,   39,   22,   40,   22,   27,   27,   30,   30,
   23,    1,    1,    1,   13,    7,    7,    7,    7,    7,
    7,    7,    7,    8,    8,    8,    8,    8,    8,    9,
    9,    9,    9,   10,   10,   10,   10,   11,   11,   11,
   11,   11,   12,   12,   12,   32,   32,    6,    5,   35,
   35,   35,   35,   35,
};
final static short yylen[] = {                            2,
    0,    1,    2,    3,    1,    1,    2,    0,    5,    3,
    3,    1,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    4,    1,    3,    5,    1,    3,
    2,    1,    2,    3,    1,    1,    1,    1,    0,    3,
    0,    6,    0,    7,    0,    7,    1,    3,    2,    1,
    1,    1,    1,    1,    1,    3,    1,    1,    1,    1,
    1,    1,    1,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    1,    1,    1,
    1,    1,    2,    2,    2,    2,    2,    7,    2,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    5,    0,    2,    0,    0,   17,
    7,    0,    3,    0,    4,   10,    0,   52,   53,   54,
   11,   18,   35,    0,    0,    0,   16,   15,   39,    0,
    0,   23,    0,   32,    0,   20,    0,   21,   22,   38,
   19,    0,   36,    0,   37,   24,   13,    0,    0,    0,
    0,    0,   14,    0,    0,    0,   33,   31,    0,    0,
   49,    9,   27,   80,   81,   82,    0,    0,   78,    0,
    0,    0,    0,    0,   79,   61,    0,   62,   57,   58,
   60,   59,   51,    0,   63,    0,    0,   40,   47,    0,
    0,   34,    0,   30,    0,    0,    0,    0,    0,    0,
   94,   93,   90,   91,   92,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   41,   25,    0,    0,    0,    0,   56,    0,    0,   64,
   65,   66,   67,   68,   69,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   48,   45,   43,   28,    0,    0,
   42,    0,    0,    0,   46,   44,    0,   88,
};
final static short yydgoto[] = {                          2,
   75,    3,    4,    5,  129,   76,   77,   78,   79,   80,
   81,   82,   83,   32,   33,   34,   35,   36,   37,   38,
   39,   40,   84,   41,    6,    7,   88,   42,   43,   44,
   89,   85,   46,    8,  106,   17,   51,  144,  153,  152,
};
final static short yysindex[] = {                      -256,
 -251,    0, -243, -249,    0, -243,    0,   -9,  -11,    0,
    0,   -5,    0, -168,    0,    0,   10,    0,    0,    0,
    0,    0,    0,  -18,   -1,   19,    0,    0,    0, -215,
    4,    0,  -82,    0,    2,    0,   14,    0,    0,    0,
    0,   10,    0,    7,    0,    0,    0, -168,  -33, -194,
   42,  -20,    0, -168,   12,   17,    0,    0,   39, -168,
    0,    0,    0,    0,    0,    0,  -33,  -33,    0,  -33,
  -33,  -33,  -33,  -37,    0,    0,   77,    0,    0,    0,
    0,    0,    0,   40,    0,   47,   10,    0,    0, -168,
 -168,    0,  -33,    0,   77,   77,   77,   77,   90,  -22,
    0,    0,    0,    0,    0,   35,  -33,  -33,  -33,  -33,
  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,
    0,    0,  -30, -179, -175,   66,    0,  -33,   35,    0,
    0,    0,    0,    0,    0,   90,   90,   48,   48,   48,
  -54,  -54,  -54,   42,    0,    0,    0,    0,   77,   35,
    0,   42,   42,   35,    0,    0,   13,    0,
};
final static short yyrindex[] = {                       112,
  -34,    0,  117,    0,    0,    0,    0, -249,    0,    0,
    0,   34,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -25,    0,    1,    0,    0,    0,
    0,  -57,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   69,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    6,   30,   57,   88,   37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  103,  110,  -10,   98,  152,
  328,  339,  352,    0,    0,    0,    0,    0,  -51,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  119,    0,    0,  115,  -99,    0,  371,    0,    0,    0,
    0,    0,    0,    0,    0,   92,  101,    0,    0,    0,
    0,    0,    0,    0,    0,  125,   22,    0,    0,  -31,
  -13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=499;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         71,
  103,   50,   17,   45,  102,  101,   73,   89,    1,   26,
   61,   72,   90,    9,  117,  118,   26,   26,  127,  115,
  113,   10,  114,   47,  116,   12,   70,   14,   45,  150,
   70,   70,   70,   26,   70,   29,   70,   26,   49,   48,
   91,   89,   29,   29,   30,   53,   86,   15,   70,   52,
  154,   28,   27,   16,  157,  123,  105,   74,   50,   29,
   60,   54,   59,   29,   86,   62,   86,   50,    8,   48,
   87,  120,   29,   45,   54,    8,    8,   85,   93,   85,
  121,   85,   70,   28,   27,  118,  104,  122,   87,   18,
   19,   20,   70,  128,  145,   85,    8,   83,   86,   26,
  146,  119,  117,  118,  147,  158,  148,  115,  113,   55,
  114,    1,  116,  117,  118,   83,    6,   11,  115,  113,
    0,  114,   87,  116,   57,   29,  117,  118,   84,   85,
   13,  115,   21,   58,   71,   31,  116,    0,   71,   71,
   71,  120,   71,   74,   71,   74,   84,   74,    0,   83,
   75,   56,   75,    0,   75,    0,   71,    0,    0,  120,
   31,   74,    0,    0,   87,  151,   63,    0,   75,   31,
  120,  119,   92,  155,  156,   18,   19,   20,   94,    0,
   84,    0,   55,  120,    0,    0,    0,    0,   72,  119,
   71,    0,   72,   72,   72,   74,   72,    0,   72,    0,
  119,    0,   75,    0,    0,   31,    0,    0,  124,  125,
   72,    0,    0,  119,  107,  108,  109,  110,  111,  112,
    0,    0,    0,    0,   18,   19,   20,    0,   64,   65,
    0,   66,   26,   26,   26,    0,    0,   26,   26,   26,
   17,    0,   67,   68,   72,   69,  107,  108,  109,  110,
  111,  112,   26,    0,    0,   26,    0,    0,   29,   29,
   29,    0,   31,   29,   29,   29,    0,   18,   19,   20,
   31,   31,   22,   23,   24,    0,    0,    0,   29,    0,
    0,   29,    0,    0,    0,    0,    0,   25,    0,    0,
   26,    8,    8,    8,    0,    0,    8,    8,    8,   18,
   19,   20,    0,    0,   22,   23,   24,    0,    0,    0,
    0,    8,    0,    0,    8,    0,  107,  108,  109,  110,
  111,  112,   26,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  107,  108,  109,  110,  111,  112,
    0,    0,    0,    0,    0,  107,  108,  109,  110,  111,
  112,    0,    0,    0,    0,    0,    0,    0,  107,  108,
  109,  110,  111,  112,   76,   76,    0,    0,   76,   76,
   76,    0,   76,    0,   76,   77,   77,    0,    0,   77,
   77,   77,    0,   77,    0,   77,   76,    0,   73,   73,
    0,    0,   73,   73,   73,    0,   73,   77,   73,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   73,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   76,   76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   77,   77,    0,    0,    0,    0,   95,   96,    0,
   97,   98,   99,  100,   73,   73,    0,    0,    0,    0,
    0,   76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   77,  126,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   73,    0,  130,  131,  132,
  133,  134,  135,  136,  137,  138,  139,  140,  141,  142,
  143,    0,    0,    0,    0,    0,    0,    0,  149,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   38,   59,   37,   17,   42,   43,   40,   59,  265,   35,
   42,   45,   33,  265,   37,   38,   42,   43,   41,   42,
   43,  265,   45,   42,   47,  275,   37,   37,   42,  129,
   41,   42,   43,   59,   45,   35,   47,   63,   40,   58,
   61,   93,   42,   43,   35,   42,   41,   59,   59,  265,
  150,   42,   43,   59,  154,   87,   94,   91,   40,   59,
   47,   58,   61,   63,   59,   59,  261,  125,   35,   58,
   41,   94,   63,   87,   58,   42,   43,   41,   40,   43,
   41,   45,   93,   42,   43,   38,  124,   41,   59,  258,
  259,  260,  126,   59,  125,   59,   63,   41,   93,  125,
  280,  124,   37,   38,  280,   93,   41,   42,   43,   41,
   45,    0,   47,   37,   38,   59,    0,    3,   42,   43,
   -1,   45,   93,   47,   33,  125,   37,   38,   41,   93,
    6,   42,   14,   33,   37,   17,   47,   -1,   41,   42,
   43,   94,   45,   41,   47,   43,   59,   45,   -1,   93,
   41,   33,   43,   -1,   45,   -1,   59,   -1,   -1,   94,
   42,   59,   -1,   -1,  123,  144,   48,   -1,   59,   51,
   94,  124,   54,  152,  153,  258,  259,  260,   60,   -1,
   93,   -1,  265,   94,   -1,   -1,   -1,   -1,   37,  124,
   93,   -1,   41,   42,   43,   93,   45,   -1,   47,   -1,
  124,   -1,   93,   -1,   -1,   87,   -1,   -1,   90,   91,
   59,   -1,   -1,  124,  269,  270,  271,  272,  273,  274,
   -1,   -1,   -1,   -1,  258,  259,  260,   -1,  262,  263,
   -1,  265,  258,  259,  260,   -1,   -1,  263,  264,  265,
  275,   -1,  276,  277,   93,  279,  269,  270,  271,  272,
  273,  274,  278,   -1,   -1,  281,   -1,   -1,  258,  259,
  260,   -1,  144,  263,  264,  265,   -1,  258,  259,  260,
  152,  153,  263,  264,  265,   -1,   -1,   -1,  278,   -1,
   -1,  281,   -1,   -1,   -1,   -1,   -1,  278,   -1,   -1,
  281,  258,  259,  260,   -1,   -1,  263,  264,  265,  258,
  259,  260,   -1,   -1,  263,  264,  265,   -1,   -1,   -1,
   -1,  278,   -1,   -1,  281,   -1,  269,  270,  271,  272,
  273,  274,  281,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  269,  270,  271,  272,  273,  274,
   -1,   -1,   -1,   -1,   -1,  269,  270,  271,  272,  273,
  274,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  269,  270,
  271,  272,  273,  274,   37,   38,   -1,   -1,   41,   42,
   43,   -1,   45,   -1,   47,   37,   38,   -1,   -1,   41,
   42,   43,   -1,   45,   -1,   47,   59,   -1,   37,   38,
   -1,   -1,   41,   42,   43,   -1,   45,   59,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   59,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   93,   94,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   93,   94,   -1,   -1,   -1,   -1,   67,   68,   -1,
   70,   71,   72,   73,   93,   94,   -1,   -1,   -1,   -1,
   -1,  124,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  124,   93,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  124,   -1,  107,  108,  109,
  110,  111,  112,  113,  114,  115,  116,  117,  118,  119,
  120,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  128,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,"'#'",null,"'%'","'&'",null,"'('","')'","'*'","'+'",
null,"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'",null,"'='",null,"'?'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'","'^'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'","'|'","'}'","'~'",null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"NL","DEC_INTEGER_LITERAL",
"HEX_INTEGER_LITERAL","DUAL_INTEGER_LITERAL","STRING_LITERAL","BOOLEAN_LITERAL",
"LABEL","PACKETREF","IDENTIFIER","LSHIFT","RSHIFT","URSHIFT","CMP_EQ","CMP_NEQ",
"CMP_LT","CMP_LEQ","CMP_GT","CMP_GEQ","ASSIGN","CAST_TO_INT","CAST_TO_BOOLEAN",
"IF","TKDOLLAR","RARROW","TK_MESSAGEID",
};
final static String yyrule[] = {
"$accept : pdldocument",
"pdldocument :",
"pdldocument : pdlbody",
"pdldocument : pdlheader pdlbody",
"pdlheader : IDENTIFIER IDENTIFIER ';'",
"pdlbody : PacketDeclList",
"PacketDeclList : PacketDecl",
"PacketDeclList : PacketDecl PacketDeclList",
"$$1 :",
"PacketDecl : PacketDeclStart ASSIGN $$1 ItemList ';'",
"PacketDecl : PacketDeclStart ASSIGN ';'",
"PacketDeclStart : PacketName '%' INTEGER_LITERAL",
"PacketDeclStart : PacketName",
"Multiplicity : IDENTIFIER '*'",
"Multiplicity : INTEGER_LITERAL '*'",
"Multiplicity : '*'",
"Multiplicity : '+'",
"PacketName : IDENTIFIER",
"SimpleItem : LABEL",
"SimpleItem : PacketRef",
"SimpleItem : Variable",
"SimpleItem : VariableList",
"SimpleItem : ImplicitVariable",
"SimpleItem : Constant",
"SimpleItem : MessageId",
"MessageId : TK_MESSAGEID '(' STRING_LITERAL ')'",
"Variable : RawVariable",
"RawVariable : IDENTIFIER ':' INTEGER_LITERAL",
"ImplicitVariable : RawVariable '=' '(' expression ')'",
"VariableList : RawVariableList",
"VariableList : RawVariableList '/' INTEGER_LITERAL",
"RawVariableList : Multiplicity RawVariable",
"Constant : ConstantWithoutMuliplicity",
"Constant : Multiplicity ConstantWithoutMuliplicity",
"ConstantWithoutMuliplicity : INTEGER_LITERAL ':' INTEGER_LITERAL",
"PacketRef : PACKETREF",
"Item : BlockItem",
"Item : SimpleItem",
"BlockItem : IfStatement",
"$$2 :",
"BlockItem : '?' $$2 Block",
"$$3 :",
"IfStatement : IF '(' IfExpression ')' $$3 Block",
"$$4 :",
"IfStatement : '#' IDENTIFIER '=' INTEGER_LITERAL RARROW $$4 Block",
"$$5 :",
"IfStatement : '#' IDENTIFIER '!' INTEGER_LITERAL RARROW $$5 Block",
"Block : SimpleItem",
"Block : '{' ItemList '}'",
"ItemList : Item ItemList",
"ItemList : Item",
"IfExpression : compiled_expression",
"INTEGER_LITERAL : DEC_INTEGER_LITERAL",
"INTEGER_LITERAL : HEX_INTEGER_LITERAL",
"INTEGER_LITERAL : DUAL_INTEGER_LITERAL",
"compiled_expression : expression",
"expression : '(' expression ')'",
"expression : MultiplicativeExpr",
"expression : AdditiveExpr",
"expression : UnaryExpr",
"expression : ConstExpr",
"expression : StreamOp",
"expression : CompareExpr",
"expression : TypeCastExpr",
"CompareExpr : expression CMP_EQ expression",
"CompareExpr : expression CMP_NEQ expression",
"CompareExpr : expression CMP_LT expression",
"CompareExpr : expression CMP_LEQ expression",
"CompareExpr : expression CMP_GT expression",
"CompareExpr : expression CMP_GEQ expression",
"MultiplicativeExpr : expression '*' expression",
"MultiplicativeExpr : expression '/' expression",
"MultiplicativeExpr : expression '%' expression",
"MultiplicativeExpr : expression '^' expression",
"AdditiveExpr : expression '+' expression",
"AdditiveExpr : expression '-' expression",
"AdditiveExpr : expression '&' expression",
"AdditiveExpr : expression '|' expression",
"ConstExpr : TKDOLLAR",
"ConstExpr : INTEGER_LITERAL",
"ConstExpr : BOOLEAN_LITERAL",
"ConstExpr : LABEL",
"ConstExpr : IDENTIFIER",
"UnaryExpr : '~' expression",
"UnaryExpr : '!' expression",
"UnaryExpr : '-' expression",
"TypeCastExpr : CAST_TO_INT expression",
"TypeCastExpr : CAST_TO_BOOLEAN expression",
"StreamOp : '[' StreamOpToken NextExpression NextExpression NextExpression NextExpression ']'",
"NextExpression : ';' expression",
"StreamOpToken : '&'",
"StreamOpToken : '|'",
"StreamOpToken : '^'",
"StreamOpToken : '*'",
"StreamOpToken : '+'",
};

//#line 292 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
  /* a reference to the lexer object */
  private PDL2Lexer lexer;
  
  private PDLDocumentImpl pdldoc = new PDLDocumentImpl();

  public PDLDocument getDocument()
  {
     return pdldoc;
  }

  private List<PDLBlock> bstack = new ArrayList<PDLBlock>();
  
  private static final boolean PDLDebug = false;
  
  private void bpush(Object b)
  {
    if (PDLDebug) System.out.println("bpush:"+b);
    bstack.add((PDLBlock)b);
  }
  
  private void bpop()
  {
    if (PDLDebug) System.out.println("bpop");
    bstack.remove(bstack.size()-1);
  }
  
  private void badd(Object item) throws PDLException
  {
    if (item == null) throw new PDLException("error in parser: cannot add null to parent block");
  
    if (PDLDebug) System.out.println("badd:"+item);
    bstack.get(bstack.size()-1).add((PDLItem)item);
  }

  private PDLPacketRefImpl textToPacketRef(String s)
  {
    int i = s.indexOf('$');
    return new PDLPacketRefImpl(pdldoc, s.substring(0,i), s.substring(i+1));
  }

  private void registerPacketDecl(PDLPacketDecl p) throws PDLException
  {
     try
     {
        pdldoc.add(p);
     }
     catch (IllegalArgumentException e)
     {
        yyerror("multiple declarations of packet: "+p.getName());
        throw e; // in case no exception is thrown
     }
  }

  public void parse() throws PDLException
  {
     yyparse();
     PDLDocumentVerifier verifier = new PDLDocumentVerifier(pdldoc);
     verifier.verify();
  }

  /* interface to the lexer */
  private int yylex () {
    int yyl_return = -1;
    try {
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }
	

  /* error reporting */
  public void yyerror (String error) throws PDLException
  {
    throw new PDLException(
      "[line "+ lexer.getLineNumber()+":"+lexer.getColumnNumber()+"] "+error);
  }

  /* lexer is created in the constructor */
  public PDL2Parser(Reader r) {
    lexer = new PDL2Lexer(r, this);
  }

  /* that's how you use the parser */
  public static void main(String args[]) throws IOException, PDLException {
    PDL2Parser yyparser = new PDL2Parser(new FileReader(args[0]));
    yyparser.yyparse();    
  }
//#line 535 "PDL2Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
throws PDLException
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 60 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 2:
//#line 61 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 3:
//#line 62 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 4:
//#line 66 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ if (!val_peek(2).sval.equals("start")) throw new PDLException("'start' expected"); pdldoc.setStartPacketName(val_peek(1).sval); }
break;
case 5:
//#line 70 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 8:
//#line 81 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:before ItemList */
                                           PDLPacketDeclImpl pdecl = (PDLPacketDeclImpl)val_peek(1).obj;
                                           bpush(pdecl);
                                           yyval.obj = pdecl;
                                           registerPacketDecl(pdecl); 
                                         }
break;
case 9:
//#line 90 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:after ItemList */
                                           bpop();
                                         }
break;
case 10:
//#line 94 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ registerPacketDecl((PDLPacketDeclImpl)val_peek(2).obj);  }
break;
case 11:
//#line 98 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ int padding = val_peek(0).ival;
                                           if(padding<1)
                                             yyerror("padding must >=1: "+padding);
                                              
                                           yyval.obj = new PDLPacketDeclImpl(val_peek(2).sval, padding); }
break;
case 12:
//#line 103 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLPacketDeclImpl(val_peek(0).sval, 1); /* default padding = 1 */ }
break;
case 13:
//#line 107 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).sval); }
break;
case 14:
//#line 108 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).ival); }
break;
case 15:
//#line 109 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("*-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.NoneOrMany); */
                                     }
break;
case 16:
//#line 112 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("+-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.OneOrMany); */
                                     }
break;
case 18:
//#line 122 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLLabelImpl(val_peek(0).sval)); }
break;
case 25:
//#line 132 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLMessageIdImpl(val_peek(1).sval)); }
break;
case 26:
//#line 136 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 27:
//#line 140 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBitcount(val_peek(0).ival);
                                           yyval.obj=new PDLVariableImpl(val_peek(2).sval, val_peek(0).ival); }
break;
case 28:
//#line 145 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLImplicitVariableImpl((PDLVariable)val_peek(4).obj, new PDLFunctionImpl((Expression)val_peek(1).obj)));  }
break;
case 29:
//#line 149 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 30:
//#line 150 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLVariableListImpl v = (PDLVariableListImpl) val_peek(2).obj; v.setTerminal(val_peek(0).ival); badd(val_peek(2).obj); }
break;
case 31:
//#line 154 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=new PDLVariableListImpl((PDLVariable)val_peek(0).obj, (PDLMultiplicity)val_peek(1).obj); }
break;
case 32:
//#line 158 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  badd(val_peek(0).obj); }
break;
case 33:
//#line 159 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ ((PDLConstantImpl)val_peek(0).obj).setMultiplicity((PDLMultiplicityImpl)val_peek(1).obj); badd(val_peek(0).obj); }
break;
case 34:
//#line 163 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBounds(val_peek(2).ival, val_peek(0).ival);
                                                yyval.obj = new PDLConstantImpl(val_peek(2).ival, val_peek(0).ival); }
break;
case 35:
//#line 168 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(textToPacketRef(val_peek(0).sval)); }
break;
case 38:
//#line 177 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 39:
//#line 178 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLOptionalImpl o = new PDLOptionalImpl(); badd(o); bpush(o); }
break;
case 40:
//#line 178 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 41:
//#line 182 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl((PDLCondition) val_peek(1).obj); badd(c); bpush(c); yyval.obj = c;  }
break;
case 42:
//#line 183 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 43:
//#line 185 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl(new PDLConditionImpl(val_peek(3).sval, val_peek(1).ival, false)); badd(c); bpush(c); yyval.obj = c;  }
break;
case 44:
//#line 186 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 45:
//#line 188 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl(new PDLConditionImpl(val_peek(3).sval, val_peek(1).ival, true)); badd(c); bpush(c); yyval.obj = c;  }
break;
case 46:
//#line 189 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 47:
//#line 192 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ }
break;
case 48:
//#line 193 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ }
break;
case 51:
//#line 202 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLCompiledCondition((Expression)val_peek(0).obj); }
break;
case 55:
//#line 212 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(0).obj; }
break;
case 56:
//#line 216 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(1).obj; }
break;
case 64:
//#line 227 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 65:
//#line 228 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpNeq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 66:
//#line 229 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpLt( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 67:
//#line 230 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpLEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 68:
//#line 231 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpGt( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 69:
//#line 232 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpGEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 70:
//#line 236 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.mul( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 71:
//#line 237 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.div( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 72:
//#line 238 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.mod( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 73:
//#line 239 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.xor( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 74:
//#line 243 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  yyval.obj = Opcode.add( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 75:
//#line 244 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.minus( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 76:
//#line 245 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.and( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 77:
//#line 246 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.or ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 78:
//#line 250 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.lpush); }
break;
case 79:
//#line 251 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.ipush, val_peek(0).ival ); }
break;
case 80:
//#line 252 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.bpush, val_peek(0).ival==1?true:false); }
break;
case 81:
//#line 253 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.lpush, (String)val_peek(0).sval); }
break;
case 82:
//#line 254 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.vpush, (String)val_peek(0).sval); }
break;
case 83:
//#line 258 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.inv( (Expression) val_peek(0).obj ); }
break;
case 84:
//#line 259 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.inv( (Expression) val_peek(0).obj ); }
break;
case 85:
//#line 260 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.neg( (Expression) val_peek(0).obj ); }
break;
case 86:
//#line 264 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.castToInt((Expression) val_peek(0).obj); }
break;
case 87:
//#line 265 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.castToBoolean((Expression) val_peek(0).obj); }
break;
case 88:
//#line 274 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{   yyval.obj = new Expression(val_peek(5).ival, 
                                           (Expression)val_peek(4).obj, (Expression)val_peek(3).obj, 
                                           (Expression)val_peek(2).obj, (Expression)val_peek(1).obj); }
break;
case 89:
//#line 280 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 90:
//#line 284 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.land; }
break;
case 91:
//#line 285 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lor; }
break;
case 92:
//#line 286 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lxor; }
break;
case 93:
//#line 287 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lmul; }
break;
case 94:
//#line 288 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.ladd; }
break;
//#line 986 "PDL2Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public PDL2Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public PDL2Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
