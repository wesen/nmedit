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
package net.sf.nmedit.jpdl2.test;

import java.io.StringReader;

import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLMessage;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.PDLPacketParser;
import net.sf.nmedit.jpdl2.dom.PDLDocument;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.format.PDL2Parser;
import net.sf.nmedit.jpdl2.stream.BitStream;

import org.junit.Assert;
import org.junit.Test;

public class PDLParseTests
{

    public PDLDocument parse(String src) throws PDLException
    {
        PDL2Parser parser = new PDL2Parser(new StringReader(src));
        parser.parse();
        return parser.getDocument();
    }
    
    public void test(String src) throws PDLException
    {
        // test parsing
        PDLDocument doc = parse(src);
        
        // generate src from DOM

        /*
        StringBuilder s = new StringBuilder();
        PDLWriter w = new PDLWriter(s);
        w.setHeaderCommentEnabled(false);
        w.append(doc);
        String domSrc = s.toString();

        src = normalizePDLSource(src);
        domSrc = normalizePDLSource(domSrc);
        
        if (!src.equals(domSrc))
        {
            System.err.println("src: (normalized)\n"+src);
            System.err.println("gen: (normalized)\n"+domSrc);
            
            throw new RuntimeException("could not generate equivalent PDL source");
        }*/
    }
    
    private String normalizePDLSource(String src)
    {
        StringBuilder s = new StringBuilder();
        for (int i=0;i<src.length();i++)
        {
            char c = src.charAt(i);
            switch (c)
            {
                case ' ': case '\n': case '\r': case '\t':
                case '(': case ')': break;
                default: s.append(c);
            }
        }
        return s.toString();
    }

    // ************************************************************************************
    // packet declarations
    // ************************************************************************************
    
    @Test // empty file
    public void emptyFile() throws PDLException
    {
        test("");
    }

    @Test // empty packet declaration
    public void emptyPacket() throws PDLException
    {
        test("Packet := ;");
    }

    @Test(expected=PDLException.class) // packet with zero-padding
    public void packetWithZeroPadding() throws PDLException
    {
        test("Packet % 0 := ;");
    }
    
    @Test // packet declaration without padding
    public void packetWithoutPadding() throws PDLException
    {
        test("Packet := ;");
    }

    @Test(expected=PDLException.class) // packet exists
    public void packetExistsError() throws PDLException
    {
        test("Packet := ; Packet := ; ");
    }

    // ************************************************************************************
    // packet references
    // ************************************************************************************

    
    @Test // packet reference
    public void packetReference() throws PDLException
    {
        test("A := B$binding ; B := ;");
    }

    @Test(expected=PDLException.class) // referenced packet missing
    public void referencedPacketMissing() throws PDLException
    {
        test("A := B$binding ;");
    }
    
    @Test(expected=PDLException.class) // infinite recursion
    public void infiniteRecursion() throws PDLException
    {
        test("A := B$binding ; B := A$binding ;");
    }
    
    @Test() // conditional recursion
    public void conditionalRecursion() throws PDLException
    {
        test("A := B$binding ; B := v:8 if (v == 0) A$binding ;");
    }

    // ************************************************************************************
    // number parsing
    // ************************************************************************************

    @Test // number parsing: decimal
    public void decimalNumber() throws PDLException
    {
        test("Packet := 0:8 1:8 2:8 3:8 4:8 5:8 6:8 7:8 8:8 9:8 10:8 11:8 9876:32;");
    }

    @Test(expected=PDLException.class) // decimal number
    public void invalidDecimalNumber() throws PDLException
    {
        test("Packet := 00002:8;");
    }

    @Test // hexadecimal number
    public void hexadecimalNumber() throws PDLException
    {
        test("Packet := 0x01234567:32 0x89:32 0xabcdef:32 0xABCDEF:32;");
    }

    @Test(expected=PDLException.class) // hexadecimal number with more than 8 digits
    public void toolargeHexadecimalNumber() throws PDLException
    {
        test("Packet := 0x01234567abcdef:32;");
    }

    @Test // 32bit dual number
    public void dualNumber() throws PDLException
    {
        test("Packet := 10001000100010001000100010001000d:32;");
    }

    @Test(expected=NumberFormatException.class) // 33bit dual number, leading 1
    public void _33bitDualNumber1() throws PDLException
    {
        test("Packet := 110001000100010001000100010001000d:32;");
    }

