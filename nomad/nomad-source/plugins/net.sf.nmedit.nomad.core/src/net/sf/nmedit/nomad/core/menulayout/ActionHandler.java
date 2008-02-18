/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.nomad.core.menulayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

public class ActionHandler implements ActionListener
{
    
    private static final boolean DEBUG = true;
    
    public static final Class<?>[] NO_ARGS_SIGNATURE = new Class[0];
    public static final Object[] NO_ARGS = new Object[0];
    
    private Object implementor;
    private String methodName;
    private Class<?>[] signature;
    private Object[] args;
    private boolean invocationDelayed;

    public static Class<?>[] getSignatureFor(Object ... args)
    {
        if (args.length == 0)
            return NO_ARGS_SIGNATURE;
        
        Class<?>[] signature = new Class[args.length];
        for (int i=0;i<args.length;i++)
        {
            Object a = args[i];
            if (a == null)
                throw new IllegalArgumentException("argument cannot be null");
            signature[i] = a.getClass();
        }
        return signature;
    }
    

    public ActionHandler(Object implementor, String method)
    {
        this(implementor, false, method);
    }
    
    public ActionHandler(Object implementor, String method, Object ... args)
    {
        this(implementor, false, method, args);
    }

    public ActionHandler(Object implementor, String method, Class<?>[] signature, Object[] args)
    {
        this(implementor, false, method, signature, args);
    }
    
    public ActionHandler(Object implementor, boolean invocationDelayed, String method)
    {
        this(implementor, invocationDelayed, method, NO_ARGS_SIGNATURE, NO_ARGS);
    }
    
    public ActionHandler(Object implementor, boolean invocationDelayed, String method, Object ... args)
    {
        this(implementor, invocationDelayed, method, getSignatureFor(args), args);
    }

    public ActionHandler(Object implementor, boolean invocationDelayed, String method, Class<?>[] signature, Object[] args)
    {
        this.invocationDelayed = invocationDelayed;
        if (implementor == null)
            throw new NullPointerException("implementor must not be null");
        if (method == null)
            throw new NullPointerException("method name not specified");
        if (signature == null)
            throw new NullPointerException("illegal signature");
        if (args == null)
            throw new NullPointerException("illegal arguments");
        
        this.implementor = implementor;
        this.methodName = method;
        this.signature = signature;
        this.args = args;
        
        if (DEBUG) checkMethod();
    }

    private RuntimeException asRuntimeException(Throwable e)
    {
        if (e instanceof RuntimeException)
            return (RuntimeException )e;
        else 
            return new RuntimeException(e);
    }
    
    public void checkMethod()
    {
        try
        {
            implementor
            .getClass()
            .getMethod(methodName, signature);
        }
        catch (Throwable ex)
        {
            // TODO log error
            throw asRuntimeException(ex);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (!invocationDelayed)
        {
            processActionEvent(e);
            return ;
        }
        SwingUtilities.invokeLater(new DelayedInvokation(e));
    }
    
    protected void processActionEvent(ActionEvent e)
    {
        try
        {
            implementor
            .getClass()
            .getMethod(methodName, signature)
            .invoke(implementor, args);
        }
        catch (Throwable ex)
        {
            throw asRuntimeException(ex);
        }
    }

    private class DelayedInvokation implements Runnable
    {
        private ActionEvent e;
        
        public DelayedInvokation(ActionEvent e)
        {
            this.e = e;
        }
        
        public void run()
        {
            processActionEvent(e);
        }
    }
    
}
