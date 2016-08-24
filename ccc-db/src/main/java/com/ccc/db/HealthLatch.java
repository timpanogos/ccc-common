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
package com.ccc.db;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.LoggerFactory;

@SuppressWarnings("javadoc")
public class HealthLatch
{
    private final AtomicBoolean dbUp;
    public HealthLatch()
    {
        dbUp = new AtomicBoolean(false);
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
    
    public boolean isUp()
    {
        return dbUp.get();
    }
}
