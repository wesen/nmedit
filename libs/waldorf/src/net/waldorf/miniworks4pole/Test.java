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
package net.waldorf.miniworks4pole;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

import javax.swing.JFrame;

import net.sf.nmedit.jnmprotocol.utils.NmLookup;
import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.ModuleDescriptionsParser;
import net.sf.nmedit.jpdl.Protocol;
import net.sf.nmedit.jsynth.midi.MidiPlug;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;
import net.waldorf.miniworks4pole.jpatch.MWPatch;
import net.waldorf.miniworks4pole.jprotocol.WProtocol;
import net.waldorf.miniworks4pole.jsynth.Miniworks4Pole;
import net.waldorf.miniworks4pole.jtheme.JTMWPatch;
import net.waldorf.miniworks4pole.jtheme.WMContext;

import org.xml.sax.InputSource;

public class Test
{
    
    static final String PATH = "/home/christian/CVS-Arbeitsbereich/nmedit/libs/waldorf/data/";

    public static void main(String[] args) throws Exception
    {
        String path = PATH;
        String miniworksui = "miniworks-ui.xml";
        testStore(path, miniworksui);
        
    }
 
    private static void testStore(String path, String miniworksui) throws Exception
    {
        ClassLoader classLoader = new RelativeClassLoader(new File(path+"theme/"));
        InputStream is = classLoader.getResourceAsStream(miniworksui);
        
        if (is == null)
            throw new NullPointerException("stream is null");
        
        try
        {
            testStore(classLoader, path, new InputSource(is));
        }
        finally
        {
            is.close();
        }
    }

    private static void testStore(ClassLoader classLoader, String path, InputSource source) throws Exception
    {
        
        ModuleDescriptions md = MWHelper.createModuleDescriptions();
        
        InputStream is = new FileInputStream(PATH+"4pole-example.xml");
        
        try
        {
            ModuleDescriptionsParser.parse(md, is);
        }
        finally
        {
            is.close();
        }
        
        WProtocol.setProtocol(new Protocol(new FileReader(PATH+"codecs/waldorf-miniworks-4pole.pdl")));
        
        MWPatch patch = new MWPatch(md);
        
        
        DefaultStorageContext context = MWHelper.createStorageContext(classLoader);
        context.parseStore(source);

        WMContext jtcontext = new WMContext();

        JTMWPatch jtpatch = new JTMWPatch(context, jtcontext, md, patch);
        

        /*
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300,300);
        f.getContentPane().add(c);
        f.setVisible(true);*/
        

        JFrame f = new JFrame("Waldorf MiniWorks 4Pole");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(10, 10, 800, 300);
        Container cp = f.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(jtpatch, BorderLayout.CENTER);

        
        Miniworks4Pole synth = new Miniworks4Pole(md);

        synth.getInPort().setPlug(new MidiPlug(NmLookup.getFirstInputDevice()));
        synth.getOutPort().setPlug(new MidiPlug(NmLookup.getFirstOutputDevice()));
        
        synth.setConnected(true);
        
        synth.getSlot(0).setPatch(patch);
        
        /*
        cutoff.setAdapter(new ControllerAdapter(output.getReceiver(),
                MiniworksMidiMessage.CN_CUTOFF_FREQUENCY));
        
        resonance.setAdapter(new ControllerAdapter(output.getReceiver(),
                MiniworksMidiMessage.CN_RESONANCE));
        
        lfoSpeed.setAdapter(new ControllerAdapter(output.getReceiver(),
                MiniworksMidiMessage.CN_LFO_SPEED));
        */
        f.setVisible(true);

    }
    
}

