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
import net.sf.nmedit.jpdl2.dom.*;
import net.sf.nmedit.jpdl2.utils.*;
//#line 25 "PDL2Parser.java"




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
public final static short INLINEPACKETREF=265;
public final static short IDENTIFIER=266;
public final static short TK_LSHIFT=267;
public final static short TK_RSHIFT=268;
public final static short TK_URSHIFT=269;
public final static short CMP_EQ=270;
public final static short CMP_NEQ=271;
public final static short CMP_LT=272;
public final static short CMP_LEQ=273;
public final static short CMP_GT=274;
public final static short CMP_GEQ=275;
public final static short ASSIGN=276;
public final static short CAST_TO_INT=277;
public final static short CAST_TO_BOOLEAN=278;
public final static short IF=279;
public final static short TKDOLLAR=280;
public final static short TK_MESSAGEID=281;
public final static short TK_FAIL=282;
public final static short TK_SWITCH=283;
public final static short TK_CASE=284;
public final static short TK_DEFAULT=285;
public final static short TK_PLUS=286;
public final static short TK_TIMES=287;
public final static short TK_MINUS=288;
public final static short TK_DIVIDE=289;
public final static short TK_PERCENT=290;
public final static short TK_AND=291;
public final static short TK_OR=292;
public final static short TK_XOR=293;
public final static short TK_TILDE=294;
public final static short TK_NOT=295;
public final static short TK_EQ=296;
public final static short TK_INTERROGATIONMARK=297;
public final static short NEG=298;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,   26,   27,    4,    4,   44,    2,    2,
    3,    3,   11,   11,   11,   11,   42,   32,   32,   32,
   32,   32,   32,   32,   32,   32,   32,   32,   32,   32,
   32,   37,   38,   39,   40,   40,   45,   41,   46,   41,
   36,   47,   35,   35,   34,   13,   15,   14,   19,   20,
   18,   17,   17,   16,   10,   10,   12,   23,   24,   25,
   29,   29,   30,   48,   30,   49,   21,   28,   28,   50,
   28,   31,   31,   22,    1,    1,    1,    9,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    8,    8,    8,    8,    8,   33,   33,    6,
    5,   43,   43,   43,   43,   43,
};
final static short yylen[] = {                            2,
    0,    1,    2,    3,    1,    1,    2,    0,    5,    3,
    3,    1,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    4,    4,    1,    2,    0,    5,    0,    4,
    3,    0,    4,    3,    4,    3,    1,    3,    1,    2,
    5,    1,    3,    2,    1,    2,    3,    1,    1,    2,
    1,    1,    1,    0,    3,    0,    6,    1,    2,    0,
    4,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    2,    2,
    2,    3,    1,    1,    1,    1,    1,    2,    2,    7,
    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    5,    0,    2,    0,    0,   17,
    7,    0,    3,    0,    4,   10,    0,   75,   76,   77,
   11,   18,   58,   59,    0,    0,    0,   32,    0,   16,
   15,    0,   64,   42,    0,   26,    0,   55,   31,    0,
   22,    0,   23,   49,   24,   25,   63,   19,   20,   21,
    0,   61,    0,   62,   27,   28,   29,   30,    0,    0,
   13,    0,    0,    0,    0,    0,    0,   50,    0,    0,
    0,   14,    0,   60,    0,   56,   54,    0,    0,   72,
    9,    0,   46,   48,  105,  106,  107,    0,    0,  103,
    0,    0,    0,    0,    0,  104,   80,    0,   79,   74,
    0,   81,    0,    0,    0,   65,   68,    0,   41,    0,
   57,    0,   53,    0,    0,    0,    0,  108,  109,  101,
   99,  100,    0,  116,  115,  112,  113,  114,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   66,   45,   34,   69,
    0,   44,    0,    0,    0,   39,   33,   36,  102,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   95,    0,   96,   97,    0,    0,    0,    0,    0,
   43,   51,   37,    0,    0,    0,   67,   71,    0,   40,
    0,   38,    0,  110,
};
final static short yydgoto[] = {                          2,
   96,    3,    4,    5,  161,   97,   98,   99,  100,   36,
   37,   38,   39,   40,   41,   42,   43,   44,   45,   46,
   47,  101,   48,   49,   50,    6,    7,  106,   51,   52,
   53,   54,  102,   55,   70,   56,   57,   58,   59,  116,
  117,    8,  129,   17,  189,  184,   71,   69,  179,  151,
};
final static short yysindex[] = {                      -252,
 -250,    0, -246, -237,    0, -246,    0, -247,  -13,    0,
    0,   -6,    0, -229,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,  -49,    4,   16,    0,   17,    0,
    0, -208,    0,    0,  -47,    0, -226,    0,    0, -236,
    0, -227,    0,    0,    0,    0,    0,    0,    0,    0,
    7,    0,   19,    0,    0,    0,    0,    0,  -43, -180,
    0, -229,   87, -178,   87,   26, -236,    0,  312,  -36,
    7,    0, -229,    0,   30,    0,    0,   49, -229,    0,
    0, -259,    0,    0,    0,    0,    0,   87,   87,    0,
   87,   87,   87,   87, -200,    0,    0, -139,    0,    0,
   55,    0,   56,  116,  -30,    0,    0,    7,    0, -193,
    0,   87,    0, -229,   43,  -25, -259,    0,    0,    0,
    0,    0,  125,    0,    0,    0,    0,    0,   44,   87,
   87,   87,   87,   87,   87,   87,   87,   87,   87,   87,
   87,   87,   87,   87,   87,   87,    0,    0,    0,    0,
    7,    0,    7,  152,   47,    0,    0,    0,    0,   87,
   44, -216, -216, -216, -106, -106, -106, -106, -106, -106,
 -239,    0, -239,    0,    0, -222, -222, -222,  312,  -23,
    0,    0,    0,  312, -139,   44,    0,    0,  312,    0,
   44,    0,   14,    0,
};
final static short yyrindex[] = {                       108,
 -266,    0,  109,    0,    0,    0,    0, -237,    0,    0,
    0,   42,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -40,
    0,   -5,    0,    0,    0,    0,    0,    0,    0,    0,
  -46,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   69,    0,    0,
    0,    0,    0,    0,   54,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -14,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  433,  444,  453,  -38,   99,  411,  468,  474,  488,
  190,    0,  250,    0,    0,  317,  387,  396,    0,    0,
    0,    0,    0,    0,  -44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -10,    0,    0,  110, -149,    0,  685,    0,    0,    0,
    0,   77,    0,  -15,    0,    0,    0,   83,    0,    0,
    0,    0,    0,    0,    0,    0,  112, -161,  -63,    0,
  -45,  -67,    0,    0,    0,    0,    0,    0,    0,   -1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=845;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   47,  107,   82,   21,  109,   80,   35,  110,   62,   17,
   73,  186,   73,    1,  111,    9,   67,  187,   47,   10,
   82,   77,  190,   17,  114,  115,   75,  192,   18,   19,
   20,   18,   19,   20,   52,   52,  191,   74,   12,   66,
   35,  193,   14,   63,  152,   15,   34,  140,  111,  142,
  143,   84,   16,   52,   82,   64,   65,   66,   35,   78,
   35,   79,  111,  139,  140,  141,  142,  143,  113,  139,
  140,  141,  142,  143,  144,  145,  146,   81,   73,   82,
   83,    8,  103,   62,   47,  124,  125,   73,  112,  181,
  126,  127,  128,   70,  150,  147,  148,   35,  153,  157,
  156,  188,  160,  155,  183,  180,  194,    1,    6,   78,
   35,  107,   11,   76,   68,  158,  107,   13,    0,   52,
    0,  107,    0,    0,    0,    0,   94,  130,  131,  132,
  133,  134,  135,  136,  137,  138,    0,    0,    0,   83,
   35,    0,   35,    0,    0,    0,  139,  140,  141,  142,
  143,  144,  145,  146,    0,    0,  149,   83,    0,    0,
  130,  131,  132,    0,    0,  159,    0,    0,   35,    0,
    0,    0,    0,   35,    0,    0,    0,   95,   35,  139,
  140,  141,  142,  143,  144,  145,  146,    0,    0,    0,
    0,   83,  182,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   47,   47,   47,
    0,    0,   47,   47,   47,   47,   60,    0,    0,    0,
   91,   82,   82,   82,   82,   82,   82,   61,   47,   72,
   47,   47,   47,   47,   47,   47,   47,    0,   91,   47,
    0,   47,   52,   52,   52,  108,   47,   52,   52,   52,
   52,    0,    0,    0,   18,   19,   20,    0,    0,   22,
   23,   24,   25,   52,    0,   52,   52,   52,   52,   52,
   52,   52,   91,    0,   52,   26,   52,   27,   28,   29,
   92,   52,   30,   31,    0,    0,   32,    0,    0,    8,
    8,    8,    0,   33,    8,    8,    8,    8,   92,    0,
    0,   70,   70,   70,    0,    0,   70,   70,   70,   70,
    8,    0,    8,    8,    8,    0,    0,    8,    8,    0,
    0,    8,   70,    0,   70,   70,   70,    0,    8,   70,
   70,    0,   92,   70,   18,   19,   20,    0,   85,   86,
   70,   34,   87,    0,    0,    0,    0,   93,    0,    0,
    0,    0,    0,   88,   89,    0,   90,    0,   83,   83,
   83,   83,   83,   83,   91,   93,    0,    0,    0,    0,
   92,   93,  130,  131,  132,  133,  134,  135,  136,  137,
  138,  130,  131,  132,  133,  134,  135,  136,  137,  138,
    0,  139,  140,  141,  142,  143,  144,  145,  146,   93,
  139,  140,  141,  142,  143,  144,  145,  146,  130,  131,
  132,  133,  134,  135,  136,  137,  138,   94,    0,    0,
    0,    0,    0,    0,  105,    0,   98,  139,  140,  141,
  142,  143,  144,  145,  146,   94,    0,    0,    0,    0,
    0,   84,    0,    0,   98,    0,   91,   91,   91,   91,
   91,   91,   91,   91,   91,    0,    0,    0,    0,   84,
    0,    0,    0,   88,    0,   91,    0,   91,    0,   94,
   91,   91,   91,    0,   89,    0,    0,    0,   98,    0,
    0,   88,    0,   90,    0,    0,    0,    0,    0,    0,
    0,    0,   89,   84,    0,    0,    0,    0,   85,    0,
    0,   90,    0,    0,   86,    0,   92,   92,   92,   92,
   92,   92,   92,   92,   92,   88,   85,    0,   87,    0,
    0,    0,   86,    0,    0,   92,   89,   92,    0,    0,
   92,   92,   92,    0,    0,   90,   87,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   85,    0,    0,    0,    0,    0,   86,    0,    0,   18,
   19,   20,    0,    0,   22,   23,   24,   25,    0,    0,
   87,    0,    0,   93,   93,   93,   93,   93,   93,   93,
   93,   93,   27,   28,   29,    0,    0,   30,   31,    0,
    0,   32,    0,    0,    0,    0,    0,   93,   93,   93,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   94,   94,   94,   94,   94,   94,   94,
   94,   94,   98,   98,   98,   98,   98,   98,   98,   98,
   98,    0,    0,    0,    0,    0,    0,   94,   94,   94,
   84,   84,   84,   84,   84,   84,   98,   98,   98,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   88,
   88,   88,   88,   88,   88,   88,   88,   88,    0,    0,
   89,   89,   89,   89,   89,   89,   89,   89,   89,   90,
   90,   90,   90,   90,   90,   90,   90,   90,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   85,   85,   85,
   85,   85,   85,   86,   86,   86,   86,   86,   86,  104,
    0,    0,    0,    0,    0,    0,    0,   87,   87,   87,
   87,   87,   87,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  118,  119,    0,  120,  121,  122,  123,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  154,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  162,  163,  164,  165,  166,  167,
  168,  169,  170,  171,  172,  173,  174,  175,  176,  177,
  178,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  185,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   69,   41,   14,   41,   51,   17,   71,   58,  276,
   58,  161,   59,  266,   59,  266,   32,  179,   59,  266,
   59,   37,  184,  290,  284,  285,   37,  189,  258,  259,
  260,  258,  259,  260,   40,   41,  186,  264,  276,  266,
   51,  191,  290,   40,  108,   59,   40,  287,   93,  289,
  290,   62,   59,   59,   93,   40,   40,  266,   69,  296,
   71,  289,   73,  286,  287,  288,  289,  290,   79,  286,
  287,  288,  289,  290,  291,  292,  293,   59,  125,  123,
  261,   40,  261,   58,  125,  286,  287,   58,   40,  153,
  291,  292,  293,   40,  125,   41,   41,  108,  292,  125,
   58,  125,   59,  114,   58,  151,   93,    0,    0,   41,
  125,  179,    3,   37,   32,  117,  184,    6,   -1,  125,
   -1,  189,   -1,   -1,   -1,   -1,   40,  267,  268,  269,
  270,  271,  272,  273,  274,  275,   -1,   -1,   -1,   41,
  151,   -1,  153,   -1,   -1,   -1,  286,  287,  288,  289,
  290,  291,  292,  293,   -1,   -1,   41,   59,   -1,   -1,
  267,  268,  269,   -1,   -1,   41,   -1,   -1,  179,   -1,
   -1,   -1,   -1,  184,   -1,   -1,   -1,   91,  189,  286,
  287,  288,  289,  290,  291,  292,  293,   -1,   -1,   -1,
   -1,   93,   41,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  258,  259,  260,
   -1,   -1,  263,  264,  265,  266,  276,   -1,   -1,   -1,
   41,  270,  271,  272,  273,  274,  275,  287,  279,  287,
  281,  282,  283,  284,  285,  286,  287,   -1,   59,  290,
   -1,  292,  258,  259,  260,  292,  297,  263,  264,  265,
  266,   -1,   -1,   -1,  258,  259,  260,   -1,   -1,  263,
  264,  265,  266,  279,   -1,  281,  282,  283,  284,  285,
  286,  287,   93,   -1,  290,  279,  292,  281,  282,  283,
   41,  297,  286,  287,   -1,   -1,  290,   -1,   -1,  258,
  259,  260,   -1,  297,  263,  264,  265,  266,   59,   -1,
   -1,  258,  259,  260,   -1,   -1,  263,  264,  265,  266,
  279,   -1,  281,  282,  283,   -1,   -1,  286,  287,   -1,
   -1,  290,  279,   -1,  281,  282,  283,   -1,  297,  286,
  287,   -1,   93,  290,  258,  259,  260,   -1,  262,  263,
  297,   40,  266,   -1,   -1,   -1,   -1,   41,   -1,   -1,
   -1,   -1,   -1,  277,  278,   -1,  280,   -1,  270,  271,
  272,  273,  274,  275,  288,   59,   -1,   -1,   -1,   -1,
  294,  295,  267,  268,  269,  270,  271,  272,  273,  274,
  275,  267,  268,  269,  270,  271,  272,  273,  274,  275,
   -1,  286,  287,  288,  289,  290,  291,  292,  293,   93,
  286,  287,  288,  289,  290,  291,  292,  293,  267,  268,
  269,  270,  271,  272,  273,  274,  275,   41,   -1,   -1,
   -1,   -1,   -1,   -1,  123,   -1,   41,  286,  287,  288,
  289,  290,  291,  292,  293,   59,   -1,   -1,   -1,   -1,
   -1,   41,   -1,   -1,   59,   -1,  267,  268,  269,  270,
  271,  272,  273,  274,  275,   -1,   -1,   -1,   -1,   59,
   -1,   -1,   -1,   41,   -1,  286,   -1,  288,   -1,   93,
  291,  292,  293,   -1,   41,   -1,   -1,   -1,   93,   -1,
   -1,   59,   -1,   41,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   59,   93,   -1,   -1,   -1,   -1,   41,   -1,
   -1,   59,   -1,   -1,   41,   -1,  267,  268,  269,  270,
  271,  272,  273,  274,  275,   93,   59,   -1,   41,   -1,
   -1,   -1,   59,   -1,   -1,  286,   93,  288,   -1,   -1,
  291,  292,  293,   -1,   -1,   93,   59,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   93,   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,  258,
  259,  260,   -1,   -1,  263,  264,  265,  266,   -1,   -1,
   93,   -1,   -1,  267,  268,  269,  270,  271,  272,  273,
  274,  275,  281,  282,  283,   -1,   -1,  286,  287,   -1,
   -1,  290,   -1,   -1,   -1,   -1,   -1,  291,  292,  293,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  267,  268,  269,  270,  271,  272,  273,
  274,  275,  267,  268,  269,  270,  271,  272,  273,  274,
  275,   -1,   -1,   -1,   -1,   -1,   -1,  291,  292,  293,
  270,  271,  272,  273,  274,  275,  291,  292,  293,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  267,
  268,  269,  270,  271,  272,  273,  274,  275,   -1,   -1,
  267,  268,  269,  270,  271,  272,  273,  274,  275,  267,
  268,  269,  270,  271,  272,  273,  274,  275,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  270,  271,  272,
  273,  274,  275,  270,  271,  272,  273,  274,  275,   65,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  270,  271,  272,
  273,  274,  275,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   88,   89,   -1,   91,   92,   93,   94,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  112,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  130,  131,  132,  133,  134,  135,
  136,  137,  138,  139,  140,  141,  142,  143,  144,  145,
  146,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  160,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=298;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,"':'","';'",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"NL","DEC_INTEGER_LITERAL",
"HEX_INTEGER_LITERAL","DUAL_INTEGER_LITERAL","STRING_LITERAL","BOOLEAN_LITERAL",
"LABEL","PACKETREF","INLINEPACKETREF","IDENTIFIER","TK_LSHIFT","TK_RSHIFT",
"TK_URSHIFT","CMP_EQ","CMP_NEQ","CMP_LT","CMP_LEQ","CMP_GT","CMP_GEQ","ASSIGN",
"CAST_TO_INT","CAST_TO_BOOLEAN","IF","TKDOLLAR","TK_MESSAGEID","TK_FAIL",
"TK_SWITCH","TK_CASE","TK_DEFAULT","TK_PLUS","TK_TIMES","TK_MINUS","TK_DIVIDE",
"TK_PERCENT","TK_AND","TK_OR","TK_XOR","TK_TILDE","TK_NOT","TK_EQ",
"TK_INTERROGATIONMARK","NEG",
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
"PacketDeclStart : PacketName TK_PERCENT INTEGER_LITERAL",
"PacketDeclStart : PacketName",
"Multiplicity : IDENTIFIER TK_TIMES",
"Multiplicity : INTEGER_LITERAL TK_TIMES",
"Multiplicity : TK_TIMES",
"Multiplicity : TK_PLUS",
"PacketName : IDENTIFIER",
"SimpleItem : LABEL",
"SimpleItem : PacketRef",
"SimpleItem : InlinePacketRef",
"SimpleItem : PacketRefList",
"SimpleItem : Variable",
"SimpleItem : VariableList",
"SimpleItem : ImplicitVariable",
"SimpleItem : ImplicitAnonymVariable",
"SimpleItem : Constant",
"SimpleItem : MessageId",
"SimpleItem : ChoiceStatement",
"SimpleItem : FailStatement",
"SimpleItem : SwitchStatement",
"SimpleItem : StringDef",
"FailStatement : TK_FAIL",
"SwitchStatement : SwitchStatementHeader '{' SwitchCaseList '}'",
"SwitchStatementHeader : TK_SWITCH '(' expression ')'",
"SwitchCaseList : CaseStatement",
"SwitchCaseList : CaseStatement SwitchCaseList",
"$$2 :",
"CaseStatement : TK_CASE INTEGER_LITERAL ':' $$2 Block",
"$$3 :",
"CaseStatement : TK_DEFAULT ':' $$3 Block",
"ChoiceStatement : '(' Choice ')'",
"$$4 :",
"Choice : $$4 Item TK_OR Item",
"Choice : Choice TK_OR Item",
"MessageId : TK_MESSAGEID '(' STRING_LITERAL ')'",
"StringDef : IDENTIFIER ASSIGN STRING_LITERAL",
"Variable : RawVariable",
"RawVariable : IDENTIFIER ':' INTEGER_LITERAL",
"ImplicitVariable : GetImplicitVariable",
"ImplicitAnonymVariable : TK_PERCENT GetImplicitVariable",
"GetImplicitVariable : RawVariable TK_EQ '(' expression ')'",
"VariableList : RawVariableList",
"VariableList : RawVariableList TK_DIVIDE INTEGER_LITERAL",
"RawVariableList : Multiplicity RawVariable",
"Constant : ConstantWithoutMuliplicity",
"Constant : Multiplicity ConstantWithoutMuliplicity",
"ConstantWithoutMuliplicity : INTEGER_LITERAL ':' INTEGER_LITERAL",
"PacketRef : PACKETREF",
"InlinePacketRef : INLINEPACKETREF",
"PacketRefList : Multiplicity PACKETREF",
"Item : BlockItem",
"Item : SimpleItem",
"BlockItem : IfStatement",
"$$5 :",
"BlockItem : TK_INTERROGATIONMARK $$5 Block",
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
"expression : expression TK_LSHIFT expression",
"expression : expression TK_RSHIFT expression",
"expression : expression TK_URSHIFT expression",
"expression : expression TK_PLUS expression",
"expression : expression TK_MINUS expression",
"expression : expression TK_AND expression",
"expression : expression TK_OR expression",
"expression : expression TK_TIMES expression",
"expression : expression TK_DIVIDE expression",
"expression : expression TK_PERCENT expression",
"expression : expression TK_XOR expression",
"expression : TK_TILDE expression",
"expression : TK_NOT expression",
"expression : TK_MINUS expression",
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
"StreamOpToken : TK_AND",
"StreamOpToken : TK_OR",
"StreamOpToken : TK_XOR",
"StreamOpToken : TK_TIMES",
"StreamOpToken : TK_PLUS",
};

