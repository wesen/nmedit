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
 * Created on May 5, 2006
 */
package net.sf.nmedit.nomad.util.computing;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.SwingUtilities;

public class Worker 
{

    private Queue<WorkerJob> jobQueue = new LinkedList<WorkerJob>();
    private Executor executor = new Executor();
    
    public void enqueue(WorkerJob job)
    {
        jobQueue.offer(job);
        executor.trigger();
    }

    private class Executor implements Runnable, WorkerJob
    {
        private int triggerCount = 0;
        
        public void trigger()
        {
            if ((triggerCount==0) && (!jobQueue.isEmpty()))
            {
                triggerCount++;
                SwingUtilities.invokeLater(this);
            }
        }
        
        public void run()
        {
            triggerCount--;
            step();
        }

        public boolean isComplete()
        {
            return jobQueue.isEmpty();
        }

        public void step()
        {
            if (jobQueue.isEmpty())
            {
                return ;
            }
            WorkerJob job = jobQueue.element();
            try
            {
                job.step();
            } 
            catch (RuntimeException e)
            {
                jobQueue.remove(); // remove failed job
                throw e;
            }
         
            if (job.isComplete())
                jobQueue.remove();
            
            trigger();
        }
    }
    
}
