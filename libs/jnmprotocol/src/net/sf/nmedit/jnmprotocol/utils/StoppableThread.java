/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003-2006 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.sf.nmedit.jnmprotocol.utils;

import net.sf.nmedit.jnmprotocol.utils.ThreadExecutionPolicy;

public class StoppableThread implements Runnable
{

    // controls the execution of the thread loop
    private ThreadExecutionPolicy executionPolicy;
    // will be called inside the thread loop
    private Runnable runnable;
    // The thread which executes this runnable.
    // The variable is uses to determine if 
    // run() is executed in the correct thread
    private volatile Thread workerThread;
    // indicates that the thread loop is exited
    private volatile boolean stopped = false;

    public StoppableThread(ThreadExecutionPolicy executionPolicy, Runnable runnable)
    {
        this.executionPolicy = executionPolicy;
        this.runnable = runnable;
    }
    
    /**
     * Starts a new worker thread in which this thread loop is executed.
     * If isStarted() is already true nothing will happen.  
     */
    public synchronized void start()
    {
        if (isStarted())
            return ;

        stopped = false;
        workerThread = new Thread(this);
        workerThread.setDaemon(false);
        workerThread.start();
    }
    
    /**
     * Stops the current worker thread.
     */
    public synchronized void stop()
    {
        workerThread = null;
        // this is necessary if the thread is sleeping,
        // otherwise it might 'never' wake up
        executionPolicy.interruptDelay();
    }
    
    /**
     * Returns true if start() was called but the thread is not finished yet.
     */
    public boolean isStarted()
    {
        return workerThread != null;
    }

    /**
     * Returns true if the thread loop was executed and is already finished.
     */
    public boolean isStopped()
    {
        return stopped;
    }
    
    public void run()
    {
        // the thread in which run() is executed
        Thread thisThread = Thread.currentThread();
        try
        {
            // loop runs as long the following conditions are true:
            // 1. the workerThread and this thread are identical
            // 2. the executionPolicy object does not indicate that the loop can exit
            while (workerThread == thisThread && (!executionPolicy.exitRequested()))
            {
                // checks if the runnable should be executed
                if (executionPolicy.executionRequested())
                {
                    runnable.run();
                }
                
                // sleep for a unspecified amount of time
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
            // set the stopped flag
            stopped = true;
            // erase the workerThread variable
            workerThread = null;
        }
    }

}
