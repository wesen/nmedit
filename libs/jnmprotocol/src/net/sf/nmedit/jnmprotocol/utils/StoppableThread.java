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
 * Created on Jan 6, 2007
 */
package net.sf.nmedit.jnmprotocol.utils;

public class StoppableThread implements Runnable
{

    private ThreadExecutionPolicy executionPolicy;
    private Runnable runnable;
    private volatile Thread workerThread;
    private volatile boolean stopped = false;

    public StoppableThread(ThreadExecutionPolicy executionPolicy, Runnable runnable)
    {
        this.executionPolicy = executionPolicy;
        this.runnable = runnable;
    }
    
    public void start()
    {
        if (isStarted())
            return ;

        stopped = false;
        workerThread = new Thread(this);
        workerThread.setDaemon(false);
        workerThread.start();
    }
    
    public void stop()
    {
        workerThread = null;
        executionPolicy.interruptDelay();
    }
    
    public boolean isStarted()
    {
        return workerThread != null;
    }

    public boolean isStopped()
    {
        return stopped;
    }
    
    public void run()
    {
        Thread thisThread = Thread.currentThread();
        try
        {
            while (workerThread == thisThread && (!executionPolicy.exitRequested()))
            {
                if (executionPolicy.executionRequested())
                {
                    runnable.run();
                }
                
                try
                {
                    executionPolicy.delay();
                }
                catch (InterruptedException e)
                {
                    // no op
                }
            }
        }
        finally
        {
            stopped = true;
            workerThread = null;
        }
    }

}
