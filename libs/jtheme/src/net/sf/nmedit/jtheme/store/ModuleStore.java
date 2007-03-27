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
package net.sf.nmedit.jtheme.store;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTModule;

import org.jdom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

public class ModuleStore extends DefaultStore implements Iterable<Store>
{
    
    private List<Store> childStore = new ArrayList<Store>();
    
    private CSSStyleDeclaration styleDecl;
    
    private Image staticLayerBackingStore;
    
    public Image getStaticLayer()
    {
        return staticLayerBackingStore;
    }
    
    public void setStaticLayer(Image staticLayer)
    {
        this.staticLayerBackingStore = staticLayer;
    }

    protected ModuleStore(Element element, CSSStyleDeclaration styleDecl)
       throws JTException
    {
        super(element);
        this.styleDecl = styleDecl;
    }

    public static ModuleStore create(StorageContext context, Element element) 
        throws JTException
    {
        CSSStyleDeclaration decl =
            CSSUtils.getStyleDeclaration(element, context);

        return new ModuleStore(element, decl);
    }

    public JTModule createModule(JTContext context) throws JTException
    {
        return createModule(context, null);
    }


    public JTModule createModule(JTContext context, Module module) throws JTException
    {
        return createModule(context, module, staticLayerBackingStore != null);
    }

    public JTModule createModule(JTContext context, Module module, boolean addReducible) throws JTException
    {
        JTModule jtmodule = createComponent(context);
        jtmodule.setModule(module);
        
        createChildren(context, jtmodule, module, addReducible);
        if (staticLayerBackingStore != null)
            jtmodule.setStaticLayerBackingStore(staticLayerBackingStore);
        
        return jtmodule;
    }
    
    private void applyStyle(JTModule jtmodule)
    {
        if (styleDecl == null) return;

        Color fill = CSSUtils.getColor(styleDecl, "fill");
        if (fill != null)
        {
            jtmodule.setBackground(fill);
        }
    }
    
    protected void createChildren(JTContext context, JTModule jtmodule, Module module, boolean addReducible) throws JTException
    {
        // iteration order is important because it implies which components are at the front/back
        for (int i=childStore.size()-1;i>=0;i--)
        {
            Store store = childStore.get(i);
            
            if ((!store.isReducible()) || ((store.isReducible() && addReducible)))
            {
                JTComponent child = store.createComponent(context, module);
                if (child != null)
                    jtmodule.add(child);
            }
        }
    }

    @Override
    public JTModule createComponent(JTContext context) throws JTException
    {
        JTModule component = (JTModule) context.createComponent(JTContext.TYPE_MODULE);
        
        component.setLocation(getX(), getY());
        component.setSize(getWidth(), getHeight());
        
        applyStyle(component);
        
        return component;
    }

    protected void link(JTContext context, JTComponent component, Module module)
      throws JTException
    {
        JTModule jtmodule = (JTModule) component;
        if (module != null)
            jtmodule.setModule(module);
    }

    public void add(Store child)
    {
        childStore.add(child);
    }
    
    public int getChildCount()
    {
        return childStore.size();
    }
    
    public Store getChild(int index)
    {
        return childStore.get(index);
    }
    
    public Iterator<Store> iterator()
    {
        return getChildStoreIterator();
    }
    
    public Iterator<Store> getChildStoreIterator()
    {
        return childStore.iterator();
    }

    public String getId()
    {
        return getElement().getAttributeValue("id");
    }
    
}

