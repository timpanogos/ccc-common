package com.ccc.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.LoggerFactory;

@SuppressWarnings("javadoc")
public abstract class BaseDataAccessor implements DataAccessor
{
    private final List<DbEventListener> dbEventListeners;
    private final AtomicBoolean dbUp;
    private volatile ExecutorService executor;

    public BaseDataAccessor()
    {
        dbEventListeners = new ArrayList<DbEventListener>();
        dbUp = new AtomicBoolean(false);
    }
    
    @Override
    public void setExecutor(ExecutorService executor)
    {
        this.executor = executor;
    }
    
    public void registerCommunicationEventListener(DbEventListener listener)
    {
        synchronized (dbEventListeners)
        {
            dbEventListeners.add(listener);
        }
    }

    public void deregisterCommunicationEventListener(DbEventListener listener)
    {
        synchronized (dbEventListeners)
        {
            dbEventListeners.remove(listener);
        }
    }

    public void fireDbEvent(DbEventListener.Type type)
    {
        if (shouldFire(type))
            executor.submit(new FireDbEventTask(type));
    }

    public boolean shouldFire(DbEventListener.Type type)
    {
        switch(type)
        {
            case Up:
                if(dbUp.get())
                    return false;
                dbUp.set(true);
                return true;
            case Down:
                if(!dbUp.get())
                    return false;
                dbUp.set(false);
                return true;
            default:
                LoggerFactory.getLogger(getClass()).warn("Unknown DbEventListener.Type: " + type);
                return true;
        }
    }
    
    private class FireDbEventTask implements Callable<Void>
    {
        private final DbEventListener.Type type;

        private FireDbEventTask(DbEventListener.Type type)
        {
            this.type = type;
        }

        @Override
        public Void call() throws Exception
        {
            try
            {
                synchronized (dbEventListeners)
                {
                    for (DbEventListener listener : dbEventListeners)
                        switch (type)
                        {
                            case Up:
                                listener.dbUp();
                                break;
                            case Down:
                                listener.dbDown();
                                break;
                            default:
                                break;
                        }
                }
            } catch (Exception e)
            {
                LoggerFactory.getLogger(getClass()).warn("An dbEventListener has thrown an exception", e);
            }
            return null;
        }
    }
}

