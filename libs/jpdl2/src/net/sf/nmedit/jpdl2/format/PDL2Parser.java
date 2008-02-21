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

import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLCondition;
import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.dom.PDLFunction;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.dom.PDLMultiplicity;
import net.sf.nmedit.jpdl2.dom.PDLPacketDecl;
import net.sf.nmedit.jpdl2.dom.PDLVariable;
import net.sf.nmedit.jpdl2.impl.*;
import net.sf.nmedit.jpdl2.utils.PDLUtils;

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
public final static short TK_LSHIFT=266;
public final static short TK_RSHIFT=267;
public final static short TK_URSHIFT=268;
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
public final static short TK_PLUS=285;
public final static short TK_TIMES=286;
public final static short TK_MINUS=287;
public final static short TK_DIVIDE=288;
public final static short TK_PERCENT=289;
public final static short TK_AND=290;
public final static short TK_OR=291;
public final static short TK_XOR=292;
public final static short TK_TILDE=293;
public final static short TK_NOT=294;
public final static short TK_EQ=295;
public final static short TK_INTERROGATIONMARK=296;
public final static short NEG=297;
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
    7,    7,    7,    7,    7,    8,    8,    8,    8,    8,
   29,   29,    6,    5,   39,   39,   39,   39,   39,
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
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    2,    2,    3,    1,    1,    1,    1,    1,
    2,    2,    7,    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    5,    0,    2,    0,    0,   17,
    7,    0,    3,    0,    4,   10,    0,   68,   69,   70,
   11,   18,   52,    0,    0,    0,   29,    0,   16,   15,
   57,   39,    0,   24,    0,   49,    0,   21,    0,   22,
   23,   56,   19,   20,    0,   54,    0,   55,   25,   26,
   27,   28,    0,   13,    0,    0,    0,    0,    0,    0,
    0,   14,    0,   53,    0,    0,   50,   48,    0,    0,
   65,    9,    0,   44,   98,   99,  100,    0,    0,   96,
    0,    0,    0,    0,    0,   97,   73,    0,   72,   67,
    0,   74,    0,    0,    0,   58,   61,    0,   38,    0,
   51,    0,   47,    0,    0,    0,    0,  101,  102,   94,
   92,   93,    0,  109,  108,  105,  106,  107,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   59,   42,   31,   62,
    0,   41,    0,    0,    0,   36,   30,   33,   95,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   88,    0,   89,   90,    0,    0,    0,    0,    0,
   40,   45,   34,    0,    0,    0,   60,   64,    0,   37,
    0,   35,    0,  103,
};
final static short yydgoto[] = {                          2,
   86,    3,    4,    5,  151,   87,   88,   89,   90,   34,
   35,   36,   37,   38,   39,   40,   41,   42,   91,   43,
   44,    6,    7,   96,   45,   46,   47,   48,   92,   49,
   60,   50,   51,   52,   53,  106,  107,    8,  119,   17,
  179,  174,   61,   59,  169,  141,
};
final static short yysindex[] = {                      -250,
 -245,    0, -234, -239,    0, -234,    0, -249,  -16,    0,
    0,  -15,    0, -185,    0,    0,    1,    0,    0,    0,
    0,    0,    0,  -56,   10,   12,    0,   19,    0,    0,
    0,    0,  -55,    0, -226,    0, -215,    0, -207,    0,
    0,    0,    0,    0,    1,    0,   24,    0,    0,    0,
    0,    0,  -39,    0, -185,   71, -175,   71,  217,  -36,
    1,    0, -185,    0,   29,   31,    0,    0,   50, -185,
    0,    0, -257,    0,    0,    0,    0,   71,   71,    0,
   71,   71,   71,   71, -268,    0,    0,  -88,    0,    0,
   51,    0,   52,  100,  -30,    0,    0,    1,    0, -186,
    0,   71,    0, -185,   48,  -18, -257,    0,    0,    0,
    0,    0,  109,    0,    0,    0,    0,    0,   49,   71,
   71,   71,   71,   71,   71,   71,   71,   71,   71,   71,
   71,   71,   71,   71,   71,   71,    0,    0,    0,    0,
    1,    0,    1,  136,   54,    0,    0,    0,    0,   71,
   49, -224, -224, -224, -189, -189, -189, -189, -189, -189,
 -217,    0, -217,    0,    0, -231, -231, -231,  217,  -12,
    0,    0,    0,  217,  -88,   49,    0,    0,  217,    0,
   49,    0,   16,    0,
};
final static short yyrindex[] = {                       110,
 -261,    0,  115,    0,    0,    0,    0, -239,    0,    0,
    0,   30,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -40,    0,  -11,    0,
    0,    0,    0,    0,  -49,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   76,    0,    0,
    0,    0,    0,    0,   42,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -7,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  370,  391,  411,  398,  420,  433,  446,  452,  466,
  146,    0,  175,    0,    0,  250,  262,  300,    0,    0,
    0,    0,    0,    0,  -46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -10,    0,    0,  116, -139,    0,  479,    0,    0,    0,
    0,   85,   87,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  117, -158,  -52,    0,  -37,  -53,    0,    0,
    0,    0,    0,    0,    0,   17,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=740;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
   43,   55,   63,   21,   99,   97,   33,   71,  100,   66,
  177,  176,  104,   17,    1,  180,  114,  115,   43,    9,
  182,  116,  117,  118,   66,  104,  105,   17,   46,   46,
   10,   18,   19,   20,   33,   12,  181,   64,   65,   14,
   32,  183,   15,   16,   74,  142,  104,   46,   33,   56,
   33,   57,  101,  129,  130,  131,  132,  133,   58,  103,
  129,  130,  131,  132,  133,  134,  135,  136,  130,    8,
  132,  133,   18,   19,   20,   66,  120,  121,  122,   69,
   70,   63,   72,   73,   43,   93,   55,   33,   63,  102,
  171,  137,  138,  145,  140,  129,  130,  131,  132,  133,
  134,  135,  136,  170,  143,  146,  147,  150,  184,    1,
   84,  173,  178,   46,    6,   97,   71,   32,   11,   67,
   97,   68,   13,  148,    0,   97,    0,    0,    0,    0,
   33,    0,   33,    0,    0,    0,    0,    0,    0,    0,
  139,    0,    0,    0,    0,    0,    0,    0,    0,  149,
    0,    0,    0,    0,    0,    0,    0,    0,   33,    0,
    0,   85,    0,   33,    0,    0,    0,    0,   33,    0,
    0,    0,    0,    0,    0,    0,  172,  120,  121,  122,
  123,  124,  125,  126,  127,  128,   84,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  129,  130,  131,  132,
  133,  134,  135,  136,   84,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   85,    0,   43,   43,   43,
    0,    0,   43,   43,   43,    0,    0,    0,    0,   54,
   62,    0,    0,   85,    0,    0,    0,   43,   84,   43,
   43,   43,   43,   43,   43,   43,   46,   46,   46,    0,
   43,   46,   46,   46,   98,   43,   32,    0,   18,   19,
   20,    0,    0,   22,   23,   24,   46,   85,   46,   46,
   46,   46,   46,   46,   46,    0,    0,    0,   25,   46,
   26,   27,   28,    0,   46,   29,   30,    8,    8,    8,
   86,    0,    8,    8,    8,    0,   31,    0,    0,   63,
   63,   63,   87,    0,   63,   63,   63,    8,   86,    8,
    8,    8,    0,    0,    8,    8,    0,    0,    0,   63,
   87,   63,   63,   63,    0,    8,   63,   63,   18,   19,
   20,    0,   75,   76,    0,   77,    0,   63,    0,   95,
   91,    0,   86,    0,    0,    0,   78,   79,    0,   80,
    0,    0,    0,    0,   87,    0,    0,   81,   91,    0,
    0,    0,    0,   82,   83,  120,  121,  122,  123,  124,
  125,  126,  127,  128,  120,  121,  122,  123,  124,  125,
  126,  127,  128,    0,  129,  130,  131,  132,  133,  134,
  135,  136,   91,  129,  130,  131,  132,  133,  134,  135,
  136,  120,  121,  122,  123,  124,  125,  126,  127,  128,
   81,   84,   84,   84,   84,   84,   84,   84,   84,   84,
  129,  130,  131,  132,  133,  134,  135,  136,   81,    0,
   84,   82,   84,    0,    0,   84,   84,   84,   75,    0,
   85,   85,   85,   85,   85,   85,   85,   85,   85,   82,
    0,   83,    0,    0,    0,    0,   75,    0,    0,   85,
   76,   85,   81,    0,   85,   85,   85,    0,    0,   83,
    0,    0,    0,   77,   18,   19,   20,    0,   76,   22,
   23,   24,    0,   82,    0,    0,   78,    0,    0,    0,
   75,   77,   79,    0,    0,    0,   26,   27,   28,    0,
    0,   29,   30,   83,   78,    0,   80,    0,    0,    0,
   79,    0,   76,    0,    0,   86,   86,   86,   86,   86,
   86,   86,   86,   86,   80,   77,    0,   87,   87,   87,
   87,   87,   87,   87,   87,   87,   94,    0,   78,   86,
   86,   86,    0,    0,   79,    0,    0,    0,    0,    0,
    0,   87,   87,   87,    0,    0,  108,  109,   80,  110,
  111,  112,  113,    0,    0,   91,   91,   91,   91,   91,
   91,   91,   91,   91,    0,    0,    0,    0,    0,    0,
  144,    0,    0,    0,    0,    0,    0,    0,    0,   91,
   91,   91,    0,    0,    0,    0,    0,    0,  152,  153,
  154,  155,  156,  157,  158,  159,  160,  161,  162,  163,
  164,  165,  166,  167,  168,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  175,    0,
    0,    0,    0,    0,    0,   81,   81,   81,   81,   81,
   81,   81,   81,   81,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   82,   82,   82,   82,
   82,   82,   82,   82,   82,    0,   75,   75,   75,   75,
   75,   75,    0,    0,    0,    0,   83,   83,   83,   83,
   83,   83,   83,   83,   83,    0,    0,    0,   76,   76,
   76,   76,   76,   76,    0,    0,    0,    0,    0,    0,
    0,   77,   77,   77,   77,   77,   77,    0,    0,    0,
    0,    0,    0,    0,   78,   78,   78,   78,   78,   78,
   79,   79,   79,   79,   79,   79,    0,    0,    0,    0,
    0,    0,    0,    0,   80,   80,   80,   80,   80,   80,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   58,   58,   14,   41,   59,   17,   45,   61,   59,
  169,  151,   59,  275,  265,  174,  285,  286,   59,  265,
  179,  290,  291,  292,   35,  283,  284,  289,   40,   41,
  265,  258,  259,  260,   45,  275,  176,  264,  265,  289,
   40,  181,   59,   59,   55,   98,   93,   59,   59,   40,
   61,   40,   63,  285,  286,  287,  288,  289,   40,   70,
  285,  286,  287,  288,  289,  290,  291,  292,  286,   40,
  288,  289,  258,  259,  260,  125,  266,  267,  268,  295,
  288,   40,   59,  123,  125,  261,   58,   98,   58,   40,
  143,   41,   41,  104,  125,  285,  286,  287,  288,  289,
  290,  291,  292,  141,  291,   58,  125,   59,   93,    0,
   40,   58,  125,  125,    0,  169,   41,  125,    3,   35,
  174,   35,    6,  107,   -1,  179,   -1,   -1,   -1,   -1,
  141,   -1,  143,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   41,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   41,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  169,   -1,
   -1,   91,   -1,  174,   -1,   -1,   -1,   -1,  179,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   41,  266,  267,  268,
  269,  270,  271,  272,  273,  274,   41,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  285,  286,  287,  288,
  289,  290,  291,  292,   59,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   41,   -1,  258,  259,  260,
   -1,   -1,  263,  264,  265,   -1,   -1,   -1,   -1,  286,
  286,   -1,   -1,   59,   -1,   -1,   -1,  278,   93,  280,
  281,  282,  283,  284,  285,  286,  258,  259,  260,   -1,
  291,  263,  264,  265,  291,  296,   40,   -1,  258,  259,
  260,   -1,   -1,  263,  264,  265,  278,   93,  280,  281,
  282,  283,  284,  285,  286,   -1,   -1,   -1,  278,  291,
  280,  281,  282,   -1,  296,  285,  286,  258,  259,  260,
   41,   -1,  263,  264,  265,   -1,  296,   -1,   -1,  258,
  259,  260,   41,   -1,  263,  264,  265,  278,   59,  280,
  281,  282,   -1,   -1,  285,  286,   -1,   -1,   -1,  278,
   59,  280,  281,  282,   -1,  296,  285,  286,  258,  259,
  260,   -1,  262,  263,   -1,  265,   -1,  296,   -1,  123,
   41,   -1,   93,   -1,   -1,   -1,  276,  277,   -1,  279,
   -1,   -1,   -1,   -1,   93,   -1,   -1,  287,   59,   -1,
   -1,   -1,   -1,  293,  294,  266,  267,  268,  269,  270,
  271,  272,  273,  274,  266,  267,  268,  269,  270,  271,
  272,  273,  274,   -1,  285,  286,  287,  288,  289,  290,
  291,  292,   93,  285,  286,  287,  288,  289,  290,  291,
  292,  266,  267,  268,  269,  270,  271,  272,  273,  274,
   41,  266,  267,  268,  269,  270,  271,  272,  273,  274,
  285,  286,  287,  288,  289,  290,  291,  292,   59,   -1,
  285,   41,  287,   -1,   -1,  290,  291,  292,   41,   -1,
  266,  267,  268,  269,  270,  271,  272,  273,  274,   59,
   -1,   41,   -1,   -1,   -1,   -1,   59,   -1,   -1,  285,
   41,  287,   93,   -1,  290,  291,  292,   -1,   -1,   59,
   -1,   -1,   -1,   41,  258,  259,  260,   -1,   59,  263,
  264,  265,   -1,   93,   -1,   -1,   41,   -1,   -1,   -1,
   93,   59,   41,   -1,   -1,   -1,  280,  281,  282,   -1,
   -1,  285,  286,   93,   59,   -1,   41,   -1,   -1,   -1,
   59,   -1,   93,   -1,   -1,  266,  267,  268,  269,  270,
  271,  272,  273,  274,   59,   93,   -1,  266,  267,  268,
  269,  270,  271,  272,  273,  274,   58,   -1,   93,  290,
  291,  292,   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,
   -1,  290,  291,  292,   -1,   -1,   78,   79,   93,   81,
   82,   83,   84,   -1,   -1,  266,  267,  268,  269,  270,
  271,  272,  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,
  102,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  290,
  291,  292,   -1,   -1,   -1,   -1,   -1,   -1,  120,  121,
  122,  123,  124,  125,  126,  127,  128,  129,  130,  131,
  132,  133,  134,  135,  136,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  150,   -1,
   -1,   -1,   -1,   -1,   -1,  266,  267,  268,  269,  270,
  271,  272,  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  266,  267,  268,  269,
  270,  271,  272,  273,  274,   -1,  269,  270,  271,  272,
  273,  274,   -1,   -1,   -1,   -1,  266,  267,  268,  269,
  270,  271,  272,  273,  274,   -1,   -1,   -1,  269,  270,
  271,  272,  273,  274,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  269,  270,  271,  272,  273,  274,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  269,  270,  271,  272,  273,  274,
  269,  270,  271,  272,  273,  274,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  269,  270,  271,  272,  273,  274,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=297;
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
"LABEL","PACKETREF","IDENTIFIER","TK_LSHIFT","TK_RSHIFT","TK_URSHIFT","CMP_EQ",
"CMP_NEQ","CMP_LT","CMP_LEQ","CMP_GT","CMP_GEQ","ASSIGN","CAST_TO_INT",
"CAST_TO_BOOLEAN","IF","TKDOLLAR","TK_MESSAGEID","TK_FAIL","TK_SWITCH",
"TK_CASE","TK_DEFAULT","TK_PLUS","TK_TIMES","TK_MINUS","TK_DIVIDE","TK_PERCENT",
"TK_AND","TK_OR","TK_XOR","TK_TILDE","TK_NOT","TK_EQ","TK_INTERROGATIONMARK",
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
"PacketDeclStart : PacketName TK_PERCENT INTEGER_LITERAL",
"PacketDeclStart : PacketName",
"Multiplicity : IDENTIFIER TK_TIMES",
"Multiplicity : INTEGER_LITERAL TK_TIMES",
"Multiplicity : TK_TIMES",
"Multiplicity : TK_PLUS",
"PacketName : IDENTIFIER",
"SimpleItem : LABEL",
"SimpleItem : PacketRef",
"SimpleItem : PacketRefList",
"SimpleItem : Variable",
"SimpleItem : VariableList",
"SimpleItem : ImplicitVariable",
"SimpleItem : Constant",
"SimpleItem : MessageId",
"SimpleItem : ChoiceStatement",
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
"ChoiceStatement : '(' Choice ')'",
"$$4 :",
"Choice : $$4 Item TK_OR Item",
"Choice : Choice TK_OR Item",
"MessageId : TK_MESSAGEID '(' STRING_LITERAL ')'",
"Variable : RawVariable",
"RawVariable : IDENTIFIER ':' INTEGER_LITERAL",
"ImplicitVariable : RawVariable TK_EQ '(' expression ')'",
"VariableList : RawVariableList",
"VariableList : RawVariableList TK_DIVIDE INTEGER_LITERAL",
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

//#line 340 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
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
//#line 674 "PDL2Parser.java"
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
//#line 79 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 2:
//#line 80 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 3:
//#line 81 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{}
break;
case 4:
//#line 85 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ if (!val_peek(2).sval.equals("start")) throw new PDLException("'start' expected"); pdldoc.setStartPacketName(val_peek(1).sval); }
break;
case 5:
//#line 89 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 8:
//#line 100 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:before ItemList */
                                           PDLPacketDeclImpl pdecl = (PDLPacketDeclImpl)val_peek(1).obj;
                                           bpush(pdecl);
                                           yyval.obj = pdecl;
                                           registerPacketDecl(pdecl); 
                                         }
