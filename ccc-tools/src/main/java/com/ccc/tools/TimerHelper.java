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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Class TimerHelper.
 */
public class TimerHelper extends TimerTask
{
    private static Timer timer;
    private final AtomicBoolean monitor;
    private final AtomicBoolean errorFlag;
    
    /**
     * Instantiates a new timer helper.
     *
     * @param monitor the monitor
     * @param errorFlag the error flag
     */
    public TimerHelper(AtomicBoolean monitor, AtomicBoolean errorFlag)
    {
        this.monitor = monitor;
        this.errorFlag = errorFlag;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        synchronized(monitor)
        {
            monitor.set(true);
            errorFlag.set(true);
            monitor.notifyAll();
        }
    }
    
    // this timer can be used for any TimerTask if you are just looking to share an existing timer
    /**
     * Schedule task.
     *
     * @param task the task
     * @param delay the delay
     */
    public static void scheduleTask(TimerTask task, long delay)
    {
        if(timer == null)
            timer = new Timer();
        timer.schedule(task, delay);
    }
    
    // this timer can be used for any TimerTask that needs fixed repeated delays
    /**
     * Schedule task.
     *
     * @param task the task
     * @param delay the delay
     * @param period the period
     */
    public static void scheduleTask(TimerTask task, long delay, long period)
    {
        if(timer == null)
            timer = new Timer();
        timer.schedule(task, delay, period);
    }
}

