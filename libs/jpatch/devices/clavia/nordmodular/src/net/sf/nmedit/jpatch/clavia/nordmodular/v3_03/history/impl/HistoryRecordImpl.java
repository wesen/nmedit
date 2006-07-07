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

import net.sf.nmedit.jpatch.history.HistoryRecord;
import net.sf.nmedit.jpatch.history.HistoryRecordType;

public abstract class HistoryRecordImpl implements HistoryRecord
{

    protected final PatchHistoryImpl history;
    protected final HistoryRecordTypeImpl type;
    private boolean isUndoRecord = true;

    public HistoryRecordImpl(PatchHistoryImpl history, HistoryRecordTypeImpl type)
    {
        this.history = history;
        this.type = type;
    }

    public void setIsUndoRecord(boolean undoRecord)
    {
        this.isUndoRecord = undoRecord;
    }

    public boolean isUndoRecord()
    {
        return isUndoRecord;
    }

    public HistoryRecordType getType()
    {
        return type;
    }

    public void restore()
    {
        HistoryRecordImpl r = createOpposite();
        r.setIsUndoRecord(!isUndoRecord);
        if (r.isUndoRecord())
            history.addToUndo(r);
        else
            history.addToRedo(r);
    }
    
    protected abstract HistoryRecordImpl createOpposite();

}
