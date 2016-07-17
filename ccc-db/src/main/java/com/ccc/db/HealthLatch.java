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
