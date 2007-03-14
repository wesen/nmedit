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

import java.util.HashMap;
import java.util.Map;

public class FormatterRegistry
{
    
    private Map<String, FormattingInstructionClass>
        instructions = new HashMap<String, FormattingInstructionClass>();
    
    private Map<String, Formatter> formatters = new HashMap<String,Formatter>();

    public FormatterRegistry()
    {
        install(new FormattingInstructionClass.FICOffset());
        install(new FormattingInstructionClass.FICRound());
        install(new FormattingInstructionClass.FICScale());
        install(new FormattingInstructionClass.FICStr());
    }
    
    public void install(FormattingInstructionClass fic)
    {
        instructions.put(fic.getFunctionName(), fic);
    }
    
    public void uninstall(FormattingInstructionClass fic)
    {
        instructions.remove(fic.getFunctionName());
    }

    public FormattingInstruction getFormattingInstruction(String functionName, String[] args)
    {
        FormattingInstructionClass fic = instructions.get(functionName);
        
        if  (fic == null)
            throw new IllegalArgumentException("function "+functionName+" not defined");
        
        return fic.getInstance(args);
    }
    
    public Formatter getFormatter(String name)
    {
        Formatter f = formatters.get(name);
        
        return f;
    }
    
    public void putFormatter(String name, Formatter f)
    {
        formatters.put(name, f);
    }
    
}