//#line 363 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
  /* a reference to the lexer object */
  private PDL2Lexer lexer;
  
  private PDLDocumentImpl pdldoc = new PDLDocumentImpl();

  public PDLDocument getDocument()
  {
     return pdldoc;
  }
  
  private List<Expression> exprStack = new ArrayList<Expression>();
  
  private void epush(Expression e)
  {
    exprStack.add(e);
  }
  
  public Expression epop1()
  {
    Expression e = exprStack.remove(exprStack.size()-2);
    return e;
  }
  
  public Expression epop()
  {
    Expression e = exprStack.remove(exprStack.size()-1);
    return e;
  }
  
  public Expression epopFinal()
  {
    if(exprStack.size()>1)
      throw new IllegalStateException();
    return epop();
  }
  

  // PDLBlock and choice elements
  private List<Object> bstack = new ArrayList<Object>();
  
  private static final boolean PDLDebug = false;
  
  private void bpush(Object b)
  {
    if (PDLDebug) System.out.println("bpush:"+b);
    
    if ((b instanceof PDLBlock) || (b instanceof PDLChoiceImpl)
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
    else if (p instanceof PDLChoiceImpl)
        ((PDLChoiceImpl)p).add((PDLItem)item);
    else if (p instanceof PDLCaseStatementImpl)
        ((PDLCaseStatementImpl)p).add((PDLItem)item);
    else if (p instanceof PDLSwitchStatementImpl)
        ((PDLSwitchStatementImpl)p).add((PDLCaseStatementImpl)item);
    else throw new ClassCastException("could not determine type of"+item.getClass());
  }

  private PDLPacketRefImpl textToPacketRef(String s)
  {
    int i = s.indexOf('$');
    return new PDLPacketRefImpl(pdldoc, s.substring(0,i), s.substring(i+1), false);
  }
  
  private PDLPacketRefImpl textToInlinePacketRef(String s)
  {
    return new PDLPacketRefImpl(pdldoc, s.substring(0,s.length()-2), null, true);
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
//#line 716 "PDL2Parser.java"
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
//#line 82 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 2:
//#line 83 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 3:
//#line 84 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 4:
//#line 88 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ if (!val_peek(2).sval.equals("start")) throw new PDLException("'start' expected"); pdldoc.setStartPacketName(val_peek(1).sval); }
break;
case 5:
//#line 92 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 8:
//#line 103 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:before ItemList */
                                           PDLPacketDeclImpl pdecl = (PDLPacketDeclImpl)val_peek(1).obj;
                                           bpush(pdecl);
                                           yyval.obj = pdecl;
                                           registerPacketDecl(pdecl); 
                                         }
break;
case 9:
//#line 112 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:after ItemList */
                                           bpop();
                                         }
