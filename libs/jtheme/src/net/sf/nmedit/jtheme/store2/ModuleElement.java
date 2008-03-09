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

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.ColorUIResource;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.store.CSSUtils;
import net.sf.nmedit.jtheme.store.StorageContext;

import org.jdom.Attribute;
import org.jdom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

public class ModuleElement extends AbstractElement implements Serializable, Iterable<ComponentElement>
{

    /**
     * 
     */
    private static final long serialVersionUID = -8151320614763566232L;
    protected String moduleId;
    protected String cssStyleValue;
    
    private transient List<ComponentElement> childStore;
    protected transient CSSStyleDeclaration decl;
    private transient CSSStyleDeclaration styleDecl;
    private transient Image staticLayerBackingStore;

    protected ModuleElement()
    {
        createChildStoreList();
    }
    
    private void createChildStoreList()
    {
        childStore = new ArrayList<ComponentElement>();
    }

    public static ModuleElement createElement(StorageContext context, Element element)
    {
        ModuleElement e = new ModuleElement();
        e.initElement(context, element);
        return e;
    }

    @Override
    public JTModule createComponent(JTContext context,
            PModuleDescriptor descriptor, PModule module) throws JTException
    {
        JTModule jtmodule = (JTModule) context.createComponent(JTContext.TYPE_MODULE);
        setBounds(jtmodule);
        applyStyle(jtmodule);
        if (module != null)
            jtmodule.setModule(module);
        return jtmodule;
    }

    public JTModule createModule(JTContext context) throws JTException
    {
        return createModule(context, null);
    }

    public JTModule createModule(JTContext context, PModule module) throws JTException
    {
        return createModule(context, module, staticLayerBackingStore == null);
    }

    @Override
    protected void initAttributes(StorageContext context, Attribute att)
    {
        if (ATT_COMPONENT_ID.equals(att.getName()))
        {
            moduleId = att.getValue();
        }
        else
        {
            super.initAttributes(context, att);
        }
    }

    @Override
    public void initializeElement(StorageContext context)
    {
        styleDecl = CSSUtils.getStyleDeclaration("module", cssStyleValue, context);
        for (int i=childStore.size()-1;i>=0;i--)
        {
            childStore.get(i).initializeElement(context);
        }
    }
    
    @Override
    protected void initCSSStyle(StorageContext context, String styleValue)
    {
        this.cssStyleValue = styleValue;
    }

    public JTModule createModule(JTContext context, PModule module, boolean addReducible) throws JTException
    {
        JTModule jtmodule = createComponent(context, module.getDescriptor(), module);
        jtmodule.setModule(module);

        createChildren(context, jtmodule, module, addReducible);
        /*if (staticLayerBackingStore != null)
            jtmodule.setStaticLayerBackingStore(staticLayerBackingStore);
        */
        return jtmodule;
    }

    private void applyStyle(JTModule jtmodule)
    {
        if (styleDecl == null) return;

        Color fill = CSSUtils.getColor(styleDecl, "fill");
        if (fill != null)
        {
            jtmodule.setBackground(fill);
            jtmodule.putClientProperty("fill", new ColorUIResource(fill));
        }
    }

    protected void createChildren(JTContext context, JTModule jtmodule, PModule module, boolean addReducible) throws JTException
    {
        // iteration order is important because it implies which components are at the front/back
        for (int i=childStore.size()-1;i>=0;i--)
        {
            ComponentElement store = childStore.get(i);
            
            if ((!store.isReducible()) || ((store.isReducible() && addReducible)))
            {
                JTComponent child = store.createComponent(context, module.getDescriptor(), module);

                if (child != null)
                    jtmodule.add(child);
            }
        }
    }
    
    public void add(ComponentElement child)
    {
        childStore.add(child);
    }

    public int getChildCount()
    {
        return childStore.size();
    }

    public ComponentElement getChild(int index)
    {
        return childStore.get(index);
    }

    public Iterator<ComponentElement> iterator()
    {
        return getChildStoreIterator();
    }
    
    public Iterator<ComponentElement> getChildStoreIterator()
    {
        return childStore.iterator();
    }

    public String getId()
    {
        return moduleId;
    } 
    
    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject(); // module id
        
        // children
        int size = childStore.size();
        out.writeInt(size); 
        for (int i=0;i<size;i++)
            out.writeObject(childStore.get(i));
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        
        int size = in.readInt();
        childStore = new ArrayList<ComponentElement>(size);
        for (int i=0;i<size;i++)
            childStore.add((ComponentElement)in.readObject());
    }
    
}
