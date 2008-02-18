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
package net.sf.nmedit.jpatch.clavia.nordmodular;

import net.sf.nmedit.jnmprotocol2.LightMessage;
import net.sf.nmedit.jnmprotocol2.MeterMessage;
import net.sf.nmedit.jpatch.PLight;
import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.PModule;

public class LightProcessor
{
    
    // modules containing lights are order by
    // - poly voice area before common voice area
    // - ascending order by module index

    // leds
    private PLight[] LEDs = new PLight[30];
    private int LEDvaPolyEnd = 0;
    private int LEDCount = 0;

    // meter
    private PLight[] METERs = new PLight[10];
    private int METERvaPolyEnd = 0;
    private int METERCount = 0;
    
    public void registerModule(PModule module)
    {
        if (module.getLightCount()<=0)
            return;
        
        int moduleIndex = module.getComponentIndex();
        int vaIndex = ((VoiceArea) module.getParentComponent()).isPolyVoiceArea()?0:1;
        PLight light = module.getLight(0);
        
        switch (light.getType())
        {
            case PLightDescriptor.TYPE_LED:
                registerLED(moduleIndex, vaIndex, light);
                break;
            case PLightDescriptor.TYPE_LED_ARRAY:
            case PLightDescriptor.TYPE_METER:
            {
                PLight a = light;
                PLight b = module.getLightCount()>1 ? module.getLight(1):null;
                
                if (b != null && a.getIntAttribute("index", -1)==1)
                {
                    PLight tmp = b;
                    b = a;
                    a = tmp;
                }
                
                registerMeter(moduleIndex, vaIndex, b, a);
            }
                break;
        }
    }

    public void unregisterModule(PModule module)
    {
        if (module.getLightCount()<=0)
            return;

        int moduleIndex = module.getComponentIndex();
        int vaIndex = ((VoiceArea) module.getParentComponent()).isPolyVoiceArea()?0:1;
        PLight light = module.getLight(0);

        switch (light.getType())
        {
            case PLightDescriptor.TYPE_LED:
                unregisterLED(moduleIndex, vaIndex, light);
                break;
            case PLightDescriptor.TYPE_METER:
            case PLightDescriptor.TYPE_LED_ARRAY:
                unregisterMeter(moduleIndex, vaIndex, light);
                break;
        }
    }
    
    private int moduleIndex(PLight l)
    {
        return l.getParentComponent().getComponentIndex();
    }

    private void registerLED(int moduleIndex, int vaIndex, PLight light)
    {
        int start;
        int end;
        int insertIndex;
        if (vaIndex < 1)
        {
            // poly va
            start = 0;
            end = LEDvaPolyEnd;
        }
        else
        {
            start = LEDvaPolyEnd;
            end = LEDCount;
        }
        insertIndex = start;
        
        for (;insertIndex<end;insertIndex++)
        {
            PLight posLight = LEDs[insertIndex];
            int posModuleIndex = moduleIndex(posLight);
            if (posModuleIndex>=moduleIndex) break;
        }
        
        if (LEDCount>=LEDs.length)
        {
            // create space
            PLight[] tmp = new PLight[LEDs.length+10];
            System.arraycopy(LEDs, 0, tmp, 0, LEDCount);
            LEDs = tmp;
        }
        
        // move elements>=insertIndex
        System.arraycopy(LEDs, insertIndex, LEDs, insertIndex+1, LEDCount-insertIndex);
        LEDs[insertIndex] = light;
        LEDCount++;
        if (vaIndex<1)
            LEDvaPolyEnd++;
        
     //   System.out.println("LED registered @ "+insertIndex);
    }

    private void unregisterLED(int moduleIndex, int vaIndex, PLight light)
    {
        int start;
        int end;
        if (vaIndex < 1)
        {
            // poly va
            start = 0;
            end = LEDvaPolyEnd;
        }
        else
        {
            start = LEDvaPolyEnd;
            end = LEDCount;
        }
        
        int index = -1;
        for (int i=start;i<end;i++)
        {
            PLight posLight = LEDs[i];
            int posModuleIndex = moduleIndex(posLight);
            if (posModuleIndex == moduleIndex)
            {
                index = i;
                break;
            }
        }
        
        if (index>=0)
        {
            System.arraycopy(LEDs, index+1, LEDs, index, LEDCount-index);
            LEDs[LEDCount-1] = null;
            LEDCount--;
            if (vaIndex < 1)
                LEDvaPolyEnd--;
        }
        //System.out.println("LED unregistered @ "+index);
    }

