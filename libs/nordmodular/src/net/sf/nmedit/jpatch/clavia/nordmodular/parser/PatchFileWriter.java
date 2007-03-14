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
 * Created on Dec 21, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.parser;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class PatchFileWriter implements PContentHandler
{

    public static final String SHEADER = "Header";
    public static final String SMODULEDUMP = "ModuleDump";
    public static final String SCURRENTNOTEDUMP = "CurrentNoteDump";
    public static final String SCABLEDUMP = "CableDump";
    public static final String SPARAMETERDUMP = "ParameterDump";
    public static final String SKNOBMAPDUMP = "KnobMapDump";
    public static final String SCUSTOMDUMP = "CustomDump";
    public static final String SNAMEDUMP = "NameDump";
    public static final String SNOTES = "Notes";
    public static final String SKEYBOARDASSIGNMENT = "KeyboardAssignment";
    public static final String SMORPHMAPDUMP = "MorphMapDump";
    public static final String SCTRLMAPDUMP = "CtrlMapDump";

    private PrintWriter out = null;
    
    public PatchFileWriter()
    {
        super();
    }
    
    public PatchFileWriter(OutputStream ostream)
    {
        setTarget(ostream);
    }
    
    public PatchFileWriter(Writer owriter)
    {
        setTarget(owriter);
    }

    public void setTarget( OutputStream ostream )
    {
        setTarget(new OutputStreamWriter(ostream));
    }

    public void setTarget( Writer owriter )
    {
        this.out = null;
        if (owriter != null)
        {
            if (owriter instanceof PrintWriter)
                this.out = (PrintWriter) owriter;
            else
                this.out = new PrintWriter(owriter);
        }
    }
    
    private void newline()
    {
        out.print("\r\n");
    }
    
    private void begin(String sname)
    {
        out.print('[');
        out.print(sname);
        out.print(']');
        newline();
    }
    
    private void end(String sname)
    {
        out.print("[/");
        out.print(sname);
        out.print(']');
        newline();
    }

    public void notes( String notes ) throws ParseException
    {
        if (notes.length()>0)
        {
            notes = notes.replaceAll("\\n\\n","\r\n\r\n");
            out.write(notes);
            newline();
        }
    }

    public void beginSection( int section, int voiceAreaId )
    {
        switch (section)
        {
            case PParser.IHEADER:
                begin(SHEADER);
                break;
            case PParser.IMODULEDUMP:
                begin(SMODULEDUMP);
                out.print(voiceAreaId);
                newline();
                break;
            case PParser.ICABLEDUMP:
                begin(SCABLEDUMP);
                out.print(voiceAreaId);
                newline();
                break;
            case PParser.IPARAMETERDUMP:
                begin(SPARAMETERDUMP);
                out.print(voiceAreaId);
                newline();
                break;
            case PParser.ICUSTOMDUMP:
                begin(SCUSTOMDUMP);
                out.print(voiceAreaId);
                newline();
                break;
            case PParser.INAMEDUMP:
                begin(SNAMEDUMP);
                out.print(voiceAreaId);
                newline();
                break;
            case PParser.ICURRENTNOTEDUMP:
                begin(SCURRENTNOTEDUMP);
                break;
            case PParser.IKNOBMAPDUMP:
                begin(SKNOBMAPDUMP);
                break;
            case PParser.INOTES:
                begin(SNOTES);
                break;
            case PParser.IKEYBOARDASSIGNMENT:
                begin(SKEYBOARDASSIGNMENT);
                break;
            case PParser.IMORPHMAPDUMP:
                begin(SMORPHMAPDUMP);
                break;
            case PParser.ICTRLMAPDUMP:
                begin(SCTRLMAPDUMP);
                break;
        }
    }

    public void endSection( int section ) 
    {
        switch (section)
        {
            case PParser.IHEADER: 
                end(SHEADER); 
                break;
            case PParser.IMODULEDUMP:
                end(SMODULEDUMP); 
                break;
            case PParser.ICABLEDUMP:
                end(SCABLEDUMP);
                break;
            case PParser.IPARAMETERDUMP:
                end(SPARAMETERDUMP);
                break;
            case PParser.ICUSTOMDUMP:
                end(SCUSTOMDUMP);
                break;
            case PParser.INAMEDUMP:
                end(SNAMEDUMP);
                break;
            case PParser.ICURRENTNOTEDUMP:
                newline();
                end(SCURRENTNOTEDUMP);
                break;
            case PParser.IKNOBMAPDUMP:
                end(SKNOBMAPDUMP);
                break;
            case PParser.INOTES:
                end(SNOTES);
                break;
            case PParser.IKEYBOARDASSIGNMENT:
                end(SKEYBOARDASSIGNMENT);
                break;
            case PParser.IMORPHMAPDUMP:
                end(SMORPHMAPDUMP);
                break;
            case PParser.ICTRLMAPDUMP:
                end(SCTRLMAPDUMP);
                break;
        }
    }

    public void header( int[] record ) throws ParseException
    {
        list(record, HEADER_RSIZE);
        newline();
    }

    public void header( String property, String value ) throws ParseException
    {
        if (value != null)
        {
            out.print(property);
            out.print("=");
            out.print(value);
            newline();
        }
    }

    private void list(int numbers[], int count)
    {
        for (int i=0;i<count;i++)
        {
            out.print(numbers[i]);
            out.print(' ');
        }
    }
    
    public void moduleDump( int[] record ) throws ParseException
    {
        list(record, MODULEDUMP_RSIZE);
        newline();
    }

    public void currentNoteDump( int[] record ) throws ParseException
    {
        list(record, CURRENTNOTEDUMP_RSIZE);
    }

    public void cableDump( int[] record ) throws ParseException
    {
        list(record, CABLEDUMP_RSIZE);
        newline();
    }

    public void parameterDump( int[] record ) throws ParseException
    {
        list(record, PARAMETERDUMP_RSIZE+record[PARAMETERDUMP_RSIZE-1]);
        newline();
    }

    public void customDump( int[] record ) throws ParseException
    {
        list(record, CUSTOMDUMP_RSIZE+record[CUSTOMDUMP_RSIZE-1]);
        newline();
    }

    public void keyboardAssignment( int[] record ) throws ParseException
    {
        list(record, KEYBOARDASSIGNMENT_RSIZE);
        newline();
    }

    public void knobMapDump( int[] record ) throws ParseException
    {
        list(record, KNOBMAPDUMP_RSIZE);
        newline();
    }

    public void morphMapDumpProlog( int[] record ) throws ParseException
    {
        list(record, MORPHMAPDUMP_PROLOG_RSIZE);
        newline();
    }

    public void morphMapDump( int[] record ) throws ParseException
    {
        list(record, MORPHMAPDUMP_RSIZE);
        newline();
    }

    public void ctrlMapDump( int[] record ) throws ParseException
    {
        list(record, CTRLMAPDUMP_RSIZE);
        newline();
    }

    public void moduleNameDump( int moduleIndex, String moduleName )
            throws ParseException
    {
        out.print(moduleIndex);
        out.print(" ");
        out.print(moduleName);
        newline();
    }

    public void beginDocument() throws ParseException
    {
        // no op
    }

    public void endDocument() throws ParseException
    {
        out.flush();
    }

}
