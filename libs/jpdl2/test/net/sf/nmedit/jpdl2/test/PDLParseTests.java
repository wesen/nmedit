package net.sf.nmedit.jpdl2.test;

import java.io.StringReader;

import net.sf.nmedit.jpdl2.PDLDocument;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.format.PDL2Parser;

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

    @Test(expected=PDLException.class) // variable referenced before assignment
    public void constant_variableReferencedBeforeAssignment() throws PDLException
    {
        test("Packet := v * 0:8 v:8;");
    }

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
    
}