    private void registerMeter(int moduleIndex, int vaIndex, PLight b, PLight a)
    {
        int start;
        int end;
        int insertIndex;
        if (vaIndex < 1)
        {
            // poly va
            start = 0;
            end = METERvaPolyEnd;
        }
        else
        {
            start = METERvaPolyEnd;
            end = METERCount*2;
        }
        insertIndex = start;
        
        for (;insertIndex<end;insertIndex+=2)
        {
            PLight posLight = METERs[insertIndex+1];
            int posModuleIndex = moduleIndex(posLight);
            if (posModuleIndex>=moduleIndex) break;
        }
        
        if (METERCount*2>=METERs.length-1)
        {
            // create space
            PLight[] tmp = new PLight[METERs.length+10];
            System.arraycopy(METERs, 0, tmp, 0, METERCount*2);
            METERs = tmp;
        }
        
        // move elements>=insertIndex
        System.arraycopy(METERs, insertIndex, METERs, insertIndex+2, METERCount*2-insertIndex);
        METERs[insertIndex] = b;
        METERs[insertIndex+1] = a;
        METERCount++;
        if (vaIndex<1)
            METERvaPolyEnd+=2;
        /*
        System.out.println("Meter registered @ "+insertIndex+
        " ("+METERs[insertIndex]+", "+METERs[insertIndex+1]+")"        
        );
        
        for (int i=0;i<METERCount*2;i++)
            System.out.print((METERs[i]!=null)+", ");
        System.out.println();
        */
        
    }

    private void unregisterMeter(int moduleIndex, int vaIndex, PLight light)
    {
        int start;
        int end;
        if (vaIndex < 1)
        {
            // poly va
            start = 0;
            end = METERvaPolyEnd;
        }
        else
        {
            start = METERvaPolyEnd;
            end = METERCount*2;
        }
        
        int index = -1;
        for (int i=start;i<end;i+=2)
        {
            PLight posLight = METERs[i+1];
            int posModuleIndex = moduleIndex(posLight);
            if (posModuleIndex == moduleIndex)
            {
                index = i;
                break;
            }
        }
        /*
        Object mb = null;
        Object ma = null;
        */
        if (index>=0)
        {
            System.arraycopy(METERs, index+2, METERs, index, METERCount*2-index-2);
/*
            mb = METERs[METERCount*2-1];
            ma = METERs[METERCount*2-2]; 
            */
            METERs[METERCount*2-1] = null;
            METERs[METERCount*2-2] = null;
            METERCount--;
            if (vaIndex < 1)
                METERvaPolyEnd-=2;
        }
     //   System.out.println("METER unregistered @ "+index+" ("+ma+","+mb+")");
    }

    private static final String[] lightValueKeys = new String[] {
            "light0",
            "light1",
            "light2",
            "light3",
            "light4",
            "light5",
            "light6",
            "light7",
            "light8",
            "light9",
            "light10",
            "light11",
            "light12",
            "light13",
            "light14",
            "light15",
            "light16",
            "light17",
            "light18",
            "light19"
    };
    
    private static final String KEY_START_INDEX ="startIndex";

    public void processLightMessage(LightMessage message)
    {
        int startIndex = message.get(KEY_START_INDEX);
     
        
        // System.out.println("light message start="+startIndex);
        
        for (int i=0;i<20;i++)
        {
            int value = message.get(lightValueKeys[i]);            
            int index = startIndex+i;
            
            if (index>=0 && index<LEDCount)
            {
                // System.out.println("setValue@"+index+" to "+value);
                LEDs[index].setValue(value);
            }
        }
    }
    private static final String[] meterValueKeysA = new String[] {
            "a0", "a1", "a2", "a3", "a4"
    };
    private static final String[] meterValueKeysB = new String[] {
            "b0", "b1", "b2", "b3", "b4"
    };

    public void processMeterMessage(MeterMessage message)
    {
        int startIndex = message.get(KEY_START_INDEX);

        for (int i=0;i<=4;i++)
        {
            int index = startIndex+(i*2);
            if (index>=0 && index<METERCount*2)
            {
                PLight b = METERs[index];
                PLight a = METERs[index+1];
                
               // System.out.print("("+at(b)+":"+message.get(meterValueKeysB[i])+","+at(a)+":"+message.get(meterValueKeysA[i])+"),");
                

                if (b == null)
                {
                    a.setValue(message.get(meterValueKeysB[i]));
                }
                else
                {
                    if (a != null)
                    {
                        a.setValue(message.get(meterValueKeysA[i]));
                        b.setValue(message.get(meterValueKeysB[i]));
                    }
                    else
                    {
                        b.setValue(message.get(meterValueKeysB[i]));
                    }
                }
            }
        }
        //System.out.println();

    }
/*
    private String at(PLight b)
    {
        if (b == null) return "?";
        else return b.getParentComponent().getTitle();
    }
*/    
}
