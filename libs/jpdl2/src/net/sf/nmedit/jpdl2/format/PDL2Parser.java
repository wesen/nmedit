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
public final static short TK_MESSAGEID=280;
public final static short TK_FAIL=281;
public final static short TK_SWITCH=282;
public final static short TK_CASE=283;
public final static short TK_DEFAULT=284;
public final static short NEG=285;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,   22,   23,    4,    4,   40,    2,    2,
    3,    3,   11,   11,   11,   11,   38,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   28,   33,   34,
   35,   36,   36,   41,   37,   42,   37,   32,   43,   31,
   31,   30,   14,   13,   17,   16,   16,   15,   10,   10,
   12,   20,   21,   25,   25,   26,   44,   26,   45,   18,
   24,   24,   46,   24,   27,   27,   19,    1,    1,    1,
    9,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    8,    8,    8,    8,    8,   29,   29,    6,
    5,   39,   39,   39,   39,   39,
};
final static short yylen[] = {                            2,
    0,    1,    2,    3,    1,    1,    2,    0,    5,    3,
    3,    1,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    4,    1,    2,    0,    5,    0,    4,    3,    0,    4,
    3,    4,    1,    3,    5,    1,    3,    2,    1,    2,
    3,    1,    2,    1,    1,    1,    0,    3,    0,    6,
    1,    2,    0,    4,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    2,    2,
    2,    3,    1,    1,    1,    1,    1,    2,    2,    7,
    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    5,    0,    2,    0,    0,   17,
    7,    0,    3,    0,    4,   10,    0,   68,   69,   70,
   11,   18,   52,    0,    0,    0,   29,    0,   16,   15,
   39,   57,    0,   24,    0,   49,    0,   21,    0,   22,
   23,   56,   19,   20,    0,   54,    0,   55,   25,   26,
   27,   28,    0,   13,    0,    0,    0,    0,    0,    0,
    0,   14,    0,   53,    0,    0,   50,   48,    0,    0,
   65,    9,    0,   44,   95,   96,   97,    0,    0,   93,
    0,    0,    0,    0,    0,   94,   73,    0,   72,   67,
    0,   74,    0,    0,    0,   38,    0,    0,   58,   61,
   51,    0,   47,    0,    0,    0,    0,   98,   99,   91,
   89,   90,    0,  102,  103,  104,  106,  105,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   59,   42,   31,   41,    0,   62,    0,
    0,    0,   36,   30,   33,   92,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   85,
   86,   87,    0,   40,    0,   45,   34,    0,    0,    0,
   60,   64,    0,   37,    0,   35,    0,  100,
};
final static short yydgoto[] = {                          2,
   86,    3,    4,    5,  148,   87,   88,   89,   90,   34,
   35,   36,   37,   38,   39,   40,   41,   42,   91,   43,
   44,    6,    7,   99,   45,   46,   47,   48,   92,   49,
   59,   50,   51,   52,   53,  106,  107,    8,  119,   17,
  173,  168,   60,   61,  163,  140,
};
final static short yysindex[] = {                      -237,
 -225,    0, -219, -228,    0, -219,    0,   12,   -8,    0,
    0,    1,    0, -203,    0,    0,   10,    0,    0,    0,
    0,    0,    0,  -21,   19,   23,    0,   26,    0,    0,
    0,    0,  -19,    0, -170,    0,    6,    0,   21,    0,
    0,    0,    0,    0,   10,    0,   17,    0,    0,    0,
    0,    0,  -43,    0, -203,  -33, -182,  -33,  -37,   10,
   68,    0, -203,    0,   39,   44,    0,    0,   41, -203,
    0,    0, -275,    0,    0,    0,    0,  -33,  -33,    0,
  -33,  -33,  -33,  -33,  -32,    0,    0,  123,    0,    0,
   60,    0,   62,   87,   10,    0,  -40,  -20,    0,    0,
    0,  -33,    0, -203,   49,  -10, -275,    0,    0,    0,
    0,    0,   98,    0,    0,    0,    0,    0,   54,  -33,
  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,  -33,
  -33,  -33,  -33,    0,    0,    0,    0,   10,    0,   10,
  112,   58,    0,    0,    0,    0,  -33,   54,  -12,  -12,
  -12,  -12,  -12,  -12,   27,   27,   27,  -18,  -18,    0,
    0,    0,   68,    0,   -7,    0,    0,   68,  123,   54,
    0,    0,   68,    0,   54,    0,   24,    0,
};
final static short yyrindex[] = {                       119,
  -34,    0,  120,    0,    0,    0,    0, -228,    0,    0,
    0,   35,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -25,    0,    2,    0,
    0,    0,    0,    0,  -54,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   80,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   43,    0,    0,
    0,    0,    0,    0,    0,    0,   -3,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  138,  144,
  183,  204,  237,  270,  281,  306,  339,  155,  296,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -39,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  424,    0,    0,  128, -134,    0,  523,    0,    0,    0,
    0,   88,  102,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  127, -141,  -47,    0,  -44,  -59,    0,    0,
    0,    0,    0,    0,    0,   31,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=670;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         83,
   71,  100,   17,   96,   66,  114,   84,  104,  105,  118,
  117,   81,   97,  170,   43,   43,   43,   43,  133,  101,
   54,  171,   62,  131,  133,  126,  174,    1,  132,  131,
  129,  176,  130,   43,  132,  175,   55,   43,   63,    9,
  177,   46,   46,   46,   46,   10,   12,  137,   14,   31,
   15,   30,   29,  101,   18,   19,   20,   85,   56,   16,
   46,  116,   57,  133,   46,   58,   69,   70,  131,  129,
   66,  130,   32,  132,    8,   72,    8,    8,   93,   73,
  102,  128,   63,  138,   63,   63,   95,   18,   19,   20,
  164,  115,   82,   64,   65,  165,   55,    8,   43,   43,
  134,   63,  135,  100,  139,   63,  143,   31,  100,   30,
   29,  127,  147,  100,  144,  167,  178,  172,    1,    6,
   71,   32,   67,  133,  126,   46,   46,  136,  131,  129,
   11,  130,   13,  132,  133,  126,   68,  145,  146,  131,
  129,    0,  130,    0,  132,    0,    0,    0,  133,  126,
    0,    0,  166,  131,  129,    0,  130,    0,  132,  133,
  126,    0,    0,    0,  131,  129,    0,  130,    0,  132,
    0,    0,    0,    0,    0,    0,    0,    0,   75,    0,
  128,    0,    0,    0,   76,    0,    0,    0,    0,    0,
   98,  128,   81,    0,    0,   81,   75,   81,    0,   81,
    0,    0,   76,    0,    0,  128,    0,    0,    0,    0,
  127,    0,    0,   81,    0,    0,  128,    0,    0,    0,
    0,  127,    0,   77,   18,   19,   20,    0,   75,   76,
   75,   77,   43,   43,   43,  127,   76,   43,   43,   43,
   17,   77,   78,   79,   78,   80,  127,   81,   81,    0,
    0,    0,   43,    0,   43,   43,   43,   43,   43,   46,
   46,   46,   78,    0,   46,   46,   46,   18,   19,   20,
    0,    0,   22,   23,   24,   77,    0,   79,   81,   46,
    0,   46,   46,   46,   46,   46,    0,   25,    0,   26,
   27,   28,    8,    8,    8,   79,   78,    8,    8,    8,
   63,   63,   63,    0,    0,   63,   63,   63,    0,    0,
   80,    0,    8,    0,    8,    8,    8,    0,   83,    0,
   63,   83,   63,   63,   63,   18,   19,   20,   80,   79,
   22,   23,   24,   82,    0,    0,   82,    0,   82,   83,
   82,    0,    0,   84,    0,    0,   84,   26,   27,   28,
    0,    0,    0,    0,   82,  120,  121,  122,  123,  124,
  125,    0,   80,    0,   84,    0,  120,  121,  122,  123,
  124,  125,    0,   83,   83,    0,   88,    0,    0,   88,
  120,  121,  122,  123,  124,  125,    0,    0,   82,   82,
    0,  120,  121,  122,  123,  124,  125,   88,   84,   84,
    0,    0,    0,    0,   83,    0,   75,   75,   75,   75,
   75,   75,   76,   76,   76,   76,   76,   76,    0,   82,
    0,    0,    0,   81,   81,   81,   81,   81,   81,   84,
    0,   88,   88,    0,    0,    0,    0,   21,    0,    0,
   33,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   77,   77,   77,   77,   77,   77,    0,   66,    0,
    0,    0,   88,    0,    0,    0,    0,    0,   33,    0,
    0,    0,   78,   78,   78,   78,   78,   78,   74,    0,
    0,    0,    0,   33,   33,    0,  101,    0,    0,    0,
    0,    0,    0,  103,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   79,   79,   79,   79,   79,
   79,    0,    0,    0,    0,    0,    0,    0,   33,    0,
    0,    0,    0,    0,    0,    0,    0,  142,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   80,   80,
   80,   80,   80,   80,    0,    0,    0,    0,    0,   83,
   83,   83,   83,   83,   83,    0,    0,    0,    0,    0,
    0,   33,    0,   33,   82,   82,   82,   82,   82,   82,
    0,    0,    0,    0,   84,   84,   84,   84,   84,   84,
   94,    0,    0,    0,    0,    0,   33,    0,    0,    0,
    0,   33,    0,    0,    0,    0,   33,    0,    0,    0,
  108,  109,    0,  110,  111,  112,  113,   88,   88,   88,
   88,   88,   88,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  141,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  149,  150,  151,  152,  153,  154,  155,  156,
  157,  158,  159,  160,  161,  162,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  169,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   45,   61,   37,   41,   59,   38,   40,  283,  284,   42,
   43,   45,   60,  148,   40,   41,   42,   43,   37,   59,
   42,  163,   42,   42,   37,   38,  168,  265,   47,   42,
   43,  173,   45,   59,   47,  170,   58,   63,   58,  265,
  175,   40,   41,   42,   43,  265,  275,   95,   37,   40,
   59,   42,   43,   93,  258,  259,  260,   91,   40,   59,
   59,   94,   40,   37,   63,   40,   61,   47,   42,   43,
  125,   45,   63,   47,   40,   59,   42,   43,  261,  123,
   40,   94,   40,  124,   42,   43,  124,  258,  259,  260,
  138,  124,  126,  264,  265,  140,   58,   63,  124,  125,
   41,   58,   41,  163,  125,   63,   58,   40,  168,   42,
   43,  124,   59,  173,  125,   58,   93,  125,    0,    0,
   41,  125,   35,   37,   38,  124,  125,   41,   42,   43,
    3,   45,    6,   47,   37,   38,   35,  107,   41,   42,
   43,   -1,   45,   -1,   47,   -1,   -1,   -1,   37,   38,
   -1,   -1,   41,   42,   43,   -1,   45,   -1,   47,   37,
   38,   -1,   -1,   -1,   42,   43,   -1,   45,   -1,   47,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   41,   -1,
   94,   -1,   -1,   -1,   41,   -1,   -1,   -1,   -1,   -1,
  123,   94,   38,   -1,   -1,   41,   59,   43,   -1,   45,
   -1,   -1,   59,   -1,   -1,   94,   -1,   -1,   -1,   -1,
  124,   -1,   -1,   59,   -1,   -1,   94,   -1,   -1,   -1,
   -1,  124,   -1,   41,  258,  259,  260,   -1,  262,  263,
   93,  265,  258,  259,  260,  124,   93,  263,  264,  265,
  275,   59,  276,  277,   41,  279,  124,   93,   94,   -1,
   -1,   -1,  278,   -1,  280,  281,  282,  283,  284,  258,
  259,  260,   59,   -1,  263,  264,  265,  258,  259,  260,
   -1,   -1,  263,  264,  265,   93,   -1,   41,  124,  278,
   -1,  280,  281,  282,  283,  284,   -1,  278,   -1,  280,
  281,  282,  258,  259,  260,   59,   93,  263,  264,  265,
  258,  259,  260,   -1,   -1,  263,  264,  265,   -1,   -1,
   41,   -1,  278,   -1,  280,  281,  282,   -1,   38,   -1,
  278,   41,  280,  281,  282,  258,  259,  260,   59,   93,
  263,  264,  265,   38,   -1,   -1,   41,   -1,   43,   59,
   45,   -1,   -1,   38,   -1,   -1,   41,  280,  281,  282,
   -1,   -1,   -1,   -1,   59,  269,  270,  271,  272,  273,
  274,   -1,   93,   -1,   59,   -1,  269,  270,  271,  272,
  273,  274,   -1,   93,   94,   -1,   38,   -1,   -1,   41,
  269,  270,  271,  272,  273,  274,   -1,   -1,   93,   94,
   -1,  269,  270,  271,  272,  273,  274,   59,   93,   94,
   -1,   -1,   -1,   -1,  124,   -1,  269,  270,  271,  272,
  273,  274,  269,  270,  271,  272,  273,  274,   -1,  124,
   -1,   -1,   -1,  269,  270,  271,  272,  273,  274,  124,
   -1,   93,   94,   -1,   -1,   -1,   -1,   14,   -1,   -1,
   17,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  269,  270,  271,  272,  273,  274,   -1,   35,   -1,
   -1,   -1,  124,   -1,   -1,   -1,   -1,   -1,   45,   -1,
   -1,   -1,  269,  270,  271,  272,  273,  274,   55,   -1,
   -1,   -1,   -1,   60,   61,   -1,   63,   -1,   -1,   -1,
   -1,   -1,   -1,   70,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  269,  270,  271,  272,  273,
  274,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   95,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  104,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  269,  270,
  271,  272,  273,  274,   -1,   -1,   -1,   -1,   -1,  269,
  270,  271,  272,  273,  274,   -1,   -1,   -1,   -1,   -1,
   -1,  138,   -1,  140,  269,  270,  271,  272,  273,  274,
   -1,   -1,   -1,   -1,  269,  270,  271,  272,  273,  274,
   58,   -1,   -1,   -1,   -1,   -1,  163,   -1,   -1,   -1,
   -1,  168,   -1,   -1,   -1,   -1,  173,   -1,   -1,   -1,
   78,   79,   -1,   81,   82,   83,   84,  269,  270,  271,
  272,  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  102,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  120,  121,  122,  123,  124,  125,  126,  127,
  128,  129,  130,  131,  132,  133,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  147,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=285;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'","'&'",null,"'('","')'","'*'","'+'",
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
"IF","TKDOLLAR","TK_MESSAGEID","TK_FAIL","TK_SWITCH","TK_CASE","TK_DEFAULT",
"NEG",
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
"Block : SimpleItem",
"Block : '{' '}'",
"$$7 :",
"Block : '{' $$7 ItemList '}'",
"ItemList : Item ItemList",
"ItemList : Item",
"IfExpression : compiled_expression",
"INTEGER_LITERAL : DEC_INTEGER_LITERAL",
"INTEGER_LITERAL : HEX_INTEGER_LITERAL",
"INTEGER_LITERAL : DUAL_INTEGER_LITERAL",
"compiled_expression : expression",
"expression : ConstExpr",
"expression : StreamOp",
"expression : TypeCastExpr",
"expression : expression CMP_EQ expression",
"expression : expression CMP_NEQ expression",
"expression : expression CMP_LT expression",
"expression : expression CMP_LEQ expression",
"expression : expression CMP_GT expression",
"expression : expression CMP_GEQ expression",
"expression : expression '+' expression",
"expression : expression '-' expression",
"expression : expression '&' expression",
"expression : expression '|' expression",
"expression : expression '*' expression",
"expression : expression '/' expression",
"expression : expression '%' expression",
"expression : expression '^' expression",
"expression : '~' expression",
"expression : '!' expression",
"expression : '-' expression",
"expression : '(' expression ')'",
"ConstExpr : TKDOLLAR",
"ConstExpr : INTEGER_LITERAL",
"ConstExpr : BOOLEAN_LITERAL",
"ConstExpr : LABEL",
"ConstExpr : IDENTIFIER",
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

//#line 321 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
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
//#line 613 "PDL2Parser.java"
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
//#line 66 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 2:
//#line 67 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 3:
//#line 68 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 4:
//#line 72 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ if (!val_peek(2).sval.equals("start")) throw new PDLException("'start' expected"); pdldoc.setStartPacketName(val_peek(1).sval); }
break;
case 5:
//#line 76 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 8:
//#line 87 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:before ItemList */
                                           PDLPacketDeclImpl pdecl = (PDLPacketDeclImpl)val_peek(1).obj;
                                           bpush(pdecl);
                                           yyval.obj = pdecl;
                                           registerPacketDecl(pdecl); 
                                         }
