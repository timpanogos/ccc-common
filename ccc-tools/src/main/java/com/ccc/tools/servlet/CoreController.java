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

import com.ccc.tools.app.serviceUtility.executor.BlockingExecutor;
import com.ccc.tools.app.serviceUtility.executor.BlockingExecutorConfig;
import com.ccc.tools.app.serviceUtility.status.StatusTracker;

@SuppressWarnings("javadoc")
public class CoreController
{
    BlockingExecutor blockingExecutor;
    
    public void init(Properties properties) throws Exception
    {
        BlockingExecutorConfig config = new BlockingExecutorConfig()
        {
            
            @Override
            public String getThreadNamePrefix()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public StatusTracker getStatusTracker()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getStatusSubsystemName()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getCorePoolSize()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public boolean isAllowCoreThreadTimeout()
            {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public int getMaximumPoolSize()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getMaximumBlockingTime()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getKeepAliveTime()
            {
                // TODO Auto-generated method stub
                return 0;
            }
        };   
    }
    
    public void destroy()
    {
        
    }
}
