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
package net.sf.nmedit.jpatch.js;

import org.mozilla.javascript.Function;

import net.sf.nmedit.jpatch.Formatter;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PParameterDescriptor;

public class JSFormatter implements Formatter
{

    private JSContext context;
    private Function f;
    private transient Object[] args;

    public JSFormatter(JSContext context, Function f)
    {
        this.context = context;
        this.f = f;
    }
    
    public String getString(PParameter parameter, int value)
    {
        return getString(value);
    }

    public String getString(PParameterDescriptor parameter, int value)
    {
        return getString(value);
    }

    private String getString(int value)
    {
        if (args == null)
            args = new Object[1];
        args[0] = value;
        Object result = f.call(context.getContext(), context.getScope(), context.getScope(), args);
        return String.valueOf(result);
    }

}