break;
case 9:
//#line 96 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:after ItemList */
                                           bpop();
                                         }
break;
case 10:
//#line 100 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ registerPacketDecl((PDLPacketDeclImpl)val_peek(2).obj);  }
break;
case 11:
//#line 104 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ int padding = val_peek(0).ival;
                                           if(padding<1)
                                             yyerror("padding must >=1: "+padding);
                                              
                                           yyval.obj = new PDLPacketDeclImpl(val_peek(2).sval, padding); }
break;
case 12:
//#line 109 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLPacketDeclImpl(val_peek(0).sval, 1); /* default padding = 1 */ }
break;
case 13:
//#line 113 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).sval); }
break;
case 14:
//#line 114 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).ival); }
break;
case 15:
//#line 115 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("*-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.NoneOrMany); */
                                     }
break;
case 16:
//#line 118 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("+-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.OneOrMany); */
                                     }
break;
case 18:
//#line 128 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLLabelImpl(val_peek(0).sval)); }
break;
case 29:
//#line 142 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.Fail)); }
break;
case 30:
//#line 146 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 31:
//#line 150 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLSwitchStatementImpl s = new PDLSwitchStatementImpl((Expression)val_peek(1).obj); badd(s); bpush(s); }
break;
case 34:
//#line 159 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(false, val_peek(1).ival); badd(c); bpush(c); }
break;
case 35:
//#line 159 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 36:
//#line 160 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(true, -1);  badd(c); bpush(c); }
break;
case 37:
//#line 160 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 38:
//#line 164 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; }
break;
case 39:
//#line 168 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLMutualExclusionImpl m = new PDLMutualExclusionImpl(); badd(m); bpush(m); }
break;
case 40:
//#line 168 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 41:
//#line 169 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(2).obj; }
break;
case 42:
//#line 173 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.MessageId, val_peek(1).sval)); }
break;
case 43:
//#line 177 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 44:
//#line 181 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBitcount(val_peek(0).ival);
                                           yyval.obj=new PDLVariableImpl(val_peek(2).sval, val_peek(0).ival); }
