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
public final static short TK_FAIL=282;
public final static short TK_SWITCH=283;
public final static short TK_CASE=284;
public final static short TK_DEFAULT=285;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,   26,   27,    4,    4,   44,    2,    2,
    3,    3,   15,   15,   15,   15,   42,   32,   32,   32,
   32,   32,   32,   32,   32,   32,   32,   32,   37,   38,
   39,   40,   40,   45,   41,   46,   41,   36,   47,   35,
   35,   34,   18,   17,   21,   20,   20,   19,   14,   14,
   16,   24,   25,   29,   29,   30,   48,   30,   49,   22,
   50,   22,   51,   22,   28,   52,   28,   31,   31,   23,
    1,    1,    1,   13,    7,    7,    7,    7,    7,    7,
    7,    7,    8,    8,    8,    8,    8,    8,    9,    9,
    9,    9,   10,   10,   10,   10,   11,   11,   11,   11,
   11,   12,   12,   12,   33,   33,    6,    5,   43,   43,
   43,   43,   43,
};
final static short yylen[] = {                            2,
    0,    1,    2,    3,    1,    1,    2,    0,    5,    3,
    3,    1,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    4,    1,    2,    0,    5,    0,    4,    3,    0,    4,
    3,    4,    1,    3,    5,    1,    3,    2,    1,    2,
    3,    1,    2,    1,    1,    1,    0,    3,    0,    6,
    0,    7,    0,    7,    1,    0,    4,    2,    1,    1,
    1,    1,    1,    1,    3,    1,    1,    1,    1,    1,
    1,    1,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    1,    1,    1,    1,
    1,    2,    2,    2,    2,    2,    7,    2,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    5,    0,    2,    0,    0,   17,
    7,    0,    3,    0,    4,   10,    0,   71,   72,   73,
   11,   18,   52,    0,    0,    0,   29,    0,   16,   15,
   39,   57,    0,    0,   24,    0,   49,    0,   21,    0,
   22,   23,   56,   19,   20,    0,   54,    0,   55,   25,
   26,   27,   28,    0,   13,    0,    0,    0,    0,    0,
    0,    0,    0,   14,    0,   53,    0,    0,   50,   48,
    0,    0,   68,    9,    0,   44,   99,  100,  101,    0,
    0,   97,    0,    0,    0,    0,    0,   98,   80,    0,
   81,   76,   77,   79,   78,   70,    0,   82,    0,    0,
    0,   38,    0,   66,   58,   65,    0,    0,   51,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  113,  112,  109,  110,  111,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   59,   42,   31,   41,    0,    0,    0,    0,    0,
    0,   36,   30,   33,   75,    0,    0,   83,   84,   85,
   86,   87,   88,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   40,    0,   63,   61,   45,   34,    0,    0,
    0,   60,   67,    0,    0,    0,   37,    0,   64,   62,
   35,    0,  107,
};
final static short yydgoto[] = {                          2,
   88,    3,    4,    5,  157,   89,   90,   91,   92,   93,
   94,   95,   96,   35,   36,   37,   38,   39,   40,   41,
   42,   43,   97,   44,   45,    6,    7,  105,   46,   47,
   48,   49,   98,   50,   60,   51,   52,   53,   54,  114,
  115,    8,  127,   17,  186,  179,   61,   62,  172,  185,
  184,  147,
};
final static short yysindex[] = {                      -244,
 -238,    0, -236, -239,    0, -236,    0,   -6,  -18,    0,
    0,  -15,    0,  -69,    0,    0,    3,    0,    0,    0,
    0,    0,    0,  -23,   19,   25,    0,   32,    0,    0,
    0,    0, -192,   -8,    0, -117,    0,   24,    0,   41,
    0,    0,    0,    0,    0,    3,    0,   34,    0,    0,
    0,    0,    0,  -27,    0,  -69,   38, -164,   38,  -30,
    3,   60,  -13,    0,  -69,    0,   67,   74,    0,    0,
   99,  -69,    0,    0, -223,    0,    0,    0,    0,   38,
   38,    0,   38,   38,   38,   38,  -20,    0,    0,  107,
    0,    0,    0,    0,    0,    0,  105,    0,  112,   39,
    3,    0,   -9,    0,    0,    0,  -69,  -69,    0,   38,
    0,  -69,  101,   36, -223,  107,  107,  107,  107,  118,
   75,    0,    0,    0,    0,    0,  120,   38,   38,   38,
   38,   38,   38,   38,   38,   38,   38,   38,   38,   38,
   38,    0,    0,    0,    0,    3,    3, -100,  -98,   93,
  127,    0,    0,    0,    0,   38,  120,    0,    0,    0,
    0,    0,    0,  118,  118,   57,   57,   57,  -96,  -96,
  -96,   60,    0,   61,    0,    0,    0,    0,   60,  107,
  120,    0,    0,   60,   60,   60,    0,  120,    0,    0,
    0,  102,    0,
};
final static short yyrindex[] = {                       197,
  -34,    0,  200,    0,    0,    0,    0, -239,    0,    0,
    0,   12,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -35,    0,  -26,
    0,    0,    0,    0,    0,  -46,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  161,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   79,  -29,   -2,   -1,   78,    8,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,   65,  125,  151,  487,  388,  448,
  459,    0,    0,    0,    0,    0,    0,    0,    0,  -33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  337,    0,    0,  202, -156,    0,  336,    0,    0,    0,
    0,    0,    0,    0,    0,  167,  170,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  201, -116,  -59,    0,
  -36,  -58,    0,    0,    0,    0,    0,    0,    0,   94,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=583;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
  181,  103,   17,  106,   43,   43,   43,   43,   46,   73,
  102,  105,   69,   46,   46,   46,   46,  124,   55,  107,
    1,  123,  122,   43,  188,  108,    9,   43,   10,  105,
   14,  192,   46,   64,   56,   12,   46,   33,  106,  102,
   15,  145,   31,   16,   30,   29,    8,  108,  104,   65,
  104,    8,  104,    8,    8,  182,  106,  102,   57,  108,
  112,  113,  187,  105,   58,   32,  104,  189,  190,  191,
   84,   59,   63,  126,    8,  138,  139,   86,   69,  144,
  136,  134,   85,  135,   71,  137,  173,   72,   43,   43,
  106,  102,   74,  101,  139,   75,   99,   46,   46,   31,
  104,   30,   29,  125,   93,   94,   93,   94,   93,   94,
  174,  138,  139,  106,  146,  155,  136,  134,  103,  135,
  106,  137,   93,   94,   56,  106,  106,  106,   87,  138,
  139,   65,  141,  177,  136,  134,  103,  135,  110,  137,
   18,   19,   20,  138,  139,  142,   66,   67,  136,  134,
  141,  135,  143,  137,  138,  139,   93,   94,  152,  136,
  153,   89,  140,   83,  137,   89,   89,   89,  141,   89,
  103,   89,  128,  129,  130,  131,  132,  133,  156,  175,
  140,  176,  104,   89,  178,  183,  141,   90,   18,   19,
   20,   90,   90,   90,  193,   90,    1,   90,  140,    6,
  141,   74,   69,   32,   11,   70,   13,    0,  154,   90,
    0,  141,    0,    0,    0,    0,  140,   89,    0,    0,
    0,    0,   43,   43,   43,    0,    0,   43,   43,   43,
  140,   46,   46,   46,    0,    0,   46,   46,   46,    0,
   17,  140,   43,   90,    0,   43,   43,   43,   43,   43,
    0,   46,    0,    0,   46,   46,   46,   46,   46,    0,
   18,   19,   20,    0,    0,   22,   23,   24,    0,    8,
    8,    8,    0,    0,    8,    8,    8,    0,    0,    0,
   25,    0,    0,   26,   27,   28,    0,    0,    0,    8,
    0,    0,    8,    8,    8,   18,   19,   20,    0,   77,
   78,    0,   79,    0,    0,    0,    0,  128,  129,  130,
  131,  132,  133,   80,   81,    0,   82,   18,   19,   20,
    0,    0,   22,   23,   24,  128,  129,  130,  131,  132,
  133,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   26,   27,   28,  128,  129,  130,  131,  132,  133,    0,
   21,    0,    0,   34,    0,    0,    0,    0,    0,    0,
    0,  128,  129,  130,  131,  132,  133,    0,    0,    0,
    0,    0,   68,    0,    0,  128,  129,  130,  131,  132,
  133,    0,   34,    0,    0,    0,  128,  129,  130,  131,
  132,  133,   76,    0,  100,    0,    0,   34,   34,    0,
    0,  109,    0,    0,    0,    0,    0,    0,  111,    0,
    0,    0,    0,    0,    0,  116,  117,    0,  118,  119,
  120,  121,    0,    0,   95,   95,    0,    0,   95,   95,
   95,    0,   95,    0,   95,    0,    0,   34,    0,    0,
    0,    0,    0,  148,  149,  150,   95,    0,  151,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  158,  159,  160,  161,  162,  163,  164,
  165,  166,  167,  168,  169,  170,  171,    0,    0,    0,
   95,   95,   34,   34,   96,   96,    0,    0,   96,   96,
   96,  180,   96,    0,   96,   92,   92,    0,    0,   92,
   92,   92,    0,   92,    0,   92,   96,    0,   34,    0,
    0,   95,    0,    0,    0,   34,    0,   92,    0,    0,
   34,   34,   34,   91,    0,    0,    0,   91,   91,   91,
    0,   91,    0,   91,    0,    0,    0,    0,    0,    0,
   96,   96,    0,    0,    0,   91,    0,    0,    0,    0,
    0,   92,   92,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   96,    0,    0,    0,    0,    0,    0,    0,   91,
    0,    0,   92,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         35,
  157,   61,   37,   62,   40,   41,   42,   43,   35,   46,
   41,   41,   59,   40,   41,   42,   43,   38,   42,   33,
  265,   42,   43,   59,  181,   59,  265,   63,  265,   59,
   37,  188,   59,   42,   58,  275,   63,   35,   41,   41,
   59,  101,   40,   59,   42,   43,   35,   61,   41,   58,
   43,   40,   45,   42,   43,  172,   59,   59,   40,   93,
  284,  285,  179,   93,   40,   63,   59,  184,  185,  186,
   33,   40,  265,   94,   63,   37,   38,   40,  125,   41,
   42,   43,   45,   45,   61,   47,  146,   47,  124,  125,
   93,   93,   59,  124,   38,  123,  261,  124,  125,   40,
   93,   42,   43,  124,   41,   41,   43,   43,   45,   45,
  147,   37,   38,  172,  124,   41,   42,   43,   41,   45,
  179,   47,   59,   59,   58,  184,  185,  186,   91,   37,
   38,   58,   94,   41,   42,   43,   59,   45,   40,   47,
  258,  259,  260,   37,   38,   41,  264,  265,   42,   43,
   94,   45,   41,   47,   37,   38,   93,   93,   58,   42,
  125,   37,  124,  126,   47,   41,   42,   43,   94,   45,
   93,   47,  269,  270,  271,  272,  273,  274,   59,  280,
  124,  280,  123,   59,   58,  125,   94,   37,  258,  259,
  260,   41,   42,   43,   93,   45,    0,   47,  124,    0,
   94,   41,   36,  125,    3,   36,    6,   -1,  115,   59,
   -1,   94,   -1,   -1,   -1,   -1,  124,   93,   -1,   -1,
   -1,   -1,  258,  259,  260,   -1,   -1,  263,  264,  265,
  124,  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,
  275,  124,  278,   93,   -1,  281,  282,  283,  284,  285,
   -1,  278,   -1,   -1,  281,  282,  283,  284,  285,   -1,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,  258,
  259,  260,   -1,   -1,  263,  264,  265,   -1,   -1,   -1,
  278,   -1,   -1,  281,  282,  283,   -1,   -1,   -1,  278,
   -1,   -1,  281,  282,  283,  258,  259,  260,   -1,  262,
  263,   -1,  265,   -1,   -1,   -1,   -1,  269,  270,  271,
  272,  273,  274,  276,  277,   -1,  279,  258,  259,  260,
   -1,   -1,  263,  264,  265,  269,  270,  271,  272,  273,
  274,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  281,  282,  283,  269,  270,  271,  272,  273,  274,   -1,
   14,   -1,   -1,   17,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  269,  270,  271,  272,  273,  274,   -1,   -1,   -1,
   -1,   -1,   36,   -1,   -1,  269,  270,  271,  272,  273,
  274,   -1,   46,   -1,   -1,   -1,  269,  270,  271,  272,
  273,  274,   56,   -1,   59,   -1,   -1,   61,   62,   -1,
   -1,   65,   -1,   -1,   -1,   -1,   -1,   -1,   72,   -1,
   -1,   -1,   -1,   -1,   -1,   80,   81,   -1,   83,   84,
   85,   86,   -1,   -1,   37,   38,   -1,   -1,   41,   42,
   43,   -1,   45,   -1,   47,   -1,   -1,  101,   -1,   -1,
   -1,   -1,   -1,  107,  108,  110,   59,   -1,  112,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  128,  129,  130,  131,  132,  133,  134,
  135,  136,  137,  138,  139,  140,  141,   -1,   -1,   -1,
   93,   94,  146,  147,   37,   38,   -1,   -1,   41,   42,
   43,  156,   45,   -1,   47,   37,   38,   -1,   -1,   41,
   42,   43,   -1,   45,   -1,   47,   59,   -1,  172,   -1,
   -1,  124,   -1,   -1,   -1,  179,   -1,   59,   -1,   -1,
  184,  185,  186,   37,   -1,   -1,   -1,   41,   42,   43,
   -1,   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,
   93,   94,   -1,   -1,   -1,   59,   -1,   -1,   -1,   -1,
   -1,   93,   94,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  124,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   93,
   -1,   -1,  124,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=285;
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
"IF","TKDOLLAR","RARROW","TK_MESSAGEID","TK_FAIL","TK_SWITCH","TK_CASE",
"TK_DEFAULT",
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
"SimpleItem : PacketRefList",
"SimpleItem : Variable",
"SimpleItem : VariableList",
"SimpleItem : ImplicitVariable",
"SimpleItem : Constant",
"SimpleItem : MessageId",
"SimpleItem : MutualExclusionStatement",
"SimpleItem : FailStatement",
"SimpleItem : SwitchStatement",
"FailStatement : TK_FAIL",
"SwitchStatement : SwitchStatementHeader '{' SwitchCaseList '}'",
"SwitchStatementHeader : TK_SWITCH '(' expression ')'",
"SwitchCaseList : CaseStatement",
"SwitchCaseList : CaseStatement SwitchCaseList",
"$$2 :",
"CaseStatement : TK_CASE INTEGER_LITERAL ':' $$2 Block",
"$$3 :",
"CaseStatement : TK_DEFAULT ':' $$3 Block",
"MutualExclusionStatement : '(' MutualExclusion ')'",
"$$4 :",
"MutualExclusion : $$4 Item '|' Item",
"MutualExclusion : MutualExclusion '|' Item",
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
"PacketRefList : Multiplicity PACKETREF",
"Item : BlockItem",
"Item : SimpleItem",
"BlockItem : IfStatement",
"$$5 :",
"BlockItem : '?' $$5 Block",
"$$6 :",
"IfStatement : IF '(' IfExpression ')' $$6 Block",
"$$7 :",
"IfStatement : '#' IDENTIFIER '=' INTEGER_LITERAL RARROW $$7 Block",
"$$8 :",
"IfStatement : '#' IDENTIFIER '!' INTEGER_LITERAL RARROW $$8 Block",
"Block : SimpleItem",
"$$9 :",
"Block : '{' $$9 ItemList '}'",
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

//#line 336 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
  /* a reference to the lexer object */
  private PDL2Lexer lexer;
  
  private PDLDocumentImpl pdldoc = new PDLDocumentImpl();

  public PDLDocument getDocument()
  {
     return pdldoc;
  }

  // PDLBlock and mutualExclusion elements
  private List<Object> bstack = new ArrayList<Object>();
  
  private static final boolean PDLDebug = false;
  
  private void bpush(Object b)
  {
    if (PDLDebug) System.out.println("bpush:"+b);
    
    if ((b instanceof PDLBlock) || (b instanceof PDLMutualExclusionImpl)
     || (b instanceof PDLSwitchStatementImpl)
     || (b instanceof PDLCaseStatementImpl))
        bstack.add(b);
    else
        throw new ClassCastException();
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
    Object p = bstack.get(bstack.size()-1);
    
    if (p instanceof PDLBlock)
    	((PDLBlock)p).add((PDLItem)item);
    else if (p instanceof PDLMutualExclusionImpl)
        ((PDLMutualExclusionImpl)p).add((PDLItem)item);
    else if (p instanceof PDLCaseStatementImpl)
        ((PDLCaseStatementImpl)p).add((PDLItem)item);
    else if (p instanceof PDLSwitchStatementImpl)
        ((PDLSwitchStatementImpl)p).add((PDLCaseStatementImpl)item);
    else throw new ClassCastException("could not determine type of"+item.getClass());
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
//#line 614 "PDL2Parser.java"
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
//#line 65 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 2:
//#line 66 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 3:
//#line 67 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 4:
//#line 71 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ if (!val_peek(2).sval.equals("start")) throw new PDLException("'start' expected"); pdldoc.setStartPacketName(val_peek(1).sval); }
break;
case 5:
//#line 75 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 8:
//#line 86 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:before ItemList */
                                           PDLPacketDeclImpl pdecl = (PDLPacketDeclImpl)val_peek(1).obj;
                                           bpush(pdecl);
                                           yyval.obj = pdecl;
                                           registerPacketDecl(pdecl); 
                                         }
break;
case 9:
//#line 95 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:after ItemList */
                                           bpop();
                                         }