break;
case 10:
//#line 116 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ registerPacketDecl((PDLPacketDeclImpl)val_peek(2).obj);  }
break;
case 11:
//#line 120 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ int padding = val_peek(0).ival;
                                           if(padding<1)
                                             yyerror("padding must >=1: "+padding);
                                              
                                           yyval.obj = new PDLPacketDeclImpl(val_peek(2).sval, padding); }
break;
case 12:
//#line 125 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLPacketDeclImpl(val_peek(0).sval, 1); /* default padding = 1 */ }
break;
case 13:
//#line 129 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).sval); }
break;
case 14:
//#line 130 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).ival); }
break;
case 15:
//#line 131 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("*-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.NoneOrMany); */
                                     }
break;
case 16:
//#line 134 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("+-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.OneOrMany); */
                                     }
break;
case 18:
//#line 144 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.Label, val_peek(0).sval)); }
break;
case 32:
//#line 161 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.Fail)); }
break;
case 33:
//#line 165 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 34:
//#line 169 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLSwitchStatementImpl s = new PDLSwitchStatementImpl(epopFinal()); badd(s); bpush(s); }
break;
case 37:
//#line 178 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(false, val_peek(1).ival); badd(c); bpush(c); }
break;
case 38:
//#line 178 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 39:
//#line 179 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(true, -1);  badd(c); bpush(c); }
break;
case 40:
//#line 179 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 41:
//#line 183 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; }
break;
case 42:
//#line 187 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLChoiceImpl m = new PDLChoiceImpl(); badd(m); bpush(m); }
break;
case 43:
//#line 187 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 44:
//#line 188 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(2).obj; }
break;
case 45:
//#line 192 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.MessageId, val_peek(1).sval)); }
break;
case 46:
//#line 196 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.StringDef, val_peek(2).sval, val_peek(0).sval)); }
break;
case 47:
//#line 200 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 48:
//#line 204 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBitcount(val_peek(0).ival);
                                           yyval.obj=PDLVariableImpl.create(val_peek(2).sval, val_peek(0).ival); }
