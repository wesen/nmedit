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
package net.sf.nmedit.jtheme.store2;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Element;

public abstract class AbstractMultiParameterElement extends AbstractElement
    implements Serializable
{

    protected transient String[] parameterElementNames;
    protected transient String[] componentIdList;
    protected transient Object[] valueList; // (String,Integer) pairs
    
    protected BindParameterInfo bindings = null;
    
    public AbstractMultiParameterElement(String[] parameterElementNames)
    {
        this.parameterElementNames = parameterElementNames;
        componentIdList = new String[parameterElementNames.length];
    }
    
    public AbstractMultiParameterElement(Class<? extends JTComponent> jtclass)
    {
        BindParameterInfo info = BindParameterInfo.forClass(jtclass);
        this.bindings = info;
        parameterElementNames = new String[info.getAdapterCount()];
        componentIdList = new String[info.getAdapterCount()];
        
        Iterator<String> iter = info.parameters();
        int i=0;
        while (iter.hasNext())
            parameterElementNames[i++] = iter.next();

        int cnt = info.getAdapterCount();
        if (cnt>0) valueList = new Object[cnt*2];
        iter = info.values();
        i=0;
        while (iter.hasNext())
        {
            String name = iter.next();
            valueList[i++] = name;
            valueList[i++] = null;
        }
    }
    
    @Override
    protected void initElement(StorageContext context, Element e)
    {
        super.initElement(context, e);
        initComponentIdList(e);
    }

    protected void initComponentIdList(Element e)
    {
        List<?> children = e.getChildren();
       
        for (int i=children.size()-1;i>=0;i--)
        {
            Element p = (Element) children.get(i);
            for (int index=parameterElementNames.length-1;index>=0;index--)
            {
                String n = parameterElementNames[index];
                
                if (n.equals(p.getName()))
                {
                    Attribute a = p.getAttribute(ATT_COMPONENT_ID);
                    if (a != null)
                    {
                        componentIdList[index] = a.getValue();   
                    }
                    
                    break;
                }
            }
            
            // default values for component
            Attribute a = p.getAttribute(ATT_VALUE);
            if (a != null)
            {
                int intValue;
                try
                {
                    intValue = Integer.parseInt(a.getValue());
                } 
                catch (NumberFormatException nfe)
                {
                    continue;
                }
                

                if (valueList != null)
                {
                    for (int index=0;index<valueList.length;index+=2)
                    {
                        String name = (String) valueList[index];
                        if (name.equals(p.getName()))
                        {
                            valueList[index+1] = intValue;
                            break;
                        }
                    }
                }

            }
        }
    }
    
    protected void link(JTComponent component, PModule module)
    {
        if (bindings == null)
            throw new UnsupportedOperationException();
        
        for (int i=0;i<parameterElementNames.length;i++)
        {
            String name = parameterElementNames[i];
           
            PParameter param = module.getParameterByComponentId(componentIdList[i]);
            
            if (param != null)
            {
                // set adapter            	
            	
                Method setter = bindings.getAdapterSetter(name);
                
                 int index = bindings.getAdapterSetterIndex(name);
                    
                    try {
						JTParameterControlAdapter adapter = new JTParameterControlAdapter(param);
						
						if (index < 0) {
							// one arg
							Object[] args = new Object[]{adapter} ;
						    setter.invoke(component, args);
						    
						    PParameter extParam = param.getExtensionParameter();
						    if (extParam != null) {
						    	JTParameterControlAdapter extParamAdapt = new JTParameterControlAdapter(extParam);
						    	
						    	Method setterExt = bindings.getAdapterSetter(name+"Extension");
						    	Object[] argsExt = new Object[]{extParamAdapt} ;
						    	if (setterExt != null)
						    		setterExt.invoke(component, argsExt);
						    	else {
						    		
						    		// TODO: detect which component don't handle morph
						    		// for example: curves
						    		//System.out.println(extParam.getName()+" "+component);
						    	}
						    }
						} 
						else {
							// several args
							Object[] args =  new Object[]{index, adapter};
						    setter.invoke(component, args);
						    PParameter extParam = param.getExtensionParameter();
						    if (extParam != null) {
						    	JTParameterControlAdapter extParamAdapt = new JTParameterControlAdapter(extParam);
						    	
						    	String extName;
						    	if( index > 9) {
						    		extName = name.substring(0,name.length()-2)+"Extension"+index;
						    		
						    	} else{
						    		extName = name.substring(0,name.length()-1)+"Extension"+index;
						    		
						    	}
						    	
						    	Method setterExt = bindings.getAdapterSetter(extName);
						    	Object[] argsExt = new Object[]{index,extParamAdapt} ;
						    	if (setterExt != null) {
						    		setterExt.invoke(component, argsExt);
						    		
						    	}
						    	else {
						    		
						    		// TODO: detect which component don't handle morph
						    		// for example: curves
						    		//System.out.println(extParam.getName()+" "+component);
						    	}
						    }
						}
					} catch (Throwable t) {
						Log log = LogFactory.getLog(getClass());
						if (log.isTraceEnabled())
						{
						    log.trace("error in link("+component+","+module+")", t);
						}
					}
             
            }
            else
            {
                // log: parameter not found
            }
        }
        
        if (valueList != null)
        {
            for (int i=0;i<valueList.length;i+=2)
            {
                String name = (String) valueList[i];
                Integer value = (Integer) valueList[i+1];
               
                if (value != null)
                {
                    Method setter = bindings.getValueSetter(name);

                    try
                    {
                        setter.invoke(component, new Object[]{value});
                    }
                    catch (Exception e)
                    {
                        Log log = LogFactory.getLog(getClass());
                        if (log.isTraceEnabled())
                        {
                            log.trace("error in link("+component+","+module+")", e);
                        }
                    }
                }
            }
        }
        
    }
    
    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        
        out.writeInt(parameterElementNames.length);
        
        int size = 0;
        for (int i=0;i<parameterElementNames.length;i++)
        {
            String n = parameterElementNames[i];
            if (n!=null)
                size++;
        }
        
        out.writeInt(size);
        for (int i=0;i<parameterElementNames.length;i++)
        {
            String n = parameterElementNames[i];
            if (n != null)
            {
                out.writeInt(i);
                out.writeObject(n);
            }
        }
        
        size = 0;
        for (int i=0;i<componentIdList.length;i++)
        {
            String n = componentIdList[i];
            if (n!=null)
                size++;
        }

        out.writeInt(size);
        for (int i=0;i<componentIdList.length;i++)
        {
            String n = componentIdList[i];
            if (n != null)
            {
                out.writeInt(i);
                out.writeObject(n);
            }
        }

        size = 0;
        if (valueList != null)
        {
            for (int i=1;i<valueList.length;i+=2)
            {
                if (valueList[i] != null)
                    size++;
            }
        }   
        out.writeInt(size);
        if (valueList!=null)
        {
            for (int i=0;i<valueList.length;i+=2)
            {
                String n = (String) valueList[i];
                Integer v = (Integer) valueList[i+1];
                if (v != null)
                {
                    out.writeObject(n);
                    out.writeInt(v.intValue());
                }
            }
        }
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        int size = in.readInt();
        
        parameterElementNames = new String[size];
        componentIdList = new String[size];

        size = in.readInt();
        for (int i=0;i<size;i++)
        {
            int index = in.readInt();
            String n = (String) in.readObject();
            parameterElementNames[index] = n;
        }
        
        size = in.readInt();
        for (int i=0;i<size;i++)
        {
            int index = in.readInt();
            String n = (String) in.readObject();
            componentIdList[index] = n;
        }
        
        size = in.readInt();
        if (size>0)
        {
            valueList = new Object[size*2];
            for (int i=0;i<size;i+=2)
            {
                String name = (String) in.readObject();
                int value = in.readInt();
                valueList[i] = name;
                valueList[i+1] = value;
            }
        }
        
    }
}
