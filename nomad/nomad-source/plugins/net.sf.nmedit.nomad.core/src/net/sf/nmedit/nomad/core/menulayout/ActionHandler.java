package net.sf.nmedit.nomad.core.menulayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionHandler implements ActionListener
{
    
    private static final boolean DEBUG = true;
    
    public static final Class[] NO_ARGS_SIGNATURE = new Class[0];
    public static final Object[] NO_ARGS = new Object[0];
    
    private Object implementor;
    private String methodName;
    private Class[] signature;
    private Object[] args;

    public static Class[] getSignatureFor(Object ... args)
    {
        if (args.length == 0)
            return NO_ARGS_SIGNATURE;
        
        Class[] signature = new Class[args.length];
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
        this(implementor, method, NO_ARGS_SIGNATURE, NO_ARGS);
    }
    
    public ActionHandler(Object implementor, String method, Object ... args)
    {
        this(implementor, method, getSignatureFor(args), args);
    }

    public ActionHandler(Object implementor, String method, Class[] signature, Object[] args)
    {
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
    
    public void checkMethod()
    {
        try
        {
            implementor
            .getClass()
            .getMethod(methodName, signature);
        }
        catch (Exception ex)
        {
            // TODO log error
            throw new RuntimeException(ex);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        try
        {
            implementor
            .getClass()
            .getMethod(methodName, signature)
            .invoke(implementor, args);
        }
        catch (Exception ex)
        {
            // TODO log error
            throw new RuntimeException(ex);
        }
    }

}
