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
package net.sf.nmedit.jpatch.formatter;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PType;
import net.sf.nmedit.jpatch.PTypes;

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
        
        PTypes<PType> types = descriptions.getType(args[0]);
        
        if (types == null)
            throw new IllegalArgumentException("type not found:"+args[0]);
        
        return new FIType(this, types);
    }

    private static class FIType extends FormattingInstruction
    {

        private PTypes type;

        protected FIType( FormattingInstructionClass fiClass, PTypes type )
        {
            super( fiClass );
            this.type = type;
        }
        
        public Object getValue(PParameter parameter, Object value)
        {
            if (value instanceof Integer)
            {
                PType t = type.getTypeById(((Integer)value).intValue());
                if (t != null)
                    return t.getName();
            }
            return value;
        }
        
    }
    
}
