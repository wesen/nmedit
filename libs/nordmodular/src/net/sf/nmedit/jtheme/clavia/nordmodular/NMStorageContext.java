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
package net.sf.nmedit.jtheme.clavia.nordmodular;

import net.sf.nmedit.jtheme.clavia.nordmodular.store.ADEnvelopeStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.ADSREnvelopeStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.ADSRModEnvelopeStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.AHDEnvelopeStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.ClipDispStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.EqMidDisplayStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.EqShelveDisplayStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.FilterEDisplayStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.FilterFDisplayStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.LFODisplayStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.NoteSeqEditorStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.NoteVelScaleDisplayStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.PhaserDisplayStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.ResetButtonStore;
import net.sf.nmedit.jtheme.clavia.nordmodular.store.WaveWrapDisplayStore;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;

public class NMStorageContext extends DefaultStorageContext
{

    public NMStorageContext(ClassLoader classLoader)
    {
        super(classLoader);
    }

    protected void installDefaults()
    {
        super.installDefaults();
        installStore("resetButton", ResetButtonStore.class);
        installStore("clip-display", ClipDispStore.class);
        installStore("NoteVelScaleDisplay", NoteVelScaleDisplayStore.class);
        installStore("adsr-envelope", ADSREnvelopeStore.class);
        installStore("ahd-envelope", AHDEnvelopeStore.class);
        installStore("adsr-mod-envelope", ADSRModEnvelopeStore.class);
        installStore("ad-envelope", ADEnvelopeStore.class);
        installStore("wavewrap-display", WaveWrapDisplayStore.class);
        installStore("LFODisplay", LFODisplayStore.class);
        installStore("eq-mid-display", EqMidDisplayStore.class);
        installStore("eq-shelving-display", EqShelveDisplayStore.class);
        installStore("phaser-display", PhaserDisplayStore.class);
        installStore("filter-e-display", FilterEDisplayStore.class);
        installStore("filter-f-display", FilterFDisplayStore.class);
        installStore("note-seq-editor", NoteSeqEditorStore.class);
    }
}
