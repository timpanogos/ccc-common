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
package com.ccc.tools.executor;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;

import com.ccc.tools.app.status.StatusTracker;

/**
 * An EMIT application friendly <code>ScheduledThreadPoolExecutor</code>.
 * </p>
 * Java's <code>java.util.Timer</code> has fallen in disfavor based on badly behaving timer tasks
 * being able to take down all tasks in the timer.  It is now suggested that a <code>ScheduledThreadPoolExecutor</code>
 * with at least two threads is used in the place of old timer.
 * </p>
 * Typically a <code>org.emitdo.service.util.Service</code> implementation's System Level will 
 * create an instance of this class as a singleton object to the whole application and include its reference
 * in the <code>Service<T></code>'s configuration object, which in turn is typically made available to all 
 * subsystems of the application, where they can utilize this timer as needed.
 */
//@ThreadSafe
public class ScheduledExecutor extends ScheduledThreadPoolExecutor
{
    private static final int initialCorePoolSize = 2; 
    
    /**
     * The default constructor.
     * </p>
     * This call will initialize a usable ScheduledThreadPoolExecutor with a core size of 2 threads.
     * However, to enable the custom thread pool factory and status tracking extensions to 
     * the queue the user must call the <code>init</code> method before utilizing 
     * this thread pool.
     * @see #init(ExecutorQueueConfig) 
     *  
     */
    public ScheduledExecutor()
    {
        super(initialCorePoolSize);
    }

    /**
     * Initialize our custom extended functionality of the SceduledThreadPoolExecutor.
     * </p>
     * This will initialize the thread pool with our thread pool factory functionality, and 
     * status tracking functionality.
     * @param config the configuration parameters used to configure the extended queue functionality.
     */
    public void init(ExecutorQueueConfig config)
    {
        setCorePoolSize(config.getCorePoolSize());
        setThreadFactory(new TimerExecutorThreadFactory(config.getThreadNamePrefix(), config.getStatusTracker(), config.getStatusSubsystemName()));
        config.getStatusTracker().setStatus(config.getStatusSubsystemName(), StatusTracker.Status.Ok);
    }
    
    private static class TimerExecutorThreadFactory implements ThreadFactory, UncaughtExceptionHandler
    {
        private final String namePrefix;
        private final AtomicInteger counter;
        private final StatusTracker statusTracker;
        private final String statusName;

        private TimerExecutorThreadFactory(String namePrefix, StatusTracker statusTracker, String statusName)
        {
            this.namePrefix = namePrefix + "-";
            this.counter = new AtomicInteger(-1);
            this.statusTracker = statusTracker;
            this.statusName = statusName;
        }

        @Override
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(r, namePrefix + counter.incrementAndGet());
            t.setUncaughtExceptionHandler(this);
            return t;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable e)
        {
            String msg = "Application Timer Executor thread uncaught exception in thread " + thread;
            LoggerFactory.getLogger(getClass()).error(msg, e);
            statusTracker.setStatus(statusName, StatusTracker.Status.Error);
        }
    }
}