break;
case 9:
//#line 109 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ /* action:after ItemList */
                                           bpop();
                                         }
break;
case 10:
//#line 113 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ registerPacketDecl((PDLPacketDeclImpl)val_peek(2).obj);  }
break;
case 11:
//#line 117 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ int padding = val_peek(0).ival;
                                           if(padding<1)
                                             yyerror("padding must >=1: "+padding);
                                              
                                           yyval.obj = new PDLPacketDeclImpl(val_peek(2).sval, padding); }
break;
case 12:
//#line 122 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLPacketDeclImpl(val_peek(0).sval, 1); /* default padding = 1 */ }
break;
case 13:
//#line 126 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).sval); }
break;
case 14:
//#line 127 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = new PDLMultiplicityImpl(val_peek(1).ival); }
break;
case 15:
//#line 128 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("*-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.NoneOrMany); */
                                     }
break;
case 16:
//#line 131 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyerror("+-multiplicity not implemented");
                                       /*$$ = new PDLMultiplicityImpl(PDLMultiplicityType.OneOrMany); */
                                     }
break;
case 18:
//#line 141 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.Label, val_peek(0).sval)); }
break;
case 29:
//#line 155 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.Fail)); }
break;
case 30:
//#line 159 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 31:
//#line 163 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLSwitchStatementImpl s = new PDLSwitchStatementImpl(epopFinal()); badd(s); bpush(s); }
break;
case 34:
//#line 172 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(false, val_peek(1).ival); badd(c); bpush(c); }
break;
case 35:
//#line 172 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 36:
//#line 173 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLCaseStatementImpl c = new PDLCaseStatementImpl(true, -1);  badd(c); bpush(c); }
break;
case 37:
//#line 173 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; bpop(); }
break;
case 38:
//#line 177 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(1).obj; }
break;
case 39:
//#line 181 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLChoiceImpl m = new PDLChoiceImpl(); badd(m); bpush(m); }
break;
case 40:
//#line 181 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 41:
//#line 182 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = val_peek(2).obj; }
break;
case 42:
//#line 186 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLInstructionImpl(PDLItemType.MessageId, val_peek(1).sval)); }
break;
case 43:
//#line 190 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 44:
//#line 194 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBitcount(val_peek(0).ival);
                                           yyval.obj=PDLVariableImpl.create(val_peek(2).sval, val_peek(0).ival); }
