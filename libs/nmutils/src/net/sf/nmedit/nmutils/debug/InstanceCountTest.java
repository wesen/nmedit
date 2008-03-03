/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.nmutils.debug;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to test for memory leaks.
 * 
 * Usage:
 * 
 * class TestedClass
 * {
 * 
 *   private static InstanceCountTest.ClassInstanceCounter ICT = 
 *      InstanceCountTest.getCounter(TestedClass.class);
 *
 *   public TestedClass()
 *   {
 *      if (InstanceCountTest.isEnabled()) ICT.newInstance();
 *   }
 *   
 *   protected void finalize() throws Throwable
 *   {
 *      if (InstanceCountTest.isEnabled()) ICT.instanceFinalized();
 *      super.finalize();
 *   }
 *
 * }
 */
public class InstanceCountTest
{
    
    private static final boolean ENABLED = true;
    
    public static boolean isEnabled()
    {
        return ENABLED;
    }

    public static final class ClassInstanceCounter
    {
        private Class<?> forClass;
        private int count;
        
        private ClassInstanceCounter(Class<?> forClass)
        {
            this.forClass = forClass;
            this.count = 0;
        }
        
        public Class<?> getForClass()
        {
            return forClass;
        }
        
        public void newInstance()
        {
            ensureAutoDumpStarted();
            synchronized (forClass)
            {
                count++;
            }
            modcounter++;
        }

        public synchronized void instanceFinalized()
        {
            synchronized (forClass)
            {
                count--;
            }
            modcounter++;
        }
        
        public int getCount()
        {
            synchronized (forClass)
            {
                return count;
            }
        }
        
        public String toString()
        {
            return "Instances for "+forClass+": "+getCount();
        }
        
    }
    
    private Map<Class<?>, ClassInstanceCounter> map = Collections.synchronizedMap(new HashMap<Class<?>, ClassInstanceCounter>());
    private static InstanceCountTest instance = new InstanceCountTest();
    private static boolean autoDumpStarted = false;
    private static volatile int modcounter ;

    
    private static void ensureAutoDumpStarted()
    {
        if (!isEnabled()) 
            return;
        if (!autoDumpStarted)
        {
            synchronized (instance)
            {
                autoDumpStarted = true;
            }

            Thread t = new Thread(new AutoDump());
            t.setDaemon(true);
            t.setName("AutoDump");
            t.start();
            
        }
    }
    
    private static InstanceCountTest getInstance()
    {
        return instance;
    }
    
    public static ClassInstanceCounter getCounter(Class<?> forClass)
    {
        InstanceCountTest test = getInstance(); 
        ClassInstanceCounter counter = test.map.get(forClass);
        if (counter == null)
        {
            counter = new ClassInstanceCounter(forClass);
            test.map.put(forClass, counter);
            modcounter++;
        }
        return counter;
    }
    
    public void printDump()
    {
        Runtime rt = Runtime.getRuntime();
        System.out.println("Instances for (free:"+rt.freeMemory()+" byte, max:"+rt.maxMemory()+")");
        for (ClassInstanceCounter counter: map.values())
        {
            System.out.println("\t"+counter.getForClass()+": "+counter.getCount());
        }
    }
    
    
    private static class AutoDump implements Runnable
    {

        public void run()
        {
            int lastmod = modcounter-10;
            System.out.println("[AutoDump] Started.");
            
            while (!Thread.interrupted())
            {
                if (instance.map.size()>0 && modcounter != lastmod)
                {
                    instance.printDump();
                    lastmod = modcounter;
                }
                
                try
                {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e)
                {
                    // no op
                }
            }
            autoDumpStarted = false;

            System.out.println("[AutoDump] Stopped.");
        }
        
    }
    
}
