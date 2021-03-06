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

import net.sf.nmedit.jsynth.event.PortAttachmentListener;

public interface Port
{

    String getName();

    boolean isPluggable(Plug p);

    Plug getPlug();
    
    void setPlug(Plug p) throws SynthException;
    
    Synthesizer getSynthesizer();

    void addPortAttachmentListener(PortAttachmentListener l);
    void removePortAttachmentListener(PortAttachmentListener l);
    
}
