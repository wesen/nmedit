/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Jun 14, 2006
 */

package net.sf.nmedit.jpdl;

/**
 * TODO implement test as JUnit test
 */
public class BitStreamTester
{

    public static void main( String[] args )
    {
        (new BitStreamTester()).runTests();
    }

    public void runTests()
    {
        testPattern(1, 1); //   (1)* pattern
        testPattern(2, 2); //  (10)* pattern
        testPattern(2, 3); // (010)* pattern
        testGetInt();
        testGetInt2();
        testRandomGetAndSet();
        testSetSize();
        testPositionGetSet();
    }
    
    void testPattern(long pattern, long pbits)
    {
        title("pattern fill");
        description(
              "1. new BitStream()\n"+
              "2. add 0..32+2 times bits from Pattern "+patternToString(pattern, pbits)+"\n"+
              "3. goto 1");
        expectedResult(
                "- BS should contain the specified number of bits\n"+
                "- BS filled with the specified pattern");
        for (int bits=0;bits<=32+2;bits++)
        {
            testPattern(bits, pattern, pbits);
        }
    }
    
    void fillPattern(BitStream bs, int bits, long pattern, long pbits )
    {
        int shift = ((int)pbits)-1;
        for (int i=1;i<=bits;i++)
        {
            int b = (int)((pattern >>> shift)&1);
            bs.append(b,1);
            shift--;
            if (shift<0) shift = ((int)pbits)-1;
        }
    }
    
    private String patternToString( long pattern, long pbits )
    {
        StringBuffer s = new StringBuffer(35);
        s.append('(');
        
        for (int i= ((int)pbits)-1;i>=0;i--)
        {
            s.append(Long.toString(((pattern>>>i)&1)));
        }
        
        s.append(")*");
        return s.toString();
    }

    void testPattern(int bits, long pattern, long pbits)
    {
        System.out.print("bits="+bits+": BS:");
        BitStream bs = new BitStream();
        fillPattern(bs, bits, pattern, pbits);
        /*
        int shift = ((int)pbits)-1;
        for (int i=1;i<=bits;i++)
        {
            int b = (int)((pattern >>> shift)&1);
            bs.append(b,1);
            shift--;
            if (shift<0) shift = ((int)pbits)-1;
        }
        */
        println(bs);
    }
    
    void testGetInt()
    {
        title("getInt(X) test");
        description(
            "tests what getInt(X) returns on an empty BitStream\n"+
            "for X=-2..32+2:\n"+
            "  getInt(X)");
        expectedResult(
                "- getInt(0) should return 0\n"+
                "- getInt(X!=0) should fail");
        for (int bits=-2;bits<=32+2;bits++)
        {
            System.out.print("getInt("+bits+")=");
            try
            {
                int result = (new BitStream()).getInt(bits);
                System.out.println("int("+result+")");
            }
            catch (Exception e)
            {
                System.out.println("error("+e+")");
            }
        }
    }
    
    void testGetInt2()
    {
        title("get/set test 2");
        
        BitStream bs = new BitStream();
        int pattern = 1<<2;
        int pbits = 3;
        fillPattern(bs, 64, pattern, pbits);
        
        bs.setPosition(pbits);
        if (bs.isAvailable(pbits) && bs.getInt(pbits)==pattern)
        {
            success();
        }
        else
        {
            failed();
        }
    }
    
