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
 * Created on Dec 10, 2006
 */
package net.sf.nmedit.jpatch.clavia.nordmodular;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.AmpGain;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.BPM;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.CompressorLimiter;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.CompressorThreshold;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.DigitizerHz;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.DrumHz;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.DrumPartials;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.EnvelopeLevelDivider;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.EqHz;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.ExpanderGate;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.ExpanderHold;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.ExpanderThreshold;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.FilterHz1;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.FilterHz2;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Freqkbt;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.LFOHz;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.LogicDelay;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Note;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.NoteScale;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Offset64_2;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.OscHz;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.PartialRange;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Partials;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.PatternStep;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Phase;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Semitones;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.SmoothTime;
import net.sf.nmedit.jpatch.clavia.nordmodular.formatter.Timbre;
import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jpatch.spec.ModuleDescriptionsParser;
import net.sf.nmedit.jpatch.spec.formatter.FormatterRegistry;
import net.sf.nmedit.jpatch.transformation.Transformations;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class NM1ModuleDescriptions extends ModuleDescriptions
{

    public static NM1ModuleDescriptions parse(InputStream stream) throws ParserConfigurationException, SAXException, IOException
    {
        NM1ModuleDescriptions mod = new NM1ModuleDescriptions();
        
        try
        {
            ModuleDescriptionsParser.parse(mod, stream);
        }
        catch (SAXParseException spe)
        {
            System.err.println("@"+spe.getLineNumber()+":"+spe.getColumnNumber());
            spe.printStackTrace();
        }
        return mod;
    }

    public NM1ModuleDescriptions()
    {
        installNM1Formatters(getFormatterRegistry());
    }
    
    private void installNM1Formatters( FormatterRegistry registry )
    {
        registry.putFormatter("bpm", new BPM());
        registry.putFormatter("digitizer-hz", new DigitizerHz());
        registry.putFormatter("drum-hz", new DrumHz());
        registry.putFormatter("drumpartials", new DrumPartials());
        registry.putFormatter("eq-hz", new EqHz());
        registry.putFormatter("amp-gain", new AmpGain());
        registry.putFormatter("filter-hz-1", new FilterHz1());
        registry.putFormatter("filter-hz-2", new FilterHz2());
        registry.putFormatter("lfo-hz", new LFOHz());
        registry.putFormatter("logic-delay", new LogicDelay());
        registry.putFormatter("note", new Note());
        registry.putFormatter("note-scale", new NoteScale());
        registry.putFormatter("osc-hz", new OscHz());
        registry.putFormatter("partial-range", new PartialRange());
        registry.putFormatter("partials", new Partials());
        registry.putFormatter("phase", new Phase());
        registry.putFormatter("semitones", new Semitones());
        registry.putFormatter("smooth-time", new SmoothTime());
        registry.putFormatter("freq-kbt", new Freqkbt());
        registry.putFormatter("compressor-limiter", new CompressorLimiter());
        registry.putFormatter("offset-64-2", new Offset64_2());
        registry.putFormatter("compressor-treshold", new CompressorThreshold());
        registry.putFormatter("expander-treshold", new ExpanderThreshold());
        registry.putFormatter("envelope-level-divider", new EnvelopeLevelDivider());
        registry.putFormatter("timbre", new Timbre());
        registry.putFormatter("pattern-step", new PatternStep());
        registry.putFormatter("expander-gate", new ExpanderGate());
        registry.putFormatter("expander-hold", new ExpanderHold());
    }

    public void setTransformations(Transformations t)
    {
        this.transformations = t;
    }

}
