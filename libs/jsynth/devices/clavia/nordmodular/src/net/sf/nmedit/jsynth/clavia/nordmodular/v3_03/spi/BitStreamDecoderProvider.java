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
 * Created on Jun 20, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.spi;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spi.NordModularPatch3_03;
import net.sf.nmedit.jpatch.io.PatchDecoder;
import net.sf.nmedit.jpatch.io.PatchDecoderException;
import net.sf.nmedit.jpatch.io.Source;
import net.sf.nmedit.jpatch.spi.PatchDecoderProvider;
import net.sf.nmedit.jpatch.spi.PatchImplementation;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.io.BitStreamDecoder;
import net.sf.nmedit.jsynth.clavia.nordmodular.v3_03.io.BitStreamSource;

public class BitStreamDecoderProvider extends PatchDecoderProvider
{

    @Override
    public boolean isSupported( PatchImplementation impl )
    {
        return NordModularPatch3_03.NAME.equals(impl.getName())
            && NordModularPatch3_03.VERSION.endsWith(impl.getVersion());
    }

    @Override
    public PatchDecoder createDecoder( PatchImplementation impl )
            throws PatchDecoderException
    {
        return new BitStreamDecoder(impl);
    }

    @Override
    public boolean isSupported( Class<? extends Source> source )
    {
        return BitStreamSource.class.isAssignableFrom(source);
    }

}
