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

import net.sf.nmedit.jpdl2.bitstream.BitStream;
import net.sf.nmedit.jpdl2.PDLPacket;

public class Expression implements Opcodes
{
    
    public int opcode;
    public Expression[] args;
    public String sval;
    public int ival;

    public Expression(int opcode, Expression ... args)
    {
        if (args == null)
            throw new NullPointerException("args must not be null");
        this.opcode = opcode;
        this.args = args;
        validateExpression();
    }

    public Expression(int opcode, String value)
    {
        this.opcode = opcode;
        this.sval = value;
        validateExpression();
    }

    public Expression(int opcode, int value)
    {
        this.opcode = opcode;
        this.ival = value;
        validateExpression();
    }

    public Expression(int opcode, boolean value)
    {
        this(opcode, value?1:0);
    }
    
    public String describe()
    {
        return Opcode.describe(opcode);
    }
    
    public Opcode.Type getResultType()
    {
        return Opcode.getResultType(opcode);
    }

    private void validateExpression()
    {
        if (args != null)
        {
            if (args.length != Opcode.getArgumentCount(opcode))
                throw new IllegalArgumentException("invalid argument count for "+
                        describe()+": "+args.length);
            
            for (int i=0;i<args.length;i++)
            {
                Expression e = args[i];
                if (e == null)
                    throw new IllegalArgumentException("argument list of "+describe()+" contains null-value");
            
                if (Opcode.getArgumentType(opcode, i) != e.getResultType())
                    throw new IllegalArgumentException("argument for "+describe()
                            +" has the wrong type: "+args[i]);            
                
            }
        }
        
        // no argument case:
        switch (opcode)
        {
            case ipush: break;
            case bpush: 
            {
                if ((ival&1)!=ival) 
                  throw new IllegalArgumentException("not a boolean value: "+ival);
                break;
            }
            case vpush:
            case lpush: 
            {
                if (sval == null)
                {
                    throw new IllegalArgumentException("string value missing for "+describe());
                }
                break;
            }
            case fpush:
            {
                break;
            }
        }
        
    }

    public String toString()
    {
        return "Expr[description='"+describe()+"']";
    }

    private int left(BitStream stream, PDLPacket packet, int fieldRegister)
    {
        return args[0].ev(stream, packet, fieldRegister);
    }

    private int right(BitStream stream, PDLPacket packet, int fieldRegister)
    {
        return args[1].ev(stream, packet, fieldRegister);
    }
    
    private int b2i(boolean value)
    {
        return value ? 1 : 0;
    }
    
    private boolean i2b(int value)
    {
        return (value&1) == 1;
    }
    
    public int computeInt(BitStream s, PDLPacket p)
    {
        return ev(s, p, null);
    }
    
    public boolean computeBoolean(BitStream s, PDLPacket p)
    {
        // TODO stream=null case
        return i2b(ev(s, p, null));
    }
    
    private int ev(BitStream s, PDLPacket p, Integer f)
    {
        switch (opcode)
        {
            case ipush:
            case bpush: return ival;
            case lpush: return p.getLabel(sval);
            case vpush: return p.getVariable(sval);
            case fpush:
                if (f == null)
                    throw new RuntimeException("field register not accessible");
                return f.intValue();
            case ineg: return -left(s, p, f);
            case binv: return 1-left(s, p, f); // 1-1 = 0, 1-0 = 1  => 1-(int)<boolean> == !<boolean>
            case iinv: return -left(s, p, f);
            case i2b:  return left(s, p, f)&1;
            case b2i:  return left(s, p, f);
            case imul: return left(s, p, f)*right(s, p, f);
            case idiv: return left(s, p, f)/right(s, p, f);
            case imod: return left(s, p, f)%right(s, p, f);
            case iadd: return left(s, p, f)+right(s, p, f);
            case isub: return left(s, p, f)-right(s, p, f);
            case ishl: return left(s, p, f)<<right(s, p, f);
            case ishr: return left(s, p, f)>>right(s, p, f);
            case iushr: return left(s, p, f)>>>right(s, p, f);
            case ilt: return b2i(left(s, p, f)<right(s, p, f));
            case igt: return b2i(left(s, p, f)>right(s, p, f));
            case ileq: return b2i(left(s, p, f)<=right(s, p, f));
            case igeq: return b2i(left(s, p, f)>=right(s, p, f));
            case beq:
            case ieq: return b2i(left(s, p, f)==right(s, p, f));
            case bneq:
            case ineq: return b2i(left(s, p, f)!=right(s, p, f));
            case band:
            case iand: return left(s, p, f)&right(s, p, f);
            case bxor: return b2i(i2b(left(s, p, f))^i2b(right(s, p, f)));
            case ixor: return left(s, p, f)^right(s, p, f);
            case bor: return b2i(i2b(left(s, p, f))|i2b(right(s, p, f)));
            case ior: return left(s, p, f)|right(s, p, f);
            case ladd: 
            case lmul:
            case land:
            case lxor:
            case lor: 
                return evalListOperator(s, p, f);
            default: 
                throw Opcode.invalidOpcodeError(opcode);
        }
    }