break;
case 45:
//#line 186 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLImplicitVariableImpl((PDLVariable)val_peek(4).obj, new PDLFunctionImpl((Expression)val_peek(1).obj)));  }
break;
case 46:
//#line 190 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 47:
//#line 191 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLVariableListImpl v = (PDLVariableListImpl) val_peek(2).obj; v.setTerminal(val_peek(0).ival); badd(val_peek(2).obj); }
break;
case 48:
//#line 195 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=new PDLVariableListImpl((PDLVariable)val_peek(0).obj, (PDLMultiplicity)val_peek(1).obj); }
break;
case 49:
//#line 199 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  badd(val_peek(0).obj); }
break;
case 50:
//#line 200 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ ((PDLConstantImpl)val_peek(0).obj).setMultiplicity((PDLMultiplicityImpl)val_peek(1).obj); badd(val_peek(0).obj); }
break;
case 51:
//#line 204 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBounds(val_peek(2).ival, val_peek(0).ival);
                                                yyval.obj = new PDLConstantImpl(val_peek(2).ival, val_peek(0).ival); }
break;
case 52:
//#line 209 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(textToPacketRef(val_peek(0).sval)); }
break;
case 53:
//#line 213 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLPacketRefListImpl(textToPacketRef(val_peek(0).sval), (PDLMultiplicity)val_peek(1).obj)); }
break;
case 56:
//#line 222 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 57:
//#line 223 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLOptionalImpl o = new PDLOptionalImpl(); badd(o); bpush(o); }
break;
case 58:
//#line 223 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 59:
//#line 227 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl((PDLCondition) val_peek(1).obj); badd(c); bpush(c); yyval.obj = c;  }
break;
case 60:
//#line 228 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 61:
//#line 231 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ }
break;
case 62:
//#line 232 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLBlockItemImpl()); }
break;
case 63:
//#line 233 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLBlockItemImpl b = new PDLBlockItemImpl(); badd(b); bpush(b); }
break;
case 64:
//#line 233 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 67:
//#line 242 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLCompiledCondition((Expression)val_peek(0).obj); }
break;
case 71:
//#line 252 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(0).obj; }
break;
case 75:
//#line 260 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.cmpEq ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 76:
//#line 261 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.cmpNeq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 77:
//#line 262 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.cmpLt ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 78:
//#line 263 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.cmpLEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 79:
//#line 264 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.cmpGt ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 80:
//#line 265 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.cmpGEq( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 81:
//#line 267 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.add   ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 82:
//#line 268 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.minus ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 83:
//#line 269 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.and   ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 84:
//#line 270 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.or    ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 85:
//#line 272 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.mul   ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 86:
//#line 273 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.div   ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 87:
//#line 274 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.mod   ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 88:
//#line 275 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.xor   ( (Expression) val_peek(2).obj, (Expression) val_peek(0).obj ); }
break;
case 89:
//#line 277 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.inv( (Expression) val_peek(0).obj ); }
break;
case 90:
//#line 278 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.inv( (Expression) val_peek(0).obj ); }
break;
case 91:
//#line 279 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.neg( (Expression) val_peek(0).obj ); }
break;
case 92:
//#line 281 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(1).obj; }
break;
case 93:
//#line 285 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.fpush); }
break;
case 94:
//#line 286 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.ipush, val_peek(0).ival ); }
break;
case 95:
//#line 287 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.bpush, val_peek(0).ival==1?true:false); }
break;
case 96:
//#line 288 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.lpush, (String)val_peek(0).sval); }
break;
case 97:
//#line 289 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new Expression(Opcodes.vpush, (String)val_peek(0).sval); }
break;
case 98:
//#line 293 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.castToInt((Expression) val_peek(0).obj); }
break;
case 99:
//#line 294 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = Expression.castToBoolean((Expression) val_peek(0).obj); }
break;
case 100:
//#line 303 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{   yyval.obj = new Expression(val_peek(5).ival, 
                                           (Expression)val_peek(4).obj, (Expression)val_peek(3).obj, 
                                           (Expression)val_peek(2).obj, (Expression)val_peek(1).obj); }
break;
case 101:
//#line 309 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 102:
//#line 313 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.land; }
break;
case 103:
//#line 314 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lor; }
break;
case 104:
//#line 315 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lxor; }
break;
case 105:
//#line 316 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lmul; }
break;
case 106:
//#line 317 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.ladd; }
break;
//#line 1104 "PDL2Parser.java"
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
