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
 * Created on May 24, 2006
 */
package net.sf.nmedit.nomad.synth.nord.protocol;

/**
 * A self regulating thread that disables itself when no work
 * was done for a specified time. If the thread is disabled
 * and {@link #signalPendingWork()} indicates that some work
 * has to be done, it will awake again to do his work.
 * 
 * @author Christian Schneider
 */
public abstract class SelfRegulatingThread extends Thread
{
    
    /**
     * The lock used for the hibernation state.
     */
    private final Object hibernation = new Object();
    
    /**
     * milleseconds after after the last message was processed
     * the thread becomes disabled
     */
    private final long HIBERNATION_DELAY;
    
    /**
     * if the thread is awake calls to {@link #processMessages()} are delayed
     * by the given milliseconds
     */
    private final long PROCESS_MESSAGES_DELAY ;  

    /**
     * true when thread is not in hibernation state
     */
    private boolean awake = true;

    // Time in milliseconds when the last message was processed.
    private long lastmessage = 0;
    
    /**
     * Creates a new self regulating thread.
     * 
     * @param hibernationDelay delay after which the thread is disabled when no work
     *  was done
     * 
     * @param processMessageDelay delay between {@link #processMessages()} calls
     */
    public SelfRegulatingThread(long hibernationDelay, long processMessageDelay)
    {
        this.HIBERNATION_DELAY = hibernationDelay;
        this.PROCESS_MESSAGES_DELAY = processMessageDelay;
    }
    
    /**
     * The thread loop.
     */
    public void run()
    {
        
        // As long as the thread is not interrupted do...
        while (!isInterrupted())
        {
            // process messages ...
            if (processMessages())
            {
                // at least one messages has been processed
                // => update lastmessages
                lastmessage = System.currentTimeMillis();
            }
            // no messages were processed
            // check if the last messages was processed long enough ago
            // to enter the hibernation state
            else if (System.currentTimeMillis()>lastmessage+HIBERNATION_DELAY)
            {
                // enter hibernation state
                // thread will sleep until it receives signalPendingWork() 
                // or the thread is interrupted
                hibernate();
            }
            // the last messages was not long ago, when the delay time
            // is specified delay until the next time processMessages() will be called
            else if (PROCESS_MESSAGES_DELAY>0)
            {
                try
                {
                    Thread.sleep(PROCESS_MESSAGES_DELAY);
                }
                catch (InterruptedException e)
                {
                    // no problem
                }
            }
            // give other threads some time when no delay time is specified
            else
            {
                Thread.yield();
            }
        }
    }
    
    /**
     * Sets the thread in hibernation state, if not already is.
     */
    private void hibernate()
    {
        // first check if the thread is not already in hibernation state
        if (awake)
        {
            synchronized (hibernation)
            {
                try
                {
                    // update indicator
                    awake = false;
                    while (!awake) // wait() might return too early
                    {
                        // wait until notify() or notifyAll() has been called
                        // (or the thread is interrupted)
                        hibernation.wait();
                    }
                }
                catch (InterruptedException e)
                {
                    // nothing to handle
                }
                finally 
                {
                    // just in case the thread was avakened
                    // from somewhere else then wakeup()
                    // update indicator
                    awake = true;
                }
            }
        }
    }
    
    /**
     * Awakes the thread from hibernation state, if not already awakened.
     */
    private void wakeup()
    {
        // first check if the thread is not already awake
        if (!awake)
        {
            // no it is not
            synchronized (hibernation)
            {
                // we thread this as message
                lastmessage = System.currentTimeMillis();
                // update indicator
                awake = true;
                // awake thread
                hibernation.notifyAll();
            }
        }
    }

    /**
     * Processes pending messages in the thread loop.
     * The return value indicates if work was done or not.
     * If for {@link #HIBERNATION_DELAY} time no messages
     * were processed, the thread will set itself disabled,
     * until {@link #signalPendingWork()} indicates that
     * some work has to be done.
     * 
     * @return true when messages have been processed
     */
    protected abstract boolean processMessages();

    /**
     * Notifies the thread that some work has to be done.
     * If the thread is temporarily disabled, it will
     * enabled again.
     */
    public void signalPendingWork()
    {
        // the thread has some work to do
        // so we have to wake the thread up
        // if it is sleeping
        wakeup();
    }

    /**
     * Stops the thread from running and waits until
     * the thread is stopped or specified time is elapsed.
     * 
     * @param timeout 
     * @return Returns false when the specified time is elapsed
     * before the thread was stopped.
     */
    public boolean dieAndWait(long timeout)
    {
        if (!isAlive())
            return true ;
        
        // first wake the thread up
        wakeup();
        // interrupt the thread
        interrupt();
        // make timeout absolute
        timeout = System.currentTimeMillis()+timeout;
        // as long as the thread is not dead
        while (isAlive())
        {
            // check if timeout has been reached
            if (System.currentTimeMillis()>timeout)
            {
                // thread is still alive
                // the operation failed (false)
                return false;
            }
            // give other threads a chance to do their job
            Thread.yield();
        }
        // the thread is not alive anymore, success (true)
        return true;
    }

    /**
     * Stops the thread from running.
     * @return Returns false when the thread has not stopped immediately.
     */
    public boolean die()
    {
        // die and do not wait (timeout:=0)
        return dieAndWait(0);
    }
    
}