    private int evalListOperator(BitStream s, PDLPacket p, Integer fieldRegister)
    {
        final int spos = s.getPosition();
        int result = 0;
        try
        {
            // TODO check start, end, size
            int start = args[0].ev(s, p, fieldRegister);
            int end = args[1].ev(s, p, fieldRegister);
            int size = args[2].ev(s, p, fieldRegister);
            Expression field = args[3];
            
            int fieldIndex = 0;
            
            while (start<end)
            {
                s.setPosition(start); // ensure the stream position is correct
                int newFieldValue = field.ev(s, p, s.getInt(size));
                
                if (fieldIndex == 0)
                {
                    result = newFieldValue;
                }
                else
                {
                    switch (opcode)
                    {
                        case ladd: result += newFieldValue; break;
                        case lmul: result *= newFieldValue; break;
                        case land: result &= newFieldValue; break;
                        case lxor: result ^= newFieldValue; break;
                        case lor:  result |= newFieldValue; break;
                    }
                }
                
                fieldIndex ++;
                start+=size;
            }
        }
        finally
        {
            s.setPosition(spos);
        }

        return result;
    }
    
    private String a(int index)
    {
        Expression e = args[index];
        if (Opcode.getArgumentCount(e.opcode)==0)
            return e.toString();
        else
            return "("+e+")";
    }
    
    public String getSource()
    {
        switch (opcode)
        {
            case ipush: return Integer.toString(ival);
            case bpush: return ival == 1 ? "true" : "false";
            case lpush: return "@"+sval;
            case vpush: return ""+sval;
            case fpush: return "$";
            case ineg: return "-"+a(0);
            case binv: return "!"+a(0);
            case iinv: return "-"+a(0);
            case i2b:  return "(boolean)"+a(0);
            case b2i:  return "(int)"+a(0);
            case imul: return a(0)+"*"+a(1);
            case idiv: return a(0)+"/"+a(1);
            case imod: return a(0)+"%"+a(1);
            case iadd: return a(0)+"+"+a(1);
            case isub: return a(0)+"-"+a(1);
            case ishl: return a(0)+"<<"+a(1);
            case ishr: return a(0)+">>"+a(1);
            case iushr: return a(0)+">>>"+a(1);
            case ilt: return a(0)+"<"+a(1);
            case igt: return a(0)+">"+a(1);
            case ileq: return a(0)+"<="+a(1);
            case igeq: return a(0)+">="+a(1);
            case beq:
            case ieq: return a(0)+"=="+a(1);
            case bneq:
            case ineq: return a(0)+"!="+a(1);
            case band:
            case iand: return a(0)+"&"+a(1);
            case bxor: 
            case ixor: return a(0)+"^"+a(1);
            case bor: 
            case ior: return a(0)+"|"+a(1);
            case ladd: return "[+;"+args[0]+";"+args[1]+";"+args[2]+";"+args[3]+"]";
            case lmul: return "[*;"+args[0]+";"+args[1]+";"+args[2]+";"+args[3]+"]";
            case land: return "[&;"+args[0]+";"+args[1]+";"+args[2]+";"+args[3]+"]";
            case lxor: return "[^;"+args[0]+";"+args[1]+";"+args[2]+";"+args[3]+"]";
            case lor:  return "[|;"+args[0]+";"+args[1]+";"+args[2]+";"+args[3]+"]";
            default: throw Opcode.invalidOpcodeError(opcode);
        }
    }
    
}
