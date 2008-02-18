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
 */package net.sf.nmedit.jsynth.clavia.nordmodular.worker;

import java.util.LinkedList;
import java.util.Queue;

import net.sf.nmedit.jnmprotocol2.NmProtocol;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;

public class Scheduler 
{
    
    private Queue<ScheduledWorker> workerQueue = 
        new LinkedList<ScheduledWorker>();
    private NmProtocol protocol;

    public Scheduler(NmProtocol protocol)
    {
        this.protocol = protocol;
    }
    
    public void clear()
    {
        synchronized (workerQueue)
        {
            workerQueue.clear();   
        }
    }
    
    public void offer(ScheduledWorker worker)
    {
        synchronized (workerQueue)
        {
            workerQueue.offer(worker);   
        }
        
        // we might have to wake up the thread
        protocol.activity();
    }
    
    public void remove(ScheduledWorker worker)
    {
        synchronized (workerQueue)
        {
            workerQueue.offer(worker);   
        }
    }
    
    public boolean contains(ScheduledWorker worker)
    {
        synchronized (workerQueue)
        {
            return workerQueue.contains(worker);   
        }
    }
    
    private void removeHead()
    {
        synchronized (workerQueue)
        {
            workerQueue.remove();
        }
    }
    
    public void schedule() throws SynthException
    {
        ScheduledWorker head;
        synchronized (workerQueue)
        {
            head = workerQueue.peek();
        }

        if (head == null)
            return;

        try
        {
            head.runWorker();
        }
        catch (Throwable t)
        {
            removeHead();
            head.aborted();
            throw NmUtils.transformException(t);
        }
        
        if (head.isWorkerFinished())
            removeHead();
    }

}