    @Test(expected=NumberFormatException.class) // 33bit dual number, leading 0
    public void _33bitDualNumber0() throws PDLException
    {
        test("Packet := 010001000100010001000100010001000d:32;");
    }
    
    // ************************************************************************************
    // variables
    // ************************************************************************************

    @Test // variable
    public void variable() throws PDLException
    {
        test("Packet := variable:8;");
    }

    @Test(expected=PDLException.class) // variable with more than 32 bits
    public void variableWithMoreThan32Bits() throws PDLException
    {
        test("Packet := variable:33;");
    }

    // ************************************************************************************
    // variable lists
    // ************************************************************************************

    @Test // variable-list with constant multiplicity
    public void variableListWithConstantMultiplicity() throws PDLException
    {
        test("Packet := 4*variable:8;");
    }

    @Test // variable-list with variable multiplicity
    public void variableListWithVariableMultiplicity() throws PDLException
    {
        test("Packet := m:8 m*variable:8;");
    }

    @Test(expected=PDLException.class) // constant with more than 32 bits
    public void constantWithMoreThan32Bits() throws PDLException
    {
        test("Packet := 3:33;");
    }

    @Test(expected=PDLException.class) // big constant
    public void bigConstant() throws PDLException
    {
        test("Packet := 0xFF:1;");
    }

    // ************************************************************************************
    // labels
    // ************************************************************************************
    
    @Test // label
    public void label() throws PDLException
    {
        test("Packet := @label;");
    }
    
    // ************************************************************************************
    // constants
    // ************************************************************************************

    @Test // constant
    public void constant() throws PDLException
    {
        test("Packet := 0:8;");
    }

    /*
    @Test(expected=PDLException.class) // variable referenced before assignment
    public void constant_variableReferencedBeforeAssignment() throws PDLException
    {
        test("Packet := v * 0:8 v:8;");
    }*/

    // ************************************************************************************
    // names
    // ************************************************************************************

    @Test(expected=PDLException.class) // variable / label with same name
    public void nameAlreadyInUse() throws PDLException
    {
        test("Packet := v:8 @v;");
    }

    // ************************************************************************************
    // message id
    // ************************************************************************************

    @Test // packet with message id
    public void messageId() throws PDLException
    {
        test("Packet := messageId(\"themessageid\");");
    }
    // ************************************************************************************
    // anonym implicit variable
    // ************************************************************************************

    @Test
    public void anonymVarTest() throws PDLException
    {
        int a = 1;
        int b = 1;
        int c = (((a&0xF)<<4) | (b&0xF));

        Assert.assertTrue(isConditionTrue("anonym == c", "%anonym:8=(((a&0xF)<<4)|(b&0xF))", a, b, c));
        Assert.assertFalse(isConditionTrue("anonym != c", "%anonym:8=(((a&0xF)<<4)|(b&0xF))", a, b, c));
        
    }
    
    // ************************************************************************************
    // mutual exclusion
    // ************************************************************************************

    @Test(expected=PDLException.class)
    public void mutualExclusionNeverReached1() throws PDLException
    {
        test("Packet := (a:7 | b:8);");
    }

    @Test(expected=PDLException.class)
    public void mutualExclusionNeverReached2() throws PDLException
    {
        test("Packet := (a:7 | 1:8);");
    }

    @Test
    public void mutualExclusionReached1() throws PDLException
    {
        test("Packet := (b:8 | a:7);");
    }

    @Test
    public void mutualExclusionReached2() throws PDLException
    {
        test("Packet := (1:8 | a:7);");
    }

    @Test
    public void mutualExclusionConstantBeforeVariableOfSameSize() throws PDLException
    {
        test("Packet := (1:8 | a:8);");
    }

    @Test(expected=PDLException.class)
    public void mutualExclusionConstantAfterVariableOfSameSize() throws PDLException
    {
        test("Packet := (a:8|1:8);");
    }
    
    /*
     * unreachable code tests
     */
    @Test(expected=PDLException.class)
    public void unreachAbleCode1() throws PDLException
    {
        test("Packet := break v:8;");
    }
    
    @Test(expected=PDLException.class)
    public void unreachAbleCode2() throws PDLException
    {
        test("Packet := fail v:8;");
    }
    
    @Test(expected=PDLException.class)
    public void unreachAbleCode3() throws PDLException
    {
        test("Packet := {break} v:8;");
    }
    
