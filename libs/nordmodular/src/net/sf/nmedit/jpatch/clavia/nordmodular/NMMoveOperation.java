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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.nmedit.jpatch.AbstractMoveOperation;
import net.sf.nmedit.jpatch.Module;

public class NMMoveOperation extends AbstractMoveOperation
{
    
    private VoiceArea va;
    private int dx;
    private int dy;
    private boolean offsetSet = false;
    private Collection<Module> moved = null;

    public NMMoveOperation(VoiceArea va)
    {
        this.va = va;
    }
    
    private void checkOffset()
    {

        if (!offsetSet)
            throw new IllegalStateException("offset not set");
    }
    
    private final static int MAX_POS = 8000-1; // which value is correct ?

    @Override
    public void move()
    {
        checkOffset();
        
        if (isEmpty())
            return;
        
        int srcminx = MAX_POS;
        int srcminy = MAX_POS;
        int srcmaxx = 0;
        int srcmaxy = 0;
        
        for (Module m: this)
        {
            int sx = m.getScreenX();
            int sy = m.getScreenY();

            srcminx = Math.min(srcminx, sx);
            srcminy = Math.min(srcminy, sy);
            srcmaxx = Math.max(srcmaxx, sx);
            srcmaxy = Math.max(srcmaxy, sy);
        }

        int dstminx = srcminx+dx;
        int dstminy = srcminy+dy;

        int dxaligned;
        int dyaligned;
        
        int minxaligned = NMModuleMetrics.computeScreenX(NMModuleMetrics.computeInternalX(dstminx));
        int minyaligned = NMModuleMetrics.computeScreenY(NMModuleMetrics.computeInternalY(dstminy));

        if (minxaligned <0) minxaligned = 0;
        if (minyaligned <0) minyaligned = 0;

        dxaligned = minxaligned-srcminx;
        dyaligned = minyaligned-srcminy;
        
        dstminx = minxaligned;
        dstminy = minyaligned;
        int dstmaxx = srcmaxx+dxaligned;
        int dstmaxy = srcmaxy+dyaligned;

        Collection<Module> tmpMoved = new ArrayList<Module>(va.getModuleCount());
        
        // move other modules so they do not overlap  
        
        List<Module> other = new ArrayList<Module>(va.getModuleCount());
        
        for (Module m: va)
        {
            if (!modules.contains(m))
            {
                int sx = m.getScreenX();
                int sy = m.getScreenY();
                if (dstminx<=sx && sx<=dstmaxx && sy+height(m)>dstminy)
                    other.add(m);
            }
        }
        
        XOrder xorder = new XOrder();
        YOrder yorder = new YOrder();
        
        if (!other.isEmpty()){
            Collections.<Module>sort(other, xorder);
            int colpx = other.get(0).getScreenX();
            Iterator<Module> iter = other.iterator();
            List<Module> col = new ArrayList<Module>(other.size());
            
            while (iter.hasNext())
            {
                Module m = iter.next();
                int sx = m.getScreenX();
                if (colpx != sx)
                {
                    // new column
                    colpx = sx;
                    Collections.<Module>sort(col, yorder);
                    moveColumn(colpx, col, tmpMoved, dxaligned, dyaligned);
                    col.clear();
                }       
                col.add(m);
            }
            Collections.<Module>sort(col, yorder);
            moveColumn(colpx, col, tmpMoved, dxaligned, dyaligned);
            
        }
        
        for (Module m: this)
        {
            int sx = m.getScreenX();
            int sy = m.getScreenY();
            m.setScreenLocation(sx+dxaligned, sy+dyaligned);
            tmpMoved.add(m);
        }
        
        moved = tmpMoved;
    }

    private int height(Module m)
    {
        Object o = m.getDescriptor().getAttribute("height");
        int h = (o != null && o instanceof Integer) ? (Integer) o : 1;
        
        return NMModuleMetrics.computeScreenY(h);
    }

    private void moveColumn(int col, Collection<Module> column, Collection<Module> moved, int dxaligned, int dyaligned)
    {
        int nexty = 0;
        int acol = col-dxaligned;
        for (Module m: this)
        {
            if (m.getScreenX()==acol)
                nexty = Math.max(nexty, m.getScreenY()+height(m));
        }
        nexty+=dyaligned;
        
        for (Module m: column)
        {
            int sy = m.getScreenY();
            
            if (sy > nexty)
                break;
            
            m.setScreenLocation(col, nexty);
            nexty+=height(m);
            moved.add(m);
        }
    }

    private static class XOrder implements Comparator<Module>
    {
        public int compare(Module o1, Module o2)
        {
            return o1.getScreenX()-o2.getScreenX();
        }
    }

    private static class YOrder implements Comparator<Module>
    {
        public int compare(Module o1, Module o2)
        {
            return o1.getScreenY()-o2.getScreenY();
        }
    }
    
    public Point getScreenOffset(Point dst)
    {   
        checkOffset();
        if (dst == null)
            dst = new Point();
        dst.setLocation(dx, dy);
        return dst;
    }

    public void setScreenOffset(int x, int y)
    {
        this.dx = x;
        this.dy = y;
        this.offsetSet = true;
    }

    public Collection<? extends Module> getMovedModules()
    {
        if (moved == null)
            throw new IllegalStateException("move not called");
        return moved;
    }

}
