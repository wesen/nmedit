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

import net.sf.nmedit.jpdl.BitStream;
import net.sf.nmedit.jpdl2.PDLDocument;
import net.sf.nmedit.jpdl2.PDLWriter;
import net.sf.nmedit.jpdl2.impl.PDLConstantImpl;
import net.sf.nmedit.jpdl2.impl.PDLDocumentImpl;
import net.sf.nmedit.jpdl2.impl.PDLFunctionRefImpl;
import net.sf.nmedit.jpdl2.impl.PDLImplicitVariableImpl;
import net.sf.nmedit.jpdl2.impl.PDLLabelImpl;
import net.sf.nmedit.jpdl2.impl.PDLPacketDeclImpl;
import net.sf.nmedit.jpdl2.impl.XOR7B8;
import net.sf.nmedit.jpdl2.parser.PDLBitstreamParser;
import net.sf.nmedit.jpdl2.parser.PDLParseException;

public class Test
{

    static PDLDocument createDoc1()
    {
        PDLDocumentImpl doc = new PDLDocumentImpl();
        PDLPacketDeclImpl packet = new PDLPacketDeclImpl("ChecksumTest", 1);
        doc.add(packet);

        packet.add(new PDLLabelImpl("lblStart"));
        packet.add(new PDLConstantImpl(0, 8));
        packet.add(new PDLConstantImpl(1, 8));
        packet.add(new PDLConstantImpl(2, 8));
        packet.add(new PDLConstantImpl(3, 8));
        packet.add(new PDLLabelImpl("lblEnd"));
        packet.add(new PDLImplicitVariableImpl("checksum", 8, new PDLFunctionRefImpl("lblStart", "lblEnd", "XOR7B8")));
        
        return doc;
    }
    
    public static void main(String[] args) throws PDLParseException 
    {
        String SEPARATOR = "*********************************";

        System.out.println(SEPARATOR);
        
        PDLDocument doc = createDoc1();
        System.out.println(PDLWriter.toString(doc));

        PDLBitstreamParser parser = new PDLBitstreamParser();
        parser.defineFunction("XOR7B8", new XOR7B8());
        
        System.out.println(SEPARATOR);

        BitStream bs = new BitStream();
        bs.append(0,8);
        bs.append(1,8);
        bs.append(2,8);
        bs.append(3,8);
        bs.append((0+1+2+3)&0x7f,8); // checksum

        System.out.println("parsing ...");
        parser.parse(bs, doc, "ChecksumTest");
        System.out.println("bitstream parsed.");

        System.out.println(SEPARATOR);
        bs.clear();
        
        bs.append(0,8);
        bs.append(1,8);
        bs.append(2,8);
        bs.append(3,8);
        bs.append((0+1+2+3+1)&0x7f,8); // wrong checksum

        System.out.println("parsing ...");
        parser.parse(bs, doc, "ChecksumTest");
        System.out.println("bitstream parsed.");
    }

}
