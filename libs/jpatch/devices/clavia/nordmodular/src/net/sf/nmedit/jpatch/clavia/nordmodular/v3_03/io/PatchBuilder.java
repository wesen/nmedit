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
 * Created on Apr 8, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.io;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.misc.Record;

/**
 * Builder that builds a patch in some special format. 
 * 
 * @author Christian Schneider
 */
public interface PatchBuilder
{

    /**
     * Notifies the builder that a new section is found.
     * 
     * @param ID id of the section. The section IDs
     * are defined in {@link net.sf.nmedit.jmisc.nomad.patch.Format}
     */
    public void beginSection(int ID);
    
    /**
     * Notifies the builder that a section is completed.
     * 
     * @param ID id of the section. The section IDs
     * are defined in {@link net.sf.nmedit.jmisc.nomad.patch.Format}
     */
    public void endSection(int ID);
    
    /**
     * Handles the current record.
     * 
     * @param r record containing the current values
     * @throws IllegalStateException if the methods is
     * not called between calls to
     * {@link #beginSection(int)} and {@link #endSection(int)}.
     */
    public void record(Record r) ;

}
