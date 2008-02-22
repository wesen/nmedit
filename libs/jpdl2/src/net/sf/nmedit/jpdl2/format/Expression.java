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

import java.util.Collection;

import net.sf.nmedit.jpdl2.stream.BitStream;
import net.sf.nmedit.jpdl2.PDLParseContext;

/**
 * Arithmetic or Boolean expression.
 */
public class Expression implements Opcodes
{
    /**
     * Expression Type 
     */
    public static enum Type
    {
        /**
         * No type. Used to detect invalid situations.
         */
        None,
        /**
         * integer expression
         */
        Integer,
        /**
         * boolean expression
         */
        Boolean
    };

    /**
     * Number of defined opcodes.
     */
    public static final int OPCODE_COUNT = 32;
    
    /**
     * The opcode of this expression
     */
    public int opcode;
    
    /**
     * The arguments of this expression
     */
    public Expression[] args;

    /**
     * Expression has a string argument (variable or label name)
     */
    public String sval;

    /**
     * Constant argument.
     */
    public int ival;
    
    /**
     * Creates a copy of the specified expression.
     * 
     * @param src source expression
     */
    public Expression(Expression src)
    {
        this.args = src.args;
        this.opcode = src.opcode;
        this.sval = src.sval;
        this.ival = src.ival;
        validateExpression();
    }

    /**
     * Creates a new expression.
     *
     * @param opcode the operator
     * @param args the arguments
     * @throws IllegalArgumentException if the number of specified arguments
     * does not match the required number of arguments (opcode) or their
     * return type does not match the argument type of the operator (opcode).
     */
    public Expression(int opcode, Expression ... args)
    {
        if (args == null)
            throw new NullPointerException("args must not be null");
/*
        for (int i=0;i<args.length-1;i++)
            for (int j=i+1;j<args.length;j++)
                if (args[i] == args[j])
                    throw new IllegalArgumentException("eq "+args[i]+", "+args[j]);
        */
        this.opcode = opcode;
        this.args = args;
        validateExpression();
    }

    /**
     * Creates a new expression.
     * 
     * Accepts opcodes with a string argument:
     * {@link Opcodes#lpush} and
     * {@link Opcodes#vpush}.
     * 
     * @param opcode the operator
     * @param value the argument
     */
    public Expression(int opcode, String value)
    {
        this.opcode = opcode;
        this.sval = value;
        validateExpression();
    }

    /**
     * Creates a new expression.
     * 
     * Accepts opcodes with an integer argument.
     * 
     * @param opcode the operator
     * @param value the argument
     */
    public Expression(int opcode, int value)
    {
        this.opcode = opcode;
        this.ival = value;
        validateExpression();
    }

    /**
     * Creates a new expression.
     * 
     * Accepts opcodes with a boolean argument.
     * 
     * @param opcode the operator
     * @param value the argument
     */
    public Expression(int opcode, boolean value)
    {
        this(opcode, value?1:0);
    }
    
    /**
     * Adds the names of variables and labels on which
     * this expression and it's children depends to the
     * specified collection.
     * @param dst destination collection
     */
    public void collectDependencies(Collection<String> dst)
    {
        switch (opcode)
        {
            case vpush:
                dst.add(sval);
                break;
            case lpush:
                dst.add("@"+sval);
                break;
            default:
                if (args != null && args.length>0)
                {
                    for (int i=0;i<args.length;i++)
                        args[i].collectDependencies(dst);
                }
                break;
        }
    }

    public String describe()
    {
        return Expression.describe(opcode);
    }
    
    /**
     * Returns the result type of this expression.
     * @return the result type of this expression
     */
    public Type getResultType()
    {
        return Expression.getResultType(opcode);
    }

