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
 * Created on Aug 29, 2006
 */
package net.sf.nmedit.jsynth.clavia.nordmodular;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.MidiDevice;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.nmedit.jnmprotocol.AckMessage;
import net.sf.nmedit.jnmprotocol.NmProtocolListener;
import net.sf.nmedit.jnmprotocol.utils.NmLookup;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchExporter;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.PatchFileWriter;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.ReceivePatchWorker;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;
import net.sf.nmedit.jsynth.midi.MidiPlug;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.xml.sax.SAXException;

/**
 * junit test for 
 * @author Christian Schneider
 */
public class NM1Test 
{
    
    private static NM1ModuleDescriptions moduleDescriptions = null;
    private static NordModular nm1; 

    @BeforeClass
    public static void initModuleDescriptions() throws IOException, ParserConfigurationException, SAXException
    {
        InputStream is = new FileInputStream("RESOURCE/modules.xml");
        moduleDescriptions = NM1ModuleDescriptions.parse(is);
    }

    @AfterClass
    public static void disposeModuleDescriptions() throws IOException
    {
        moduleDescriptions = null;
    }
    
    @BeforeClass
    public static void initNordModular() throws Exception 
    {
        nm1 = new NordModular(NmUtils.parseModuleDescriptions(), true);
        MidiDevice.Info[] devs = NmLookup.lookup(NmLookup.getHardwareDevices(), 1, 600);
        nm1.getPCInPort().setPlug(new MidiPlug(devs[0]));
        nm1.getPCOutPort().setPlug(new MidiPlug(devs[1]));
        nm1.addSynthesizerStateListener(new SynthesizerStateListener(){
            public void synthConnectionStateChanged( SynthesizerEvent e )
            {
                Synthesizer synth = e.getSynthesizer();
                System.out.println(synth.getName()+": "+(synth.isConnected()?"connected":"disconnected"));
            }});
        nm1.setConnected(true);
    }

    @AfterClass
    public static void disposeNordModular() throws SynthException 
    {
        nm1.setConnected(false);
    }
    
    public void createPatch() throws Exception
    {
        //nm1.getSlot(0).
    }
    
   // @Test
    public void receivePatch() throws Exception
    {
        final int slot = 0;

        RequestPatchWorker rw = new RequestPatchWorker();
        nm1.addProtocolListener(rw);
        try
        {
            nm1.getProtocol().send(NmUtils.createRequestPatchMessage(slot));
            
            long timeout = System.currentTimeMillis()+3000;
            while (rw.pid < 0)
            {

                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    // ignore
                }
                
                if (System.currentTimeMillis()>timeout)
                    break;
            }
            
            if (rw.pid < 0)
                throw new Exception("no pid");
            
        }
        finally
        {
            nm1.removeProtocolListener(rw);
        }
        
        System.out.println("requesting patch(pid="+rw.pid+")");
        
        ReceivePatchWorker worker = new ReceivePatchWorker(0, rw.pid, moduleDescriptions);
        nm1.addProtocolListener(worker);
        try
        {
            long timeout = System.currentTimeMillis()+(3*1000);
            worker.getPatch(nm1);
            while (nm1.isConnected())
            {
                if (worker.complete() || worker.hasError())
                    break;
                
                try
                {
                    nm1.getProtocol().awaitWorkSignal(100);
                }
                catch (InterruptedException e)
                {
                    // ignore
                }
                
                if (System.currentTimeMillis()>timeout)
                    break;
            }
        }
        finally
        {
            nm1.removeProtocolListener(worker);
        }
        
        NMPatch patch = worker.getPatch();
        
        PatchExporter pe = new PatchExporter();
        pe.export(patch, new PatchFileWriter(System.out));
        System.out.flush();

        if (!worker.complete())
            throw new Exception("Patch is not complete. sections: "+worker.getSections());
        
        if (worker.hasError())
            throw new Exception("Patch has errors");
    }

    private static class RequestPatchWorker extends NmProtocolListener
    {
        
        private int pid = -1;

        public void messageReceived(AckMessage message) 
        {
            this.pid = message.get("pid1");
        }
    }
    
}