break;
case 10:
//#line 99 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ registerPacketDecl((PDLPacketDeclImpl)val_peek(2).obj);  }
break;
case 11:
//#line 103 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ int padding = val_peek(0).ival;
                                           if(padding<1)
                                             yyerror("padding must >=1: "+padding);
                                              
                                           yyval.obj = new PDLPacketDeclImpl(val_peek(2).sval, padding); }
break;
case 12:
//#line 108 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLPacketDeclImpl(val_peek(0).sval, 1); /* default padding = 1 */ }
break;
case 13:
//#line 112 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).sval); }
break;
case 14:
//#line 113 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).ival); }
break;
case 15:
//#line 114 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("*-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.NoneOrMany); */
                                     }
break;
case 16:
//#line 117 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("+-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.OneOrMany); */
                                     }
break;
case 18:
//#line 127 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLLabelImpl(val_peek(0).sval)); }
break;
case 29:
//#line 141 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.Fail)); }
break;
case 30:
//#line 145 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 31:
//#line 149 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLSwitchStatementImpl s = new PDLSwitchStatementImpl((Expression)val_peek(1).obj); badd(s); bpush(s); }
break;
case 34:
//#line 158 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(false, val_peek(1).ival); badd(c); bpush(c); }
break;
case 35:
//#line 158 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 36:
//#line 159 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(true, -1);  badd(c); bpush(c); }
break;
case 37:
//#line 159 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 38:
//#line 163 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; }
break;
case 39:
//#line 167 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLMutualExclusionImpl m = new PDLMutualExclusionImpl(); badd(m); bpush(m); }
break;
case 40:
//#line 167 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 41:
//#line 168 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(2).obj; }
break;
case 42:
//#line 172 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.MessageId, val_peek(1).sval)); }
break;
case 43:
//#line 176 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 44:
//#line 180 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBitcount(val_peek(0).ival);
                                           yyval.obj=new PDLVariableImpl(val_peek(2).sval, val_peek(0).ival); }
