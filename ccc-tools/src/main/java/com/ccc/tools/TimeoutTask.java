/*
**  Copyright (c) 2016, Cascade Computer Consulting.
**
**  Permission to use, copy, modify, and/or distribute this software for any
**  purpose with or without fee is hereby granted, provided that the above
**  copyright notice and this permission notice appear in all copies.
**
**  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
**  WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
**  MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
**  ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
**  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
**  ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
**  OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
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