    /**
     * Validates the expression.
     * Ensures the arguments match the opcode specifications (number of arguments, return type).
     * 
     * Used internally, called by the constructors.
     * 
     * @throws IllegalArgumentException error in arguments 
     */
    private void validateExpression()
    {
        if (args != null)
        {
            if (args.length != Expression.getArgumentCount(opcode))
                throw new IllegalArgumentException("invalid argument count for "+
                        describe()+": "+args.length);
            
            for (int i=0;i<args.length;i++)
            {
                Expression e = args[i];
                if (e == null)
                    throw new IllegalArgumentException("argument list of "+describe()+" contains null-value");
            
                if (Expression.getArgumentType(opcode, i) != e.getResultType())
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
        return getSource();
    }

    private int left(PDLParseContext context, Integer fieldRegister)
    {
        return args[0].ev(context, fieldRegister);
    }

    private int right(PDLParseContext context, Integer fieldRegister)
    {
        return args[1].ev(context, fieldRegister);
    }
    
    private int b2i(boolean value)
    {
        return value ? 1 : 0;
    }
    
    private boolean i2b(int value)
    {
        return (value&1) == 1;
    }
    
    public int computeInt(PDLParseContext context)
    {
        return ev(context, null);
    }
    
    public boolean computeBoolean(PDLParseContext context)
    {
        // TODO stream=null case
        return i2b(ev(context, null));
    }
    
    private int ev(PDLParseContext c, Integer f)
    {
        if (c == null)
            throw new NullPointerException("context not specified");
        
        switch (opcode)
        {
            case ipush:
            case bpush: return ival;
            case lpush: return c.getLabel(sval);
            case vpush: return c.getPacket().getVariable(sval);
            case fpush:
                if (f == null)
                    throw new RuntimeException("field register not accessible");
                return f.intValue();
            case ineg: return -left(c, f);
            case binv: return 1-left(c, f); // 1-1 = 0, 1-0 = 1  => 1-(int)<boolean> == !<boolean>
            case iinv: return ~left(c, f);
            case i2b:  return left(c, f)&1;
            case b2i:  return left(c, f);
            case imul: return left(c, f)*right(c, f);
            case idiv: return left(c, f)/right(c, f);
            case imod: return left(c, f)%right(c, f);
            case iadd: return left(c, f)+right(c, f);
            case isub: return left(c, f)-right(c, f);
            case ishl: return left(c, f)<<right(c, f);
            case ishr: return left(c, f)>>right(c, f);
            case iushr: return left(c, f)>>>right(c, f);
            case ilt: return b2i(left(c, f)<right(c, f));
            case igt: return b2i(left(c, f)>right(c, f));
            case ileq: return b2i(left(c, f)<=right(c, f));
            case igeq: return b2i(left(c, f)>=right(c, f));
            case beq:
            case ieq: return b2i(left(c, f)==right(c, f));
            case bneq:
            case ineq: return b2i(left(c, f)!=right(c, f));
            case band:
            case iand: return left(c, f)&right(c, f);
            case bxor: return b2i(i2b(left(c, f))^i2b(right(c, f)));
            case ixor: return left(c, f)^right(c, f);
            case bor: return b2i(i2b(left(c, f))|i2b(right(c, f)));
            case ior: return left(c, f)|right(c, f);
            case ladd: 
            case lmul:
            case land:
            case lxor:
            case lor: 
                return evalListOperator(c, f);
            default: 
                throw Expression.invalidOpcodeError(opcode);
        }
    }

    private int evalListOperator(PDLParseContext c, Integer fieldRegister)
    {
        BitStream s = c.getBitStream();
        final int spos = s.getPosition();
        int result = 0;
        try
        {
            // TODO check start, end, size
            int start = args[0].ev(c, fieldRegister);
            int end = args[1].ev(c, fieldRegister);
            int size = args[2].ev(c, fieldRegister);
            Expression field = args[3];
            
            int fieldIndex = 0;
            
            while (start<end)
            {
                s.setPosition(start); // ensure the stream position is correct
                int newFieldValue = field.ev(c, s.getInt(size));
                
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
        if (Expression.getArgumentCount(e.opcode)==0)
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
            case iinv: return "~"+a(0);
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
            default: throw Expression.invalidOpcodeError(opcode);
        }
    }

    public static String describe(int opcode)
    {
        return "opcode <"+opcode+"> ("+Expression.toString(opcode)+")";
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
            case ilt:
            case igt:
            case ileq:
            case igeq:
            case ieq:
            case ineq:
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

    public static Expression cmpEq(Expression a, Expression b)
    {
        return new Expression(checkResult(a,b) ? ieq : beq, a, b);
    }

    public static Expression cmpNeq(Expression a, Expression b)
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
        return new Expression(igt, a, b);
    }

    public static Expression cmpLEq(Expression a, Expression b)
    {
        return new Expression(ileq, a, b);
    }

    public static Expression cmpGEq(Expression a, Expression b)
    {
        return new Expression(igeq, a, b);
    }

    public static Expression shl(Expression a, Expression b)
    {
        return new Expression(ishl, a, b);
    }

    public static Expression shr(Expression a, Expression b)
    {
        return new Expression(ishr, a, b);
    }

    public static Expression ushr(Expression a, Expression b)
    {
        return new Expression(iushr, a, b);
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
