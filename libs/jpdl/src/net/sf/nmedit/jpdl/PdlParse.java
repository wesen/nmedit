//### This file created by BYACC 1.8(/Java extension  1.11)
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



package net.sf.nmedit.jpdl;







public class PdlParse
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
//public class PdlParseVal is defined in PdlParseVal.java


String   yytext;//user variable to return contextual strings
PdlParseVal yyval; //used to return semantic vals from action routines
PdlParseVal yylval;//the 'lval' (result) I got from yylex()
PdlParseVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new PdlParseVal[YYSTACKSIZE];
  yyval=new PdlParseVal();
  yylval=new PdlParseVal();
  valptr=-1;
}
void val_push(PdlParseVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
PdlParseVal val_pop()
{
  if (valptr<0)
    return new PdlParseVal();
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
PdlParseVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new PdlParseVal();
  return valstk[ptr];
}
//#### end semantic value section ####
public final static short NUMBER=257;
public final static short IDENTIFIER=258;
public final static short PAD=259;
public final static short ASSIGN=260;
public final static short CHOOSE=261;
public final static short TIMES=262;
public final static short STOP=263;
public final static short OPTIONAL=264;
public final static short COMPARE=265;
public final static short NOT_COMPARE=266;
public final static short BIND=267;
public final static short SIZE=268;
public final static short END=269;
public final static short CONDITIONAL=270;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    3,    1,    2,    2,    4,    5,    5,    5,
    5,    6,    6,    6,    6,    6,
};
final static short yylen[] = {                            2,
    2,    0,    0,    7,    2,    0,    2,    5,    5,    1,
    0,    3,    5,    3,    7,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    1,    0,    3,    0,   10,    0,
    0,    0,    0,    0,    4,    5,    0,    0,    7,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   16,
    0,   12,   14,    8,    9,    0,    0,    0,   13,    0,
   15,
};
final static short yydgoto[] = {                          2,
    3,   11,    8,   12,   13,   19,
};
final static short yysindex[] = {                      -247,
 -246,    0, -247, -241,    0, -243,    0, -261,    0, -240,
 -250, -261, -252, -251,    0,    0, -258, -260,    0, -237,
 -236, -235, -233, -232, -231, -229, -239, -230, -238,    0,
 -242,    0,    0,    0,    0, -228, -226, -227,    0, -224,
    0,
};
final static short yyrindex[] = {                        34,
    0,    0,   34,    0,    0,    0,    0, -257,    0,    0,
    0, -257,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                        32,
    0,   25,    0,    0,    0,    0,
};
final static int YYTABLESIZE=37;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         11,
   11,   24,    9,   22,   17,   18,   25,   26,   10,   23,
    1,    6,    4,   20,   21,    6,    7,   14,   15,   27,
   28,   34,   29,   30,   37,   31,   32,   33,   38,   36,
   35,   39,   41,    2,    5,   40,   16,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        257,
  258,  262,  264,  262,  257,  258,  267,  268,  270,  268,
  258,  269,  259,  265,  266,  257,  260,  258,  269,  257,
  257,  261,  258,  257,  267,  258,  258,  257,  257,  268,
  261,  258,  257,    0,    3,  263,   12,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=270;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"NUMBER","IDENTIFIER","PAD","ASSIGN","CHOOSE","TIMES","STOP",
"OPTIONAL","COMPARE","NOT_COMPARE","BIND","SIZE","END","CONDITIONAL",
};
final static String yyrule[] = {
"$accept : protocol",
"protocol : packet protocol",
"protocol :",
"$$1 :",
"packet : IDENTIFIER PAD NUMBER ASSIGN $$1 matchers END",
"matchers : matcher matchers",
"matchers :",
"matcher : matcherOption matcherType",
"matcherOption : CONDITIONAL IDENTIFIER COMPARE NUMBER CHOOSE",
"matcherOption : CONDITIONAL IDENTIFIER NOT_COMPARE NUMBER CHOOSE",
"matcherOption : OPTIONAL",
"matcherOption :",
"matcherType : IDENTIFIER BIND IDENTIFIER",
"matcherType : IDENTIFIER TIMES IDENTIFIER BIND IDENTIFIER",
"matcherType : IDENTIFIER SIZE NUMBER",
"matcherType : NUMBER TIMES IDENTIFIER SIZE NUMBER STOP NUMBER",
"matcherType : NUMBER SIZE NUMBER",
};

//#line 95 "pdlparse.yy"

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
//#line 254 "PdlParse.java"
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
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
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
      if (yydebug) debug("reduce");
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
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 3:
//#line 43 "pdlparse.yy"
{
	  packetParser = protocol.newPacketParser(val_peek(3).sval, val_peek(1).ival);
	}
break;
case 8:
//#line 59 "pdlparse.yy"
{ condition = new Condition(val_peek(3).sval, val_peek(1).ival, false);
	  optional = false; }
break;
case 9:
//#line 63 "pdlparse.yy"
{ condition = new Condition(val_peek(3).sval, val_peek(1).ival, true);
	  optional = false; }
break;
case 10:
//#line 67 "pdlparse.yy"
{ condition = null; optional = true; }
break;
case 11:
//#line 69 "pdlparse.yy"
{ condition = null; optional = false; }
break;
case 12:
//#line 74 "pdlparse.yy"
{ packetParser.addPacketMatcher("", val_peek(2).sval, val_peek(0).sval,
                                        condition, optional); }
break;
case 13:
//#line 78 "pdlparse.yy"
{ packetParser.addPacketMatcher(val_peek(4).sval, val_peek(2).sval, val_peek(0).sval,
                                        condition, optional); }
break;
case 14:
//#line 82 "pdlparse.yy"
{ packetParser.addVariableMatcher(1, val_peek(2).sval, val_peek(0).ival, 0,
					  condition, optional); }
break;
case 15:
//#line 86 "pdlparse.yy"
{ packetParser.addVariableMatcher(val_peek(6).ival, val_peek(4).sval, val_peek(2).ival, val_peek(0).ival,
					  condition, optional); }
break;
case 16:
//#line 90 "pdlparse.yy"
{ packetParser.addConstantMatcher(val_peek(2).ival, val_peek(0).ival,
                                          condition, optional); }
break;
//#line 440 "PdlParse.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
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
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public PdlParse()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public PdlParse(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
