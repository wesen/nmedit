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
 * Created on Jan 16, 2007
 */
package net.sf.nmedit.jsynth.clavia.nordmodular;

import net.sf.nmedit.jsynth.AbstractBank;
import net.sf.nmedit.jsynth.worker.SendPatchWorker;

public class NmBank<S extends NordModular> extends AbstractBank<S>
{

    public NmBank(S synth, int bankIndex)
    {
        super(synth, bankIndex);
    }

    public String getName()
    {
        return Integer.toString(getSection())+"-"+Integer.toString(getSection()+99);
    }

    public int getPatchCount()
    {
        return 99;
    }

    public int getSection()
    {
        return getBankIndex()+1;
    }

    public SendPatchWorker createSendPatchWorker(int position)
    {
        throw new UnsupportedOperationException("not implemented");
    }

}
