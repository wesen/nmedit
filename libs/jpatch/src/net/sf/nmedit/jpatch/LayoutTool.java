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
package net.sf.nmedit.jpatch;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutTool
{
    
    private PModuleContainer container;
    private List<PModule> moving ;
    private int dx;
    private int dy;

    public LayoutTool(PModuleContainer container, int initialCapacity)
    {
        this.container = container;
        moving = new ArrayList<PModule>(initialCapacity);
    }
    
    public LayoutTool(PModuleContainer container, Collection<PModule> moved)
    {
        this.container = container;
        moving = new ArrayList<PModule>(moved);
    }

    public void setDelta(int x, int y)
    {
        this.dx = x;
        this.dy = y;
    }
    
    public void setDelta(Point delta)
    {
        setDelta(delta.x, delta.y);
    }
    
    public void add(PModule module)
    {
        moving.add(module);
    }
    
    public Object[] move()
    {
        return _move();
    }

    // sorts by column, then by row
    private final Comparator<PModule> Y_ORDER = new YOrder(); 
    
    private Object[] _move()
    {
        if (container == null)
            return new Object[0]; // implies that no modules were added to the tool

        // metrics
        PModuleMetrics metrics = container.getModuleMetrics();
        
        // maps containing (column, list(module)) pairs
        Map<Integer, List<PModule>> movCols = new HashMap<Integer, List<PModule>>();
        Map<Integer, List<PModule>> fixCols = new HashMap<Integer, List<PModule>>();
        
        // compute bounds, add modules to map 
        int minx = Integer.MAX_VALUE;
        int maxx = 0;
        int miny = Integer.MAX_VALUE;
        int maxy = 0;
        int tx = dx;
        int ty = dy;
        
        int minyunaligned = Integer.MAX_VALUE;
        
        for (PModule m: moving)
        {
            int sy = m.getScreenY()+ty;
            int ax = metrics.alignScreenX(m.getScreenX()+tx);
            int ay = metrics.alignScreenY(sy);
            
            minyunaligned = Math.min(minyunaligned, sy);
            
            minx = Math.min(minx, ax);
            maxx = Math.max(maxx, ax+m.getScreenWidth() -1);
            miny = Math.min(miny, ay);
            maxy = Math.max(maxy, ay+m.getScreenHeight()-1);
            
            add(movCols, ax, m);
        }
        
        {
            int d = minyunaligned-miny;
            ty-=d;
        }
        
        if (minx <0)
        {
            tx -= minx;
            maxx -= minx;
            minx = 0;
        }
        if (miny < 0)
        {
            ty -= miny;
            maxy -= miny;
            miny = 0;
        }
        
        // find modules which might be moved 
        for (PModule m: container)
        {
            int sx = m.getScreenX();
            int sb = m.getScreenY()+m.getScreenHeight()-1;
            if (sb>=miny && movCols.containsKey(sx) && (!moving.contains(m)))
                add(fixCols, sx, m);
        }

        // see if modules have to be moved down more than dy
        {
            int add_y = 0;
            for (List<PModule> list: fixCols.values())
                for (PModule m: list)
                {
                    int m_h = m.getScreenHeight();
                    int m_y = m.getScreenY();
                    int m_bot = m_y+m_h-1;
                    if (m_y<miny && miny<=m_bot)
                    {                        
                        add_y = Math.max(add_y, m_bot-miny+1);
                    }
                }
            miny+=add_y;
            maxy+=add_y;
            ty+=add_y;
        }
        
        List<Object> layout = new ArrayList<Object>(moving.size());

        // moved modules
        for (Integer sx: movCols.keySet())
        {
            int top = miny;
            // move column
            for (PModule m: movCols.get(sx))
            {
                int sy = metrics.alignScreenY(ty+m.getScreenY());
                layout.add(m);
                layout.add(sx);
                layout.add(sy);
                top = Math.max(top, sy+m.getScreenHeight());
            }

            // indirect movements

            List<PModule> indirect = fixCols.get(sx);
            if (indirect != null)
            {
                Collections.sort(indirect, Y_ORDER);
                for (PModule m: indirect)
                {
                    int sy = m.getScreenY();
                    int sb = sy+m.getScreenHeight()-1;
                    if (sb<miny) continue;
                    int add_y = top-sy;
                    if (add_y<=0) break;
                    int ny = sy+add_y; // new y location
                    layout.add(m);
                    layout.add(sx);
                    layout.add(ny);
                    top = ny+m.getScreenHeight();
                }
            }
        }
        
        return layout.toArray();
    }

    private void add(Map<Integer, List<PModule>> map, int key, PModule m)
    {
        List<PModule> list = map.get(key);
        if (list == null)
            map.put(key, list = new ArrayList<PModule>(1));
        list.add(m);
    }

    private static class YOrder implements Comparator<PModule>
    {
        public int compare(PModule a, PModule b)
        {
            return Integer.signum(a.getScreenY()-b.getScreenY());
        }
    }
    
}