break;
case 45:
//#line 185 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLImplicitVariableImpl((PDLVariable)val_peek(4).obj, new PDLFunctionImpl((Expression)val_peek(1).obj)));  }
break;
case 46:
//#line 189 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 47:
//#line 190 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLVariableListImpl v = (PDLVariableListImpl) val_peek(2).obj; v.setTerminal(val_peek(0).ival); badd(val_peek(2).obj); }
break;
case 48:
//#line 194 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=new PDLVariableListImpl((PDLVariable)val_peek(0).obj, (PDLMultiplicity)val_peek(1).obj); }
break;
case 49:
//#line 198 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  badd(val_peek(0).obj); }
break;
case 50:
//#line 199 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ ((PDLConstantImpl)val_peek(0).obj).setMultiplicity((PDLMultiplicityImpl)val_peek(1).obj); badd(val_peek(0).obj); }
break;
case 51:
//#line 203 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBounds(val_peek(2).ival, val_peek(0).ival);
                                                yyval.obj = new PDLConstantImpl(val_peek(2).ival, val_peek(0).ival); }
break;
case 52:
//#line 208 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(textToPacketRef(val_peek(0).sval)); }
break;
case 53:
//#line 212 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLPacketRefListImpl(textToPacketRef(val_peek(0).sval), (PDLMultiplicity)val_peek(1).obj)); }
break;
case 56:
//#line 221 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 57:
//#line 222 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLOptionalImpl o = new PDLOptionalImpl(); badd(o); bpush(o); }
break;
case 58:
//#line 222 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 59:
//#line 226 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl((PDLCondition) val_peek(1).obj); badd(c); bpush(c); yyval.obj = c;  }
break;
case 60:
//#line 227 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 61:
//#line 229 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl(new PDLConditionImpl(val_peek(3).sval, val_peek(1).ival, false)); badd(c); bpush(c); yyval.obj = c;  }
break;
case 62:
//#line 230 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 63:
//#line 232 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl(new PDLConditionImpl(val_peek(3).sval, val_peek(1).ival, true)); badd(c); bpush(c); yyval.obj = c;  }
break;
case 64:
//#line 233 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 65:
//#line 236 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ }
break;
case 66:
//#line 237 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLBlockItemImpl b = new PDLBlockItemImpl(); badd(b); bpush(b); }
break;
case 67:
//#line 237 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 70:
//#line 246 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLCompiledCondition((Expression)val_peek(0).obj); }
break;
case 74:
//#line 256 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(0).obj; }
break;
case 75:
//#line 260 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(1).obj; }
break;
case 83:
//#line 271 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 84:
//#line 272 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpNeq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 85:
//#line 273 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpLt( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 86:
//#line 274 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpLEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 87:
//#line 275 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpGt( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 88:
//#line 276 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.cmpGEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 89:
//#line 280 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.mul( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 90:
//#line 281 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.div( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 91:
//#line 282 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.mod( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 92:
//#line 283 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.xor( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 93:
//#line 287 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  yyval.obj = Opcode.add( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 94:
//#line 288 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.minus( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 95:
//#line 289 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.and( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 96:
//#line 290 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.or ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 97:
//#line 294 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.fpush); }
break;
case 98:
//#line 295 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.ipush, val_peek(0).ival ); }
break;
case 99:
//#line 296 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.bpush, val_peek(0).ival==1?true:false); }
break;
case 100:
//#line 297 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.lpush, (String)val_peek(0).sval); }
break;
case 101:
//#line 298 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.vpush, (String)val_peek(0).sval); }
break;
case 102:
//#line 302 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.inv( (Expression) val_peek(0).obj ); }
break;
case 103:
//#line 303 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.inv( (Expression) val_peek(0).obj ); }
break;
case 104:
//#line 304 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.neg( (Expression) val_peek(0).obj ); }
break;
case 105:
//#line 308 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.castToInt((Expression) val_peek(0).obj); }
break;
case 106:
//#line 309 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Opcode.castToBoolean((Expression) val_peek(0).obj); }
break;
case 107:
//#line 318 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{   yyval.obj = new Expression(val_peek(5).ival, 
                                           (Expression)val_peek(4).obj, (Expression)val_peek(3).obj, 
                                           (Expression)val_peek(2).obj, (Expression)val_peek(1).obj); }
break;
case 108:
//#line 324 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 109:
//#line 328 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.land; }
break;
case 110:
//#line 329 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lor; }
break;
case 111:
//#line 330 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lxor; }
break;
case 112:
//#line 331 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lmul; }
break;
case 113:
//#line 332 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.ladd; }
break;
//#line 1117 "PDL2Parser.java"
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
