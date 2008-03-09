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
 * Created on Dec 19, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class PParser implements ErrorHandler
{

    public static final String SHEADER = "header";
    public static final String SMODULEDUMP = "moduledump";
    public static final String SCURRENTNOTEDUMP = "currentnotedump";
    public static final String SCABLEDUMP = "cabledump";
    public static final String SPARAMETERDUMP = "parameterdump";
    public static final String SKNOBMAPDUMP = "knobmapdump";
    public static final String SCUSTOMDUMP = "customdump";
    public static final String SNAMEDUMP = "namedump";
    public static final String SNOTES = "notes";
    public static final String SKEYBOARDASSIGNMENT = "keyboardassignment";
    public static final String SMORPHMAPDUMP = "morphmapdump";
    public static final String SCTRLMAPDUMP = "ctrlmapdump";

    public static final int IHEADER = 0;
    public static final int IMODULEDUMP = 1;
    public static final int ICURRENTNOTEDUMP = 2;
    public static final int ICABLEDUMP = 3;
    public static final int IPARAMETERDUMP = 4;
    public static final int IKNOBMAPDUMP = 5;
    public static final int ICUSTOMDUMP = 6;
    public static final int INAMEDUMP = 7;
    public static final int INOTES = 8;
    public static final int IKEYBOARDASSIGNMENT = 9;
    public static final int IMORPHMAPDUMP = 10;
    public static final int ICTRLMAPDUMP = 11;
    
    private PScanner scanner;
    private int token = -1;
    
    private static final Map<String,Integer> sectionMap = new HashMap<String,Integer>();
    
    static
    {
        sectionMap.put(SHEADER, IHEADER);
        sectionMap.put(SMODULEDUMP, IMODULEDUMP);
        sectionMap.put(SCURRENTNOTEDUMP, ICURRENTNOTEDUMP);
        sectionMap.put(SCABLEDUMP, ICABLEDUMP);
        sectionMap.put(SPARAMETERDUMP, IPARAMETERDUMP);
        sectionMap.put(SKNOBMAPDUMP, IKNOBMAPDUMP);
        sectionMap.put(SCUSTOMDUMP, ICUSTOMDUMP);
        sectionMap.put(SNAMEDUMP, INAMEDUMP);
        sectionMap.put(SNOTES, INOTES);
        sectionMap.put(SKEYBOARDASSIGNMENT, IKEYBOARDASSIGNMENT);
        sectionMap.put(SMORPHMAPDUMP, IMORPHMAPDUMP);
        sectionMap.put(SCTRLMAPDUMP, ICTRLMAPDUMP);
    }
    
    private int[] allnumbers = new int[10];
    private int numbers = 0;
    
    private PContentHandler handler = null;
    private ErrorHandler errorHandler;

    public PParser(ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
        scanner = new PScanner();
    }
    
    public void setContentHandler(PContentHandler handler)
    {
        this.handler = handler;
    }
    
    public PContentHandler getContentHandler()
    {
        return handler;
    }
    
    public PParser(InputStream stream, ErrorHandler errorHandler)
    {
        this(errorHandler);
        setSource(stream);
    }
    
    public PParser(Reader reader,ErrorHandler errorHandler)
    {
        this(errorHandler);
        setSource(reader);
    }
    
    public void setSource(InputStream source)
    {
        scanner.setSource(source);
        reset();
    }
    
    public void setSource(Reader source)
    {
        scanner.setSource(source);
        reset();
    }
    
    private void reset()
    {
        numbers = 0;
        token = -1;
    }
    
    public void warning( ParseException e ) throws ParseException
    {
        emitwarning(e.getMessage());
    }

    public void error( ParseException e ) throws ParseException
    {
        emiterror(e.getMessage());
    }

    public void fatal( ParseException e ) throws ParseException
    {
        emitfatal(e.getMessage());
    }

    private String errormsg(int line, int column, String weight, String message)
    {
        return weight+"@"+line+":"+column + (message != null ? ": "+message : "");
    }
    
    private void emitwarning(int line, int column, String message) throws ParseException
    {
        errorHandler.warning(new ParseException(errormsg(line, column, "warning",message)));
    }

    private void emiterror(int line, int column, String message) throws ParseException
    {
        ParseException e = new ParseException(errormsg(line, column, "error",message));
        errorHandler.error(e);
    }

    private void emitfatal(int line, int column, String message) throws ParseException
    {
        ParseException e = new ParseException(errormsg(line, column, "fatal",message)); 
        errorHandler.fatal(e);
        throw e; // fallback
    }

    public void emitwarning(String message) throws ParseException
    {
        emitwarning(getLineNumber(), getColumn(), message);
    }

    public void emiterror(String message) throws ParseException
    {
        emiterror(getLineNumber(), getColumn(), message);
    }

    public void emitfatal(String message) throws ParseException
    {
        emitfatal(getLineNumber(), getColumn(), message);
    }
    
    public int getPosition()
    {
        return scanner.getPosition();
    }
    
    public int getColumn()
    {
        return scanner.getColumn();
    }
    
    public int getLineNumber()
    {
        return scanner.getLineNumber();
    }
    
    public void parse() throws ParseException
    {
        try
        {
            handler.beginDocument();
            sections();
            handler.endDocument();
        }
        catch (ParseException pe)
        {
            throw pe;
        }
        catch (Throwable t)
        {
            throw new ParseException(errormsg(getLineNumber(), getColumn(), "error", null), t);
        }
    }

    private void sections() throws IOException, ParseException
    {
        int sectionid;
        int position;
        boolean firstsection = true;
        
        do
        {
            position = getPosition();
            
            if (firstsection)
            {
                firstsection = false;

                skipws();
                if (PScanner.BROPEN == next())
                {
                    take();
                    skipws();
                    sectionid = sectionname(-1);
                }
                else
                {
                    emiterror("expected '[' (<begin-section Header >), found "+found()) ;
                    emitwarning("assuming Header section");
                    sectionid = IHEADER;
                }
            }
            else
            {
                if (PScanner.BROPEN != inclusive(PScanner.BROPEN))
                {
                    if (next()==PScanner.EOF)
                        break;
                    emiterror("expected '[' (<begin-section>), found "+found()) ;
                }
    
                skipws();
                sectionid = sectionname(-1);
            }
            
            firstsection = false;
           
            if (sectionid>=0)
            {
                  // <NAME>
                if (PScanner.BRCLOSE != wsinclusive(PScanner.BRCLOSE))
                    emiterror("expected ']' (<begin-section>), found "+found()) ;
                    ;
                    
                if (section(sectionid))
                {
                    if (PScanner.BROPEN != inclusive(PScanner.BROPEN))
                        emiterror("expected '[' (<end-section>), found "+found()) ;
                        ;
                    if (PScanner.SLASH != inclusive(PScanner.SLASH))
                        emiterror("expected '/' (<end-section>), found "+found()) ;
                        ;
                    
                    skipws();
                    sectionname(sectionid);
                    
                    if (PScanner.BRCLOSE != wsinclusive(PScanner.BRCLOSE))
                        emiterror("expected ']' (<end-section>), found "+found()) ;
                        ;
                }
            }
            
            if (position == getPosition())
                throw new ParseException("invalid state, found "+found());
            
        } while (next()!=-1);
    }

    private int sectionname(int sectionid) throws IOException, ParseException
    {
        int tclass = next();
        
        if (tclass != PScanner.ANY)
        {
            String sname = sectionNameForId(sectionid);
            if (sname!=null)
                emiterror("expected <section-name> '"+sname+"', found "+found());
            else
                emiterror("expected <section-name>, found "+found());
            return -1;
        }
        take();

        Integer sid = sectionMap.get(scanner.getString().toLowerCase());
        
        if (sid == null || (sectionid>= 0 && sid.intValue()!=sectionid))
        {
            String sname = sectionNameForId(sectionid);
            if (sname!=null)
                emiterror("invalid <section-name> '"+scanner.getString()+"' expected '"+sname+"'");
            else
                emiterror("invalid <section-name> '"+scanner.getString()+"'");
            
            return -1;
        }
        else
        {
            return sid.intValue();
        }
    }
    
    private String sectionNameForId(int id)
    {
        for (String key : sectionMap.keySet())
            if (sectionMap.get(key)==id)
                return key;
        return null;
    }

    private boolean section(int sectionid) throws IOException, ParseException
    {
        switch (sectionid)
        {
            case IHEADER:
                header();
                break;
            case IMODULEDUMP:
                if (!isEmptySection(sectionid))
                    moduleDump();
                break;
            case ICURRENTNOTEDUMP:
                if (!isEmptySection(sectionid))
                    currentNoteDump();
                break;
            case ICABLEDUMP:
                if (!isEmptySection(sectionid))
                    cableDump();
                break;
            case IPARAMETERDUMP:
                if (!isEmptySection(sectionid))
                    parameterDump();
                break;
            case IKNOBMAPDUMP:
                if (!isEmptySection(sectionid))
                    knobMapDump();
                break;
            case ICUSTOMDUMP:
                if (!isEmptySection(sectionid))
                    customDump();
                break;
            case INAMEDUMP:
                if (!isEmptySection(sectionid))
                    nameDump();
                break;
            case IKEYBOARDASSIGNMENT:
                if (!isEmptySection(sectionid))
                    kbAssignment();
                break;
            case IMORPHMAPDUMP:
                if (!isEmptySection(sectionid))
                    morphMapDump();
                break;
            case ICTRLMAPDUMP:
                if (!isEmptySection(sectionid))
                    ctrlMapDump();
                break;
            case INOTES:
                notesSection();
                return false;
        }
        return true;
    }
    
    private boolean isEmptySection(int sectionid) throws IOException, ParseException
    {
        skipws();
        if (next() == PScanner.BROPEN)
        {
            emitwarning("section contains no data "+sectionNameForId(sectionid));
            return true;
        }
        return false;
    }
    
    private void header() throws IOException, ParseException
    {
        handler.beginSection(IHEADER, -1);
        loop:while (true)
        {
            skipws();
            
            switch (next())
            {
                case PScanner.NUMBER:                    
                    parseNumbers(PContentHandler.HEADER_RSIZE, -1);
                    if (numbers != PContentHandler.HEADER_RSIZE)
                        emiterror("erroneous numbers("+numbers+") in header");
                    else
                        handler.header(allnumbers);
                    break;
                case PScanner.EOF:
                    emitfatal("unexpected end of file");
                    break loop;
                case PScanner.BROPEN:
                    break loop;
                default:
                    try
                    {
                        testVersionKey = true;
                        keyValuePair();
                    }
                    finally
                    {
                        testVersionKey = false;
                    }
                    break;
            }
        }
        handler.endSection(IHEADER);
    }
    
    private boolean testVersionKey = false;

    private void keyValuePair() throws IOException, ParseException
    {
        String key = scanner.getString();
        take();
        if (wsinclusive(PScanner.EQ)!=PScanner.EQ)
        {
            emiterror("expected '=', found "+found());
            throw new RuntimeException();
        }
        else
        {
            StringBuilder value = getCachedBuilder();
            
            loop:while(true)
            {
                switch (next())
                {
                    case PScanner.NEWLINEWS:
                        break loop;
                    case PScanner.EOF:
                        break loop;
                    case PScanner.NUMBER:
                        value.append(Integer.toString(scanner.getNumber()));
                        break;
                    default:
                        value.append(scanner.getString());
                        break;
                }
                take();
            }
            
            String valueString = value.toString();
            
            if (key != null && "version".contains(key.toLowerCase()))
            {
                if (valueString.contains("2.10"))
                    emiterror("unsupported patch format:\""+valueString+"\"");
            }
            
            handler.header(key, valueString);
        }
    }

    private void incompleteRecord(int section, int expectedSize) throws ParseException
    {
        StringBuilder sb = getCachedBuilder();
        sb.append("incomplete record in ");
        sb.append(sectionNameForId(section));
        sb.append(" [");
        for (int i=0;i<expectedSize;i++)
        {
            if (i<numbers)
                sb.append(allnumbers[i]);
            else
                sb.append("?");
            sb.append(",");
        }
        if (expectedSize>0)
            sb.replace(sb.length()-1, sb.length(),"]");
        else
            sb.append("]");
        emitwarning(sb.toString());
    }

    private void ctrlMapDump() throws IOException, ParseException
    {       
        boolean content = false;
        
        while (true)
        {
            parseNumbers(PContentHandler.CTRLMAPDUMP_RSIZE, -1);
            if (numbers != PContentHandler.CTRLMAPDUMP_RSIZE)
                break;
            
            if (!content)
            {
                content = true;
                handler.beginSection(ICTRLMAPDUMP, -1);
            }
            
            handler.ctrlMapDump(allnumbers);
        }
        if (numbers>0)
            incompleteRecord(ICTRLMAPDUMP, PContentHandler.CTRLMAPDUMP_RSIZE);
        if (content)
            handler.endSection(ICTRLMAPDUMP);
    }
    
    int xcount=0;
    private void moduleDump() throws IOException, ParseException
    {
        int va = voiceAreaId();
        if (va<0)
            return;

        handler.beginSection(IMODULEDUMP, va);
        while (true)
        {
            parseNumbers(PContentHandler.MODULEDUMP_RSIZE, -1);
            if (numbers == PContentHandler.MODULEDUMP_RSIZE)
            {
                handler.moduleDump(allnumbers);
            }
            else break;
        }
        if (numbers>0)
            incompleteRecord(IMODULEDUMP, PContentHandler.MODULEDUMP_RSIZE);
        
        handler.endSection(IMODULEDUMP);   
    }

    private void morphMapDump() throws IOException, ParseException
    {
        handler.beginSection(IMORPHMAPDUMP, -1);
        
        parseNumbers(PContentHandler.MORPHMAPDUMP_PROLOG_RSIZE, -1);
        if (numbers == PContentHandler.MORPHMAPDUMP_PROLOG_RSIZE)
        {
            handler.morphMapDumpProlog(allnumbers);
            while (true)
            {
                parseNumbers(PContentHandler.MORPHMAPDUMP_RSIZE, -1);
                if (numbers != PContentHandler.MORPHMAPDUMP_RSIZE)
                    break;
                handler.morphMapDump(allnumbers);
            }
            if (numbers>0)
                incompleteRecord(IMORPHMAPDUMP, PContentHandler.MORPHMAPDUMP_RSIZE);
        }
        else
        {
            incompleteRecord(IMORPHMAPDUMP, PContentHandler.MORPHMAPDUMP_PROLOG_RSIZE);
        }
        handler.endSection(IMORPHMAPDUMP);   
    }

    private void kbAssignment() throws IOException, ParseException
    {
        handler.beginSection(IKEYBOARDASSIGNMENT, -1);
        while (true)
        {
            parseNumbers(PContentHandler.KEYBOARDASSIGNMENT_RSIZE, -1);
            if (numbers != PContentHandler.KEYBOARDASSIGNMENT_RSIZE)
                break;
            handler.keyboardAssignment(allnumbers);
        }
        if (numbers>0)
            incompleteRecord(IKEYBOARDASSIGNMENT, PContentHandler.KEYBOARDASSIGNMENT_RSIZE);
        
        handler.endSection(IKEYBOARDASSIGNMENT);  
    }

    private void customDump() throws IOException, ParseException
    {
        int va = voiceAreaId();
        if (va<0)
            return;

        handler.beginSection(ICUSTOMDUMP, va);
        while (true)
        {
            parseNumbers(PContentHandler.CUSTOMDUMP_RSIZE, 
                    PContentHandler.CUSTOMDUMP_RSIZE-1);

            if (numbers < PContentHandler.CUSTOMDUMP_RSIZE
            || numbers != (PContentHandler.CUSTOMDUMP_RSIZE
                +allnumbers[PContentHandler.CUSTOMDUMP_RSIZE-1]) )
                break;    
            handler.customDump(allnumbers);
        }
        if (numbers>0)
            incompleteRecord(ICUSTOMDUMP, PContentHandler.CUSTOMDUMP_RSIZE);
        
        handler.endSection(ICUSTOMDUMP);   
    }

    private void knobMapDump() throws IOException, ParseException
    {
        handler.beginSection(IKNOBMAPDUMP, -1);
        while (true)
        {
            parseNumbers(PContentHandler.KNOBMAPDUMP_RSIZE, -1);
            if (numbers != PContentHandler.KNOBMAPDUMP_RSIZE)
                break;
            handler.knobMapDump(allnumbers);
        }
        if (numbers>0)
            incompleteRecord(IKNOBMAPDUMP, PContentHandler.KNOBMAPDUMP_RSIZE);
        
        handler.endSection(IKNOBMAPDUMP);   
    }

    private void parameterDump() throws IOException, ParseException
    {
        int va = voiceAreaId();
        if (va<0)
            return;

        handler.beginSection(IPARAMETERDUMP, va);
        while (true)
        {
            parseNumbers(PContentHandler.PARAMETERDUMP_RSIZE, 
                    PContentHandler.PARAMETERDUMP_RSIZE-1);
            
            if (numbers < PContentHandler.PARAMETERDUMP_RSIZE
            || numbers != (PContentHandler.PARAMETERDUMP_RSIZE
                +allnumbers[PContentHandler.PARAMETERDUMP_RSIZE-1]) )
                break;
            handler.parameterDump(allnumbers);
        }
        if (numbers>0)
            incompleteRecord(IPARAMETERDUMP, PContentHandler.PARAMETERDUMP_RSIZE);
        
        handler.endSection(IPARAMETERDUMP);   
    }

    private void cableDump() throws IOException, ParseException
    {
        int va = voiceAreaId();
        if (va<0)
            return;

        handler.beginSection(ICABLEDUMP, va);
        while (true)
        {
            parseNumbers(PContentHandler.CABLEDUMP_RSIZE, -1);
            if (numbers != PContentHandler.CABLEDUMP_RSIZE)
                break;
            handler.cableDump(allnumbers);
        }
        if (numbers>0)
            incompleteRecord(ICABLEDUMP, PContentHandler.CABLEDUMP_RSIZE);
        
        handler.endSection(ICABLEDUMP);   
    }

    private void currentNoteDump() throws IOException, ParseException
    {
        handler.beginSection(ICURRENTNOTEDUMP, -1);
        while (true)
        {
            parseNumbers(PContentHandler.CURRENTNOTEDUMP_RSIZE, -1);
            if (numbers != PContentHandler.CURRENTNOTEDUMP_RSIZE)
                break;
            handler.currentNoteDump(allnumbers);
        }
        if (numbers>0)
            incompleteRecord(ICURRENTNOTEDUMP, PContentHandler.CURRENTNOTEDUMP_RSIZE);
        
        handler.endSection(ICURRENTNOTEDUMP);   
    }

    private void parseNumbers(int count, int more) throws IOException, ParseException
    {
        numbers = 0;
        for(;;)
        {
            switch (next())
            {
                case PScanner.NUMBER:
                    if (numbers>=allnumbers.length)
                    {
                        int[] expanded = new int[(numbers+1)*2];
                        System.arraycopy(allnumbers, 0, expanded, 0, numbers);
                        allnumbers = expanded;
                    }

                    if (more == numbers)
                    {
                        count+=scanner.getNumber();
                    }
                    
                    allnumbers[numbers++] = scanner.getNumber();
                    
                    take();
                    
                    if (--count<=0)
                        return;
                    break;
                case PScanner.INLINEWS:
                case PScanner.NEWLINEWS:
                    take();
                    break;
                case PScanner.EOF:
                    emitfatal("unexpected end of file");
                case PScanner.BROPEN:
                    return;
                default:
                    emiterror("expected <number> or '[', found "+found());
                    return;
            }
        } 
    }

    private int voiceAreaId() throws IOException, ParseException
    {
        skipws();
        if (next()!=PScanner.NUMBER || (scanner.getNumber()<0 || scanner.getNumber()>1))
        {
            emiterror("expected voice area id ('0'|'1'), found "+found());
            return -1;
        }
        take();
        return scanner.getNumber();
    }
    
    private String found() throws IOException
    {
        switch (next())
        {
            case PScanner.NUMBER:
                return "number <"+scanner.getNumber()+">";
            case PScanner.NEWLINEWS:
                return "[\\r\\n]+";
            case PScanner.INLINEWS:
                return "(<whitespace>)+";
            case PScanner.EOF:
                return "<EOF>";
        }
        
        String s = scanner.getString();
        if (s.length()>20)
        {
            return "'"+s.substring(0,20) + "' ("+(s.length()-20)+" more)";
        }
        else 
        {
            return "'"+s+"'";
        }
    }

    private void nameDump() throws IOException, ParseException
    {
        int voicearea = voiceAreaId();
        if (voicearea<0)
            return;
        
        handler.beginSection(INAMEDUMP, voicearea);
        
        StringBuilder sb = getCachedBuilder();
        
        loop:while (true)
        {
            skipws();
            switch(next())
            {
                case PScanner.BROPEN:
                    break loop;
                case PScanner.NUMBER:
                    break;
                default:
                    emiterror("expected <module index>, found "+found());
                    break loop;
            
            }
            
            int modindex = scanner.getNumber();
            take();
            sb.setLength(0);
         
            loop2:while (true)
            {
                switch (next())
                {
                    case PScanner.NEWLINEWS:
                        break loop2;
                    case PScanner.NUMBER:
                        sb.append(Integer.toString(scanner.getNumber()));
                        break;
                    case PScanner.EOF:
                        break loop2;
                    default:
                        sb.append(scanner.getString());
                        break;
                }
                take();
            }
            
            String modname;
            if (sb.length()>0 && Character.isWhitespace(sb.charAt(0)))
                modname = sb.substring(1);
            else
                modname = sb.toString();
            
            handler.moduleNameDump(modindex, modname);
        }

        handler.endSection(INAMEDUMP);
    }

    private StringBuilder cachedBuilder = null;
    
    private StringBuilder getCachedBuilder()
    {
        if (cachedBuilder == null)
            cachedBuilder = new StringBuilder();
        else
            cachedBuilder.setLength(0);
        return cachedBuilder;
    }
    
    private void notesSection() throws IOException, ParseException
    {
        handler.beginSection(INOTES,-1);
        
        StringBuilder notes = getCachedBuilder();

        boolean onlywhitespace = true; 
        int truncstart = -1;
        boolean firstnewline = true;
        final int ST_NONE = 0;
        final int ST_BROPEN = 1;
        final int ST_SLASH = 2;
        final int ST_NOTES = 3;
        final int ST_BRCLOSE = 4;
        int state = ST_NONE;
     
        loop:while (true)
        {   
            switch (next())
            {
                case PScanner.NEWLINEWS:
                    if (state!=ST_BRCLOSE)
                    {
                        state = ST_NONE;
                        if (state == ST_NONE)
                            truncstart = notes.length();
                    }
                    if ((!firstnewline)) 
                        appendNewlines(notes, scanner.getNumber());
                    onlywhitespace = true;
                    break;
                case PScanner.INLINEWS:
                    if (!firstnewline) notes.append(scanner.getString());
                    onlywhitespace = true;
                    break;
                case PScanner.NUMBER:
                    notes.append(Integer.toString(scanner.getNumber()));
                    state = ST_NONE;
                    onlywhitespace = false;
                    break;
                case PScanner.EOF:
                    if (state != ST_BRCLOSE)
                    {
                        truncstart = notes.length();
                        emiterror("unexpected end of file");
                    }
                    break loop;
                case PScanner.BROPEN:
                    if (state == ST_NONE)
                    {
                        if (!onlywhitespace)
                            truncstart = notes.length();
                        state = ST_BROPEN;
                    }
                    else state = ST_NONE;
                    notes.append(scanner.getString());
                    break;
                case PScanner.SLASH:
                    state = state == ST_BROPEN ? ST_SLASH : ST_NONE;
                    notes.append(scanner.getString());
                    onlywhitespace = false;
                    break;
                case PScanner.BRCLOSE:
                    if (state == ST_NOTES)
                    {
                        // finished ???
                        state = ST_BRCLOSE;
                    }
                    else
                    {
                        state = ST_NONE;
                    }
                    onlywhitespace = false;
                    notes.append(scanner.getString());
                    break;
                default:
                    String s = scanner.getString();
                    notes.append(s);
                    
                    if (state == ST_SLASH)
                    {
                        Integer sid = sectionMap.get(s.toLowerCase());
                        if (sid != null && sid.intValue() == INOTES)
                            state = ST_NOTES;
                        else
                            state = ST_NONE;
                    }
                    onlywhitespace = false;
                    break;
            }
            take();
            firstnewline = false;
        }
        
        if (state == ST_BRCLOSE && truncstart>=0 && truncstart<notes.length())
            notes.replace(truncstart, notes.length(), "");

        handler.notes(notes.toString());
        handler.endSection(INOTES);
    }
    
    private void appendNewlines(StringBuilder sb, int count)
    {
        while (count-->0)
            sb.append("\n");
    }
    
    private final int next() throws IOException
    {
        return token != -1 ? token : (token=scanner.nextToken());
    }
    
    private final void take()
    {
        token = -1;
    }
    
    private int wsinclusive(int tclass) throws IOException
    {
        int rtoken;
        loop: while (true)
        {
            rtoken = next();
            if (tclass == rtoken || rtoken == -1)
            {
                take();
                break loop;
            }
            
            switch (rtoken)
            {
                case PScanner.NEWLINEWS:
                case PScanner.INLINEWS:
                    break;
                default:
                    break loop;
            }
            take();
        }
        return rtoken;
    }
    
    private int inclusive(int tclass) throws IOException, ParseException
    {
        int rtoken;
        
        int position = getPosition();
        int line = getLineNumber();
        int column = getColumn();
        
        boolean takeerroneous = true;
        StringBuilder erroneous = null;
        final int emax = 20;
        
        loop: while (true)
        {
            rtoken = next();
            if (rtoken == tclass || rtoken == -1)
            {
                take();
                break loop;
            }
            
            switch (rtoken)
            {
                case PScanner.ANY:
                case PScanner.BRCLOSE:
                case PScanner.BROPEN:
                case PScanner.EOF:
                case PScanner.EQ:
                case PScanner.SLASH:
                    // set skipped flag
                    if (erroneous == null)
                      erroneous = new StringBuilder();

                    if (takeerroneous && erroneous != null && erroneous.length()<emax)
                    {
                        String s = scanner.getString();
                        erroneous.append(s.substring(0, Math.min(s.length(), 1+emax-erroneous.length())));
                    }
                    break;
                case PScanner.NUMBER:
                    // set skipped flag
                    if (erroneous == null)
                      erroneous = new StringBuilder();

                    if (takeerroneous && erroneous != null && erroneous.length()<emax)
                    {
                        erroneous.append(Integer.toString(scanner.getNumber()));
                    }
                    break;
                    
                case PScanner.INLINEWS:
                    // ignore 
                    if (takeerroneous && erroneous != null && erroneous.length()<emax)
                    {
                        String s = scanner.getString();
                        erroneous.append(s.substring(0, Math.min(s.length(), 1+emax-erroneous.length())));
                    }
                    break;
                case PScanner.NEWLINEWS:
                    if (erroneous != null)
                        takeerroneous = false;
                    // ignore 
                    break;
            }
            take();
        }
        
        if (erroneous!=null)
        {
            String msg = "erroneous characters";
            
            if (erroneous.length()>0)
            {
                msg += ": '"+erroneous+"'";
                
                int more = scanner.getPosition()-position-erroneous.length();
                if (more>0)
                    msg+=" ("+more+" more)";
            }
            
            emitwarning(line, column, msg);
        }
        
        return rtoken;
    }
    
    public void skipws() throws IOException
    {
        while (true)
        {
            switch (next())
            {
                case PScanner.INLINEWS:
                case PScanner.NEWLINEWS:
                    take();
                    break;
                default:
                    return;
            }
        }
    }

}
