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

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;

public class ModuleAddRecord extends HistoryRecordImpl
{
    
    private int index;
    private boolean polyVA;
    private DModule definition;
    
    public ModuleAddRecord( PatchHistoryImpl history, HistoryRecordTypeImpl type, Module module )
    {
        this( history, type, module.getIndex(), module.getDefinition(), module.getVoiceArea().isPolyVoiceArea() );
    }
    
    public ModuleAddRecord( PatchHistoryImpl history, HistoryRecordTypeImpl type, int index, DModule def, boolean polyVA )
    {
        super( history, type );
        this.index = index;;
        this.definition = def;
        this.polyVA = polyVA;
    }

    public void restore()
    {
        Patch p = history.getPatch();
        
        VoiceArea va = polyVA ? p.getPolyVoiceArea() : p.getCommonVoiceArea();
        va.remove(va.get(index));
        
        super.restore();
    }

    @Override
    protected HistoryRecordImpl createOpposite()
    {
        return new ModuleRemoveRecord(history, (HistoryRecordTypeImpl) type.getOpposite(), index, definition, polyVA);
    }

}