break;
case 49:
//#line 209 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj);  }
break;
case 50:
//#line 214 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLVariableImpl v = (PDLVariableImpl) val_peek(0).obj; v.setAnonym(); badd(v); }
break;
case 51:
//#line 218 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLFunction function;
                                                 try { function = new PDLFunctionImpl(epopFinal()); } catch(IllegalArgumentException iae) {throw new PDLException(iae);}
                                                 yyval.obj = PDLVariableImpl.createImplicit((PDLVariable)val_peek(4).obj, function, false);  }
break;
case 52:
//#line 224 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 53:
//#line 225 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLVariableImpl v = (PDLVariableImpl) val_peek(2).obj; v.setTerminal(val_peek(0).ival); badd(val_peek(2).obj); }
break;
case 54:
//#line 229 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj= PDLVariableImpl.createVariableList((PDLVariable)val_peek(0).obj, (PDLMultiplicity)val_peek(1).obj); }
break;
case 55:
//#line 233 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  badd(val_peek(0).obj); }
break;
case 56:
//#line 234 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ ((PDLConstantImpl)val_peek(0).obj).setMultiplicity((PDLMultiplicityImpl)val_peek(1).obj); badd(val_peek(0).obj); }
break;
case 57:
//#line 238 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBounds(val_peek(2).ival, val_peek(0).ival);
                                                yyval.obj = new PDLConstantImpl(val_peek(2).ival, val_peek(0).ival); }