    @Test(expected=PDLException.class)
    public void unreachAbleCode4() throws PDLException
    {
        test("Packet := {fail} v:8;");
    }

    @Test(expected=PDLException.class)
    public void unreachAbleCode5() throws PDLException
    {
        test("Packet := if(true) {break v:8};");
    }

    @Test(expected=PDLException.class)
    public void unreachAbleCode6() throws PDLException
    {
        test("Packet := if(true) {fail v:8};");
    }

    @Test(expected=PDLException.class)
    public void unreachAbleCode7() throws PDLException
    {
        test("Packet := switch(1) {default:{break v:8}};");
    }

    @Test(expected=PDLException.class)
    public void unreachAbleCode8() throws PDLException
    {
        test("Packet := switch(1) {default:{fail v:8}};");
    }

    @Test(expected=PDLException.class)
    public void unreachAbleCode9() throws PDLException
    {
        test("Packet := ?{break v:8};");
    }

    @Test(expected=PDLException.class)
    public void unreachAbleCode10() throws PDLException
    {
        test("Packet := ?{fail v:8};");
    }
    
    public void unreachAbleCodeNoError1() throws PDLException
    {
        test("Packet := if(true) {break} v:8;");
    }
    
    public void unreachAbleCodeNoError2() throws PDLException
    {
        test("Packet := if(true) {fail} v:8;");
    }

    public void unreachAbleCodeNoError3() throws PDLException
    {
        test("Packet := switch(1) {default:{break}} v:8;");
    }

    public void unreachAbleCodeNoError4() throws PDLException
    {
        test("Packet := switch(1) {default:{fail}} v:8;");
    }

    public void unreachAbleCodeNoError5() throws PDLException
    {
        test("Packet := ?break v:8;");
    }

    public void unreachAbleCodeNoError6() throws PDLException
    {
        test("Packet := ?fail  v:8;");
    }
    
    
    
    
    /*
     * operator precedence:
     * operator                                    |  associativity
     * ------------------------------------------------------------
     * + - unary plus, minus                       |  right
     * ~ bitwise NOT                               |
     * ! boolean (logical) NOT                     |
     * (type) type cast                            |
     * ------------------------------------------------------------      
     * * / % multiplication, division, remainder   |   left
     * + - addition, substraction                  |
     * << signed bit shift left                    |
     * >> signed bit shift right                   |
     * >>> unsigned bit shift right                |
     * < <= less than, less than or equal to       |
     * > >= greater than, greater than or equal to |
     * == equal to                                 |
     * != not equal to                             |
     * & bitwise AND                               |
     * & boolean (logical) AND                     |
     * ^ bitwise XOR                               |
     * ^ boolean (logical) XOR                     |
     * | bitwise OR                                |
     * |  boolean (logical) OR                     |
     * ------------------------------------------------------------
     */

    public boolean isConditionTrue(String condition, int a, int b, int c) throws PDLException
    {
        return isConditionTrue(condition, "", a, b, c);
    }
    
    public boolean isConditionTrue(String condition, String extra, int a, int b, int c) throws PDLException
    {
        final String OK_RESULT = "OK"; 
        String src = "start Packet; Packet := a:8 b:8 c:8 "+extra+" if("+condition+") { messageId(\""+OK_RESULT+"\") };";

        PDL2Parser parser = new PDL2Parser(new StringReader(src));
        try
        {
        parser.parse();
        }
        catch (PDLException e)
        {
            //System.out.println("error parsing:\n"+src);
            
            throw e;
        }
        PDLDocument doc = parser.getDocument();
        
        BitStream bs = new BitStream();
        bs.append(a, 8);
        bs.append(b, 8);
        bs.append(c, 8);
        PDLPacketParser packetParser = new PDLPacketParser(doc);
        PDLMessage message = packetParser.parseMessage(bs);

        return OK_RESULT.equals(message.getMessageId());
    }
    
    @Test(expected=PDLException.class)
    public void testConditionNoBooleanExpression1() throws PDLException
    {
        isConditionTrue("5", 0, 0, 0);
    }
    
    @Test(expected=PDLException.class)
    public void testConditionNoBooleanExpression2() throws PDLException
    {
        isConditionTrue("5<<2", 0, 0, 0);
    }
    
