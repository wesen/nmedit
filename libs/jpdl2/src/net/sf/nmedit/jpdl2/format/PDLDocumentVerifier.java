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
package net.sf.nmedit.jpdl2.format;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.nmedit.jpdl2.PDLBlock;
import net.sf.nmedit.jpdl2.PDLDocument;
import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLItem;
import net.sf.nmedit.jpdl2.PDLMultiplicity;
import net.sf.nmedit.jpdl2.PDLPacketDecl;
import net.sf.nmedit.jpdl2.PDLPacketRef;
import net.sf.nmedit.jpdl2.PDLPacketRefList;
import net.sf.nmedit.jpdl2.PDLUtils;

public class PDLDocumentVerifier
{
    
    private PDLDocument doc;

    private boolean detectConditionalRecursion = true;
    private boolean conditionalRecursionDeteced = false;

    private int warningMessageCount = 0;
    
    public PDLDocumentVerifier(PDLDocument doc)
    {
        this.doc = doc;
    }
    
    public void verify() throws PDLException
    {
        // verify start packet
        if (doc.getStartPacketName()!=null && doc.getPacketDecl(doc.getStartPacketName())==null)
        {
            throw new PDLException("start packet not declared: "+doc.getStartPacketName());
        }
        
        // verify packets
        for (PDLPacketDecl packet: doc)
        {
            try
            {
                verifyPacket(packet);
            }
            catch (PDLException e)
            {
                throw new PDLException(e, packet);
            }
        }
        
        detectReferencesBeforeAssignment();
        detectRecursion();
        verifyPaths(); // test if each possible path contains a message() statement
        
    }

    private void verifyPaths()
    {
        // TODO Auto-generated method stub
        
    }

    private void detectRecursion() throws PDLException
    {
        Set<String> visited = new HashSet<String>();
        for (PDLPacketDecl packet: doc)
        {
            visited.add(packet.getName());
            detectRecursion(visited, packet, false);            
        }
    }

    private void detectRecursion(Set<String> visited, PDLBlock block, boolean conditionalPath) throws PDLException
    {
        Iterator<PDLItem> iter = block.iterator();
        while (iter.hasNext())
        {
            if (conditionalPath && conditionalRecursionDeteced)
            {
                // no need to examin conditional paths any further
                break;
            }
            
            PDLItem item = iter.next();
            switch (item.getType())
            {
            // no children
            case MessageId:
            case Constant:
            case ImplicitVariable:
            case Label:
            case Variable:
            case VariableList:
                break;
            // detect recursion
            case PacketRef:
            case PacketRefList:
            {
                PDLPacketRef packetRef = item.asPacketRef();
                if (visited.contains(packetRef.getPacketName()))
                {
                    // recursion detected
                    if (!conditionalPath)
                        error(item, "recursion detected");
                    
                    // path is conditional
                    conditionalRecursionDeteced = true;
                    
                    // conditional recursion
                    // no error in pdl2: error(item, "recursion detected");
                }
                else
                {
                    // not visited yet
                    visited.add(packetRef.getPacketName()); // remember visited packet
                    detectRecursion(visited, packetRef.getReferencedPacket(), false);
                    visited.remove(packetRef.getPacketName()); // remove visited packet
                }
                
                break;
            }
            // have children
            case Conditional:
            case Optional:
            {
                if (detectConditionalRecursion && !conditionalRecursionDeteced)
                {
                    detectRecursion(visited, (PDLBlock) item, true);
                }
                break;
            }
            // error: item is undefined
            default:
                throw new InternalError("unknown item type: "+item.getType());
            }
        
            
        }
    }

    private void detectReferencesBeforeAssignment()
    {
        // TODO Auto-generated method stub
        
    }

    public void verifyPacket(PDLPacketDecl packet) throws PDLException
    {
        verifyReferences(packet);
    }
    
    private static <A,B> Map<A,B> map(Map<A,B> map)
    {
        if (map == null)
            return new HashMap<A,B>();
        return map;
    }

    final String VARIABLE = "variable";
    final String LABEL = "label";
    
    public void verifyReferences(PDLPacketDecl packet) throws PDLException
    {
        Map<String, String> defined = null;
        Iterator<PDLItem> iter = PDLUtils.flatten(packet);
        while (iter.hasNext())
        {
            PDLItem item = iter.next();
            
            switch (item.getType())
            {
              case MessageId:
                  break;
              case Conditional:
                  break;
              case Constant:
              {
                  // check multiplicity
                  checkMultiplicity(defined, item, item.asConstant().getMultiplicity());
                  break;
              }
              case ImplicitVariable:
                  defined = define(item, defined, VARIABLE, item.asImplicitVariable().getName());
                  break;
              case Label:
                  defined = define(item, defined, LABEL, item.asLabel().getName());
                  break;
              case Optional:
                  break;
              case PacketRef:
              {
                  PDLPacketRef p = item.asPacketRef();
                  if (doc.getPacketDecl(p)==null)
                      error(item, "referenced packet does not exist: "+p.getPacketName());
                  
                  break;
              }
              case PacketRefList:
              {
                  PDLPacketRefList p = item.asPacketRefList();
                  if (doc.getPacketDecl(p)==null)
                      error(item, "referenced packet does not exist: "+p.getPacketName());
                  
                  // check multiplicity
                  checkMultiplicity(defined, item, p.getMultiplicity());
                  break;
              }
              case Variable:
                  defined = define(item, defined, VARIABLE, item.asVariable().getName());
                  break;
              case VariableList:
              {
                  // check multiplicity
                  checkMultiplicity(defined, item, item.asVariableList().getMultiplicity());
                  break;
              }
              default:
                  throw new InternalError("unknown item type: "+item.getType());
            }
            
        }
    }
    
    private void error(PDLItem item, String message) throws PDLException
    {
        throw new PDLException(item, message);
    }
    
    private void warning(PDLItem item, String message)
    {
        warningMessageCount++;
        System.err.println("["+warningMessageCount+"] "+item+": "+message);
    }

    private Map<String, String> define(PDLItem item, Map<String, String> defined,
            String type, String name) throws PDLException
    {
        defined = map(defined);
        if (defined.containsKey(name))
            throw new PDLException(item, "name already defined: "+name);
        defined.put(name, type);
        return defined;
    }

    private void checkMultiplicity(Map<String, String> defined,
            PDLItem item,
            PDLMultiplicity m) throws PDLException
    {
        if (m != null && m.getVariable() != null)
        {
            if (defined == null || !VARIABLE.equals(defined.get(m.getVariable())))
                throw new PDLException(item, "variable referenced before assignment:"+m.getVariable());
        }
    }

}
