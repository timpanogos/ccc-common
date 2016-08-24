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


/**
 * Declares the required BlockingExecutorConfig interface
 */
public interface BlockingExecutorConfig extends ExecutorQueueConfig
{
    /**
     * Get the maximum queue size.
     * </p>
     * @return the maximum queue size.
     */
    public int getMaximumPoolSize();

    /**
     * Get the allow core threads to timeout flag.
     * </p>
     * Should the number of core threads collapse if no activity is seen?
     * The default is true.
     * @return enable the core threads should collapse if true and remain if false.
     * @see #getKeepAliveTime()
     */
    public boolean isAllowCoreThreadTimeout();

    /**
     * Get the keep alive delay for core threads.
     * </p>
     * If the allow core thread timeout is true, this is the delay before they
     * will start to collapse.
     * @return the delay in milliseconds before core pool threads will collapse is idle.
     * @see #isAllowCoreThreadTimeout()
     */
    public int getKeepAliveTime();
    
    /**
     * Get the maximum time to block queue input if queue is full.
     * </p>
     * If the queue is full when a new input is attempted this value is checked
     * to see if the inputing thread should be blocked for some period of time
     * before to allow the queue to clear before throwing an exception.
     * 
     * @return blocking time in milliseconds.
     */
    public int getMaximumBlockingTime();
}