    @Test
    public void testBooleanExpr() throws PDLException
    {
        {
            int a = 3, b = 4, c = 5;
            Assert.assertTrue(isConditionTrue((a+(b*c))+"==(a+(b*c))", a, b, c));
            Assert.assertTrue(isConditionTrue(((a+b)*c)+"!=(a+(b*c))", a, b, c));
            Assert.assertTrue(isConditionTrue((a+(b*c))+"==(a+b*c)", a, b, c));
            Assert.assertTrue(isConditionTrue(((a+b)*c)+"!=(a+b*c)", a, b, c));;
        }
        {
            int a = 2, b = 8, c = 2;
            Assert.assertTrue(isConditionTrue("(a+b/c)==(a+(b/c))", a, b, c));

            Assert.assertTrue(isConditionTrue("(a+b/c)!=((a+b)/c)", a, b, c));
            Assert.assertTrue(isConditionTrue("(b/c+a)!=((a+b)/c)", a, b, c));
        }
        Assert.assertTrue(isConditionTrue("1<2", 0,0,0));
        Assert.assertFalse(isConditionTrue("1>2", 0,0,0));
        Assert.assertTrue(isConditionTrue("1<=2", 0,0,0));
        Assert.assertFalse(isConditionTrue("1>=2", 0,0,0));
        Assert.assertTrue(isConditionTrue("1<=1", 0,0,0));
        Assert.assertTrue(isConditionTrue("1>=1", 0,0,0));
        Assert.assertTrue(isConditionTrue("1==1", 0,0,0));
        Assert.assertFalse(isConditionTrue("1==2", 0,0,0));
        Assert.assertTrue(isConditionTrue("1!=2", 0,0,0));
        Assert.assertFalse(isConditionTrue("1!=1", 0,0,0));
        Assert.assertTrue(isConditionTrue("-1==-1", 0,0,0));
        Assert.assertTrue(isConditionTrue("1!=-1", 0,0,0));
        Assert.assertTrue(isConditionTrue("0xFfFfFfFf==(~0)", 0,0,0));
    }
    
    @Test
    public void testPrec() throws PDLException
    {
            int a = 3, b = 4, c = 5;
            Assert.assertTrue(isConditionTrue((a*b+c)+"==a*b+c", a, b, c));
            Assert.assertTrue(isConditionTrue((a<<b+c)+"==a<<b+c", a, b, c));
            Assert.assertTrue(isConditionTrue((c+a<<b)+"==c+a<<b", a, b, c));
            Assert.assertTrue(isConditionTrue((a&b|c)+"==a&b|c", a, b, c));
    }
    
    /**
     * inline packets 
     */
    @Test
    public void testInlinePacketRef() throws PDLException
    {
        String pdl = "start Start; Start:= a:8 b:8 Inline$$ ; Inline:= @lblEnd %inline:8=(1);";
        
        PDL2Parser parser = new PDL2Parser(new StringReader(pdl));
        parser.parse();
        PDLDocument doc = parser.getDocument();
        
        PDLPacketParser pp = new PDLPacketParser(doc);
        
        BitStream bs = new BitStream();
        bs.append(0, 8); // a
        bs.append(0, 8); // b
        
        PDLPacket packet = pp.parse(bs);
        
        Assert.assertTrue("if defined: anonym variable", packet.getAllVariables().contains("inline"));
        Assert.assertTrue("anonym variable value", packet.getVariable("inline")==1);
    }
 
    
    /**
     * String def
     */
    @Test
    public void testStringDef() throws PDLException
    {
        String pdl = "start Start; Start:= a:8 string:=\"TEXT\";";
        
        PDL2Parser parser = new PDL2Parser(new StringReader(pdl));
        parser.parse();
        PDLDocument doc = parser.getDocument();
        
        PDLPacketParser pp = new PDLPacketParser(doc);
        
        BitStream bs = new BitStream();
        bs.append(0, 8); // a
        
        PDLPacket packet = pp.parse(bs);
        
        Assert.assertTrue("if defined: string", packet.getAllStrings().contains("string"));
        Assert.assertTrue("string value", "TEXT".equals(packet.getString("string")));
    }
    
    public static String generateCodeForItem(PDLItemType type)
    {
        switch (type)
        {
            case AnonymousVariable: 
                return "%anonym=(4)";
            case Block:
                return "{}";
            case Choice:
                return "(v:7 | 0x1:1)";
            case Conditional:
                return "if (true) fail";
            case Constant:
                return "3*0xF:8";
            //case
            
            default:
                throw new InternalError();
        }
        
    }
    
    
}
