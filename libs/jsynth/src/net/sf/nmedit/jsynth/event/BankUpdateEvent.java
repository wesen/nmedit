/* Copyright (C) 2006-2008 Christian Schneider
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
package net.sf.nmedit.jsynth.event;

import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.Synthesizer;

public class BankUpdateEvent extends SynthesizerEvent
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1465573513123819310L;
    private int beginIndex;
    private int endIndex;

    public BankUpdateEvent(Bank<? extends Synthesizer> bank, int beginIndex, int endIndex)
    {
        super(bank.getSynthesizer(), SYNTH_BANK_UPDATE, bank);
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }
    
    @SuppressWarnings("unchecked")
    public Bank<? extends Synthesizer> getBank()
    {
        return (Bank) arg;
    }
    
    public int getBeginIndex()    
    {
        return beginIndex;
    }
    public int getEndIndex()    
    {
        return endIndex;
    }
    
}
