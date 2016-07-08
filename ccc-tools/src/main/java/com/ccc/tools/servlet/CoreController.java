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
package com.ccc.tools.servlet;

import java.util.Properties;

import com.ccc.tools.TabToLevel;
import com.ccc.tools.app.status.StatusTracker;
import com.ccc.tools.app.status.StatusTrackerProvider;
import com.ccc.tools.executor.BlockingExecutor;
import com.ccc.tools.executor.PropertiesBlockingExecutorConfig;
import com.ccc.tools.executor.PropertiesScheduledExecutorConfig;
import com.ccc.tools.executor.ScheduledExecutor;
import com.ccc.tools.executor.PropertiesBlockingExecutorConfig.ExecutorConfig;
import com.ccc.tools.executor.PropertiesScheduledExecutorConfig.ScheduledExecutorConfig;

@SuppressWarnings("javadoc")
public class CoreController
{
    private volatile StatusTracker statusTracker;
    public volatile BlockingExecutor blockingExecutor;
    public volatile ScheduledExecutor scheduledExecutor;
    
    public void init(Properties properties, TabToLevel format) throws Exception
    {
        statusTracker = new StatusTrackerProvider();
        blockingExecutor = new BlockingExecutor();
        ExecutorConfig beconfig = PropertiesBlockingExecutorConfig.propertiesToConfig(properties, statusTracker, format);
        blockingExecutor.init(beconfig);
        properties.put(OauthServlet.BlockingExcecutorKey, blockingExecutor);
        scheduledExecutor = new ScheduledExecutor();
        ScheduledExecutorConfig seconfig = PropertiesScheduledExecutorConfig.propertiesToConfig(properties, statusTracker, format);
        scheduledExecutor.init(seconfig);
        properties.put(OauthServlet.ScheduledExcecutorKey, scheduledExecutor);
    }
    
    public void destroy()
    {
        if(statusTracker != null)
            statusTracker.destroy();
        if(scheduledExecutor != null)
            scheduledExecutor.shutdownNow();
        if(blockingExecutor != null)
            blockingExecutor.shutdownNow();
    }
}
