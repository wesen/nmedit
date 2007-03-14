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
 * Created on Dec 20, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.parser;

public interface PContentHandler
{

    public static final int HEADER_RSIZE = 23;
    public static final int MODULEDUMP_RSIZE = 4;
    public static final int CURRENTNOTEDUMP_RSIZE = 3;
    public static final int CABLEDUMP_RSIZE = 7;
    public static final int PARAMETERDUMP_RSIZE = 3;//+parameters
    public static final int CUSTOMDUMP_RSIZE = 2;//+parameters
    public static final int KEYBOARDASSIGNMENT_RSIZE = 4; // only one record
    public static final int KNOBMAPDUMP_RSIZE = 4;
    public static final int MORPHMAPDUMP_PROLOG_RSIZE = 4;
    public static final int MORPHMAPDUMP_RSIZE = 5;
    public static final int CTRLMAPDUMP_RSIZE = 4;

    void notes(String notes) throws ParseException;

    void beginDocument() throws ParseException;
    void endDocument() throws ParseException;
    
    void beginSection(int section, int voiceAreaId) throws ParseException;
    void endSection(int section) throws ParseException;

    void header(int[] record) throws ParseException;
    void header(String property, String value) throws ParseException;
    
    void moduleDump(int[] record) throws ParseException;
    void currentNoteDump(int[] record) throws ParseException;
    void cableDump(int[] record) throws ParseException;
    void parameterDump(int[] record) throws ParseException;
    void customDump(int[] record) throws ParseException;
    void keyboardAssignment(int[] record) throws ParseException;
    void knobMapDump(int[] record) throws ParseException;
    void morphMapDumpProlog(int[] record) throws ParseException;
    void morphMapDump(int[] record) throws ParseException;
    void ctrlMapDump(int[] record) throws ParseException;
    void moduleNameDump( int moduleIndex, String moduleName ) throws ParseException;

}