break;
case 45:
//#line 199 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLFunction function;
                                                 try { function = new PDLFunctionImpl(epopFinal()); } catch(IllegalArgumentException iae) {throw new PDLException(iae);}
                                                 badd(PDLVariableImpl.createImplicit((PDLVariable)val_peek(4).obj, function));  }
break;
case 46:
//#line 205 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(val_peek(0).obj); }
break;
case 47:
//#line 206 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLVariableImpl v = (PDLVariableImpl) val_peek(2).obj; v.setTerminal(val_peek(0).ival); badd(val_peek(2).obj); }
break;
case 48:
//#line 210 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj= PDLVariableImpl.createVariableList((PDLVariable)val_peek(0).obj, (PDLMultiplicity)val_peek(1).obj); }
break;
case 49:
//#line 214 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  badd(val_peek(0).obj); }
break;
case 50:
//#line 215 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ ((PDLConstantImpl)val_peek(0).obj).setMultiplicity((PDLMultiplicityImpl)val_peek(1).obj); badd(val_peek(0).obj); }
break;
case 51:
//#line 219 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLUtils.checkBounds(val_peek(2).ival, val_peek(0).ival);
                                                yyval.obj = new PDLConstantImpl(val_peek(2).ival, val_peek(0).ival); }
break;
case 52:
//#line 224 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(textToPacketRef(val_peek(0).sval)); }
break;
case 53:
//#line 228 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLPacketRefListImpl(textToPacketRef(val_peek(0).sval), (PDLMultiplicity)val_peek(1).obj)); }
break;
case 56:
//#line 237 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=val_peek(0).obj; }
break;
case 57:
//#line 238 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLOptionalImpl o = new PDLOptionalImpl(); badd(o); bpush(o); }
break;
case 58:
//#line 238 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 59:
//#line 242 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{  PDLConditionalImpl c = new PDLConditionalImpl((PDLCondition) val_peek(1).obj); badd(c); bpush(c); yyval.obj = c;  }
break;
case 60:
//#line 243 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 61:
//#line 246 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ }
break;
case 62:
//#line 247 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ badd(new PDLBlockItemImpl()); }
break;
case 63:
//#line 248 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ PDLBlockItemImpl b = new PDLBlockItemImpl(); badd(b); bpush(b); }
break;
case 64:
//#line 248 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ bpop(); }
break;
case 67:
//#line 257 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ try {yyval.obj = new PDLCompiledCondition((Expression)val_peek(0).obj);} catch(IllegalArgumentException iae) {throw new PDLException(iae);} }
break;
case 71:
//#line 267 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj = epopFinal(); }
break;
case 75:
//#line 275 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpEq  ( epop1(), epop() )); }
break;
case 76:
//#line 276 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpNeq ( epop1(), epop() )); }
break;
case 77:
//#line 277 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpLt  ( epop1(), epop() )); }
break;
case 78:
//#line 278 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpLEq ( epop1(), epop() )); }
break;
case 79:
//#line 279 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpGt  ( epop1(), epop() )); }
break;
case 80:
//#line 280 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.cmpGEq ( epop1(), epop() )); }
break;
case 81:
//#line 282 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.shl  ( epop1(), epop() )); }
break;
case 82:
//#line 283 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.shr  ( epop1(), epop() )); }
break;
case 83:
//#line 284 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.ushr ( epop1(), epop() )); }
break;
case 84:
//#line 286 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.add   ( epop1(), epop() )); }
break;
case 85:
//#line 287 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.minus ( epop1(), epop() )); }
break;
case 86:
//#line 288 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.and   ( epop1(), epop() )); }
break;
case 87:
//#line 289 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.or    ( epop1(), epop() )); }
break;
case 88:
//#line 291 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.mul   ( epop1(), epop() )); }
break;
case 89:
//#line 292 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.div   ( epop1(), epop() )); }
break;
case 90:
//#line 293 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.mod ( epop1(), epop() )); }
break;
case 91:
//#line 294 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.xor ( epop1(), epop() )); }
break;
case 92:
//#line 296 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.inv ( epop() )); }
break;
case 93:
//#line 297 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.inv ( epop() )); }
break;
case 94:
//#line 298 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.neg ( epop() )); }
break;
case 95:
//#line 300 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression( epop() )); }
break;
case 96:
//#line 304 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.fpush)); }
break;
case 97:
//#line 305 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.ipush, val_peek(0).ival )); }
break;
case 98:
//#line 306 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.bpush, val_peek(0).ival==1?true:false)); }
break;
case 99:
//#line 307 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.lpush, (String)val_peek(0).sval)); }
break;
case 100:
//#line 308 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(new Expression(Opcodes.vpush, (String)val_peek(0).sval)); }
break;
case 101:
//#line 312 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.castToInt((Expression) val_peek(0).obj)); }
break;
case 102:
//#line 313 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ epush(Expression.castToBoolean((Expression) val_peek(0).obj)); }
break;
case 103:
//#line 322 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{   epush(new Expression(val_peek(5).ival, 
                                           (Expression)val_peek(4).obj, (Expression)val_peek(3).obj, 
                                           (Expression)val_peek(2).obj, (Expression)val_peek(1).obj)); }
break;
case 104:
//#line 328 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.obj=epopFinal(); }
break;
case 105:
//#line 332 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.land; }
break;
case 106:
//#line 333 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lor; }
break;
case 107:
//#line 334 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lxor; }
break;
case 108:
//#line 335 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.lmul; }
break;
case 109:
//#line 336 "/home/christian/dev/nmedit/libs/jpdl2/format/pdl2.byaccj"
{ yyval.ival=Opcodes.ladd; }
break;
//#line 1179 "PDL2Parser.java"
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
