/*
**  Copyright (c) 2016, Chad Adams.
**
**  This program is free software: you can redistribute it and/or modify
**  it under the terms of the GNU Lesser General Public License as 
**  published by the Free Software Foundation, either version 3 of the 
**  License, or any later version.
**
**  This program is distributed in the hope that it will be useful,
**  but WITHOUT ANY WARRANTY; without even the implied warranty of
**  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
**  GNU General Public License for more details.

**  You should have received copies of the GNU GPLv3 and GNU LGPLv3
**  licenses along with this program.  If not, see http://www.gnu.org/licenses
*/
package com.ccc.tools;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("javadoc")
public class TimeoutTask implements Callable<Void>
{
    private final FutureTask<Void> timeoutTask;
    private final AtomicBoolean timedout;
    private final AtomicBoolean completed;
    
    public TimeoutTask(ScheduledThreadPoolExecutor timer, int timeout)
    {
        timedout = new AtomicBoolean(false);
        completed = new AtomicBoolean(false);
        timeoutTask = new FutureTask<Void>(this);
        timer.schedule(timeoutTask, timeout, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public Void call() throws Exception
    {
        synchronized (this)
        {
            completed.set(true);
            timedout.set(true);
            notifyAll();
        }
        return null;
    }
    
    public void completed()
    {
        synchronized(this)
        {
            completed.set(true);
            timedout.set(false);
            timeoutTask.cancel(true);   
            notifyAll();
        }
    }
    
    public void cancel()
    {
        synchronized(this)
        {
            completed.set(false);
            timedout.set(false);
            timeoutTask.cancel(true);   
            notifyAll();
        }
    }
    
    /**
     * 
     * @return false if timed out, true if completed normally
     * @throws InterruptedException 
     */
    public boolean waitForComplete() throws InterruptedException
    {
        synchronized (this)
        {
            if(completed.get())
                return timedout.get();
            wait();
        }
        return timedout.get();
    }
}
