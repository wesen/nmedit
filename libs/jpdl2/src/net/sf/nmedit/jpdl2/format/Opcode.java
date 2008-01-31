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

public class Opcode implements Opcodes
{
    
    public static enum Type
    {
        None,
        Integer,
        Boolean
    };

    public static final int OPCODE_COUNT = 32;
    
    public static String describe(int opcode)
    {
        return "opcode <"+opcode+"> ("+Opcode.toString(opcode)+")";
    }
    
    public static String toString(int opcode)
    {
        switch (opcode)
        {
            case ipush: return "<integer>";
            case bpush: return "<boolean>";
            case lpush: return "<label>";
            case vpush: return "<variable>";
            case fpush: return "$";
            case ineg: return "-<integer>";
            case binv: return "!<boolean>";
            case iinv: return "~<integer>";
            case i2b: return "typecast (int)";
            case b2i: return "typecast (boolean)";
            case imul: return "*";
            case idiv: return "/";
            case imod: return "%";
            case iadd: return "+";
            case isub: return "-";
            case ishl: return "<<";
            case ishr: return ">>";
            case iushr: return ">>>";
            case ilt: return "<";
            case igt: return ">";
            case ileq: return "<=";
            case igeq: return ">=";
            case ieq: return "==";
            case ineq: return "!=";
            case beq: return "==";
            case bneq: return "!=";
            case iand: return "&";
            case ixor: return "^";
            case ior: return "|";
            case band: return "&";
            case bor: return "|";
            case bxor: return "^";
            case ladd: return "[+;...]";
            case lmul: return "[*;...]";
            case land: return "[&;...]";
            case lxor: return "[^;...]";
            case lor:  return "[|;...]";
            default: 
                throw invalidOpcodeError(opcode);
        }
    }
    
    public static boolean isOpcodeDefined(int opcode)
    {
        return 0<=opcode && opcode<OPCODE_COUNT;
    }
    
    public static int getArgumentCount(int opcode)
    {
        switch (opcode)
        {
            case ipush:
            case bpush:
            case lpush:
            case vpush:
            case fpush:
                return 0;
            case ineg:
            case binv:
            case iinv:
            case i2b:
            case b2i:
                return 1;
            case imul:
            case idiv:
            case imod:
            case iadd:
            case isub:
            case ishl:
            case ishr:
            case iushr:
            case ilt:
            case igt:
            case ileq:
            case igeq:
            case ieq:
            case ineq:
            case beq:
            case bneq:
            case iand:
            case ixor:
            case ior:
            case band:
            case bor:
            case bxor:
                return 2;
            case ladd:
            case lmul:
            case land:
            case lxor:
            case lor: 
                return 4; // start, end, size, field
            default: 
                throw invalidOpcodeError(opcode);
        }
    }
    
    public static Type getResultType(int opcode)
    {
        switch (opcode)
        {
            case bpush:
            case binv:
            case i2b:
            case beq:
            case bneq:
            case band:
            case bor:
            case bxor:
                return Type.Boolean;
            case ipush:
            case lpush:
            case vpush:
            case fpush:
            case ineg:
            case iinv:
            case b2i:
            case imul:
            case idiv:
            case imod:
            case iadd:
            case isub:
            case ishl:
            case ishr:
            case iushr:
            case ilt:
            case igt:
            case ileq:
            case igeq:
            case ieq:
            case ineq:
            case iand:
            case ixor:
            case ior:
            case ladd:
            case lmul:
            case land:
            case lxor:
            case lor: 
                return Type.Integer;
            default: 
                throw invalidOpcodeError(opcode);
        }
    }

    public static Type getArgumentType(int opcode, int argumentIndex)
    {
        if (argumentIndex<0||argumentIndex>=getArgumentCount(opcode))
            throw new IllegalArgumentException( 
                    "invalid argument index: "+argumentIndex
                    +" for "+describe(opcode));
        
        switch (opcode)
        {
            case lpush:
            case vpush:
            case fpush:
            case bpush:
            case ipush:
                // code should never be reached because opcode has not arguments
                return Type.None;

            case i2b:
            case ineg:
            case iinv:
            case imul:
            case idiv:
            case imod:
            case iadd:
            case isub:
            case ishl:
            case ishr:
            case iushr:
            case ilt:
            case igt:
            case ileq:
            case igeq:
            case ieq:
            case ineq:
            case iand:
            case ixor:
            case ior:
                return Type.Integer;
            case ladd:
            case lmul:
            case land:
            case lxor:
            case lor: 
                return Type.Integer;
            case b2i:
            case binv:
            case beq:
            case bneq:
            case band:
            case bor:
            case bxor:
                return Type.Boolean;
            default: 
                throw invalidOpcodeError(opcode);
        }
    }

