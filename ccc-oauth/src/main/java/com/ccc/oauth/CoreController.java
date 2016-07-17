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
package com.ccc.oauth;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.slf4j.LoggerFactory;

import com.ccc.db.BaseDataAccessor;
import com.ccc.db.DataAccessor;
import com.ccc.oauth.clientInfo.BaseClientInfo;
import com.ccc.oauth.events.AuthEventListener;
import com.ccc.tools.StrH;
import com.ccc.tools.TabToLevel;
import com.ccc.tools.app.status.StatusTracker;
import com.ccc.tools.app.status.StatusTrackerProvider;
import com.ccc.tools.executor.BlockingExecutor;
import com.ccc.tools.executor.PropertiesBlockingExecutorConfig;
import com.ccc.tools.executor.PropertiesBlockingExecutorConfig.ExecutorConfig;
import com.ccc.tools.executor.PropertiesScheduledExecutorConfig;
import com.ccc.tools.executor.PropertiesScheduledExecutorConfig.ScheduledExecutorConfig;
import com.ccc.tools.executor.ScheduledExecutor;


@SuppressWarnings("javadoc")
public class CoreController
{
    public static final String LogFilePathKey = "ccc.tools.log-file-path";
    public static final String LogFileBaseKey = "ccc.tools.log-file-base";
    
    private static CoreController controller;
    private volatile StatusTracker statusTracker;
    public volatile ExecutorService executor;
    public volatile ScheduledExecutor scheduledExecutor;
    
    private final List<AuthEventListener> authenticatedEventListeners; 
    protected volatile Properties properties;
    protected volatile BaseDataAccessor dataAccessor;
    
    public CoreController()
    {
    	authenticatedEventListeners = new ArrayList<AuthEventListener>();
    }
    
    public static CoreController getController()
    {
        return controller;
    }
    
    public Properties getProperties()
    {
        return properties;
    }

    //TODO: decide if throwing exception on duplicates
    public void registerAuthenticatedEventListener(AuthEventListener listener)
    {
    	synchronized(authenticatedEventListeners)
    	{
    		authenticatedEventListeners.add(listener);
    	}
    }
    
    public void deregisterAuthenticatedEventListener(AuthEventListener listener)
    {
    	synchronized(authenticatedEventListeners)
    	{
    		authenticatedEventListeners.remove(listener);
    	}
    }
    
    public void fireAuthenticatedEvent(BaseClientInfo clientInfo, AuthEventListener.Type type)
    {
        executor.submit(new FireAuthenticatedTask(clientInfo, type));
    }
    
    public void init(Properties properties, TabToLevel format) throws Exception
    {
        this.properties = properties;
        statusTracker = new StatusTrackerProvider();
        executor = new BlockingExecutor();
        ExecutorConfig beconfig = PropertiesBlockingExecutorConfig.propertiesToConfig(properties, statusTracker, format);
        ((BlockingExecutor)executor).init(beconfig);
        scheduledExecutor = new ScheduledExecutor();
        ScheduledExecutorConfig seconfig = PropertiesScheduledExecutorConfig.propertiesToConfig(properties, statusTracker, format);
        scheduledExecutor.init(seconfig);
        
        String daImplClass = StrH.getParameter(properties, DataAccessor.DaImplKey, null, format);

        if(daImplClass != null)
        {
            Class<?> clazz = Class.forName(daImplClass);
            dataAccessor = (BaseDataAccessor) clazz.newInstance();
            dataAccessor.init(properties);
        }
        controller = this;
    }
    
    public void destroy()
    {
        if(statusTracker != null)
            statusTracker.destroy();
        if(scheduledExecutor != null)
            scheduledExecutor.shutdownNow();
        if(executor != null)
            executor.shutdownNow();
    }

    private class FireAuthenticatedTask implements Callable<Void>
    {
        private BaseClientInfo clientInfo;
        private final AuthEventListener.Type type;
        
        private FireAuthenticatedTask(BaseClientInfo clientInfo, AuthEventListener.Type type)
        {
            this.clientInfo = clientInfo;
            this.type = type;
        }
        
        @Override
        public Void call() throws Exception
        {
            try
            {
            synchronized(authenticatedEventListeners)
            {
                for(AuthEventListener listener : authenticatedEventListeners)
                {
                    switch(type)
                    {
                    case Authenticated:
                        listener.authenticated(clientInfo);
                        break;
                    case Dropped:
                        listener.dropped(clientInfo);
                        break;
                    case Refreshed:
                        listener.refreshed(clientInfo);
                        break;
                    default:
                        break;
                    }
                }
            }
            }catch(Exception e)
            {
                LoggerFactory.getLogger(getClass()).warn("An authenticatedEventListener has thrown an exception", e);
            }
            return null;
        }
    }
}
