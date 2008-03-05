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
package net.sf.nmedit.jpdl2.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.nmedit.jpdl2.PDLException;
import net.sf.nmedit.jpdl2.PDLPacket;
import net.sf.nmedit.jpdl2.dom.PDLBlock;
import net.sf.nmedit.jpdl2.dom.PDLItem;
import net.sf.nmedit.jpdl2.dom.PDLItemType;
import net.sf.nmedit.jpdl2.dom.PDLMultiplicity;
import net.sf.nmedit.jpdl2.dom.PDLPacketRef;

public class PDLUtils
{

    public static String toHexadecimal( int[] b )
    {
        if (b.length == 0)
            return "[]";
        
        StringBuilder sb = new StringBuilder(b.length*3+1);
        sb.append('[');
        sb.append(Integer.toHexString(b[0]).toUpperCase());
        for (int i=1;i<b.length;i++)
        {
            sb.append(' ');
            sb.append(Integer.toHexString(b[i]).toUpperCase());
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toHexadecimal( byte[] b )
    {
        if (b.length == 0)
            return "[]";
        
        StringBuilder sb = new StringBuilder(b.length*3+1);
        sb.append('[');
        sb.append(toHexadecimal(b[0]));
        for (int i=1;i<b.length;i++)
        {
            sb.append(' ');
            sb.append(toHexadecimal(b[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String toHexadecimal(byte b)
    {
        int unsignedbyte = (int)(b&0xFF);
        return (unsignedbyte<=0xF ? "0" : "") + Integer.toHexString(unsignedbyte).toUpperCase();
    }
    
    private static void toString(PDLPacket packet, StringBuilder sb, final int depth)
    {
        sb.append(packet.getName());
        if (packet.getBinding() != null)
            sb.append("$"+packet.getBinding());
        sb.append("{");

        List<String> members;
        
        int memberCount = 0;
        members = packet.getAllVariables();
        memberCount += members.size();
        if (!members.isEmpty())
        {
            for (String variable: members) sb.append(variable+"="+packet.getVariable(variable)+";");
        }
        members = packet.getAllStrings();
        memberCount += members.size();
        if (!members.isEmpty())
        {
            for (String s: members) 
            {
                sb.append(s+":=\""+packet.getString(s)+"\";");
            }
        }
        members = packet.getAllVariableLists();
        memberCount += members.size();
        if (!members.isEmpty())
        {
            if (memberCount>1) sb.append("\n  ");
            for (String vlist: members) sb.append(variableListToString(vlist, packet)+";");
        }
        members = packet.getAllPacketLists();
        memberCount += members.size();
        if (!members.isEmpty())
        {
            if (memberCount>2) sb.append("\n  ");
            for (String plist: members) 
            {
                sb.append(plist+"=[\n  ");
                PDLPacket[] list = packet.getPacketList(plist);
                for (PDLPacket p: list)
                    toString(p, sb, depth+1);
                sb.append("]\n");
            }
        }
        members = packet.getAllPackets();
        memberCount += members.size();
        if (!members.isEmpty())
        {
            if (memberCount>2) sb.append("\n  ");
            for (String p: members) 
            {
                toString(packet.getPacket(p), sb, depth+1);
            }
        }
        
        sb.append("}");
        if (memberCount>2)
            sb.append("\n  ");
    }

    private static String variableListToString(String vlist, PDLPacket packet)
    {
        String defaultResult =  vlist+"[]";
        int[] data = packet.getVariableList(vlist);
        
        if (data.length<=16)
        {
            StringBuilder sb = new StringBuilder();
            StringBuilder ints = new StringBuilder();
            for (int i=0;i<data.length;i++)
            {
                if (!Character.isDefined((char)data[i]))
                    return defaultResult;
                sb.append((char)data[i]);
                if (i>0) ints.append(", ");
                ints.append(Integer.toString(data[i]));
            }
            return "[\""+sb+"\"|"+ints+"]";
        }
        return defaultResult;
    }

    public static String toString(PDLPacket packet)
    {
        StringBuilder sb = new StringBuilder();
        toString(packet, sb, 0);
        return sb.toString();
    }

    public static int parseHex(String s)
    {
      if (s.length()<3 || s.charAt(0)!='0' || s.charAt(1)!='x')
        throw new NumberFormatException("not a hexadecimal number: "+s);
      int val = 0;
      for (int i=2;i<s.length();i++)
      {
          val*=16;
          char c = s.charAt(i);
          if ('0'<=c && c<='9') val+=(c-'0');
          else if ('a'<=c && c<='f') val+=(c-'a'+10);
          else if ('A'<=c && c<='F') val+=(c-'A'+10);
          else throw new NumberFormatException("not a hexadecimal number: "+s);
      }
      return val;  
    }
    
    public static int parseDual(String s)
    {
      if (s.length()<2 || Character.toLowerCase(s.charAt(s.length()-1))!='d')
        throw new NumberFormatException("not a dual number: "+s);
      
      if (s.length()>33)
          throw new NumberFormatException("number has more than 32 digits:"+s);
      
      int val = 0;
      for (int i=0;i<s.length()-1;i++)
      {
        val = val << 1;
        switch(s.charAt(i))
        {
          case '1': val |= 1; break;
          case '0': break;
          default: throw new NumberFormatException("not a dual number: "+s);
        }
      }
      return val;
    }
    
    public static void checkBitcount(int bitcount) throws PDLException
    {
        if (bitcount<0 || bitcount>32)
            throw new PDLException("size must be in set [0,1,..,32]: "+bitcount);
    }
    
    public static void checkBounds(int value, int bitcount) throws PDLException
    {
        PDLUtils.checkBitcount(bitcount);
        int mask = 0xFfFfFfFf >>> (32-bitcount);
        
        if (value != (value&mask))
            throw new PDLException("value requires more than "+bitcount+" bits: "+value);
    }
    
    public static String getBinding(PDLPacketRef packetReference)
    {
        String binding = packetReference.getBinding();
        if (binding == null)
            binding = packetReference.getPacketName();
        return binding;
    }

    public static int getMinMultiplicity(PDLMultiplicity multiplicity)
    {
        if (multiplicity == null)
            return 1;
        if (multiplicity.getConstant()>=0)
            return multiplicity.getConstant();
        else
            return 0;
    }

    public static int getMultiplicity(PDLPacket context, PDLMultiplicity multiplicity)
    {
        if (multiplicity == null)
            return 1;
        if (multiplicity.getConstant()>=0)
            return multiplicity.getConstant();
        else
            return context.getVariable(multiplicity.getVariable());
    }

    
    public static <T extends PDLItem> Iterator<T> filter(final PDLItemType type, final Iterator<T> iter)
    {
        return new Iterator<T>()
        {
            
            T next = null;
            
            private void align()
            {
                while (next == null && iter.hasNext())
                {
                    T i = iter.next();
                    if (i.getType() == type)
                        next = i;
                }
            }

            public boolean hasNext()
            {
                align();
                return next != null;
            }

            public T next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                T t = next;
                next = null;
                return t;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
            
        };
    }
    
    public static Iterator<PDLItem> flatten(final PDLBlock items)
    {
        return new Iterator<PDLItem>()
        {
            
            List<Iterator<PDLItem>> list;
            Iterator<PDLItem> iter = items.iterator();
            
            
            public boolean hasNext()
            {
                return iter.hasNext() || (list != null && !list.isEmpty());
            }

            public PDLItem next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();
                
                
                PDLItem item;
                
                if (!iter.hasNext())
                {
                    iter = list.remove(0);
                }
                
                item = iter.next();

                switch (item.getType())
                {
                    // no children
                    case MessageId:
                    case Constant:
                    case ImplicitVariable:
                    case Label:
                    case Variable:
                    case VariableList:
                    case PacketRef:
                    case PacketRefList:
                    case Fail:
                        break;
                    // children but not a sequence
                    case Choice:
                    case SwitchStatement:
                        break;
                    // have children
                    case Block:
                    case Conditional:
                    case Optional:
                    {
                        PDLBlock block = (PDLBlock) item;
                        if (block.getItemCount()>0)
                        {
                            if (list == null)
                              list = new ArrayList<Iterator<PDLItem>>();
                            Iterator<PDLItem> iter = block.iterator();
                            list.add(iter);
                        }
                        break;
                    }
                    // error: item is undefined
                    default:
                        PDLUtils.unknownItemTypeError(item.getType());
                        break;
                }
                return item;
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
          
            
            
        };
    }
    
    public static void unknownItemTypeError(PDLItem item)
    {
        throw new InternalError("unknown item type: "+item.getType()+"; item: "+item);
    }
    
    public static void unknownItemTypeError(PDLItemType type)
    {
        throw new InternalError("unknown item type: "+type);
    }
    
}