    public static IllegalArgumentException invalidOpcodeError(int opcode)
    {
        return new IllegalArgumentException("unknown opcode: "+describe(opcode));
    }

    public static boolean checkResult(Expression e)
    {
        switch (e.getResultType())
        {
            case Boolean:
                return false;
            case Integer:
                return true;
            default:
                throw new RuntimeException("invalid result type: "+e.getResultType());
        }
    }
    
    public static boolean checkResult(Expression a, Expression b)
    {
        boolean type = checkResult(a);
        if (type!=checkResult(b))
            throw new IllegalArgumentException("incompatible return types: "+a.getResultType()+", "+b.getResultType());
        return type;
    }

    public static void checkType(Expression a, Expression b, boolean type)
    {
        if (checkResult(a)!=type || checkResult(b)!=type)
            throw new IllegalArgumentException("expected type: "+(type?"int":"boolean"));
    }

    public static void checkType(Expression a, boolean type)
    {
        if (checkResult(a)!=type)
            throw new IllegalArgumentException("expected type: "+(type?"int":"boolean"));
    }


    public static Expression mul(Expression a, Expression b)
    {
        return new Expression(imul, a, b);
    }

    public static Expression div(Expression a, Expression b)
    {
        return new Expression(idiv, a, b);
    }

    public static Expression mod(Expression a, Expression b)
    {
        return new Expression(imod, a, b);
    }

    public static Expression add(Expression a, Expression b)
    {
        return new Expression(iadd, a, b);
    }
    
    public static Expression castToInt(Expression a)
    {
        return new Expression(b2i, a);
    }
    
    public static Expression castToBoolean(Expression a)
    {
        return new Expression(i2b, a);
    }

    public static Expression minus(Expression a, Expression b)
    {
        return new Expression(isub, a, b);
    }

    public static Expression neg(Expression a)
    {
        return new Expression(ineg, a);
    }

    public static Expression inv(Expression a)
    {
        return new Expression(checkResult(a)?iinv:binv, a);
    }

    public static Object cmpEq(Expression a, Expression b)
    {
        return new Expression(checkResult(a,b) ? ieq : beq, a, b);
    }

    public static Object cmpNeq(Expression a, Expression b)
    {
        return new Expression(checkResult(a,b) ? ineq : bneq, a, b);
    }

    public static Expression xor(Expression a, Expression b)
    {
        return new Expression(checkResult(a,b) ? ixor : bxor, a, b);
    }

    public static Expression and(Expression a, Expression b)
    {
        return new Expression(checkResult(a,b) ? iand : band, a, b);
    }

    public static Expression or(Expression a, Expression b)
    {
        return new Expression(checkResult(a,b) ? ior : bor, a, b);
    }

    public static Expression cmpLt(Expression a, Expression b)
    {
        return new Expression(ilt, a, b);
    }

    public static Expression cmpGt(Expression a, Expression b)
    {
        return new Expression(ilt, a, b);
    }

    public static Expression cmpLEq(Expression a, Expression b)
    {
        return new Expression(ileq, a, b);
    }

    public static Expression cmpGEq(Expression a, Expression b)
    {
        return new Expression(igeq, a, b);
    }

  
    /**
     * for copy and paste:
        switch (opcode)
        {
            case ipush:
            case bpush:
            case lpush:
            case vpush:
            case fpush:
            case ineg:
            case binv:
            case iinv:
            case i2b:
            case b2i:
            case imul:
            case idiv:
            case imod:
            case iadd:
            case isub:
            case ishl:
            case ishr:
            case iushr:
            case ilt:
            case igt:
            case ileq:
            case igeq:
            case ieq:
            case ineq:
            case beq:
            case bneq:
            case iand:
            case ixor:
            case ior:
            case band:
            case bor:
            case bxor:
            case ladd:
            case lmul:
            case land:
            case lxor:
            case lor: 
            default: 
                throw invalidOpcodeError(opcode);
        }
     */
    
    
    
}