    void testRandomGetAndSet()
    {
        title("Random Get and Set");
        int tests = 1000000;
        description(
            "Performs "+tests+" tests:\n"+
            "1. new BitStream()\n"+
            "2. data, bits = random(), random()\n"+
            "3. bs.append(data, bits)\n"+
            "4. expect(bs.getInt(bits)==data)"
        );
        
        for (int i=1;i<=tests;i++)
        {
            BitStream bs = new BitStream();
            int data1 = (int) (Math.random()*0xFFFFFFFF);
            int data2 = (int) (Math.random()*0xFFFFFFFF);
            final int bits1 = Long.bitCount(data1);
            final int bits2 = Long.bitCount(data2);

            bs.append((int)data1, bits1);
            bs.append((int)data2, bits2);

            data1 &= (1<<bits1)-1;
            data2 &= (1<<bits2)-1;
            
            final int read1 = bs.getInt(bits1);
            final int read2 = bs.getInt(bits2);
            
            boolean ok_result = (read1==data1) && (read2==data2) && (bs.getSize()==bits1+bits2);
          
            if (!ok_result)
            {
                System.out.println("Test["+i+"] failed: {");
                StringBuffer sb = new StringBuffer();

                sb.append("bits1="+bits1+"\n");
                sb.append("bits2="+bits2+"\n");
                sb.append("data1="+data1+" ("+Long.toBinaryString(data1)+"b)\n");
                sb.append("data2="+data2+" ("+Long.toBinaryString(data2)+"b)\n");
                sb.append("read1="+read1+"\n");
                sb.append("read2="+read2+"\n");
                sb.append("size(BitStream)="+bs.getSize());
                
                System.out.println(tab(sb.toString()));
                System.out.println("}");
                return;
            }
        }
        
        success();
    }
    
    void testSetSize()
    {
        title("setSize() test");
        description("tests setSize() behaviour:\n"+
                "for x=0..32+2:\n"+
                tab(
                    "new BitStream():\n"+
                    "append(0xFFFFFFFF, 32);\n"+
                    "append(0xFFFFFFFF, 32);\n"+
                    "setSize(32+x-16);"));
        
        expectedResult(
                "BitStream:\n"+
                "- 32+x times 1 (one)\n"+
                "- size(BitStream)==min(32+x,64)");
        
        for (int i=0;i<=32+2;i++)
        {
            BitStream bs = new BitStream();
            bs.append(0xFFFFFFFF, 32);
            bs.append(0xFFFFFFFF, 32);
            int x = 32+i-16;
            bs.setSize(x);
            //bs.append(0,32-i);
            
            System.out.print("x:"+x+" BS:");
            println(bs);
        }
        
    }
    
    void testPositionGetSet()
    {
        title("get/set position");
        
        BitStream bs = new BitStream();
        bs.append(0,32);
        final int setpos = 5;
        bs.setPosition(setpos);
        
        if (bs.getPosition()==setpos)
        {
            success();
        }
        else
        {
            failed();
        }
    }
    
    void title(String title)
    {
        System.out.println();
        StringBuffer hline = new StringBuffer();
        for (int i=1;i<=60;i++) hline.append('=');
        System.out.println(hline);
        System.out.println("["+title+"]");
        System.out.println();
    }
    
    void description(String description)
    {
        System.out.println("{ description:");
        System.out.println(tab(description));
        System.out.println("}");
    }
    
    void expectedResult(String result)
    {
        System.out.println("{ expected result:");
        System.out.println(tab(result));
        System.out.println("}");
    }
    
    String tab(String text)
    {
        return tab(text, "  ");
    }
    
    String tab(String text, String prefix)
    {
        return prefix+text.replaceAll("\\n", "\n"+prefix);
    }
    
    void println(BitStream bs)
    {
        print(bs);
        System.out.println();
    }

    void print(BitStream bs)
    {
        final char SEPARATOR = ' ';

        if (bs.getSize()==0)
        {
            print8(bs);
            System.out.print(SEPARATOR);
        }
        else
        {
            while (bs.isAvailable(1))
            {
                print8(bs);
                System.out.print(SEPARATOR);
            }
        }
        System.out.print(" /size(BitStream)="+bs.getSize());
    }

    void print8(BitStream bs)
    {
        for(int i=8;i>=1;i--) print1(bs);
    }

    void print1(BitStream bs)
    {
        System.out.print(bs.isAvailable(1) ? Integer.toString(bs.getInt(1)) : "_");
    }
    
    void success()
    {
        System.out.println("-> SUCCESS");
    }
    
    void failed()
    {
        System.out.println("-> failed");
    }
    
}




