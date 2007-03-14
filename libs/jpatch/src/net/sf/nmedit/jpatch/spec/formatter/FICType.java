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

import net.sf.nmedit.jpatch.Parameter;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jpatch.spec.Type;

public class FICType extends FormattingInstructionClass
{

    private ModuleDescriptions descriptions;

    public FICType(ModuleDescriptions descriptions)
    {
        super("type");
        this.descriptions = descriptions;
    }

    @Override
    public FormattingInstruction getInstance( String[] args )
    {
        checkArgs(this, args, 1);
        
        Type type = descriptions.getType(args[0]);
        
        if (type == null)
            throw new IllegalArgumentException("type not found:"+args[0]);
        
        return new FIType(this, type);
    }

    private static class FIType extends FormattingInstruction
    {

        private Type type;

        protected FIType( FormattingInstructionClass fiClass, Type type )
        {
            super( fiClass );
            this.type = type;
        }
        
        public Object getValue(Parameter parameter, Object value)
        {
            if (value instanceof Integer)
            {
                String name = type.getValue(((Integer)value).intValue());
                if (name != null)
                    return name;
            }
            return value;
        }
        
    }
    
}
