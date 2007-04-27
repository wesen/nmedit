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
package net.sf.nmedit.jtheme.editor;

import java.awt.Component;

import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTDefaultControlAdapter;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.reflect.PropertyAccess;
import net.sf.nmedit.jtheme.reflect.PropertyAccessGroup;
import net.sf.nmedit.jtheme.reflect.PropertyDatabase;

public class ModuleCloneTool
{

    private JTModule module;
    private JTModule clone;

    public ModuleCloneTool(JTModule module)
    {
        this.module = module;
    }
    
    public JTModule getSource()
    {
        return module;
    }
    
    public JTModule getClone() throws JTException
    {
        if (clone == null)
            clone = createClone();
        
        return clone;
    }

    private JTModule createClone() throws JTException
    {
        JTContext context = module.getContext();
        PropertyDatabase pd = context.getBuilder().getPropertyDatabase();
        
        JTModule clone = (JTModule) context.createComponentInstance(module.getClass());
        
        transferProperties(clone, module, pd);
        
        cloneChildren(clone, module.getComponents(), context, pd);
        
        return clone;
    }

    private void cloneChildren(JTModule target, Component[] children, JTContext context, PropertyDatabase pd) throws JTException
    {
        for (int i=0;i<children.length;i++)
        {
            cloneChild(target, children[i], context, pd);
        }
    }

    private void cloneChild(JTModule target, Component child, JTContext context, PropertyDatabase pd) throws JTException
    {
        Class c = (Class) child.getClass();
        
        JTComponent clonedChild = context.createComponentInstance(c);
        transferProperties(clonedChild, child, pd);
        clonedChild.setBounds(child.getX(), child.getY(), child.getWidth(), child.getHeight());
        target.add(clonedChild);
        
        if (clonedChild instanceof JTControl)
        {
            JTControl control = (JTControl) clonedChild;
            
            if (control.getControlAdapter() == null)
            {
                control.setAdapter(new JTDefaultControlAdapter(0, 127, 64));
                control.setValue(60);
            }
        }
    }

    private void transferProperties(Component target, Component source, PropertyDatabase pd)
    {        
        PropertyAccessGroup targetGroup = pd.getPropertyAccessGroup(target.getClass());
        PropertyAccessGroup sourceGroup = pd.getPropertyAccessGroup(source.getClass());
     
        for (PropertyAccess sourceAccess : sourceGroup)
        {
            PropertyAccess targetAccess =
            targetGroup.getPropertyAccess(sourceAccess.getPropertyName());
            
            if (targetAccess != null)
            {
                targetAccess.set(target, sourceAccess.get(source));
            }
        }
        
    }
    
}

