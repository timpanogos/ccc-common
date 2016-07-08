/*
 * Copyright (c) 2010-2015, Panasonic Corporation.
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
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
