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

import java.util.Properties;

import com.ccc.tools.StrH;
import com.ccc.tools.TabToLevel;
import com.ccc.tools.app.status.StatusTracker;

@SuppressWarnings("javadoc")
public class PropertiesBlockingExecutorConfig
{
    public static final String CorePoolSizeKey = "ccc.tools.executor.core-size";
    public static final String MaximumQueueSizeKey = "ccc.tools.executor.max-queue-size";
    public static final String MaxBlockingTimeKey = "ccc.tools.executor.max-blocking-time";
    public static final String ThreadNamePrefixKey = "ccc.tools.executor.thread-name";
    public static final String KeepAliveDelayKey = "ccc.tools.executor.keep-alive-delay";
    public static final String AllowCoreThreadTimeoutKey = "ccc.tools.executor.core-timeout";
    public static final String StatusNameKey = "ccc.tools.executor.status-name";

    public static final String CorePoolSizeDefault = "128";
    public static final String MaximumQueueSizeDefault = "128";
    public static final String MaxBlockingTimeDefault = "120000";
    public static final String ThreadNamePrefixDefault = "CccBlockingExecutor";
    public static final String KeepAliveDelayDefault = "120000";
    public static final String AllowCoreThreadTimeoutDefault = "true";
    public static final String StatusNameDefault = "CccBlockingExecutor";
    
    public static ExecutorConfig propertiesToConfig(Properties properties, StatusTracker statusTracker, TabToLevel format) throws Exception
    {
        String msg ="ok";
        try
        {
            String value = properties.getProperty(CorePoolSizeKey, CorePoolSizeDefault);
            value = StrH.trim(value);
            format.ttl(CorePoolSizeKey, "=", value);
            msg = "invalid corePoolSize value";
            int corePoolSize = Integer.parseInt(value);
            value = properties.getProperty(MaximumQueueSizeKey, MaximumQueueSizeDefault);
            value = StrH.trim(value);
            format.ttl(MaximumQueueSizeKey, "=", value);
            msg = "invalid maxQueueSize value";
            int maxQueueSize = Integer.parseInt(value);
            value = properties.getProperty(MaxBlockingTimeKey, MaxBlockingTimeDefault);
            value = StrH.trim(value);
            format.ttl(MaxBlockingTimeKey, "=", value);
            msg = "invalid maxBlockingTime value";
            int maxBlockingTime = Integer.parseInt(value);
            String threadName = properties.getProperty(ThreadNamePrefixKey, ThreadNamePrefixDefault);
            threadName = StrH.trim(threadName);
            format.ttl(ThreadNamePrefixKey, "=", value);
            value = properties.getProperty(KeepAliveDelayKey, KeepAliveDelayDefault);
            value = StrH.trim(value);
            format.ttl(KeepAliveDelayKey, "=", value);
            msg = "invalid keepAliveDelay value";
            int keepAliveDelay = Integer.parseInt(value);
            value = properties.getProperty(AllowCoreThreadTimeoutKey, AllowCoreThreadTimeoutDefault);
            value = StrH.trim(value);
            format.ttl(AllowCoreThreadTimeoutKey, "=", value);
            msg = "invalid allowCoreThreadTimeout value";
            boolean allowCoreThreadTimeout = Boolean.parseBoolean(value);
            
            String statusName = properties.getProperty(StatusNameKey, StatusNameDefault);
            statusName = StrH.trim(statusName);
            format.ttl(StatusNameKey, "=", statusName);

            properties.remove(CorePoolSizeKey);
            properties.remove(MaximumQueueSizeKey);
            properties.remove(MaxBlockingTimeKey);
            properties.remove(ThreadNamePrefixKey);
            properties.remove(KeepAliveDelayKey);
            properties.remove(AllowCoreThreadTimeoutKey);
            properties.remove(StatusNameKey);
            
            msg = "BlockingExecutor builder failed";
            return new ExecutorConfig(statusTracker, corePoolSize, maxQueueSize, maxBlockingTime, threadName, keepAliveDelay, allowCoreThreadTimeout, statusName);
        }catch(Exception e)
        {
            format.ttl(msg);
            throw e;
        }
    }
    
    public static class ExecutorConfig implements BlockingExecutorConfig
    {    
        public final StatusTracker statusTracker;
        public final int corePoolSize;
        public final int maxQueueSize;
        public final int maxBlockingTime;
        public final String threadNamePrefix;
        public final int keepAliveDelay;
        public final boolean allowCoreThreadTimeout;
        public final String statusName;

        public ExecutorConfig(
                        StatusTracker statusTracker,
                        int corePoolSize,
                        int maxQueueSize,
                        int maxBlockingTime,
                        String threadNamePrefix,
                        int keepAliveDelay,
                        boolean allowCoreThreadTimeout,
                        String statusName)
        {
            this.statusTracker = statusTracker;
            this.corePoolSize = corePoolSize;
            this.maxQueueSize = maxQueueSize;
            this.maxBlockingTime = maxBlockingTime;
            this.threadNamePrefix = threadNamePrefix;
            this.keepAliveDelay = keepAliveDelay;
            this.allowCoreThreadTimeout = allowCoreThreadTimeout;
            this.statusName = statusName;
        }

        @Override
        public int getCorePoolSize()
        {
            return corePoolSize;
        }

        @Override
        public String getThreadNamePrefix()
        {
            return threadNamePrefix;
        }

        @Override
        public StatusTracker getStatusTracker()
        {
            return statusTracker;
        }

        @Override
        public String getStatusSubsystemName()
        {
            return statusName;
        }

        @Override
        public int getMaximumPoolSize()
        {
            return maxQueueSize;
        }

        @Override
        public boolean isAllowCoreThreadTimeout()
        {
            return allowCoreThreadTimeout;
        }

        @Override
        public int getKeepAliveTime()
        {
            return keepAliveDelay;
        }

        @Override
        public int getMaximumBlockingTime()
        {
            return maxBlockingTime;
        }
    }
}
