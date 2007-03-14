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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.spec.formatter;

public abstract class FormattingInstructionClass
{

    private String functionName;

    public FormattingInstructionClass(String functionName)
    {
        this.functionName = functionName;
    }
    
    public String getFunctionName()
    {
        return functionName;
    }
    
    public String toString()
    {
        return "function "+getFunctionName();
    }
    
    public abstract FormattingInstruction getInstance(String[] args);
    
    protected static void checkArgs(FormattingInstructionClass fic, String[] args, int expected)
    {
        if (args.length != expected)
            throw new IllegalArgumentException(fic+": invalid number of arguments");
    }

    public static class FICOffset extends FormattingInstructionClass
    {

        public FICOffset()
        {
            super("offset");
        }

        @Override
        public FormattingInstruction getInstance( String[] args )
        {
            checkArgs(this, args, 1);
            double offset = Double.parseDouble(args[0]);
            return new FIOffset(this, offset);
        }
    }
    
    public static class FICScale extends FormattingInstructionClass
    {

        public FICScale()
        {
            super("scale");
        }

        @Override
        public FormattingInstruction getInstance( String[] args )
        {
            checkArgs(this, args, 1);
            double factor = Double.parseDouble(args[0]);
            return new FIScale(this, factor);
        }
    }
    
    public static class FICRound extends FormattingInstructionClass
    {

        public FICRound()
        {
            super("round");
        }

        @Override
        public FormattingInstruction getInstance( String[] args )
        {
            checkArgs(this, args, 1);
            int base = Integer.parseInt(args[0]);
            return new FIRound(this, base);
        }
    }
    
    public static class FICStr extends FormattingInstructionClass
    {

        public FICStr()
        {
            super("str");
        }

        @Override
        public FormattingInstruction getInstance( String[] args )
        {
            checkArgs(this, args, 1);
            return new FIStr(this, args[0]);
        }
    }
    
}
