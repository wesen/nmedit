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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

public class JSContext
{
    
    private Context cx;
    private Scriptable scope;
    
    public JSContext()
    {
        super();
        initialize();
    }

    public Context getContext()
    {
        return cx;
    }
    
    public Scriptable getScope()
    {
        return scope;
    }
    
    private void initialize()
    {
        cx = Context.enter();
        cx.setOptimizationLevel(9);
        scope = cx.initStandardObjects();
    }

    public Script addScript(File source) throws IOException
    {
        return addScript(source.toURI().toURL());
    }
    
    public Script addScript(URL source) throws IOException
    {
        Reader r = new BufferedReader(new InputStreamReader(source.openStream()));
        try
        {
            return addScript(r, source.toString());
        }
        finally
        {
            r.close();
        }      
    }

    public Script addScript(String source, String sourceName)
    {
        Script s = cx.compileString(source, sourceName, 0, null);
        exec(s);
        return s;
    }
    
    public Script addScript(Reader in, String sourceName) throws IOException
    {
        Script s = cx.compileReader(in, sourceName, 0, null);
        exec(s);
        return s;
    }
    
    private void exec(Script script)
    {
        script.exec(cx, scope);
    }

    protected void finalize() throws Throwable
    {
        try
        {
        Context.exit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.finalize();
    }
    

    public Function getFormatterFunction(String src)
    {
        Function f = getFunction(src);
        return  (f != null) ? f : getBodyFunction(src);
    }
    
    private Map<String, Function> functionBodyMap = new HashMap<String, Function>();
    
    private Function getBodyFunction(String body)
    {
        String norm = normalizeFunctionBody(body);
        Function f = functionBodyMap.get(norm);
        if (f == null)
        {
            f = compileFunctionBody(body);
            functionBodyMap.put(norm, f);
        }
        return f;
    }
    
    private String normalizeFunctionBody(String body)
    {
        body = body.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<body.length();i++)
        {
            char c = body.charAt(i);
            if (!Character.isWhitespace(c))
                sb.append(c);
        }
        return sb.toString();
    }

    private Function compileFunctionBody(String body)
    {
        try
        {
            return cx.compileFunction(scope, "function(value) {return "+body+";}", "sourceName", 0, null);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private Function getFunction(String name)
    {
        Object f = scope.get(name, scope);
        return f == Scriptable.NOT_FOUND ? null : ((Function)f);
    }
/*
    public void test() throws IOException
    {
        test(cx);
    }

    String scriptFile()
    {
        return "/home/christian/CVS-Arbeitsbereich/nmedit/libs/nordmodular/data/module-descriptions/nmformat.js";
    }
    
    private void test(Context cx) throws IOException
    {
        addScript(new File(scriptFile()));

        Function f = getFunction("fmtSemitones");
        
        if (f != null)
        {
         //   System.out.println(Context.toString(f));

            Object[] args = new Object[1];
            
            for (int i=0;i<127;i++)
            {
                args[0] = i;
                Object result = f.call(cx, scope, scope, args);
                System.out.println("fmt[i]="+result);
            }
        }
        
        f = getFormatterFunction("fmtSemitones");
        System.out.println(Context.toString(f));
        
        f = getFormatterFunction("value-64");
        System.out.println(Context.toString(f));
        
    }
    
    public static void main(String[] args) throws IOException
    {
        JSContext jsc = new JSContext();
        jsc.test();
    }
  */  
}