break;
case 58:
//#line 243 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(textToPacketRef(val_peek(0).sval)); }
break;
case 59:
//#line 247 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(textToInlinePacketRef(val_peek(0).sval)); }
break;
case 60:
//#line 251 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLPacketRefImpl(pdldoc, textToPacketRef(val_peek(0).sval), (PDLMultiplicity)val_peek(1).obj)); }
break;
case 63:
//#line 260 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 64:
//#line 261 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLOptionalImpl o = new PDLOptionalImpl(); badd(o); bpush(o); }
break;
case 65:
//#line 261 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 66:
//#line 265 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl((PDLCondition) val_peek(1).obj); badd(c); bpush(c); yyval.obj = c;  }
break;
case 67:
//#line 266 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 68:
//#line 269 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ }
break;
case 69:
//#line 270 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLBlockImpl()); }
break;
case 70:
//#line 271 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLBlockImpl b = new PDLBlockImpl(); badd(b); bpush(b); }
break;
case 71:
//#line 271 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 74:
//#line 280 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ try {yyval.obj = new PDLCompiledCondition((Expression)val_peek(0).obj);} catch(IllegalArgumentException iae) {throw new PDLException(iae);} }
break;
case 78:
//#line 290 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = epopFinal(); }
break;
case 82:
//#line 298 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpEq  ( epop1(), epop() )); }
break;
case 83:
//#line 299 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpNeq ( epop1(), epop() )); }
break;
case 84:
//#line 300 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpLt  ( epop1(), epop() )); }
break;
case 85:
//#line 301 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpLEq ( epop1(), epop() )); }
break;
case 86:
//#line 302 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpGt  ( epop1(), epop() )); }
break;
case 87:
//#line 303 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpGEq ( epop1(), epop() )); }
break;
case 88:
//#line 305 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.shl  ( epop1(), epop() )); }
break;
case 89:
//#line 306 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.shr  ( epop1(), epop() )); }
break;
case 90:
//#line 307 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.ushr ( epop1(), epop() )); }
break;
case 91:
//#line 309 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.add   ( epop1(), epop() )); }
break;
case 92:
//#line 310 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.minus ( epop1(), epop() )); }
break;
case 93:
//#line 311 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.and   ( epop1(), epop() )); }
break;
case 94:
//#line 312 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.or    ( epop1(), epop() )); }
break;
case 95:
//#line 314 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.mul   ( epop1(), epop() )); }
break;
case 96:
//#line 315 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.div   ( epop1(), epop() )); }
break;
case 97:
//#line 316 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.mod ( epop1(), epop() )); }
break;
case 98:
//#line 317 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.xor ( epop1(), epop() )); }
break;
case 99:
//#line 319 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.inv ( epop() )); }
break;
case 100:
//#line 320 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.inv ( epop() )); }
break;
case 101:
//#line 321 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.neg ( epop() )); }
break;
case 102:
//#line 323 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression( epop() )); }
break;
case 103:
//#line 327 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.fpush)); }
break;
case 104:
//#line 328 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.ipush, val_peek(0).ival )); }
break;
case 105:
//#line 329 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.bpush, val_peek(0).ival==1?true:false)); }
break;
case 106:
//#line 330 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.lpush, (String)val_peek(0).sval)); }
break;
case 107:
//#line 331 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.vpush, (String)val_peek(0).sval)); }
break;
case 108:
//#line 335 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.castToInt((Expression) val_peek(0).obj)); }
break;
case 109:
//#line 336 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.castToBoolean((Expression) val_peek(0).obj)); }
break;
case 110:
//#line 345 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{   epush(new Expression(val_peek(5).ival, 
                                           (Expression)val_peek(4).obj, (Expression)val_peek(3).obj, 
                                           (Expression)val_peek(2).obj, (Expression)val_peek(1).obj)); }
break;
case 111:
//#line 351 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=epopFinal(); }
break;
case 112:
//#line 355 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.land; }
break;
case 113:
//#line 356 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lor; }
break;
case 114:
//#line 357 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lxor; }
break;
case 115:
//#line 358 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lmul; }
break;
case 116:
//#line 359 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.ladd; }
break;
//#line 1237 "PDL2Parser.java"
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
