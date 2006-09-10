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
package net.sf.nmedit.nomad.patch.ui.drag;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;

public class ModuleSelectionTool implements Iterable<ModuleUI>
{
    
    private Set<ModuleUI> modules = new HashSet<ModuleUI>();

    public Set<ModuleUI> getModuleUIs()
    {
        return Collections.<ModuleUI>unmodifiableSet(modules);
    }

    public void clear()
    {
        for (ModuleUI m : modules)
            m.setSelected(false);
        modules.clear();
    }
    
    public boolean isEmpty()
    {
        return modules.isEmpty();
    }
    
    public void add(ModuleUI m)
    {
        modules.add(m);
        m.setSelected(true);
    }
    
    public void remove(ModuleUI m)
    {
        modules.remove(m);
        m.setSelected(false);
    }
    
    public ModuleUI[] toArray()
    {
        return modules.toArray(new ModuleUI[modules.size()]);
    }
    
    public Iterator<ModuleUI> iterator()
    {
        return modules.iterator();
    }
    
    public Set<Module> modules()
    {
        Set<Module> set = new HashSet<Module>();
        for (ModuleUI m : this)
        {
            Module mod =  m.getModule();
            if (mod!=null)
                set.add(mod);
        }
        return set;
    }
    
    public Point getTopLeft()
    {
        if (isEmpty())
            return new Point(0,0);
        
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        
        for (Module m : modules())
        {
            x = Math.min(x, m.getX());
            y = Math.min(y, m.getY());
        }
        
        return new Point(Math.max(0, x), Math.max(0, y));
    }
    
    public Point getTopLeftPX()
    {
        if (isEmpty())
            return new Point(0,0);
        
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        
        for (ModuleUI m : modules)
        {
            x = Math.min(x, m.getX());
            y = Math.min(y, m.getY());
        }
        
        return new Point(Math.max(0, x), Math.max(0, y));
    }

    public void delete()
    {
        for (ModuleUI m : toArray()) // use array because of concurrent modi exception
        {
            Module mod =  m.getModule();
            if (mod!=null)
            {
                mod.removeCables();
                mod.getVoiceArea().remove(mod);
            }
            m.setSelected(false);
        }
        modules.clear();
    }
    
    public Rectangle getBounds(Rectangle re)
    {
        if (re == null) re = new Rectangle();
        int t = Integer.MAX_VALUE;
        int l = Integer.MAX_VALUE;
        int r = Integer.MIN_VALUE;
        int b = Integer.MIN_VALUE;
        
        for (ModuleUI m:modules)
        {
            l = Math.min(m.getX(), l);
            t = Math.min(m.getY(), t);
            r = Math.max(m.getX()+m.getWidth(), r);
            b = Math.max(m.getY()+m.getHeight(), b);
        }
        
        re.setBounds(l,t,r-l,b-t);
        
        return re;
    }

    public void copyTo(VoiceArea v, int dx, int dy)
    {
        
        Point tl = getTopLeft();
        
        Set<Module> modules = modules();
     
        if (modules.isEmpty())
            return ;

        Map<Module,Module> clonemap = new HashMap<Module,Module>();
        for (Module m : modules)
        {    
            Module clone = m.clone();
            clone.setIndex(-1);
            
            clone.setLocation(clone.getX()-tl.x+dx, clone.getY()-tl.y+dy);
            clonemap.put(m, clone);
            
            v.add(clone);
        }
        
        for (Module m : modules)
        {
            Module clone = clonemap.get(m);
            for (int i=m.getConnectorCount()-1;i>=0;i--)
            {
                Connector c1 = m.getConnector(i);
                Iterator<Connector> i2 = c1.childIterator();
                while (i2.hasNext())
                {
                    Connector c2 = i2.next();
                    Module m2 = c2.getModule();
                    Module clone2 = clonemap.get(m2);
                    if (clone2!=null)
                    {
                        Connector c1Clone = clone.getConnector(i);
                        Connector c2Clone = clone2.getConnector(c2.getDefinition().getContextId());
                        Connector.connect(c1Clone, c2Clone, null);
                    }
                }
            }
        }
        
    }
    
    public void moveSelection(int pxX, int pxY)
    {
        ModuleUI[] moved = toArray();
        
        Arrays.sort(moved, new DescendingYOrder());
        
        // we have to get the locations first, otherwise when dragging multiple modules
        // they will be moved to other locations than expected
        int[] xcoords = new int[moved.length];
        int[] ycoords = new int[moved.length];
        for (int i=0;i<moved.length;i++)
        {
            ModuleUI ui = moved[i];
            xcoords[i]=ui.getX()+pxX;
            ycoords[i]=ui.getY()+pxY;
        }

        for (int i=0;i<moved.length;i++)
        {
            ModuleUI ui = moved[i];
            ui.setLocationEx(xcoords[i], ycoords[i]);
        }
    }

    private static class DescendingYOrder implements Comparator<Component>
    {
        public int compare( Component o1, Component o2 )
        {            
            return o1.getY()-o2.getY();
        }
        
    }

    public boolean contains( ModuleUI moduleUI )
    {
        return modules.contains(moduleUI);
    }

}
