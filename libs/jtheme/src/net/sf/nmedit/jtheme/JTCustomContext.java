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
package net.sf.nmedit.jtheme;

import java.awt.Image;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.UIDefaults;

import net.sf.nmedit.jtheme.component.JTComponent;

public abstract class JTCustomContext extends JTContext
{

    private boolean hasModuleContainerOverlay;
    private UIDefaults uiDefaults;
    private Set<Class> componentClasses = new HashSet<Class>();
    private Map<String,Class<? extends JTComponent>> classMap = new HashMap<String,Class<? extends JTComponent>>();
    private boolean dndAllowed = false;
    
    protected JTCustomContext(boolean hasModuleContainerOverlay, boolean dndAllowed)
    {
        this.hasModuleContainerOverlay = hasModuleContainerOverlay;
        this.dndAllowed = dndAllowed;
        
        uiDefaults = new UIDefaults();
        
        installComponentClasses();
        setDefaults(uiDefaults);
        installComponentClassMap();
    }
    
    public boolean isDnDAllowed()
    {
        return dndAllowed;
    }
    
    protected abstract void installComponentClasses();

    protected abstract void installComponentClassMap();
    
    public <T extends JTComponent> void installComponentType(String type, Class<T> clazz)
    {
        classMap.put(type, clazz);
    }
    
    public Class<? extends JTComponent> getComponentType(String type)
    {
        return classMap.get(type);
    }

    public JTComponent createComponent(String type) throws JTException
    {
        Class<? extends JTComponent> clazz = getComponentType(type);

        if (clazz == null)
            throw new JTException("type not found: "+type);
        
        return createComponentInstance(clazz);
    }
    
    protected void installComponentClass(Class<?> clazz)
    {
        componentClasses.add(clazz);
    }
    
    protected abstract void setDefaults(UIDefaults uiDefaults);

    @Override
    public Class[] getComponentClasses()
    {
        return componentClasses.toArray(new Class<?>[componentClasses.size()]);
    }

    @Override
    public Image getImage(String key)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UIDefaults getUIDefaults()
    {
        return uiDefaults;
    }

    @Override
    public boolean hasModuleContainerOverlay()
    {
        return hasModuleContainerOverlay;
    }

}

