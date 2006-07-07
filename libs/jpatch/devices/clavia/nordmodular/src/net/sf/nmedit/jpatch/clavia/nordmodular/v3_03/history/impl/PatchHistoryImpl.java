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
 * Created on Jul 6, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.history.impl;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.history.PatchHistory;

public class PatchHistoryImpl implements PatchHistory
{

    private Patch patch;
    private boolean modified;
    private PatchEventTracer tracer;

    public PatchHistoryImpl(Patch patch)
    {
        this.patch = patch;
        this.modified = false;
        this.tracer = new PatchEventTracer(this);
    }
    
    public void uninstall()
    {
        tracer.uninstall();
    }
    
    public Patch getPatch()
    {
        return patch;
    }

    public int getUndoCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getRedoCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void clearUndo()
    {
    // TODO Auto-generated method stub

    }

    public void clearRedo()
    {
    // TODO Auto-generated method stub

    }

    public void clear()
    {
    // TODO Auto-generated method stub

    }

    public void setUndoLimit( int limit )
    {
    // TODO Auto-generated method stub

    }

    public void restore( HistoryRecordImpl impl )
    {
        // TODO Auto-generated method stub
        
    }

    public void undo()
    {
        // TODO Auto-generated method stub
        
    }

    public void redo()
    {
        // TODO Auto-generated method stub
        
    }

    public boolean canUndo()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean canRedo()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void addToUndo( HistoryRecordImpl r )
    {
        // TODO Auto-generated method stub
        
    }

    public void addToRedo( HistoryRecordImpl r )
    {
        // TODO Auto-generated method stub
        
    }
    
    public void setModified(boolean modified)
    {
        this.modified = modified;
    }

    public boolean isModified()
    {
        return modified;
    }

}
