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
public class PropertiesScheduledExecutorConfig 
{
    public static final String CorePoolSizeKey = "ccc.tools.scheduled-executor.core-size";
    public static final String ThreadNamePrefixKey = "ccc.tools.scheduled-executor.thread-name";
    public static final String StatusNameKey = "ccc.tools.scheduled-executor.status-name";

    public static final String CorePoolSizeDefault = "6";
    public static final String ThreadNamePrefixDefault = "CccScheduledExecutor";
    public static final String StatusNameDefault = "CccScheduledExecutor";

    public static ScheduledExecutorConfig propertiesToConfig(Properties properties, StatusTracker statusTracker, TabToLevel format) throws Exception
    {
        String msg ="ok";
        try
        {
            String value = properties.getProperty(CorePoolSizeKey, CorePoolSizeDefault);
            value = StrH.trim(value);
            format.ttl(CorePoolSizeKey, "=", value);
            msg = "invalid corePoolSize value";
            int corePoolSize = Integer.parseInt(value);
            String threadName = properties.getProperty(ThreadNamePrefixKey, ThreadNamePrefixDefault);
            threadName = StrH.trim(threadName);
            format.ttl(ThreadNamePrefixKey, "=", value);
            String statusName = properties.getProperty(StatusNameKey, StatusNameDefault);
            statusName = StrH.trim(statusName);
            format.ttl(StatusNameKey, "=", statusName);
            
            properties.remove(CorePoolSizeKey);
            properties.remove(ThreadNamePrefixKey);
            properties.remove(StatusNameKey);
            
            return new ScheduledExecutorConfig(statusTracker, corePoolSize, threadName, statusName);
        }catch(Exception e)
        {
            format.ttl(msg);
            throw e;
        }
    }
    
    public static class ScheduledExecutorConfig implements ExecutorQueueConfig
    {    
        public final int corePoolSize;
        public final String threadNamePrefix;
        public final String statusName;
        public final StatusTracker statusTracker;

        public ScheduledExecutorConfig(StatusTracker statusTracker, int corePoolSize, String threadNamePrefix, String statusName)
        {
            this.statusTracker = statusTracker;
            this.corePoolSize = corePoolSize;
            this.threadNamePrefix = threadNamePrefix;
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
    }
}
