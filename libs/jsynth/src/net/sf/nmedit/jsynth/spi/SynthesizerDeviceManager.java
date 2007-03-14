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
 * Created on Jun 21, 2006
 */
package net.sf.nmedit.jsynth.spi;


public class SynthesizerDeviceManager
{
/*
    public static Synthesizer.Info[] getSynthesizerInfo()
    {
        java.util.List<Synthesizer.Info> infoList = new ArrayList<Synthesizer.Info>();
        Iterator i = Service.providers(SynthesizerDeviceProvider.class);
        while (i.hasNext())
        {
            SynthesizerDeviceProvider provider = (SynthesizerDeviceProvider) i.next();
            Synthesizer.Info info = provider.getInfo();
            if (info == null) throw new NullPointerException("Synthesizer.Info must not be null.");
            infoList.add(info);
        }
        return infoList.toArray(new Synthesizer.Info[infoList.size()]);
    }
    
    private static SynthesizerDeviceProvider getProvider(Synthesizer.Info info)
    {
        Iterator i = Service.providers(SynthesizerDeviceProvider.class);
        while (i.hasNext())
        {
            SynthesizerDeviceProvider provider = (SynthesizerDeviceProvider) i.next();
            if (info.equals(provider.getInfo()))
                return provider;
        }
        return null;
    }
    
    private static Synthesizer getSynthesizer(SynthesizerDeviceProvider provider)
    {
        if (provider == null) throw new IllegalArgumentException("Synthesizer.Info not known to the manager");
        return provider.createSynthesizer();
    }
    
    public static Synthesizer getSynthesizer(Synthesizer.Info info)
    {
        return getSynthesizer(getProvider(info));
    }
    
    public static Synthesizer getSynthesizer(String name, String version)
    {
        for (Synthesizer.Info info : getSynthesizerInfo())
        {
            if (name.equals(info.getName()) && version.equals(info.getVersion()))
                return getSynthesizer(info);
        }
        
        return null; // not found
    }
*/
}
