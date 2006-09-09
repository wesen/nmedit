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
 * Created on Sep 9, 2006
 */
package net.sf.nmedit.jtheme;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import net.sf.nmedit.jtheme.dom.ContainerNode;
import net.sf.nmedit.jtheme.dom.ThemeDom;

public class Theme
{
    
    public final static String CONTAINER_ALIAS = "container"; 
    
    private ThemeConfiguration conf = new ThemeConfiguration();
    private ComponentBuilder builder = new ComponentBuilder(this);
    private ThemeDom dom = new ThemeDom();
    
    private Map<String,Class<? extends JComponent>> componentClasses = new
        HashMap<String,Class<? extends JComponent>>();

    public Class<? extends JComponent>[] getClassList()
    {
        Collection<Class<? extends JComponent>> c = componentClasses.values();
        return c.toArray(new Class[c.size()]);
    }
    
    public void putComponentClass(Class<? extends JComponent> cclass, String alias)
    {
        componentClasses.put(alias, cclass);
    }
    
    public Class<? extends JComponent> getComponentClassForAlias(String alias)
    {
        return componentClasses.get(alias);
    }
    
    public JComponent createComponent(String alias) 
        throws InstantiationException, IllegalAccessException
    {
        Class<? extends JComponent> clazz = getComponentClassForAlias(alias);
        if (clazz==null)
            throw new InstantiationException("no component installed for alias <"+alias+">");
        return clazz.newInstance();
    }
    
    public ThemeDom getDom()
    {
        return dom;
    }
    
    public ThemeConfiguration getThemeConfiguration()
    {
        return conf;
    }
    
    public ComponentBuilder getComponentBuilder()
    {
        return builder;
    }
    
    public JComponent build(int ID)
    {
        ContainerNode cn = dom.get(ID);
        return cn == null ? null : builder.build(cn);
    }

}
