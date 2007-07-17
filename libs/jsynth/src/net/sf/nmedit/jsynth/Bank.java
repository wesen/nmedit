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
 * Created on Dec 29, 2006
 */
package net.sf.nmedit.jsynth;

import net.sf.nmedit.jsynth.event.BankUpdateListener;

public interface Bank<S extends Synthesizer>
{

    String getName();
    int getPatchCount();
    int getBankIndex();
    S getSynthesizer();

    String getPatchName(int index);
    boolean containsPatch(int index);
    boolean isPatchInfoAvailable(int index);
    
    void update();
    void update(int beginIndex, int endIndex);

    void addBankUpdateListener(BankUpdateListener l);
    void removeBankUpdateListener(BankUpdateListener l);
    String getPatchLocationName(int position);

}
